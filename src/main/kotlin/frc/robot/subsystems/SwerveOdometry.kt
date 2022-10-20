package frc.robot.subsystems

import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Robot
import frc.robot.util.*


class SwerveOdometry(private var fieldPosition: FieldPosition) : SubsystemBase() {

    private var lastUpdateTime = 1.0
    private var robotVelocities = Vector2()

    init {
        Gyro.setOffset()
    }

    fun getPosition(): FieldPosition {
        return fieldPosition
    }

    fun getVelocities(): Vector2 {
        return robotVelocities
    }

    fun resetPos() {
        fieldPosition.reset()
    }

    fun calculateVelocities(): Vector2 {
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
            val cartCoords: Vector2 = MathClass.polarToCartesian(Polar(wheelAngle, wheelSpeed))
            wheelCoords[i] = Vector2(cartCoords.x, cartCoords.y)
            totalX += cartCoords.x
            totalY += cartCoords.y
        }
        val robotPolar: Polar = MathClass.cartesianToPolar(Vector2(totalX, totalY))
        // maybe below is done incorrectly / is unnecessary? also possible that it
        // should be subtracting gyro not adding
        robotPolar.theta -= Gyro.getAngle()
        robotVelocities = MathClass.polarToCartesian(robotPolar)

        // return new double[] { totalX, totalY };
        return robotVelocities
    }

    fun updatePosition() {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val period = if (lastUpdateTime >= 0) timeNow - lastUpdateTime else 0.0
        val velocities = calculateVelocities()
        fieldPosition.positionCoord.x += velocities.x * period
        fieldPosition.positionCoord.y += velocities.y * period
        fieldPosition.angle = Gyro.getAngle()
        SmartDashboard.putNumber("fieldPosX ", fieldPosition.positionCoord.x)
        SmartDashboard.putNumber("fieldPosY ", fieldPosition.positionCoord.y)
        SmartDashboard.putNumber("fieldPosX M", MathClass.swosToMeters(fieldPosition.positionCoord.x))
        SmartDashboard.putNumber("fieldPosY M", MathClass.swosToMeters(fieldPosition.positionCoord.y))
        SmartDashboard.putNumber("fieldPosAngle ", fieldPosition.angle)
        lastUpdateTime = timeNow
    }
}
