package AppKickstarter.misc;


//======================================================================
// Msg
public class Msg {
    private String sender;
    private MBox senderMBox;
    private Type type;
    private String details;

    //------------------------------------------------------------
    // Msg
    /**
     * Constructor for a msg.
     * @param sender id of the msg sender (String)
     * @param senderMBox mbox of the msg sender
     * @param type message type
     * @param details details of the msg (free format String)
     */
    public Msg(String sender, MBox senderMBox, Type type, String details) {
	this.sender = sender;
	this.senderMBox = senderMBox;
	this.type = type;
	this.details = details;
    } // Msg


    //------------------------------------------------------------
    // getSender
    /**
     * Returns the id of the msg sender
     * @return the id of the msg sender
     */
    public String getSender()     { return sender; }


    //------------------------------------------------------------
    // getSenderMBox
    /**
     * Returns the mbox of the msg sender
     * @return the mbox of the msg sender
     */
    public MBox   getSenderMBox() { return senderMBox; }


    //------------------------------------------------------------
    // getType
    /**
     * Returns the message type
     * @return the message type
     */
    public Type   getType()       { return type; }


    //------------------------------------------------------------
    // getDetails
    /**
     * Returns the details of the msg
     * @return the details of the msg
     */
    public String getDetails()    { return details; }


    //------------------------------------------------------------
    // toString
    /**
     * Returns the msg as a formatted String
     * @return the msg as a formatted String
     */
    public String toString() {
	return sender + " (" + type + ") -- " + details;
    } // toString


    //------------------------------------------------------------
    // Msg Types
    /**
     * Message Types used in Msg.
     * @see Msg
     */
    public enum Type {
        /** Terminate the running thread */	    Terminate,
	    /** Generic error msg */		        Error,
	    /** Set a timer */			            SetTimer,
	    /** Set a timer */			            CancelTimer,
	    /** Timer clock ticks */		        Tick,
	    /** Time's up for the timer */		    TimesUp,
	    /** Health poll */			            Poll,
	    /** Health poll +ve acknowledgement */	PollAck,
        /** Health poll -ve acknowledgement */	PollNak,
	    /** Update Display */			        TD_UpdateDisplay,
	    /** Mouse Clicked */			        TD_MouseClicked,
        /** Passcode input error */             TD_WrongPasscode,
        /** correct passcode */                 TD_CorrectPasscode,
        /** Display user input passcode */      TD_SetPasscodeTF,
        /**  receive late day from slc */       TD_ReceiveLateDay,

        /** Barcode Reader Go Activate */	    BR_GoActive,
        /** Barcode Reader Go Standby */	    BR_GoStandby,
        /** Barcode Reader Go Activate Response*/	BR_GoActive_Response,
        /** Barcode Reader Go Standby Response*/	BR_GoStandby_Response,

        /** Card inserted */			        BR_BarcodeRead,

        /** Octopus Card Reader Go Activate */	OCR_GoActive,
        /** Octopus Card Reader Go Standby */	OCR_GoStandby,
        /** Octopus Card inserted */			OCR_OctopusCardRead,
        /** Octopus Card receive payment request*/ OCR_ReceivePayment,
        /** back to main page after payment*/      OCR_BackToMainPage,
        /** receive late day from slc*/            OCR_ReceiveLateDay,
         /** receive late day from slc*/           OCR_SwitchToPayment,
         /** receive lockerId from slc*/        OCR_ReceiveLockerId,
        /** Passcode value */ Passcode,

        /** Check pickup passcode */            CheckPickupPasscode,
        /** Open Locker */                      OpenLocker,
        /** Close Locker */                     CloseLocker,

        /** Barcode Verification */             BarcodeVerification,
        /** Update server backup passcode map */ BackupPasscodeMap

    } // Type
} // Msg
