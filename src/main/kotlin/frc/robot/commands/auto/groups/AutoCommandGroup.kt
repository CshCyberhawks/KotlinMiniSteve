package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Robot
import frc.robot.util.Gyro

/*
Autonomous Configs:
0 - Drive Straight Backwards
1 - Main swerve configuration
 */

class AutoCommandGroup(configuration: Int) : SequentialCommandGroup() {
    private var autoConfigurations: Map<Int, () -> Unit> =
            mapOf(
                    0 to
                            {
                                Robot.swo.resetPos()
                                Gyro.setOffset()
                                addCommands()
                            },
                    1 to
                            {
                                Robot.swo.resetPos()
                                Gyro.setOffset()
                                addCommands()
                            }
            )

    init {
        autoConfigurations[configuration]?.invoke()
    }
}
