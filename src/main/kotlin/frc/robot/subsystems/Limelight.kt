package frc.robot.subsystems

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.util.Vector2

class Limelight() : SubsystemBase() {
    private val table: NetworkTable = NetworkTableInstance.getDefault().getTable("limelight")
    private val tv: NetworkTableEntry = table.getEntry("tv") // 0 or 1 whether it has a valid target

    private val tx: NetworkTableEntry =
            table.getEntry("tx") // The horizontal offset between the crosshair and

    // target in degrees
    private val ty: NetworkTableEntry =
            table.getEntry("ty") // The vertical offset between the crosshair and target

    // in degrees
    private val ta: NetworkTableEntry = table.getEntry("ta") // Percentage of image

    private val tc: NetworkTableEntry =
            table.getEntry("tc") // HSV color underneath the crosshair region as a

    // NumberArray
    private val pipeline: NetworkTableEntry = table.getEntry("pipeline") // Pipeline

    private val team: DriverStation.Alliance = DriverStation.getAlliance()
    val cameraHeight = .711 // Height of camera (meters)
    val ballHeight = 0.24 // Height of target (meters) measured perfectly
    val mountAngle = 40.0 // Angle that the limelight is mounted

    override fun periodic() {
        // SmartDashboard.putBoolean("Limelight hasValidTarget", hasTarget())
        // SmartDashboard.putNumber("Limelight horrizontalOffset", getHorizontalOffset())
        // SmartDashboard.putNumber("Limelight verticalOffset", getVerticalOffset())
        // SmartDashboard.putNumber("Limelight distance", getBallDistance())
    }

    fun pipelineInit() {
        if (team == Alliance.Red) {
            pipeline.setDouble(0.0)
        } else if (team == Alliance.Blue) {
            pipeline.setDouble(1.0)
        }
    }

    fun getBallAngleVertical(): Double {
        var offset: Double = tx.getDouble(0.0)
        if (offset < 0) {
            offset = mountAngle + offset
        } else {
            offset = mountAngle + offset
        }
        return offset
    }

    fun getHorizontalOffset(): Double {
        var offset: Double = tx.getDouble(0.0)

        return offset
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
        if (hasTarget()) {
            return (cameraHeight - ballHeight) * Math.tan(Math.toRadians(getBallAngleVertical()))
        } else {
            return 0.0
        }
    }


    fun getPosition(addToDistance: Double): Vector2 {
        var distance: Double = getBallDistance() + addToDistance
        var angle: Double = Math.toRadians(getHorizontalOffset())

        var x: Double = distance * (Math.cos(angle))
        var y: Double = -(distance * (Math.sin(angle)))

        // SmartDashboard.putNumber("limeLightDistance", distance)
        // SmartDashboard.putNumber("limeLightAngle", angle)
        // SmartDashboard.putNumber("limeLightPosX", x)
        // SmartDashboard.putNumber("limeLightPosY", y)

        return Vector2(x, y)
    }

    fun getPosition(): Vector2 {
        var distance: Double = getBallDistance()
        var angle: Double = Math.toRadians(getHorizontalOffset())

        var x: Double = distance * (Math.cos(angle))
        var y: Double = distance * (Math.sin(angle))

        // SmartDashboard.putNumber("limeLightDistance", distance)
        // SmartDashboard.putNumber("limeLightAngle", angle)
        // SmartDashboard.putNumber("limeLightPosX", x)
        // SmartDashboard.putNumber("limeLightPosY", y)

        return Vector2(x, y)
    }
}
