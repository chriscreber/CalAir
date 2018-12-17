package Controller;

import Model.*;

import Model.FlightDatabase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.collections.ObservableList;

import java.io.IOException;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.time.LocalDate;

public class ScheduleFlightController extends ComponentController
{

    @FXML
    private Label businessRulesTitle;

    @FXML
    private Label ruleOne;

    @FXML
    private Label ruleTwo;

    @FXML
    private Label ruleThree;

    @FXML
    private Button businessRulesButton;

    @FXML
    private Label locationInstructions;

    @FXML
    private ChoiceBox<String> destinationChoices;

    @FXML
    private Button locationButton;

    @FXML
    private Label durationInstructions;

    @FXML
    private TextField durationField;

    @FXML
    private DatePicker dateField;

    @FXML
    private Button submitDurationButton;

    @FXML
    private Label selectTimeInstructions;

    @FXML
    private ChoiceBox<String> suggestionChoices;

    @FXML
    private Button selectTime;

    @FXML
    private Label returnInstructions;

    @FXML
    private ChoiceBox<String> returnSuggestions;

    @FXML
    private Button selectReturnTakeoff;

    @FXML
    private Label priceInstructions;

    @FXML
    private TextField priceField;

    @FXML Button scheduleFlightButton;

    @FXML Label scheduleConfirmation;

    private Flight scheduled = new Flight();
    private Flight departFlight = new Flight();
    private Flight returnFlight = new Flight();
    private String notHome;

    private String durationFromUnixTime(int unixTime) {
        int hours = unixTime / 3600;
        int minutes = (unixTime % 3600) / 60;
        return String.format("%d:%02d", hours, minutes);
    }

    private int unixTimeFromString(String duration) {
        String[] hoursAndMinutes = duration.split(":");
        int hours;
        int minutes;

        if (hoursAndMinutes.length > 2) {
            durationInstructions.setText("Duration format: hh:mm");
            return -1;
        }

        try {
            hours = Integer.parseInt(hoursAndMinutes[0]);
            minutes = Integer.parseInt(hoursAndMinutes[1]);
        } catch (NumberFormatException e) {
            durationInstructions.setText("Duration format: hh:mm");
            return -1;
        }

        return hours * 3600 + minutes * 60;
    }

    public void acceptBusinessRules() {
        businessRulesTitle.setVisible(false);
        ruleOne.setVisible(false);
        ruleTwo.setVisible(false);
        ruleThree.setVisible(false);
        businessRulesButton.setVisible(false);

        locationInstructions.setVisible(true);
        destinationChoices.setVisible(true);
        locationButton.setVisible(true);
    }

    public void submitDestination() {
        String home = "San Luis Obispo";
        String destination = destinationChoices.getValue();

        if (destination == null) {
            locationInstructions.setText("A destination must be entered");
            return;
        }

        departFlight.setSource(home);
        departFlight.setDestination(destination);

        returnFlight.setSource(destination);
        returnFlight.setDestination(home);

        locationInstructions.setText("Destination successfully set");

        durationField.setText(durationFromUnixTime(
                LocationDatabase.getInstance().getMeanDuration(destination)
        ));

        durationInstructions.setVisible(true);
        durationField.setVisible(true);
        dateField.setVisible(true);
        submitDurationButton.setVisible(true);
    }

    public void getDepartSuggestions() {
        Scheduler scheduler = Scheduler.getInstance();
        DateConverter dateConverter = DateConverter.getInstance();
        String durationText = durationField.getText();
        LocalDate departDate = dateField.getValue();

        if (durationText == null) {
            durationInstructions.setText("Duration not provided");
            return;
        }

        departFlight.setDuration(unixTimeFromString(durationText));
        returnFlight.setDuration(unixTimeFromString(durationText));

        if (departDate == null) {
            durationInstructions.setText("Depart date not provided");
            return;
        }

        departFlight.setDepart(dateConverter.dateToTimestamp(departDate));

        durationInstructions.setText("Depart takeoff date and duration successfully set");

        ArrayList<String> suggestions = scheduler.getValidDepartTimes(departFlight);

        suggestionChoices.setItems(FXCollections.observableArrayList(
                scheduler.getValidDepartTimes(departFlight)
        ));

        selectTimeInstructions.setVisible(true);
        suggestionChoices.setVisible(true);
        selectTime.setVisible(true);
    }

