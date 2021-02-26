package org.firstinspires.ftc.teamcode.Autonomous;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

@Autonomous(name = "Red High Goal And Wobble", group = "Final")
public class RedHighGoalAndWobble extends LinearOpMode {

    public double[] highGoalShotPosition = {0.88, 0.47};
    public double[] powerShotPosition = {0.8, 0.35};
    public double shooterBottom = 0.25;
    public double hopperBottom = 0;
    PerspectiveHardware h = new PerspectiveHardware();
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime runtime = new ElapsedTime();
    private double globalAngle, correction;
    private Orientation lastAngles = new Orientation();

    @Override
    public void runOpMode() {

        h.init(hardwareMap);
        h.setZeroBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        h.hopper.setPosition(0.85);
        timer.reset();
        h.flicker.setPosition(0);
        h.wobbleArmUp();
        h.wobbleGrip();
        waitForStart();

        resetAngle();

        drive(60, 0.7f, 5);
        h.shootTilt.setPosition(0.42);
        shootVelocity(1500);
        sleep(750);
        h.flicker.setPosition(1);
        sleep(250);
        h.flicker.setPosition(0);
        sleep(250);
        h.flicker.setPosition(1);
        sleep(250);
        h.flicker.setPosition(0);
        sleep(250);
        h.flicker.setPosition(1);
        sleep(250);
        h.flicker.setPosition(0);
        h.shootTilt.setPosition(shooterBottom);
        shootVelocity(0);
        drive(40, 0.7f, 5);
        globalAngle += 30;
        drive(15, 0.7f, 5);
        globalAngle -= 30;
        h.wobbleArmDown();
        sleep(250);
        h.wobbleRelease();
        sleep(50);
        h.wobbleArmUp();
        drive(-40, 1f, 4);

    }

    private void resetAngle() {
        lastAngles = h.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

    private double getAngle() {
        Orientation angles = h.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double deltaAngle = angles.firstAngle - lastAngles.firstAngle;
        if (deltaAngle < -180)
            deltaAngle += 360;
        else if (deltaAngle > 180)
            deltaAngle -= 360;
        globalAngle += deltaAngle;
        lastAngles = angles;
        return globalAngle;
    }

    private double rotatePower() {
        double power, angle, gain = 0.02, /* CHANGE THE GAIN TO CHANGE THE SENSITIVITY */ minPower = 0.1;
        angle = getAngle();
        power = angle;             // no adjustment.
        power = power * gain;
        if (Math.abs(power) < minPower) {
            if (power < 0)
                power = -minPower;
            if (power > 0)
                power = minPower;
        }
        return -power;
    }

    private void rotateForSecs(float degrees, float time, float power) {
        globalAngle += degrees;
        runtime.reset();
        while (opModeIsActive() && !isStopRequested() && runtime.seconds() < time) {
            correction = rotatePower();
            if (Math.abs(correction) > power) {
                if (correction > 0)
                    correction = power;
                if (correction < 0)
                    correction = -power;
            }
            h.leftMotorPower(correction);
            h.rightMotorPower(-correction);
            telemetry.addData("Status", "Rotating");
            telemetry.addData("Correction", correction);
            telemetry.addData("Rotate Power", rotatePower());
            telemetry.update();
        }
        h.leftMotorPower(0);
        h.rightMotorPower(0);
    }

    private double encoderDrivePower(int targetTicks, double maxPower) {
        int direction = 1;
        if (targetTicks < 0) {
            direction *= -1;
        }
        targetTicks = Math.abs(targetTicks);
        int encoderError = 0;
        double minDrivePower = 0.2;
        double ticks;
        double scale = (maxPower - minDrivePower) / (0.8 * targetTicks);
        double ticksToTrav;
        double ticksForMin = (0.8 * targetTicks);
        ticks = Math.abs(h.getEncoderPosition());
        ticksToTrav = (targetTicks - ticks) - (0.3 * targetTicks);
        double drivePower;
        if (ticks < targetTicks + (double) encoderError && ticks > targetTicks - (double) encoderError)
            drivePower = 0;             // no adjustment.
        else {
            drivePower = (scale * ticksToTrav) + minDrivePower;
        }
        Log.d("Power", "Scale " + scale);
        Log.d("Power", "To travel " + ticksToTrav);
        Log.d("Power", "Ticks for min " + ticksForMin);
        Log.d("Power", "Power" + drivePower);

        if (ticks > ticksForMin) {
            drivePower = minDrivePower;
        }
        return drivePower * direction;
    }

    private double checkDirection() {
        double correction, angle, gain = 0.03 /* CHANGE THE GAIN TO CHANGE THE SENSITIVITY */;
        angle = getAngle();
        correction = angle;             // no adjustment.
        correction = correction * gain;
        return -correction;
    }

    private void drive(float inches, float power, float timeOutS) {
//        double ticksPerMM = 0.916025;
//        int ticksPerIN = (int) (ticksPerMM * 25.4);
        int ticksPerIN = (int) (806 / 24.0);
        double targetTicks = -inches * ticksPerIN;
        double motorPower;
        h.resetEncoders();
        runtime.reset();
        while (opModeIsActive() && !isStopRequested() && runtime.seconds() < timeOutS) {
            int encoderPos = Math.abs(h.getEncoderPosition());
            if (encoderPos < Math.abs(targetTicks)) {
                motorPower = encoderDrivePower((int) targetTicks, power);
                telemetry.addData("Status", "Driving using encoders");
                telemetry.addData("Current Ticks", -encoderPos);
                telemetry.addData("Target Ticks", targetTicks);
                telemetry.addData("Current Motor Power", motorPower);

                correction = checkDirection();
//                correction = 0;
                telemetry.addData("Correction", correction);
                h.leftMotorPower(motorPower + correction);
                h.rightMotorPower(motorPower - correction);
                telemetry.update();

            } else {
                break;
//                h.leftMotorPower(0);
//                h.rightMotorPower(0);
            }
        }
        h.leftMotorPower(0);
        h.rightMotorPower(0);
    }

    @Override
    public void waitForStart() {
        while (!isStarted() && !isStopRequested()) {
            telemetry.addData("Status", "Waiting for start");
            telemetry.addData("Current wait time", timer.seconds());
        }
        runtime.reset();
    }

    public void shootVelocity(int velocity) {

        h.shoot1.setVelocity(velocity);
        h.shoot2.setVelocity(-velocity);

    }
}
