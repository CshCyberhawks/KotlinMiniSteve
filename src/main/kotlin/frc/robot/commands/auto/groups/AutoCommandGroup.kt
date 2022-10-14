package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.Constants
import frc.robot.commands.auto.commands.AutoBall
import frc.robot.commands.auto.commands.AutoGoToPosition
import frc.robot.commands.auto.commands.AutoGoToAngle
import frc.robot.commands.auto.commands.AutoGoToPositionAndAngle
import frc.robot.util.Vector2


class AutoCommandGroup : SequentialCommandGroup {
    constructor(configuration: Int) : super() {
        // add your autonomous commands below
        // example: below will move robot 2 meters on the x and rotate to 90 degrees
        // then it will wait 1 second before moving the robot back to its starting
        // position
         if (configuration == 0 && DriverStation.getAlliance() == Alliance.Blue) {
             addCommands(
                    // AutoGoToPosition(Constants.blueBallPositions[0], 0.0)
//             AutoGoToPosition(Vector2(3.0, 0.0), 0.0)
              AutoBall(0)
              // AutoGoToPositionAndAngle(Vector2(5.0, 0.0), 0.0, 0.0)
             )
             // new AutoGoToCenterAndShoot(0, true),
         } else if (configuration == 0 && DriverStation.getAlliance() == Alliance.Red) {
             addCommands( // new Wait(3),
                 AutoGoToCenterAndShoot(0, false),
                 AutoGoToPosition(Vector2(3.0, 0.0), 0.0)
             )}
             }
}
