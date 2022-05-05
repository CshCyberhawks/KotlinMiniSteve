package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.TransportSystem

class AutoTransportCommand : CommandBase {
    private var transportSystem: TransportSystem? = null
    private var cargoStored = 0
    private var hitBackBreak = false

    constructor(transportSystem: TransportSystem) {
        this.transportSystem = transportSystem
        cargoStored = transportSystem.cargoAmount
        hitBackBreak = false
        addRequirements(transportSystem)
    }

    override fun execute() {
        transportSystem!!.move(.25)
    }

    override fun end(interrupted: Boolean) {
        // if (transportSystem.getCargoAmount() < 2 && interrupted == false)
        // transportSystem.setCargoAmount(transportSystem.getCargoAmount() + 1);
        transportSystem!!.move(0.0)
    }

    override fun isFinished(): Boolean {
        val backBeam: Boolean = Robot.backBreakBeam!!.get()
        hitBackBreak = if (!hitBackBreak) !backBeam else hitBackBreak
        return if (cargoStored < 1) !backBeam else backBeam && hitBackBreak
    }
}