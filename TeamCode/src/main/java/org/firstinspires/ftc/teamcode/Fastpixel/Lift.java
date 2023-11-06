package org.firstinspires.ftc.teamcode.Fastpixel;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Lift {
    private DcMotorEx f;
    private DcMotorEx b;

    public Lift(HardwareMap hm){
        f = hm.get(DcMotorEx.class, "liftL");
        b = hm.get(DcMotorEx.class, "liftR");
        f.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        b.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        f.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        f.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setTarget(0);
        f.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        b.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        f.setDirection(DcMotorSimple.Direction.REVERSE );
        setPower(1.0);
    }

    public void setPower(double powr){
        f.setPower(powr);
        b.setPower(powr);
    }

    public void setTarget(int targ){
        f.setTargetPosition(targ);
        b.setTargetPosition(targ);
    }

    public int getPozition(){
        return f.getCurrentPosition();
    }

    public int getTarget()
    {
        return f.getTargetPosition();
    }

}