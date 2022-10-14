package frc.robot.subsystems

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.math.trajectory.TrapezoidProfile
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.util.DriveState
import frc.robot.util.Gyro
import frc.robot.util.MathClass
import frc.robot.util.Vector2


class SwerveAuto() {
    private var desiredPosition: Vector2 = Vector2(0.0, 0.0)
    private var desiredAngle = 0.0

    private var team: Alliance = DriverStation.getAlliance()
    var ballPositions: Array<Vector2> = if (team == Alliance.Blue) Constants.blueBallPositions else Constants.redBallPositions

    private var byBall = false
    private val ballDistanceDeadzone = 0.05
    private val normalDistanceDeadzone = .5

    private val positionStopRange = .1
    private val isAtPosition = false
    private val isAtAngle = false

    private var trapXFinished = false
    private var trapYFinsihed = false

    // both below args are in m/s - first is velocity (35% of max robot velocity of
    // 3.77), and a max accel of .05 m/s
    private val trapConstraints = TrapezoidProfile.Constraints(2.0, .8)
    private var trapXCurrentState: TrapezoidProfile.State = TrapezoidProfile.State(
        Robot.swo.getPosition().positionCoord.x, Robot.swo.getVelocities().get(0)
    )
    private var trapXDesiredState: TrapezoidProfile.State = TrapezoidProfile.State(desiredPosition.x, 0.0)
    private var trapYCurrentState: TrapezoidProfile.State = TrapezoidProfile.State(
        Robot.swo.getPosition().positionCoord.y, Robot.swo.getVelocities().get(1)
    )
    private var trapYDesiredState: TrapezoidProfile.State = TrapezoidProfile.State(desiredPosition.y, 0.0)

    // TODO: prob need to increase derivatives
    private val xPID = PIDController(5.0, 0.0, 0.05)
    private val yPID = PIDController(5.0, 0.0, 0.05)

    private var prevTime = 0.0

    fun setDesiredPosition(desiredPosition: Vector2) { // , double desiredVelocity) {
        byBall = false
        this.desiredPosition = desiredPosition
        // SmartDashboard.putNumber("Desired pos input", desiredPosition.x)
        val polarPosition = MathClass.cartesianToPolar(desiredPosition.x, desiredPosition.y)
        // double[] desiredVelocities = MathClass.polarToCartesian(polarPosition[0],
        // desiredVelocity);
        trapXDesiredState = TrapezoidProfile.State(this.desiredPosition.x, 0.0) // desiredVelocities[0]);
        trapYDesiredState = TrapezoidProfile.State(this.desiredPosition.y, 0.0) // desiredVelocities[0]);
    }

    fun setDesiredPositionBall(ballNumber: Int) { // , double desiredVelocity) {
        // SmartDashboard.putNumber("desired pos from ball fun", ballPositions[ballNumber].x)
        setDesiredPosition(ballPositions[ballNumber]) // , desiredVelocity);
        byBall = true
    }

    fun setDesiredPositionDistance(distance: Double) {
        val desiredPositionCart = MathClass.polarToCartesian(Robot.swo.getPosition().angle, distance)
        setDesiredPosition(Vector2(desiredPositionCart!![0], desiredPositionCart[1])) // , 0);
    }

    fun setDesiredAngle(angle: Double, robotRelative: Boolean) {
        desiredAngle = if (robotRelative) {
            MathClass.wrapAroundAngles(angle - Gyro.getAngle())
        } else {
            angle
        }
    }

    fun isAtDesiredPosition(): Boolean {
        // SmartDashboard.putNumber("Desired position distance x", desiredPosition!!.x - MathClass.swosToMeters(Robot.swo!!.getPosition()!!.positionCoord!!.x))
        val deadzone = if (byBall) ballDistanceDeadzone else normalDistanceDeadzone
        return (MathClass.calculateDeadzone(
                desiredPosition.x - MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x),
                deadzone
            ) == 0.0
                    && MathClass.calculateDeadzone(
                desiredPosition.y - MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y),
                deadzone
            ) == 0.0)
    }

    fun isAtDesiredAngle(): Boolean {
        return MathClass.calculateDeadzone(
            MathClass.wrapAroundAngles(Robot.swo.getPosition().angle) - MathClass.wrapAroundAngles(desiredAngle),
            20.0
        ) == 0.0
    }

    fun translate() {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val trapTime: Double = if (prevTime == 0.0) 0.0 else timeNow - prevTime
        val trapXProfile = TrapezoidProfile(trapConstraints, trapXDesiredState, trapXCurrentState)
        val trapYProfile = TrapezoidProfile(trapConstraints, trapYDesiredState, trapYCurrentState)
        trapXFinished = trapXProfile.isFinished(trapTime)
        trapYFinsihed = trapYProfile.isFinished(trapTime)
        val trapXOutput = trapXProfile.calculate(trapTime)
        val trapYOutput = trapYProfile.calculate(trapTime)
        // SmartDashboard.putNumber("TrapY", trapYOutput.position);
        // SmartDashboard.putNumber("TrapX", trapXOutput.position);
        val xPIDOutput = xPID.calculate(
            MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x),
            trapXOutput.position
        )
        val yPIDOutput = yPID.calculate(
            MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y),
            trapYOutput.position
        )
        val xVel = (trapXOutput.velocity + xPIDOutput)
        val yVel = (trapYOutput.velocity + yPIDOutput)
        // val xVel = xPIDOutput
        // val yVel = yPIDOutput
        // SmartDashboard.putNumber("xDriveInput", xVel / 3.777)
        // SmartDashboard.putNumber("yDriveInput", yVel / 3.777)
        Robot.swerveSystem.drive(xVel / 3.777, yVel / 3.777, 0.0, 0.0, DriveState.AUTO)
        trapXCurrentState = trapXOutput
        trapYCurrentState = trapYOutput
        prevTime = timeNow
    }

    fun twist() {
        val twistValue: Double = MathUtil.clamp(Robot.swo.getPosition().angle - desiredAngle, -1.0, 1.0)
        val twistInput = twistValue * .3
        // SmartDashboard.putNumber(" auto twistVal ", twistInput)
        Robot.swerveSystem.drive(0.0, 0.0, twistInput, 0.0, DriveState.AUTO)
    }

    fun kill() {
        Robot.swerveSystem.backRight.kill()
        Robot.swerveSystem.backLeft.kill()
        Robot.swerveSystem.frontRight.kill()
        Robot.swerveSystem.frontLeft.kill()
    }
}