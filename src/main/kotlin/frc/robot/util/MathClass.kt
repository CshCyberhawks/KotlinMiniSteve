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

        fun cartesianToPolar(x: Double, y: Double): DoubleArray {
            // math to turn cartesian into polar
            val r = sqrt(x.pow(2.0) + y.pow(2.0))
            val theta = Math.toDegrees(atan2(y, x))
            return doubleArrayOf(theta, r)
        }

        fun polarToCartesian(theta: Double, r: Double): DoubleArray {
            // math to turn polar coordinate into cartesian
            var theta = theta
            theta = Math.toRadians(theta)
            val x = r * cos(theta)
            val y = r * sin(theta)
            return doubleArrayOf(x, y)
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
            maxSpeed: Double,
            minSpeed: Double
        ): DoubleArray {
            val minMax = getMinMax(speeds)
            val divSpeed = if (abs(minMax[0]) > minMax[1]) abs(minMax[0]) else minMax[1]
            val highestSpeed = if (minMax[1] > maxSpeed) maxSpeed else minMax[1]
            val lowestSpeed = if (minMax[0] < minSpeed) minSpeed else minMax[0]
            for (i in speeds.indices) {
                if (minMax[1] > maxSpeed && speeds[i] > 0) speeds[i] =
                    speeds[i] / divSpeed * highestSpeed else if (minMax[0] < minSpeed && speeds[i] < 0) speeds[i] =
                    speeds[i] / -divSpeed * lowestSpeed
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
            return meters * 3.69230769231
        }

        fun swosToMeters(swos: Double): Double {
            return swos * 0.27083333333
        }

        fun wrapAroundAngles(angle: Double): Double {
            return if (angle < 0) 360 + angle else angle
        }
    }
}