package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.TransportSystem
import frc.robot.util.IO
import kotlin.math.max
import kotlin.math.min


class ManualTransportCommand(private var transportSystem: TransportSystem) : CommandBase() {

    init {
        addRequirements(transportSystem)
    }

    override fun execute() {
        if (!transportSystem.isRunningSequence && !Robot.isSpitting && !Robot.shootSystem.autoShootRunning) {
            transportSystem.move(-IO.moveTransport())
        }
        if (IO.getResetCargo()) {
            transportSystem.cargoAmount = 0
        }
        if (IO.increaseCargoAmount()) {
            transportSystem.cargoAmount++
        }
        if (IO.decreaseCargoAmount()) {
            transportSystem.cargoAmount--
        }
        transportSystem.cargoAmount = max(transportSystem.cargoAmount, 0)
        transportSystem.cargoAmount = min(transportSystem.cargoAmount, 2)
    }
}