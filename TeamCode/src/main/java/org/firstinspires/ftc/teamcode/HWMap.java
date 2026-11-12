package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HWMap {
    public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;
    public DcMotorEx flywheelMotor;
    public DcMotor intake, outtake;
    public Servo flywheelServo;

    public void init(HardwareMap hwMap) {
        frontLeftDrive  = hwMap.dcMotor.get("FLD");
        frontRightDrive = hwMap.dcMotor.get("FRD");
        backLeftDrive   = hwMap.dcMotor.get("BLD");
        backRightDrive  = hwMap.dcMotor.get("BRD");
//        feedServo       = hwMap.get(Servo.class, "feedServo");
        flywheelMotor     = hwMap.get(DcMotorEx.class, "FlywheelMotor");
        intake          = hwMap.get(DcMotor.class, "intake");
        flywheelServo   = hwMap.get(Servo.class, "flywheelServo");



        //fl = 0
        //fr = 1
        //bl =  2
        //br = 3



        //directions
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        flywheelMotor.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        frontLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //zero power behavior

        stopAll();
    }

    public void stopAll() {
        frontLeftDrive.setPower(0);
        frontRightDrive.setPower(0);
        backLeftDrive.setPower(0);
        backRightDrive.setPower(0);
        intake.setPower(0);
        flywheelServo.setPosition(0.55);
    }

    }
