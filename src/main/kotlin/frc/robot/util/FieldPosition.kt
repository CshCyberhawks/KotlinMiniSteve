package frc.robot.util

class FieldPosition {
    var angle: Double = 0.0
    var positionCoord: Vector2? = null

    constructor(startX: Double, startY: Double, startAngle: Double) {
        positionCoord = Vector2(startX, startY)
        angle = startAngle
    }

    fun reset() {
        positionCoord = Vector2(0.0, 0.0)
        angle = 0.0
    }
}