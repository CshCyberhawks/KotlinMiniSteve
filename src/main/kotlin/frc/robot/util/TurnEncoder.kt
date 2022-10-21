package frc.robot.util

import edu.wpi.first.wpilibj.AnalogInput
import frc.robot.Constants

class TurnEncoder(private val port: Int) {
    private var encoder: AnalogInput = AnalogInput(port)

    private fun voltageToDegrees(input: Double): Double {
        return input / (2.5 / 180)
    }

    fun get(): Double {
        // return
        // (double)Math.floor(filter.calculate(voltageToDegrees(encoder.getVoltage()) -
        // Constants.turnEncoderOffsets[encoderPort]) * 10d) / 10d;
        // return
        // (double)Math.floor(filter.calculate(voltageToDegrees(encoder.getVoltage())) *
        // 10d) / 10d;
//        SmartDashboard.putNumber("Turn Encoder " + encoderPort, voltageToDegrees(encoder!!.voltage) - Constants.turnEncoderOffsets[encoderPort])
        return voltageToDegrees(encoder.voltage) - Constants.turnEncoderOffsets[port]
    }

    fun getRaw(): Double {
        return voltageToDegrees(encoder.voltage)
    }
}