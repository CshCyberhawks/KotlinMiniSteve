package frc.robot.commands.auto.groups

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.commands.auto.commands.AutoBall

class AutoCommandGroup(configuration: Int) : SequentialCommandGroup() {
    private var autoConfigurations: Map<Int, () -> Unit> = mapOf(0 to {
        addCommands(
            AutoBall(0),
            AutoGoToCenterAndShoot(0, true)
        )
    })

    init {
        autoConfigurations[configuration]?.invoke()

    }
}
