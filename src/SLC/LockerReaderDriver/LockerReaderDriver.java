package SLC.LockerReaderDriver;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;
import SLC.HWHandler.HWHandler;


//======================================================================
// LockerReaderDriver
public class LockerReaderDriver extends HWHandler {
    //------------------------------------------------------------
    // LockerReaderDriver
    public LockerReaderDriver(String id, AppKickstarter appKickstarter) {
	super(id, appKickstarter);
    } // LockerReaderDriver


    //------------------------------------------------------------
    // processMsg
    protected void processMsg(Msg msg) {
        switch (msg.getType()) {
            case Locker_LockerRead:
                slc.send(new Msg(id, mbox, Msg.Type.Locker_LockerRead, msg.getDetails()));
                break;

            case Locker_GoActive:
                handleGoActive();
                break;

            case Locker_GoStandby:
                handleGoStandby();
                break;

            default:
                log.warning(id + ": unknown message type: [" + msg + "]");
        }
    } // processMsg


    //------------------------------------------------------------
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
} // LockerReaderDriver
