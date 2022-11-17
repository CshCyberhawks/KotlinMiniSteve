package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode
import com.ctre.phoenix.motorcontrol.can.TalonFX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants


class DriveSystem : SubsystemBase() {
//    var leftBackMotor = VictorSPX(Constants.leftBackMotor)
//    var leftFrontMotor = VictorSPX(Constants.leftFrontMotor)
//    var rightBackMotor = VictorSPX(Constants.rightBackMotor)
//    var rightFrontMotor = VictorSPX(Constants.rightFrontMotor)

    fun setSpeed(leftSpeed: Double, rightSpeed: Double) {
//        leftBackMotor[ControlMode.PercentOutput] = -leftSpeed
//        leftFrontMotor[ControlMode.PercentOutput] = -leftSpeed
//        rightBackMotor[ControlMode.PercentOutput] = rightSpeed
//        rightFrontMotor[ControlMode.PercentOutput] = rightSpeed
        SmartDashboard.putNumber("Left Motor", -leftSpeed)
        SmartDashboard.putNumber("Right Motor", rightSpeed)
    }

    override fun periodic() {
        // This method will be called once per scheduler run
    }

    override fun simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }
}
