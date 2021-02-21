package org.firstinspires.ftc.teamcode.Teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Hardware.MyGamepad;
import org.firstinspires.ftc.teamcode.Hardware.PerspectiveHardware;

public class Teleop extends OpMode {

    PerspectiveHardware hardware = new PerspectiveHardware();

    ElapsedTime timer = new ElapsedTime();

    MyGamepad controller = new MyGamepad(gamepad1);

    public void init(){
        hardware.init(hardwareMap);
        timer.reset();
    }

    public void init_loop(){

        telemetry.addLine("Waiting for start");
        telemetry.addData("Waiting time", timer.seconds());

    }

    public void start(){

        timer.reset();

    }

    public void loop(){

        hardware.drivetrain.setLeftPower(controller.driveInput + controller.rotateInput);
        hardware.drivetrain.setRightPower(controller.driveInput - controller.rotateInput);

        telemetry.addLine("Running");
        telemetry.addData("Elapsed time", timer.seconds());

    }

    public void stop(){

    }

}
