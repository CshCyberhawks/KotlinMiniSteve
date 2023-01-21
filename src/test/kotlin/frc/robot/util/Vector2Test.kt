package frc.robot.util

import org.junit.Test

class Vector2Test {

    @Test
    fun add() {
        assert(Vector2(2.0, 3.0) + Vector2(3.0, 2.0) == Vector2(5.0, 5.0))
    }

    @Test
    fun subtract() {
        assert(Vector2(2.0, 3.0) - Vector2(1.0, 1.0) == Vector2(1.0, 2.0))
    }

    @Test
    fun testEquals() {
        assert(Vector2(2.0, 3.0) == Vector2(2.0, 3.0))
    }
}