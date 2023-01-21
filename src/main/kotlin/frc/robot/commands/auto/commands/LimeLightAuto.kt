package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto

class LimeLightAuto : CommandBase() {
    private val swerveAuto: SwerveAuto = Robot.swerveAuto
    private val limelight: Limelight = Robot.limelight

    override fun initialize() {
        if (limelight.hasTarget()) {
            setDesired()
        }

        Robot.swerveAuto.reset()
        Robot.autoMoveRunning = true
    }

    fun setDesired() {
        var pos = limelight.getPosition()
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
        Robot.autoMoveRunning = false
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("isLimelightDone", swerveAuto.isFinishedMoving())
        SmartDashboard.putBoolean("isLimelightRunning", !swerveAuto.isFinishedMoving())
        return swerveAuto.isFinishedMoving()
    }
}
