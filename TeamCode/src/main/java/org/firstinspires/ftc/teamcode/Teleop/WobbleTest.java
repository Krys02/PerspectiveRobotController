package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

@TeleOp(name = "Wobble Test", group = "Tests")
public class WobbleTest extends OpMode {

    PerspectiveHardware h = new PerspectiveHardware();

    public void init() {

        h.init(hardwareMap);

    }

    public void loop() {

        if (gamepad1.a) { // UP
            h.wobbleArmPosition(0.24);
        }
        if (gamepad1.b) { // DROPPING
            h.wobbleArmPosition(0.39);
        }
        if (gamepad1.y) { // DOWN
            h.wobbleArmPosition(0.92);
        }
        if (gamepad1.x) {
            h.wobbleArmPosition(0.57);
        }

        if (gamepad1.left_bumper) {
            // OPEN
            h.wobbleClaw.setPosition(0.6);

        }
        if (gamepad1.right_bumper) {
            // CLOSED
            h.wobbleClaw.setPosition(0.92);

        }


    }
}
