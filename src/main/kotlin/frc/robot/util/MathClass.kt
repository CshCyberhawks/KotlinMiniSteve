package frc.robot.util

import edu.wpi.first.util.WPIUtilJNI
import kotlin.math.*


class MathClass {
    companion object {
        fun calculateDeadzone(input: Double, deadzone: Double): Double {
            return if (abs(input) > deadzone) input else 0.0
        }

        fun gToMetersPerSecond(g: Double): Double {
            return g / 9.8066
        }

        fun cartesianToPolar(input: Vector2): Polar {
            // math to turn cartesian into polar
            val r = Math.sqrt(Math.pow(input.x, 2.0) + Math.pow(input.y, 2.0))
            val theta = Math.toDegrees(Math.atan2(input.y, input.x))
            return Polar(theta, r)
        }

        fun polarToCartesian(input: Polar): Vector2 {
            // math to turn polar coordinate into cartesian
            val x = input.r * Math.cos(Math.toRadians(input.theta))
            val y = input.r * Math.sin(Math.toRadians(input.theta))
            return Vector2(x, y)
        }

        fun getMinMax(values: DoubleArray): DoubleArray {
            var min = values[0]
            var max = values[0]
            for (v in values) {
                if (v < min) min = v
                if (v > max) max = v
            }
            return doubleArrayOf(min, max)
        }

        fun normalizeSpeeds(
            speeds: DoubleArray,
            distanceFromZero: Double
        ): DoubleArray {
            var max = abs(speeds[0])

            for (wheelVector in speeds) {
                if (abs(wheelVector) > max) {
                    max = abs(wheelVector)
                }
            }

            val maxSpeed = if (max > distanceFromZero) max else distanceFromZero

            for (i in speeds.indices) {
                speeds[i] = speeds[i] / maxSpeed * distanceFromZero
            }

            return speeds
        }

        fun optimize(desiredAngle: Double, currentAngle: Double): Double {
            return if (abs(desiredAngle - currentAngle) > 90 && abs(desiredAngle - currentAngle) < 270)
                -1.0
            else
                1.0
        }

        fun getCurrentTime(): Double {
            return WPIUtilJNI.now() * 1.0e-6
        }

        fun metersToSwos(meters: Double): Double {
            return meters * 3.918699
        }

        fun swosToMeters(swos: Double): Double {
            return swos * 0.255187
        }

        fun wrapAroundAngles(angle: Double): Double {
            return if (angle < 0) 360 + angle else angle
        }
    }
}