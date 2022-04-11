package SLC.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.logging.Logger;


//======================================================================
// TouchDisplayEmulatorController
public class TouchDisplayEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private TouchDisplayEmulator touchDisplayEmulator;
    private MBox touchDisplayMBox;
    private MBox barcodeReaderMBox;
    private MBox octopusCardReaderMBox;
    private String selectedScreen;
    private String pollResp;
    public ChoiceBox screenSwitcherCBox;
    public ChoiceBox pollRespCBox;
    public TextField passcodeTF;
    String textValue = "";

    //payment
    public TextField lateDayField;
    public TextField totalChargeField;
    public String lateTime;


    private Stage stage;
    private Scene scene;
    private Parent root;
    //--------------------------------------
    //barcode page variable
    public Label fxBarcodeStatusLabel;
    public Label fxbarcodeworkinglabel;
    public Label fxBarcodeNoLabel;

    public Label passcodeMsgLbl;
    public Label openLockerLbl;



    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, TouchDisplayEmulator touchDisplayEmulator, String pollRespParam) {
        this.id = id;

	this.appKickstarter = appKickstarter;
	this.log = log;
	this.touchDisplayEmulator = touchDisplayEmulator;
	this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
    this.barcodeReaderMBox = appKickstarter.getThread("BarcodeReaderDriver").getMBox();
    this.octopusCardReaderMBox = appKickstarter.getThread("OctopusCardReaderDriver").getMBox();
	this.pollResp = pollRespParam;
	this.pollRespCBox.setValue(this.pollResp);
        this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
            }
        });
        this.screenSwitcherCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                selectedScreen = screenSwitcherCBox.getItems().get(newValue.intValue()).toString();
                switch (selectedScreen) {
                    case "Blank":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
                        break;

                    case "Main Menu":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                        break;

                    case "Confirmation":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
                        break;

                    case "Payment":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Payment"));
                    break;

                    case "Enter Passcode":
                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "EnterPasscode"));
                        break;
                }
            }
        });
        this.selectedScreen = screenSwitcherCBox.getValue().toString();
        lateTime = "1";

    } // initialize


    //------------------------------------------------------------
    // getSelectedScreen
    public String getSelectedScreen() {
        return selectedScreen;
    } // getSelectedScreen


    //------------------------------------------------------------
    // getPollResp
    public String getPollResp() {
        return pollResp;
    } // getPollResp


    //------------------------------------------------------------
    // td_mouseClick
    public void td_mouseClick(MouseEvent mouseEvent) {
        int x = (int) mouseEvent.getX();
	    int y = (int) mouseEvent.getY();

	    log.fine(id + ": mouse clicked: -- (" + x + ", " + y + ")");
	    //touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));

	    if (getPollResp().equals("ACK")) {
	        switch (getSelectedScreen()) {
                case "Blank":
                    touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
                    break;

                case "Main Menu":
                    touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, "MainMenu " + x + " " + y));
                    break;

                case "Enter Passcode":
                    touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, "EnterPasscode " + x + " " + y));
                    break;

                case "Open Locker":
                    touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "BlankScreen"));
                    break;
            }

        }
    } // td_mouseClick

    //----------------------------------------------------------------------
    //switch scene
    public void switchToConfirmation(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TouchDisplayConfirmation.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Confirmation"));
    }

    //-----------------------------------
    //octopus scene
    public void switchToPayment(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TouchDisplayPayment.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Payment"));

        octopusCardReaderMBox.send(new Msg(id,octopusCardReaderMBox,Msg.Type.OCR_GoActive,"Active"));

        //for testing
        String paymentAmount = Integer.toString(getPaymentAmount(lateTime));
        octopusCardReaderMBox.send(new Msg(id,octopusCardReaderMBox,Msg.Type.OCR_ReceivePayment,paymentAmount));
  
    }

    public void getPaymentDetail(ActionEvent event) throws IOException {
        setTextPayment(lateTime);
    }

    public int getPaymentAmount(String lateDay){
        int totalCharge = 0;
        String lateDate = lateDay;
        totalCharge = calTotalCharge(lateDate);
        return totalCharge;
    }

    public void setTextPayment(String lateDay){
        //test data
        int totalCharge = 0;
        String lateDate = lateDay;
        //
        lateDayField.setText(lateDate);
        totalCharge = calTotalCharge(lateDate);
        totalChargeField.setText(String.valueOf("HK$"+totalCharge));
 
    }

    public int calTotalCharge(String lateDay){
        int day = Integer.parseInt(lateDay);
        return day * 10;
    }

    public void switchToMainMenu_octopus(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("TouchDisplayMainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));

        octopusCardReaderMBox.send(new Msg(id,octopusCardReaderMBox,Msg.Type.OCR_GoStandby,"Standby"));       
    }

    public void backTOMainPage(){
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
        octopusCardReaderMBox.send(new Msg(id,octopusCardReaderMBox,Msg.Type.OCR_GoStandby,"Standby"));
    }
    //-------------------------------------

    public void switchToMainMenu_action(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TouchDisplayMainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
        
    }

    public void onStaffLoginBtnClick(ActionEvent event) {

    }

    public void barcodebuttonclicked(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("BarcodeDisplayEmulator.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "Barcodepage"));
        //send the msg to alert barcode go to active
        barcodeReaderMBox.send(new Msg(id,touchDisplayMBox,Msg.Type.BR_GoActive,""));
    }

    //region Control pickup locker display
    public void setPasscodeTF(String passcode) {
        Platform.runLater(() -> passcodeTF.setText(passcode));
    }

    public void updatePasscodeMsgLblText(String msg) {
        Platform.runLater(() -> passcodeMsgLbl.setText(msg));
    }

    public void showOpenLockerScreen(String lockerId) {
        String id = String.format("%02d", Integer.parseInt(lockerId));
        openLockerLbl.setText("Locker " + id +" will be open");
    }

    //endregion

    //----------------------------------------------------------------------------
    //update the gui of barcode status
    public void updatebarcodestatusgui(String response) {
        //set to activated/standby
        Platform.runLater(() -> fxBarcodeStatusLabel.setText(response));
        if (response == "Activated") {
            Platform.runLater(() -> fxbarcodeworkinglabel.setText("Working"));
        } else {
            Platform.runLater(() -> fxbarcodeworkinglabel.setText("Not work"));
            Platform.runLater(() -> fxBarcodeStatusLabel.setText("None"));
        }
    }
    //update barcode no
    public void updatebarcodeno(String response) {
        //set to activated/standby
        Platform.runLater(() -> fxBarcodeNoLabel.setText(response));
    }
} // TouchDisplayEmulatorController
