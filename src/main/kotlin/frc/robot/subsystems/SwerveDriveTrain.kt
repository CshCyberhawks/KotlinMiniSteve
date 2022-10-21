package frc.robot.subsystems

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.util.DriveState
import frc.robot.util.Gyro
import frc.robot.util.MathClass
import frc.robot.util.Vector2


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

    var xPID: PIDController = PIDController(10.0, 0.0, 1.0)
    var yPID: PIDController = PIDController(10.0, 0.0, 1.0)

    var predictedVelocity: Vector2 = Vector2(0.0, 0.0)

    var wheelArr: Array<SwerveWheel> = arrayOf(backLeft, backRight, frontLeft, frontRight)

    var isTwisting = false

    // I do this to prevent large jumps in value with first run of loop in predicted
    // odometry
    private var lastUpdateTime = -1.0

    private var throttleShuffle: NetworkTableEntry = Robot.driveShuffleboardTab.add("throttle", throttle).entry

    var maxSwos = 13.9458
    var maxMeters = 3.777

    init {
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

    fun fieldOriented(input: Vector2, gyroAngle: Double): Vector2 {
        // turns the translation input into polar
        val polar = MathClass.cartesianToPolar(input)
        // subtracts the gyro angle from the polar angle of the translation of the robot
        // makes it field oriented
        polar.theta += gyroAngle
        // returns the new field oriented translation but converted to cartesian
        return MathClass.polarToCartesian(polar)
    }

    fun calculateDrive(x1: Double, y1: Double, theta2: Double, r2: Double, twistMult: Double): DoubleArray {
        // X is 0 and Y is 1
        // Gets the cartesian coordinate of the robot's joystick translation inputs
        val driveCoordinate = fieldOriented(Vector2(x1, y1), Gyro.getAngle())
        // Turns the twist constant + joystick twist input into a cartesian coordinates
        val twistCoordinate = polarToCartesian(theta2, r2 * twistMult)

        // Args are theta, r
        // Vector math adds the translation and twisting cartesian coordinates before
        // turning them into polar and returning
        // can average below instead of add - need to look into it
        return cartesianToPolar(
            driveCoordinate.x + twistCoordinate[0],
            driveCoordinate.y + twistCoordinate[1]
        )
    }

    fun drive(inputX: Double, inputY: Double, inputTwist: Double, throttleChange: Double, mode: DriveState?) {
        if (Robot.autoMoveRunning && mode == DriveState.TELE) return
        var inputX = inputX
        var inputY = inputY
        var inputTwist = inputTwist
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val period = if (lastUpdateTime >= 0) timeNow - lastUpdateTime else 0.0
        val gyroAngle: Double = Gyro.getAngle()

        throttle = throttleChange
        throttleShuffle.setDouble(throttle)

        SmartDashboard.putNumber("throttle ", throttle)
        // SmartDashboard.putNumber("gyro val", gyroAngle)
        if (inputX == 0.0 && inputY == 0.0 && inputTwist == 0.0) {
            backRight.preserveAngle()
            backLeft.preserveAngle()
            frontRight.preserveAngle()
            frontLeft.preserveAngle()
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
        if (mode == DriveState.TELE) {
            inputX *= throttle
            inputY *= throttle
            inputTwist *= throttle // (throttle * 3);
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
        var wheelSpeeds: DoubleArray = doubleArrayOf(frontRightSpeed, frontLeftSpeed, backRightSpeed, backLeftSpeed)
        wheelSpeeds = MathClass.normalizeSpeeds(wheelSpeeds, 1.0, -1.0)

        // SmartDashboard.putNumber("frontRightAngle", frontRightAngle);
        // SmartDashboard.putNumber("frontLeftAngle", frontLeftAngle);
        // SmartDashboard.putNumber("backRightAngle", backRightAngle);
        // SmartDashboard.putNumber("backLeftAngle", backLeftAngle);

        // sets the speed and angle of each motor
        backRight.drive(wheelSpeeds[2], backRightAngle, mode)
        backLeft.drive(wheelSpeeds[3], backLeftAngle, mode)
        frontRight.drive(wheelSpeeds[0], frontRightAngle, mode)
        frontLeft.drive(wheelSpeeds[1], frontLeftAngle, mode)

        // predictedVelocity.x = inputX * maxSwos * period;
        // predictedVelocity.y = inputY * maxSwos * period;
        lastUpdateTime = timeNow
    }

    // public void resetPredictedOdometry() {
    // predictedVelocity = new Vector2(0, 0);
    // }

}
