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


class ShootSystem() : SubsystemBase() {
// traversalEncoder = traversalMotor.getEncoder();

    // Set distance to pulse to distance in rotation (makes get rate return
// rotations)
// Why write readable code when I can write this 1/8192
// bottom wheel encoder -3.7 for perfect shot
// top wheel encoder 19 for perfect shot
// top wheel encoder 22 max
// bottom wheel encoder -24 max
    private var topMotor: CANSparkMax = CANSparkMax(Constants.topShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless)
    private var bottomRightMotor: CANSparkMax =
        CANSparkMax(Constants.rightShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless)
    private var bottomLeftMotor: CANSparkMax =
        CANSparkMax(Constants.leftShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless)
    private var topEncoder: Encoder = Encoder(2, 3)
    private var bottomEncoder: Encoder = Encoder(0, 1)
    private var oldEncoder: RelativeEncoder = topMotor.encoder
    private var topPIDController: PIDController = PIDController(.01, 0.0, 0.0)
    private var bottomPIDController: PIDController = PIDController(.01, 0.0, 0.0)
    private val maxRPM = 5
    private var autoShootRunning = false
    var bottomWheelSpeed = 0.0
    var topWheelSpeed = 0.0

    private var shootSpeedTable: NetworkTableEntry =
        Robot.driveShuffleboardTab.add("Shoot Speed", topEncoder.getRate()).getEntry()
    private var isAtSpeedTable: NetworkTableEntry = Robot.driveShuffleboardTab.add("At Desired Speed", false).getEntry()

    // bottom wheel encoder -3.7 for perfect shot
    // top wheel encoder 19 for perfect shot

    // top wheel encoder 22 max
    // bottom wheel encoder -24 max

    init {
        topEncoder.setDistancePerPulse(1.0 / 8192.0)
        bottomEncoder.setDistancePerPulse(1.0 / 8192.0)
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
    private fun setBottom() {
        val power = .45
        val bottomPIDOutput = bottomPIDController.calculate(
            bottomEncoder.getRate(),
            Constants.bottomShootSetpoint
        )

        // SmartDashboard.putNumber("rightBottomPID", bottomRightPIDOutput);

        // double rightSet = -((bottomRightPIDOutput / maxRPM) + (power / maxRPM));
        // double leftSet = ((bottomLeftPIDOutput / maxRPM) + (power / maxRPM));

        // SmartDashboard.putNumber("bottomPower", power);
        // SmartDashboard.putNumber("rightSet", rightSet);
        // SmartDashboard.putNumber("leftSet", leftSet);

        // SmartDashboard.putNumber("bottomMotorSets", MathUtil.clamp(power, -1, 1));
        bottomRightMotor.set(-MathUtil.clamp(power, -1.0, 1.0))
        bottomLeftMotor.set(MathUtil.clamp(power, -1.0, 1.0))
    }

    fun shoot(shooting: Boolean) {
        SmartDashboard.putNumber("Top Encoder", topEncoder.getRate())
        SmartDashboard.putNumber("Bottom Encoder", bottomEncoder.getRate())
        shootSpeedTable.setDouble(topEncoder.getRate())
        if (!shooting) {
            topMotor.set(0.0);
            bottomLeftMotor.set(0.0);
            bottomRightMotor.set(0.0);
            isAtSpeedTable.setBoolean(false);
            return
        }

        val power = .77

        // SmartDashboard.putNumber("Old Encoder", oldEncoder.getVelocity());
        bottomWheelSpeed = bottomEncoder.rate;
        topWheelSpeed = topEncoder.rate;
        isAtSpeedTable.setBoolean(topWheelSpeed >= 19);
        // power *= maxRPM; // Convert to RPM
        val topPIDOut = topPIDController.calculate(topWheelSpeed, Constants.topShootSetpoint)
        topMotor.set(MathUtil.clamp(-(power), -1.0, 1.0))
        setBottom()
    }
}