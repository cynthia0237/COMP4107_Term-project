# Group 1 Smart Locker Controlling System

Group member: Leung Cynthia

&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Chan Ho Lam Thomas

&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp; Chan Chak Nam

<br/>

## The structure of location of the directories and files

---

* AppKickstarter
  * misc
    * AppThread.java
    * Lib.java
    * LogFormatter.java
    * MBox.java
    * Msg.java
  * timer
    * Timer.java
  * AppKickstarter.java

<br/>

AppKickstarter is proved by Dr. Yau the details can refer from the PowerPoint

---

* DeliveryCompan
  * DeliveryCompany.fxml
  * DeliveryCompanyController.java
  * DeliveryCompanyEmulator.java

<u>DeliveryCompany </u>

* This package emulates the delivery company to tell the server they want to reserve the locker.

---

* SLC
  * BarcodeReaderDriver
    * Emulator
      * BarcodeReaderEmulator.fxml
      * BarcodeReaderEmulator.java
      * BarcodeReaderEmulatorController.java
    * BarcodeReaderDriver.java
  * LockerReaderDriver
    * Emulator
      * LockerReaderEmulator.fxml
      * LockerReaderEmulator.java
      * LockerReaderEmulatorController.java
    * LockerReaderDriver.java
  * OctopusCardReaderDriver
    * Emulator
      * OctopusCardReaderEmulator.fxml
      * OctopusCardReaderEmulator.java
      * OctopusCardReaderEmulatorController.java
    * OctopusCardReaderDriver.java

<u>BarcodeReaderDriver, LockerReaderDriver and OctopusCardReaderDriver</u>

* These are the hardware components emulators
* The *.fxml is the emulators GUI
* The EmulatorController.java is the fxml controller
* Emulator.java is communicate between the hardware driver and the controller.
* The Deiver.java will send the message to SLC to process further action.

---

* SLC
  * Locker
    * Locker.java
    * LockerManager.java
    * LockerSize.java
    * LockerStatus.java

<u>Locker</u>

* <u>Locker.java</u>

&emsp; &emsp; &emsp;Locker is a class to store each locker object attributes

* <u>LockerManager.java</u>

&emsp; &emsp; &emsp;LockerManager is the class implement with the Singleton design pattern because we need to restrict one and only one data set of the lockers.

* <u>LockerSize.java and LockerStatus.java</u>

&emsp; &emsp; &emsp;These are the Enum type representing the locker size and the locker status.

---

* SLC
  * SLC
    * SLC.java

<u>SLC.java</u>

* This class is the controller it all of the smart locker processes. The process included the process of a mouse click, checking the locker pickup passcode, whether the locker has a payment or not when the customer picks up the parcer and communicating with different hardware

---

* SLC
  * TouchDisplayHandler
    * Emulator
      * BarcodeDisplayEmulator.fxml
      * PickupPasscodeEnter.fxml
      * TouchDisplayEmulator.fxml
      * TouchDisplayEmulator.java
      * TouchDisplayEmulatorController.java
      * TouchDisplayMainMenu.fxml
      * TouchDisplayOpenLocker.fxml
      * TouchDisplayPayment.fxml
    * TouchDisplayHandler.java

<u>TouchDisplayHandler</u>

* This is the touch screen emulator
* For all the fxml are the different scene of the touch display all the fxml is using the same controller which is TouchDisplayEmulatorController.java
* The TouchDisplayEmulator.java will do different actions depending on what message it receives from the handler
* TouchDisplayHandler.java focuses on the communication with the SLC by sending messages to SLC and receiving messages from SLC

---

* SLC
  * SLSvr.java

<u>SLSvr.java</u>

* This is a class of the SLC server it will handle the verification of the barcode and the recevie the locker.
* The SLSvr uses the Singleton design pattern to let the delivery company tell the server to reserve a locker. Reserve a locker should implement it with the RESTful API in a normal situation, but we do not have to implement the database or the server here. We emulate it to implement with the Singleton to restrict only one server.

---

* SLC
  * SLCEmulatorStarter.java
  * SLCStarter.java

<u>SLCEmulatorStarter.java</u>

* Start all the emulator

<u>SLCStarter.java</u>

* Start the SLC

---

<br/>

## Instructions for compiling, starting and stopping of your system

---

**Compile Programme**

* If you have IntrlliJ IDEA application you can set the Run/Debug Configurations The Name is SLCEmulatorStarter. For the Build and run choose the java 8 environment.

* For window / apple user using the cmd / Terminal to compile programme

1. Open cmd or Terminal
2. cd to the project directory

```
3. type 
javac AppKickstarter/*.java AppKickstarter/misc/*.java AppKickstarter/timer/*.java DeliveryCompany/*.java SLC/*.java SLC/BarcodeReaderDriver/*.java SLC/BarcodeReaderDriver/Emulator*.java SLC/HWHandler/*.java SLC/Locker/*.java SLC/LockerReaderDriver/*.java SLC/LockerReaderDriver/Emulator*.java SLC/OctopusCardReaderDriver/*.java SLC/OctopusCardReaderDriver/Emulator*.java SLC/TouchDisplayHandler/*.java SLC/TouchDisplayHandler/Emulator*.java SLC/*.java
in one line on the cmd / Terminal
```

