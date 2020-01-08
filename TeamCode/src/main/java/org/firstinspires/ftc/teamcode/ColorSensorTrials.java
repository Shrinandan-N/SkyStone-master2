

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@Autonomous(name="Color Sensor Trials", group="Linear Opmode")
public class ColorSensorTrials extends LinearOpMode {

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
        placeBoi = hardwareMap.get(Servo.class, "placeBoi");



        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);



        waitForStart();
        runtime.reset();



        while(opModeIsActive()) {



            int count = 0;
            if(scanForStone()){

                drive(10, 1, "forward");

            }else {
                for (int i = 0; i < 5; i++) {
                    telemetry.addData("Status", "Green: " + colorSensor.green() + " Red: " + colorSensor.red()+ " Blue: "+ colorSensor.blue());
                    telemetry.update();
                    drive(8,0.05,"right");
                    count++;
                    if(scanForStone()){

                        drive(10, 1, "forward");
                        break;
                    }
                }
            }

            stop();

        }



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


    public boolean scanForStone(){

        if(colorSensor.red() > 30 && colorSensor.green() > 30){
            return false;
        }
        return true;
    }


}
