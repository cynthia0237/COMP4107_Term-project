package SLC.OctopusCardReaderDriver.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import AppKickstarter.timer.Timer;
import java.util.logging.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//======================================================================
// BarcodeReaderEmulatorController
public class OctopusCardReaderEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private OctopusCardReaderEmulator octopusCardReaderEmulator;
    private MBox octopusCardReaderMBox;
    private MBox touchDisplayMBox;
    private MBox lockerReaderMBox;
    private Timer octopusCardReaderTimer;
    private String activationResp;
    private String standbyResp;
    private String pollResp;
    public TextField octopusCardNumField;
    public TextField octopusCardReaderStatusField;
    public TextArea octopusCardReaderTextArea;
    public ChoiceBox standbyRespCBox;
    public ChoiceBox activationRespCBox;
    public ChoiceBox pollRespCBox;
    private TextField remainingMoney;
    private int remainingMoney_int;
    public int paymentAmount;
    public int lockerId;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, OctopusCardReaderEmulator octopusCardReaderEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
        this.octopusCardReaderEmulator = octopusCardReaderEmulator;
        this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
        this.lockerReaderMBox = appKickstarter.getThread("LockerReaderDriver").getMBox();
        //this.octopusCardReaderTimer.getMBox()
        this.octopusCardReaderMBox = appKickstarter.getThread("OctopusCardReaderDriver").getMBox();
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
            case "1":
                octopusCardNumField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard1"));
 
                break;

            case "2":
                octopusCardNumField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard2"));

            break;

            case "3":
                octopusCardNumField.setText(appKickstarter.getProperty("OctopusCardReader.OctopusCard3"));

            break;

            case "Reset":
                octopusCardNumField.setText("");
            break;

            case "Send":
               if(octopusCardReaderStatusField.getText().equals("Active")){
                octopusCardReaderMBox.send(new Msg(id, octopusCardReaderMBox, Msg.Type.OCR_OctopusCardRead, octopusCardNumField.getText()));
                //touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.OCR_BackToMainPage, "MainPage"));
                lockerReaderMBox.send(new Msg(id, lockerReaderMBox, Msg.Type.OpenLocker, Integer.toString(lockerId)));
                touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_CorrectPasscode, Integer.toString(lockerId)));
                octopusCardReaderTextArea.appendText("locker id " + lockerId +"\n");
                octopusCardReaderTextArea.appendText("Sending octopus card number " + octopusCardNumField.getText()+"\n");
                octopusCardReaderTextArea.appendText("Transaction Success: received HK$" + paymentAmount + " from Octopus Card " + octopusCardNumField.getText()+"\n");
                
               }
            break;

            case "Activate":
                octopusCardReaderMBox.send(new Msg(id, octopusCardReaderMBox, Msg.Type.OCR_GoActive, octopusCardNumField.getText()));

            break;

            case "Standby":
                octopusCardReaderMBox.send(new Msg(id, octopusCardReaderMBox, Msg.Type.OCR_GoStandby, octopusCardNumField.getText()));

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
        updateOctopusCardReaderStatus("Active");
    } // goActive


    //------------------------------------------------------------
    // goStandby
    public void goStandby() {
        updateOctopusCardReaderStatus("Standby");
    } // goStandby


    //------------------------------------------------------------
    // updateBarcodeReaderStatus
    private void updateOctopusCardReaderStatus(String status) {
        octopusCardReaderStatusField.setText(status);
    } // updateBarcodeReaderStatus


    //------------------------------------------------------------
    // appendTextArea
    public void appendTextArea(String status) {
        octopusCardReaderTextArea.appendText(status+"\n");
    } // appendTextArea
} // OCtopusCardReaderEmulatorController
