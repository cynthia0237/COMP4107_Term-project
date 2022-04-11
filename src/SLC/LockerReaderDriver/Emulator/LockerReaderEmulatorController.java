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
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
    public ChoiceBox pollRespCBox;

    public GridPane lockerGp1, lockerGp2;
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
//        LockerManager.getInstance().printLockers();
        LockerManager.getInstance().getLockerById("33").setStartTime(System.currentTimeMillis());
//        //test auto open
//        openLocker(getRectNodeInFxml("1"));
//        openLocker(getRectNodeInFxml("28"));


    } // initialize


    //------------------------------------------------------------



    //------------------------------------------------------------
    // getters
    public String getPollResp() { return pollResp; }






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

            if (locker.getLockerStatus() == LockerStatus.Open) {
                rect.setFill(Color.WHITE);
                lockerReaderMBox.send(new Msg(id, lockerReaderMBox, Msg.Type.CloseLocker, lockerId));
                //lockerReaderMBox.send(new Msg(id, lockerReaderMBox, Msg.Type.FinishPickup, lockerId));
            }
        }
    }

    public Node getRectNodeInFxml(String lockerId) {
        int id = Integer. parseInt(lockerId);
        ObservableList<Node> childs;
        if (id > 20) {
            childs = lockerGp2.getChildren();
            id -= 20;
        } else {
            childs = lockerGp1.getChildren();
        }

        return childs.get(id-1);
    }

    public void openLocker(Node node) {
        if (node != null) {
            Pane pane = (Pane) node;
            Rectangle rect = (Rectangle) pane.getChildren().get(0);
            rect.setFill(Color.BLACK);
        }
    }

} // LockerReaderEmulatorController
