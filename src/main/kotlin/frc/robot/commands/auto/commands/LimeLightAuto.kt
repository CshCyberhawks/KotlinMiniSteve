package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto
import frc.robot.util.MathClass
import frc.robot.util.Vector2

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
        var limelightPos = limelight.getPosition()
        SmartDashboard.putNumber("autoLimeX pre", limelightPos.x)
        SmartDashboard.putNumber("autoLimeY pre", limelightPos.y)
        var posX = limelightPos.x + MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x)
        var posY = limelightPos.y + MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y)
        SmartDashboard.putNumber("autoLimeX", posX)
        SmartDashboard.putNumber("autoLimeY", posY)
        swerveAuto.setDesiredPosition(Vector2(posX, posY))
        val angleLime =
            MathClass.wrapAroundAngles(
                limelight.getHorizontalOffset() + Robot.swo.getPosition().angle
            )
        SmartDashboard.putNumber("autoLimeAngle", angleLime)
        // swerveAuto.setDesiredAngle(angleLime, false)
    }

    override fun execute() {
        SmartDashboard.putBoolean("Limelight Has Target", limelight.hasTarget())
        if (limelight.hasTarget()) {
            // setDesired()
            // swerveAuto.move()
        }
    }

    override fun end(interrupted: Boolean) {
        swerveAuto.kill()
        Robot.autoMoveRunning = false
    }

    override fun isFinished(): Boolean {
        // SmartDashboard.putBoolean("isLimelightDone", swerveAuto.isFinsihedMoving())
        return swerveAuto.isFinishedMoving()
    }
}
