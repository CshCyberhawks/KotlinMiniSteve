package frc.robot

import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.music.Orchestra
import edu.wpi.first.cscore.HttpCamera
import edu.wpi.first.net.PortForwarder
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.CommandScheduler
import frc.robot.commands.*
import frc.robot.commands.auto.SwerveAutoInfo
import frc.robot.commands.auto.TransportInfo
import frc.robot.commands.auto.groups.AutoCommandGroup
import frc.robot.subsystems.*
import frc.robot.util.FieldPosition
import java.util.Map

//import cshcyberhawks.swolib.math.AngleCalculations

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
class Robot : TimedRobot() {
    companion object {
        val transportInfo = TransportInfo(false)
        val swerveAutoInfo = SwerveAutoInfo(false, 0)

        lateinit var swerveAuto: SwerveAuto
        lateinit var limelightFeed: HttpCamera
        lateinit var swerveSystem: SwerveDriveTrain
        lateinit var swo: SwerveOdometry
        var swerveCommand: SwerveCommand? = null
        lateinit var limelight: Limelight

        // public Alliance teamColor
        // public OldSwerveDriveTrain swerveSystem
        // public OldSwerveDriveTrain swerveSystem
        // public SwerveDriveTrain swerveSystem
        lateinit var shootSystem: ShootSystem
        lateinit var frontBreakBeam: DigitalInput
        lateinit var backBreakBeam: DigitalInput
        lateinit var topBreakBeam: DigitalInput
        lateinit var shootBreakBeam: DigitalInput

        // public OldSwerveDriveTrain swerveSystem
        // public SwerveDriveTrain swerveSystem
        lateinit var intakeSystem: IntakeSystem
        lateinit var transportSystem: TransportSystem
        lateinit var climbSystem: ClimbSystem

        var autoCommands: AutoCommandGroup? = null

        private val autoConfiguration = SendableChooser<Int>()

        var driveShuffleboardTab = Shuffleboard.getTab("DriverStream")
        var odometryShuffleboardTab = Shuffleboard.getTab("OdometryStream")
    }

    // public RobotContainer m_robotContainer

    // public RobotContainer m_robotContainer
    /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     */
    override fun robotInit() {
        limelightFeed = HttpCamera("limelight", "http://10.28.75.11:5800")
        // CameraServer.startAutomaticCapture(limelightFeed)
        driveShuffleboardTab
                .add("LimeLight", limelightFeed)
                .withPosition(6, 0)
                .withSize(8, 4)
                .withProperties(Map.of<String, Any>("Show Crosshair", true, "Show Controls", false))
        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our
        // autonomous chooser on the dashboard.
        // teamColor = DriverStation.getAlliance()
        // m_robotContainer = new RobotContainer()
        // PortForwarder.add(5800, "limelight.local", 5800)
        PortForwarder.add(5800, "limelight.local", 5800)
        autoConfiguration.setDefaultOption("Auto 0", 0)
        autoConfiguration.addOption("Auto 1", 1)
        frontBreakBeam = DigitalInput(Constants.frontBreakBeam)
        backBreakBeam = DigitalInput(Constants.backBreakBeam)
        topBreakBeam = DigitalInput(Constants.topBreakBeam)
        shootBreakBeam = DigitalInput(Constants.shootBreakBeam)
        shootSystem = ShootSystem()
        intakeSystem = IntakeSystem()
        transportSystem = TransportSystem()
        climbSystem = ClimbSystem()
        swo = SwerveOdometry(FieldPosition(0.0, 0.0, 0.0))
        swerveSystem = SwerveDriveTrain(swo, swerveAutoInfo)
        limelight = Limelight(0.711, 0.24, 40.0)

        //
        // driveSystem = new DriveSystem()
        // CameraServer.startAutomaticCapture()
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
        CommandScheduler.getInstance().run()
        // SmartDashboard.putNumber("cargoStored", transportSystem.cargoAmount.toDouble())
    }

