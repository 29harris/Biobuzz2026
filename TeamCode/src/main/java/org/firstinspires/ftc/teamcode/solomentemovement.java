package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "solomentemovement")
public class solomentemovement extends LinearOpMode {

    public HWMap robot = new HWMap();

    private static final double MOTOR_TICKS_PER_REV = 560.0;
    private static final double MOTOR_MAX_RPM = 300.0;
    private static final double SHOOTER_TO_MOTOR_RATIO = 4.0;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);



        double driveSpeed = 1.0;


        waitForStart();

        while (opModeIsActive()) {
            // ----------------- INTAKE TOGGLE -----------------
            if (gamepad1.left_bumper) {
                robot.intake.setPower(-1);

            } else if (gamepad1.right_bumper) {
                robot.intake.setPower(1);
            } else {
                robot.intake.setPower(0);
            }
//
            // ----------------- DRIVETRAIN -----------------
            double y = -gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;


            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            robot.frontLeftDrive.setPower(frontLeftPower * driveSpeed);
            robot.backLeftDrive.setPower(backLeftPower * driveSpeed);
            robot.frontRightDrive.setPower(frontRightPower * driveSpeed);
            robot.backRightDrive.setPower(backRightPower * driveSpeed);
        }
    }
}