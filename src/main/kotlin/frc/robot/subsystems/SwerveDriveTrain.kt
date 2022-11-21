package frc.robot.subsystems

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.util.*
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.*


class SwerveDriveTrain : SubsystemBase() { // p = 10 gets oscillation
    var backLeft: SwerveWheel = SwerveWheel(
        Constants.backLeftTurnMotor, Constants.backLeftDriveMotor,
        Constants.backLeftEncoder
    )
    var backRight: SwerveWheel = SwerveWheel(
        Constants.backRightTurnMotor, Constants.backRightDriveMotor,
        Constants.backRightEncoder
    )
    var frontLeft: SwerveWheel = SwerveWheel(
        Constants.frontLeftTurnMotor, Constants.frontLeftDriveMotor,
        Constants.frontLeftEncoder
    )
    var frontRight: SwerveWheel = SwerveWheel(
        Constants.frontRightTurnMotor, Constants.frontRightDriveMotor,
        Constants.frontRightEncoder
    )
    var gyro: Gyro? = null
    var throttle = 0.35

    var lastThrottle: Double = -1.0

    var xPID: PIDController = PIDController(10.0, 0.0, 1.0)
    var yPID: PIDController = PIDController(10.0, 0.0, 1.0)

    var wheelArr: Array<SwerveWheel> = arrayOf(backLeft, backRight, frontLeft, frontRight)

    var isTwisting = false

    // I do this to prevent large jumps in value with first run of loop in predicted
    // odometry
    private var lastUpdateTime = -1.0

    private var throttleShuffle: NetworkTableEntry = Robot.driveShuffleboardTab.add("throttle", throttle).entry
    private val fieldOrientedShuffle: NetworkTableEntry = Robot.driveShuffleboardTab.add("Field Oriented", true).entry

    var maxSwos = 13.9458
    var maxMeters = 3.777

    init {
        Gyro.setOffset()
    }

    fun fieldOriented(input: Coordinate, gyroAngle: Double): Coordinate {
        input.setTheta(input.getTheta() + gyroAngle)
        return input
    }

    fun calculateDrive(x1: Double, y1: Double, theta2: Double, r2: Double, twistMult: Double, fieldOrientedEnabled: Boolean): Coordinate {
        // X is 0 and Y is 1
        // Gets the cartesian coordinate of the robot's joystick translation inputs
//        SmartDashboard.putBoolean("Field Oriented", fieldOrientedEnabled)
        val driveCoordinate = if (fieldOrientedEnabled) fieldOriented(Coordinate(x1, y1), Gyro.getAngle()) else Coordinate(x1, y1)
        // Turns the twist constant + joystick twist input into a cartesian coordinates
        val twistCoordinate = Coordinate.fromPolar(theta2, r2 * twistMult)

        // Args are theta, r
        // Vector math adds the translation and twisting cartesian coordinates before
        // turning them into polar and returning
        // can average below instead of add - need to look into it
        return driveCoordinate + twistCoordinate
    }

