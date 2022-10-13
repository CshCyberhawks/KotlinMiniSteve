package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.ScheduleCommand
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.Robot
import frc.robot.commands.sequences.IntakeSequence
import frc.robot.util.MathClass
import frc.robot.util.Vector2


class AutoBall(ballNumber: Int) : CommandBase() {
    val desiredPosition: Vector2 = Robot.swerveAuto!!.ballPositions.get(ballNumber)

    private var startTime = MathClass.getCurrentTime()
    private var intakeSequence: IntakeSequence = IntakeSequence()
    private var autoPos: AutoGoToPosition = AutoGoToPosition(ballNumber, 0.0)
    private var autoLimeLight: LimeLightAuto = LimeLightAuto()
    private var desiredAngle: Double = MathClass.cartesianToPolar(
        desiredPosition.x - Robot.swo.getPosition().positionCoord.x,
        desiredPosition.y - Robot.swo.getPosition().positionCoord.y
    )[0]
    private var autoAngle: AutoGoToAngle = AutoGoToAngle(desiredAngle)
    private var autoAngleScheduled: Boolean = false
    private var autoLimeLightScheduled: Boolean = false

    init {
        Robot.swo.resetPos()
        SmartDashboard.putBoolean("auto2", false)
    }

    override fun initialize() {
        autoPos.schedule()
        intakeSequence.schedule()
    }

    override fun execute() {
        SmartDashboard.putBoolean("aub aua", Robot.swerveAuto!!.isAtDesiredAngle())

        if (autoPos.isFinished && !autoAngleScheduled) {
            CommandScheduler.getInstance().schedule(autoAngle)
            autoAngleScheduled = true
        }
        if (Robot.swerveAuto!!.isAtDesiredAngle() && autoAngleScheduled && !autoLimeLightScheduled) {
            SmartDashboard.putBoolean("auto2", true)
            CommandScheduler.getInstance().schedule(autoLimeLight)
            autoAngle.end(true)
            autoLimeLightScheduled = true
        }
    }
 
    override fun isFinished(): Boolean {
        return intakeSequence.isFinished() // || MathClass.getCurrentTime() - startTime > 5;
    }
}