package Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.io.IOException;
import Model.Flight;

public class StatusController extends ComponentController
{
    @FXML
    private Button submitButton;
    @FXML
    private TextField flightField;
    @FXML
    private Label invalidLabel;
    @FXML
    private Label flightNumLabel;
    @FXML
    private Label sourceLabel;
    @FXML
    private Label destinationLabel;
    @FXML
    private Label departLabel;
    @FXML
    private Label durationLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label flightInfoLabel;
    @FXML
    private Label sourceInfoLabel;
    @FXML
    private Label destinationInfoLabel;
    @FXML
    private Label departInfoLabel;
    @FXML
    private Label durationInfoLabel;
    @FXML
    private Label statusInfoLabel;




    public void searchFlight(ActionEvent event) throws IOException
    {
        String temp = flightField.getText();
        int flightNum = 0;
        Flight flight = null;

        FlightDatabase db = FlightDatabase.getInstance();
        flightNum = KeyManager.getFlightNumber(temp);

        if(flightNum == -1)
        {
            invalidLabel.setText("The flight number does not exist");
            invalidLabel.setVisible(true);
        }
        else {
            flight = db.getFlight(flightNum);
        }
        if(flight == null)
        {
            invalidLabel.setText("The flight number does not exist");
            invalidLabel.setVisible(true);
        }
        else
        {
            submitButton.setVisible(false);
            flightField.setVisible(false);
            invalidLabel.setVisible(false);

            flightNumLabel.setVisible(true);
            sourceLabel.setVisible(true);
            destinationLabel.setVisible(true);
            departLabel.setVisible(true);
            durationLabel.setVisible(true);
            statusLabel.setVisible(true);

            flightInfoLabel.setText(KeyManager.convertFlightNum(flightNum));
            flightInfoLabel.setVisible(true);

            sourceInfoLabel.setText(flight.getSource());
            sourceInfoLabel.setVisible(true);

            destinationInfoLabel.setText(flight.getDestination());
            destinationInfoLabel.setVisible(true);

            departInfoLabel.setText(DateConverter.getInstance().localDateToString(flight.getDepartUnix()));
            departInfoLabel.setVisible(true);

            durationInfoLabel.setText(String.valueOf(DateConverter.getInstance().minutesToHourMinutes(flight.getDuration())));
            durationInfoLabel.setVisible(true);

            statusInfoLabel.setText(flight.getStatus());
            statusInfoLabel.setVisible(true);


        }

    }
}
