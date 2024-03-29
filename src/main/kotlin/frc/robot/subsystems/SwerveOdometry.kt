package frc.robot.subsystems

import edu.wpi.first.util.WPIUtilJNI
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.networktables.NetworkTableEntry
import frc.robot.Robot
import frc.robot.util.FieldPosition
import frc.robot.util.Gyro
import frc.robot.util.MathClass
import frc.robot.util.Vector2


class SwerveOdometry(private var fieldPosition: FieldPosition) : SubsystemBase() {

    private var lastUpdateTime = 1.0
    private var robotVelocities = doubleArrayOf(0.0, 0.0, 0.0)

    private var lastAngle = 0.0;
    private var totalAngleChange = 0.0;

    private var fieldPosXTab: NetworkTableEntry = Robot.odometryShuffleboardTab.add("fieldPosX", 0.0).entry
    private var fieldPosYTab: NetworkTableEntry = Robot.odometryShuffleboardTab.add("fieldPosY", 0.0).entry
    private var fieldPosXMTab: NetworkTableEntry = Robot.odometryShuffleboardTab.add("fieldPosXM", 0.0).entry
    private var fieldPosYMTab: NetworkTableEntry = Robot.odometryShuffleboardTab.add("fieldPosYM", 0.0).entry
    private var fieldPosAngleTab: NetworkTableEntry = Robot.odometryShuffleboardTab.add("fieldPosAngle", 0.0).entry

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

    fun changePos(position: FieldPosition, meters: Boolean = true) {
        resetPos()
        if (meters) {
            position.positionCoord.x = MathClass.metersToSwos(position.positionCoord.x)            
            position.positionCoord.y = MathClass.metersToSwos(position.positionCoord.y)
            System.out.println(fieldPosition.positionCoord.x)
        }
        Gyro.offset = MathClass.wrapAroundAngles(position.angle)
        System.out.println(Gyro.offset)
        fieldPosition = position;
    }

    fun calculateVelocities(): DoubleArray {
        val wheelCoords = arrayOfNulls<Vector2>(4)
        var totalX = 0.0
        var totalY = 0.0
        for (i in 0..3) {
            var wheelAngle: Double = Robot.swerveSystem.wheelArr[i].getTurnValue()
            var wheelSpeed: Double = Robot.swerveSystem.wheelArr[i].getCurrentDriveSpeed()

            // if (i == 0 || i == 2) {
            // wheelSpeed = -wheelSpeed
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

        SmartDashboard.putNumber("Total Angle Change: ", totalAngleChange)

        lastAngle = Gyro.getAngle();

        // return new double[] { totalX, totalY }
        return doubleArrayOf(robotVelocities[0], robotVelocities[1])
    }

    fun updatePosition() {
        val timeNow = WPIUtilJNI.now() * 1.0e-6
        val period = if (lastUpdateTime >= 0) timeNow - lastUpdateTime else 0.0
        val velocities = calculateVelocities()
        fieldPosition.positionCoord.x += velocities[0] * period
        fieldPosition.positionCoord.y += velocities[1] * period
        fieldPosition.angle = Gyro.getAngle()

        SmartDashboard.putNumber("veloX", velocities[0])
        SmartDashboard.putNumber("veloY", velocities[1])
        SmartDashboard.putNumber("fieldPosX ", fieldPosition.positionCoord.x)
        SmartDashboard.putNumber("fieldPosY ", fieldPosition.positionCoord.y)
        SmartDashboard.putNumber("fieldPosX M", MathClass.swosToMeters(fieldPosition.positionCoord.x))
        SmartDashboard.putNumber("fieldPosY M", MathClass.swosToMeters(fieldPosition.positionCoord.y))
        SmartDashboard.putNumber("fieldPosAngle ", fieldPosition.angle)
        


        fieldPosXTab.setNumber(fieldPosition.positionCoord.x)
        fieldPosYTab.setNumber(fieldPosition.positionCoord.y)
        fieldPosXMTab.setNumber(MathClass.swosToMeters(fieldPosition.positionCoord.x))
        fieldPosYMTab.setNumber(MathClass.swosToMeters(fieldPosition.positionCoord.y))
        fieldPosAngleTab.setNumber(fieldPosition.angle)

        lastUpdateTime = timeNow
    }
}
