package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PerspectiveHardware {

    HardwareMap hwMap = null;

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

    public PerspectiveHardware() {
    }

    public void init(HardwareMap ahwMap) {

        hwMap = ahwMap;

        left1 = hwMap.get(DcMotorEx.class, "left1");
        left2 = hwMap.get(DcMotorEx.class, "left2");
        left3 = hwMap.get(DcMotorEx.class, "left3");
        right1 = hwMap.get(DcMotorEx.class, "right1");
        right2 = hwMap.get(DcMotorEx.class, "right2");
        right3 = hwMap.get(DcMotorEx.class, "right3");

        shoot1 = hwMap.get(DcMotorEx.class, "shoot1");
        shoot2 = hwMap.get(DcMotorEx.class, "shoot2");

        in1 = hwMap.get(CRServo.class, "in1");
        in2 = hwMap.get(CRServo.class, "in2");

        hopper = hwMap.get(Servo.class, "hopper");

        flicker = hwMap.get(Servo.class, "flicker");

        wobble1 = hwMap.get(Servo.class, "wobble1");
        wobble2 = hwMap.get(Servo.class, "wobble2");
        wobbleClaw = hwMap.get(Servo.class, "wobbleClaw");

        shootTilt = hwMap.get(Servo.class, "shootTilt");

    }



}
