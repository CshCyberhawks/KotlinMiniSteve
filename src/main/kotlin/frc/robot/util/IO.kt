package frc.robot.util

import edu.wpi.first.wpilibj.Joystick
import kotlin.math.abs


object IO {
    private val joystick = Joystick(0)

    fun getY(): Double {
        return if (abs(joystick.y) > 0.05) joystick.y else 0.0
    }

    fun getZ(): Double {
        return if (abs(joystick.z) > 0.05) joystick.z else 0.0
    }

    fun getX(): Double {
        return if (abs(joystick.x) > 0.05) joystick.x else 0.0
    }

    fun getThrottle(): Double {
        return joystick.throttle
    }
}