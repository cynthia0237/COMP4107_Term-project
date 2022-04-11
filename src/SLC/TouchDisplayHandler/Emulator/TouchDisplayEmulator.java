package SLC.TouchDisplayHandler.Emulator;

import SLC.SLCStarter;
import SLC.TouchDisplayHandler.TouchDisplayHandler;
import AppKickstarter.misc.Msg;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;


//======================================================================
// TouchDisplayEmulator
public class TouchDisplayEmulator extends TouchDisplayHandler {
    private final int WIDTH = 680;
    private final int HEIGHT = 570;
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private TouchDisplayEmulatorController touchDisplayEmulatorController;

    //------------------------------------------------------------
    // TouchDisplayEmulator
    public TouchDisplayEmulator(String id, SLCStarter slcStarter) throws Exception {
	super(id, slcStarter);
	this.slcStarter = slcStarter;
	this.id = id;
    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
	// Parent root;
	myStage = new Stage();
	reloadStage("TouchDisplayEmulator.fxml");
	myStage.setTitle("Touch Display");
	myStage.setResizable(false);
	myStage.setOnCloseRequest((WindowEvent event) -> {
	    slcStarter.stopApp();
	    Platform.exit();
	});
	myStage.show();
    } // TouchDisplayEmulator


    //------------------------------------------------------------
    // reloadStage
    private void reloadStage(String fxmlFName) {
        TouchDisplayEmulator touchDisplayEmulator = this;

        Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    log.info(id + ": loading fxml: " + fxmlFName);

		    // get the latest pollResp string, default to "ACK"
		    String pollResp = "ACK";
		    if (touchDisplayEmulatorController != null) {
		        pollResp = touchDisplayEmulatorController.getPollResp();
                    }

		    Parent root;
		    FXMLLoader loader = new FXMLLoader();
		    loader.setLocation(TouchDisplayEmulator.class.getResource(fxmlFName));
		    root = loader.load();
		    touchDisplayEmulatorController = (TouchDisplayEmulatorController) loader.getController();
		    touchDisplayEmulatorController.initialize(id, slcStarter, log, touchDisplayEmulator, pollResp);
		    myStage.setScene(new Scene(root, WIDTH, HEIGHT));
		} catch (Exception e) {
		    log.severe(id + ": failed to load " + fxmlFName);
		    e.printStackTrace();
		}
	    }
	});
    } // reloadStage


    //------------------------------------------------------------
    // handleUpdateDisplay
    protected void handleUpdateDisplay(Msg msg) {
        log.info(id + ": update display -- " + msg.getDetails());

        switch (msg.getDetails()) {
            case "BlankScreen":
                reloadStage("TouchDisplayEmulator.fxml");
                break;

            case "MainMenu":
                reloadStage("TouchDisplayMainMenu.fxml");
                break;

            case "Confirmation":
                reloadStage("TouchDisplayConfirmation.fxml");
                break;

            case "Payment":
                reloadStage("TouchDisplayPayment.fxml");
                break;

            case "Barcodepage":
                reloadStage("BarcodeDisplayEmulator.fxml");
                break;

            case "EnterPasscode":
                reloadStage("PickupPasscodeEnter.fxml");
                break;

            default:
                log.severe(id + ": update display with unknown display type -- " + msg.getDetails());
                break;
        }
    } // handleUpdateDisplay
    //------------------------------------------------------------------------------
    //handle UpdateBarcode status(Go active)------------Touch screen Barcode GUI
    protected void handle_BR_GoActive_Status_UpdateDisplay(Msg msg){
        log.info(id + ": Update Barcode status for go active -- " + msg.getDetails());

        switch (msg.getDetails()) {
            case "Activated":
                touchDisplayEmulatorController.updatebarcodestatusgui(msg.getDetails());
                break;

            case "Standby":
                touchDisplayEmulatorController.updatebarcodestatusgui(msg.getDetails());
                break;

            default:
                //null
                touchDisplayEmulatorController.updatebarcodestatusgui("");
                break;
        }
    }
    //handle UpdateBarcode status(Go standby)------------Touch screen Barcode GUI
    protected void handle_BR_GoStandby_Status_UpdateDisplay(Msg msg){
        log.info(id + ": Update Barcode status for go standby -- " + msg.getDetails());

        switch (msg.getDetails()) {
            case "Activated":
                //need to update code
                touchDisplayEmulatorController.updatebarcodestatusgui(msg.getDetails());
                break;

            case "Standby":
                touchDisplayEmulatorController.updatebarcodestatusgui(msg.getDetails());
                break;

            default:
                //null
                touchDisplayEmulatorController.updatebarcodestatusgui("");
                break;
        }
    }
    //Update Barcode No
    protected void handle_BR_BarcodeNo_UpdateDisplay(Msg msg){
        touchDisplayEmulatorController.updatebarcodeno(msg.getDetails());
    }

    //handle UpdateBarcode status

    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (touchDisplayEmulatorController.getPollResp()) {
            case "ACK":
                slc.send(new Msg(id, mbox, Msg.Type.PollAck, id + " is up!"));
                break;

            case "NAK":
                slc.send(new Msg(id, mbox, Msg.Type.PollNak, id + " is down!"));
                break;

            case "Ignore":
                // Just ignore.  do nothing!!
                break;
        }
    } // handlePoll

    protected void handlePasscodeInput(Msg msg) {
        switch (msg.getType()) {
            case TD_WrongPasscode:
                touchDisplayEmulatorController.updatePasscodeMsgLblText(msg.getDetails());
                break;

            case TD_CorrectPasscode:
                showOpenLockScreen(msg);
                break;
        }
    }

    protected void showOpenLockScreen(Msg msg) {
        reloadStage("TouchDisplayOpenLocker.fxml");
        Platform.runLater(() -> {
            touchDisplayEmulatorController.showOpenLockerScreen(msg.getDetails());
        });
    }

    protected void setPasscodeTF(Msg msg) {
        touchDisplayEmulatorController.setPasscodeTF(msg.getDetails());
    }

    //------------------------------------------------------------
    // handleAfterPayment
    protected void handleAfterPayment() {
        touchDisplayEmulatorController.backTOMainPage();
    }//handleAfterPayment

    //------------------------------------------------------------
    // handleLateDay
    protected void handleLateDay(String lateDay){

        //super.handleLateDay(lateDay);
        touchDisplayEmulatorController.lateTime = lateDay;

    }
    //handleLateDay
    
    //------------------------------------------------------------
    // handleSwitchToPayment
    protected void handleSwitchToPayment(){

        //super.handleSwitchToPayment();
        touchDisplayEmulatorController.switchToPayment();

    }
    //handleSwitchToPayment
} // TouchDisplayEmulator
