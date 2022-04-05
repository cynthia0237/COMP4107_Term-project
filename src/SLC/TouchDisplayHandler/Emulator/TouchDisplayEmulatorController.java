package SLC.TouchDisplayHandler.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import SLC.BarcodeReaderDriver.BarcodeReaderDriver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private String selectedScreen;
    private String pollResp;
    public ChoiceBox screenSwitcherCBox;
    public ChoiceBox pollRespCBox;

    public TextField passcodeTF;
    String textValue = "";

    private Stage stage;
    private Scene scene;
    private Parent root;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, TouchDisplayEmulator touchDisplayEmulator, String pollRespParam) {
        this.id = id;
	this.appKickstarter = appKickstarter;
	this.log = log;
	this.touchDisplayEmulator = touchDisplayEmulator;
	this.touchDisplayMBox = appKickstarter.getThread("TouchDisplayHandler").getMBox();
    this.barcodeReaderMBox = appKickstarter.getThread("BarcodeReaderDriver").getMBox();
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

//                    case "EnterPasscode":
//                        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "EnterPasscode"));
//                        break;
                }
            }
        });
        this.selectedScreen = screenSwitcherCBox.getValue().toString();

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
	touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_MouseClicked, x + " " + y));
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

    public void switchToMainMenu_action(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TouchDisplayMainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
    }

    public void switchToMainMenu_mouse(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TouchDisplayMainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "MainMenu"));
    }

    public void onPickupBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("PickupPasscodeEnter.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        touchDisplayMBox.send(new Msg(id, touchDisplayMBox, Msg.Type.TD_UpdateDisplay, "EnterPasscode"));
    }

    public void onStaffLoginBtnClick(ActionEvent event) {


    }
    public void barcodebuttonclicked(ActionEvent actionEvent) {
        //(testing) Click the button to call barcode activated inorder to verify the barcode
        barcodeReaderMBox.send(new Msg(id,touchDisplayMBox,Msg.Type.BR_GoActive,""));
    }

    //region Control passcode enter
    public void onNumClick(MouseEvent event)
    {
        Pane pane = (Pane) event.getSource();
        textValue += pane.getId().replace("btn", "");
        passcodeTF.setText(textValue);
    }

    public void onSymbolClick(MouseEvent event)
    {
        Pane pane = (Pane) event.getSource();
        String symbol = pane.getId();
        switch (symbol)
        {
            case "C":
                if(!textValue.isEmpty())
                    textValue = textValue.substring(0,textValue.length()-1);
                passcodeTF.setText(textValue);
                break;

            case "OK":
                System.out.println("submitted passcode " + passcodeTF.getText());
                break;
        }
    }


    //endregion

} // TouchDisplayEmulatorController
