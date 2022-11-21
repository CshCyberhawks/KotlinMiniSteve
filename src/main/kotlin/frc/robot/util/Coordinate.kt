package frc.robot.util

import java.lang.Math.toDegrees
import java.lang.Math.toRadians
import kotlin.math.*

class Coordinate(var x: Double = 0.0, var y: Double = 0.0) {
    companion object {
        fun fromPolar(theta: Double, r: Double): Coordinate {
            return Coordinate(r * cos(toRadians(theta)), r * sin(toRadians(theta)))
        }
    }

    fun getTheta(): Double {
        return toDegrees(atan2(y, x))
    }

    fun setTheta(theta: Double) {
        val r = getHyp()
        x = r * cos(toRadians(theta))
        y = r * sin(toRadians(theta))
    }

    fun getHyp(): Double {
        return sqrt(x.pow(2) + y.pow(2))
    }

    fun setHyp(r: Double) {
        val theta = getTheta()
        x = r * cos(toRadians(theta))
        y = r * sin(toRadians(theta))
    }

    operator fun plus(other: Coordinate): Coordinate {
        return Coordinate(x + other.x, y + other.y)
    }
}