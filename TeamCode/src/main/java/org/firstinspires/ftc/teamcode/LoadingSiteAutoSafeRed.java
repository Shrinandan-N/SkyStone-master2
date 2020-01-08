package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="AutoLoadingSideRed", group="Linear Opmode")
public class LoadingSiteAutoSafeRed extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private ColorSensor colorSensor;
    private DcMotor leftFront = null;
    private DcMotor rightFront = null;
    private DcMotor leftBack = null;
    private DcMotor rightBack= null;
    private Servo moverBoi = null;
    private DcMotor liftBoi;
    private Servo placeBoi;


    DcMotor[] driveMotors = new DcMotor[]{leftFront, leftBack, rightBack, rightFront};

    static final double COUNTS_PER_REV = 48;
    static final double DIAMETER = 4;
    static final double COUNTS_INCH = COUNTS_PER_REV / DIAMETER * Math.PI ;



    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        leftFront  = hardwareMap.get(DcMotor.class, "tl_motor");
        rightFront = hardwareMap.get(DcMotor.class, "tr_motor");
        leftBack = hardwareMap.get(DcMotor.class, "bl_motor");
        rightBack = hardwareMap.get(DcMotor.class, "br_motor");
        moverBoi = hardwareMap.get(Servo.class, "moverBoi");
        colorSensor = hardwareMap.get(ColorSensor.class, "scannyBoi");
        liftBoi = hardwareMap.get(DcMotor.class, "liftBoi");
        placeBoi = hardwareMap.get(Servo.class, "dropBlock");



        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

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



        waitForStart();
        runtime.reset();



        while(opModeIsActive()) {
            telemetry.addData("Status", "WOOOOOt");
            telemetry.update();

            linearSlideUp();


            telemetry.addData("Status", "done");
            drive(34, 0.3, "forward");

            sleep(1000);

            telemetry.addData("Status", "moving servo down");
            moverBoi.setPosition(0.3);

            sleep(2000);

            telemetry.addData("Status", "backward");
            drive(32, 0.3, "backwards");

            sleep(2000);

            telemetry.addData("Status", "moving servo up");
            moverBoi.setPosition(1);

            sleep(1000);


            telemetry.addData("Status", "rightt");
            drive(40, 0.6, "left");

            linearSlideDown();

            placeBoi.setPosition(0.2);

            drive(8, 0.6, "left");

            stop();

        }



    }

    public void linearSlideUp(){

        int target = liftBoi.getCurrentPosition() + 350;

        telemetry.addData("Status", "inside");

        while(liftBoi.getCurrentPosition() < 500 && liftBoi.getCurrentPosition() < target){
            liftBoi.setPower(0.1);
        }
        liftBoi.setPower(0);

    }

    public void linearSlideDown(){

        int target = liftBoi.getCurrentPosition() - 310;

        while(liftBoi.getCurrentPosition() > 0 && liftBoi.getCurrentPosition() > target){
            liftBoi.setPower(-0.1);
        }
        liftBoi.setPower(0);

    }




    public void drive(double distance, double speed, String str){



        boolean leftBackB = true;
        boolean rightBackB = true;
        boolean leftFrontB = true;
        boolean rightFrontB = true;

        int targetLeftFront = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH );
        int targetRightFront = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH );
        int targetLeftBack = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH );
        int targetRightBack = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH );

        int errorLB = 7*(targetLeftBack/8);
        int errorRB = 7*(targetRightBack/8);
        int errorRF = 7*(targetRightFront/8);
        int errorLF = 7*(targetLeftFront/8);

        if(str.equals("forward")) {
            while (targetLeftFront > leftFront.getCurrentPosition() &&
                    targetRightFront > rightFront.getCurrentPosition() &&
                    targetLeftBack > leftBack.getCurrentPosition() &&
                    targetRightBack > rightBack.getCurrentPosition()) {

                if (leftFront.getCurrentPosition() >= errorLF ||
                        rightFront.getCurrentPosition() >= errorRF ||
                        rightBack.getCurrentPosition() >= errorRB ||
                        leftBack.getCurrentPosition() >= errorLB) {

                    telemetry.addData("Status", "YESYSYSYSYSY");
                    telemetry.update();
                    leftBack.setPower(0);
                    rightBack.setPower(0);
                    leftFront.setPower(0);
                    rightFront.setPower(0);
                    leftBackB = false;
                    rightBackB = false;
                    leftFrontB = false;
                    rightFrontB = false;
                    break;


                }

                if ((targetLeftBack > leftBack.getCurrentPosition() && leftBackB) &&
                        (targetRightBack > rightBack.getCurrentPosition() && rightBackB) &&
                        (targetLeftFront > leftFront.getCurrentPosition() && leftFrontB) &&
                        (targetRightFront > rightFront.getCurrentPosition() && rightFrontB)) {

                    leftBack.setPower(speed);
                    rightBack.setPower(speed);
                    leftFront.setPower(speed);
                    rightFront.setPower(speed);
                }


            }
        }else if(str.equals("right")) {
            while (targetLeftFront > leftFront.getCurrentPosition() &&
                    targetRightFront > rightFront.getCurrentPosition() &&
                    targetLeftBack > leftBack.getCurrentPosition() &&
                    targetRightBack > rightBack.getCurrentPosition()) {

                if (leftFront.getCurrentPosition() >= errorLF ||
                        rightFront.getCurrentPosition() >= errorRF ||
                        rightBack.getCurrentPosition() >= errorRB ||
                        leftBack.getCurrentPosition() >= errorLB) {

                    telemetry.addData("Status", "YESYSYSYSYSY");
                    telemetry.update();
                    leftBack.setPower(0);
                    rightBack.setPower(0);
                    leftFront.setPower(0);
                    rightFront.setPower(0);
                    leftBackB = false;
                    rightBackB = false;
                    leftFrontB = false;
                    rightFrontB = false;
                    break;


                }

                if ((targetLeftBack > leftBack.getCurrentPosition() && leftBackB) &&
                        (targetRightBack > rightBack.getCurrentPosition() && rightBackB) &&
                        (targetLeftFront > leftFront.getCurrentPosition() && leftFrontB) &&
                        (targetRightFront > rightFront.getCurrentPosition() && rightFrontB)) {

                    leftBack.setPower(-speed);
                    rightBack.setPower(speed);
                    leftFront.setPower(speed);
                    rightFront.setPower(-speed);
                }


            }
        }else if(str.equals("left")) {
            while (targetLeftFront > leftFront.getCurrentPosition() &&
                    targetRightFront > rightFront.getCurrentPosition() &&
                    targetLeftBack > leftBack.getCurrentPosition() &&
                    targetRightBack > rightBack.getCurrentPosition()) {

                if (leftFront.getCurrentPosition() >= errorLF ||
                        rightFront.getCurrentPosition() >= errorRF ||
                        rightBack.getCurrentPosition() >= errorRB ||
                        leftBack.getCurrentPosition() >= errorLB) {

                    telemetry.addData("Status", "YESYSYSYSYSY");
                    telemetry.update();
                    leftBack.setPower(0);
                    rightBack.setPower(0);
                    leftFront.setPower(0);
                    rightFront.setPower(0);
                    leftBackB = false;
                    rightBackB = false;
                    leftFrontB = false;
                    rightFrontB = false;

                    break;


                }

                if ((targetLeftBack > leftBack.getCurrentPosition() && leftBackB) &&
                        (targetRightBack > rightBack.getCurrentPosition() && rightBackB) &&
                        (targetLeftFront > leftFront.getCurrentPosition() && leftFrontB) &&
                        (targetRightFront > rightFront.getCurrentPosition() && rightFrontB)) {

                    leftBack.setPower(speed);
                    rightBack.setPower(-speed);
                    leftFront.setPower(-speed);
                    rightFront.setPower(speed);
                }


            }
        }else if(str.equals("backwards")) {
            rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
            rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
            leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
            leftBack.setDirection(DcMotorSimple.Direction.REVERSE);


            while (targetLeftFront > leftFront.getCurrentPosition() &&
                    targetRightFront > rightFront.getCurrentPosition() &&
                    targetLeftBack > leftBack.getCurrentPosition() &&
                    targetRightBack > rightBack.getCurrentPosition()) {




                if (leftFront.getCurrentPosition() >= errorLF ||
                        rightFront.getCurrentPosition() >= errorRF ||
                        rightBack.getCurrentPosition() >= errorRB ||
                        leftBack.getCurrentPosition() >= errorLB) {

                    telemetry.addData("Status", "YESYSYSYSYSY");
                    telemetry.update();
                    leftBack.setPower(0);
                    rightBack.setPower(0);
                    leftFront.setPower(0);
                    rightFront.setPower(0);
                    leftBackB = false;
                    rightBackB = false;
                    leftFrontB = false;
                    rightFrontB = false;
                    rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
                    rightBack.setDirection(DcMotorSimple.Direction.REVERSE);
                    leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
                    leftBack.setDirection(DcMotorSimple.Direction.FORWARD);
                    break;


                }

                if ((targetLeftBack > leftBack.getCurrentPosition() && leftBackB) &&
                        (targetRightBack > rightBack.getCurrentPosition() && rightBackB) &&
                        (targetLeftFront > leftFront.getCurrentPosition() && leftFrontB) &&
                        (targetRightFront > rightFront.getCurrentPosition() && rightFrontB)) {

                    leftBack.setPower(speed);
                    rightBack.setPower(speed);
                    leftFront.setPower(speed);
                    rightFront.setPower(speed);
                }


            }
        }


        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


    }

    public void turn(double degrees, double speed, String str){

        double distance = (Math.PI * DIAMETER) * (degrees / 360);

        boolean leftBackB = true;
        boolean rightBackB = true;
        boolean leftFrontB = true;
        boolean rightFrontB = true;

        int targetLeftFront = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH);
        int targetRightFront = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH);
        int targetLeftBack = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH);
        int targetRightBack = leftFront.getCurrentPosition() + (int)(distance * COUNTS_INCH );


        if(str.equals("turn left")) {
            while (targetLeftFront > leftFront.getCurrentPosition() &&
                    targetRightFront > rightFront.getCurrentPosition() &&
                    targetLeftBack > leftBack.getCurrentPosition() &&
                    targetRightBack > rightBack.getCurrentPosition()) {

                if (leftFront.getCurrentPosition() >= targetLeftFront ||
                        rightFront.getCurrentPosition() >= targetRightFront ||
                        rightBack.getCurrentPosition() >= targetRightBack ||
                        leftBack.getCurrentPosition() >= targetLeftBack) {

                    telemetry.addData("Status", "YESYSYSYSYSY");
                    telemetry.update();
                    leftBack.setPower(0);
                    rightBack.setPower(0);
                    leftFront.setPower(0);
                    rightFront.setPower(0);
                    leftBackB = false;
                    rightBackB = false;
                    leftFrontB = false;
                    rightFrontB = false;
                    break;


                }

                if ((targetLeftBack > leftBack.getCurrentPosition() && leftBackB) &&
                        (targetRightBack > rightBack.getCurrentPosition() && rightBackB) &&
                        (targetLeftFront > leftFront.getCurrentPosition() && leftFrontB) &&
                        (targetRightFront > rightFront.getCurrentPosition() && rightFrontB)) {

                    leftBack.setPower(-speed);
                    rightBack.setPower(speed);
                    leftFront.setPower(-speed);
                    rightFront.setPower(speed);
                }


            }
        }else if(str.equals("turn right")) {
            while (targetLeftFront > leftFront.getCurrentPosition() &&
                    targetRightFront > rightFront.getCurrentPosition() &&
                    targetLeftBack > leftBack.getCurrentPosition() &&
                    targetRightBack > rightBack.getCurrentPosition()) {

                if (leftFront.getCurrentPosition() >= targetLeftFront ||
                        rightFront.getCurrentPosition() >= targetRightFront ||
                        rightBack.getCurrentPosition() >= targetRightBack ||
                        leftBack.getCurrentPosition() >= targetLeftBack) {

                    telemetry.addData("Status", "YESYSYSYSYSY");
                    telemetry.update();
                    leftBack.setPower(0);
                    rightBack.setPower(0);
                    leftFront.setPower(0);
                    rightFront.setPower(0);
                    leftBackB = false;
                    rightBackB = false;
                    leftFrontB = false;
                    rightFrontB = false;
                    break;


                }

                if ((targetLeftBack > leftBack.getCurrentPosition() && leftBackB) &&
                        (targetRightBack > rightBack.getCurrentPosition() && rightBackB) &&
                        (targetLeftFront > leftFront.getCurrentPosition() && leftFrontB) &&
                        (targetRightFront > rightFront.getCurrentPosition() && rightFrontB)) {

                    leftBack.setPower(speed);
                    rightBack.setPower(-speed);
                    leftFront.setPower(speed);
                    rightFront.setPower(-speed);
                }


            }
        }
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }




    public void Servo(double degrees){

        telemetry.addData("SSTSYFGUIHU", "ESKETIT");
        degrees = degrees / 360;
        moverBoi.setPosition(degrees);


    }


}
