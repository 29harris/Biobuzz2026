package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name = "teleop2")
public class teleop2 extends LinearOpMode {


    public HWMap robot = new HWMap();


    private static final double MOTOR_TICKS_PER_REV = 1200.0;
    private static final double MOTOR_MAX_RPM      = 1200.0;
    private static final double SHOOTER_TO_MOTOR_RATIO = 1.0;
    private static final double INTAKE_POWER = 0.1; // 0 to 1, lower is slower


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);


        DcMotorEx flywheelmotorOne = robot.flywheelOne;
        DcMotorEx flywheelmotorTwo = robot.flywheelTwo;
        Servo platformServo = robot.platformServo;
        Servo trapdoorServo = robot.trapdoorServo;
        Servo flywheelServo = robot.flywheelServo;






        flywheelmotorOne.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        flywheelmotorTwo.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);


        flywheelmotorOne.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        flywheelmotorTwo.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);


        flywheelmotorOne.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        flywheelmotorTwo.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);


        double P = 10.0;
        double I = 0.5;
        double D = 0.0;
        double F = 12.0;


        flywheelmotorOne.setVelocityPIDFCoefficients(P, I, D, F);


        double driveSpeed = 1.0;


        double startPosition = 0.5;


        platformServo.setPosition(startPosition + 15.0 / 180.0);


        double shooterRPM    = 6000;
        double minShooterRPM = 6000;
        double maxShooterRPM = 6000;


        boolean toggleStateFlywheel = false;
        boolean wasPressedFlywheel = false;


        boolean toggleStateIntake = false;
        boolean wasPressedIntake = false;


        boolean ToggleStatePollen = false;
        boolean PollenMode = false;


        boolean ToggleStateNectar = false;
        boolean NectarMode = false;


        double startPositionServo = 0.5;
        double startPositionTrapdoorServo = 0.5;
        double startPositionFlywheelServo = 0.5;




        boolean prevDpadUp = false;
        boolean prevDpadDown = false;


        waitForStart();


        while (opModeIsActive()) {


            // ----------------- INTAKE TOGGLE -----------------
            if (gamepad1.left_bumper && !wasPressedIntake) {
                toggleStateIntake = !toggleStateIntake;
            }
            wasPressedIntake = gamepad1.left_bumper;


            if (toggleStateIntake) {
                robot.intake.setPower(-INTAKE_POWER);
            } else {
                robot.intake.setPower(0);
            }


            if (gamepad1.dpad_right){
                robot.intake.setPower(INTAKE_POWER);
            }
//
            // ----------------- DRIVETRAIN -----------------
            double y = gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x * 1.1;
            double rx = -gamepad1.right_stick_x;




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
                shooterRPM += 100;
                if (shooterRPM > maxShooterRPM) shooterRPM = maxShooterRPM;
            }
            if (gamepad1.dpad_down && !prevDpadDown) {
                shooterRPM -= 100;
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


            double targetTicksPerSec = motorRPM * MOTOR_TICKS_PER_REV / 120.0;


            if (toggleStateFlywheel) {
                flywheelmotorOne.setVelocity(targetTicksPerSec);
                // Motor two follows motor one's power so it spins even without its own encoder
                flywheelmotorTwo.setPower(flywheelmotorOne.getPower());
            } else {
                flywheelmotorOne.setVelocity(0);
                flywheelmotorTwo.setPower(0);
            }
//-------------------POLLEN / NECTAR MODES ---------------------
            // Pollen toggles on A, Nectar on the left trigger (left bumper is the intake)
            if (gamepad1.a && !PollenMode) {
                ToggleStatePollen = !ToggleStatePollen;
            }
            PollenMode = gamepad1.a;


            if (gamepad1.left_trigger_pressed && !NectarMode) {
                ToggleStateNectar = !ToggleStateNectar;
            }
            NectarMode = gamepad1.left_trigger_pressed;


            // Nectar overrides Pollen so the platform servo is only set once per loop
            if (ToggleStateNectar) {
                platformServo.setPosition(startPositionServo + 25.0 / 180.0);
                trapdoorServo.setPosition(startPositionTrapdoorServo + 45.0 / 180.0);
                flywheelServo.setPosition(startPositionFlywheelServo + 90.0 / 180.0);
            } else {
                if (ToggleStatePollen) {
                    platformServo.setPosition(startPositionServo + 15.0 / 180.0);
                } else {
                    platformServo.setPosition(startPositionServo);
                }
                trapdoorServo.setPosition(startPositionTrapdoorServo);
                flywheelServo.setPosition(startPositionFlywheelServo);
            }


            // ----------------- TELEMETRY -----------------


            telemetry.addData("Intake Toggle (Controls Flipped)", toggleStateIntake ? "ON" : "OFF");




            double fly1_tps = flywheelmotorOne.getVelocity();
            double fly2_tps = flywheelmotorTwo.getVelocity();


            double fly1_motorRPM = fly1_tps * 120.0 / MOTOR_TICKS_PER_REV;
            double fly2_motorRPM = fly2_tps * 120.0 / MOTOR_TICKS_PER_REV;


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
            telemetry.addData("Fly1 power", flywheelmotorOne.getPower());
            telemetry.addData("Fly2 power", flywheelmotorTwo.getPower());


            telemetry.update();
        }
    }
}


