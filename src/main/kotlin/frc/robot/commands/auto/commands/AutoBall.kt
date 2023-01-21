package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.Robot
import frc.robot.util.FieldPosition
import frc.robot.util.MathClass
import frc.robot.util.Vector2
import frc.robot.commands.sequences.IntakeSequence

class AutoBall : CommandBase {
    // add your autonomous commands below
    // example: below will move robot 2 meters on the x and rotate to 90 degrees
    // then it will wait 1 second before moving the robot back to its starting
    // position
    // Robot.driveShuffleboardTab.add("desiredAngleAuto", desiredAngle)
    // SmartDashboard.putNumber("desiredAngleAuto", desiredAngle)
    private var intakeSequence: IntakeSequence = IntakeSequence()
    private var desiredAngle: Double = 0.0
    private var autoMove: AutoGoToPositionAndAngle
    private var autoLimeLight: LimeLightAuto
    private var scheduledP2 = false

    constructor(ballNumber: Int) : super() {
        // add your autonomous commands below
        // example: below will move robot 2 meters on the x and rotate to 90 degrees
        // then it will wait 1 second before moving the robot back to its starting
        // position
        val desiredPosition: Vector2 = Robot.swerveAuto.ballPositions[ballNumber]
        desiredAngle = MathClass.cartesianToPolar(
                Vector2(
                        desiredPosition.x - MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x),
                        -(desiredPosition.y - MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y))
                )
        ).theta
        SmartDashboard.putNumber("desiredAngle: ", desiredAngle)
        autoMove = AutoGoToPositionAndAngle(ballNumber, MathClass.wrapAroundAngles(desiredAngle), 0.0)
    }

    constructor(ballNumber: Int, angle: Double) {
        autoMove = AutoGoToPositionAndAngle(ballNumber, angle, 0.0)
    }

    constructor(position: FieldPosition) : super() {
        autoMove = AutoGoToPositionAndAngle(position, 0.0)
    }

    init {
        autoLimeLight = LimeLightAuto();
        intakeSequence = IntakeSequence(autoLimeLight);
    }

    override fun initialize() {
        autoMove.schedule();
    }

    override fun execute() {
        if (!autoMove.isScheduled() && !scheduledP2) {
            autoMove.cancel();
            autoLimeLight.schedule();
            intakeSequence.schedule();
            scheduledP2 = true
        }
    }

    override fun end(interrupted: Boolean) {
        autoLimeLight.cancel()
    }


   override fun isFinished(): Boolean {
       SmartDashboard.putBoolean("autoBallDone", intakeSequence.autoIntakeCommand.isFinished)
       return intakeSequence.isFinished // || MathClass.getCurrentTime() - startTime > 5
   }
}
