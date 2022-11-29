package frc.robot.util

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.math.filter.LinearFilter
import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

class Gyro {
    companion object { // Makes the class static
        private val gyro: AHRS = AHRS(SPI.Port.kMXP)
        var offset: Double = 0.0
        private val filter: LinearFilter = LinearFilter.highPass(0.1, 0.02)

        private var lastAngle: Double = 0.0
        private var lastUpdateTime: Double = 0.0

        private fun wrapAroundAngles(input: Double): Double {
            return if (input < 0) 360 + input else input
        }

        fun getAngle(): Double {
            SmartDashboard.putNumber("Gyro raw", gyro.yaw - offset)
            SmartDashboard.putNumber("Gyro wrapped", wrapAroundAngles(gyro.yaw - offset))
            return wrapAroundAngles(wrapAroundAngles(gyro.yaw.toDouble()) - offset)
            // return gyro.getYaw()
        }

        fun isConnected(): Boolean {
            return gyro.isConnected
        }

        fun reset() {
            gyro.reset()
        }

        /**
         * Returns The rate of rotation reported by the gyro (deg/s)
         * @return The rate of rotation reported by the gyro (deg/s)
         */
        fun getAngularVelocity(): Double {
            val timeNow = WPIUtilJNI.now() * 1.0e-6
            val period = if (lastUpdateTime >= 0) timeNow - lastUpdateTime else 0.0

            var angleChange = MathClass.smallestDistanceBetween(getAngle(), lastAngle)            
            lastUpdateTime = timeNow
            return angleChange / period
        }

        fun setOffset() {
            // The gyro wasn't being nice
            offset = wrapAroundAngles(gyro.yaw.toDouble())
        }

        fun getZVelocity(): Double {
            return filter.calculate(gyro.velocityZ.toDouble())
        }

        fun getXVelocity(): Double {
            return filter.calculate(gyro.velocityX.toDouble())
            // return gyro.getVelocityX()
        }

        fun getYVelocity(): Double {
            return filter.calculate(gyro.velocityY.toDouble())
            // return gyro.getVelocityY()
        }

        fun getXAccel(): Double {
            return filter.calculate(gyro.worldLinearAccelX.toDouble())
            // return gyro.getWorldLinearAccelX()
        }

        fun getYAccel(): Double {
            return filter.calculate(gyro.worldLinearAccelX.toDouble())
            // return gyro.getWorldLinearAccelX()
        }

        fun calibrate() {
            gyro.calibrate()
        }
    }
}
