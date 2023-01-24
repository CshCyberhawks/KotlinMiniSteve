package frc.robot.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import kotlin.math.abs


object IO {
    private val joystick: Joystick = Joystick(0)
    private val joystick2: Joystick = Joystick(1)
    private val xbox: XboxController = XboxController(2)
    private const val controllerDeadzone: Double = 0.3

    private var dualStickDrive: Boolean = true


    fun getPolarCoords(): DoubleArray {
        return doubleArrayOf(
                -MathClass.calculateDeadzone(joystick.directionDegrees, controllerDeadzone),
                MathClass.calculateDeadzone(joystick.magnitude, controllerDeadzone),
                MathClass.calculateDeadzone(joystick.twist, controllerDeadzone)
        )
    }

    fun autoIntake(): Boolean {
        return xbox.yButtonPressed
    }

    fun autoShoot(): Boolean {
        return xbox.xButtonPressed
    }

    fun climbControl(): Double {
        return xbox.rightY
    }

    fun deployClimbSolenoid(): Boolean {
        return xbox.aButtonPressed
    }

    fun getAutoIntakeCancel(): Boolean {
        return xbox.bButton
    }

    fun intakeBall(): Double {
        return xbox.leftTriggerAxis
    }

    fun limelightLockOn(): Boolean {
        return joystick.getRawButton(3)
    }

    fun cancelLimelightLockOn(): Boolean {
        return joystick.getRawButton(14)
    }

    fun moveTransport(): Double {
        return if (abs(xbox.leftY) > controllerDeadzone) xbox.leftY else 0.0
    }

    fun moveRobotX(): Double {
        // SmartDashboard.putNumber("Joystick X", joystick.y)
        return MathClass.calculateDeadzone(joystick.y, controllerDeadzone)
    }

    fun disableFieldOrientation(): Boolean {
        return joystick.trigger
    }

    fun moveRobotY(): Double {
        // SmartDashboard.putNumber("Joystick Y", joystick.x)
        return MathClass.calculateDeadzone(joystick.x, controllerDeadzone)
    }

    fun removeBall(): Boolean {
        return xbox.backButton
    }

    fun getAutoShootCancel(): Boolean {
        return xbox.bButton || shootBall() > 0
    }

    fun resetGyro(): Boolean {
        return joystick.getRawButtonPressed(2)
    }

    fun shootBall(): Double {
        return xbox.rightTriggerAxis
    }

    fun turnControl(): Double {
        return if (dualStickDrive) MathClass.calculateDeadzone(
                joystick2.x * -1,
                .1
        ) else MathClass.calculateDeadzone(joystick.twist * -1, .1)
    }

    fun getSWOReset(): Boolean {
        return joystick.getRawButton(7)
    }

    fun getJoyThrottle(): Double {
        return if (dualStickDrive) {
            MathClass.calculateDeadzone((-joystick2.throttle + 1) / 2, .05)
        } else {
            MathClass.calculateDeadzone((-joystick.throttle + 1) / 2, .05)
        }
    }

    fun getQuickThrottle(): Int {
        return joystick2.pov
    }

    fun getNormalThrottle(): Boolean {
        return joystick2.getRawButtonPressed(3)
    }

    fun getFastThrottle(): Boolean {
        return joystick2.getRawButtonPressed(4)
    }

    fun getResetCargo(): Boolean {
        return xbox.bButtonPressed
    }

    fun increaseCargoAmount(): Boolean {
        return xbox.rightBumperPressed
    }

    fun decreaseCargoAmount(): Boolean {
        return xbox.leftBumperPressed
    }

    fun autoBalance(): Boolean {
        return joystick.getRawButtonPressed(8);
    }

    // public static boolean getXboxRightBumper() {
    // return xbox.getRightBumper()
    // }

    // public static boolean getXboxLeftBumper() {
    // return xbox.getLeftBumper()
    // }

    // public static double getXboxLeftX() {
    // return Math.abs(xbox.getLeftX()) > controllerMathClass.calculateDeadzone ?
    // xbox.getLeftX() : 0
    // }

    // public static double getXboxRightX() {
    // return Math.abs(xbox.getRightX()) > controllerMathClass.calculateDeadzone ?
    // xbox.getRightX() :
    // 0
    // }

    // public static double getJoyTwist() {
    // SmartDashboard.putNumber("Joystick Twist", joystick.getTwist())
    // return MathClass.calculateDeadzone(joystick.getTwist(),
    // controllerMathClass.calculateDeadzone)
    // }
}
