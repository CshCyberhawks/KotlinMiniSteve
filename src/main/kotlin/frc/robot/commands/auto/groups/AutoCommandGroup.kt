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
1 - One ball and shoot
 */

class AutoCommandGroup(configuration: Int) : SequentialCommandGroup() {
    private var blueAutoConfigurations: Map<Int, () -> Unit> = mapOf(0 to {
        Robot.swo.resetPos()
        Gyro.setOffset()
        addCommands(
            AutoShootCommand(Robot.shootSystem),
            AutoGoToPosition(FieldPosition(Vector2(0, 3), 0))
        )
    }, 1 to {
        Robot.swo.resetPos()
        Gyro.setOffset()
        addCommands(
            AutoBall(FieldPosition(Vector2(0, .4), 0)),
            AutoGoToCenterAndShoot()
        )
        }
    )

    private var redAutoConfigurations: Map<Int, () -> Unit> = mapOf(0 to {
        Robot.swo.resetPos()
        Gyro.setOffset()
        addCommands(
            AutoShootCommand(Robot.shootSystem),
            AutoGoToPosition(0, 3)
        )
    }, 1 to {
        addCommands(
            AutoBall(0)
        )
    }
    )

    init {
        if (DriverStation.getAlliance() == Alliance.Blue) {
            blueAutoConfigurations[configuration]?.invoke()
        } else {
            redAutoConfigurations[configuration]?.invoke()
        }
    }
}
