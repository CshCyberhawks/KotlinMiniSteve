package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto
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
        var limelightPos = limelight.getPosition(2.0)
        var posX = limelightPos.x + Robot.swo.getPosition().positionCoord.x
        var posY = limelightPos.y + Robot.swo.getPosition().positionCoord.y
        SmartDashboard.putNumber("autoLimeX", posX)
        SmartDashboard.putNumber("autoLimeY", posY)
        swerveAuto.setDesiredPosition(Vector2(posX, posY))
        // swerveAuto.setDesiredAngle(
        //         limelight.getHorizontalOffset() + Robot.swo.getPosition().angle,
        //         false
        // )
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
