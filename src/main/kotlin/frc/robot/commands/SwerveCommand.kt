package frc.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.commands.auto.commands.LimeLightAuto
import frc.robot.commands.sequences.IntakeSequence
import frc.robot.subsystems.SwerveDriveTrain
import frc.robot.util.DriveState
import frc.robot.util.Gyro
import frc.robot.util.IO
import frc.robot.util.Vector2


class SwerveCommand(private var swerveDriveTrain: SwerveDriveTrain) : CommandBase() {
    private var limeLightAuto: LimeLightAuto? = null
    private var intakeSequence: IntakeSequence? = null
    private var firstRun: Boolean = true

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

        if (IO.cancelLimelightLockOn()) {
            limeLightAuto?.cancel()
            intakeSequence?.cancel()
        }

        if (intakeSequence?.isFinished == true && limeLightAuto?.isFinished == false) {
            limeLightAuto?.cancel()
        }

        SmartDashboard.putBoolean("Limelight Auto Finished", limeLightAuto?.isFinished == true)
        SmartDashboard.putBoolean("Intake Auto Finished", intakeSequence?.isFinished == true)
        SmartDashboard.putBoolean(
            "Limelight Scheduled",
            IO.limelightLockOn() && (limeLightAuto?.isFinished == true || firstRun)
        )
        if (IO.limelightLockOn() && ((limeLightAuto?.isFinished == true && intakeSequence?.isFinished == true) || firstRun)) {
            limeLightAuto = LimeLightAuto()
            limeLightAuto?.schedule()
            intakeSequence = IntakeSequence()
            intakeSequence?.schedule()
            firstRun = false
        }
        swerveDriveTrain.drive(
            Vector2(
                -IO.moveRobotX(),
                -IO.moveRobotY()
            ),
            -IO.turnControl(),
            IO.getJoyThrottle(),
            DriveState.TELE
        )
    }
}