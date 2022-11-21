package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.util.Coordinate
import frc.robot.util.MathClass

class AutoGoToPosition : CommandBase {
    private var desiredPosition: Coordinate = Coordinate(0.0, 0.0)
    private var desiredVelocity = 0.0
    private var ballNumber = 0
    private var byBallNumber = false
    private var startTime = 0.0

    constructor(desiredPosition: Coordinate, desiredVelocity: Double) : super() {
        this.desiredPosition = desiredPosition
        this.desiredVelocity = desiredVelocity
    }

    constructor(ballNumber: Int, desiredVelocity: Double) : super() {
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
        startTime = MathClass.getCurrentTime()
        // desired position = x in swos
        // (https://www.notion.so/Odometry-baacd114086e4218a5eedb5ef45a223f) (.27
        // meters), y in swos, and twist in degrees
        // (based on
        // robot staring position)
        if (!byBallNumber) {
            Robot.swerveAuto.setDesiredPosition(desiredPosition) // , desiredVelocity)
        } else {
            Robot.swerveAuto.setDesiredPositionBall(ballNumber) // , desiredVelocity)
        }
    }

    override fun execute() {
        Robot.swerveAuto.translate()
    }

    override fun end(interrupted: Boolean) {
        // commented below code out so that robot will maintain desired autonomous
        // velocities
        Robot.swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("posCmdFin", Robot.swerveAuto.isAtDesiredPosition())
        return Robot.swerveAuto.isAtDesiredPosition() // || MathClass.getCurrentTime() - startTime > 5
    }
}
