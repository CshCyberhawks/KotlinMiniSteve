package frc.robot.util

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard


class IO {
    companion object {
        private val joystick = Joystick(0)
        private val joystick2 = Joystick(1)
        private val xbox = XboxController(2)
        private const val controllerDeadzone = 0.3

        var hosas = false;

        fun getPolarCoords(): DoubleArray? {
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
            return xbox.startButtonPressed
        }

        fun intakeBall(): Double {
            return xbox.leftTriggerAxis
        }

        fun limelightLockOn(): Boolean {
            return joystick.getRawButton(3)
        }

        fun moveTransport(): Double {
            return if (Math.abs(xbox.leftY) > controllerDeadzone) xbox.leftY else 0.0
        }

        fun moveRobotX(): Double {
            // SmartDashboard.putNumber("Jotstick X", joystick.y)
            return MathClass.calculateDeadzone(joystick.y, controllerDeadzone)
        }

        fun moveRobotY(): Double {
            // SmartDashboard.putNumber("Joystick Y", joystick.x)
            return MathClass.calculateDeadzone(joystick.x, controllerDeadzone)
        }

        fun removeBall(): Boolean {
            return xbox.backButton
        }

        fun getAutoShootCancel(): Boolean {
            return xbox.startButton || shootBall() > 0
        }

        fun resetGyro(): Boolean {
            return joystick.getRawButtonPressed(8)
        }

        fun shootBall(): Double {
            return xbox.rightTriggerAxis
        }

        fun turnControl(): Double {
            return if (hosas) MathClass.calculateDeadzone(
                joystick2.x,
                .1
            ) else MathClass.calculateDeadzone(joystick.twist, .1)
        }

        fun getSWOReset(): Boolean {
            return joystick.getRawButton(7)
        }

        fun getJoyThrottle(): Double {
            if (hosas) {
            return MathClass.calculateDeadzone((-joystick2.throttle + 1) / 2, .05)
            }
            else {
            return MathClass.calculateDeadzone((-joystick.throttle + 1) / 2, .05)
            }
        }

        fun getResetCargo(): Boolean {
            return xbox.bButtonPressed
        }

        fun raiseShootSpeed(): Boolean {
            return xbox.rightBumperPressed
        }

        fun lowerShootSpeed(): Boolean {
            return xbox.leftBumperPressed
        }

        // public static boolean getXboxRightBumper() {
        // return xbox.getRightBumper();
        // }

        // public static boolean getXboxLeftBumper() {
        // return xbox.getLeftBumper();
        // }

        // public static double getXboxLeftX() {
        // return Math.abs(xbox.getLeftX()) > controllerMathClass.calculateDeadzone ?
        // xbox.getLeftX() : 0;
        // }

        // public static double getXboxRightX() {
        // return Math.abs(xbox.getRightX()) > controllerMathClass.calculateDeadzone ?
        // xbox.getRightX() :
        // 0;
        // }

        // public static double getJoyTwist() {
        // SmartDashboard.putNumber("Joystick Twist", joystick.getTwist());
        // return MathClass.calculateDeadzone(joystick.getTwist(),
        // controllerMathClass.calculateDeadzone);
        // }
    }
}
