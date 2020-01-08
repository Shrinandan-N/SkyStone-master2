package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="First", group="Iterative Opmode")
public class FinalTeleOp extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    DcMotor leftFront;
    DcMotor rightFront;
    DcMotor leftBack;
    DcMotor rightBack;

    //block picking up mech
    DcMotor liftBoi;
    Servo dropBlock;
    Servo liftMech;

    //moving the foundation mech
    Servo moverBoi;

    //Color sensor boi
    ColorSensor colorSensor;

    int encoderMin = 0;
    int encoderMax = 400;
    //private DcMotor latchMech = null;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");



        leftFront  = hardwareMap.get(DcMotor.class, "tl_motor");
        rightFront = hardwareMap.get(DcMotor.class, "tr_motor");
        leftBack = hardwareMap.get(DcMotor.class, "bl_motor");
        rightBack = hardwareMap.get(DcMotor.class, "br_motor");

        liftBoi = hardwareMap.get(DcMotor.class, "liftBoi");
        dropBlock = hardwareMap.get(Servo.class, "dropBlock");
        liftMech = hardwareMap.get(Servo.class, "liftMech");

        moverBoi = hardwareMap.get(Servo.class, "moverBoi");

        colorSensor = hardwareMap.get(ColorSensor.class, "scannyBoi");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);


//        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftBoi.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftBoi.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




        telemetry.addData("Status", "WE ARE INNNNN");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    double PowerX = 0;
    double PowerY = 0;
    @Override
    public void loop() {

        double xPos = -PowerX*(gamepad1.left_stick_x);
        double yPos = PowerY*(gamepad1.left_stick_y);
        double rot = 0.25 * -gamepad1.right_stick_x;

        if(Math.abs(gamepad1.left_stick_x) > 0) {
            PowerX += 0.01;
        }else{
            PowerX = 0;
        }

        if(Math.abs(gamepad1.left_stick_y) > 0) {
            PowerY += 0.01;
        }else{
            PowerY = 0;
        }
        leftFront.setPower(xPos + yPos + rot);
        rightFront.setPower(-xPos + yPos - rot);
        leftBack.setPower(-xPos + yPos + rot);
        rightBack.setPower(xPos + yPos - rot);

        if(gamepad1.a) {
            moverBoi.setPosition(1);

        }else if(gamepad1.b){
            moverBoi.setPosition(0.3);
        }

        if(gamepad1.y){
            dropBlock.setPosition(1);
        }else if(gamepad1.x){
            dropBlock.setPosition(0);
        }

        if(gamepad1.dpad_up){

            if(liftBoi.getCurrentPosition() < 400){
                liftBoi.setPower(0.1);
            }
        }else if(gamepad1.dpad_down){

            if(liftBoi.getCurrentPosition() > 0){
                liftBoi.setPower(-0.1);
            }
        }else{
            liftBoi.setPower(0);
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }




}
