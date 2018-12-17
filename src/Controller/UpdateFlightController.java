package Controller;

import Model.FlightDatabase;
import Model.KeyManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

public class UpdateFlightController extends ComponentController
{
    @FXML
    private TextField flightNumberField;

    @FXML
    private TextField flightStatusField;


    @FXML
    private Label messageLabel;

    public void updateFlight(ActionEvent event) throws IOException
    {
        messageLabel.setVisible(false);
        int flightNumber = 0;
        FlightDatabase db = FlightDatabase.getInstance();
        flightNumber = KeyManager.getFlightNumber(flightNumberField.getText());
        if(db.getFlight(flightNumber) == null)
        {
            messageLabel.setVisible(true);
            messageLabel.setText("Flight does Not Exist");
            messageLabel.setTextFill(Color.RED);
        }
        else if(flightStatusField.getText().equals(""))
        {
            messageLabel.setVisible(true);
            messageLabel.setText("Enter New Status");
            messageLabel.setTextFill(Color.RED);
        }
        else
        {
            db.editFlight(flightNumber,flightStatusField.getText());
            flightStatusField.setText("");
            flightNumberField.setText("");
            messageLabel.setText("Successfully  changed flight status");
        }
    }
}
