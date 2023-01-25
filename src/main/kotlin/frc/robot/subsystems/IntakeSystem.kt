package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.networktables.GenericEntry
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Robot


class IntakeSystem : SubsystemBase() {
    // private final double powerMult = 1
    private var intakeMotor: VictorSPX = VictorSPX(Constants.intakeMotor)
    private var intakeSolenoid: Solenoid = Solenoid(Constants.pcm, PneumaticsModuleType.CTREPCM, 0)
    val intakeSequenceShuffle: GenericEntry = Robot.driveShuffleboardTab!!.add("Can Run IntakeSeq", true).entry

    // private final double powerMult = 1

    init {
        intakeMotor.inverted = true
    }

    fun intake(speed: Double) {
        intakeMotor[ControlMode.PercentOutput] = -speed
        intakeSolenoid.set(speed != 0.0)
        // bottomFeedMotor.set(speed)
        // topFeedMotor.set(speed)
        // SmartDashboard.putNumber("Intake Motor Speed ", speed)
    }

    fun kill() {
        intakeMotor[ControlMode.PercentOutput] = 0.0
        intakeSolenoid.set(false)
    }
}