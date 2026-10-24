package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class HWMap {
    public DcMotor frontLeftDrive, frontRightDrive, backLeftDrive, backRightDrive;
    public DcMotorEx flywheelOne, flywheelTwo;
    public DcMotor intake, outtake;
    public Servo platformServo, flywheelServo,trapdoorServo;

    public void init(HardwareMap hwMap) {
        frontLeftDrive  = hwMap.dcMotor.get("FLD");
        frontRightDrive = hwMap.dcMotor.get("FRD");
        backLeftDrive   = hwMap.dcMotor.get("BLD");
        backRightDrive  = hwMap.dcMotor.get("BRD");
//        feedServo       = hwMap.get(Servo.class, "feedServo");
        flywheelOne     = hwMap.get(DcMotorEx.class, "flywheelOne");
        flywheelTwo     = hwMap.get(DcMotorEx.class, "flywheelTwo");
        intake          = hwMap.get(DcMotor.class, "intake");
        outtake         = hwMap.get(DcMotor.class, "outtake");
        platformServo   = hwMap.get(Servo.class, "platformServo");
        flywheelServo   = hwMap.get(Servo.class, "flywheelServo");
        trapdoorServo   = hwMap.get(Servo.class, "trapdoorServo");



        //fl = 0
        //fr = 1
        //bl =  2
        //br = 3



        //directions
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        backRightDrive.setDirection(DcMotor.Direction.REVERSE);
        flywheelOne.setDirection(DcMotor.Direction.REVERSE);
        flywheelTwo.setDirection(DcMotor.Direction.FORWARD);
        intake.setDirection(DcMotor.Direction.REVERSE);

        frontLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //zero power behavior

        stopAll();
    }

    public void stopAll() {

    }
}