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

    var roll: Double = 0.0
    var pitch: Double = 0.0

    val deadzone: Double = 3.0

    constructor() : super() {}

    override fun initialize() {
        setPos()
        pos.schedule()
    }

    fun setPos() {
        val pitchRoll: Vector2 = Gyro.mergePitchRoll(1.1)
        val negPitchRoll: Vector2 = MathClass.polarToCartesian(Polar(-MathClass.cartesianToPolar(pitchRoll).theta, 0.3))
        val robotPosMeters: Vector2 = Vector2(MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.x), MathClass.swosToMeters(Robot.swo.getPosition().positionCoord.y))
        val position: Vector2 = robotPosMeters + negPitchRoll
        pos = AutoGoToPosition(position, 0.0)
        roll = Gyro.getRoll()
        pitch = Gyro.getPitch()
        SmartDashboard.putNumber("AutoBalance Position X", position.x)
        SmartDashboard.putNumber("AutoBalance Position Y", position.y)
    }

    override fun execute() {
        if (pos.isScheduled == false || pos.isFinished()) {
            setPos()
            pos.schedule()
        }
        else if (MathClass.calculateDeadzone(Gyro.getRoll() - roll, deadzone) != 0.0 || MathClass.calculateDeadzone(Gyro.getPitch() - pitch, deadzone) != 0.0) {
            pos.cancel()
            setPos()
            pos.schedule()
        }
    }

    override fun end(interrupted: Boolean) {
        Robot.swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        SmartDashboard.putNumber("rollPitchMag", MathClass.cartesianToPolar(Gyro.mergePitchRoll(1.1)).r)
        return MathClass.cartesianToPolar(Gyro.mergePitchRoll(1.1)).r < 6.0
    }
}
