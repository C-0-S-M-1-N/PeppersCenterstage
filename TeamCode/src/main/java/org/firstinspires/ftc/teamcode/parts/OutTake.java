package org.firstinspires.ftc.teamcode.parts;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.components.BratOutTake;
import org.firstinspires.ftc.teamcode.components.Elevator;
import org.firstinspires.ftc.teamcode.utils.StickyGamepads;
import org.firstinspires.ftc.teamcode.components.LiftStates;

@Config
public class OutTake {
    private static enum STATES_OOUTTAKE {
        RESET_ELEVATOR,
        SET_FEED_FORWARD,
        SHOUD_RETRACT,
        WAIT_FOR_ARM_REST,
        WAIT_FOR_ELEVATOR_REST,
        READY_FOR_PIXELS;

        public static int pos = 0;
    }

    ;
    STATES_OOUTTAKE STATE;
    private Elevator elevator;
    private BratOutTake arm;
    private Telemetry telemetry;

    private static int feedForwardLevel = 3;
    private StickyGamepads gamepad1, gamepad2;

    public int RetractLevel = 4;
    public OutTake(HardwareMap hm, Gamepad gp1, Gamepad gp2, Telemetry tele){
        elevator = new Elevator(hm, gp1, gp2, tele);
        arm = new BratOutTake(hm, tele);

        telemetry = tele;
        STATE = STATES_OOUTTAKE.READY_FOR_PIXELS;

        gamepad1 = new StickyGamepads(gp1);
        gamepad2 = new StickyGamepads(gp2);

    }

    public void loop(){

        switch (STATE){
            case SHOUD_RETRACT:
                elevator.setLevel(RetractLevel);
                arm.deactivate();
                STATE = STATES_OOUTTAKE.WAIT_FOR_ELEVATOR_REST;
                STATES_OOUTTAKE.pos = RetractLevel;
                break;
            case RESET_ELEVATOR:
                STATE = STATES_OOUTTAKE.WAIT_FOR_ELEVATOR_REST;
                STATES_OOUTTAKE.pos = 0;
                elevator.setLevel(0);
                break;
            case SET_FEED_FORWARD:
                // TODO
                break;
            case WAIT_FOR_ARM_REST:
                if(arm.isAtRest()){
                    STATE = STATES_OOUTTAKE.RESET_ELEVATOR;
                }
                break;
            case WAIT_FOR_ELEVATOR_REST:
                if(elevator.getLevelNow() == STATES_OOUTTAKE.pos){
                    if(STATES_OOUTTAKE.pos == RetractLevel) {
                        STATE = STATES_OOUTTAKE.WAIT_FOR_ARM_REST;
                    } else if(STATES_OOUTTAKE.pos == 0){
                        STATE = STATES_OOUTTAKE.READY_FOR_PIXELS;
                    }
                }
                break;
        }

        if(elevator.getLevelNow() >= 3 && elevator.STATE == LiftStates.GO_UP && STATE != STATES_OOUTTAKE.SHOUD_RETRACT){
            arm.activate();
            arm.rotate90();
        }

        if(gamepad2.y && elevator.getLevelNow() > 0){
            STATE = STATES_OOUTTAKE.SHOUD_RETRACT;
        }

        if(gamepad2.b){
            if(arm.isRotated) arm.antiRotate90();
            else arm.rotate90();
        }

        if(gamepad2.right_bumper){
            arm.signalClaw1();
        }
        if(gamepad2.left_bumper){
            arm.signalClaw2();
        }


        arm.update();
        elevator.loop();
        gamepad2.update();
        gamepad1.update();
    }
}
