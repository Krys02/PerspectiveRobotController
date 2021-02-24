package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

@TeleOp(name = "Teleop", group = "Final")
public class Teleop extends OpMode {

    PerspectiveHardware h = new PerspectiveHardware();

    ElapsedTime timer = new ElapsedTime();

    // This is an array, index 0 is the hopper pos, index 1 is the shooter pos
    public double[] highGoalShotPosition = {0.88, 0.47};
    public double[] powerShotPosition = {0.8, 0.35};

    public double shooterBottom = 0.25;
    public double hopperBottom = 0;

    public double intakeSpeed = 0.0;

    /**
     * This array holds the wobble arm positions. index zero holds the position for the servo with just the gear,
     * index one holds the position for the servo with the arm attached.
     **/
    public double[] wobbleArmPosition = {0, 0};
    // This array holds claw positions, index 0 is open, index 1 is closed.
    public double[] wobbleClawPosition = {0, 0};

    public boolean shooting = false;
    public int shooterSpeed = 1540;

    public ShotType shot = ShotType.HIGH_GOAL;

    enum ShotType {
        HIGH_GOAL,
        POWER_SHOT
    }

    boolean xPressed = false, aPressed = false, yPressed = false, bPressed = false;

    boolean leftBumpPressed = false, rightBumpPressed = false;

    boolean flickerActuated = false;

    ElapsedTime flickerTime = new ElapsedTime();
    double flickTime = 0.25;
    boolean flicking = false;

    public void init() {

        h.init(hardwareMap);
        h.hopper.setPosition(0);
        h.flicker.setPosition(0);
        h.shootTilt.setPosition(shooterBottom);

    }

    public void init_loop() {

        telemetry.addLine("Waiting for start");
        telemetry.addData("Waiting time", timer.seconds());

    }

    public void start() {

        timer.reset();

    }

    public void loop() {

        setLeftPower(gamepad1.left_stick_y - gamepad1.right_stick_x);
        setRightPower(gamepad1.left_stick_y + gamepad1.right_stick_x);

        intakeSpeed = gamepad1.right_trigger > 0 ? -1 : gamepad1.left_trigger > 0 ? 1 : 0;
        intakePower(intakeSpeed);
        // High goal shot
        if (gamepad1.right_bumper && !rightBumpPressed) {
            rightBumpPressed = true;
            if (!shooting) {
                readyShot(ShotType.HIGH_GOAL);
            } else if (shooting) {
                shooterRest();
            }
        }
        if (!gamepad1.right_bumper) {
            rightBumpPressed = false;
        }
        // Powershot shot
        if (gamepad1.left_bumper && !leftBumpPressed) {
            leftBumpPressed = true;
            if (!shooting) {
                readyShot(ShotType.POWER_SHOT);
            } else if (shooting) {
                shooterRest();
            }
        }
        if (!gamepad1.left_bumper) {
            leftBumpPressed = false;
        }

        if (shooting) {
            shootVelocity(shooterSpeed);
        } else {
            shootVelocity(0);
        }

        if (shooting) {
            if (gamepad1.a && !aPressed && !flicking) {
                aPressed = true;
                flickerTime.reset();
                flicking = true;
            }
        }
        if (flicking) {
            if (flickerTime.seconds() > flickTime) {
                flicking = false;
                flickerActuated = false;
            } else {
                flickerActuated = true;
            }
        }


        if (!gamepad1.a) {
            aPressed = false;
        }
        h.flicker.setPosition(flickerActuated ? 1 : 0);

        telemetry.addData("Shooter Speed", Math.abs(h.shoot1.getVelocity()));
        telemetry.addLine("Running");
        telemetry.addData("Elapsed time", timer.seconds());

    }

    public void stop() {

    }

    public void setLeftPower(double power) {

        h.left1.setPower(power);
        h.left2.setPower(power);
        h.left3.setPower(power);

    }

    public void setRightPower(double power) {

        h.right1.setPower(power);
        h.right2.setPower(power);
        h.right3.setPower(power);

    }

    public void intakePower(double power) {

        h.in1.setPower(power);
        h.in2.setPower(-power);

    }

    public void shootSpeed(double power) {

        h.shoot1.setPower(power);
        h.shoot2.setPower(-power);

    }

    public void readyShot(ShotType type) {
        if (type == ShotType.POWER_SHOT) {
            shot = ShotType.POWER_SHOT;
            h.hopper.setPosition(powerShotPosition[0]);
            h.shootTilt.setPosition(powerShotPosition[1]);
            shooting = true;
        } else if (type == ShotType.HIGH_GOAL) {
            shot = ShotType.HIGH_GOAL;
            h.hopper.setPosition(highGoalShotPosition[0]);
            h.shootTilt.setPosition(highGoalShotPosition[1]);
            shooting = true;
        }
    }

    public void shooterRest() {
        h.hopper.setPosition(0);
        h.shootTilt.setPosition(shooterBottom);
        shooting = false;
    }

    public void shootVelocity(int velocity) {

        h.shoot1.setVelocity(velocity);
        h.shoot2.setVelocity(-velocity);

    }
}