    public void getReturnSuggestions() {
        Scheduler scheduler = Scheduler.getInstance();
        DateConverter dateConverter = DateConverter.getInstance();

        selectTimeInstructions.setText("Depart takeoff time successfully set");

        departFlight.setDepart(
                dateConverter.dateTimeTotimestamp(
                        LocalDateTime.parse(suggestionChoices.getValue())
                )
        );

        returnSuggestions.setItems(
                FXCollections.observableArrayList(
                        scheduler.getValidReturnTimes(departFlight)
                )
        );

        returnInstructions.setVisible(true);
        returnSuggestions.setVisible(true);
        selectReturnTakeoff.setVisible(true);
    }

    public void selectTime() {
        String selection = suggestionChoices.getValue();
        long departUnix;

        if (selection == null) {
            selectTimeInstructions.setText("No departure date selected");
            return;
        }

        departUnix = DateConverter.getInstance().dateTimeTotimestamp(
                LocalDateTime.parse(selection)
        );
        scheduled.setDepart(departUnix);

        priceField.setText("" + FlightDatabase.getInstance().getPriceRecommendation(notHome));
    }

    public void getSuggestedPrice() {
        FlightDatabase flightDatabase = FlightDatabase.getInstance();
        DateConverter dateConverter = DateConverter.getInstance();

        returnInstructions.setText("Return flight takeoff time successfully set");

        returnFlight.setDepart(
                dateConverter.dateTimeTotimestamp(
                        LocalDateTime.parse(returnSuggestions.getValue())
                )
        );

        priceField.setText(
                "" + flightDatabase.getPriceRecommendation(
                        departFlight.getDestination()
                )
        );

        priceInstructions.setVisible(true);
        priceField.setVisible(true);
        scheduleFlightButton.setVisible(true);
    }

    public void scheduleFlight() {
        FlightDatabase flightDatabase = FlightDatabase.getInstance();
        Float price = Float.parseFloat(priceField.getText());

        if (price <= 0) {
            priceInstructions.setText("Invalid price");
            return;
        }

        departFlight.setPrice(price);
        returnFlight.setPrice(price);

        flightDatabase.insertFlight(departFlight);
        flightDatabase.insertFlight(returnFlight);

        priceInstructions.setText("Price successfully set");

        locationInstructions.setVisible(false);
        destinationChoices.setVisible(false);
        locationButton.setVisible(false);

        durationInstructions.setVisible(false);
        durationField.setVisible(false);
        dateField.setVisible(false);
        submitDurationButton.setVisible(false);

        selectTimeInstructions.setVisible(false);
        suggestionChoices.setVisible(false);
        selectTime.setVisible(false);

        returnInstructions.setVisible(false);
        returnSuggestions.setVisible(false);
        selectReturnTakeoff.setVisible(false);

        priceInstructions.setVisible(false);
        priceField.setVisible(false);
        scheduleFlightButton.setVisible(false);

        scheduleConfirmation.setVisible(true);
    }

    public void initialize() {
        destinationChoices.setItems(
                FXCollections.observableArrayList(
                        LocationDatabase.getInstance().getOtherLocations()
                )
        );

        locationInstructions.setVisible(false);
        destinationChoices.setVisible(false);
        locationButton.setVisible(false);

        durationInstructions.setVisible(false);
        durationField.setVisible(false);
        dateField.setVisible(false);
        submitDurationButton.setVisible(false);

        selectTimeInstructions.setVisible(false);
        suggestionChoices.setVisible(false);
        selectTime.setVisible(false);

        returnInstructions.setVisible(false);
        returnSuggestions.setVisible(false);
        selectReturnTakeoff.setVisible(false);

        priceInstructions.setVisible(false);
        priceField.setVisible(false);
        scheduleFlightButton.setVisible(false);

        scheduleConfirmation.setVisible(false);
    }
}
