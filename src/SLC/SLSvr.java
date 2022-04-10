package SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import SLC.Locker.*;

import java.util.HashMap;
import java.util.Map;

public class SLSvr extends AppThread {
    private int pollingTime;
    private MBox slc;

    private HashMap<String, String> reserveLockerMap = new HashMap<>(); //barcode, lockerId
    private HashMap<String, String> lockerPasscodeMap = new HashMap<>(); //id, passcode

    public SLSvr(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
        pollingTime = Integer.parseInt(appKickstarter.getProperty("SLSvr.PollingTime"));

        //For test

    } // SLC

    @Override
    public void run() {
        Timer.setTimer(id, mbox, pollingTime);
        log.info(id + ": starting...");

        slc = appKickstarter.getThread("SLC").getMBox();

        for (boolean quit = false; !quit;) {
            Msg msg = mbox.receive();

            log.fine(id + ": message received: [" + msg + "].");

            switch (msg.getType()) {
                case TimesUp:
                    Timer.setTimer(id, mbox, pollingTime);
                    log.info("SLSvrPoll: " + msg.getDetails());
                    break;

                case PollAck:
                    log.info("SLSvr PollAck: " + msg.getDetails());
                    break;

                case Terminate:
                    quit = true;
                    break;

                case BackupPasscodeMap:
                    backupLockerPasscodeMapToSvr(stringToMap(msg.getDetails()));
                    break;


                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } //run

    private void reserveLocker(String barcode,LockerSize size) {
        String id = LockerManager.getInstance().reserveLocker(size).getLockerId();
        reserveLockerMap.put(barcode, id);
        LockerManager.getInstance().getLockerById(id).setLockerStatus(LockerStatus.Booked);
    }

    private String verifyBarcode(String barcode) {
        if (reserveLockerMap.containsValue(barcode)) {
            for(Map.Entry<String, String> entry : reserveLockerMap.entrySet()) {
                if(entry.getValue().equals(barcode)) {
                    return entry.getKey();
                }
            }
        }
        return "error";
    }

    private void removeUsedBarcode(String barcode) {
        if (reserveLockerMap.containsKey(barcode)) {
            reserveLockerMap.remove(barcode);
        }
        else {
            System.out.println("cannot find barcode " + barcode + " in reserveLockerMap");
        }
    }

    private HashMap<String, String> stringToMap(String mapStr) {
        //sample string result = {33=53855204, 25=75033307}
        mapStr = mapStr.substring(1, mapStr.length()-1);
        String[] keyPairVal = mapStr.split(", ");
        HashMap<String, String> map = new HashMap<>();

        for(String pair : keyPairVal)
        {
            String[] entry = pair.split("=");
            map.put(entry[0].trim(), entry[1].trim());
        }

        return map;
    }

    private void backupLockerPasscodeMapToSvr(HashMap<String, String> map) {
        if (lockerPasscodeMap != null) {
            lockerPasscodeMap.clear();
        }
        lockerPasscodeMap = map;
    }
}
