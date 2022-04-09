package SLC.TouchDisplayHandler;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;

import java.io.IOException;


//======================================================================
// TouchDisplayHandler
public class TouchDisplayHandler extends HWHandler {
    //------------------------------------------------------------
    // TouchDisplayHandler
    public TouchDisplayHandler(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
    } // TouchDisplayHandler


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case TD_MouseClicked:
                slc.send(new Msg(id, mbox, Msg.Type.TD_MouseClicked, msg.getDetails()));
                break;

            case TD_UpdateDisplay:
                handleUpdateDisplay(msg);
                break;

            //update touchscreen GUI-------------Go active
            case BR_GoActive_Response:
                handle_BR_GoActive_Status_UpdateDisplay(msg);
                break;

            //update touchscreen GUI-------------Go standby
            case BR_GoStandby_Response:
                handle_BR_GoStandby_Status_UpdateDisplay(msg);
                break;

            case TD_CheckPickupPasscode:
                slc.send(new Msg(id, mbox, Msg.Type.TD_CheckPickupPasscode, msg.getDetails()));
                break;

            case TD_WrongPasscode:
                handlePasscodeInput(msg);
                break;

            case TD_ShowOpenLocker:
                showOpenLockScreen(msg);
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    //update barcode gui method---------Start
    protected void handle_BR_GoActive_Status_UpdateDisplay(Msg msg){}
    protected void handle_BR_GoStandby_Status_UpdateDisplay(Msg msg){}
    //update barcode gui method---------End

    protected void handlePasscodeInput(Msg msg) {
        System.out.println("handlePasscodeInput");
    }

    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
	log.info(id + ": update display -- " + msg.getDetails());
    } // handleUpdateDisplay


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        log.info(id + ": Handle Poll");
    } // handlePoll

    protected void showOpenLockScreen(Msg msg){}
} // TouchDisplayHandler
