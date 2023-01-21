package frc.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.subsystems.SwerveDriveTrain
import frc.robot.util.DriveState
import frc.robot.util.Gyro
import frc.robot.util.IO
import frc.robot.util.MathClass


class SwerveCommand(private var swerveDriveTrain: SwerveDriveTrain) : CommandBase() {
    init {
        addRequirements(swerveDriveTrain)
    }

    // Called when the command is initially scheduled.
    override fun initialize() {
//        Gyro.setOffset()
    }

    // Called every time the scheduler runs while the command is scheduled.
    override fun execute() {
        if (IO.getSWOReset()) Robot.swo.resetPos()
        Robot.swo.getPosition()
        if (IO.resetGyro()) Gyro.setOffset()

        if (IO.getFastThrottle()) {
            swerveDriveTrain.throttle = 0.9
        }

        if (IO.getNormalThrottle()) {
            swerveDriveTrain.throttle = 0.4
        }

        val quickThrottle = IO.getQuickThrottle()
        SmartDashboard.putNumber("Quick Throttle", quickThrottle.toDouble())
        if (quickThrottle in 135..225) {
            swerveDriveTrain.throttle -= Constants.quickThrottleChange
        } else if (quickThrottle == 315 || quickThrottle == 45 || quickThrottle == 0) {
            swerveDriveTrain.throttle += Constants.quickThrottleChange
        }

        val angle = if (IO.limelightLockOn()) MathClass.calculateDeadzone(
                -Robot.limelight.getHorizontalOffset(),
                .5
        ) / 32 else IO.turnControl()

        swerveDriveTrain.drive(
                -IO.moveRobotX(),
                -IO.moveRobotY(),
                -angle,
                IO.getJoyThrottle(),
                DriveState.TELE,
                !IO.disableFieldOrientation()
        )
    }
}
