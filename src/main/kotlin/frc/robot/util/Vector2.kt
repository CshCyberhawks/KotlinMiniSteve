package frc.robot.util

class Vector2(var x: Double = 0.0, var y: Double = 0.0) {
    fun set(vec: Vector2) {
        x = vec.x
        y = vec.y
    }

    fun equals(other: Vector2): Boolean {
        return x == other.x && y == other.y
    }
}