package DeliveryCompany;

import SLC.Locker.LockerSize;
import SLC.SLSvr;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class DeliveryCompanyController {

    public TextArea companyTextArea;
    public ChoiceBox barcodeCBox;
    public ChoiceBox sizeCBox;

    public void onSendToServerClick(ActionEvent actionEvent) {
        String barcode = (String) barcodeCBox.getValue();
        String size = (String) sizeCBox.getValue();

        if(SLSvr.getInstance().reserveLocker(barcode, LockerSize.valueOf(size))) {
            companyTextArea.appendText("locker reserved for barcode - " + barcode + "\n");
        } else {
            companyTextArea.appendText("do not have suitable locker for barcode - " + barcode + " or the barcode already reserve a locker\n");
        }


    }

}
