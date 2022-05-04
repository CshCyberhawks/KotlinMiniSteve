package frc.robot.util

class Vector2 {
    var x: Number = 0.0
    var y: Number = 0.0

    constructor() {
        x = 0.0
        y = 0.0
    }

    constructor(inputX: Number, inputY: Number) {
        x = inputX
        y = inputY
    }

    fun equals(other: Vector2): Boolean {
        return x == other.x && y == other.y
    }

    fun set(x: Number, y: Number) {
        this.x = x
        this.y = y
    }

    fun set(vec: Vector2) {
        x = vec.x
        y = vec.y
    }
}