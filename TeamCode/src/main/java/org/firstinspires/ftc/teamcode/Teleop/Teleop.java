package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.MyGamepad;
import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

@TeleOp(name="Teleop", group="Final")
public class Teleop extends OpMode {

    PerspectiveHardware hardware = new PerspectiveHardware();

    ElapsedTime timer = new ElapsedTime();

    public DcMotorEx left1 = null;
    public DcMotorEx left2 = null;
    public DcMotorEx left3 = null;
    public DcMotorEx right1 = null;
    public DcMotorEx right2 = null;
    public DcMotorEx right3 = null;

    public DcMotorEx shoot1 = null;
    public DcMotorEx shoot2 = null;

    public CRServo in1 = null;
    public CRServo in2 = null;

    public Servo hopper = null;

    public Servo flicker = null;

    public Servo wobble1 = null;
    public Servo wobble2 = null;
    public Servo wobbleClaw = null;

    public Servo shootTilt = null;

    // This is an array, index 0 is the hopper pos, index 1 is the shooter pos
    public double highGoalShotPosition[] = {0, 0};

    public double shooterBottom = 0.25;

    public void init(){

        left1 = hardwareMap.get(DcMotorEx.class, "left1");
        left2 = hardwareMap.get(DcMotorEx.class, "left2");
        left3 = hardwareMap.get(DcMotorEx.class, "left3");
        right1 = hardwareMap.get(DcMotorEx.class, "right1");
        right2 = hardwareMap.get(DcMotorEx.class, "right2");
        right3 = hardwareMap.get(DcMotorEx.class, "right3");

        shoot1 = hardwareMap.get(DcMotorEx.class, "shoot1");
        shoot2 = hardwareMap.get(DcMotorEx.class, "shoot2");

        in1 = hardwareMap.get(CRServo.class, "in1");
        in2 = hardwareMap.get(CRServo.class, "in2");

        hopper = hardwareMap.get(Servo.class,"hopper");

        flicker = hardwareMap.get(Servo.class,"flicker");
        flicker.setPosition(0.2);

        wobble1 = hardwareMap.get(Servo.class,"wobble1");
        wobble2 = hardwareMap.get(Servo.class,"wobble2");
        wobbleClaw = hardwareMap.get(Servo.class,"wobbleClaw");

        shootTilt = hardwareMap.get(Servo.class, "shootTilt");

    }

    public void init_loop(){

        telemetry.addLine("Waiting for start");
        telemetry.addData("Waiting time", timer.seconds());

    }

    public void start(){

        timer.reset();

    }

    boolean xPressed = false;
    boolean flicking = false;
    ElapsedTime flickTimer = new ElapsedTime();

    public void loop(){

        setLeftPower(-gamepad1.left_stick_y + gamepad1.right_stick_x);
        setRightPower(gamepad1.left_stick_y + gamepad1.right_stick_x);

        if (gamepad1.dpad_down) {
            intakePower(1);
        }
        if (gamepad1.dpad_up) {
            intakePower(-1);
        }
        if (gamepad1.dpad_left || gamepad1.dpad_right) {
            intakePower(0);
        }

        if (gamepad1.right_bumper) {
            shootSpeed(1);
        }
        if (gamepad1.left_bumper){
            shootSpeed(0);
        }

        if (gamepad1.a) {
            hopper.setPosition(1);
        }
        if (gamepad1.b) {
            hopper.setPosition(0);
        }


        if (gamepad1.x && !xPressed){
            xPressed = true;
            flicking = true;
            flickTimer.reset();
            flicker.setPosition(1);
        }
        if (!gamepad1.x) {
            xPressed = false;
        }
        if (flicking) {
            if (flickTimer.seconds() < 0.4) {
                flicker.setPosition(0);
                flicking = false;
            }
        }


        telemetry.addLine("Running");
        telemetry.addData("Elapsed time", timer.seconds());

    }

    public void stop(){

    }

    public void setLeftPower(double power) {

        left1.setPower(power);
        left2.setPower(power);
        left3.setPower(power);

    }

    public void setRightPower(double power) {

        right1.setPower(power);
        right2.setPower(power);
        right3.setPower(power);

    }

    public void intakePower (double power) {

        in1.setPower(power);
        in2.setPower(-power);

    }

    public void shootSpeed (double power) {

        shoot1.setPower(power);
        shoot2.setPower(-power);

    }

}
