package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class PerspectiveHardware {

    //IMU stuffs
    public BNO055IMU imu;
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
    HardwareMap hwMap = null;

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

        left1.setDirection(DcMotorSimple.Direction.REVERSE);
        left2.setDirection(DcMotorSimple.Direction.REVERSE);
        left3.setDirection(DcMotorSimple.Direction.REVERSE);

        right1.setDirection(DcMotorSimple.Direction.FORWARD);
        right2.setDirection(DcMotorSimple.Direction.FORWARD);
        right3.setDirection(DcMotorSimple.Direction.FORWARD);

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

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

    }

    public void leftMotorPower(double power) {

        left1.setPower(power);
        left2.setPower(power);
        left3.setPower(power);

    }

    public void rightMotorPower(double power) {

        right1.setPower(power);
        right2.setPower(power);
        right3.setPower(power);

    }

    public void resetEncoders() {
        left2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        left2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public int getLeftPosition() {
        return left2.getCurrentPosition();
    }

    public int getRightPosition() {
        return right2.getCurrentPosition();
    }

    public void setZeroBehavior(DcMotor.ZeroPowerBehavior behavior) {
        left1.setZeroPowerBehavior(behavior);
        left2.setZeroPowerBehavior(behavior);
        left3.setZeroPowerBehavior(behavior);
        right1.setZeroPowerBehavior(behavior);
        right2.setZeroPowerBehavior(behavior);
        right3.setZeroPowerBehavior(behavior);
    }

    /**
     * Returns the average encoder position between the two sides of the robot.
     *
     * @return an integer
     */
    public int getEncoderPosition() {
        return (int) (getLeftPosition() + getRightPosition() / 2.0);
    }

    public void wobbleArmPosition(double position) {
        wobble1.setPosition(position);
        wobble2.setPosition(1 - position);
    }

    public void wobbleArmUp() {
        wobbleArmPosition(0.24);
    }

    public void wobbleArmDown() {
        wobbleArmPosition(0.92);
    }

    public void wobbleArmVertical() {
        wobbleArmPosition(0.39);
    }

    public void wobbleGrip() {
        wobbleClaw.setPosition(0.92);
    }

    public void wobbleRelease() {
        wobbleClaw.setPosition(0.57);
    }

}
