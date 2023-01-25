package frc.robot.subsystems

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.util.MathClass
import frc.robot.util.Polar
import frc.robot.util.Vector2
import kotlin.math.tan

class Limelight(private val name: String = "limelight", private val cameraHeight: Double, private val ballHeight:
Double, private
val
mountAngle: Double) :
        SubsystemBase() {
    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable(name)
    private val hasTarget: NetworkTableEntry = table.getEntry("tv") // 0 or 1 whether it has a valid target
    private val horizontalOffset: NetworkTableEntry = table.getEntry("tx") // The horizontal offset between the

    // crosshair and target in degrees
    private val verticalOffset: NetworkTableEntry = table.getEntry("ty") // The vertical offset between the

    // crosshair and target in degrees
    private val area: NetworkTableEntry = table.getEntry("ta") // Percentage of image
    private val detectedColor: NetworkTableEntry = table.getEntry("tc") // HSV color underneath the crosshair

    // region as a NumberArray
    private val pipeline: NetworkTableEntry = table.getEntry("pipeline") // Pipeline

    private val team: Alliance = DriverStation.getAlliance()
//    0.711 Height of camera (meters)
//    0.24 Height of target (meters) measured perfectly
//    40.0 Angle that the limelight is mounted


//    override fun periodic() {
//        // SmartDashboard.putBoolean("Limelight hasValidTarget", hasTarget())
//        // SmartDashboard.putNumber("Limelight horrizontalOffset", getHorizontalOffset())
//        // SmartDashboard.putNumber("Limelight verticalOffset", getVerticalOffset())
//        // SmartDashboard.putNumber("Limelight distance", getBallDistance())
//    }

    fun pipelineInit() {
        when (team) {
            Alliance.Red -> pipeline.setDouble(0.0)
            Alliance.Blue -> pipeline.setDouble(1.0)
            Alliance.Invalid -> error("INVALID ALLIANCE")
        }
    }

    fun getBallAngleVertical(): Double {
        SmartDashboard.putNumber("rawLimeLightVer", verticalOffset.getDouble(0.0))
        return verticalOffset.getDouble(0.0) + mountAngle
    }

    fun getHorizontalOffset(): Double {
        return horizontalOffset.getDouble(0.0)
    }

    fun getVerticalOffset(): Double {
        return verticalOffset.getDouble(0.0)
    }

    fun getArea(): Double {
        return area.getDouble(0.0)
    }

    fun hasTarget(): Boolean {
        return hasTarget.getDouble(0.0) == 1.0
    }

    fun getColor(): Array<Number> {
        return detectedColor.getNumberArray(arrayOf<Number>(-1))
    }

    fun getBallDistance(): Double {
        // SmartDashboard.putNumber("verOff", getVerticalOffset())

        return if (hasTarget()) {
            (cameraHeight - ballHeight) * tan(Math.toRadians(getBallAngleVertical()))
        } else {
            0.0
        }
    }

    fun getPositionAndAddRobot(distance: Double, angle: Double, robotAngle: Double, robot: Vector2): Vector2 {
        return MathClass.polarToCartesian(Polar(angle + robotAngle, distance)) + robot
    }

    fun getPosition(distance: Double, angle: Double, robotAngle: Double): Vector2 {
        return MathClass.polarToCartesian(Polar(angle + robotAngle, distance))
    }


    fun getPosAndAdd(odometry: SwerveOdometry, dist: Double): Vector2 {
        val distance: Double = getBallDistance() + dist
        val angle: Double = getHorizontalOffset() + odometry.getPosition().angle

        var ret = MathClass.polarToCartesian(Polar(angle, distance))
        ret += Vector2(MathClass.swosToMeters(odometry.getPosition().positionCoord.x), MathClass.swosToMeters(odometry
                .getPosition().positionCoord.x))


        SmartDashboard.putNumber("limeLightDistance", distance)
        SmartDashboard.putNumber("limeLightAngle", getHorizontalOffset())

        return ret
    }

    fun getPosition(odometry: SwerveOdometry): Vector2 {
        val distance: Double = getBallDistance()  //.639
        val angle: Double = MathClass.wrapAroundAngles(getHorizontalOffset() + odometry.getPosition().angle) // 357

        var ret = MathClass.polarToCartesian(Polar(angle, distance))
        ret += Vector2(MathClass.swosToMeters(odometry.getPosition().positionCoord.x), MathClass.swosToMeters(odometry
                .getPosition().positionCoord.y))

        SmartDashboard.putNumber("limeLightDistance", distance)
        SmartDashboard.putNumber("limeLightAngle", getHorizontalOffset())

        return ret
    }
}
