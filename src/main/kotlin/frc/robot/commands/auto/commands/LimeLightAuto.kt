package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.commands.auto.SwerveAutoInfo
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto
import frc.robot.subsystems.SwerveOdometry

class LimeLightAuto(private val swerveAuto: SwerveAuto, private val limelight: Limelight, private val
swerveAutoInfo: SwerveAutoInfo, private val odometry: SwerveOdometry) :
        CommandBase() {

    override fun initialize() {
        if (limelight.hasTarget()) {
            setDesired()
        }

        swerveAuto.reset()
        swerveAutoInfo.autoMoveRunning = true
    }

    fun setDesired() {
        var pos = limelight.getPosition(odometry)
        swerveAuto.setDesiredPosition(pos)
        swerveAuto.setDesiredAngle(limelight.getHorizontalOffset(), true)

        SmartDashboard.putNumber("limeDes x:", pos.x)
        SmartDashboard.putNumber("limeDes y:", pos.y)
    }

    override fun execute() {
        SmartDashboard.putBoolean("Limelight Has Target", limelight.hasTarget())
        if (limelight.hasTarget()) {
            // setDesired()
        }
        swerveAuto.move()
    }

    override fun end(interrupted: Boolean) {
        swerveAuto.kill()
        swerveAutoInfo.autoMoveRunning = false
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("isLimelightDone", swerveAuto.isFinishedMoving())
        SmartDashboard.putBoolean("isLimelightRunning", !swerveAuto.isFinishedMoving())
        return swerveAuto.isFinishedMoving()
    }
}
