package org.firstinspires.ftc.teamcode.components;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.CoolServo;
import org.firstinspires.ftc.teamcode.utils.StickyGamepads;

@Config
public class BratOutTake {
    public static boolean reverseV1, reverseV2;
    private Servo rot1, rot2, al_b, rotp;
    private CoolServo rotate1, rotate2, align_backdrop, rotatePixels;
    private Servo claw1, claw2;


    public static double activate_angle = 0.75, deactivate_angle = 0, parallerl_backdrop = 0.9, parallel_ground = 0;
    public static double rotate_unit = 0.3;
    private Telemetry telemetry;


    public BratOutTake(HardwareMap hm, Telemetry tele){
        rot1 = hm.get(Servo.class, "virtual1");
        rot2 = hm.get(Servo.class, "virtual2");
        al_b = hm.get(Servo.class, "angle");
        rotp = hm.get(Servo.class, "pivot");

        rotate1 = new CoolServo(rot1, false, 15, 8, 0);
        rotate2 = new CoolServo(rot2, true, 15, 8, 0);

        align_backdrop = new CoolServo(al_b, false,16, 15, 0);
        rotatePixels = new CoolServo(rotp, true, 20, 12,0);

        rotate1.setPosition(0);
        rotate2.setPosition(0);

        align_backdrop.setPosition(0);
        rotatePixels.setPosition(0);

        telemetry = tele;
    }
    public boolean isActive, isRotated;
    public void update(){
        if(isActive){
            rotate1.setPosition(activate_angle);
            rotate2.setPosition(activate_angle);

            align_backdrop.setPosition(parallerl_backdrop);
        } else {
            rotate1.setPosition(deactivate_angle);
            rotate2.setPosition(deactivate_angle);

            align_backdrop.setPosition(parallel_ground);
        }

        if(isRotated){
            rotatePixels.setPosition(rotate_unit * 1);
        } else {
            rotatePixels.setPosition(rotate_unit * 0);
        }

        rotate1.update();
        rotate2.update();

        rotatePixels.update();
        align_backdrop.update();
    }

    public void activate(){
        isActive = true;
    }
    public void deactivate(){
        isActive = false;
    }

    public void rotate90(){ isRotated = true; }
    public void antiRotate90(){ isRotated = false; }


    public boolean isAtRest(){
        return rotate1.getTimeToMotionEnd() == 0 && rotate2.getTimeToMotionEnd() == 0 && rotatePixels.getTimeToMotionEnd() == 0 &&
                align_backdrop.getTimeToMotionEnd() == 0;
    }
}


// 260 -- level 3