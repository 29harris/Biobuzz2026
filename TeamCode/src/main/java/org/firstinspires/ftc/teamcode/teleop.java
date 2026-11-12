
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "teleop")
public class teleop extends LinearOpMode {
    public HWMap robot =  new HWMap();


    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap);

        DcMotorEx flywheelmotor = robot.flywheelMotor;
        Servo flywheelServo = robot.flywheelServo;


        telemetry.update();
        waitForStart();
        double servoPos = 0;




        while (opModeIsActive()) {




            if (gamepad1.dpad_left){
                servoPos += .01;
            }
            else if (gamepad1.dpad_right){
                servoPos -= .01;
            }


            telemetry.addData("servo position", servoPos);
            flywheelServo.setPosition(servoPos);




        }




    }


}
