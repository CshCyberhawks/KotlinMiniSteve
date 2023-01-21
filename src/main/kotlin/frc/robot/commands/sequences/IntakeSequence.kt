package frc.robot.commands.sequences
//
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.commands.auto.commands.AutoIntakeCommand
import frc.robot.commands.auto.commands.AutoTransportCommand
import frc.robot.commands.auto.commands.LimeLightAuto
import frc.robot.util.IO

class IntakeSequence : CommandBase {
    // this gets called upon running intake on xbox controller
    var autoIntakeCommand: AutoIntakeCommand = AutoIntakeCommand(Robot.intakeSystem)
    private var autoTransportCommand: AutoTransportCommand =
            AutoTransportCommand(Robot.transportSystem)
    private var limeLightAuto: LimeLightAuto? = null

    constructor() : super()

    constructor(limeLightAuto: LimeLightAuto) : super() {
        this.limeLightAuto = limeLightAuto
    }

    init {
        Robot.transportSystem.isRunningSequence = true
        autoIntakeCommand.schedule()
    }

    override fun execute() {
        if (!autoIntakeCommand.isScheduled()) {
            autoTransportCommand.schedule()
        }
    }

    override fun end(interrupted: Boolean) {
        println("intakeSequenceEnd")
        Robot.transportSystem.isRunningSequence = false

        if (this.limeLightAuto != null) {
            limeLightAuto!!.cancel()
        }
    }

    override fun isFinished(): Boolean {
        if (this.autoIntakeCommand.isFinished) {
            if (this.limeLightAuto != null) {
                limeLightAuto!!.cancel()
            }
        }
        if (!autoTransportCommand.isFinished) {
            if (IO.getAutoIntakeCancel()) {
                println("manually canceled sequence")
                autoIntakeCommand.cancel()
                autoTransportCommand.cancel()
                return true
            }
            return IO.getAutoIntakeCancel()
        }
        return autoTransportCommand.isFinished
    }
}
