package SLC.LockerReaderDriver.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.logging.Logger;

//======================================================================
// LockerReaderEmulatorController
public class LockerReaderEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private LockerReaderEmulator lockerReaderEmulator;
    private MBox lockerReaderMBox;
    private String activationResp;
    private String standbyResp;
    private String pollResp;
    public TextField lockerNumField;
    public TextField lockerReaderStatusField;
    public TextArea lockerReaderTextArea;
    public ChoiceBox standbyRespCBox;
    public ChoiceBox activationRespCBox;
    public ChoiceBox pollRespCBox;



    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, LockerReaderEmulator lockerReaderEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
	this.log = log;
	this.lockerReaderEmulator = lockerReaderEmulator;
	this.lockerReaderMBox = appKickstarter.getThread("LockerReaderDriver").getMBox();
        this.activationRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                activationResp = activationRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Activation Response set to " + activationRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.standbyRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                standbyResp = standbyRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Standby Response set to " + standbyRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
	this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
                appendTextArea("Poll Response set to " + pollRespCBox.getItems().get(newValue.intValue()).toString());
            }
        });
        this.activationResp = activationRespCBox.getValue().toString();
        this.standbyResp = standbyRespCBox.getValue().toString();
        this.pollResp = pollRespCBox.getValue().toString();
        this.goStandby();
    } // initialize


    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
	Button btn = (Button) actionEvent.getSource();

	switch (btn.getText()) {
	    case "Locker 1":
	        lockerNumField.setText(appKickstarter.getProperty("LockerReader.Locker1"));
	        break;

	    case "Locker 2":
            lockerNumField.setText(appKickstarter.getProperty("LockerReader.Locker2"));
		break;

	    case "Locker 3":
            lockerNumField.setText(appKickstarter.getProperty("LockerReader.Locker3"));
		break;

	    case "Reset":
            lockerNumField.setText("");
		break;

	    case "Send Locker":
            lockerReaderMBox.send(new Msg(id, lockerReaderMBox, Msg.Type.Locker_LockerRead, lockerNumField.getText()));
            lockerReaderTextArea.appendText("Sending locker " + lockerNumField.getText()+"\n");
		break;

	    case "Activate/Standby":
            lockerReaderMBox.send(new Msg(id, lockerReaderMBox, Msg.Type.BR_GoActive, lockerNumField.getText()));
            lockerReaderTextArea.appendText("Removing card\n");
		break;

	    default:
	        log.warning(id + ": unknown button: [" + btn.getText() + "]");
		break;
	}
    } // buttonPressed


    //------------------------------------------------------------
    // getters
    public String getActivationResp() { return activationResp; }
    public String getStandbyResp()    { return standbyResp; }
    public String getPollResp()       { return pollResp; }


    //------------------------------------------------------------
    // goActive
    public void goActive() {
        updateLockerReaderStatus("Active");
    } // goActive


    //------------------------------------------------------------
    // goStandby
    public void goStandby() {
        updateLockerReaderStatus("Standby");
    } // goStandby


    //------------------------------------------------------------
    // updateLockerReaderStatus
    private void updateLockerReaderStatus(String status) {
        lockerReaderStatusField.setText(status);
    } // updateLockerReaderStatus


    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        lockerReaderTextArea.appendText(status+"\n");
    } // appendTextArea
} // LockerReaderEmulatorController
