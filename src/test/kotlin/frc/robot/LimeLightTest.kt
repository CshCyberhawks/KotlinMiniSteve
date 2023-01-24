package frc.robot.subsystems

import frc.robot.util.Vector2
import org.junit.Assert.assertEquals
import org.junit.Test

class LimeLightTest {
    @Test
    fun getPositionStraight() {
        val limelight = Limelight(0.711, 0.24, 40.0)
        assert(limelight.getPosition(1.0, 0.0, 0.0) == Vector2(1.0, 0.0))
    }

    @Test
    fun getPositionCurvy() {
        val limelight = Limelight(0.711, 0.24, 40.0)
        assertEquals(limelight.getPosition(1.0, 5.0, 0.0).x, .996, .05)
        assertEquals(limelight.getPosition(1.0, 5.0, 0.0).y, .087, .05)
    }

    @Test
    fun getPositionCurvyAndAdd() {
        val limelight = Limelight(0.711, 0.24, 40.0)
        assertEquals(limelight.getPositionAndAddRobot(1.0, 5.0, 0.0, Vector2(1.0, 1.0)).x, 1.996, .05)
        assertEquals(limelight.getPositionAndAddRobot(1.0, 5.0, 0.0, Vector2(1.0, 1.0)).y, 1.087, .05)
    }

    @Test
    fun everything() {
        val limelight = Limelight(0.711, 0.24, 40.0)
        assertEquals(limelight.getPositionAndAddRobot(1.0, -15.0, 17.0, Vector2(1.7, -2.0)).x, 2.69, .05)
        assertEquals(limelight.getPositionAndAddRobot(1.0, -15.0, 17.0, Vector2(1.7, -2.0)).y, -1.965, .05)
    }

}
