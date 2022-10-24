package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Robot
import frc.robot.commands.auto.commands.AutoBall
import frc.robot.commands.auto.commands.AutoGoToPosition
import frc.robot.util.Gyro
import frc.robot.util.Vector2

/*
Autonomous Configs:
0 - Drive Straight Backwards
1 - Main swerve configuration
 */

class AutoCommandGroup(configuration: Int) : SequentialCommandGroup() {
    private var autoConfigurations: Map<Int, () -> Unit> = mapOf(0 to {
        Robot.swo.resetPos()
        Gyro.setOffset()
        addCommands(
            AutoGoToPosition(Vector2(0.0, -5.0), 0.0)
        )
    }, 1 to {
        addCommands(
            AutoBall(0)
        )
    })

    init {
        autoConfigurations[configuration]?.invoke()

    }
}
