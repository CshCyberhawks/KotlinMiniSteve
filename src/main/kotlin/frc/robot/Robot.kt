package frc.robot

import edu.wpi.first.cscore.HttpCamera
import edu.wpi.first.util.net.PortForwarder
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.commands.*
import frc.robot.commands.auto.groups.AutoCommandGroup
import frc.robot.subsystems.*
import frc.robot.util.FieldPosition
import frc.robot.util.IO
import java.util.Map


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
class Robot : TimedRobot() {
    companion object {
        var swerveAuto: SwerveAuto? = null
        var limelightFeed: HttpCamera? = null
        var swerveSystem: SwerveDriveTrain? = null
        var swo: SwerveOdometry? = null
        var swerveCommand: SwerveCommand? = null

        // public Alliance teamColor;
        // public OldSwerveDriveTrain swerveSystem;
        // public OldSwerveDriveTrain swerveSystem;
        // public SwerveDriveTrain swerveSystem;
        var shootSystem: ShootSystem? = null
        var frontBreakBeam: DigitalInput? = null
        var backBreakBeam: DigitalInput? = null
        var topBreakBeam: DigitalInput? = null
        var shootBreakBeam: DigitalInput? = null

        var isSpitting = false

        // public OldSwerveDriveTrain swerveSystem;
        // public SwerveDriveTrain swerveSystem;
        var intakeSystem: IntakeSystem? = null
        var transportSystem: TransportSystem? = null
        var climbSystem: ClimbSystem? = null

        var autoCommands: AutoCommandGroup? = null
        private val startingPosition = 0

        private val autoConfiguration = SendableChooser<Int>()
        private val driveConfiguration = SendableChooser<Boolean>()

        var driveShuffleboardTab = Shuffleboard.getTab("DriverStream")
    }

    // public RobotContainer m_robotContainer;

    // public RobotContainer m_robotContainer;
    /**
     * This function is run when the robot is first started up and should be used
     * for any
     * initialization code.
     */
    override fun robotInit() {
        limelightFeed = HttpCamera("limelight", "http://10.28.75.11:5800")
        // CameraServer.startAutomaticCapture(limelightFeed);
        driveShuffleboardTab.add("LL", limelightFeed).withPosition(0, 0).withSize(8, 4)
            .withProperties(Map.of<String, Any>("Show Crosshair", true, "Show Controls", false))
        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our
        // autonomous chooser on the dashboard.
        // teamColor = DriverStation.getAlliance();
        // m_robotContainer = new RobotContainer();
        // PortForwarder.add(5800, "limelight.local", 5800);
        PortForwarder.add(5800, "limelight.local", 5800)
        autoConfiguration.setDefaultOption("Auto 0", 0)
        autoConfiguration.addOption("Auto 1", 1)
        driveConfiguration.setDefaultOption("Not HOSAS", false)
        driveConfiguration.addOption("HOSAS", true)
        frontBreakBeam = DigitalInput(Constants.frontBreakBeam)
        backBreakBeam = DigitalInput(Constants.backBreakBeam)
        topBreakBeam = DigitalInput(Constants.topBreakBeam)
        shootBreakBeam = DigitalInput(Constants.shootBreakBeam)
        shootSystem = ShootSystem()
        intakeSystem = IntakeSystem()
        transportSystem = TransportSystem()
        climbSystem = ClimbSystem()
        swerveSystem = SwerveDriveTrain()
        // if (DriverStation.getAlliance() == Alliance.Blue) {
        // swo = new SwerveOdometry(Constants.blueStartingPositions[0]);//
        // autoConfiguration.getSelected()]);
        // } else {
        // swo = new SwerveOdometry(Constants.redStartingPositions[0]);//
        // autoConfiguration.getSelected()]);
        // }
        swo = SwerveOdometry(FieldPosition(0.0, 0.0, 0.0))

        // driveSystem = new DriveSystem();
        // CameraServer.startAutomaticCapture();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like
     * diagnostics that you want ran during disabled, autonomous, teleoperated and
     * test.
     *
     *
     *
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and
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
        CommandScheduler.getInstance().run()
        IO.hosas = driveConfiguration.selected
        SmartDashboard.putNumber("cargoStored", transportSystem!!.cargoAmount.toDouble())
    }

    /** This function is called once each time the robot enters Disabled mode.  */
    override fun disabledInit() {
        swo!!.resetPos()
        // swerveSystem.resetPredictedOdometry();
    }

    override fun disabledPeriodic() {}

    /**
     * This autonomou6s runs the autonomous command selected by your
     * [RobotContainer] class.
     */
    override fun autonomousInit() {
        if (swerveCommand != null) {
            swerveCommand!!.cancel()
        }
        Limelight.pipelineInit()

        // m_autonomousCommand = m_robotContainer.getAutonomousCommand();
        swerveAuto = SwerveAuto()
        transportSystem!!.cargoAmount = 1
        autoCommands = AutoCommandGroup(0) // autoConfiguration.getSelected());

        // schedule the autonomous command (example)
        autoCommands!!.schedule()
    }

    /** This function is called periodically during autonomous.  */
    override fun autonomousPeriodic() {
        swo!!.updatePosition()
        transportSystem!!.cargoMonitor()
    }

    override fun teleopInit() {
        shootSystem!!.defaultCommand = ShootCommand(shootSystem)
        intakeSystem!!.defaultCommand = ManualIntakeCommand(intakeSystem)
        transportSystem!!.defaultCommand = ManualTransportCommand(transportSystem)
        climbSystem!!.defaultCommand = ClimbCommand(climbSystem)
        swerveCommand = SwerveCommand(swerveSystem)
        swerveCommand!!.schedule()
        Limelight.pipelineInit()

        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        // if (autoCommands != null) {
        // autoCommands.cancel();
        // }
    }

    /** This function is called periodically during operator control.  */
    override fun teleopPeriodic() {
        swo!!.updatePosition()
        transportSystem!!.cargoMonitor()
        SmartDashboard.putBoolean("frontBreakBeam", frontBreakBeam!!.get())
        SmartDashboard.putBoolean("backBreakBeam", backBreakBeam!!.get())
        SmartDashboard.putBoolean("topBreakBeam", topBreakBeam!!.get())
        SmartDashboard.putBoolean("shootBreakBeam", shootBreakBeam!!.get())
    }

    override fun testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()
    }

    /** This function is called periodically during test mode.  */
    override fun testPeriodic() {}
}
