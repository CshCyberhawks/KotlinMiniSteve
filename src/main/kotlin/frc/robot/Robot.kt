package frc.robot

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import edu.wpi.first.wpilibj.TimedRobot
import frc.robot.commands.DriveCommand
import frc.robot.subsystems.DriveSystem
import javax.naming.ldap.Control

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
class Robot : TimedRobot() {
    companion object {
        lateinit var driveSystem: DriveSystem
    }

    // public RobotContainer m_robotContainer

    // public RobotContainer m_robotContainer
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    override fun robotInit() {
        driveSystem = DriveSystem()
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
     *
     * This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     */
    override fun robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
    }

    /** This function is called once each time the robot enters Disabled mode. */
    override fun disabledInit() {
        // swerveSystem.resetPredictedOdometry()
    }

    override fun disabledPeriodic() {}

    /** This autonomous runs the autonomous command selected by your [RobotContainer] class. */
    override fun autonomousInit() {

    }

    /** This function is called periodically during autonomous. */
    override fun autonomousPeriodic() {

    }

    override fun teleopInit() {
        var driveCommand = DriveCommand(driveSystem)
        driveCommand.schedule()
    }

    /** This function is called periodically during operator control. */
    override fun teleopPeriodic() {

    }

    lateinit var testingMotor: TalonFX


    override fun testInit() {
        testingMotor = TalonFX(9, "driveBus")
        testingMotor[ControlMode.PercentOutput] = 0.1
    }

    /** This function is called periodically during test mode. */
    override fun testPeriodic() {
    }
}
