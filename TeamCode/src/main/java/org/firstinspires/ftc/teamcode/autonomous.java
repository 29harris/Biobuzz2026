package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Drive Forward", group = "Autonomous")
public class autonomous extends LinearOpMode {

    public HWMap robot = new HWMap();
    private static final double MOTOR_TICKS_PER_REV = 560.0;
    private static final double MOTOR_MAX_RPM = 300.0;
    private static final double SHOOTER_TO_MOTOR_RATIO = 4.0;
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        waitForStart();
        if (opModeIsActive()) {
            DcMotor frontLeftDrive = robot.frontLeftDrive;
            DcMotor frontRightDrive = robot.frontRightDrive;
            DcMotor backLeftDrive = robot.backLeftDrive;
            DcMotor backRightDrive = robot.backRightDrive;
            DcMotorEx flywheelmotor = robot.flywheelMotor;
            Servo flywheelServo = robot.flywheelServo;


            flywheelmotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

            flywheelmotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            flywheelmotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

            double P = 10.0;
            double I = 0.5;
            double D = 0.0;
            double F = 12.0;

            flywheelmotor.setVelocityPIDFCoefficients(P, I, D, F);

            double driveSpeed = 1.0;

            double shooterRPM = 650;
            double minShooterRPM = 100;
            double maxShooterRPM = 1300;

            boolean toggleStateFlywheel = false;
            boolean wasPressedFlywheel = false;
            double servoPos = 0.7;


            boolean prevDpadUp = false;
            boolean prevDpadDown = false;


            while (opModeIsActive()) {
                // ----------------- INTAKE TOGGLE -----------------
                double motorRPM = shooterRPM / SHOOTER_TO_MOTOR_RATIO;
                if (motorRPM > MOTOR_MAX_RPM) motorRPM = MOTOR_MAX_RPM;

                double targetTicksPerSec = motorRPM * MOTOR_TICKS_PER_REV / 60.0;

                flywheelmotor.setVelocity(targetTicksPerSec);

                // Drive for 2 seconds
                sleep(5000);

                flywheelServo.setPosition(0.7);
                robot.intake.setPower(-1);

                sleep(5000);


            }
        }
    }
}
