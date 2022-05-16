package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.robot.Constants
import frc.robot.util.DriveEncoder
import frc.robot.util.DriveState
import frc.robot.util.TurnEncoder


class SwerveWheel {
    private var turnMotor: TalonSRX? = null
    private var driveMotor: TalonFX? = null
    public var turnEncoder: TurnEncoder? = null
    private var driveEncoder: DriveEncoder? = null

    private var oldAngle = 0.0

    private var m_turnEncoderPort = 0

    // below is in m / 20 ms
    private var maxAcceleration = .01
    private var lastSpeed = 0.0

    var turnValue = 0.0
    var currentDriveSpeed = 0.0
    var rawTurnValue = 0.0

    private var turnPidController: PIDController? = null
    private var drivePidController: PIDController? = null
    private var speedPID: PIDController? = null

    constructor(turnPort: Int, drivePort: Int, turnEncoderPort: Int) {
        turnMotor = TalonSRX(turnPort)
        driveMotor = TalonFX(drivePort)
        driveEncoder = DriveEncoder(driveMotor!!)
        turnEncoder = TurnEncoder(turnEncoderPort)
        driveMotor!!.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0)
        driveMotor!!.config_kF(0, 0.0)
        driveMotor!!.config_kP(0, 0.01)
        driveMotor!!.config_kI(0, 0.0)
        driveMotor!!.config_kD(0, 0.0)
        driveMotor!!.setNeutralMode(NeutralMode.Brake)
        m_turnEncoderPort = turnEncoderPort
        turnPidController = PIDController(.01, 0.0, 0.0)
        turnPidController!!.setTolerance(4.0)
        turnPidController!!.enableContinuousInput(0.0, 360.0)
        speedPID = PIDController(0.03, 0.0, 0.0)
        drivePidController = PIDController(0.01, 0.0, 0.0)
        if (turnEncoderPort == 1 || turnEncoderPort == 3) {
            driveMotor!!.setInverted(true)
        } else {
            driveMotor!!.setInverted(false)
        }
    }

    private fun wrapAroundAngles(input: Double): Double {
        return if (input < 0) 360 + input else input
    }

    fun convertToMetersPerSecond(rpm: Double): Double {
        val radius = 0.0505
        // Gear ratio is 7:1
        return 2 * Math.PI * radius / 60 * (rpm / 7)
    }

    fun convertToMetersPerSecondFromSecond(rps: Double): Double {
        val radius = 0.0505
        return 2 * Math.PI * radius * (rps / 7)
    }

    fun convertToWheelRotations(meters: Double): Double {
        val wheelConstant: Double = 2 * Math.PI * Constants.wheelRadius / 60
        return 7 * meters / wheelConstant
    }

    fun drive(speed: Double, angle: Double, mode: DriveState?) {
        var speed = speed
        var angle = angle
        oldAngle = angle
        maxAcceleration = when (mode) {
            DriveState.TELE -> 0.05
            DriveState.AUTO -> 0.01
            else -> 0.05
        }

        val driveVelocity = driveEncoder!!.getVelocity()
        currentDriveSpeed = convertToMetersPerSecondFromSecond(driveVelocity)
        SmartDashboard.putNumber("$m_turnEncoderPort wheel rotations", driveVelocity)
        turnValue = wrapAroundAngles(turnEncoder!!.get())
        rawTurnValue = turnEncoder!!.get()
        angle = wrapAroundAngles(angle)

        // Optimization Code stolen from
        // https://github.com/Frc2481/frc-2015/blob/master/src/Components/SwerveModule.cpp
        if (Math.abs(angle - turnValue) > 90 && Math.abs(angle - turnValue) < 270) {
            angle = ((angle.toInt() + 180) % 360).toDouble()
            speed = -speed
        }

        // if (mode == "tele") {
        if (Math.abs(speed - lastSpeed) > maxAcceleration) {
            speed = if (speed > lastSpeed) {
                lastSpeed + maxAcceleration
            } else {
                lastSpeed - maxAcceleration
            }
        }
        // }
        lastSpeed = speed
        speed = convertToMetersPerSecond(speed * 5000) // Converting the speed to m/s with a max rpm of 5000 (GEar
        // ratio is 7:1)
        val turnPIDOutput = turnPidController!!.calculate(turnValue, angle)

        // maybe reason why gradual deecleration isn't working is because the PID
        // controller is trying to slow down by going opposite direction in stead of
        // just letting wheels turn? maybe we need to skip the PID for slowing down?
        // maybe needs more tuning?
        val drivePIDOutput = drivePidController!!.calculate(currentDriveSpeed, speed)

        // SmartDashboard.putNumber(m_turnEncoderPort + " pid value", drivePIDOutput);

        // double driveFeedForwardOutput = driveFeedforward.calculate(currentDriveSpeed,
        // speed);

        // SmartDashboard.putNumber(m_turnEncoderPort + " currentDriveSpeed",
        // currentDriveSpeed);
        // SmartDashboard.putNumber(m_turnEncoderPort + " turn set", turnPIDOutput);

        // SmartDashboard.putNumber(m_turnEncoderPort + " driveSet", (speed / 3.777) +
        // drivePIDOutput);
        // SmartDashboard.putNumber(m_turnEncoderPort + " turnSet", turnPIDOutput);
        // 70% speed is about 5.6 feet/second
        driveMotor!![ControlMode.PercentOutput] = MathUtil.clamp(speed / 3.777 /* + drivePIDOutput */, -1.0, 1.0)
        if (!turnPidController!!.atSetpoint()) {
            turnMotor!![ControlMode.PercentOutput] = MathUtil.clamp(turnPIDOutput, -1.0, 1.0)
        }
    }

    fun preserveAngle() {
        drive(0.0, oldAngle, DriveState.OTHER)
    }

    fun kill() {
        driveMotor!![ControlMode.PercentOutput] = 0.0
        turnMotor!![ControlMode.PercentOutput] = 0.0
    }
}