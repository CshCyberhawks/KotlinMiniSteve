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
    private var blueAutoConfigurations: Map<Int, () -> Unit> = mapOf(0 to {
        Robot.swo.resetPos()
        Gyro.setOffset()
        addCommands(
            AutoBall(4),
            AutoGoToCenterAndShoot(0, true),
            AutoBall(5),
            AutoGoToCenterAndShoot(0, true)
        )
    }, 1 to {
                addCommands(
                    AutoBall(0)
                )
        }
    )

    private var redAutoConfigurations: Map<Int, () -> Unit> = mapOf(0 to {
        Robot.swo.resetPos()
        Gyro.setOffset()
        addCommands(
            
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
