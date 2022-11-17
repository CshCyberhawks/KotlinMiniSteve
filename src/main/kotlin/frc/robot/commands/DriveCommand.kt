package frc.robot.commands

import edu.wpi.first.math.MathUtil
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.subsystems.DriveSystem
import frc.robot.util.IO

/**
 * @property driveSystem
 */
class DriveCommand(private val driveSystem: DriveSystem) : CommandBase() {
    /**
     * Creates a new ExampleCommand.
     */
    init {
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(driveSystem)
    }

    // Called when the command is initially scheduled.
    override fun initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    override fun execute() {
        val throttle: Double = (IO.getThrottle() - 1) * -.5
        val forwardSpeed: Double = IO.getY()
        val turnSpeed: Double = IO.getZ()

        val leftSpeed: Double = MathUtil.clamp(forwardSpeed * throttle - turnSpeed * .5 * Math.sqrt(throttle), -1.0, 1.0)
        val rightSpeed: Double = MathUtil.clamp(forwardSpeed * throttle + turnSpeed * .5 * Math.sqrt(throttle), -1.0, 1.0)
        driveSystem.setSpeed(leftSpeed, rightSpeed)
    }

    // Called once the command ends or is interrupted.
    override fun end(interrupted: Boolean) {}

    // Returns true when the command should end.
    override fun isFinished(): Boolean {
        return false
    }
}
