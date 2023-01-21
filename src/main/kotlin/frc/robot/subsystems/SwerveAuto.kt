package frc.robot.subsystems

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.util.*

class SwerveAuto {
    private var desiredPosition: Vector2 = Vector2(0.0, 0.0)
    private var desiredAngle = 0.0

    private var team: Alliance = DriverStation.getAlliance()
    var ballPositions: Array<Vector2>

    private var byBall = false
    private val ballDistanceDeadzone = 0.1
    private val normalDistanceDeadzone = 0.1

    private val positionStopRange = 0.1
    private val isAtPosition = false
    private val isAtAngle = false

    private var trapXFinished = false
    private var trapYFinsihed = false

    private var angleDeadzone: Double = 4.0

    // max accel = 1.71m/s^2
    // max velo actual: 4.0m/s
    private val trapConstraints = TrapezoidProfile.Constraints(4.0, 1.5)
    private var trapXCurrentState: TrapezoidProfile.State =
            TrapezoidProfile.State(
                    Robot.swo.getPosition().positionCoord.x,
                    Robot.swo.getVelocities()[0]
            )
    private var trapXDesiredState: TrapezoidProfile.State =
            TrapezoidProfile.State(desiredPosition.x, 0.0)
    private var trapYCurrentState: TrapezoidProfile.State =
            TrapezoidProfile.State(
                    Robot.swo.getPosition().positionCoord.y,
                    Robot.swo.getVelocities()[1]
            )
    private var trapYDesiredState: TrapezoidProfile.State =
            TrapezoidProfile.State(desiredPosition.y, 0.0)

    // TODO: prob need to increase derivatives
    private val xPID = PIDController(1.0, 0.0, 2.0)
    private val yPID = PIDController(1.0, 0.0, 2.0)

    private val twistPID = PIDController(6.0, 0.0, .8)

    private var prevTime = 0.0

    val startingPos: Int

    constructor(startingPos: Int) {
        if (startingPos == 0) {
            ballPositions = Constants.ballPositionsZero
        } else if (startingPos == 1) {
            // ballPositions = Constants.ballPositionsOne
        }
        this.startingPos = startingPos

        ballPositions = Constants.ballPositionsZero
    }

    init {
        twistPID.enableContinuousInput(0.0, 360.0)
    }

    fun setDesiredPosition(desiredPosition: Vector2) { // , double desiredVelocity) {
        byBall = false
        this.desiredPosition = desiredPosition
        // SmartDashboard.putNumber("Desired pos input", desiredPosition.x)
        // val polarPosition = MathClass.cartesianToPolar(desiredPosition.x, desiredPosition.y)
        // double[] desiredVelocities = MathClass.polarToCartesian(polarPosition[0],
        // desiredVelocity)

        trapXDesiredState =
                TrapezoidProfile.State(this.desiredPosition.x, 0.0) // desiredVelocities[0])
        trapYDesiredState =
                TrapezoidProfile.State(this.desiredPosition.y, 0.0) // desiredVelocities[0])
    }

    fun setDesiredPositionBall(ballNumber: Int) { // , double desiredVelocity) {
        // SmartDashboard.putNumber("desired pos from ball fun", ballPositions[ballNumber].x)
        setDesiredPosition(ballPositions[ballNumber]) // , desiredVelocity)
        byBall = true
    }

    fun setDesiredPositionDistance(distance: Double, limeLightAngle: Double) {
        val pos = Robot.swo.getPosition()
        val desiredPositionCart =
                MathClass.polarToCartesian(Polar(pos.angle + limeLightAngle, distance))
        setDesiredPosition(
                Vector2(
                        desiredPositionCart.x + pos.positionCoord.x,
                        desiredPositionCart.y + pos.positionCoord.y
                )
        ) // , 0)
    }

    fun setDesiredAngle(angle: Double, robotRelative: Boolean) {
        // TODO: Make robot relative not wrong
        desiredAngle =
                if (robotRelative) {
                    MathClass.wrapAroundAngles(angle + Gyro.getAngle())
                } else {
                    angle
                }
    }

    fun setDesiredDistance(x: Double, y: Double) {}

