package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.subsystems.ShootSystem
import frc.robot.subsystems.TransportSystem
import frc.robot.util.IO
import frc.robot.util.MathClass
import kotlin.math.abs


class AutoShootCommand(private var shootSystem: ShootSystem) : CommandBase() {
    private var transportSystem: TransportSystem = Robot.transportSystem
    private var startTime = 0.0
    private var lastShootTime = 0.0

    init {
        shootSystem.autoShootRunning = true
        startTime = MathClass.getCurrentTime()
        addRequirements(shootSystem)
    }

    override fun execute() {
        val currentTopEncoderSpeed = shootSystem.topWheelSpeed

        // if (!Robot.getShootBreakBeam().get())
        // transportSystem.setCargoAmount(transportSystem.getCargoAmount() - 1)
        if (abs(currentTopEncoderSpeed) > abs(Constants.topShootSetpoint)) {
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
        lastShootTime =
            if (transportSystem.cargoAmount <= 0 && lastShootTime == 0.0) MathClass.getCurrentTime() else lastShootTime
        val cargoReturn = transportSystem.cargoAmount <= 0 && MathClass.getCurrentTime() - lastShootTime > 0.1
        SmartDashboard.putBoolean(
            "shoot command done",
            transportSystem.cargoAmount <= 0 && cargoReturn || IO.getAutoShootCancel() || MathClass.getCurrentTime() - startTime > 4.0
        )
        return (transportSystem.cargoAmount <= 0 && cargoReturn || IO.getAutoShootCancel() || MathClass.getCurrentTime() - startTime > 4.0)
    }
}