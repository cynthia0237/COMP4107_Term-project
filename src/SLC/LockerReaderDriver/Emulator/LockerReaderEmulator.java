package SLC.LockerReaderDriver.Emulator;

import AppKickstarter.misc.Msg;
import SLC.LockerReaderDriver.LockerReaderDriver;
import SLC.SLCStarter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

//======================================================================
// LockerReaderEmulator
public class LockerReaderEmulator extends LockerReaderDriver {
    private SLCStarter slcStarter;
    private String id;
    private Stage myStage;
    private LockerReaderEmulatorController lockerReaderEmulatorController;

    //------------------------------------------------------------
    // LockerReaderEmulator
    public LockerReaderEmulator(String id, SLCStarter slcStarter) {
        super(id, slcStarter);
        this.slcStarter = slcStarter;
        this.id = id;
    } // LockerReaderEmulator


    //------------------------------------------------------------
    // start
    public void start() throws Exception {
        Parent root;
        myStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        String fxmlName = "LockerReaderEmulator.fxml";
        loader.setLocation(LockerReaderEmulator.class.getResource(fxmlName));
        root = loader.load();
        lockerReaderEmulatorController = (LockerReaderEmulatorController) loader.getController();
        lockerReaderEmulatorController.initialize(id, slcStarter, log, this);
        myStage.initStyle(StageStyle.DECORATED);
        myStage.setScene(new Scene(root, 700, 600));
        myStage.setTitle("Locker Reader");
        myStage.setResizable(false);
        myStage.setOnCloseRequest((WindowEvent event) -> {
            slcStarter.stopApp();
            Platform.exit();
        });
        myStage.show();
    } // LockerReaderEmulator


    //------------------------------------------------------------
    // handlePoll
    protected void handlePoll() {
        // super.handlePoll();

        switch (lockerReaderEmulatorController.getPollResp()) {
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
} // LockerReaderEmulator
