package frc.robot

import frc.robot.util.FieldPosition
import frc.robot.util.Vector2

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. inside the companion object).  Do not put anything functional in this class.
 *
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
class Constants {
    companion object {
        // Put constant values inside the companion object to make them globally accessible.
        // ex. val motorPort: Int = 0
        const val frontRightTwistMult: Int = -1
        const val frontLeftTwistMult: Int = 1
        const val backRightTwistMult: Int = -1
        const val backLeftTwistMult: Int = 1

        // twist speed mults for motors
        const val twistSpeedMult: Double = .6

        // twist angles for each motor
//        val twistAngleMap: HashMap<String, Int> = object : HashMap() {
//            init {
//                put("frontRight", 45)
//                put("frontLeft", 135)
//                put("backRight", -45)
//                put("backLeft", -135)
//            }
//        }
        val twistAngleMap: Map<String, Double> = mapOf(
            "frontRight" to 45.0,
            "frontLeft" to 135.0,
            "backRight" to -45.0,
            "backLeft" to -135.0
        )

        // TalonSRX Motors
        const val frontRightTurnMotor: Int = 1
        const val frontLeftTurnMotor: Int = 3
        const val backRightTurnMotor: Int = 5
        const val backLeftTurnMotor: Int = 2

        // Falcon Motors
        const val frontRightDriveMotor: Int = 9
        const val frontLeftDriveMotor: Int = 7
        const val backRightDriveMotor: Int = 6
        const val backLeftDriveMotor: Int = 8

        // Shoot System Motors
        const val rightShootMotor: Int = 11
        const val leftShootMotor: Int = 13
        const val topShootMotor: Int = 12
        const val traversalMotor: Int = 16

        // encoder values for perfect shots
        //-3.7
        const val bottomShootSetpoint: Double = -5.9

        //19.0
        const val topShootSetpoint: Double = 12.0

        // Intake System
        const val intakeMotor: Int = 15

        // Solenoid Controller
        const val pcm: Int = 4

        // Climb System
        const val climbMotor: Int = 10

        // Dimensions
        const val length: Double = .53
        const val width: Double = .53

        // Capacities
        const val maxVolts: Double = 4.85

        const val wheelRadius: Double = 0.0505 // In meters

        const val wheelGearRatio: Double = (7 / 1).toDouble() // 7:1 gear ratio

        // public static final Vector2[] redBallPositions = { new Vector2(-2.2, 3.2),
        // new Vector2(0.6, 3.8),
        // new Vector2(3.2, 2.2),
        // new Vector2(3.3, -2.1), new Vector2(0.9, -3.8), new Vector2(-3.8, -0.9) };
        // public static final Vector2[] blueBallPositions = { new Vector2(-3.3, 2.1),
        // new Vector2(-0.9, 3.8),
        // new Vector2(3.8, 0.9),
        // new Vector2(2.3, -3.2), new Vector2(-0.6, -3.8), new Vector2(-3.2, -2.2) };

        // public static final Vector2[] redBallPositions = { new Vector2(-2.2, 3.2),
        // new Vector2(0.6, 3.8),
        // new Vector2(3.2, 2.2),
        // new Vector2(3.3, -2.1), new Vector2(0.9, -3.8), new Vector2(-3.8, -0.9) };
        // public static final Vector2[] blueBallPositions = { new Vector2(-3.3, 2.1),
        // new Vector2(-0.9, 3.8),
        // new Vector2(3.8, 0.9),
        // new Vector2(2.3, -3.2), new Vector2(-0.6, -3.8), new Vector2(-3.2, -2.2) };
        val redBallPositions: Array<Vector2> = arrayOf<Vector2>(Vector2(3.0, 0.0), Vector2(0.0, 0.0))
        val blueBallPositions: Array<Vector2> =
            arrayOf<Vector2>(Vector2(3.0, 0.0), Vector2(2.0, -.8), Vector2(3.65, 1.9), Vector2(6.5, -2.2))

        val blueShootingPositions: Array<Vector2> = arrayOf<Vector2>(
            Vector2(-0.41, -1.1), Vector2(0.0, 0.0)
        )

        val redShootingPositions: Array<Vector2> = arrayOf<Vector2>(
            Vector2(0.41, 1.1), Vector2(0.0, 0.0)
        )

        val blueStartingPositions = arrayOf<FieldPosition>(
            FieldPosition(1.28, -0.6, 0.0),
            FieldPosition(-0.41, -1.1, 111.0),
            FieldPosition(-2.8, 1.0, 0.0) // 111 is 0 angle
        )

        val redStartingPositions = arrayOf<FieldPosition>(
            FieldPosition(0.41, 1.1, 291.0), FieldPosition(2.8, -1.0, 0.0) // 291 is 0 angle
        )

        val blueTaxiPos: Vector2 = Vector2(-6.0, 0.0)
        val redTaxiPos: Vector2 = Vector2(6.0, 0.0)

        // Break Beams
        const val frontBreakBeam = 6
        const val backBreakBeam = 4
        const val topBreakBeam = 5
        const val shootBreakBeam = 7

        // Encoders
        const val frontRightEncoder = 0
        const val frontLeftEncoder = 1
        const val backRightEncoder = 3
        const val backLeftEncoder = 2

        // Tape faces inside
        val turnEncoderOffsets = doubleArrayOf(
            305.94723429600003, 87.27538168800001, 112.58787909600001, 279.140596416
        )
    }
}
