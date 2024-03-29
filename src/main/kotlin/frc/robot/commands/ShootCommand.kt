package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.commands.auto.commands.AutoShootCommand
import frc.robot.subsystems.ShootSystem
import frc.robot.util.IO


class ShootCommand(private val shootSystem: ShootSystem) : CommandBase() {
    init {
        addRequirements(shootSystem)
    }

    override fun execute() {
        // if (IO.raiseShootSpeed()) shootSystem!!.shootMult += 0.05
        // if (IO.lowerShootSpeed()) shootSystem!!.shootMult -= 0.05
        if (IO.autoShoot()) {
            AutoShootCommand(shootSystem).schedule()
        } else if (!shootSystem.autoShootRunning) {
            shootSystem.shoot(IO.shootBall() != 0.0)
        }
    }
}