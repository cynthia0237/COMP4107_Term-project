# Group 1 Smart Locker Controlling System

Group member: Leung Cynthia&emsp;&emsp;&emsp; &nbsp; &nbsp; 21201943

&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Chan Ho Lam Thomas&emsp;21221839

&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Chan chak nam&emsp;&emsp;&emsp;&emsp;21237395

<br/>

## The structure of location of the directories and files

---

* [AppKickstarter/](.\src\AppKickstarter)
  * [misc/](.\src\AppKickstarter\misc)
    * [AppThread.java](.\src\AppKickstarter\misc\AppThread.java)
    * [Lib.java](.\src\AppKickstarter\misc\Lib.java)
    * [LogFormatter.java](.\src\AppKickstarter\misc\LogFormatter.java)
    * [MBox.java](.\src\AppKickstarter\misc\MBox.java)
    * [Msg.java](.\src\AppKickstarter\misc\Msg.java)
  * [timer/](.\src\AppKickstarter\timer)
    * [Timer.java](.\src\AppKickstarter\timer\Timer.java)
  * [AppKickstarter.java](.\src\AppKickstarter\AppKickstarter.java)

<br/>

AppKickstarter is proved by Dr. Yau the details can refer from the PowerPoint

---

* [DeliveryCompany/](.\src\DeliveryCompany)
  * [DeliveryCompany.fxml](.\src\DeliveryCompany\DeliveryCompany.fxml)
  * [DeliveryCompanyController.java](.\src\DeliveryCompany\DeliveryCompanyController.java)
  * [DeliveryCompanyEmulator.java](.\src\DeliveryCompany\DeliveryCompanyEmulator.java)

<u>DeliveryCompany </u>

* This package emulates the delivery company to tell the server they want to reserve the locker.

---

* [SLC/](.\src\SLC)
  * [BarcodeReaderDriver/](.\src\SLC\BarcodeReaderDriver)
    * [Emulator/](.\src\SLC\BarcodeReaderDriver\Emulator)
      * [BarcodeReaderEmulator.fxml](.\src\SLC\BarcodeReaderDriver\Emulator\BarcodeReaderEmulator.fxml)
      * [BarcodeReaderEmulator.java](.\src\SLC\BarcodeReaderDriver\Emulator\BarcodeReaderEmulator.java)
      * [BarcodeReaderEmulatorController.java](.\src\SLC\BarcodeReaderDriver\Emulator\BarcodeReaderEmulatorController.java)
    * [BarcodeReaderDriver.java](.\src\SLC\BarcodeReaderDriver\BarcodeReaderDriver.java)
  * [LockerReaderDriver/](.\src\SLC\LockerReaderDriver)
    * [Emulator/](.\src\SLC\LockerReaderDriver\Emulator)
      * [LockerReaderEmulator.fxml](.\src\SLC\LockerReaderDriver\Emulator\LockerReaderEmulator.fxml)
      * [LockerReaderEmulator.java](.\src\SLC\LockerReaderDriver\Emulator\LockerReaderEmulator.java)
      * [LockerReaderEmulatorController.java](.\src\SLC\LockerReaderDriver\Emulator\LockerReaderEmulatorController.java)
    * [LockerReaderDriver.java](.\src\SLC\LockerReaderDriver\LockerReaderDriver.java)
  * [OctopusCardReaderDriver/](.\src\SLC\OctopusCardReaderDriver)
    * [Emulator/](.\src\SLC\OctopusCardReaderDriver\Emulator)
      * [OctopusCardReaderEmulator.fxml](.\src\SLC\OctopusCardReaderDriver\Emulator\OctopusCardReaderEmulator.fxml)
      * [OctopusCardReaderEmulator.java](.\src\SLC\OctopusCardReaderDriver\Emulator\OctopusCardReaderEmulator.java)
      * [OctopusCardReaderEmulatorController.java](.\src\SLC\OctopusCardReaderDriver\Emulator\OctopusCardReaderEmulatorController.java)
    * [OctopusCardReaderDriver.java](.\src\SLC\OctopusCardReaderDriver\OctopusCardReaderDriver.java)

<u>BarcodeReaderDriver, LockerReaderDriver and OctopusCardReaderDriver</u>

