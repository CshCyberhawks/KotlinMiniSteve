package frc.robot.util

class Polar {
    var theta = 0.0
    var r = 0.0

    constructor() {
        theta = 0.0
        r = 0.0
    }

    constructor(theta: Double, r: Double) {
        this.theta = theta
        this.r = r
    }

    fun equals(other: Polar): Boolean {
        return theta == other.theta && r == other.r
    }

    fun add(other: Polar): Polar {
        return Polar(theta + other.theta, r + other.r)
    }
}