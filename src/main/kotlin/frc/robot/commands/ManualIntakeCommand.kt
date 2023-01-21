package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.IntakeSystem
import frc.robot.util.IO

class ManualIntakeCommand(private val intakeSystem: IntakeSystem) : CommandBase() {
    // private double speedMult
    // private double speedMult

    // private var intakeCommandSequence: IntakeSequence = IntakeSequence()

    init {
        addRequirements(intakeSystem)
    }

    override fun execute() {
        val speed = IO.intakeBall()

        intakeSystem.intakeSequenceShuffle.setBoolean(!Robot.transportSystem.isRunningSequence)

        if (IO.autoIntake()) {
//            Robot.isSpitting = false
//            val intakeCommandSequence = IntakeSequence()
//            intakeCommandSequence.schedule()
            // SmartDashboard.putBoolean("intakeSequenceBegan", true)
        } else if (!Robot.transportSystem.isRunningSequence)
            if (IO.removeBall()) {
                Robot.isSpitting = true
                intakeSystem.intake(-1.0)
                Robot.transportSystem.move(-1.0)
            } else {
                Robot.isSpitting = false
                intakeSystem.intake(speed)
            }
    }
}
