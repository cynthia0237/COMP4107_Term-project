package SLC.LockerReaderDriver.Emulator;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.MBox;
import AppKickstarter.misc.Msg;
import SLC.Locker.Locker;
import SLC.Locker.LockerManager;
import SLC.Locker.LockerSize;
import SLC.Locker.LockerStatus;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.logging.Logger;

//======================================================================
// LockerReaderEmulatorController
public class LockerReaderEmulatorController {
    private String id;
    private AppKickstarter appKickstarter;
    private Logger log;
    private LockerReaderEmulator lockerReaderEmulator;
    private MBox lockerReaderMBox;
    private String pollResp;
    public TextField lockerNumField;
    public TextField lockerReaderStatusField;
    public TextArea lockerReaderTextArea;
    public ChoiceBox pollRespCBox;

    int totalLocker;


    //------------------------------------------------------------
    // initialize
    public void initialize(String id, AppKickstarter appKickstarter, Logger log, LockerReaderEmulator lockerReaderEmulator) {
        this.id = id;
        this.appKickstarter = appKickstarter;
        this.log = log;
	    this.lockerReaderEmulator = lockerReaderEmulator;
	    this.lockerReaderMBox = appKickstarter.getThread("LockerReaderDriver").getMBox();
	    this.pollRespCBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pollResp = pollRespCBox.getItems().get(newValue.intValue()).toString();
            }
        });

        this.pollResp = pollRespCBox.getValue().toString();
        instantiateLocker();

        //Debug use
        LockerManager.getInstance().printLockers();

    } // initialize


    //------------------------------------------------------------
    // buttonPressed
    public void buttonPressed(ActionEvent actionEvent) {
	Button btn = (Button) actionEvent.getSource();

	switch (btn.getText()) {
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
    public String getPollResp() { return pollResp; }



    // updateLockerReaderStatus
    private void updateLockerReaderStatus(String status) {
        lockerReaderStatusField.setText(status);
    } // updateLockerReaderStatus



    //------------------------------------------------------------

    private void instantiateLocker() {
        totalLocker = 40;
        for (int i = 1; i <= totalLocker; i++) {
            String cfgSize = appKickstarter.getProperty("Locker" + i + ".Size");
            Locker locker = new Locker(String.valueOf(i), LockerSize.valueOf(cfgSize));
            LockerManager.getInstance().lockers.add(locker);
            LockerManager.getInstance().lockerMap.put(Integer.toString(i), locker);
        }
    }

    public void onLockerClick(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        String lockerId = pane.getId().replace("locker", "");
        Locker locker = LockerManager.getInstance().getLockerById(lockerId);

        if (!locker.isLock()) {
            Rectangle rect = (Rectangle) pane.getChildren().get(0);
            if (locker.getLockerStatus() == LockerStatus.InUse) {
                rect.setFill(Color.BLACK);
                lockerReaderMBox.send(new Msg(id, lockerReaderMBox, Msg.Type.OpenLocker, lockerId));
            }

            if (locker.getLockerStatus() == LockerStatus.Open) {
                rect.setFill(Color.WHITE);
                lockerReaderMBox.send(new Msg(id, lockerReaderMBox, Msg.Type.CloseLocker, lockerId));
            }
        }
    }

} // LockerReaderEmulatorController
