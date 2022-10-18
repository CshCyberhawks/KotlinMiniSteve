package frc.robot.subsystems

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.util.Vector2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan
import kotlin.properties.Delegates

class Limelight(private val cameraHeight: Double, private val ballHeight: Double, private val mountAngle: Double) : SubsystemBase() {
    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")
    private val tv: NetworkTableEntry = table.getEntry("tv") // 0 or 1 whether it has a valid target
    private val tx: NetworkTableEntry = table.getEntry("tx") // The horizontal offset between the crosshair and
    // target in degrees
    private val ty: NetworkTableEntry = table.getEntry("ty") // The vertical offset between the crosshair and target
    // in degrees
    private val ta: NetworkTableEntry = table.getEntry("ta") // Percentage of image
    private val tc: NetworkTableEntry = table.getEntry("tc") // HSV color underneath the crosshair region as a
    // NumberArray
    private val pipeline: NetworkTableEntry = table.getEntry("pipeline") // Pipeline

    private val team: Alliance = DriverStation.getAlliance();
//    .711 Height of camera (meters)
//     0.24 Height of target (meters) measured perfectly
//    40.0 Angle that the limelight is mounted


//    override fun periodic() {
//        // SmartDashboard.putBoolean("Limelight hasValidTarget", hasTarget())
//        // SmartDashboard.putNumber("Limelight horrizontalOffset", getHorizontalOffset())
//        // SmartDashboard.putNumber("Limelight verticalOffset", getVerticalOffset())
//        // SmartDashboard.putNumber("Limelight distance", getBallDistance())
//    }

    fun pipelineInit() {
        if (team == Alliance.Red) {
            pipeline.setDouble(0.0)
        } else if (team == Alliance.Blue) {
            pipeline.setDouble(1.0)
        }
    }

    fun getBallAngleVertical(): Double {
        var offset: Double = tx.getDouble(0.0)
        offset = if (offset < 0) {
            mountAngle + offset
        } else {
            mountAngle + offset
        }
        return offset
    }

    fun getHorizontalOffset(): Double {
        return tx.getDouble(0.0);
    }
    fun getVerticalOffset(): Double {
        return ty.getDouble(0.0)
    }

    fun getArea(): Double {
        return ta.getDouble(0.0)
    }

    fun hasTarget(): Boolean {
        return tv.getDouble(0.0) == 1.0
    }

    fun getColor(): Array<Number> {
        return tc.getNumberArray(arrayOf<Number>(-1))
    }

    fun getBallDistance(): Double {
        SmartDashboard.putNumber("verOff", getVerticalOffset())
        return if (hasTarget()) {
            (cameraHeight - ballHeight) * tan(Math.toRadians(getBallAngleVertical()))
        } else {
            0.0
        }
    }


    fun getPosition(addToDistance: Double): Vector2 {
        val distance: Double = getBallDistance() + addToDistance
        val angle: Double = Math.toRadians(getHorizontalOffset())

        val x: Double = distance * (cos(angle))
        val y: Double = -(distance * (sin(angle)))

        // SmartDashboard.putNumber("limeLightDistance", distance)
        // SmartDashboard.putNumber("limeLightAngle", angle)
        // SmartDashboard.putNumber("limeLightPosX", x)
        // SmartDashboard.putNumber("limeLightPosY", y)

        return Vector2(x, y)
    }

    fun getPosition(): Vector2 {
        val distance: Double = getBallDistance()
        val angle: Double = Math.toRadians(getHorizontalOffset())

        val x: Double = distance * (cos(angle))
        val y: Double = distance * (sin(angle))

        // SmartDashboard.putNumber("limeLightDistance", distance)
        // SmartDashboard.putNumber("limeLightAngle", angle)
        // SmartDashboard.putNumber("limeLightPosX", x)
        // SmartDashboard.putNumber("limeLightPosY", y)

        return Vector2(x, y)
    }
}
