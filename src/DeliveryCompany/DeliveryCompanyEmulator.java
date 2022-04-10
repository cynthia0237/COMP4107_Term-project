package DeliveryCompany;

import AppKickstarter.AppKickstarter;
import AppKickstarter.misc.Msg;
import SLC.HWHandler.HWHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DeliveryCompanyEmulator {

    Stage stage;

    public void start() throws IOException
    {
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("DeliveryCompany.fxml"));
        stage.setTitle("Delivery Company");
        stage.setScene(new Scene(root, 350, 470));
        stage.show();

    }

}
