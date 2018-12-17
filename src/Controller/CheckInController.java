package Controller;

import Model.KeyManager;
import Model.ReservationDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class CheckInController extends ComponentController
{
    @FXML
    private javafx.scene.control.TextField confirmText;

    @FXML
    private javafx.scene.control.TextField bagsText;

    @FXML
    private javafx.scene.control.Label checkInReturn;


    public void submitCheckIn(ActionEvent event) throws IOException
    {
        ReservationDatabase db = ReservationDatabase.getInstance();
        int confirmationNumber = KeyManager.decryptConfirmationNumber(confirmText.getText());

        if(!db.checkReservation(confirmationNumber))
        {
            checkInReturn.setText("Invalid Confirmation Number");
            checkInReturn.setTextFill(Paint.valueOf("DA1309"));
            return;
        }
        if(!db.checkStatus(confirmationNumber))
        {
            checkInReturn.setText("Seat Has Already Been Checked In");
            checkInReturn.setTextFill(Paint.valueOf("DA1309"));
            return;
        }

        int bags = Integer.valueOf(bagsText.getText());
        db.setBags(confirmationNumber,bags);

        checkInReturn.setText("Successful Check In");
    }

    public void openLink(ActionEvent event) throws IOException
    {
        FXMLLoader loaderWeb = null;
        Stage stageWeb = new Stage();
        WebView web = new WebView();
        web.getEngine().load("https://www.united.com/ual/en/us/fly/travel/baggage.html");
        Scene scene = new Scene(web);
        stageWeb.setScene(scene);
        stageWeb.show();
    }
}
