package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.util.MathClass
import frc.robot.util.Vector2


class AutoGoToPosition : CommandBase {
    private var desiredPosition: Vector2? = null
    private var desiredVelocity = 0.0
    private var ballNumber = 0
    private var byBallNumber = false
    private var startTime = 0.0

    constructor(desiredPosition: Vector2?, desiredVelocity: Double) {
        this.desiredPosition = desiredPosition
        this.desiredVelocity = desiredVelocity
        startTime = MathClass.getCurrentTime()
    }

    constructor(ballNumber: Int, desiredVelocity: Double) {
        this.ballNumber = ballNumber
        this.desiredVelocity = desiredVelocity
        byBallNumber = true
    }

    // this command will move the robot to the desired position x, y and twist
    // values
    // it does this by first driving full speed in the direction of the desired x
    // and y position, and then it will stop and twist until it reaches desired
    // angle

    // this command will move the robot to the desired position x, y and twist
    // values
    // it does this by first driving full speed in the direction of the desired x
    // and y position, and then it will stop and twist until it reaches desired
    // angle
    override fun initialize() {
        // desired position = x in swos
        // (https://www.notion.so/Odometry-baacd114086e4218a5eedb5ef45a223f) (.27
        // meters), y in swos, and twist in degrees
        // (based on
        // robot staring position)
        if (!byBallNumber) {
            Robot.swerveAuto!!.setDesiredPosition(desiredPosition!!) // , desiredVelocity);
        } else {
            Robot.swerveAuto!!.setDesiredPositionBall(ballNumber) // , desiredVelocity);
        }
    }

    override fun execute() {
        Robot.swerveAuto!!.translate()
    }

    override fun end(interrupted: Boolean) {
        // commented below code out so that robot will maintain desired autonomous
        // velocities
        Robot.swerveAuto!!.kill()
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("auto translate command finsihed", Robot.swerveAuto!!.isAtDesiredPosition())
        return Robot.swerveAuto!!.isAtDesiredPosition() // || MathClass.getCurrentTime() - startTime > 5;
    }
}