**Start and stop the system**

* For IntrlliJ IDEA user
  * Click run to start the system
  * Close the one of the java application to stop the system

* For the user using the terminal or cmd
  1. cd to the project directory

  ```
  2. java SLCEmulatorStarter to start the system
  ```

  * Close the one of the java application to stop the system

Note that : If close the application of the delivery company is, the system won't stop because the delivery company will be regarded as an outsider

---
<br/>

## System flow

<u>Step of Reserve Locker</u>

1. Use the Delivery Company emulator choose the barcode and the size of the locker
2. Click the "Send to Server" button to reserve the locker
3. The textArea will display the message success or not to reserve the locker

<u>Step of Scan Barcode</u>

1.	Click the blank screen to start use the system
2.	Select "Scan Barcode" in the main page
3.	Select the barcode and click the "Send Barcode" button in Barcode Reader emulator
4.	Sending the barcode No to SLC and to server to verify the barcode
5.	Verify Barcode to return the locker id
6.	Touch Screen Display will show the barcode number and locker number
7.	The relatively locker will open (which will turn to black color and show in Locker Reader emulator)


<u>Step of Parcer Pickup</u>

1. Click the blank screen to start use the system
2. Select "Parcer Pickup" in the main page
3. Enter the pickup passcode
    * If enter passcode is incorrect system will require input again
    * If passcod is correct will check have payment or not
      * If no payment the relatively locker will open (which will turn to black color and show in Locker Reader emulator) and show which locker will be open in the touch display
      * If have payment touch display will turn to page of payment and the system will active Octopus Card Reader
4. Click the locker to close it
5. Update display to the blacnk screen after close the locker

<u>Step of Payment</u>

1. Check the late day and the total charge on the touch display screen by clicking the "Get Detail"
2. "Total charge" is get from the equation of (late day * 10)
3. The Octopus Card Reader waits for the user to make the payment by bringing his/her Octopus Card to the Reader
4. Select one of number to choose a default Octopus Card number (1, 2, and 3)
5. Press "Send" button to make the payment
6. The Octopus Card Reader collects the money from the Octopus Card (assume that the transaction must be success in anyway)
7. Display the message in the octopus card reader to check what have done
8. The relatively locker will open (which will turn to black color and show in Locker Reader emulator) and show which locker will be open in the touch display
9. Octopus Card Reader will go to standby mode

<u>Detail of open locker</u>

1. SLC will check the current user is staff or not

If is staff

2. SLC will send the message to server to remove the used barcode

If is customer
2. SLC will remove the used passcode
3. SLC will send the message to the server, let the server backup the passcode

<u>Detail of close locker</u>

Close the locker means finsh check in or pickup parcer

1. SLC will check the current user is staff or not

If is staff

2. SLC will generate 8-digit pickup passcode for the locker and log the message
3. SLC will send the message to the server, let the server backup the passcode
4. SLC will set the locker startTime (For check the payment use)
5. SLC will set the locker status to InUse and log the message

If is customer

2. SLC will set the locker status to Available and log the message

---
<br/>

## Response and Health Poll

*Touch Display response and health poll*
<u>About the health poll</u>

* When the poll response is NAK, it doesn't have any repose in the Touch Display which means you click you cannot turn to any scene or do other processes

<u>About the ACK response</u>

* You can use the touch display and TouchDisplayHandler will send the process message to SLC to let SLC do the xy position checking
* The system will log the TouchDisplayHandler is up message

<u>About the Ignore response</u>

* You still can use the touch display and TouchDisplayHandler will send the process message to SLC to let SLC do the XY position checking
* There dont have any poll message will log

<br/>

**Barcode Reader response and health poll**

<u><ins>About the health poll</ins></u>

* When the poll response is NAK, you can’t do anything in the Barcode Reader and can’t receive any things outside.

<u><ins>About the active response Status</ins></u>

* “Activated”: the barcode will be activated and allowed to scan the barcode. Also, sending the reply to SLC  showing the working and activated status in touchscreen(barcode)
* “Standby”: the barcode will be set to standby and not available to scan. Then, sending the response to SLCshowing the not working and standby status in touchscreen(barcode)
* “Ignore”: the barcode will do nothing with the incoming request. The touchscreen(barcode) will show not working.

<u><ins>About the standby response</ins></u>
* Same logic from the active response and do the same thing as the active response.

</br>

*Octopus Card Reader response and health poll*

<u>About the health poll</u>

* When the poll response is NAK, you can’t do anything in the Octopus Card Reader

<u>About the active response</u>

* When the active response is "Activated", it should become active, and send a reply to SLC: "Octopus Card Reader: Go Active"
* When the active response is "Standby", it should become standby, and send a reply to SLC: "Octopus Card Reader: Go Standby"
* When the active response is "Ignore", it ignore the message and do not give any reply

<u>About the standby response</u>

* Same logic from the active response and do the same thing as the active response.
