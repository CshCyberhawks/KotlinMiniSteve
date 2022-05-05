package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.IntakeSystem


class AutoIntakeCommand : CommandBase {
    private var intakeSystem: IntakeSystem? = null
    private var storedCargoAtStart = 0

    constructor(subsystem: IntakeSystem?) {
        intakeSystem = subsystem
        storedCargoAtStart = Robot.transportSystem!!.cargoAmount
        addRequirements(subsystem)
    }

    override fun execute() {
        intakeSystem!!.intake(1.0)
    }

    override fun end(interrupted: Boolean) {
        println("frontBrokeIntake")
        Robot.transportSystem!!.move(0.0)
        Robot.intakeSystem!!.kill()
    }

    override fun isFinished(): Boolean {
        return !Robot.frontBreakBeam!!.get()
    }
}