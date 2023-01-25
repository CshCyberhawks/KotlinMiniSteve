package frc.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Constants.frontBreakBeam
import frc.robot.Constants.shootBreakBeam
import frc.robot.Robot
import frc.robot.util.MathClass


class TransportSystem(private val frontBreakBeam: DigitalInput, private val shootBreakBeam: DigitalInput) : SubsystemBase() {
    private var transportMotor: VictorSPX = VictorSPX(Constants.traversalMotor)
    private val traversalMult = 2.0
    var cargoAmount: Int = 0
    var isRunningSequence: Boolean = false
    private var lastCargoPickupTime = 0.0
    private var lastCargoShootTime = 0.0

    private var cargoPickedUp = false
    private var cargoShot = false

    private var lastFrontBB = true

    private var cargoAmountShuffle = Robot.driveShuffleboardTab.add("cargoAmount", cargoAmount).entry

    init {
        transportMotor.setNeutralMode(NeutralMode.Brake)
        isRunningSequence = false
        cargoAmount = 0
        // lastCargoPickupTime = MathClass.getCurrentTime()
        // lastCargoShootTime = MathClass.getCurrentTime()
    }

    fun cargoMonitor() {
        val shootDifference = MathClass.getCurrentTime() - lastCargoShootTime
        val pickupDifference = MathClass.getCurrentTime() - lastCargoPickupTime
        // SmartDashboard.putNumber("pickupDiff", pickupDifference)
        cargoPickedUp = !frontBreakBeam.get() && pickupDifference > 1
        cargoShot = !shootBreakBeam.get() && cargoAmount > 0 && shootDifference > .5
        if (cargoPickedUp && frontBreakBeam.get() != lastFrontBB) {
            lastCargoPickupTime = MathClass.getCurrentTime()
            cargoAmount++
        }
        if (cargoShot && cargoAmount > 0) {
            lastCargoShootTime = MathClass.getCurrentTime()
            cargoAmount--
        }
        cargoAmountShuffle.setInteger(cargoAmount.toLong())
        lastFrontBB = frontBreakBeam.get()
    }


    fun move(speed: Double) {
        // SmartDashboard.putNumber("transportSpeed", speed * traversalMult)
        transportMotor[ControlMode.PercentOutput] = speed * traversalMult
    }
}