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

            //update barcode touchscreen GUI-------------Go active
            case BR_GoActive_Response:
                handle_BR_GoActive_Status_UpdateDisplay(msg);
                break;

            //update barcode touchscreen GUI-------------Go standby
            case BR_GoStandby_Response:
                handle_BR_GoStandby_Status_UpdateDisplay(msg);
                break;

            //update barcode touchscreen GUI-------------Barcode No
            case BR_BarcodeRead:
                handle_BR_BarcodeNo_UpdateDisplay(msg);
                break;

            case TD_SetPasscodeTF:
                setPasscodeTF(msg);
                break;

            case CheckPickupPasscode:
                slc.send(new Msg(id, mbox, Msg.Type.CheckPickupPasscode, msg.getDetails()));
                break;

            case TD_WrongPasscode:

            case TD_CorrectPasscode:
                handlePasscodeInput(msg);
                break;

            case OCR_BackToMainPage:
                handleAfterPayment();
                break;
                
            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    //update barcode gui method---------Start
    protected void handle_BR_GoActive_Status_UpdateDisplay(Msg msg){}
    protected void handle_BR_GoStandby_Status_UpdateDisplay(Msg msg){}
    protected void handle_BR_BarcodeNo_UpdateDisplay(Msg msg){}
    //update barcode gui method---------End

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

    protected void handlePasscodeInput(Msg msg) {
        System.out.println("handlePasscodeInput");
    }

    protected void setPasscodeTF(Msg msg) { }


    protected void handleAfterPayment() {
        log.info(id + ": Handle After Payment");
    }
} // TouchDisplayHandler
