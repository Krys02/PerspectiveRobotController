package org.firstinspires.ftc.teamcode.Vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@TeleOp(name = "Vision test", group = "Tests")
public class VisionTest extends LinearOpMode {
    StackDetectionPipeline pipeline;

    PerspectiveHardware h = new PerspectiveHardware();

    @Override
    public void runOpMode() {

        h.webcam.setPipeline(new StackDetectionPipeline());

        h.webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // Edit @width and @height to fit your camera's resolution
                h.webcam.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }
        });

        waitForStart();

        while (opModeIsActive()) {
            /*
             * Send some stats to the telemetry
             */
            telemetry.addData("Frame Count", h.webcam.getFrameCount());
            telemetry.addData("FPS", String.format("%.2f", h.webcam.getFps()));
            telemetry.addData("Total frame time ms", h.webcam.getTotalFrameTimeMs());
            telemetry.addData("Pipeline time ms", h.webcam.getPipelineTimeMs());
            telemetry.addData("Overhead time ms", h.webcam.getOverheadTimeMs());
            telemetry.addData("Theoretical max FPS", h.webcam.getCurrentPipelineMaxFps());
            telemetry.update();

            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.addData("Position", pipeline.position);
            telemetry.update();


            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }
    }


}