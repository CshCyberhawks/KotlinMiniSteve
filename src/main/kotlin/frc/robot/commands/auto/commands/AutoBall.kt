package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.commands.sequences.IntakeSequence
import frc.robot.util.MathClass
import frc.robot.util.Vector2


class AutoBall : CommandBase {
    private var startTime = 0.0
    private var intakeSequence: IntakeSequence? = null
    private var autoPos: AutoGoToPosition? = null

    constructor(ballNumber: Int) {
        startTime = MathClass.getCurrentTime()
        Robot.swo!!.resetPos()
        // add your autonomous commands below
        // example: below will move robot 2 meters on the x and rotate to 90 degrees
        // then it will wait 1 second before moving the robot back to its starting
        // position
        val desiredPosition: Vector2 = Robot.swerveAuto!!.ballPositions.get(ballNumber)
        val desiredAngle = (180
                - MathClass.cartesianToPolar(
            desiredPosition.x - Robot.swo!!.getPosition()!!.positionCoord!!.x,
            desiredPosition.y - Robot.swo!!.getPosition()!!.positionCoord!!.y
        )!![0])
        // Robot.driveShuffleboardTab.add("desiredAngleAuto", desiredAngle)
        SmartDashboard.putNumber("desiredAngleAuto", desiredAngle)
        intakeSequence = IntakeSequence()
        autoPos = AutoGoToPosition(ballNumber, 0.0)
        // new AutoGoToAngle((desiredAngle + 180) % 360), // desiredAngle),
        // new LimeLightAuto());
    }

    override fun initialize() {
        autoPos!!.schedule()
        intakeSequence!!.schedule()
    }

    override fun isFinished(): Boolean {
        return intakeSequence!!.isFinished() // || MathClass.getCurrentTime() - startTime > 5;
    }
}