    fun drive(inputX: Double, inputY: Double, inputTwist: Double, throttleChange: Double, mode: DriveState, fieldOrientedEnabled: Boolean) {
        if (Robot.autoMoveRunning && mode == DriveState.TELE) return
        var inputX = inputX
        var inputY = inputY
        var inputTwist = inputTwist
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val period = if (lastUpdateTime >= 0) timeNow - lastUpdateTime else 0.0
        val gyroAngle: Double = Gyro.getAngle()

        if (throttleChange != lastThrottle) {
            throttle = throttleChange
        }

        throttle = min(max(throttle, 0.0), 1.0)

        throttleShuffle.setDouble(throttle)

        SmartDashboard.putNumber("throttle ", throttle)
        SmartDashboard.putNumber("throttle change", throttleChange)
        SmartDashboard.putNumber("last throttle", lastThrottle)
        fieldOrientedShuffle.setBoolean(fieldOrientedEnabled)
        // SmartDashboard.putNumber("gyro val", gyroAngle)
        if (inputX == 0.0 && inputY == 0.0 && inputTwist == 0.0) {
            backRight.preserveAngle()
            backLeft.preserveAngle()
            frontRight.preserveAngle()
            frontLeft.preserveAngle()
            lastUpdateTime = timeNow
            lastThrottle = throttleChange
            return
        }

        /*
                 * double highestSpeed = Math.max(inputX, inputY) > Math.abs(Math.min(inputX,
                 * inputY))
                 * ? Math.max(inputX, inputY)
                 * : Math.abs(Math.min(inputX, inputY))
                 */
        // random decimal below is the max speed of robot in swos
        // double constantScaler = 13.9458 * highestSpeed
        // SmartDashboard.putNumber("drive inputTwist ", inputTwist)
        if (mode == DriveState.TELE) {
            inputX *= throttle
            inputY *= throttle
            inputTwist *= throttle // (throttle * 3)
        }
        // SmartDashboard.putNumber("drive inputX ", inputX)
        // SmartDashboard.putNumber("drive inputY ", inputY)

        // double pidPredictX = inputX * maxSwos * period
        // double pidPredictY = inputY * maxSwos * period

        // double pidInputX = xPID.calculate(Robot.swo.getVelocities()[0], pidPredictX)
        // / maxSwos
        // double pidInputY = yPID.calculate(Robot.swo.getVelocities()[1], pidPredictY)
        // / maxSwos

        // inputX += pidInputX
        // inputY += pidInputY
        isTwisting = inputTwist != 0.0

        // calculates the speed and angle for each motor
        val frontRightVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap[Wheels.FrontRight]!!,
            inputTwist, Constants.twistSpeedMult, fieldOrientedEnabled
        )
        val frontLeftVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap[Wheels.FrontLeft]!!,
            inputTwist, Constants.twistSpeedMult, fieldOrientedEnabled
        )
        val backRightVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap[Wheels.BackRight]!!,
            inputTwist, Constants.twistSpeedMult, fieldOrientedEnabled
        )
        val backLeftVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap[Wheels.BackLeft]!!,
            inputTwist, Constants.twistSpeedMult, fieldOrientedEnabled
        )
        val frontRightSpeed = frontRightVector.getHyp()
        val frontLeftSpeed = frontLeftVector.getHyp()
        val backRightSpeed = backRightVector.getHyp()
        val backLeftSpeed = backLeftVector.getHyp()
        val frontRightAngle = frontRightVector.getTheta()
        val frontLeftAngle = frontLeftVector.getTheta()
        val backRightAngle = backRightVector.getTheta()
        val backLeftAngle = backLeftVector.getTheta()
        var wheelSpeeds: DoubleArray = doubleArrayOf(frontRightSpeed, frontLeftSpeed, backRightSpeed, backLeftSpeed)
        wheelSpeeds = MathClass.normalizeSpeeds(wheelSpeeds, 1.0, -1.0)

        // SmartDashboard.putNumber("frontRightAngle", frontRightAngle)
        // SmartDashboard.putNumber("frontLeftAngle", frontLeftAngle)
        // SmartDashboard.putNumber("backRightAngle", backRightAngle)
        // SmartDashboard.putNumber("backLeftAngle", backLeftAngle)

        // sets the speed and angle of each motor
        backRight.drive(wheelSpeeds[2], backRightAngle, mode)
        backLeft.drive(wheelSpeeds[3], backLeftAngle, mode)
        frontRight.drive(wheelSpeeds[0], frontRightAngle, mode)
        frontLeft.drive(wheelSpeeds[1], frontLeftAngle, mode)

        // predictedVelocity.x = inputX * maxSwos * period
        // predictedVelocity.y = inputY * maxSwos * period
        lastUpdateTime = timeNow
        lastThrottle = throttleChange
    }

    // public void resetPredictedOdometry() {
    // predictedVelocity = new Vector2(0, 0)
    // }

}

