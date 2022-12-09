package frc.robot.util

import org.junit.Test

import org.junit.Assert.*

class MathClassTest {

    @Test
    fun smallestDistanceBetween() {
        assert(MathClass.smallestDistanceBetween(1.0, 359.0) == -2.0)
        assert(MathClass.smallestDistanceBetween(0.0, 180.0) == 180.0)
        assert(MathClass.smallestDistanceBetween(359.0, 1.0) == 2.0)
    }
}