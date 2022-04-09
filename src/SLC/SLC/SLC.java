package SLC.SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import SLC.Locker.Locker;
import SLC.Locker.LockerManager;
import SLC.Locker.LockerStatus;

import java.lang.invoke.SwitchPoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


//======================================================================
// SLC
public class SLC extends AppThread {
    private int pollingTime;
    private MBox barcodeReaderMBox;
    private MBox touchDisplayMBox;
	private MBox octopuscardReaderMBox;
	private MBox lockerReaderMBox;

	private HashMap<String, String> lockerPasscodeMap = new HashMap<>(); //id, passcode


    //------------------------------------------------------------
    // SLC
    public SLC(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
	pollingTime = Integer.parseInt(appKickstarter.getProperty("SLC.PollingTime"));


		//For test
		genPickupPasscode("33");
		//Callback cb = () -> genPickupPasscode("4");
		//StaffPutCargoTask staffTask = new StaffPutCargoTask();
		//staffTask.runWith(cb);

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

		// if receive BR_GoActive_Response,
		case BR_GoActive_Response:
			switch (msg.getDetails()){
				case "Activated":
					//Change the status of barcodescreen
					touchDisplayMBox.send(new Msg(id, barcodeReaderMBox, Msg.Type.BR_GoActive_Response, "Activated"));
					break;
				case "Standby":
					//Change the status of barcodescreen
					touchDisplayMBox.send(new Msg(id, barcodeReaderMBox, Msg.Type.BR_GoActive_Response, "Standby"));
					break;
				case "Ignore":
					//return error of ignore and set active alert
					touchDisplayMBox.send(new Msg(id, barcodeReaderMBox, Msg.Type.BR_GoStandby_Response, ""));
					break;
			}
			break;

		// if receive BR_GoStandby_Response
		case BR_GoStandby_Response:
			switch (msg.getDetails()){
				case "Activated":
					//Change the status of barcodescreen
					touchDisplayMBox.send(new Msg(id, barcodeReaderMBox, Msg.Type.BR_GoStandby_Response, "Activated"));
					break;
				case "Standby":
					//Change the status of barcodescreen
					touchDisplayMBox.send(new Msg(id, barcodeReaderMBox, Msg.Type.BR_GoStandby_Response, "Standby"));
					break;
				case "Ignore":
					//return error of ignore and set active alert
					touchDisplayMBox.send(new Msg(id, barcodeReaderMBox, Msg.Type.BR_GoStandby_Response, ""));
					break;
				}
			break;

		case OCR_OctopusCardRead:
			log.info("Octopus Card Number: " + msg.getDetails());
			break;

		//receive the barcode no from barcodeemulator
		case BR_BarcodeRead:
			//send message to server and verify

			//get response from server
			boolean serverResponse = true;
			if(serverResponse){
				//save the locker and passcode in SLC
				//check locker availability????

				//return the message to touchscreen and give the locker to distribute the locker
				touchDisplayMBox.send(new Msg(id,mbox,Msg.Type.BR_BarcodeRead,msg.getDetails()));


			}else{
				//if the response is null -> restart again
				touchDisplayMBox.send(new Msg(id,mbox,Msg.Type.BR_BarcodeRead,""));
			}
			//set back the barcode to standby
			barcodeReaderMBox.send(new Msg(id,mbox,Msg.Type.BR_GoStandby,""));
			break;

		case TD_CheckPickupPasscode:
			String lockerId = checkPickupPasscode(msg.getDetails());
			if (lockerId.equals("error")) {
				//System.out.println("wrong passcode send msg");
				touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_WrongPasscode, "Invalid Passcode Please enter again"));

			} else {
				System.out.println("correct passcode - LockerId: " + lockerId);
				//TODO check have payment or not

				//if no payment
				lockerReaderMBox.send(new Msg(id, mbox, Msg.Type.OpenLocker, lockerId));
				touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_ShowOpenLocker, lockerId));
				LockerManager.getInstance().getLockerById(lockerId).setLock(false);
				LockerManager.getInstance().getLockerById(lockerId).setLockerStatus(LockerStatus.Open);
			}
			break;

		case OpenLocker:
			LockerManager.getInstance().getLockerById(msg.getDetails()).setLockerStatus(LockerStatus.Open);
			break;

		case FinishPickup:
			LockerManager.getInstance().getLockerById(msg.getDetails()).setLockerStatus(LockerStatus.Available);
			LockerManager.getInstance().getLockerById(msg.getDetails()).setLock(true);
			break;


		default:
			log.warning(id + ": unknown message type: [" + msg + "]");
		}
	}

	// declaring our departure
	appKickstarter.unregThread(this);
	log.info(id + ": terminating...");
    } // run


    //------------------------------------------------------------
    //TODO check FXML object pos
	// processMouseClicked
    private void processMouseClicked(Msg msg) {
	// *** process mouse click here!!! ***
    } // processMouseClicked


	//region Locker Pickup Passcode
	private void genPickupPasscode(String lockerId) {
		Random rand = new Random();
		int number = rand.nextInt(99999999);

		String passcode = String.format("%08d", number);
		System.out.println("lockerId: " + lockerId + " gen passcode: " + passcode);
		lockerPasscodeMap.put(lockerId, passcode);
	}

	private void removeUsedPasscode(String lockerId) {
		if (lockerPasscodeMap.containsKey(lockerId)) {
			lockerPasscodeMap.remove(lockerId);
		}
		else {
			System.out.println("cannot find key " + lockerId + " in lockerPasscodeMap");
		}
	}

	public String checkPickupPasscode(String passcode) {
		if (lockerPasscodeMap.containsValue(passcode)) {
			for(Map.Entry<String, String> entry : lockerPasscodeMap.entrySet()) {
				if(entry.getValue().equals(passcode)) {
					return entry.getKey();
				}
			}
		}
		return "error";
	}
	//endregion

} // SLC
