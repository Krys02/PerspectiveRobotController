package org.firstinspires.ftc.teamcode.Autonomous;


import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

@Autonomous(name = "Red 1", group = "Final")
public class Red1 extends LinearOpMode {

    PerspectiveHardware h = new PerspectiveHardware();
    ElapsedTime timer = new ElapsedTime();
    ElapsedTime runtime = new ElapsedTime();

    private double globalAngle, correction;

    // TODO: find ticks per by maths or empirically
    private final int ticksPerIN = 0;

    private final int encoderError = 0;
    private final double pX = 0.00005;
    private final double pRot = 0.02;
    private final double pCorr = 0.035;
    private final double minDrivePower = 0.19;
    private final double minRotPower = 0.1;
    private double drivePower = 0;
    private Orientation lastAngles = new Orientation();

    @Override
    public void runOpMode() {

        h.init(hardwareMap);
        timer.reset();
        waitForStart();

        resetAngle();

        // TODO: write this lol


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

    private double checkDirection() {
        double correction, angle, gain = pCorr;
        angle = getAngle();
        correction = angle;             // no adjustment.
        correction = correction * gain;
        return correction;
    }

    private double rotatePower() {
        double power, angle, gain = pRot, minPower = minRotPower;
        angle = getAngle();
        power = angle;             // no adjustment.
        power = power * gain;
        if (Math.abs(power) < minPower) {
            if (power < 0)
                power = -minPower;
            if (power > 0)
                power = minPower;
        }
        return power;
    }

    private void rotateForSecs(float degrees, float time, float power) {
        globalAngle += degrees;
        runtime.reset();
        while (opModeIsActive() && isStopRequested() && runtime.seconds() < time) {
            correction = rotatePower();
            if (Math.abs(correction) > power) {
                if (correction > 0)
                    correction = power;
                if (correction < 0)
                    correction = -power;
            }
            h.leftMotorPower(correction);
            h.rightMotorPower(-correction);
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
        double ticks, gain = pX, error = encoderError, minPower = minDrivePower;
        double scale = (maxPower - minPower) / (0.7 * targetTicks);
        double ticksToTrav;
        double ticksForMin = (0.7 * targetTicks);
        ticks = Math.abs(h.getEncoderPosition());
        ticksToTrav = (targetTicks - ticks) - (0.3 * targetTicks);
        if (ticks < targetTicks + error && ticks > targetTicks - error)
            drivePower = 0;             // no adjustment.
        else {
            drivePower = (scale * ticksToTrav) + minPower;
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

    private void drive(float inches, float power, float timeOutS) {
        double targetTicks = inches * ticksPerIN;
        double motorPower;
        h.resetEncoders();
        runtime.reset();
        while (opModeIsActive() && !isStopRequested() && runtime.seconds() < timeOutS) {
            int encoderPos = Math.abs(h.getEncoderPosition());
            if (encoderPos < Math.abs(targetTicks)) {
                motorPower = encoderDrivePower((int) targetTicks, power);
                telemetry.addData("Status", "Driving using encoders");
                telemetry.addData("Current Ticks", encoderPos);
                telemetry.addData("Target Ticks", targetTicks);
                telemetry.addData("Current Motor Power", motorPower);

                correction = checkDirection();
                h.leftMotorPower(motorPower + correction);
                h.rightMotorPower(motorPower - correction);
                telemetry.update();

            } else {
                break;
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
}
