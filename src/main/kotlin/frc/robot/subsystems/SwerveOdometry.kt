package frc.robot.subsystems

import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Robot
import frc.robot.util.FieldPosition
import frc.robot.util.Gyro
import frc.robot.util.Vector2
import frc.robot.util.MathClass


class SwerveOdometry(private var fieldPosition: FieldPosition) : SubsystemBase() {

    private var lastUpdateTime = 1.0
    private var robotVelocities = doubleArrayOf(0.0, 0.0)

    init {
        Gyro.setOffset()
    }

    fun getPosition(): FieldPosition {
        return fieldPosition
    }

    fun getVelocities(): DoubleArray {
        return robotVelocities
    }

    fun resetPos() {
        fieldPosition.reset()
    }

    fun calculateVelocities(): DoubleArray {
        val wheelCoords = arrayOfNulls<Vector2>(4)
        var totalX = 0.0
        var totalY = 0.0
        for (i in 0..3) {
            var wheelAngle: Double = Robot.swerveSystem.wheelArr[i].getTurnValue()
            var wheelSpeed: Double = Robot.swerveSystem.wheelArr[i].getCurrentDriveSpeed()

            // if (i == 0 || i == 2) {
            // wheelSpeed = -wheelSpeed;
            // }

            // Undoes the wheel optimization
            // commented out to see if it will work without optimization
            if (wheelSpeed < 0) {
                wheelSpeed = -wheelSpeed
                wheelAngle = (wheelAngle + 180) % 360
            }
            val cartCoords: DoubleArray = Robot.swerveSystem.polarToCartesian(wheelAngle, wheelSpeed)
            wheelCoords[i] = Vector2(cartCoords[0], cartCoords[1])
            totalX += cartCoords[0]
            totalY += cartCoords[1]
        }
        val robotPolar: DoubleArray = Robot.swerveSystem.cartesianToPolar(totalX, totalY)
        // maybe below is done incorrectly / is unnecessary? also possible that it
        // should be subtracting gyro not adding
        robotPolar[0] -= Gyro.getAngle()
        robotVelocities = Robot.swerveSystem.polarToCartesian(robotPolar[0], robotPolar[1])

        // return new double[] { totalX, totalY };
        return doubleArrayOf(robotVelocities[0], robotVelocities[1])
    }

    fun updatePosition() {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val period = if (lastUpdateTime >= 0) timeNow - lastUpdateTime else 0.0
        val velocities = calculateVelocities()
        fieldPosition.positionCoord.x += velocities[0] * period
        fieldPosition.positionCoord.y += velocities[1] * period
        fieldPosition.angle = Gyro.getAngle()
        /*
        SmartDashboard.putNumber("fieldPosX ", fieldPosition.positionCoord.x)
        SmartDashboard.putNumber("fieldPosY ", fieldPosition.positionCoord.y)
        SmartDashboard.putNumber("fieldPosX M", MathClass.swosToMeters(fieldPosition.positionCoord.x))
        SmartDashboard.putNumber("fieldPosY M", MathClass.swosToMeters(fieldPosition.positionCoord.y))
        SmartDashboard.putNumber("fieldPosAngle ", fieldPosition.angle)
        */
        Robot.odometryShuffleboardTab.add("fieldPosX ", fieldPosition.positionCoord.x)
        Robot.odometryShuffleboardTab.add("fieldPosY ", fieldPosition.positionCoord.y)
        Robot.odometryShuffleboardTab.add("fieldPosX M", MathClass.swosToMeters(fieldPosition.positionCoord.x))
        Robot.odometryShuffleboardTab.add("fieldPosY M", MathClass.swosToMeters(fieldPosition.positionCoord.y))
        Robot.odometryShuffleboardTab.add("fieldPosAngle ", fieldPosition.angle)

        lastUpdateTime = timeNow
    }
}
