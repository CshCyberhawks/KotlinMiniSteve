package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.commands.auto.TransportInfo
import frc.robot.subsystems.IntakeSystem
import frc.robot.subsystems.TransportSystem
import frc.robot.util.IO

class ManualIntakeCommand(private val intakeSystem: IntakeSystem, private val transportSystem: TransportSystem,
                          private val transportInfo: TransportInfo) : CommandBase() {
    // private double speedMult
    // private double speedMult

    // private var intakeCommandSequence: IntakeSequence = IntakeSequence()

    init {
        addRequirements(intakeSystem)
    }

    override fun execute() {
        val speed = IO.intakeBall()

        intakeSystem.intakeSequenceShuffle.setBoolean(!transportSystem.isRunningSequence)

        if (IO.autoIntake()) {
//            Robot.isSpitting = false
//            val intakeCommandSequence = IntakeSequence()
//            intakeCommandSequence.schedule()
            // SmartDashboard.putBoolean("intakeSequenceBegan", true)
        } else if (!transportSystem.isRunningSequence)
            if (IO.removeBall()) {
                transportInfo.isSpitting = true
                intakeSystem.intake(-1.0)
                transportSystem.move(-1.0)
            } else {
                transportInfo.isSpitting = false
                intakeSystem.intake(speed)
            }
    }
}
