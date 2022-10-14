package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.TransportSystem
import frc.robot.util.IO


class ManualTransportCommand(private var transportSystem: TransportSystem) : CommandBase() {

    init {
        addRequirements(transportSystem)
    }

    override fun execute() {
        val transportPower = -IO.moveTransport()
        if (!transportSystem.isRunningSequence && !Robot.isSpitting && !Robot.shootSystem.getAutoShootState()) transportSystem.move(
            transportPower
        )
        if (IO.getResetCargo()) {
            transportSystem.cargoAmount = 0
        }
    }
}