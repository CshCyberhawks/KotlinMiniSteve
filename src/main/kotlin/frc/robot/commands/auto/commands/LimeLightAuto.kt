package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto
import frc.robot.util.Gyro

class LimeLightAuto : CommandBase() {
    private val swerveAuto: SwerveAuto = Robot.swerveAuto!!
    private val limelight: Limelight = Robot.limelight!!

    override fun execute() {
        val ballAngle = limelight.getHorizontalOffset()
        swerveAuto.setDesiredAngle(ballAngle + Gyro.getAngle(), false)

        if (swerveAuto.isAtDesiredAngle()) {
            swerveAuto.setDesiredPositionDistance(limelight.getBallDistance())
            swerveAuto.move()
        } else {
            swerveAuto.twist()
        }
    }

    override fun isFinished(): Boolean {
        return swerveAuto.isFinsihedMoving()
    }
}