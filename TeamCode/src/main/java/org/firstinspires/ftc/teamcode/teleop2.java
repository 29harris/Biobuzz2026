package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "teleop2")
public class teleop2 extends LinearOpMode {

    public HWMap robot = new HWMap();

    private static final double MOTOR_TICKS_PER_REV = 560.0;
    private static final double MOTOR_MAX_RPM      = 300.0;
    private static final double SHOOTER_TO_MOTOR_RATIO = 4.0;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        DcMotorEx flywheelmotorOne = robot.flywheelOne;
        DcMotorEx flywheelmotorTwo = robot.flywheelTwo;

        flywheelmotorOne.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        flywheelmotorTwo.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        flywheelmotorOne.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        flywheelmotorTwo.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        flywheelmotorOne.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelmotorTwo.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        double P = 10.0;
        double I = 0.5;
        double D = 0.0;
        double F = 12.0;

        flywheelmotorOne.setVelocityPIDFCoefficients(P, I, D, F);
        flywheelmotorTwo.setVelocityPIDFCoefficients(P, I, D, F);

        double driveSpeed = 1.0;

        double shooterRPM    = 360;
        double minShooterRPM = 100;
        double maxShooterRPM = 1200;

        boolean toggleStateFlywheel = false;
        boolean wasPressedFlywheel = false;

        boolean toggleStateIntake = false;
        boolean wasPressedIntake = false;

        boolean prevDpadUp = false;
        boolean prevDpadDown = false;

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.left_bumper) {
                robot.intake.setPower(-1);
            } else if (gamepad1.dpad_right) {
                robot.intake.setPower(1);
            } else {
                robot.intake.setPower(0);
            }
            // ----------------- INTAKE TOGGLE -----------------
            if (gamepad1.left_bumper && !wasPressedIntake) {
                toggleStateIntake = !toggleStateIntake;
            }
            wasPressedIntake = gamepad1.left_bumper;

            if (toggleStateIntake) {
                robot.intake.setPower(-1);
            } else {
                robot.intake.setPower(0);
            }

            if (gamepad1.dpad_right){
                robot.intake.setPower(1);
            }

            //-----------------Outtake?----------------------

            if (gamepad1.right_bumper && !wasPressedFlywheel) {
                toggleStateFlywheel = !toggleStateFlywheel;
            }
            wasPressedFlywheel = gamepad1.right_bumper;

            double motorRPM = shooterRPM / SHOOTER_TO_MOTOR_RATIO;
            if (motorRPM > MOTOR_MAX_RPM) motorRPM = MOTOR_MAX_RPM;

            double targetTicksPerSec = motorRPM * MOTOR_TICKS_PER_REV / 60.0;

            if (toggleStateFlywheel) {
                flywheelmotorOne.setVelocity(targetTicksPerSec);
                flywheelmotorTwo.setVelocity(targetTicksPerSec);
            } else {
                flywheelmotorOne.setVelocity(0);
                flywheelmotorTwo.setVelocity(0);

            }

            }

            // ----------------- DRIVETRAIN -----------------
            double y = gamepad1.right_stick_y;
            double x = gamepad1.right_stick_x * 1.1;
            double rx = -gamepad1.left_stick_x;

            // Flip controls while intake toggle is ON
            boolean controlsFlipped = toggleStateIntake;
            if (controlsFlipped) {
                y = y;    // forward/back
                x = x;    // strafing
                rx = rx;  // rotation
            }

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower  = (y - x + rx) / denominator;
            double backLeftPower   = (y + x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower  = (y + x - rx) / denominator;

            robot.frontLeftDrive.setPower(frontLeftPower * driveSpeed);
            robot.backLeftDrive.setPower(backLeftPower * driveSpeed);
            robot.frontRightDrive.setPower(frontRightPower * driveSpeed);
            robot.backRightDrive.setPower(backRightPower * driveSpeed);


            // ----------------- FEED SERVO -----------------
//            if (gamepad1.x) {
//                robot.feedServo.setPosition(0);
//            } else {
//                robot.feedServo.setPosition(1);
//            }

            // ----------------- TELEMETRY -----------------


        }
    }

