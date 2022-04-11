package SLC.SLC;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.*;
import AppKickstarter.timer.Timer;
import SLC.Locker.LockerManager;
import SLC.Locker.LockerStatus;

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
	private MBox svrMBox;

	private HashMap<String, String> lockerPasscodeMap = new HashMap<>(); //id, passcode
	boolean isStaff; //differentiate which type of user
	int lockerTimeLimit; //in second
	String passcode = "";


    //------------------------------------------------------------
    // SLC
    public SLC(String id, AppKickstarter appKickstarter) throws Exception {
	super(id, appKickstarter);
	pollingTime = Integer.parseInt(appKickstarter.getProperty("SLC.PollingTime"));
	lockerTimeLimit = 30;


		//For test
		genPickupPasscode("33");
		genPickupPasscode("25");
		//System.out.println(lockerPasscodeMap.toString());
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
	svrMBox = appKickstarter.getThread("SLSvr").getMBox();

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

		case OCR_GoActive:
		log.info("Activation Response: " + msg.getDetails());
		break;

		case OCR_GoStandby:
		log.info("Standby Response: " + msg.getDetails());
		break;
		
		case OCR_OctopusCardRead:
		log.info("Octopus Card Number: " + msg.getDetails() + "settle the payment successfully");

		//after payment action (go to the locker?)
		
		break;

		//receive the barcode no from barcodeemulator
		case BR_BarcodeRead:
			isStaff = true;
			//send message to server and verify

			//get response from server
			boolean serverResponse = true;
			if(serverResponse){
				//TODO save the locker and passcode in SLC
				//TODO check locker availability -> Open -> Store it????
				String Lockerid = "";


				//return the message to touchscreen and give the locker to distribute the locker
				touchDisplayMBox.send(new Msg(id,mbox,Msg.Type.BR_BarcodeRead,msg.getDetails()));
			}else{
				//if the response is null -> restart again
				touchDisplayMBox.send(new Msg(id,mbox,Msg.Type.BR_BarcodeRead,""));
			}
			//set back the barcode to standby
			barcodeReaderMBox.send(new Msg(id,mbox,Msg.Type.BR_GoStandby,""));
			break;

		case CheckPickupPasscode:
			isStaff = false;
			String lockerId = checkPickupPasscode(msg.getDetails());
			if (lockerId.equals("error")) {
				//System.out.println("wrong passcode send msg");
				touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_WrongPasscode, "Invalid Passcode Please enter again"));

			} else {
				System.out.println("correct passcode - LockerId: " + lockerId);
				//TODO check have payment or not
				int dueTime = checkPayment(lockerId);
				if (dueTime > 0) {
					//have payment active octopus
				} else {
					lockerReaderMBox.send(new Msg(id, mbox, Msg.Type.OpenLocker, lockerId));
					touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_CorrectPasscode, lockerId));
					removeUsedPasscode(msg.getDetails());
					svrMBox.send(new Msg(id, mbox, Msg.Type.BackupPasscodeMap, lockerPasscodeMap.toString()));

					octopuscardReaderMBox.send(new Msg(id, mbox, Msg.Type.OCR_ReceiveLateDay, Integer.toString(dueTime)));
					touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.OCR_ReceiveLateDay, Integer.toString(dueTime)));
				}

			}
			break;

		case OpenLocker:
			LockerManager.getInstance().getLockerById(msg.getDetails()).setLockerStatus(LockerStatus.Open);
			LockerManager.getInstance().getLockerById(msg.getDetails()).setLock(false);
			//TODO confirm which UI will display after staff check in (below in user pickup open locker GUI)
			//touchDisplayMBox.send(new Msg(id, mbox, Msg.Type.TD_CorrectPasscode, msg.getDetails()));
			break;

		case CloseLocker:
			LockerManager.getInstance().getLockerById(msg.getDetails()).setLock(true);
			if (isStaff) {
				LockerManager.getInstance().getLockerById(msg.getDetails()).setStartTime(System.currentTimeMillis());
				LockerManager.getInstance().getLockerById(msg.getDetails()).setLockerStatus(LockerStatus.InUse);
			} else {
				LockerManager.getInstance().getLockerById(msg.getDetails()).setLockerStatus(LockerStatus.Available);
			}
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
		String[] subStrs;
		String scene = "";
		int x = 0, y = 0;
		if (msg.getDetails().contains(" ")) {
			subStrs = msg.getDetails().split(" ");
			scene = subStrs[0];
			x = Integer.parseInt(subStrs[1]);
			y = Integer.parseInt(subStrs[2]);
			System.out.println("processMouseClicked " + subStrs[0] + " " + x + " " + y);
		}

		switch (scene) {
			case "MainMenu":
				if (0 <= x && x<= 300) {
					if (270 <= y && y <= 340) {
						//Enter passcode page
						touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "EnterPasscode"));
					}
				}
				break;

			case "EnterPasscode":
				if (177 <= y && y <= 253) {
					if (142 <= x && x <= 272) {
						passcode += 1;
					}
					if (276 <= x && x <= 406) {
						passcode += 2;
					}
					if (410 <= x && x <= 540) {
						passcode += 3;
					}
				}

				if (265 <= y && y <= 341) {
					if (142 <= x && x <= 272) {
						passcode += 4;
					}
					if (276 <= x && x <= 406) {
						passcode += 5;
					}
					if (410 <= x && x <= 540) {
						passcode += 6;
					}
				}

				if (349 <= y && y <= 425) {
					if (142 <= x && x <= 272) {
						passcode += 7;
					}
					if (276 <= x && x <= 406) {
						passcode += 8;
					}
					if (410 <= x && x <= 540) {
						passcode += 9;
					}
				}

				if (433 <= y && y <= 509) {
					if (142 <= x && x <= 272) {
						if(!passcode.isEmpty())
							passcode = passcode.substring(0, passcode.length()-1);
					}
					if (276 <= x && x <= 406) {
						passcode += 0;
					}
					if (410 <= x && x <= 540) {
						touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.CheckPickupPasscode, passcode));
					}
				}

				touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_SetPasscodeTF, passcode));
				System.out.println(passcode);
				break;
		}
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

	public int checkPayment(String lockerId) {
    	long spentTime = System.currentTimeMillis() - LockerManager.getInstance().getLockerById(lockerId).getStartTime();
    	int useTime = (int)(spentTime / 1000) - lockerTimeLimit;
    	if (useTime > 0) {
			return (useTime / lockerTimeLimit + ((useTime % lockerTimeLimit == 0) ? 0 : 1));
		}

    	return -1;
	}

} // SLC
