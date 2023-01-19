package frc.robot
import frc.robot.util.FieldPosition
import frc.robot.util.Vector2
import frc.robot.util.Wheels

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. inside the companion object). Do not put anything functional in this class.
 *
 * It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
object Constants {
    // Put constant values inside the companion object to make them globally accessible.
    // ex. val motorPort: Int = 0
    const val frontRightTwistMult: Int = -1
    const val frontLeftTwistMult: Int = 1
    const val backRightTwistMult: Int = -1
    const val backLeftTwistMult: Int = 1

    //max angular turn speed (degrees/sec)
    const val maxSpeedSWOS: Double = 15.64

    //TODO: replace this with actual val
    const val maxTwistSpeed: Double = 370.0

    const val quickThrottleChange: Double = 0.01

    // twist speed mults for motors
    const val twistSpeedMult: Double = 0.8

    // twist angles for each motor
    //        val twistAngleMap: HashMap<String, Int> = object : HashMap() {
    //            init {
    //                put("frontRight", 45)
    //                put("frontLeft", 135)
    //                put("backRight", -45)
    //                put("backLeft", -135)
    //            }
    //        }
    // val twistAngleMap: Map<Wheels, Double> =
    //         mapOf(
    //                 Wheels.FrontRight to 45.0,
    //                 Wheels.FrontLeft to 135.0,
    //                 Wheels.BackRight to -45.0,
    //                 Wheels.BackLeft to -135.0
    //         )

    val twistAngleMap: Map<Wheels, Double> =
        mapOf(
            Wheels.FrontRight to -135.0,
            Wheels.FrontLeft to -45.0,
            Wheels.BackRight to 135.0,
            Wheels.BackLeft to 45.0
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
    // -3.7
    const val bottomShootSetpoint: Double = -6.4

    // 19.0
    const val topShootSetpoint: Double = 18.0

    // Intake System
    const val intakeMotor: Int = 15

    // Solenoid Controller
    const val pcm: Int = 4

    // Climb System
    const val climbMotor: Int = 10

    // Dimensions
    const val length: Double = 0.53
    const val width: Double = 0.53

    // Capacities
    const val maxVolts: Double = 4.85

    const val wheelRadius: Double = 0.0505 // In meters

    const val wheelGearRatio: Double = 7.0 / 1.0 // 7:1 gear ratio

    // public static final Vector2[] redBallPositions = { new Vector2(-2.2, 3.2),
    // new Vector2(0.6, 3.8),
    // new Vector2(3.2, 2.2),
    // new Vector2(3.3, -2.1), new Vector2(0.9, -3.8), new Vector2(-3.8, -0.9) }
    // public static final Vector2[] blueBallPositions = { new Vector2(-3.3, 2.1),
    // new Vector2(-0.9, 3.8),
    // new Vector2(3.8, 0.9),
    // new Vector2(2.3, -3.2), new Vector2(-0.6, -3.8), new Vector2(-3.2, -2.2) }

    // public static final Vector2[] redBallPositions = { new Vector2(-2.2, 3.2),
    // new Vector2(0.6, 3.8),
    // new Vector2(3.2, 2.2),
    // new Vector2(3.3, -2.1), new Vector2(0.9, -3.8), new Vector2(-3.8, -0.9) }
    // public static final Vector2[] blueBallPositions = { new Vector2(-3.3, 2.1),
    // new Vector2(-0.9, 3.8),
    // new Vector2(3.8, 0.9),
    // new Vector2(2.3, -3.2), new Vector2(-0.6, -3.8), new Vector2(-3.2, -2.2) }
    //     val redBallPositions: Array<Vector2> =
    //             arrayOf(
    //                     Vector2(-2.2, 3.2),
    //                     Vector2(0.7, 3.8),
    //                     Vector2(3.2, 2.2),
    //                     Vector2(3.3, -2.1),
    //                     Vector2(0.9, -3.8),
    //                     Vector2(-3.8, -0.9)
    //             )
    //     val blueBallPositions: Array<Vector2> =
    //             arrayOf(
    //                     Vector2(-3.3, 2.1),
    //                     Vector2(-.9, 3.8),
    //                     Vector2(3.8, 0.9),
    //                     Vector2(2.3, -3.2),
    //                     Vector2(-0.6, -3.8),
    //                     Vector2(-3.2, -2.2)
    //             )

    //     val blueShootingPositions: Array<FieldPosition> =
    //             arrayOf(FieldPosition(-.3, -.78, 115.0), FieldPosition(-.78, .3, 105.0))

    //     val redShootingPositions: Array<FieldPosition> =
    //             arrayOf(FieldPosition(.3, .78, -75.0), FieldPosition(.78, -0.3, 285.0))

    //     val blueStartingPositions =
    //             arrayOf(FieldPosition(-0.41, -1.1, 0.0), FieldPosition(-2.8, 1.0, 90.0))

    //     val redStartingPositions =
    //             arrayOf(FieldPosition(.41, 1.1, 180.0), FieldPosition(2.8, -1.0, 270.0))

    //     val blueTaxiPos: Vector2 = Vector2(-6.0, 0.0)
    //     val redTaxiPos: Vector2 = Vector2(6.0, 0.0)

    // These are relative to robot start because coordinates are being annoying
    // Towards limelight is -x
    // Right (facing with limelight) is +y
    // val redBallPositions: Array<Vector2> = arrayOf(Vector2(), Vector2(2.7, -.19))
    val ballPositionsZero: Array<Vector2> =
        arrayOf(
            Vector2(),
            Vector2(),
            Vector2(),
            Vector2(),
            Vector2(2.7, .1),
            Vector2(1.1, 2.8),
            Vector2(2.3, 6.6)
        )
    val ballPositionsOne: Array<Vector2> = arrayOf(Vector2(), Vector2(), Vector2(), Vector2())

    // val redShootingPositions: Array<FieldPosition> = arrayOf(FieldPosition(-0.11, 0.0, 15.0))
    val shootingPositionsZero: Array<FieldPosition> = arrayOf(FieldPosition(0.0, 0.0, 15.0))
    val shootingPositionsOne: Array<FieldPosition> = arrayOf(FieldPosition(-0.11, 0.0, 15.0))

    // val redStartingPositions: Array<FieldPosition> = arrayOf(FieldPosition(0.0, 0.0, 0.0))
    val startingPositions: Array<FieldPosition> = arrayOf(FieldPosition(0.0, 0.0, 0.0))

    // val redTaxiPositions: Array<Vector2> = arrayOf(Vector2(5.0, 0.0))
    val taxiPositionsZero: Array<Vector2> = arrayOf(Vector2(3.0, 0.0))
    val taxiPositionsOne: Array<Vector2> = arrayOf(Vector2(3.0, 0.0))

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

    // Wheels face inside
    val turnEncoderOffsets =
        doubleArrayOf(95.75, 137.15, 102.21, 9.22)
}
