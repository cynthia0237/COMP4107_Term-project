package SLC.OctopusCardReaderDriver.Emulator;

import AppKickstarter.misc.Msg;
import SLC.SLCStarter;
import SLC.OctopusCardReaderDriver.OctopusCardReaderDriver;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;


//======================================================================
// OctopusCardReaderEmulator
public class OctopusCardReaderEmulator extends OctopusCardReaderDriver {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private OctopusCardReaderEmulatorController octopusCardReaderEmulatorController;

    //------------------------------------------------------------
    // OctopusCardReaderrEmulator
    public OctopusCardReaderEmulator(String id, SLCStarter slcStarter) {
	super(id, slcStarter);
	this.slcStarter = slcStarter;
	this.id = id;
    } //OctopusCardReaderEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "OctopusCardReaderEmulator.fxml";
        loader.setLocation(OctopusCardReaderEmulator.class.getResource(fxmlName));
        root = loader.load();
        octopusCardReaderEmulatorController = (OctopusCardReaderEmulatorController) loader.getController();
        octopusCardReaderEmulatorController.initialize(id, slcStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 350, 470));
        myStage.setTitle("Octopus Card Reader");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            slcStarter.stopApp();
            Platform.exit();
            
	});
	myStage.show();
    } // OctopusCardReaderEmulator


    //------------------------------------------------------------
    // handleGoActive
    protected void handleGoActive() {
        // fixme
        super.handleGoActive();
        switch(octopusCardReaderEmulatorController.getActivationResp()){
            case "Ignore":
            //
            break;

            case "Activated":
                slc.send(new Msg(id, mbox, Msg.Type.OCR_GoActive, id + " Go Active"));
                octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader Activated");
                octopusCardReaderEmulatorController.goActive();
            break;

            case "Standby":
                slc.send(new Msg(id, mbox, Msg.Type.OCR_GoStandby, id + " Go Standby"));
                octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader Standby");
                octopusCardReaderEmulatorController.goStandby();
            break;
        }
        
    } // handleGoActive

    //------------------------------------------------------------
    // handleGoStandby
    protected void handleGoStandby() {
        // fixme
        super.handleGoStandby();
        switch(octopusCardReaderEmulatorController.getStandbyResp()){
            case "Ignore":
            //
            break;

            case "Activated":
                slc.send(new Msg(id, mbox, Msg.Type.OCR_GoActive, id + " Go Active"));
                octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader Activated");
                octopusCardReaderEmulatorController.goActive();
            break;

            case "Standby":
                slc.send(new Msg(id, mbox, Msg.Type.OCR_GoStandby, id + " Go Standby"));
                octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader Standby");
                octopusCardReaderEmulatorController.goStandby();
            break;
        }
        
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        //super.handlePoll();

        switch (octopusCardReaderEmulatorController.getPollResp()) {
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


    //------------------------------------------------------------
    // handlePaymentAmount (for testing only)
    protected void handlePaymentAmount(String amount){

        super.handlePaymentAmount(amount);
        octopusCardReaderEmulatorController.paymentAmount = Integer.parseInt(amount);
        octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader ready to receive: HK$"+amount);
        octopusCardReaderEmulatorController.appendTextArea("Waiting for the user to make the payment by sending the octopus card number!");

    }
    //handlePaymentAmount

    //------------------------------------------------------------
    // handleLateDay
    protected void handleLateDay(String lateDay){

        super.handleLateDay(lateDay);
        octopusCardReaderEmulatorController.paymentAmount = Integer.parseInt(lateDay) * 10;
        octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader ready to receive: HK$"+octopusCardReaderEmulatorController.paymentAmount);
        octopusCardReaderEmulatorController.appendTextArea("Waiting for the user to make the payment by sending the octopus card number!");

    }
    //handleLateDay

} // OctopusCardReaderEmulator