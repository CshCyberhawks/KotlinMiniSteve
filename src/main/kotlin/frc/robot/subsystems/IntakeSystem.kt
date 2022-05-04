package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants


class IntakeSystem : SubsystemBase {
    private var intakeMotor: VictorSPX? = null
    private var intakeSolenoid: Solenoid? = null

    // private final double powerMult = 1;

    // private final double powerMult = 1;
    constructor() : super() {
        intakeMotor = VictorSPX(Constants.intakeMotor)
        intakeMotor!!.inverted = true
        intakeSolenoid = Solenoid(Constants.pcm, PneumaticsModuleType.CTREPCM, 0)
    }

    fun intake(speed: Double) {
        intakeMotor!![ControlMode.PercentOutput] = -speed
        if (speed != 0.0) intakeSolenoid!!.set(true) else intakeSolenoid!!.set(false)
        // bottomFeedMotor.set(speed);
        // topFeedMotor.set(speed);
        SmartDashboard.putNumber("Intake Motor Speed ", speed)
    }

    fun kill() {
        intakeMotor!![ControlMode.PercentOutput] = 0.0
        intakeSolenoid!!.set(false)
    }
}