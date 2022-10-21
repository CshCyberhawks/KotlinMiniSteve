package frc.robot.commands.auto.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.Robot


class AutoGoToAngle(private var desiredAngle: Double) : CommandBase() {
    // this command will move the robot to the desired position x, y and twist
    // values
    // it does this by first driving full speed in the direction of the desired x
    // and y position, and then it will stop and twist until it reaches desired
    // angle

    // this command will move the robot to the desired position x, y and twist
    // values
    // it does this by first driving full speed in the direction of the desired x
    // and y position, and then it will stop and twist until it reaches desired
    // angle
    override fun initialize() {
        // desired position = x in swos
        // (https://www.notion.so/Odometry-baacd114086e4218a5eedb5ef45a223f) (.27
        // meters), y in swos, and twist in degrees
        // (based on
        // robot staring position)
        Robot.swerveAuto.setDesiredAngle(desiredAngle, false)
    }

    override fun execute() {
        Robot.swerveAuto.twist()
    }

    override fun end(interrupted: Boolean) {
        SmartDashboard.putBoolean("auto angle command finished", Robot.swerveAuto.isAtDesiredAngle())
        Robot.swerveAuto.kill()
    }

    override fun isFinished(): Boolean {
        return Robot.swerveAuto.isAtDesiredAngle()
    }
}
