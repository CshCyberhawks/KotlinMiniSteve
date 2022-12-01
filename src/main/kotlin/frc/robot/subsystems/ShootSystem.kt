package frc.robot.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.math.MathUtil
import edu.wpi.first.networktables.NetworkTableEntry
import edu.wpi.first.wpilibj.Encoder
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Robot


class ShootSystem : SubsystemBase() {
// traversalEncoder = traversalMotor.getEncoder()

    // Set distance to pulse to distance in rotation (makes get rate return
// rotations)
// Why write readable code when I can write this 1/8192
// bottom wheel encoder -3.7 for perfect shot
// top wheel encoder 19 for perfect shot
// top wheel encoder 22 max
// bottom wheel encoder -24 max
    private val topMotor: CANSparkMax = CANSparkMax(Constants.topShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless)
    private val bottomRightMotor: CANSparkMax =
        CANSparkMax(Constants.rightShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless)
    private val bottomLeftMotor: CANSparkMax =
        CANSparkMax(Constants.leftShootMotor, CANSparkMaxLowLevel.MotorType.kBrushless)
    private val topEncoder: Encoder = Encoder(2, 3)
    private val bottomEncoder: Encoder = Encoder(0, 1)

    //    private val oldEncoder: RelativeEncoder = topMotor.encoder
//    private val topPIDController: PIDController = PIDController(.01, 0.0, 0.0)
//    private val bottomPIDController: PIDController = PIDController(.01, 0.0, 0.0)
//    private val maxRPM = 5
    var autoShootRunning = false
    var bottomWheelSpeed = 0.0
    var topWheelSpeed = 0.0

    // private var shootSpeedTable: NetworkTableEntry =
    //     Robot.driveShuffleboardTab.add("Shoot Speed", topEncoder.rate).entry
    private var isAtSpeedTable: NetworkTableEntry =
        Robot.driveShuffleboardTab.add("At Desired Speed", false).entry

    // bottom wheel encoder -3.7 for perfect shot
    // top wheel encoder 19 for perfect shot

    // top wheel encoder 22 max
    // bottom wheel encoder -24 max

    init {
        topEncoder.distancePerPulse = 1.0 / 8192.0
        bottomEncoder.distancePerPulse = 1.0 / 8192.0
    }

//    fun getTopEncoder(): Encoder {
//        return topEncoder
//    }

//    fun getBottomEncoder(): Encoder {
//        return bottomEncoder
//    }

    fun getAutoShootState(): Boolean {
        return autoShootRunning
    }

    fun setAutoShootState(state: Boolean) {
        autoShootRunning = state
    }

    // Syncing of bottom 2 motors
    private fun setBottom() {
        val power = 0.31
//        val bottomPIDOutput = bottomPIDController.calculate(
//            bottomEncoder.getRate(),
//            Constants.bottomShootSetpoint
//        )

        // SmartDashboard.putNumber("rightBottomPID", bottomRightPIDOutput)

        // double rightSet = -((bottomRightPIDOutput / maxRPM) + (power / maxRPM))
        // double leftSet = ((bottomLeftPIDOutput / maxRPM) + (power / maxRPM))

        // SmartDashboard.putNumber("bottomPower", power)
        // SmartDashboard.putNumber("rightSet", rightSet)
        // SmartDashboard.putNumber("leftSet", leftSet)

        // SmartDashboard.putNumber("bottomMotorSets", MathUtil.clamp(power, -1, 1))
        bottomRightMotor.set(-MathUtil.clamp(power, -1.0, 1.0))
        bottomLeftMotor.set(MathUtil.clamp(power, -1.0, 1.0))
    }

    fun shoot(shooting: Boolean) {
        SmartDashboard.putNumber("Top Encoder", topEncoder.rate)
        SmartDashboard.putNumber("Bottom Encoder", bottomEncoder.rate)
        // shootSpeedTable.setDouble(topEncoder.rate)

        if (!shooting) {
            topMotor.set(0.0)
            bottomLeftMotor.set(0.0)
            bottomRightMotor.set(0.0)
            isAtSpeedTable.setBoolean(false)
            return
        }

        val power = 0.83

        // SmartDashboard.putNumber("Old Encoder", oldEncoder.getVelocity());
        bottomWheelSpeed = bottomEncoder.rate
        topWheelSpeed = topEncoder.rate
        isAtSpeedTable.setBoolean(topWheelSpeed >= 18.0)
        // power *= maxRPM; // Convert to RPM
        // val topPIDOut = topPIDController.calculate(topWheelSpeed, Constants.topShootSetpoint)

        topMotor.set(MathUtil.clamp(-(power), -1.0, 1.0))
        setBottom()
    }
}
