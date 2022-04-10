package SLC;

import AppKickstarter.timer.Timer;

import DeliveryCompany.DeliveryCompanyEmulator;
import SLC.SLC.SLC;
import SLC.BarcodeReaderDriver.BarcodeReaderDriver;
import SLC.BarcodeReaderDriver.Emulator.BarcodeReaderEmulator;
import SLC.TouchDisplayHandler.Emulator.TouchDisplayEmulator;
import SLC.TouchDisplayHandler.TouchDisplayHandler;
import SLC.OctopusCardReaderDriver.OctopusCardReaderDriver;
import SLC.OctopusCardReaderDriver.Emulator.OctopusCardReaderEmulator;
import SLC.LockerReaderDriver.LockerReaderDriver;
import SLC.LockerReaderDriver.Emulator.LockerReaderEmulator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


//======================================================================
// SLCEmulatorStarter
public class SLCEmulatorStarter extends SLCStarter {
    //------------------------------------------------------------
    // main
    public static void main(String [] args) {
	new SLCEmulatorStarter().startApp();
    } // main


    //------------------------------------------------------------
    // startHandlers
    @Override
    protected void startHandlers() {
        Emulators.slcEmulatorStarter = this;
        new Emulators().start();
    } // startHandlers


    //------------------------------------------------------------
    // Emulators
    public static class Emulators extends Application {
        private static SLCEmulatorStarter slcEmulatorStarter;

        //should be an outsider
        private DeliveryCompanyEmulator deliveryCompanyEmulator = new DeliveryCompanyEmulator();

	//----------------------------------------
	// start
        public void start() {
            launch();
	} // start

	//----------------------------------------
	// start
        public void start(Stage primaryStage) {
	    Timer timer = null;
	    SLC slc = null;
	    BarcodeReaderEmulator barcodeReaderEmulator = null;
	    TouchDisplayEmulator touchDisplayEmulator = null;
        OctopusCardReaderEmulator octopusCardReaderEmulator = null;
        LockerReaderEmulator lockerReaderEmulator = null;
        SLSvr svr = null;

	    // create emulators
	    try {
	        timer = new Timer("timer", slcEmulatorStarter);
	        slc = new SLC("SLC", slcEmulatorStarter);
	        barcodeReaderEmulator = new BarcodeReaderEmulator("BarcodeReaderDriver", slcEmulatorStarter);
	        touchDisplayEmulator = new TouchDisplayEmulator("TouchDisplayHandler", slcEmulatorStarter);
            octopusCardReaderEmulator = new OctopusCardReaderEmulator("OctopusCardReaderDriver", slcEmulatorStarter);
            lockerReaderEmulator = new LockerReaderEmulator("LockerReaderDriver", slcEmulatorStarter);
            svr = new SLSvr("SLSvr", slcEmulatorStarter);


		// start emulator GUIs
            barcodeReaderEmulator.start();
            touchDisplayEmulator.start();
            octopusCardReaderEmulator.start();
            lockerReaderEmulator.start();

            deliveryCompanyEmulator.start();
	    } catch (Exception e) {
            System.out.println("Emulators: start failed");
            e.printStackTrace();
            Platform.exit();
	    }
	    slcEmulatorStarter.setTimer(timer);
	    slcEmulatorStarter.setSLC(slc);
	    slcEmulatorStarter.setBarcodeReaderDriver(barcodeReaderEmulator);
	    slcEmulatorStarter.setTouchDisplayHandler(touchDisplayEmulator);
        slcEmulatorStarter.setOctopusCardReaderDriver(octopusCardReaderEmulator);
        slcEmulatorStarter.setLockerReaderDriver(lockerReaderEmulator);
        slcEmulatorStarter.setSLSvr(svr);

	    // start threads
	    new Thread(timer).start();
	    new Thread(slc).start();
        new Thread(barcodeReaderEmulator).start();
	    new Thread(touchDisplayEmulator).start();
        new Thread(octopusCardReaderEmulator).start();
        new Thread(lockerReaderEmulator).start();
        new Thread(svr).start();
	} // start
    } // Emulators


    //------------------------------------------------------------
    //  setters
    private void setTimer(Timer timer) {
        this.timer = timer;
    }
    private void setSLC(SLC slc) {
        this.slc = slc;
    }
    private void setBarcodeReaderDriver(BarcodeReaderDriver barcodeReaderDriver) {
        this.barcodeReaderDriver = barcodeReaderDriver;
    }
    private void setTouchDisplayHandler(TouchDisplayHandler touchDisplayHandler) {
        this.touchDisplayHandler = touchDisplayHandler;
    }
    private void setOctopusCardReaderDriver(OctopusCardReaderDriver octopusCardReaderDriver) {
        this.octopusCardReaderDriver = octopusCardReaderDriver;
    }
    private void setLockerReaderDriver(LockerReaderDriver lockerReaderDriver) {
        this.lockerReaderDriver = lockerReaderDriver;
    }
    private void setSLSvr(SLSvr svr) {
        this.svr = svr;
    }
} // SLCEmulatorStarter