    /** This function is called once each time the robot enters Disabled mode. */
    override fun disabledInit() {
        swo.resetPos()
        // swerveSystem.resetPredictedOdometry()
    }

    private fun wrapAroundAngles(input: Double): Double {
        return if (input < 0) 360 + input else input
    }

    override fun disabledPeriodic() {

    }

    /** This autonomous runs the autonomous command selected by your [RobotContainer] class. */
    override fun autonomousInit() {

        swerveAuto = SwerveAuto(0)
        //        swo = SwerveOdometry(Constants.blueStartingPositions[0])
        swerveCommand?.cancel()
        limelight.pipelineInit()

        // m_autonomousCommand = m_robotContainer.getAutonomousCommand()
        transportSystem.cargoAmount = 1
        autoCommands = AutoCommandGroup(swo, shootSystem, transportSystem, swerveAuto, swerveAutoInfo, limelight, 1, 0) //
        // autoConfiguration
        // .getSelected())

        // schedule the autonomous command (example)
        autoCommands!!.schedule()
    }

    /** This function is called periodically during autonomous. */
    override fun autonomousPeriodic() {
        swo.updatePosition()
        transportSystem.cargoMonitor()


    }

    override fun teleopInit() {
        shootSystem.defaultCommand = ShootCommand(shootSystem, transportSystem)
        intakeSystem.defaultCommand = ManualIntakeCommand(intakeSystem, transportSystem, transportInfo)
        // TODO: remove this before the competition and leave the leftover cargo stored from auto
        transportSystem.cargoAmount = 0
        transportSystem.defaultCommand = ManualTransportCommand(transportSystem, shootSystem, transportInfo)
        climbSystem.defaultCommand = ClimbCommand(climbSystem)
        swerveCommand = SwerveCommand(swerveSystem, swo, limelight)
        swerveCommand!!.schedule()
        limelight.pipelineInit()

        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        // if (autoCommands != null) {
        // autoCommands.cancel()
        // }
    }

    /** This function is called periodically during operator control. */
    override fun teleopPeriodic() {
        swo.updatePosition()
        transportSystem.cargoMonitor()
        // SmartDashboard.putBoolean("frontBreakBeam", frontBreakBeam.get())
        // SmartDashboard.putBoolean("backBreakBeam", backBreakBeam.get())
        // SmartDashboard.putBoolean("topBreakBeam", topBreakBeam.get())
        // SmartDashboard.putBoolean("shootBreakBeam", shootBreakBeam.get())
    }

    var orchestra: Orchestra? = null
    var falcon: TalonFX? = null
    var songs = arrayOf("africa", "imperialmarch")

    private fun loadSong(idx: Int) {
        orchestra!!.loadMusic("music/" + songs[idx] + ".chrp")
    }

    override fun testInit() {
        CommandScheduler.getInstance().cancelAll()
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll()
        val instruments = listOf(
                TalonFX(Constants.frontRightDriveMotor),
                TalonFX(Constants.frontLeftDriveMotor),
                TalonFX(Constants.backRightDriveMotor),
                TalonFX(Constants.backLeftDriveMotor)
        )
        orchestra = Orchestra(instruments)
        loadSong(1)
        orchestra!!.play()
    }

    /** This function is called periodically during test mode. */
    override fun testPeriodic() {
        val frontRightValue = wrapAroundAngles(swerveSystem.frontRight.turnEncoder.getRaw())
        val backRightValue = wrapAroundAngles(swerveSystem.backRight.turnEncoder.getRaw())
        val frontLeftValue = wrapAroundAngles(swerveSystem.frontLeft.turnEncoder.getRaw())
        val backLeftValue = wrapAroundAngles(swerveSystem.backLeft.turnEncoder.getRaw())
        val encoderValues: DoubleArray =
                doubleArrayOf(frontRightValue, frontLeftValue, backRightValue, backLeftValue)

        SmartDashboard.putString("Encoder Values", encoderValues.joinToString(", "))

//        println(AngleCalculations.wrapAroundAngles(-180.0))
    }
}
