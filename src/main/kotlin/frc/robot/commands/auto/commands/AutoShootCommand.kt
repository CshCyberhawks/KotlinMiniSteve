package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Constants
import frc.robot.subsystems.ShootSystem
import frc.robot.subsystems.TransportSystem
import kotlin.math.abs


class AutoShootCommand(private val transportSystem: TransportSystem, var shootSystem: ShootSystem) : CommandBase() {
    init {
        shootSystem.autoShootRunning = true
        addRequirements(shootSystem)
    }

    override fun execute() {
        val currentTopEncoderSpeed = shootSystem.topWheelSpeed
        val currentBottomEncoderSpeed = shootSystem.bottomWheelSpeed

        // if (!Robot.getShootBreakBeam().get())
        // transportSystem.setCargoAmount(transportSystem.getCargoAmount() - 1)
        if (abs(currentTopEncoderSpeed) > abs(Constants.topShootSetpoint) && abs(currentBottomEncoderSpeed) > abs(
                        Constants.bottomShootSetpoint
                )
        ) {
            transportSystem.move(0.5)
        }
        shootSystem.shoot(true)
    }

    override fun end(interrupted: Boolean) {
        shootSystem.shoot(false)
        transportSystem.move(0.0)
        shootSystem.autoShootRunning = false
    }

    override fun isFinished(): Boolean {
        return transportSystem.cargoAmount <= 0
        // lastShootTime =
        // if (transportSystem.cargoAmount <= 0 && lastShootTime == 0.0) MathClass.getCurrentTime() else lastShootTime
        // val cargoReturn = transportSystem.cargoAmount <= 0 && MathClass.getCurrentTime() - lastShootTime > 0.1
        // SmartDashboard.putNumber("time", MathClass.getCurrentTime() - startTime)
        // SmartDashboard.putBoolean(
        //     "shoot command done",
        //     transportSystem.cargoAmount <= 0 && cargoReturn || IO.getAutoShootCancel() || MathClass.getCurrentTime() - startTime > 4.0
        // )
        // return (transportSystem.cargoAmount <= 0 && cargoReturn || IO.getAutoShootCancel() || MathClass.getCurrentTime() - startTime > 4.0)
    }
}
