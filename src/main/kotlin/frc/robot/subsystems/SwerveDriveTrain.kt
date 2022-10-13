package frc.robot.subsystems

import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.util.*


class SwerveDriveTrain : SubsystemBase {
    var backLeft: SwerveWheel? = null
    var backRight: SwerveWheel? = null
    var frontLeft: SwerveWheel? = null
    var frontRight: SwerveWheel? = null
    var gyro: Gyro? = null
    var throttle = 0.35

    var xPID: PIDController? = null
    var yPID: PIDController? = null

    var predictedVelocity: Vector2? = null

    var wheelArr: Array<SwerveWheel?> = arrayOfNulls<SwerveWheel>(4)

    var isTwisting = false

    // I do this to prevent large jumps in value with first run of loop in predicted
    // odometry
    private var lastUpdateTime = -1.0

    private var throttleShuffle: NetworkTableEntry? = null

    var maxSwos = 13.9458
    var maxMeters = 3.777

    constructor() {
        // p = 10 gets oscillation
        xPID = PIDController(10.0, 0.0, 1.0)
        yPID = PIDController(10.0, 0.0, 1.0)
        backLeft = SwerveWheel(
            Constants.backLeftTurnMotor, Constants.backLeftDriveMotor,
            Constants.backLeftEncoder
        )
        backRight = SwerveWheel(
            Constants.backRightTurnMotor, Constants.backRightDriveMotor,
            Constants.backRightEncoder
        )
        frontLeft = SwerveWheel(
            Constants.frontLeftTurnMotor, Constants.frontLeftDriveMotor,
            Constants.frontLeftEncoder
        )
        frontRight = SwerveWheel(
            Constants.frontRightTurnMotor, Constants.frontRightDriveMotor,
            Constants.frontRightEncoder
        )
        wheelArr[0] = backLeft
        wheelArr[1] = backRight
        wheelArr[2] = frontLeft
        wheelArr[3] = frontRight
        predictedVelocity = Vector2(0.0, 0.0)
        throttleShuffle = Robot.driveShuffleboardTab.add("throttle", throttle).getEntry()
        Gyro.setOffset()
    }

    fun polarToCartesian(theta: Double, r: Double): DoubleArray {
        // math to turn polar coordinate into cartesian
        val x = r * Math.cos(Math.toRadians(theta))
        val y = r * Math.sin(Math.toRadians(theta))
        return doubleArrayOf(x, y)
    }

