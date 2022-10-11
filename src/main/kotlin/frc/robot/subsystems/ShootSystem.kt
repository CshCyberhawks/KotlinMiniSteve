package frc.robot.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import com.revrobotics.RelativeEncoder
import edu.wpi.first.math.MathUtil
import edu.wpi.first.math.controller.PIDController
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Robot


class ShootSystem : SubsystemBase {
    private var topMotor: CANSparkMax;
    private var bottomRightMotor: CANSparkMax;
    private var bottomLeftMotor: CANSparkMax;
    private var topEncoder: Encoder;
    private var bottomEncoder: Encoder;
    private var oldEncoder: RelativeEncoder;
    private var topPIDController: PIDController;
    private var bottomPIDController: PIDController;
    private val topMotorMult = 1.0;
    private val maxRPM = 5;
    private var autoShootRunning = false;
    var bottomWheelSpeed = 0.0;
    var topWheelSpeed = 0.0;

    var shootMult = .7;

    private var shootSpeedTable: NetworkTableEntry;
    private var shootMultTable: NetworkTableEntry;
    private var isAtSpeedTable: NetworkTableEntry;

    // bottom wheel encoder -3.7 for perfect shot
    // top wheel encoder 19 for perfect shot

    // top wheel encoder 22 max
    // bottom wheel encoder -24 max

    // bottom wheel encoder -3.7 for perfect shot
    // top wheel encoder 19 for perfect shot
    // top wheel encoder 22 max
    // bottom wheel encoder -24 max
    constructor() {
        topMotor = CANSparkMax(Constants.topShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        bottomLeftMotor = CANSparkMax(Constants.leftShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        bottomRightMotor = CANSparkMax(Constants.rightShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        oldEncoder = topMotor.encoder;

        // traversalEncoder = traversalMotor.getEncoder();
        topEncoder = Encoder(2, 3);
        bottomEncoder = Encoder(0, 1);

        // Set distance to pulse to distance in rotation (makes get rate return
        // rotations)
        // Why write readable code when I can write this 1/8192
        topEncoder.setDistancePerPulse(0.00012207031);
        bottomEncoder.setDistancePerPulse(0.00012207031);
        topPIDController = PIDController(.01, 0.0, 0.0);
        bottomPIDController = PIDController(.01, 0.0, 0.0);
        autoShootRunning = false;
        shootSpeedTable = Robot.driveShuffleboardTab.add("Shoot Speed", topEncoder.rate).entry;
        shootMultTable = Robot.driveShuffleboardTab.add("Shoot Mult", shootMult).entry;
        isAtSpeedTable = Robot.driveShuffleboardTab.add("At Desired Speed", false).entry;
    }

    fun getTopEncoder(): Encoder {
        return topEncoder;
    }

    fun getBottomEncoder(): Encoder {
        return bottomEncoder;
    }

    fun getAutoShootState(): Boolean {
        return autoShootRunning;
    }

    fun setAutoShootState(state: Boolean) {
        autoShootRunning = state;
    }

    // Syncing of bottom 2 motors
    private fun setBottom(power: Double) {
        var power = power;
        power = .23 * shootMult;
        val bottomPIDOutput = bottomPIDController.calculate(
            bottomEncoder.rate,
            Constants.bottomShootSetpoint * shootMult
        );

        // SmartDashboard.putNumber("rightBottomPID", bottomRightPIDOutput);

        // double rightSet = -((bottomRightPIDOutput / maxRPM) + (power / maxRPM));
        // double leftSet = ((bottomLeftPIDOutput / maxRPM) + (power / maxRPM));

        // SmartDashboard.putNumber("bottomPower", power);
        // SmartDashboard.putNumber("rightSet", rightSet);
        // SmartDashboard.putNumber("leftSet", leftSet);

        // SmartDashboard.putNumber("bottomMotorSets", MathUtil.clamp(power, -1, 1));
        bottomRightMotor.set(-MathUtil.clamp(power + bottomPIDOutput, -1.0, 1.0));
        bottomLeftMotor.set(MathUtil.clamp(power + bottomPIDOutput, -1.0, 1.0));
    }

    fun shoot(power: Double) {
        var power = power;
        shootMultTable.setDouble(shootMult);
        SmartDashboard.putNumber("Top Encoder", topEncoder.rate);
        SmartDashboard.putNumber("Bottom Encoder", bottomEncoder.rate);
        shootSpeedTable.setDouble(topEncoder.rate);
        SmartDashboard.putNumber("shootPower", power);
        if (power == 0.0) {
            topMotor.set(0.0);
            bottomLeftMotor.set(0.0);
            bottomRightMotor.set(0.0);
            isAtSpeedTable.setBoolean(false);
            return
        }
        power = .95 * shootMult;

        // double traversalPIDOUt =
        // motorController.calculate(traverseEncoder.getVelocity(), power *
        // traversalMult);

        // SmartDashboard.putNumber("Old Encoder", oldEncoder.getVelocity());
        bottomWheelSpeed = bottomEncoder.rate;
        topWheelSpeed = topEncoder.rate;
        isAtSpeedTable.setBoolean(topWheelSpeed >= 19);
        // power *= maxRPM; // Convert to RPM
        val topPIDOut = topPIDController.calculate(topWheelSpeed, Constants.topShootSetpoint * shootMult);
        topMotor.set(MathUtil.clamp(-(power + topPIDOut), -1.0, 1.0));
        setBottom(power);
    }
}