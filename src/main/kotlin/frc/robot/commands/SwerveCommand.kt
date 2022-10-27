package frc.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.commands.auto.commands.LimeLightAuto
import frc.robot.commands.sequences.IntakeSequence
import frc.robot.subsystems.SwerveDriveTrain
import frc.robot.util.DriveState
import frc.robot.util.Gyro
import frc.robot.util.IO


class SwerveCommand(private var swerveDriveTrain: SwerveDriveTrain) : CommandBase() {
    private var intakeSequence: IntakeSequence? = null

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

        if (IO.cancelLimelightLockOn()) {
            intakeSequence?.cancel()
        }

        if (IO.limelightLockOn()){
            if (intakeSequence != null) {
                intakeSequence!!.cancel()
            }
            val limeLightAuto: LimeLightAuto = LimeLightAuto()
            intakeSequence = IntakeSequence(limeLightAuto)
            CommandScheduler.getInstance().schedule(intakeSequence)
            CommandScheduler.getInstance().schedule(limeLightAuto)
        }
        swerveDriveTrain.drive(
            -IO.moveRobotX(), -IO.moveRobotY(), -IO.turnControl(), IO.getJoyThrottle(), DriveState.TELE
        )
    }
}
