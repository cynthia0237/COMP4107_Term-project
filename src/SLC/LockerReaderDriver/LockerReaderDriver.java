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
            case OpenLocker:
                slc.send(new Msg(id, mbox, Msg.Type.OpenLocker, msg.getDetails()));
                openLocker(msg);
                log.info(id + " open locker " + msg.getDetails());
                break;

            case CloseLocker:
                slc.send(new Msg(id, mbox, Msg.Type.CloseLocker, msg.getDetails()));
                log.info(id + " close locker " + msg.getDetails());
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

    protected void openLocker(Msg msg) {
        System.out.println("open locker: " + msg.getDetails());
    }
} // LockerReaderDriver
