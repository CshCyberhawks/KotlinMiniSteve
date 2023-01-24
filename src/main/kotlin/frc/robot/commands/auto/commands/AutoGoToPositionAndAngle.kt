package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.SwerveAuto
import frc.robot.util.FieldPosition
import frc.robot.util.Vector2


class AutoGoToPositionAndAngle : CommandBase {
    private var desiredPosition: Vector2 = Vector2(0.0, 0.0)
    private var desiredVelocity = 0.0
    private var desiredAngle = 0.0
    private var ballNumber = 0
    private var byBallNumber = false
    lateinit var swerveAuto: SwerveAuto

    constructor(swerveAuto: SwerveAuto, desiredPosition: Vector2, desiredAngle: Double, desiredVelocity:
    Double) :
            super
    () {
        this.swerveAuto = swerveAuto
        this.desiredPosition = desiredPosition
        this.desiredVelocity = desiredVelocity
        this.desiredAngle = desiredAngle
    }

    constructor(swerveAuto: SwerveAuto, fieldPos: FieldPosition, desiredVelocity: Double) : super() {
        this.swerveAuto = swerveAuto
        this.desiredPosition = fieldPos.positionCoord
        this.desiredAngle = fieldPos.angle
        this.desiredVelocity = desiredVelocity
        byBallNumber = false
    }

    constructor(swerveAuto: SwerveAuto, ballNumber: Int, desiredAngle: Double, desiredVelocity: Double) : super() {
        this.swerveAuto = swerveAuto
        this.ballNumber = ballNumber
        this.desiredVelocity = desiredVelocity
        this.desiredAngle = desiredAngle
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
            // SmartDashboard.putNumber("desiredPos x:", desiredPosition.x)
            // SmartDashboard.putNumber("desiredPos y: ", desiredPosition.y)
            // System.out.println("setting desired pos to: " + desiredPosition.x + "," + desiredPosition.y)
            swerveAuto.setDesiredPosition(desiredPosition) // , desiredVelocity)
        } else {
            swerveAuto.setDesiredPositionBall(ballNumber) // , desiredVelocity)
        }
        swerveAuto.setDesiredAngle(desiredAngle, false)
    }

    override fun execute() {
        swerveAuto.move()
    }

    override fun end(interrupted: Boolean) {
        // commented below code out so that robot will maintain desired autonomous
        // velocities
        swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putBoolean("moveCmdFin", swerveAuto.isFinishedMoving())
        return swerveAuto.isFinishedMoving() // || MathClass.getCurrentTime() - startTime > 5
    }
}
