package org.firstinspires.ftc.teamcode.parts;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.utils.StickyGamepads;

@Config
public class Intake {
    private static DcMotor intakeMotor;
    private static StickyGamepads gamepad1, gamepad2;
    private static Servo intake_servo;
    public int intake_level = 0;
    public static final int MAX_LEVELS = 5;
    public static double up_static_pos = 0, down_static_pos = 0.6, step_pos = 0.83/MAX_LEVELS;

    public Intake(HardwareMap hm, Gamepad gp1, Gamepad gp2){
        intakeMotor = hm.get(DcMotor.class, "Intake");
        intake_servo = hm.get(Servo.class, "intake_retract");

        intake_servo.setDirection(Servo.Direction.FORWARD);


        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        MotorConfigurationType mct = intakeMotor.getMotorType().clone();
        mct.setAchieveableMaxRPMFraction(1.0);

        intakeMotor.setMotorType(mct);
        intake_servo.setPosition(0);

        gamepad1 = new StickyGamepads(gp1);
        gamepad2 = new StickyGamepads(gp2);

    }
    private boolean IntakeOn = false, go_up;
    public void loop(){
        if(gamepad1.x) {
            IntakeOn = !IntakeOn;

            if(IntakeOn) intakeMotor.setPower(1);
            else intakeMotor.setPower(0);
        }
        if(gamepad1.a){
            intake_level = 0;
        }
        if(gamepad1.b){
            if(go_up) intake_level ++;
            else intake_level --;

            if(go_up){
                if(intake_level > MAX_LEVELS){intake_level = MAX_LEVELS; go_up = false;}
            } else {
                if(intake_level < 0){intake_level = 0; go_up = true; }
            }
        }

        intake_servo.setPosition(intake_level * step_pos);

        gamepad1.update();
        gamepad2.update();
    }
}
