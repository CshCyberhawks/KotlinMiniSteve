package frc.robot.util

class FieldPosition(startX: Double, startY: Double, startAngle: Double) {
    var angle: Double = startAngle
    var positionCoord: Vector2 = Vector2(startX, startY)

    fun reset() {
        positionCoord = Vector2(0.0, 0.0)
        angle = 0.0
    }
}