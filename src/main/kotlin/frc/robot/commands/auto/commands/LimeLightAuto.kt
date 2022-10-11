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
    var intakeSequence: IntakeSequence? = null

    constructor() {
        // Use addRequirements() here to declare subsystem dependencies.
        swerveAuto = Robot.swerveAuto
        swerveAuto!!.setDesiredAngle(Robot.limelight!!.getHorizontalOffset(), true)
    }

    override fun execute() {
        SmartDashboard.putNumber("limeLightDistance", Robot.limelight!!.getBallDistance())
        if (!isAtAngle) swerveAuto!!.twist() else if (isAtAngle && !isAtPosition && firstTimeAtAngle) {
            intakeSequence = IntakeSequence()
            swerveAuto!!.setDesiredPositionDistance(Robot.limelight!!.getBallDistance())
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
            firstTimeAtAngle = if (isAtAngle) true else false
        } else if (isAtAngle && !isAtPosition) isAtPosition = swerveAuto!!.isAtDesiredPosition()
        pickedUpBall = intakeSequence!!.isFinished()
        return pickedUpBall
    }
}