package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class PerspectiveHardware {

    HardwareMap hwMap = null;

    public Drivetrain drivetrain = null;
    public Intake intake = null;

    public PerspectiveHardware() {
    }

    public void init(HardwareMap ahwMap) {

        hwMap = ahwMap;

        // Initialize the drivetrain
        drivetrain = new Drivetrain(hwMap);
        drivetrain.init();

        // Init the intake
        intake = new Intake(hwMap);
        intake.init();

    }

}
