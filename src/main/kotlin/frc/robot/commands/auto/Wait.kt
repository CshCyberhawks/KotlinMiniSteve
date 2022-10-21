package frc.robot.commands.auto

import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.util.MathClass


class Wait(private var length: Double) : CommandBase() {
    private var startTime = 0.0

    override fun initialize() {
        startTime = WPIUtilJNI.now() * 1.0e-6
    }

    override fun execute() {}

    override fun end(interrupted: Boolean) {}

    override fun isFinished(): Boolean {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        return MathClass.calculateDeadzone(timeNow - startTime - length, .05) == 0.0
    }
}