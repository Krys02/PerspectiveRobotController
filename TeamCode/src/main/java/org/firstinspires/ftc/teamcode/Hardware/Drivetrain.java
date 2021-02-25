package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

// A class that contains the robot hardware for the drivetrain
public class Drivetrain {

    // Hardware Map object
    public HardwareMap hwMap;

    // Left motors, the front of the robot is the intake
    public DcMotorEx left1 = null;
    public DcMotorEx left2 = null;
    public DcMotorEx left3 = null;
    // The names of the left motors in the config
    public String[] leftMotorNames = {"left1", "left2", "left3"};

    // Right motors, the front of the robot is the intake
    public DcMotorEx right1 = null;
    public DcMotorEx right2 = null;
    public DcMotorEx right3 = null;
    // The names of the right motors in the config
    public String[] rightMotorNames = {"right1", "right2", "right3"};

    public Drivetrain(HardwareMap ahwMap) {
        hwMap = ahwMap;
    }

    public void init() {
        assignMotorsToHardwareMap();
        setZeroPowerBehavior(left1, DcMotor.ZeroPowerBehavior.BRAKE);
        setZeroPowerBehavior(left2, DcMotor.ZeroPowerBehavior.BRAKE);
        setZeroPowerBehavior(left3, DcMotor.ZeroPowerBehavior.BRAKE);
        setZeroPowerBehavior(right1, DcMotor.ZeroPowerBehavior.BRAKE);
        setZeroPowerBehavior(right2, DcMotor.ZeroPowerBehavior.BRAKE);
        setZeroPowerBehavior(right3, DcMotor.ZeroPowerBehavior.BRAKE);

        setRunMode(left1, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRunMode(left2, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRunMode(left3, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRunMode(right1, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRunMode(right2, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRunMode(right3, DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    /**
     * Assigns 6 drivetrain motors to the virtual hardware map
     */
    void assignMotorsToHardwareMap() {
        left1 = hwMap.get(DcMotorEx.class, leftMotorNames[0]);
        left2 = hwMap.get(DcMotorEx.class, leftMotorNames[1]);
        left3 = hwMap.get(DcMotorEx.class, leftMotorNames[2]);
        right1 = hwMap.get(DcMotorEx.class, rightMotorNames[0]);
        right2 = hwMap.get(DcMotorEx.class, rightMotorNames[1]);
        right3 = hwMap.get(DcMotorEx.class, rightMotorNames[2]);
    }

    /**
     * Sets the zero power behavior of a motor
     *
     * @param motor    the target motor
     * @param behavior the target ZeroPowerBehavior
     */
    void setZeroPowerBehavior(DcMotorEx motor, DcMotor.ZeroPowerBehavior behavior) {
        motor.setZeroPowerBehavior(behavior);
    }

    /**
     * Sets the runmode of a motor
     *
     * @param motor   the target motor
     * @param runmode the target runmode
     */
    void setRunMode(DcMotorEx motor, DcMotor.RunMode runmode) {
        motor.setMode(runmode);
    }

    /**
     * Assigns a motor with a null (or other) object to one on the HardwareMap
     *
     * @param name the name of the motor in the hardware config given by the Driver Station app
     */
    void assignMotorToHardwareMap(String name) {
        DcMotorEx motor = hwMap.get(DcMotorEx.class, name);
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


}
