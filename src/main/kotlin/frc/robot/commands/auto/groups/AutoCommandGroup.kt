package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Constants
import frc.robot.Robot.Companion.limelight
import frc.robot.Robot.Companion.swerveAutoInfo
import frc.robot.Robot.Companion.transportSystem
import frc.robot.commands.auto.SwerveAutoInfo
import frc.robot.commands.auto.commands.AutoBall
import frc.robot.commands.auto.commands.AutoGoToPosition
import frc.robot.commands.auto.commands.AutoShootCommand
import frc.robot.commands.auto.commands.LimeLightAuto
import frc.robot.subsystems.*
import frc.robot.util.Gyro

/*
Autonomous Configs:
0 - Drive Straight Backwards
1 - One ball and shoot
 */

class AutoCommandGroup(odometry: SwerveOdometry, shootSystem: ShootSystem, transportSystem: TransportSystem,
                       swerveAuto: SwerveAuto, swerveAutoInfo: SwerveAutoInfo, limelight: Limelight, configuration: Int,
                       startingPos: Int) : SequentialCommandGroup() {

    private var startingPosZero: Map<Int, () -> Unit> =
            mapOf(
                    0 to
                            {
                                odometry.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoShootCommand(transportSystem, shootSystem),
                                        AutoGoToPosition(swerveAuto, Constants.taxiPositionsZero[0], 0.0)
                                )
                            },
                    1 to
                            {
                                odometry.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem, 0, true),
                                        AutoBall(swerveAuto, 4, 0.0),
                                        AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem, 0, true),
                                        AutoGoToPosition(swerveAuto, Constants.taxiPositionsZero[0], 0.0)
                                )
                            },
                    2 to
                            {
                                odometry.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoBall(swerveAuto, 4, 0.0),
                                        AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem, 0, true),
                                        AutoBall(swerveAuto, 6, 50.0),
                                        AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem, 0, true),
                                        AutoGoToPosition(swerveAuto, Constants.taxiPositionsZero[0], 0.0)
                                )
                            },
                    3 to
                            {
                                odometry.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoBall(swerveAuto, 4, 0.0),
                                        AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem,0, true),
                                        AutoBall(swerveAuto, 5, 45.0),
                                        AutoBall(swerveAuto, 6, 50.0),
                                        AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem,0, true),
                                        AutoGoToPosition(swerveAuto, Constants.taxiPositionsZero[0], 0.0)
                                )
                            }
            )

    private var startingPosOne: Map<Int, () -> Unit> =
            mapOf(
                    0 to {
                        odometry.resetPos()
                        Gyro.setOffset()
                        addCommands(
                                AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem, 1, false),
                                AutoGoToPosition(swerveAuto, Constants.taxiPositionsOne[0], 0.0)
                        )
                    },
                    1 to
                            {
                                odometry.resetPos()
                                Gyro.setOffset()
                                addCommands(
                                        AutoBall(swerveAuto, 0, 15.0),
                                        AutoGoToCenterAndShoot(swerveAuto, transportSystem, shootSystem, 1, true),
                                        AutoGoToPosition(swerveAuto, Constants.taxiPositionsOne[0], 0.0)
                                )
                            },
            )

    init {
        addCommands(
                // AutoBall(FieldPosition(5.0, 0.0, 180.0))
                // AutoGoToPositionAndAngle(FieldPosition(.9, 0.0, 0.0), 0.0),
                LimeLightAuto(swerveAuto, limelight, swerveAutoInfo),
                // AutoGoToPositionAndAngle(FieldPosition(-2.0, 5.0, 0.0), 0.0)
        )
//        if (startingPos == 0) {
//            startingPosZero[configuration]?.invoke()
//        } else if (startingPos == 1) {
//            startingPosOne[configuration]?.invoke()
//        }
    }
}
