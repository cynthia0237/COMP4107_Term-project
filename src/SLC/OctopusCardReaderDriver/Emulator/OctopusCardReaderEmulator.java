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
            case "Standby":
                octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader Activated");
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
            case "Standby":
                octopusCardReaderEmulatorController.appendTextArea("Octopus Card Reader Standby");
            break;
        }
        
    } // handleGoStandby


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

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
} // OctopusCardReaderEmulator