package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.commands.sequences.IntakeSequence
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto


class LimeLightAuto() : CommandBase() {
    var swerveAuto: SwerveAuto = Robot.swerveAuto!!
    var isAtAngle = false
    var isAtPosition = false
    var pickedUpBall = false
    var firstTimeAtAngle = false

    override fun initialize() {
        // Use addRequirements() here to declare subsystem dependencies.
        swerveAuto.setDesiredAngle(Robot.limelight.getHorizontalOffset() + Robot.swo.getPosition().angle, false)
    }
    override fun execute() {
        SmartDashboard.putNumber("limeLightDistance", Robot.limelight.getBallDistance())
        if (!isAtAngle) {
            swerveAuto.twist()
        }
        else if (!isAtPosition && firstTimeAtAngle) {
            swerveAuto.setDesiredPositionDistance(Robot.limelight.getBallDistance())
        } else if (!isAtPosition) {
            swerveAuto.translate()
        }
    }

    override fun end(interrupted: Boolean) {
        swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        if (!isAtAngle) {
            isAtAngle = swerveAuto.isAtDesiredAngle()
            firstTimeAtAngle = isAtAngle
        } else if (!isAtPosition) {
            isAtPosition = swerveAuto.isAtDesiredPosition()
        }

//        pickedUpBall = intakeSequence!!.isFinished()
//        return pickedUpBall
        return isAtPosition
    }
}