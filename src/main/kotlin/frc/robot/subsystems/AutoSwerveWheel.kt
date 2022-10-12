package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import edu.wpi.first.math.controller.PIDController
import frc.robot.Constants
import frc.robot.util.DriveEncoder
import frc.robot.util.TurnEncoder
import kotlin.math.abs


class AutoSwerveWheel {
    private var turnMotor: TalonSRX;
    private var driveMotor: TalonFX;

    private var turnEncoder: TurnEncoder;
    private var driveEncoder: DriveEncoder;

    private val oldAngle = 0.0;

    private var turnEncoderPort = 0;

    var turnValue = 0.0;
    var currentDriveSpeed = 0.0;
    var rawTurnValue = 0.0;

    private var speedPID: PIDController;
    private var turnPID: PIDController;

    constructor(turnPort: Int, drivePort: Int, turnEncoderPort: Int) {
        turnMotor = TalonSRX(turnPort);
        driveMotor = TalonFX(drivePort);
        turnEncoder = TurnEncoder(turnEncoderPort);
        driveEncoder = DriveEncoder(driveMotor);
        this.turnEncoderPort = turnEncoderPort;
        speedPID = PIDController(0.03, 0.0, 0.0);
        turnPID = PIDController(0.01, 0.0, 0.0);
        turnPID.setTolerance(4.0);
        turnPID.enableContinuousInput(0.0, 360.0);
    }

    private fun wrapAroundAngles(input: Double): Double {
        return if (input < 0) 360 + input else input;
    }

    fun convertToWheelRotations(meters: Double): Double {
        val wheelConstant: Double = 2 * Math.PI * Constants.wheelRadius / 60;
        return 7 * meters / wheelConstant;
    }

    fun convertToMetersPerSecond(rpm: Double): Double {
        val wheelConstant: Double = 2 * Math.PI * Constants.wheelRadius / 60;
        return wheelConstant * (rpm / Constants.wheelGearRatio);
    }

    fun drive(theta: Double, r: Double) {
        val currentWheelRotations = driveEncoder.getPosition();
        var desiredWheelRotations = convertToWheelRotations(r);
        val currentTurnPosition = wrapAroundAngles(turnEncoder.get());
        var desiredTurnPosition = wrapAroundAngles(theta);
        if (abs(desiredTurnPosition - currentTurnPosition) > 90
            && abs(desiredTurnPosition - currentTurnPosition) < 270
        ) {
            desiredTurnPosition = (desiredTurnPosition + 180) % 360;
            desiredWheelRotations *= -1.0;
        }
        val wheelSpeed = speedPID.calculate(currentWheelRotations, desiredWheelRotations);
        val wheelTurn = turnPID.calculate(currentTurnPosition, desiredTurnPosition);

        // talonFX controlmode.position for positional control mode
        driveMotor[ControlMode.Velocity] = wheelSpeed;
        if (!turnPID.atSetpoint()) {
            turnMotor[ControlMode.PercentOutput] = wheelTurn;
        }
    }

    fun kill() {
        driveMotor[ControlMode.PercentOutput] = 0.0;
        turnMotor[ControlMode.PercentOutput] = 0.0;
    }
}