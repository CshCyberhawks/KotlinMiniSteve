package frc.robot.util

class FieldPosition {
    var angle: Number = 0.0
    var positionCoord: Vector2? = null

    constructor(startX: Number, startY: Number, startAngle: Number) {
        positionCoord = Vector2(startX, startY)
        angle = startAngle
    }

    fun reset() {
        positionCoord = Vector2(0, 0)
        angle = 0.0
    }
}