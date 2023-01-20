package frc.robot.util

import edu.wpi.first.apriltag.AprilTagDetection
import edu.wpi.first.apriltag.AprilTagPoseEstimator
import edu.wpi.first.cameraserver.CameraServer
import edu.wpi.first.cscore.VideoSource
import edu.wpi.first.math.geometry.Translation3d
import org.opencv.core.Mat

fun vectorToTag(estimator: AprilTagPoseEstimator, tag: AprilTagDetection): Translation3d {
    return estimator.estimate(tag).translation
}

fun imageFromCamera(camera: VideoSource, width: Int, height: Int): Mat {
    val image: Mat = Mat(height, width, 0) // change the type later

    CameraServer.getVideo().grabFrame(image)

    return image
}
