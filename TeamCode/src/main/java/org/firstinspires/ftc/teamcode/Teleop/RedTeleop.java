package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

@TeleOp(name = "Red Teleop", group = "Final")
public class RedTeleop extends OpMode {

    // This is an array, index 0 is the hopper pos, index 1 is the shooter pos
    public double[] highGoalShotPosition = {0, 0};
    public double[] powerShotPosition = {0, 0};
    public double shooterBottom = 0.25;
    public double intakeSpeed = 0.0;
    /**
     * This array holds the wobble arm positions. index zero holds the position for the servo with just the gear,
     * index one holds the position for the servo with the arm attached.
     **/
    public double[] wobbleArmPosition = {0, 0};
    // This array holds claw positions, index 0 is open, index 1 is closed.
    public double[] wobbleClawPosition = {0, 0};
    public boolean shooting = false;
    public int[] shootingVelocity = {0, 0};
    public ShotType shot = ShotType.IDLE;
    public ShotPosition position = ShotPosition.RED_STRAIGHT;
    PerspectiveHardware h = new PerspectiveHardware();
    ElapsedTime timer = new ElapsedTime();
    /**
     * Red wall shots | high goal is (0.88, 0.49), velocity is 1500 tps | power shots are (0.82, 0.36), velocity is 1520 tps
     * Red straight shots | high goal is (0.87, 0.48), velocity is 1520 tps | power shots are (0.82, 0.38), velocity is 1520 tps
     * Red cross shots | high goal is (0.86, 0.46), velocity is 1500 tps | power shots are (0.82, 0.4), velocity is 1500 tps
     * Red center shots | high goal is (0.86, 0.46), velocity is  1500 tps| power shots (0.82, 0.38), velocity is 1500 tps
     */

