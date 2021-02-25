package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

@TeleOp(name = "Shooting test", group = "Tests")
public class ShootingTest extends OpMode {

    double hopperPos = 0.5, shooterPos = 0.5;

    PerspectiveHardware h = new PerspectiveHardware();

    boolean flickerActuated = false;

    ElapsedTime flickerTime = new ElapsedTime();
    double flickTime = 0.25;
    boolean flicking = false;
    boolean xPressed = false;
    boolean upPressed = false, downPressed = false, yPressed = false, aPressed = false;
    boolean leftPressed = false, rightPressed = false;
    int velocity = 1500;

    public void init() {

        h.init(hardwareMap);

    }

    public void loop() {


        if (gamepad1.x && !xPressed && !flicking) {
            aPressed = true;
            flickerTime.reset();
            flicking = true;
        }
        if (flicking) {
            if (flickerTime.seconds() > flickTime) {
                flicking = false;
                flickerActuated = false;
            } else {
                flickerActuated = true;
            }
        }
        if (!gamepad1.x) {
            xPressed = false;
        }
        h.flicker.setPosition(flickerActuated ? 1 : 0);

        if (gamepad1.dpad_up && !upPressed) {
            upPressed = true;
            hopperPos += 0.02;
        }
        if (gamepad1.dpad_down && !downPressed) {
            downPressed = true;
            hopperPos -= 0.02;
        }

        if (gamepad1.y && !yPressed) {
            yPressed = true;
            shooterPos += 0.02;
        }
        if (gamepad1.a && !aPressed) {
            aPressed = true;
            shooterPos -= 0.02;
        }

        if (!gamepad1.dpad_up) {
            upPressed = false;
        }
        if (!gamepad1.dpad_down) {
            downPressed = false;
        }
        if (!gamepad1.y) {
            yPressed = false;
        }
        if (!gamepad1.a) {
            aPressed = false;
        }

        h.hopper.setPosition(hopperPos);
        h.shootTilt.setPosition(shooterPos);

        if (gamepad1.dpad_right && !rightPressed) {
            rightPressed = true;
            velocity += 20;
        }

        if (gamepad1.dpad_left && !leftPressed) {
            leftPressed = true;
            velocity -= 20;
        }
        if (!gamepad1.dpad_left) {
            leftPressed = false;
        }
        if (!gamepad1.dpad_right) {
            rightPressed = false;
        }


        if (gamepad1.right_bumper) {
            shootVelocity(velocity);
        }
        if (gamepad1.left_bumper) {
            shootVelocity(0);
        }

        h.leftMotorPower((gamepad1.left_stick_y - gamepad1.right_stick_x) / 2);
        h.rightMotorPower((gamepad1.left_stick_y + gamepad1.right_stick_x) / 2);

        telemetry.addData("Status", "Running");
        telemetry.addLine();
        telemetry.addData("Hopper pos", hopperPos);
        telemetry.addLine("Press 'a' and 'y' to adjust hopper up and down");
        telemetry.addLine();
        telemetry.addData("Shoot pos", shooterPos);
        telemetry.addLine("Press 'up' and 'down' to adjust shooter up and down");
        telemetry.addLine();
        telemetry.addData("Target velocity", velocity);
        telemetry.addLine("Press 'left' and 'right' to adjust velocity");
        telemetry.addLine();
        telemetry.addLine("Press 'x' to actuate the flicker");
        telemetry.addLine();
        telemetry.addData("Shooter velocity", Math.abs(h.shoot1.getVelocity()));
        telemetry.update();

    }

    public void shootSpeed(double power) {

        h.shoot1.setPower(power);
        h.shoot2.setPower(-power);

    }

    public void shootVelocity(int velocity) {

        h.shoot1.setVelocity(velocity);
        h.shoot2.setVelocity(-velocity);

    }
}
