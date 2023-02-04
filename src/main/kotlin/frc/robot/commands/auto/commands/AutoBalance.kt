package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.Robot
import frc.robot.util.Gyro
import frc.robot.util.IO
import frc.robot.util.Vector2
import frc.robot.util.MathClass
import frc.robot.util.Polar

class AutoBalance : CommandBase {

    lateinit var pos: AutoGoToPosition

    constructor() : super() {}

    override fun initialize() {
        setPos()
        pos.schedule()
    }

    fun setPos() {
        val pitchRoll: Vector2 = Gyro.mergePitchRoll()
        val negPitchRoll: Vector2 = MathClass.polarToCartesian(Polar(-MathClass.cartesianToPolar(pitchRoll).theta, 0.1))
        val robotPosMeters: Vector2 = Vector2(MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x), MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y))
        val position: Vector2 = robotPosMeters + negPitchRoll
        pos = AutoGoToPosition(position, 0.0)
        SmartDashboard.putNumber("AutoBalance Position X", position.x)
        SmartDashboard.putNumber("AutoBalance Position Y", position.y)
    }

    override fun execute() {
        if (pos.isScheduled == false || pos.isFinished()) {
            setPos()
            pos.schedule()
        }
    }

    override fun end(interrupted: Boolean) {
        Robot.swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        return IO.autoBalance()
    }
}
