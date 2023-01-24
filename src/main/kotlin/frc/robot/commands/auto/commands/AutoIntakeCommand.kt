package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.IntakeSystem
import frc.robot.subsystems.TransportSystem

class AutoIntakeCommand(private val transportSystem: TransportSystem, private val intakeSystem: IntakeSystem,
                        private val frontBreakBeam: DigitalInput) : CommandBase() {
    private var storedCargoAtStart = 0

    init {
        storedCargoAtStart = transportSystem.cargoAmount
        addRequirements(intakeSystem)
    }

    override fun execute() {
        intakeSystem.intake(1.0)
    }

    override fun end(interrupted: Boolean) {
        println("frontBrokeIntake")
        transportSystem.move(0.0)
        intakeSystem.kill()
    }

    override fun isFinished(): Boolean {
        return !frontBreakBeam.get()
    }
}