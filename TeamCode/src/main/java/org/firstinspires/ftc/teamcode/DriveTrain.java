package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.teamcode.IStateBasedModule;

import java.util.ArrayList;
   
public static class DriveParameters{
    public double forward, right , turn;

    public DriveParameters(double forward, double right, double turn) {
        this.forward = forward;
        this.right = right;
        this.turn = turn;
    }

    void normalize(){
        double denominator = Math.abs(forward) + Math.abs(right) + Math.abs(turn);
        denominator = Math.max(1, denominator);

        forward /= denominator;
        right /= denominator;
        turn /= denominator;
    }

    void setMultiplier(double multiplier){
        forward *= multiplier;
        right *= multiplier;
        turn *= multiplier;
    }

}

public class DriveTrain {

    DcMotorEx mfl;
    DcMotorEx mfr;
    DcMotorEx mbl;
    DcMotorEx mbr;
    Gamepad gamepad1, gamepad2;

    public enum SPEED{
        SLOW(0.5),
        FAST(1);
        public final double multiplier;

        SPEED(double multiplier_param) {
            multiplier = multiplier_param;
        }
    } // pare destul de sus

    public SPEED speed = SPEED.FAST;

    public DriveTrain (HardwareMap hm, Gamepad gamepad1, Gamepad gamepad2)
    {
		mfl = hm.get(DcMotorEx.class, "mfl");
        mfr = hm.get(DcMotorEx.class, "mfr");
        mbl = hm.get(DcMotorEx.class, "mbl");
        mbr = hm.get(DcMotorEx.class, "mbr");

//        mfl.setDirection(DcMotorSimple.Direction.REVERSE);
        mfr.setDirection(DcMotorSimple.Direction.REVERSE);
//        mbl.setDirection(DcMotorSimple.Direction.REVERSE);
        mbr.setDirection(DcMotorSimple.Direction.REVERSE);

        ArrayList<DcMotorEx> motorListDrive = new ArrayList<>(mfl, mfr, mbl, mbr); //suna a o idee nice de a fi global definita

        for(DcMotorEx motor: motorListDrive){
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            MotorConfigurationType motorConfigurationType = motor.getMotorType().clone();
            motorConfigurationType.setAchieveableMaxRPMFraction(1.0);
            motor.setMotorType(motorConfigurationType);

        }

    }


    public DriveParameters getDriveParameters  (double forward , double right , double turn){
        DriveParameters driveParameters = new DriveParameters(forward,right,turn);
        driveParameters.setMultiplier(speed.multiplier);
        return driveParameters;

    }
    private void driveForValues(DriveParameters parameters){
        parameters.normalize();
        mfl.setPower(parameters.forward + parameters.right + parameters.turn);
        mfr.setPower(parameters.forward - parameters.right - parameters.turn);
        mbl.setPower(parameters.forward - parameters.right + parameters.turn);
        mbr.setPower(parameters.forward + parameters.right - parameters.turn);

    }

    public void loop() {
        double forward = -gamepad1.left_stick_y;
        double right = gamepad1.left_stick_x;
        double turn = gamepad1.right_trigger - gamepad1.left_trigger;
        driveForValues(getDriveParameters(forward, right, turn));

    }
}