* These are the hardware components emulators
* The *.fxml is the emulators GUI
* The EmulatorController.java is the fxml controller
* Emulator.java is communicate between the hardware driver and the controller.
* The Deiver.java will send the message to SLC to process further action.

---

* [SLC/](.\src\SLC)
  * [Locker/](.\src\SLC\Locker)
    * [Locker.java](.\src\SLC\Locker\Locker.java)
    * [LockerManager.java](.\src\SLC\Locker\LockerManager.java)
    * [LockerSize.java](.\src\SLC\Locker\LockerSize.java)
    * [LockerStatus.java](.\src\SLC\Locker\LockerStatus.java)

<u>Locker</u>

* <u>Locker.java</u>

&emsp; &emsp; &emsp;Locker is a class to store each locker object attributes

* <u>LockerManager.java</u>

&emsp; &emsp; &emsp;LockerManager is the class implement with the Singleton design pattern because we need to restrict one and only one data set of the lockers.

* <u>LockerSize.java and LockerStatus.java</u>

&emsp; &emsp; &emsp;These are the Enum type representing the locker size and the locker status.

---

* [SLC/](.\src\SLC)
  * [SLC/](.\src\SLC\SLC)
    * [SLC.java](.\src\SLC\SLC\SLC.java)

<u>SLC.java</u>

* This class is the controller it all of the smart locker processes. The process included the process of a mouse click, checking the locker pickup passcode, whether the locker has a payment or not when the customer picks up the parcer and communicating with different hardware

---

* [SLC/](.\src\SLC)
  * [TouchDisplayHandler/](.\src\SLC\TouchDisplayHandler)
    * [Emulator/](.\src\SLC\TouchDisplayHandler\Emulator)
      * [BarcodeDisplayEmulator.fxml](.\src\SLC\TouchDisplayHandler\Emulator\BarcodeDisplayEmulator.fxml)
      * [PickupPasscodeEnter.fxml](.\src\SLC\TouchDisplayHandler\Emulator\PickupPasscodeEnter.fxml)
      * [TouchDisplayEmulator.fxml](.\src\SLC\TouchDisplayHandler\Emulator\TouchDisplayEmulator.fxml)
      * [TouchDisplayEmulator.java](.\src\SLC\TouchDisplayHandler\Emulator\TouchDisplayEmulator.java)
      * [TouchDisplayEmulatorController.java](.\src\SLC\TouchDisplayHandler\Emulator\TouchDisplayEmulatorController.java)
      * [TouchDisplayMainMenu.fxml](.\src\SLC\TouchDisplayHandler\Emulator\TouchDisplayMainMenu.fxml)
      * [TouchDisplayOpenLocker.fxml](.\src\SLC\TouchDisplayHandler\Emulator\TouchDisplayOpenLocker.fxml)
      * [TouchDisplayPayment.fxml](.\src\SLC\TouchDisplayHandler\Emulator\TouchDisplayPayment.fxml)
    * [TouchDisplayHandler.java](.\src\SLC\TouchDisplayHandler\TouchDisplayHandler.java)

<u>TouchDisplayHandler</u>

* This is the touch screen emulator
* For all the fxml are the different scene of the touch display all the fxml is using the same controller which is TouchDisplayEmulatorController.java
* The TouchDisplayEmulator.java will do different actions depending on what message it receives from the handler
* TouchDisplayHandler.java focuses on the communication with the SLC by sending messages to SLC and receiving messages from SLC

---

* [SLC/](.\src\SLC)
  * [SLSvr.java](.\src\SLC\SLSvr.java)

<u>SLSvr.java</u>

* This is a class of the SLC server it will handle the verification of the barcode and the recevie the locker.
* The SLSvr uses the Singleton design pattern to let the delivery company tell the server to reserve a locker. Reserve a locker should implement it with the RESTful API in a normal situation, but we do not have to implement the database or the server here. We emulate it to implement with the Singleton to restrict only one server.

---

* [SLC/](.\src\SLC)
  * [SLCEmulatorStarter.java](.\src\SLC\SLCEmulatorStarter.java)
  * [SLCStarter.java](.\src\SLC\SLCStarter.java)

<u>SLCEmulatorStarter.java</u>

* Start all the emulator

<u>SLCStarter.java</u>

* Start the SLC

---

<br/>

## Instructions for compiling, starting and stopping of your system

---

