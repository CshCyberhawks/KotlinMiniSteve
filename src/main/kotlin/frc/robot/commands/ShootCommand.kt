package frc.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.commands.auto.commands.AutoShootCommand
import frc.robot.subsystems.ShootSystem
import frc.robot.util.IO


class ShootCommand : CommandBase {
    private var shootSystem: ShootSystem? = null
    private var shootCommand: AutoShootCommand? = null
    private val speedMult = .55

    constructor(subsystem: ShootSystem?) {
        shootSystem = subsystem
        addRequirements(subsystem)
    }

    override fun execute() {
        // if (IO.raiseShootSpeed()) shootSystem!!.shootMult += 0.05
        // if (IO.lowerShootSpeed()) shootSystem!!.shootMult -= 0.05
        if (IO.autoShoot()) {
            shootCommand = AutoShootCommand(shootSystem)
            shootCommand!!.schedule()
        } else if (!shootSystem!!.getAutoShootState()) {
            SmartDashboard.putNumber("Speed Mult", speedMult)
            shootSystem!!.shoot(IO.shootBall())
        }
    }
}