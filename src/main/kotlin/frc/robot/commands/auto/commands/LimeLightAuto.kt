package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
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
    }

    fun setDesired() {
        var limelightPos = limelight.getPosition(.6)
        var posX = limelightPos.x + Robot.swo.getPosition().positionCoord.x
        var posY = limelightPos.y + Robot.swo.getPosition().positionCoord.y
        // SmartDashboard.putNumber("autoLimeX", posX)
        // SmartDashboard.putNumber("autoLimeY", posY)
        // SmartDashboard.putNumber("autoLimeHori", limelight.getHorizontalOffset())
        swerveAuto.setDesiredPosition(Vector2(posX, posY))
        var angleLime =
                MathClass.wrapAroundAngles(
                        limelight.getHorizontalOffset() + Robot.swo.getPosition().angle
                )
        SmartDashboard.putNumber("autoLimeAngle", angleLime)
        swerveAuto.setDesiredAngle(angleLime, false)
        swerveAuto.setDesiredAngle(0.0, false)
    }

    override fun execute() {
        if (limelight.hasTarget()) {
            // setDesired()
            swerveAuto.move()
        }
    }

    override fun end(interrupted: Boolean) {
        swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        // SmartDashboard.putBoolean("isLimelightDone", swerveAuto.isFinsihedMoving())
        return swerveAuto.isFinsihedMoving()
    }
}
