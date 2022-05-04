package frc.robot.util

class Vector2 {
    var x: Double = 0.0
    var y: Double = 0.0

    constructor() {
        x = 0.0
        y = 0.0
    }

    constructor(inputX: Double, inputY: Double) {
        x = inputX
        y = inputY
    }

    fun equals(other: Vector2): Boolean {
        return x == other.x && y == other.y
    }

    fun set(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    fun set(vec: Vector2) {
        x = vec.x
        y = vec.y
    }
}