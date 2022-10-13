package frc.robot.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.subsystems.Limelight
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
        Gyro.setOffset()
    }

    // Called every time the scheduler runs while the command is scheduled.
    override fun execute() {
        if (IO.getSWOReset()) Robot.swo.resetPos()
        Robot.swo.getPosition()
        if (IO.resetGyro()) Gyro.setOffset()
        if (IO.limelightLockOn()) swerveDriveTrain.drive(
            -IO.moveRobotX(),
            -IO.moveRobotY(),
            -MathClass.calculateDeadzone(Robot.limelight.getHorizontalOffset(), .5) / 27,
            IO.getJoyThrottle(),
            DriveState.TELE
        ) else swerveDriveTrain.drive(
            -IO.moveRobotX(),
            -IO.moveRobotY(),
            -IO.turnControl(),
            IO.getJoyThrottle(),
            DriveState.TELE
        )
    }
}