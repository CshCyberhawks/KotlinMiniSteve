package frc.robot.commands.sequences

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Robot
import frc.robot.commands.auto.commands.AutoIntakeCommand
import frc.robot.commands.auto.commands.AutoTransportCommand
import frc.robot.util.IO


class IntakeSequence : SequentialCommandGroup() {
    // this gets called upon running intake on xbox controller
    var autoIntakeCommand: AutoIntakeCommand = AutoIntakeCommand(Robot.intakeSystem)
    private var autoTransportCommand: AutoTransportCommand = AutoTransportCommand(Robot.transportSystem)

    init {
        Robot.transportSystem.isRunningSequence = true
        addCommands(
            autoIntakeCommand, autoTransportCommand
        )
    }

    override fun end(interrupted: Boolean) {
        println("intakeSequenceEnd")
        Robot.transportSystem.isRunningSequence = false
    }

    override fun isFinished(): Boolean {
        if (!autoTransportCommand.isFinished) {
            if (IO.getAutoIntakeCancel()) {
                println("manually canceled sequence")
            }
            return IO.getAutoIntakeCancel()
        }
        return autoTransportCommand.isFinished
    }
}
