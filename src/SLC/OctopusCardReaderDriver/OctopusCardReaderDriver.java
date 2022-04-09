package SLC.OctopusCardReaderDriver;

import SLC.HWHandler.HWHandler;
import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;


//======================================================================
// OctopusCardReaderDriver
public class OctopusCardReaderDriver extends HWHandler{

    //------------------------------------------------------------
    // OctopusCardReaderDriver
    public OctopusCardReaderDriver(String id, AppKickstarter appKickstarter) {
	super(id, appKickstarter);

    } // OctopusCardReaderDriver

   

    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {

        switch (msg.getType()) {
            case OCR_OctopusCardRead:
                slc.send(new Msg(id, mbox, Msg.Type.OCR_OctopusCardRead, msg.getDetails()));
                break;

            case OCR_GoActive:
                handleGoActive();
                break;

            case OCR_GoStandby:
                handleGoStandby();
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg

    // handleGoActive
    protected void handleGoActive() {
	log.info(id + ": Go Active");
    } // handleGoActive


    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
	log.info(id + ": Go Standby");
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        log.info(id + ": Handle Poll");
    } // handlePoll
} // OctopusCardReaderDriver