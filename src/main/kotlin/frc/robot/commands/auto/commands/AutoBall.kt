package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.Robot
import frc.robot.commands.sequences.IntakeSequence
import frc.robot.util.MathClass
import frc.robot.util.Vector2


class AutoBall(ballNumber: Int) : CommandBase() {
    // add your autonomous commands below
    // example: below will move robot 2 meters on the x and rotate to 90 degrees
    // then it will wait 1 second before moving the robot back to its starting
    // position
    // Robot.driveShuffleboardTab.add("desiredAngleAuto", desiredAngle)
    // SmartDashboard.putNumber("desiredAngleAuto", desiredAngle)
    private var startTime = 0.0
    private var intakeSequence: IntakeSequence = IntakeSequence()
    private var desiredAngle: Double = 0.0
    private var autoMove: AutoGoToPositionAndAngle
    // private var autoLimeLight: LimeLightAuto
    // private var limeLightScheduled: Boolean = false;

    init {
        startTime = MathClass.getCurrentTime()
        Robot.swo.resetPos()
        // add your autonomous commands below
        // example: below will move robot 2 meters on the x and rotate to 90 degrees
        // then it will wait 1 second before moving the robot back to its starting
        // position
        val desiredPosition: Vector2 = Robot.swerveAuto.ballPositions.get(ballNumber)
        desiredAngle =  MathClass.cartesianToPolar(Vector2(
            desiredPosition.x - Robot.swo.getPosition().positionCoord.x,
            desiredPosition.y - Robot.swo.getPosition().positionCoord.y)
        ).theta
        // Robot.driveShuffleboardTab.add("desiredAngleAuto", desiredAngle)
        // SmartDashboard.putNumber("desiredAngleAuto", desiredAngle)
        autoMove = AutoGoToPositionAndAngle(ballNumber, 0.0, 0.0)
        // autoLimeLight = LimeLightAuto()
    }

    override fun initialize() {
        CommandScheduler.getInstance().schedule(autoMove)
        intakeSequence.schedule()
    }

    override fun execute() {
        // if (Robot.swerveAuto.isAtDesiredAngle() && autoAngleScheduled && autoLimeLightScheduled == false) {
        //     SmartDashboard.putBoolean("auto2", true)
        //     // CommandScheduler.getInstance().schedule(autoLimeLight)
        //     autoAngle?.end(true)
        //     autoLimeLightScheduled = true
        // }

        // if (Robot.swerveAuto.isFinishedMoving() && !limeLightScheduled) {
        //     CommandScheduler.getInstance().schedule(autoLimeLight)
        //     limeLightScheduled = true;
        // }
    }

    override fun end(interrupted: Boolean) {
        // autoLimeLight.cancel()
    }


    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("autoBallDone", intakeSequence.autoIntakeCommand.isFinished)
        return intakeSequence.autoIntakeCommand.isFinished // || MathClass.getCurrentTime() - startTime > 5;
    }
}
