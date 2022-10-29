package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.commands.auto.commands.AutoBall
import frc.robot.commands.auto.commands.AutoGoToPosition
import frc.robot.commands.auto.commands.AutoShootCommand
import frc.robot.util.Gyro

/*
Autonomous Configs:
0 - Drive Straight Backwards
1 - One ball and shoot
 */

class AutoCommandGroup(configuration: Int, startingPos: Int) : SequentialCommandGroup() {

    private var startingPosZero: Map<Int, () -> Unit> =
            mapOf(
                    0 to
                            {
                                Robot.swo.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoShootCommand(Robot.shootSystem),
                                        AutoGoToPosition(Constants.taxiPositionsZero[0], 0.0)
                                )
                            },
                    1 to
                            {
                                Robot.swo.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoBall(4, 15.0),
                                        AutoGoToCenterAndShoot(0, true),
                                        AutoGoToPosition(Constants.taxiPositionsZero[0], 0.0)
                                )
                            },
                    2 to
                            {
                                Robot.swo.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoBall(4, 15.0),
                                        AutoGoToCenterAndShoot(0, true),
                                        AutoBall(6, 90.0),
                                        AutoGoToCenterAndShoot(0, true),
                                        AutoGoToPosition(Constants.taxiPositionsZero[0], 0.0)
                                )
                            },
                    3 to
                            {
                                Robot.swo.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoBall(4, 15.0),
                                        AutoGoToCenterAndShoot(0, true),
                                        AutoBall(5, 75.0),
                                        AutoBall(6, 75.0),
                                        AutoGoToCenterAndShoot(0, true),
                                        AutoGoToPosition(Constants.taxiPositionsZero[0], 0.0)
                                )
                            }
            )

    private var startingPosOne: Map<Int, () -> Unit> =
            mapOf(
                    0 to
                            {
                                Robot.swo.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                    AutoBall(0, 15.0),
                                    AutoGoToCenterAndShoot(1, true),
                                    AutoGoToPosition(Constants.taxiPositionsOne[0], 0.0)
                                )
                            },
            )

    init {
        if (startingPos == 0) {
            startingPosZero[configuration]?.invoke()
        } else if (startingPos == 1) {
            startingPosOne[configuration]?.invoke()
        }
    }
}
