package frc.robot.util

class FieldPosition(var positionCoord: Coordinate, var angle: Double) {
    fun reset() {
        positionCoord = Coordinate(0.0, 0.0)
        angle = 0.0
    }
}