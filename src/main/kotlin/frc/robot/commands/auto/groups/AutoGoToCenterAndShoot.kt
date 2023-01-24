package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Constants
import frc.robot.commands.auto.commands.AutoGoToPositionAndAngle
import frc.robot.commands.auto.commands.AutoShootCommand
import frc.robot.subsystems.ShootSystem
import frc.robot.subsystems.SwerveAuto
import frc.robot.subsystems.TransportSystem
import frc.robot.util.FieldPosition


class AutoGoToCenterAndShoot(swerveAuto: SwerveAuto, transportSystem: TransportSystem, shootSystem: ShootSystem, shootPosition:
Int, move:
Boolean) :
        SequentialCommandGroup() {
    // new AutoGoToAngle(111),
// shootPositions[shootPosition], 0),
// add your autonomous commands below
// example: below will move robot 2 meters on the x and rotate to 90 degrees
// then it will wait 1 second before moving the robot back to its starting
// position
    private var shootPositions: Array<FieldPosition>

    init {
        if (swerveAuto.startingPos == 0) {
            shootPositions = Constants.shootingPositionsZero
        }
        shootPositions = Constants.shootingPositionsZero

        if (move) {
            addCommands( // new AutoGoToAngle(111),
                    AutoGoToPositionAndAngle(swerveAuto, shootPositions[shootPosition], 0.0), // shootPositions[shootPosition],
                    // 0),
                    AutoShootCommand(transportSystem, shootSystem)
            )
        } else {
            addCommands(
                    AutoShootCommand(transportSystem, shootSystem)
            )
        }
    }
}
