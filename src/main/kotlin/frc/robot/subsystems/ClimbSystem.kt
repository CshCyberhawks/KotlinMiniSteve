package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants


class ClimbSystem : SubsystemBase() {
    // rightSolenoid = new Solenoid(Constants.pcm, PneumaticsModuleType.CTREPCM, 1)
    private var climbMotor: TalonFX = TalonFX(Constants.climbMotor)
    private var leftSolenoid: Solenoid = Solenoid(Constants.pcm, PneumaticsModuleType.CTREPCM, 2)
//    private val rightSolenoid: Solenoid
//    private val climbPidController: PIDController

    init {
        climbMotor.config_kI(0, 0.0)
        climbMotor.config_kF(0, 0.0)
        climbMotor.config_kP(0, 0.01)
        climbMotor.config_kD(0, 0.0)
        climbMotor.setStatusFramePeriod(8, 2800)
        climbMotor.setStatusFramePeriod(14, 2600)
        climbMotor.setStatusFramePeriod(10, 100000)

    }

    fun climb(speed: Double) {
        climbMotor[ControlMode.PercentOutput] = speed
    }

    fun controlPneumatics() {
        leftSolenoid.toggle()
        // rightSolenoid.set(control)
    }
}