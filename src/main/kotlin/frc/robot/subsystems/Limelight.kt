package frc.robot.subsystems

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase


class Limelight : SubsystemBase() {
    override fun periodic() {
        // SmartDashboard.putBoolean("Limelight hasValidTarget", hasTarget())
        // SmartDashboard.putNumber("Limelight horrizontalOffset", getHorizontalOffset())
        // SmartDashboard.putNumber("Limelight verticalOffset", getVerticalOffset())
        // SmartDashboard.putNumber("Limelight distance", getBallDistance())
    }

    companion object {
        private val table = NetworkTableInstance.getDefault().getTable("limelight")
        private val tv = table.getEntry("tv") // 0 or 1 whether it has a valid target

        private val tx = table.getEntry("tx") // The horizontal offset between the crosshair and

        // target in degrees
        private val ty = table.getEntry("ty") // The vertical offset between the crosshair and target

        // in degrees
        private val ta = table.getEntry("ta") // Percentage of image

        private val tc = table.getEntry("tc") // HSV color underneath the crosshair region as a

        // NumberArray
        private val pipeline = table.getEntry("pipeline") // Pipeline


        private val team = DriverStation.getAlliance()

        fun pipelineInit() {
            if (team == Alliance.Red) {
                pipeline.setDouble(0.0)
            } else if (team == Alliance.Blue) {
                pipeline.setDouble(1.0)
            }
        }

        fun getHorizontalOffset(): Double {
            return tx.getDouble(0.0)
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

        fun getColor(): Array<Number?>? {
            return tc.getNumberArray(arrayOf<Number>(-1))
        }

        fun getBallDistance(): Double {
            val cameraHeight = .711 // Height of camera (meters)
            val ballHeight = 0.24 // Height of target (meters) measured perfectly
            val mountAngle = 23.0 // Angle that the limelight is mounted
            return (ballHeight - cameraHeight) / Math.tan(mountAngle + getVerticalOffset())
        }
    }
}