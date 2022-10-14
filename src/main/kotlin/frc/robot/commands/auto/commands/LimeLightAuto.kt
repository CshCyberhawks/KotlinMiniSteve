package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto
import frc.robot.util.Gyro

class LimeLightAuto : CommandBase() {
    private val swerveAuto: SwerveAuto = Robot.swerveAuto
    private val limelight: Limelight = Robot.limelight

    override fun initialize() {
        val ballAngle = limelight.getHorizontalOffset()
        swerveAuto.setDesiredAngle(ballAngle + Gyro.getAngle(), false)
        SmartDashboard.putNumber("limelight distance: ", limelight.getBallDistance())
        swerveAuto.setDesiredPositionDistance(limelight.getBallDistance())
    }

    override fun execute() {
        val ballAngle = limelight.getHorizontalOffset()
        swerveAuto.setDesiredAngle(ballAngle + Gyro.getAngle(), false)
        SmartDashboard.putNumber("limelight distance: ", limelight.getBallDistance())
        swerveAuto.setDesiredPositionDistance(limelight.getBallDistance())
        swerveAuto.move()
    }

    override fun end(interrupted: Boolean) {
        swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        return swerveAuto.isFinsihedMoving()
    }
}

