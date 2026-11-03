package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "TeleopEmmeKenny2")
public class teleopEmmeKenny2 extends LinearOpMode {

    public HWMap robot = new HWMap();

    private static final double MOTOR_TICKS_PER_REV = 560.0;
    private static final double MOTOR_MAX_RPM      = 300.0;
    private static final double SHOOTER_TO_MOTOR_RATIO = 4.0;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        DcMotorEx flywheelmotorOne = robot.flywheelOne;
        DcMotorEx flywheelmotorTwo = robot.flywheelTwo;
        Servo flywheelServo = robot.flywheelServo;


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

        double startPositionFlywheelServo = 0.5;
        flywheelServo.setPosition(startPositionFlywheelServo + 15.0 / 180.0);



        boolean prevDpadUp = false;
        boolean prevDpadDown = false;

        waitForStart();

        while (opModeIsActive()) {
            // ----------------- INTAKE TOGGLE -----------------
//            if (gamepad1.left_bumper && !wasPressedIntake) {
//                toggleStateIntake = !toggleStateIntake;
//            }
            if (gamepad1.left_bumper) {
                robot.intake.setPower(0.5);
            } else {
                robot.intake.setPower(0);
            }
//
            // ----------------- DRIVETRAIN -----------------
            double y = -gamepad1.left_stick_y;
            double x = -gamepad1.left_stick_x * 1.1;
            double rx = gamepad1.right_stick_x;

            // Flip controls while intake toggle is ON

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower  = (y + x + rx) / denominator;
            double backLeftPower   = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower  = (y + x - rx) / denominator;

            robot.frontLeftDrive.setPower(frontLeftPower * driveSpeed);
            robot.backLeftDrive.setPower(backLeftPower * driveSpeed);
            robot.frontRightDrive.setPower(frontRightPower * driveSpeed);
            robot.backRightDrive.setPower(backRightPower * driveSpeed);

            // ----------------- SHOOTER SPEED ADJUST -----------------
            if (gamepad1.dpad_up && !prevDpadUp) {
                shooterRPM += 10;
                if (shooterRPM > maxShooterRPM) shooterRPM = maxShooterRPM;
            }
            if (gamepad1.dpad_down && !prevDpadDown) {
                shooterRPM -= 10;
                if (shooterRPM < minShooterRPM) shooterRPM = minShooterRPM;
            }
            prevDpadUp = gamepad1.dpad_up;
            prevDpadDown = gamepad1.dpad_down;

            // ----------------- FLYWHEEL TOGGLE -----------------
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
                flywheelServo.setPosition(startPositionFlywheelServo + 50.0 / 180.0);


            } else {
                flywheelmotorOne.setVelocity(0);
                flywheelmotorTwo.setVelocity(0);
                flywheelServo.setPosition(startPositionFlywheelServo + 15.0 / 180.0);

            }


            // ----------------- TELEMETRY -----------------


            double fly1_tps = flywheelmotorOne.getVelocity();
            double fly2_tps = flywheelmotorTwo.getVelocity();

            double fly1_motorRPM = fly1_tps * 60.0 / MOTOR_TICKS_PER_REV;
            double fly2_motorRPM = fly2_tps * 60.0 / MOTOR_TICKS_PER_REV;

            double fly1_shooterRPM = fly1_motorRPM * SHOOTER_TO_MOTOR_RATIO;
            double fly2_shooterRPM = fly2_motorRPM * SHOOTER_TO_MOTOR_RATIO;

            telemetry.addData("Target Shooter RPM", shooterRPM);
            telemetry.addData("Target Motor RPM", motorRPM);
            telemetry.addData("Target tps", targetTicksPerSec);

            telemetry.addData("Motor1 RPM", fly1_motorRPM);
            telemetry.addData("Motor2 RPM", fly2_motorRPM);
            telemetry.addData("Shooter1 RPM (est)", fly1_shooterRPM);
            telemetry.addData("Shooter2 RPM (est)", fly2_shooterRPM);

            telemetry.addData("Fly1 tps", fly1_tps);
            telemetry.addData("Fly2 tps", fly2_tps);
            telemetry.addData("Fly1 pos", flywheelmotorOne.getCurrentPosition());
            telemetry.addData("Fly2 pos", flywheelmotorTwo.getCurrentPosition());

            telemetry.update();
        }
    }
}