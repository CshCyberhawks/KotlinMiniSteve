package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.TransportSystem

class AutoTransportCommand(private var transportSystem: TransportSystem, private val backBreakBeam: DigitalInput) :
        CommandBase() {
    private var cargoStored = transportSystem.cargoAmount
    private var hitBackBreak = false

    init {
        addRequirements(transportSystem)
    }

    override fun execute() {
        transportSystem.move(0.25)
    }

    override fun end(interrupted: Boolean) {
        // if (transportSystem.getCargoAmount() < 2 && interrupted == false)
        // transportSystem.setCargoAmount(transportSystem.getCargoAmount() + 1)
        transportSystem.move(0.0)
    }

    override fun isFinished(): Boolean {
        val backBeam: Boolean = backBreakBeam.get()
        hitBackBreak = hitBackBreak || !backBeam
        return if (cargoStored < 1) !backBeam else backBeam && hitBackBreak
    }
}