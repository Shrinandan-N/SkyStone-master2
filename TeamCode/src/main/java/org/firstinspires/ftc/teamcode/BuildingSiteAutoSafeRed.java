package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;


@Autonomous(name="AutoBuildingSideRed", group="Linear Opmode")
public class BuildingSiteAutoSafeRed extends LinearOpMode {

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


    static final double COUNTS_PER_REV = 48;
    static final double DIAMETER = 4;
    static final double COUNTS_INCH = COUNTS_PER_REV / DIAMETER * Math.PI;


    private static final VuforiaLocalizer.CameraDirection CAMERA_CHOICE = BACK;
    private static final boolean PHONE_IS_PORTRAIT = false  ;

    private static final String VUFORIA_KEY =
            "AcVp//T/////AAABmRVKkeBC8kqXiyvw0zcx9BEr7gSLmxhPzgj7Zi12FXnET4Aejohj7YNSQ3SMKSg51oI2RQk7QObxYHm2eIzJNln3aW/uJYW+7Z9YJID949jZ/JGiU/O7xbvmzWLeV0kG12/MQbDgMlFkFafMbHi2YJ5QNAWqm/hOgULMYwfGn6OlQrSl60JL3tB6tga0VGQFnRBuxqgPXsNoJvjLEeeb6QL+enUwGppTQvVpH+MWrgen94/P1EpI2tjRj5Kj/duL3o0jZzXBu4/DUcxrINkStYfOw6MGMe9TzOCd96Fw3j5jD0cpOq9l7Hjfw8G9hqIKxdCy19zLNJJjPZqzVqu/K6kkcJAAFeCSSa6B1o3k58bg";


    private static final float mmPerInch        = 25.4f;
    private static final float mmTargetHeight   = (6) * mmPerInch;


    private static final float stoneZ = 2.00f * mmPerInch;


    private static final float bridgeZ = 6.42f * mmPerInch;
    private static final float bridgeY = 23 * mmPerInch;
    private static final float bridgeX = 5.18f * mmPerInch;
    private static final float bridgeRotY = 59;
    private static final float bridgeRotZ = 180;


    private static final float halfField = 72 * mmPerInch;
    private static final float quadField  = 36 * mmPerInch;


    private OpenGLMatrix lastLocation = null;
    private VuforiaLocalizer vuforia = null;

    WebcamName rustyWebcam = null;

    private boolean targetVisible = false;
    private float phoneXRotate    = 0;
    private float phoneYRotate    = 0;
    private float phoneZRotate    = 0;

    boolean stoneScanned = false;



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
        rustyWebcam = hardwareMap.get(WebcamName.class, "RustyCam");


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






        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parameters.cameraName = rustyWebcam;

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection   = CAMERA_CHOICE;


        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        VuforiaTrackables targetsSkyStone = this.vuforia.loadTrackablesFromAsset("Skystone");
        VuforiaTrackable stoneTarget = targetsSkyStone.get(0);
        stoneTarget.setName("Stone Target");
        VuforiaTrackable blueRearBridge = targetsSkyStone.get(1);
        blueRearBridge.setName("Blue Rear Bridge");
        VuforiaTrackable redRearBridge = targetsSkyStone.get(2);
        redRearBridge.setName("Red Rear Bridge");
        VuforiaTrackable redFrontBridge = targetsSkyStone.get(3);
        redFrontBridge.setName("Red Front Bridge");
        VuforiaTrackable blueFrontBridge = targetsSkyStone.get(4);
        blueFrontBridge.setName("Blue Front Bridge");
        VuforiaTrackable red1 = targetsSkyStone.get(5);
        red1.setName("Red Perimeter 1");
        VuforiaTrackable red2 = targetsSkyStone.get(6);
        red2.setName("Red Perimeter 2");
        VuforiaTrackable front1 = targetsSkyStone.get(7);
        front1.setName("Front Perimeter 1");
        VuforiaTrackable front2 = targetsSkyStone.get(8);
        front2.setName("Front Perimeter 2");
        VuforiaTrackable blue1 = targetsSkyStone.get(9);
        blue1.setName("Blue Perimeter 1");
        VuforiaTrackable blue2 = targetsSkyStone.get(10);
        blue2.setName("Blue Perimeter 2");
        VuforiaTrackable rear1 = targetsSkyStone.get(11);
        rear1.setName("Rear Perimeter 1");
        VuforiaTrackable rear2 = targetsSkyStone.get(12);
        rear2.setName("Rear Perimeter 2");


        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(targetsSkyStone);

