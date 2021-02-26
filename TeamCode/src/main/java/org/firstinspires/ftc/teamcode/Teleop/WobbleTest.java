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

        if (gamepad1.a) {
            h.wobbleArmPosition(0.2);
        }
        if (gamepad1.b) {
            h.wobbleArmPosition(0.33);
        }
        if (gamepad1.y) {
            h.wobbleArmPosition(0.66);
        }
        if (gamepad1.x) {
            h.wobbleArmPosition(0.57);
        }

        if (gamepad1.left_bumper) {

            h.wobbleClaw.setPosition(0);

        }
        if (gamepad1.right_bumper) {

            h.wobbleClaw.setPosition(1);

        }


    }
}
