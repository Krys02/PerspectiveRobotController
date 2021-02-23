package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Shooting test", group="Tests")
public class ShootingTest extends OpMode {

    double hopperPos, shooterPos;

    public Servo hopper = null;

    public Servo flicker = null;

    public Servo shootTilt = null;

    public DcMotorEx shoot1 = null;
    public DcMotorEx shoot2 = null;


    public void init () {

        hopper = hardwareMap.get(Servo.class,"hopper");

        flicker = hardwareMap.get(Servo.class,"flicker");

        shootTilt = hardwareMap.get(Servo.class, "shootTilt");

        shootTilt.setPosition(0.5);
        hopper.setPosition(0.5);

        shoot1 = hardwareMap.get(DcMotorEx.class, "shoot1");
        shoot2 = hardwareMap.get(DcMotorEx.class, "shoot2");


    }

    boolean xPressed = false;
    boolean flicking = false;
    ElapsedTime flickTimer = new ElapsedTime();

    boolean upPressed = false, downPressed = false, yPressed = false, aPressed = false;

    public void loop () {

//        if (gamepad1.x && !xPressed){
//            xPressed = true;
//            flickTimer.reset();
//            flicking = true;
//
//            flicker.setPosition(1);
//        }
//        if (!gamepad1.x) {
//            xPressed = false;
//        }
//        if (flicking) {
//            if (flickTimer.seconds() < 0.4) {
//                flicker.setPosition(0);
//                flicking = false;
//            }
//        }
        if (gamepad1.x) {flicker.setPosition(1);}
        if (gamepad1.b) {flicker.setPosition(0);}

        if (gamepad1.dpad_up && !upPressed) { upPressed = true; hopperPos += 0.05; }
        if (gamepad1.dpad_down && !downPressed) { downPressed = true; hopperPos -= 0.05; }

        if (gamepad1.y && !yPressed) { yPressed = true; shooterPos += 0.05; }
        if (gamepad1.a && !aPressed) { aPressed = true; shooterPos -= 0.05; }

        if (!gamepad1.dpad_up) { upPressed = false; }
        if (!gamepad1.dpad_down) { downPressed = false; }
        if (!gamepad1.y) { yPressed = false; }
        if (!gamepad1.a) { aPressed = false; }

        hopper.setPosition(hopperPos);
        shootTilt.setPosition(shooterPos);

        if (gamepad1.right_bumper) {
            shootVelocity(1600);
        }
        if (gamepad1.left_bumper){
            shootVelocity(0);
        }

        telemetry.addData("Hopper pos", hopperPos);
        telemetry.addData("Shoot pos", shooterPos);
        telemetry.addData("Shooter1 velocity", shoot1.getVelocity());
        telemetry.addData("Shooter2 velocity", shoot2.getVelocity());
        telemetry.update();

    }

    public void shootSpeed (double power) {

        shoot1.setPower(power);
        shoot2.setPower(-power);

    }
    public void shootVelocity (int velocity) {

        shoot1.setVelocity(velocity);
        shoot2.setVelocity(-velocity);

    }
}