        stoneTarget.setLocation(OpenGLMatrix.translation(0, 0, stoneZ).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        //Set the position of the bridge support targets with relation to origin (center of field)
        blueFrontBridge.setLocation(OpenGLMatrix.translation(-bridgeX, bridgeY, bridgeZ).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, bridgeRotZ)));

        blueRearBridge.setLocation(OpenGLMatrix.translation(-bridgeX, bridgeY, bridgeZ).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, bridgeRotZ)));

        redFrontBridge.setLocation(OpenGLMatrix.translation(-bridgeX, -bridgeY, bridgeZ).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, -bridgeRotY, 0)));

        redRearBridge.setLocation(OpenGLMatrix.translation(bridgeX, -bridgeY, bridgeZ).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 0, bridgeRotY, 0)));

        //Set the position of the perimeter targets with relation to origin (center of field)
        red1.setLocation(OpenGLMatrix.translation(quadField, -halfField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        red2.setLocation(OpenGLMatrix.translation(-quadField, -halfField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 180)));

        front1.setLocation(OpenGLMatrix.translation(-halfField, -quadField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , 90)));

        front2.setLocation(OpenGLMatrix.translation(-halfField, quadField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 90)));

        blue1.setLocation(OpenGLMatrix.translation(-quadField, halfField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        blue2.setLocation(OpenGLMatrix.translation(quadField, halfField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, 0)));

        rear1.setLocation(OpenGLMatrix.translation(halfField, quadField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0 , -90)));

        rear2.setLocation(OpenGLMatrix.translation(halfField, -quadField, mmTargetHeight).multiplied(Orientation.getRotationMatrix(EXTRINSIC, XYZ, DEGREES, 90, 0, -90)));

        if (CAMERA_CHOICE == BACK) {
            phoneYRotate = -90;
        } else {
            phoneYRotate = 90;
        }


        if (PHONE_IS_PORTRAIT) {
            phoneXRotate = 90 ;
        }

        final float CAMERA_FORWARD_DISPLACEMENT  = 4.0f * mmPerInch;
        final float CAMERA_VERTICAL_DISPLACEMENT = 8.0f * mmPerInch;
        final float CAMERA_LEFT_DISPLACEMENT     = 0;





        OpenGLMatrix robotFromCamera = OpenGLMatrix
                .translation(CAMERA_FORWARD_DISPLACEMENT, CAMERA_LEFT_DISPLACEMENT, CAMERA_VERTICAL_DISPLACEMENT)
                .multiplied(Orientation.getRotationMatrix(EXTRINSIC, YZX, DEGREES, phoneYRotate, phoneZRotate, phoneXRotate));

        for (VuforiaTrackable trackable : allTrackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(robotFromCamera, parameters.cameraDirection);
        }

        targetsSkyStone.activate();

        waitForStart();
        runtime.reset();

        while(opModeIsActive()){

            linearSlideUp();
            drive(16, 0.3, "forward");


            int count = 0;

            for(int i = 0; i < 6; i++) {
                for (VuforiaTrackable trackable : allTrackables) {
                    if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                        targetVisible = true;

                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                        if (robotLocationTransform != null) {
                            lastLocation = robotLocationTransform;
                        }
                        break;
                    }
                }
                sleep(250);
                if (targetVisible == true) {
                    telemetry.addData("Visible Target: ", "is seen");

                    drive(3, 0.1, "right");
                    drive(15.5, 0.15, "forward");
                    sleep(250);

                    count++;
                    break;
                } else {
                    telemetry.addData("Visible Target: ", "not seen");
                    drive(8, 0.1, "left");
                    sleep(1000);
                    count++;
                }

            }

            targetsSkyStone.deactivate();

            drive(0.1, 0.05, "backwards");

            linearSlideDown();
            sleep(1000);
            placeBoi.setPosition(0);

            sleep(1000);
            linearSlideUpHalf();

            drive(22.5, 0.2, "backwards");

            drive(8*count, 0.2, "right");

            linearSlideDownHalf();
            drive(48, 0.2, "right");


            sleep(1000);
            placeBoi.setPosition(0.5);
            sleep(1000);

            drive(22, 0.3, "left");


            stop();


        }

    }



    public void linearSlideUpHalf(){

        int target = liftBoi.getCurrentPosition() + 175;

        while(liftBoi.getCurrentPosition() < 500 && liftBoi.getCurrentPosition() < target){
            liftBoi.setPower(0.1);
        }
        liftBoi.setPower(0);

    }

    public void linearSlideDownHalf(){

        int target = liftBoi.getCurrentPosition() - 150;

        while(liftBoi.getCurrentPosition() > 0 && liftBoi.getCurrentPosition() > target){
            liftBoi.setPower(-0.1);
        }
        liftBoi.setPower(0);

    }



    public void linearSlideUp(){

        int target = liftBoi.getCurrentPosition() + 350;

        while(liftBoi.getCurrentPosition() < 500 && liftBoi.getCurrentPosition() < target){
            liftBoi.setPower(0.1);
        }
        liftBoi.setPower(0);

    }

    public void linearSlideDown(){

        int target = liftBoi.getCurrentPosition() - 330;

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

        double errorLB = 7*(targetLeftBack/8);
        double errorRB = 7*(targetRightBack/8);
        double errorRF = 7*(targetRightFront/8);
        double errorLF = 7*(targetLeftFront/8);

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

}