    boolean xPressed = false;
    boolean aPressed = false;
    boolean yPressed = false;
    boolean wobbleDown = false;
    boolean wobbleGripped = false;
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
        telemetry.addData("Waiting time", ((int) (timer.seconds() / 60) < 10 ? 0 : "").toString() + (int) (timer.seconds() / 60) + ":" + ((timer.seconds() % 60) < 10 ? 0 : "") + (int) (timer.seconds() % 60));

    }

    public void start() {

        timer.reset();

    }

    WobbleState wobbleState = WobbleState.UP_EMTPY;

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

    int wobbleInt = 0;

    public void shooterRest() {
        h.hopper.setPosition(0);
        h.shootTilt.setPosition(shooterBottom);
        shootingVelocity[0] = 0;
        shootingVelocity[1] = 0;
        shot = ShotType.IDLE;
        shooting = false;
    }

    public void shootVelocity(int velocity) {

        h.shoot1.setVelocity(velocity);
        h.shoot2.setVelocity(-velocity);

    }

    public void setShot(double highGoalHopper, double highGoalShooter, int highGoalVelocity,
                        double powerShotHopper, double powerShotShooter, int powerShotVelocity) {

        highGoalShotPosition[0] = highGoalHopper;
        highGoalShotPosition[1] = highGoalShooter;
        shootingVelocity[0] = highGoalVelocity;
        powerShotPosition[0] = powerShotHopper;
        powerShotPosition[1] = powerShotShooter;
        shootingVelocity[1] = powerShotVelocity;

    }

    public void loop() {

        getShotPosition();
        wobbleStateMachine();

        setLeftPower(gamepad1.left_stick_y - gamepad1.right_stick_x);
        setRightPower(gamepad1.left_stick_y + gamepad1.right_stick_x);

        if (shooting)
            intakeSpeed = gamepad1.right_trigger > 0.1 ? -1 : gamepad1.left_trigger > 0.1 ? 1 : 0;
        else
            intakeSpeed = -1 + gamepad1.left_trigger * 2;
        intakePower(intakeSpeed);
        // High goal shot
        if (gamepad1.right_bumper && !rightBumpPressed) {
            rightBumpPressed = true;
            if (!shooting) {
                readyShot(ShotType.HIGH_GOAL);
            } else {
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
            } else {
                shooterRest();
            }
        }
        if (!gamepad1.left_bumper) {
            leftBumpPressed = false;
        }

        if (shooting) {
            shootVelocity(shot == ShotType.HIGH_GOAL ? shootingVelocity[0] : shootingVelocity[1]);
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

        telemetry.addData("Status", "Running");
        telemetry.addLine();
        telemetry.addData("Current Shot", position.toString() + " / " + shot.toString());
        telemetry.addLine();
        telemetry.addData("Shooter Speed", Math.abs(h.shoot1.getVelocity()));
        if (shot == ShotType.HIGH_GOAL) {
            telemetry.addData("Target Speed", shootingVelocity[0]);
            telemetry.addData("Shooter Status", (
                    Math.abs(h.shoot1.getVelocity() - shootingVelocity[0]) > 40 || shootingVelocity[0] == 0 ? "NOT READY" : "READY, Press 'a' to fire"
            ));
        } else {
            telemetry.addData("Target Speed", shootingVelocity[1]);
            telemetry.addData("Shooter Status", (
                    Math.abs(h.shoot1.getVelocity() - shootingVelocity[1]) > 40 || shootingVelocity[1] == 0 ? "NOT READY" : "READY, Press 'a' to fire"
            ));
        }
        telemetry.addLine();
        telemetry.addData("Wobble Goal State", wobbleState.toString());
        telemetry.addLine();
        telemetry.addData("Elapsed time", ((int) (timer.seconds() / 60) < 10 ? 0 : "").toString() + (int) (timer.seconds() / 60) + ":" + ((timer.seconds() % 60) < 10 ? 0 : "") + (int) (timer.seconds() % 60));

    }

    enum ShotType {
        HIGH_GOAL,
        POWER_SHOT,
        IDLE
    }

    public void readyShot(ShotType type) {

        switch (position) {
            case RED_WALL:
                setShot(0.88, 0.49, 1500,
                        0.82, 0.36, 1520);
                break;
            case RED_STRAIGHT:
                setShot(0.87, 0.48, 1520,
                        0.82, 0.38, 1520);
                break;
            case RED_CROSS:
                setShot(0.86, 0.46, 1500,
                        0.82, 0.4, 1500);
                break;
            case RED_CENTER:
                setShot(0.86, 0.46, 1500,
                        0.82, 0.38, 1500);
        }

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

    public void getShotPosition() {
        if (gamepad1.dpad_right)
            position = ShotPosition.RED_WALL;
        if (gamepad1.dpad_up)
            position = ShotPosition.RED_STRAIGHT;
        if (gamepad1.dpad_left)
            position = ShotPosition.RED_CROSS;
        if (gamepad1.dpad_up)
            position = ShotPosition.RED_CENTER;
    }

    public void wobbleStateMachine() {

        if (wobbleInt >= 6) {
            wobbleInt = 0;
        }
        if (wobbleInt <= -1) {
            wobbleInt = 5;
        }

        switch (wobbleInt) {
            case 0:
                wobbleState = WobbleState.UP_EMTPY;
                h.wobbleArmUp();
                h.wobbleRelease();
                break;
            case 1:
                wobbleState = WobbleState.DOWN_EMPTY;
                h.wobbleArmDown();
                h.wobbleRelease();
                break;
            case 2:
                wobbleState = WobbleState.DOWN_GRABBED;
                h.wobbleArmDown();
                h.wobbleGrip();
                break;
            case 3:
                wobbleState = WobbleState.UP_GRABBED;
                h.wobbleArmUp();
                h.wobbleGrip();
                break;
            case 4:
                wobbleState = WobbleState.DOWN_TO_RELEASE;
                h.wobbleArmPosition(0.3);
                h.wobbleGrip();
                break;
            case 5:
                wobbleState = WobbleState.DOWN_RELEASED;
                h.wobbleArmPosition(0.3);
                h.wobbleRelease();
                break;
        }

        if (gamepad1.x && !xPressed) {
            xPressed = true;
            wobbleInt++;
        }
        if (!gamepad1.x) {
            xPressed = false;
        }

        if (gamepad1.y && !yPressed) {
            yPressed = true;
            wobbleInt--;
        }
        if (!gamepad1.y) {
            yPressed = false;
        }
    }

    enum ShotPosition {
        RED_WALL,
        RED_STRAIGHT,
        RED_CROSS,
        RED_CENTER
    }

    enum WobbleState {
        UP_EMTPY,
        DOWN_EMPTY,
        DOWN_GRABBED,
        UP_GRABBED,
        DOWN_TO_RELEASE,
        DOWN_RELEASED
    }
}
