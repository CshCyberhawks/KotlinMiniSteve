package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.commands.auto.commands.AutoGoToPosition
import frc.robot.util.Vector2


class AutoCommandGroup : SequentialCommandGroup {
    constructor(configuration: Int) : super() {
        println("asdijakdshjajksdhakjsghdjkasghdkjaghsdj")
        // add your autonomous commands below
        // example: below will move robot 2 meters on the x and rotate to 90 degrees
        // then it will wait 1 second before moving the robot back to its starting
        // position
        // if (configuration == 0 && DriverStation.getAlliance() == Alliance.Blue) {
        //     addCommands( // new AutoBall(1),
        //         AutoGoToCenterAndShoot(0, false),  // new AutoBall(0));
        //         AutoGoToPosition(Vector2(3.0, 0.0), 0.0)
        //     )
        //     // new AutoGoToCenterAndShoot(0, true),
        // } else if (configuration == 0 && DriverStation.getAlliance() == Alliance.Red) {
        //     addCommands( // new Wait(3),
        //         AutoGoToCenterAndShoot(0, false),
        //         AutoGoToPosition(Vector2(3.0, 0.0), 0.0)
        //     )
            // new AutoBall(0));
            // new AutoGoToCenterAndShoot(0, true),
            // new AutoGoToCenterAndShoot(0, true));
            // new AutoGoToCenterAndShoot(0, false),
            
            addCommands(
                AutoGoToPosition(Vector2(4.0, 0.0), 0.0),
                AutoGoToPosition(Vector2(0.0, -2.0), 0.0)
                );
            // new AutoGoToPosition(new Vector2(.7, 3.8), 0));
        // }
    }
}