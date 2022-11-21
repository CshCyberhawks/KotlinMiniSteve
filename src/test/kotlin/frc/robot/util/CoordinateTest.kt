package frc.robot.util

import org.junit.Test

import org.junit.Assert.*

class CoordinateTest {
    @Test
    fun equals() {
        assert(Coordinate(2.0, 3.0) == Coordinate(2.0, 3.0))
    }

    @Test
    fun plus() {
        assert(Coordinate(2.0, 3.0) + Coordinate(3.0, 5.0) == Coordinate(5.0, 8.0))

    }
}