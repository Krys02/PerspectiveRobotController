package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.Gamepad;

public class MyGamepad {

    public Gamepad gamepad = null;

    public MyGamepad(Gamepad agamepad) {
        gamepad = agamepad;
    }

    public double driveInput = -gamepad.left_stick_y;
    public double rotateInput = gamepad.right_stick_x;

}
