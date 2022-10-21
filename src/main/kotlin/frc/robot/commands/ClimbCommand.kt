package frc.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.ClimbSystem
import frc.robot.util.IO


class ClimbCommand(private val climbSystem: ClimbSystem) : CommandBase() {
    private val speedMult = 1.0

    init {
        addRequirements(climbSystem)
    }

    override fun execute() {
        // SmartDashboard.putNumber("climbControl", IO.climbControl())
        climbSystem.climb(IO.climbControl() * speedMult)
        if (IO.deployClimbSolenoid()) {
            climbSystem.controlPneumatics()
        }
    }
}