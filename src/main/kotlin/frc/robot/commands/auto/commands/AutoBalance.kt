package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot
import frc.robot.util.Gyro
import frc.robot.util.IO
import frc.robot.util.MathClass
import frc.robot.util.Vector2

class AutoBalance : CommandBase {

    var pos: AutoGoToPosition? = null

    constructor() : super() {}

    override fun initialize() {}

    override fun execute() {
        val deadzonePitch = MathClass.calculateDeadzone(Gyro.getPitch(), 2.0)
        val deadzoneRoll = MathClass.calculateDeadzone(Gyro.getRoll(), 2.0)

        // if (pos?.isScheduled == false) {
        //     if (deadzonePitch != 0.0) {
        //         if (deadzonePitch < 0) {
        //             pos = AutoGoToPosition(Vector2(1.0, 0.0), 0.0)
        //         } else {
        //             pos = AutoGoToPosition(Vector2(-1.0, 0.0), 0.0)
        //         }
        //     } else if (deadzoneRoll != 0.0) {
        //         if (deadzoneRoll < 0) {
        //             pos = AutoGoToPosition(Vector2(1.0, 0.0), 0.0)
        //         } else {
        //             pos = AutoGoToPosition(Vector2(-1.0, 0.0), 0.0)
        //         }
        //     }
        //     pos?.schedule()
        // }
    }

    override fun end(interrupted: Boolean) {
        Robot.swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        return IO.autoBalance()
    }
}
