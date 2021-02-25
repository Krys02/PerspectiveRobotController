package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    // Hardware Map object
    public HardwareMap hwMap;

    // Servos
    public CRServo in1 = null;
    public CRServo in2 = null;

    // Servo names
    public String[] names = {"in1", "in2"};

    public Intake(HardwareMap ahwMap) {
        hwMap = ahwMap;
    }

    public void init() {

        in1 = hwMap.get(CRServo.class, names[0]);
        in2 = hwMap.get(CRServo.class, names[1]);

        in1.setDirection(DcMotorSimple.Direction.FORWARD);
        in2.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    /**
     * Sets the speed of the intake
     *
     * @param speed the speed to run at, between -1 and 1, negative runs out, positive runs in
     */
    public void setSpeed(double speed) {

        in1.setPower(speed);
        in2.setPower(speed);

    }

}
