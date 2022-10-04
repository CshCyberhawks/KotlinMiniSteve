package frc.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.ClimbSystem
import frc.robot.util.IO


class ClimbCommand : CommandBase {
    private var climbSystem: ClimbSystem? = null
    private val speedMult = 1.0

    constructor(subsystem: ClimbSystem?) {
        climbSystem = subsystem
        addRequirements(subsystem)
    }

    override fun execute() {
        // SmartDashboard.putNumber("climbControl", IO.climbControl())
        climbSystem!!.climb(IO.climbControl() * speedMult)
        if (IO.deployClimbSolenoid()) {
            climbSystem!!.controlPneumatics()
        }
    }
}