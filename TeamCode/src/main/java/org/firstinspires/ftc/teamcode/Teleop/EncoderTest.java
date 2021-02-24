package org.firstinspires.ftc.teamcode.Teleop;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

@TeleOp(name = "Encoder Test", group = "Tests")
public class EncoderTest extends OpMode {

    PerspectiveHardware h = new PerspectiveHardware();

    public void init() {

        h.init(hardwareMap);
        h.left1.setZeroPowerBehavior(FLOAT);
        h.left2.setZeroPowerBehavior(FLOAT);
        h.left3.setZeroPowerBehavior(FLOAT);
        h.right1.setZeroPowerBehavior(FLOAT);
        h.right2.setZeroPowerBehavior(FLOAT);
        h.right3.setZeroPowerBehavior(FLOAT);

    }

    public void loop() {

        telemetry.addLine("This OpMode will print the encoder positions of all 6 drive motors to the console of a wifi connected computer. This can be used to determine which of the six drive motors have an encoder connected to them.");
        telemetry.addData("Status", "Running");
        telemetry.update();

        Log.d("Encoder: ", "Left 1 encoder position " + h.left1.getCurrentPosition());
        Log.d("Encoder: ", "Left 2 encoder position " + h.left2.getCurrentPosition());
        Log.d("Encoder: ", "Left 3 encoder position " + h.left3.getCurrentPosition());
        Log.d("Encoder: ", "Right 1 encoder position " + h.right1.getCurrentPosition());
        Log.d("Encoder: ", "Right 2 encoder position " + h.right2.getCurrentPosition());
        Log.d("Encoder: ", "Right 3 encoder position " + h.right3.getCurrentPosition());

    }
}

