package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.ShootSystem
import frc.robot.subsystems.TransportSystem
import frc.robot.util.IO
import frc.robot.util.MathClass


class AutoShootCommand : CommandBase {
    private var shootSystem: ShootSystem? = null
    private var transportSystem: TransportSystem? = null
    private val desiredShootSpeed = 19.0
    private var startTime = 0.0
    private var lastShootTime = 0.0

    constructor(subsystem: ShootSystem?) {
        shootSystem = subsystem
        shootSystem!!.setAutoShootState(true)
        transportSystem = Robot.transportSystem
        startTime = MathClass.getCurrentTime()
        addRequirements(subsystem)
    }

    override fun execute() {
        val currentTopEncoderSpeed = shootSystem!!.topWheelSpeed

        // if (!Robot.getShootBreakBeam().get())
        // transportSystem.setCargoAmount(transportSystem.getCargoAmount() - 1);
        if (Math.abs(currentTopEncoderSpeed) > Math.abs(desiredShootSpeed)) {
            transportSystem!!.move(.5)
        }
        shootSystem!!.shoot(1.0)
    }

    override fun end(interrupted: Boolean) {
        shootSystem!!.shoot(0.0)
        transportSystem!!.move(0.0)
        shootSystem!!.setAutoShootState(false)
    }

    override fun isFinished(): Boolean {
        lastShootTime =
            if (transportSystem!!.cargoAmount <= 0 && lastShootTime == 0.0) MathClass.getCurrentTime() else lastShootTime
        val cargoReturn = transportSystem!!.cargoAmount <= 0 && MathClass.getCurrentTime() - lastShootTime > .1
        SmartDashboard.putBoolean(
            "shoot command done", transportSystem!!.cargoAmount <= 0 && cargoReturn || IO.getAutoShootCancel()
                    || MathClass.getCurrentTime() - startTime > 4
        )
        return (transportSystem!!.cargoAmount <= 0 && cargoReturn || IO.getAutoShootCancel()
                || MathClass.getCurrentTime() - startTime > 4)
    }
}