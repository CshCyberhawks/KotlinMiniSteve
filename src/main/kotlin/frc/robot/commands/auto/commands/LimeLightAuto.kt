package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.commands.sequences.IntakeSequence
import frc.robot.subsystems.Limelight
import frc.robot.subsystems.SwerveAuto


class LimeLightAuto : CommandBase {
    var swerveAuto: SwerveAuto? = null
    var isAtAngle = false
    var isAtPosition = false
    var pickedUpBall = false
    var firstTimeAtAngle = false

    constructor() {
    }

    override fun initialize() {
        // Use addRequirements() here to declare subsystem dependencies.
        swerveAuto = Robot.swerveAuto
        swerveAuto!!.setDesiredAngle(Limelight.getHorizontalOffset() + Robot.swo!!.getPosition()!!.angle, false)
    }
    override fun execute() {
        SmartDashboard.putNumber("limeLightDistance", Limelight.getBallDistance())
        if (!isAtAngle) {
            swerveAuto!!.twist() 
        }
        else if (isAtAngle && !isAtPosition && firstTimeAtAngle) {
            swerveAuto!!.setDesiredPositionDistance(Limelight.getBallDistance())
        } else if (!isAtPosition && isAtAngle) {
            swerveAuto!!.translate()
        }
    }

    override fun end(interrupted: Boolean) {
        Robot.swerveAuto!!.kill()
    }

    override fun isFinished(): Boolean {
        if (!isAtAngle) {
            isAtAngle = swerveAuto!!.isAtDesiredAngle()
            firstTimeAtAngle = isAtAngle
        } else if (!isAtPosition && isAtAngle) {
            isAtPosition = swerveAuto!!.isAtDesiredPosition()
        }

//        pickedUpBall = intakeSequence!!.isFinished()
//        return pickedUpBall
        return isAtPosition
    }
}