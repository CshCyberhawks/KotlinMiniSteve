package frc.robot.util

import com.kauailabs.navx.frc.AHRS
import edu.wpi.first.math.filter.LinearFilter
import edu.wpi.first.wpilibj.SPI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard


class Gyro {
    companion object { // Makes the class static
        private val gyro: AHRS = AHRS(SPI.Port.kMXP)
        private var offset: Double = 0.0
        private val filter: LinearFilter = LinearFilter.highPass(0.1, 0.02)

        private fun wrapAroundAngles(input: Double): Double {
            return if (input < 0) 360 + input else input
        }

        fun getAngle(): Double {
            // SmartDashboard.putNumber("Gyro raw", gyro.yaw - offset)
            return wrapAroundAngles(wrapAroundAngles(gyro.yaw.toDouble()) - offset)
            // return gyro.getYaw()
        }

        fun isConnected(): Boolean {
            return gyro.isConnected
        }

        fun reset() {
            gyro.reset()
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
