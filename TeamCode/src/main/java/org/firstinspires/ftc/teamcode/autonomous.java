package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Autonomous(name = "Drive Forward", group = "Autonomous")
public class autonomous extends LinearOpMode {

    public HWMap robot = new HWMap();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        if (opModeIsActive()) {
            DcMotor frontLeftDrive = robot.frontLeftDrive;
            DcMotor frontRightDrive = robot.frontRightDrive;
            DcMotor backLeftDrive = robot.backLeftDrive;
            DcMotor backRightDrive = robot.backRightDrive;

        waitForStart();

            robot.frontLeftDrive.setPower(-0.5);
            robot.frontRightDrive.setPower(-0.5);
            robot.backLeftDrive.setPower(0.5);
            robot.backRightDrive.setPower(0.5);

            // Drive for 2 seconds
            sleep(2000);

            robot.frontLeftDrive.setPower(0);
            robot.frontRightDrive.setPower(0);
            robot.backLeftDrive.setPower(0);
            robot.backRightDrive.setPower(0);
        }
    }
}
