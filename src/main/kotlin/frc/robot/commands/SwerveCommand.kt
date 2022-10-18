package frc.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.Robot
import frc.robot.commands.auto.commands.LimeLightAuto
import frc.robot.subsystems.SwerveDriveTrain
import frc.robot.util.DriveState
import frc.robot.util.Gyro
import frc.robot.util.IO


class SwerveCommand(private var swerveDriveTrain: SwerveDriveTrain) : CommandBase() {
    private var limeLightAuto: LimeLightAuto = LimeLightAuto()

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
        SmartDashboard.putBoolean("Limelight Auto Finished", limeLightAuto.isFinished())
        if (IO.limelightLockOn() && Robot.swerveAuto.isFinishedMoving()) {
            CommandScheduler.getInstance().schedule(limeLightAuto)
        }
        swerveDriveTrain.drive(
            -IO.moveRobotX(),
            -IO.moveRobotY(),
            -IO.turnControl(),
            IO.getJoyThrottle(),
            DriveState.TELE
        )
    }
}