/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;



@TeleOp(name="LinearSlideEncoder", group="Iterative Opmode")

public class LinearSlideTester extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor liftBoi = null;
    private Servo Arnav = null;


    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        liftBoi  = hardwareMap.get(DcMotor.class, "liftBoi");
        Arnav = hardwareMap.get(Servo.class, "Arnav");

        liftBoi.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        liftBoi.setMode(DcMotor.RunMode.RUN_USING_ENCODER);






        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        runtime.reset();
    }


    @Override
    public void loop() {



        if(gamepad1.dpad_up){
            int target = liftBoi.getCurrentPosition() + 100;

            while(liftBoi.getCurrentPosition() < 400 && liftBoi.getCurrentPosition() < target){
                liftBoi.setPower(0.1);

            }
        }else if(gamepad1.dpad_down){
            int target = liftBoi.getCurrentPosition() - 100;


            while(liftBoi.getCurrentPosition() > 0 && liftBoi.getCurrentPosition() > target){

                liftBoi.setPower(-0.1);

            }
        }else if(gamepad1.dpad_right){

            while(liftBoi.getCurrentPosition() > 0){
                liftBoi.setPower(-0.1);
            }
        }else if(gamepad1.dpad_left){
            while(liftBoi.getCurrentPosition() < 350){
                liftBoi.setPower(0.1);
            }
        }else{
            liftBoi.setPower(0);
        }








        if(gamepad1.x){
            Arnav.setPosition(0);
        }
        if(gamepad1.y){
            Arnav.setPosition(1);

        }
        liftBoi.setPower(-gamepad1.right_stick_y * 0.2 );
        telemetry.addData("Status", "Encoder: " + Arnav.getPosition());

    }


    @Override
    public void stop() {
    }

}