    fun isAtDesiredPosition(): Boolean {
        // SmartDashboard.putNumber("Desired position distance x", desiredPosition.x -
        // MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x))
        val deadzone = if (byBall) ballDistanceDeadzone else normalDistanceDeadzone
        return (MathClass.calculateDeadzone(
                desiredPosition.x - MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x),
                deadzone
        ) == 0.0 &&
                MathClass.calculateDeadzone(
                        desiredPosition.y -
                                MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y),
                        deadzone
                ) == 0.0)
    }

    fun isAtDesiredAngle(): Boolean {
        return MathClass.calculateDeadzone(
                MathClass.wrapAroundAngles(Robot.swo.getPosition().angle) -
                        MathClass.wrapAroundAngles(desiredAngle),
                angleDeadzone
        ) == 0.0
    }

    fun isFinishedMoving(): Boolean {
        return isAtDesiredAngle() && isAtDesiredPosition()
    }

    // twists and translates
    fun move() {
        var translation: Vector2 = Vector2(0.0, 0.0)
        var twist: Double = 0.0

        if (!isAtDesiredPosition()) {
            translation = calculateTranslation()
        }

        if (!isAtDesiredAngle()) {
            twist = calculateTwist(desiredAngle)
        }

        Robot.swerveSystem.drive(translation.x, translation.y, twist, 0.0, DriveState.AUTO, true)
    }

    fun calculateTranslation(): Vector2 {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val trapTime: Double = if (prevTime == 0.0) 0.0 else timeNow - prevTime
        val trapXProfile = TrapezoidProfile(trapConstraints, trapXDesiredState, trapXCurrentState)
        val trapYProfile = TrapezoidProfile(trapConstraints, trapYDesiredState, trapYCurrentState)
        trapXFinished = trapXProfile.isFinished(trapTime)
        trapYFinsihed = trapYProfile.isFinished(trapTime)
        val trapXOutput = trapXProfile.calculate(trapTime)
        val trapYOutput = trapYProfile.calculate(trapTime)
        // SmartDashboard.putNumber("TrapY", trapYOutput.position)
        // SmartDashboard.putNumber("TrapX", trapXOutput.position)
        val xPIDOutput =
                xPID.calculate(
                        MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x),
                        trapXOutput.position
                )
        val yPIDOutput =
                yPID.calculate(
                        MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y),
                        trapYOutput.position
                )
        SmartDashboard.putNumber("xPID", xPIDOutput)
        SmartDashboard.putNumber("yPID", yPIDOutput)
        val xVel = (trapXOutput.velocity + xPIDOutput)
        val yVel = (trapYOutput.velocity + yPIDOutput)
        // val xVel = xPIDOutput
        // val yVel = yPIDOutput
        SmartDashboard.putNumber("xDriveInput", xVel / 4.0)
        SmartDashboard.putNumber("yDriveInput", yVel / 4.0)
        trapXCurrentState = trapXOutput
        trapYCurrentState = trapYOutput
        prevTime = timeNow
        return Vector2(xVel / 4.0, yVel / 4.0)
    }

    fun calculateTwist(desiredAngle: Double): Double {
        // SmartDashboard.putNumber("desiredTwistAngle", desiredAngle)
        val currentAngle: Double = Robot.swo.getPosition().angle

        // val angleChange =
        //     MathClass.wrapAroundAngles(
        //         MathClass.wrapAroundAngles(desiredAngle) -
        //                 MathClass.wrapAroundAngles(currentAngle)
        //     )
        //
        // val targetVal =
        //     MathUtil.clamp(
        //         if (angleChange > 180) {
        //             angleChange
        //         } else {
        //             -angleChange
        //         },
        //         -1.0,
        //         1.0
        //     )
        //
        // //        val targetVal = -MathUtil.clamp(
        // //            MathClass.wrapAroundAngles(desiredAngle - currentAngle), -1.0, 1.0
        // //        )
        //
        // // val twistValue: Double = desiredAngle, Robot.swo.getPosition().angle
        // val twistFeedForward = (targetVal / 5)
        // // NOTE: divide by 360 is to go from angle to percent output + divide by 10 is to lower it
        // val twistPIDOutput =
        //     -(twistPID.calculate(
        //         currentAngle,
        //         MathClass.wrapAroundAngles(currentAngle + angleChange)
        //     ) / 360)
        //
        // // SmartDashboard.putNumber("twistFeed", twistFeedForward)
        // //
        // // if (Math.abs(desiredAngle - currentAngle) > 90 &&
        // //                 Math.abs(desiredAngle - currentAngle) < 270
        // // ) {
        // //     // desiredTurnPosition = (desiredAngle + 180) % 360
        // //     // targetVal *= -1
        // //     twistPIDOutput *= -1
        // // }
        // val twistVal: Double = MathUtil.clamp(twistFeedForward + twistPIDOutput, -1.0, 1.0)
        // SmartDashboard.putNumber("auto twistVal", twistVal)
        val twistVal = twistPID.calculate(currentAngle, desiredAngle) / 360
        SmartDashboard.putNumber("auto twist PID", twistVal)

        return twistVal / 3
    }

    fun reset() {
        this.trapXCurrentState = TrapezoidProfile.State()
        this.trapYCurrentState = TrapezoidProfile.State()
    }

    fun translate() {
        val driveInputs: Vector2 = calculateTranslation()
        Robot.swerveSystem.drive(driveInputs.x, driveInputs.y, 0.0, 0.0, DriveState.AUTO, true)
    }

    fun twist() {
        // val twistValue: Double = MathUtil.clamp(Robot.swo.getPosition().angle - desiredAngle,
        // -1.0, 1.0)
        val twistInput = calculateTwist(desiredAngle)
        Robot.swerveSystem.drive(0.0, 0.0, twistInput, 0.0, DriveState.AUTO, true)
    }

    fun kill() {
        Robot.swerveSystem.backRight.kill()
        Robot.swerveSystem.backLeft.kill()
        Robot.swerveSystem.frontRight.kill()
        Robot.swerveSystem.frontLeft.kill()
    }
}
