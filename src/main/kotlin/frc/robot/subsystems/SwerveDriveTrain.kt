package frc.robot.subsystems

import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.util.*


class SwerveDriveTrain() : SubsystemBase() { // p = 10 gets oscillation
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

    private var throttleShuffle: NetworkTableEntry = Robot.driveShuffleboardTab.add("throttle", throttle).getEntry()

    var maxSwos = 13.9458
    var maxMeters = 3.777

    init {
        Gyro.setOffset()
    }

    fun fieldOriented(input: Vector2, gyroAngle: Double): Vector2 {
        val polar = MathClass.cartesianToPolar(input)
        polar.theta += gyroAngle
        return MathClass.polarToCartesian(polar)
    }

    fun calculateDrive(cartesian: Vector2, polar: Polar, twistMult: Double): Polar {
        // X is 0 and Y is 1
        // Gets the cartesian coordinate of the robot's joystick translation inputs
        val driveCoordinate = fieldOriented(cartesian, Gyro.getAngle())
        // Turns the twist constant + joystick twist input into a cartesian coordinates
        polar.r *= twistMult
        val twistCoordinate = MathClass.polarToCartesian(polar)

        // Args are theta, r
        // Vector math adds the translation and twisting cartesian coordinates before
        // turning them into polar and returning
        // can average below instead of add - need to look into it
        return MathClass.cartesianToPolar(driveCoordinate.add(twistCoordinate))
    }

    fun drive(input: Vector2, inputTwist: Double, throttleChange: Double, mode: DriveState?) {
        if (Robot.autoMoveRunning && mode == DriveState.TELE) return
        var inputX = input.x
        var inputY = input.y
        var inputTwist = inputTwist
        val timeNow = WPIUtilJNI.now() * 1.0e-6

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
            Vector2(inputX, inputY), Polar(
                Constants.twistAngleMap["frontRight"]!!,
                inputTwist
            ), Constants.twistSpeedMult
        )
        val frontLeftVector = calculateDrive(
            Vector2(inputX, inputY), Polar(
                Constants.twistAngleMap["frontLeft"]!!,
                inputTwist
            ), Constants.twistSpeedMult
        )
        val backRightVector = calculateDrive(
            Vector2(inputX, inputY), Polar(
                Constants.twistAngleMap["backRight"]!!,
                inputTwist
            ), Constants.twistSpeedMult
        )
        val backLeftVector = calculateDrive(
            Vector2(inputX, inputY), Polar(
                Constants.twistAngleMap["backLeft"]!!,
                inputTwist
            ), Constants.twistSpeedMult
        )

        val wheelSpeeds: DoubleArray = MathClass.normalizeSpeeds(
            doubleArrayOf(
                frontRightVector.r,
                frontLeftVector.r,
                backRightVector.r,
                backLeftVector.r
            ), 1.0, -1.0
        )

        // SmartDashboard.putNumber("frontRightAngle", frontRightAngle);
        // SmartDashboard.putNumber("frontLeftAngle", frontLeftAngle);
        // SmartDashboard.putNumber("backRightAngle", backRightAngle);
        // SmartDashboard.putNumber("backLeftAngle", backLeftAngle);

        // sets the speed and angle of each motor
        backRight.drive(wheelSpeeds[2], backRightVector.theta, mode)
        backLeft.drive(wheelSpeeds[3], backLeftVector.theta, mode)
        frontRight.drive(wheelSpeeds[0], frontRightVector.theta, mode)
        frontLeft.drive(wheelSpeeds[1], frontLeftVector.theta, mode)

        // predictedVelocity.x = inputX * maxSwos * period;
        // predictedVelocity.y = inputY * maxSwos * period;
        lastUpdateTime = timeNow
    }

    // public void resetPredictedOdometry() {
    // predictedVelocity = new Vector2(0, 0);
    // }

}
