package frc.robot.util

import com.ctre.phoenix.motorcontrol.can.TalonFX


class DriveEncoder(private var driveMotor: TalonFX) { // This is a primary constructor in Kotlin
    fun getVelocity(): Double {
        return driveMotor.selectedSensorVelocity / 204.8
    }

    fun getPosition(): Double {
        return driveMotor.selectedSensorPosition / 2048.0
    }
}