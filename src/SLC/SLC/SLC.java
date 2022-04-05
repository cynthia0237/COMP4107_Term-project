package SLC.SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import SLC.Locker.LockerManager;

import java.util.HashMap;
import java.util.Random;


//======================================================================
// SLC
public class SLC extends AppThread {
    private int pollingTime;
    private MBox barcodeReaderMBox;
    private MBox touchDisplayMBox;
	private MBox octopuscardReaderMBox;
	private MBox lockerReaderMBox;

	private LockerManager lockerMgr;
	private HashMap lockerPasscodeMap = new HashMap<String, String>(); //id, passcode


    //------------------------------------------------------------
    // SLC
    public SLC(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
	pollingTime = Integer.parseInt(appKickstarter.getProperty("SLC.PollingTime"));

	lockerMgr = new LockerManager();
    } // SLC


    //------------------------------------------------------------
    // run
    public void run() {
	Timer.setTimer(id, mbox, pollingTime);
	log.info(id + ": starting...");

	barcodeReaderMBox = appKickstarter.getThread("BarcodeReaderDriver").getMBox();
	octopuscardReaderMBox = appKickstarter.getThread("OctopusCardReaderDriver").getMBox();
	touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
	lockerReaderMBox = appKickstarter.getThread("LockerReaderDriver").getMBox();

	for (boolean quit = false; !quit;) {
	    Msg msg = mbox.receive();

	    log.fine(id + ": message received: [" + msg + "].");

	    switch (msg.getType()) {
		case TD_MouseClicked:
		    log.info("MouseCLicked: " + msg.getDetails());
		    processMouseClicked(msg);
		    break;

		case TimesUp:
		    Timer.setTimer(id, mbox, pollingTime);
		    log.info("Poll: " + msg.getDetails());
		    barcodeReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
			octopuscardReaderMBox.send(new Msg(id,mbox,Msg.Type.Poll,""));
		    touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
			lockerReaderMBox.send(new Msg(id, mbox, Msg.Type.Poll, ""));
		    break;

		case PollAck:
		    log.info("PollAck: " + msg.getDetails());
		    break;

		case Terminate:
		    quit = true;
		    break;

		case BR_BarcodeRead:
			//send message to server and verify

			//get response from server
			boolean serverresponse = true;
			if(serverresponse){
				//save the locker and passcode in SLC


				//return the message to touchscreen and give the locker to distribute the locker



			}else{
				//set go activate
				barcodeReaderMBox.send(new Msg(id,mbox,Msg.Type.BR_GoActive,""));
			}
			//set the barcode to standby
			barcodeReaderMBox.send(new Msg(id,mbox,Msg.Type.BR_GoStandby,""));


			default:
				log.warning(id + ": unknown message type: [" + msg + "]");
		}
	}

	// declaring our departure
	appKickstarter.unregThread(this);
	log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    // processMouseClicked
    private void processMouseClicked(Msg msg) {
	// *** process mouse click here!!! ***
    } // processMouseClicked

	void genPickupPasscode(String lockerId) {
		Random rand = new Random();
		int number = rand.nextInt(99999999);

		String passcode = String.format("%08d", number);
		System.out.println("lockerId: " + lockerId + " gen passcode: " + passcode);
		lockerPasscodeMap.put(lockerId, passcode);
	}

	void removeUsedPasscode(String lockerId) {
		if (lockerPasscodeMap.containsKey(lockerId)) {
			lockerPasscodeMap.remove(lockerId);
		}
		else {
			System.out.println("cannot find key " + lockerId + " in lockerPasscodeMap");
		}
	}


} // SLC