    fun cartesianToPolar(x: Double, y: Double): DoubleArray {
        // math to turn cartesian into polar
        val r = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0))
        val theta = Math.toDegrees(Math.atan2(y, x))
        return doubleArrayOf(theta, r)
    }

    fun fieldOriented(x: Double, y: Double, gyroAngle: Double): DoubleArray {
        // turns the translation input into polar
        val polar = cartesianToPolar(x, y)
        // subtracts the gyro angle from the polar angle of the translation of the robot
        // makes it field oriented
        val theta = polar[0] + gyroAngle
        val r = polar[1]

        // returns the new field oriented translation but converted to cartesian
        return polarToCartesian(theta, r)
    }

    fun calculateDrive(x1: Double, y1: Double, theta2: Double, r2: Double, twistMult: Double): DoubleArray {
        // X is 0 and Y is 1
        // Gets the cartesian coordinate of the robot's joystick translation inputs
        val driveCoordinate = fieldOriented(x1, y1, Gyro.getAngle())
        // Turns the twist constant + joystick twist input into a cartesian coordinates
        val twistCoordinate = polarToCartesian(theta2, r2 * twistMult)

        // Args are theta, r
        // Vector math adds the translation and twisting cartesian coordinates before
        // turning them into polar and returning
        // can average below instead of add - need to look into it
        return cartesianToPolar(
            driveCoordinate[0] + twistCoordinate[0],
            driveCoordinate[1] + twistCoordinate[1]
        )
    }

    fun drive(inputX: Double, inputY: Double, inputTwist: Double, throttleChange: Double, mode: DriveState?) {
        var inputX = inputX
        var inputY = inputY
        var inputTwist = inputTwist
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val period = if (lastUpdateTime >= 0) timeNow - lastUpdateTime else 0.0
        val gyroAngle: Double = Gyro.getAngle()

        throttle = throttleChange
        throttleShuffle!!.setDouble(throttle)
        SmartDashboard.putNumber("throttle ", throttle)
        // SmartDashboard.putNumber("gyro val", gyroAngle)
        if (inputX == 0.0 && inputY == 0.0 && inputTwist == 0.0) {
            backRight!!.preserveAngle()
            backLeft!!.preserveAngle()
            frontRight!!.preserveAngle()
            frontLeft!!.preserveAngle()
            lastUpdateTime = timeNow
            return
        }

        /*
                 * double highestSpeed = Math.max(inputX, inputY) > Math.abs(Math.min(inputX,
                 * inputY))
                 * ? Math.max(inputX, inputY)
                 * : Math.abs(Math.min(inputX, inputY));
                 */
        // random decimal below is the max speed of robot in swos
        // double constantScaler = 13.9458 * highestSpeed;
        SmartDashboard.putNumber("drive inputTwist ", inputTwist)
        when (mode) {
            DriveState.TELE -> {
                inputX *= throttle
                inputY *= throttle
                inputTwist *= throttle // (throttle * 3);
            }
        }
        SmartDashboard.putNumber("drive inputX ", inputX)
        SmartDashboard.putNumber("drive inputY ", inputY)

        // double pidPredictX = inputX * maxSwos * period;
        // double pidPredictY = inputY * maxSwos * period;

        // double pidInputX = xPID.calculate(Robot.swo.getVelocities()[0], pidPredictX)
        // / maxSwos;
        // double pidInputY = yPID.calculate(Robot.swo.getVelocities()[1], pidPredictY)
        // / maxSwos;

        // inputX += pidInputX;
        // inputY += pidInputY;
        isTwisting = inputTwist != 0.0

        // calculates the speed and angle for each motor
        val frontRightVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap["frontRight"]!!,
            inputTwist, Constants.twistSpeedMult
        )
        val frontLeftVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap["frontLeft"]!!,
            inputTwist, Constants.twistSpeedMult
        )
        val backRightVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap["backRight"]!!,
            inputTwist, Constants.twistSpeedMult
        )
        val backLeftVector = calculateDrive(
            inputX, inputY, Constants.twistAngleMap["backLeft"]!!,
            inputTwist, Constants.twistSpeedMult
        )
        val frontRightSpeed = frontRightVector[1]
        val frontLeftSpeed = frontLeftVector[1]
        val backRightSpeed = backRightVector[1]
        val backLeftSpeed = backLeftVector[1]
        val frontRightAngle = frontRightVector[0]
        val frontLeftAngle = frontLeftVector[0]
        val backRightAngle = backRightVector[0]
        val backLeftAngle = backLeftVector[0]
        var wheelSpeeds: DoubleArray? = doubleArrayOf(frontRightSpeed, frontLeftSpeed, backRightSpeed, backLeftSpeed)
        wheelSpeeds = MathClass.normalizeSpeeds(wheelSpeeds!!, 1.0, -1.0)

        // SmartDashboard.putNumber("frontRightAngle", frontRightAngle);
        // SmartDashboard.putNumber("frontLeftAngle", frontLeftAngle);
        // SmartDashboard.putNumber("backRightAngle", backRightAngle);
        // SmartDashboard.putNumber("backLeftAngle", backLeftAngle);

        // sets the speed and angle of each motor
        backRight!!.drive(wheelSpeeds!![2], backRightAngle, mode)
        backLeft!!.drive(wheelSpeeds[3], backLeftAngle, mode)
        frontRight!!.drive(wheelSpeeds[0], frontRightAngle, mode)
        frontLeft!!.drive(wheelSpeeds[1], frontLeftAngle, mode)

        // predictedVelocity.x = inputX * maxSwos * period;
        // predictedVelocity.y = inputY * maxSwos * period;
        lastUpdateTime = timeNow
    }

    // public void resetPredictedOdometry() {
    // predictedVelocity = new Vector2(0, 0);
    // }

}
