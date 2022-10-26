package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Constants
import frc.robot.Robot
import frc.robot.commands.auto.commands.AutoGoToPosition
import frc.robot.commands.auto.commands.AutoShootCommand
import frc.robot.commands.auto.commands.AutoGoToPositionAndAngle
import frc.robot.util.Vector2


class AutoGoToCenterAndShoot(shootPosition: Int, move: Boolean) : SequentialCommandGroup() {
    // new AutoGoToAngle(111),
// shootPositions[shootPosition], 0),
// add your autonomous commands below
// example: below will move robot 2 meters on the x and rotate to 90 degrees
// then it will wait 1 second before moving the robot back to its starting
// position
    private var shootPositions: Array<Vector2> =
        if (DriverStation.getAlliance() == Alliance.Blue) Constants.blueShootingPositions else Constants.redShootingPositions

    init {
        if (move) {
            addCommands( // new AutoGoToAngle(111),
                AutoGoToPositionAndAngle(shootPositions[shootPosition], 0.0, 0.0), // shootPositions[shootPosition], 0),
                AutoShootCommand(Robot.shootSystem)
            )
        } else {
            addCommands(
                AutoShootCommand(Robot.shootSystem)
            )
        }
    }
}