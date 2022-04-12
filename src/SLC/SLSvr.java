package SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import SLC.Locker.*;

import java.util.HashMap;
import java.util.Map;

public class SLSvr extends AppThread {

    static SLSvr instance = null;
    private static AppKickstarter slcEmulatorStarter;

    private int pollingTime;
    private MBox slc;

    private HashMap<String, String> reserveLockerMap = new HashMap<>(); //barcode, lockerId
    private HashMap<String, String> lockerPasscodeMap = new HashMap<>(); //id, passcode

    public static SLSvr getInstance(){
        if(instance == null){
            synchronized(SLSvr.class){
                if(instance == null){
                    System.out.println("cannot find svr");
                }
            }
        }
        return instance;
    }

    public SLSvr(String id, AppKickstarter appKickstarter) throws Exception {
        super(id, appKickstarter);
        pollingTime = Integer.parseInt(appKickstarter.getProperty("SLSvr.PollingTime"));
        instance = this;
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

                case BarcodeVerification:
                    verifyBarcode(msg.getDetails());
                    break;

                case BackupPasscodeMap:
                    backupLockerPasscodeMapToSvr(stringToMap(msg.getDetails()));
                    break;

                case RemoveUsedBarcode:
                    removeUsedBarcode(msg.getDetails());
                    break;

                default:
                    log.warning(id + ": unknown message type: [" + msg + "]");
            }
        }

        // declaring our departure
        appKickstarter.unregThread(this);
        log.info(id + ": terminating...");
    } //run

    public boolean reserveLocker(String barcode,LockerSize size) {
        if (reserveLockerMap.containsKey(barcode)) {
            log.info(id + ": barcode - " + barcode + " already reserve locker");
            return false;
        }
        Locker locker = LockerManager.getInstance().reserveLocker(size);
        String lockerId;
        if (locker != null)
        {
            lockerId = locker.getLockerId();
            reserveLockerMap.put(barcode, lockerId);
            LockerManager.getInstance().getLockerById(lockerId).setLockerStatus(LockerStatus.Booked);
            return true;
        }
        return false;
    }

    private String verifyBarcode(String barcode) {
        log.info(id + ": verifying barcode");
        if (reserveLockerMap.containsKey(barcode)) {
            for(Map.Entry<String, String> entry : reserveLockerMap.entrySet()) {
                //If get the available barcode
                if(entry.getKey().equals(barcode)) {
                    //send the locker id to SLC
                    slc.send(new Msg(id,mbox,Msg.Type.barcodechecklockerid,entry.getValue()));
                    return entry.getValue();
                }
            }
        }
        slc.send(new Msg(id,mbox,Msg.Type.barcodechecklockerid,"error"));
        return "error";
    }

    private void removeUsedBarcode(String barcode) {
        if (reserveLockerMap.containsKey(barcode)) {
            reserveLockerMap.remove(barcode);
            log.info(id + ": remove used barcode - " + barcode);
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
