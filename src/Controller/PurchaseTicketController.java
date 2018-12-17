package Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;


public class PurchaseTicketController extends ComponentController
{
    @FXML
    private ListView flightView;
    @FXML
    private DatePicker selectDates;
    @FXML
    private Button submitButton;
    @FXML
    private Button purchaseDepartButton;
    @FXML
    private Button purchaseReturnButton;
    @FXML
    private Label titleLabel;
    @FXML
    private Label listLabel;
    @FXML
    private Label sourceDepartLabel;
    @FXML
    private Label sourceDepartInfoLabel;
    @FXML
    private Label destinationDepartLabel;
    @FXML
    private Label destinationDepartInfoLabel;
    @FXML
    private Label priceDepartLabel;
    @FXML
    private Label priceDepartInfoLabel;
    @FXML
    private Label departDepartDateLabel;
    @FXML
    private Label departDepartDateInfoLabel;
    @FXML
    private Label sourceReturnLabel;
    @FXML
    private Label sourceReturnInfoLabel;
    @FXML
    private Label destinationReturnLabel;
    @FXML
    private Label destinationReturnInfoLabel;
    @FXML
    private Label priceReturnLabel;
    @FXML
    private Label priceReturnInfoLabel;
    @FXML
    private Label departDateReturnLabel;
    @FXML
    private Label departDateReturnInfoLabel;
    @FXML
    private Label departLabel;
    @FXML
    private Label returnLabel;
    @FXML
    private Label thankYouLabel;
    @FXML
    private Label confirmationLabel;
    @FXML
    private Label numberLabel;
    @FXML
    private Button confirmButton;



    private ArrayList<Flight> fInfo;

    private Flight sourceFlight;
    private Flight returnFlight;

    private Operator me = Operator.getInstance();




    public void getDates(ActionEvent event) throws IOException
    {
        ArrayList<Flight> departureFlights = new ArrayList<Flight>();
        LocalDate birthDate = selectDates.getValue();
        ZoneId zoneId;
        long wantedDepartTime = 0;
        if(birthDate != null)
        {
            zoneId = ZoneId.systemDefault();
            wantedDepartTime = birthDate.atStartOfDay(zoneId).toEpochSecond();
        }
        fInfo = FlightDatabase.getInstance().getFlights();
        for(Flight f: fInfo)
        {
            if((f.getDepartUnix() > wantedDepartTime) && (f.getEmptySeats()>0) && (f.getDepartUnix() < (wantedDepartTime + 604800)))
            {
                departureFlights.add(f);
            }
        }
        flightView.setItems(FXCollections.observableArrayList(departureFlights));
        submitButton.setVisible(false);
        selectDates.setVisible(false);
        titleLabel.setVisible(false);
        flightView.setVisible(true);
        listLabel.setText("Select Depart Flight");
        purchaseDepartButton.setVisible(true);

    }

    public void purchaseDeparture(ActionEvent event) throws IOException
    {


        ArrayList<Flight> returnFlights = new ArrayList<>();
        ObservableList<Flight> ms = flightView.getSelectionModel().getSelectedItems();

        if(!ms.isEmpty()) {
            purchaseDepartButton.setVisible(false);
            sourceFlight = ms.get(0);

            for (Flight f : fInfo) {
                if ((f.getDepartUnix() > sourceFlight.getDepartUnix() + sourceFlight.getDuration()) && (f.getDestination().equals(sourceFlight.getSource())) && (f.getEmptySeats() > 0)) {
                    returnFlights.add(f);
                }
            }
            flightView.setItems(FXCollections.observableArrayList(returnFlights));
            flightView.setVisible(true);
            purchaseReturnButton.setVisible(true);
            listLabel.setText("Select Return Flight");
        }
    }

    public void purchaseReturn(ActionEvent event) throws IOException
    {

        ArrayList<Flight> returnFlights = new ArrayList<>();
        ObservableList<Flight> ms = flightView.getSelectionModel().getSelectedItems();

        if(!ms.isEmpty()) {

            purchaseReturnButton.setVisible(false);
            returnFlight = ms.get(0);
            flightView.setVisible(false);

            departLabel.setVisible(true);
            returnLabel.setVisible(true);

            sourceDepartLabel.setVisible(true);
            sourceDepartInfoLabel.setVisible(true);
            sourceDepartInfoLabel.setText(sourceFlight.getSource());

            destinationDepartLabel.setVisible(true);
            destinationDepartInfoLabel.setVisible(true);
            destinationDepartInfoLabel.setText(sourceFlight.getDestination());

            priceDepartLabel.setVisible(true);
            priceDepartInfoLabel.setVisible(true);
            priceDepartInfoLabel.setText(String.valueOf(sourceFlight.getPrice()));


            departDepartDateLabel.setVisible(true);
            departDepartDateInfoLabel.setVisible(true);
            departDepartDateInfoLabel.setText(DateConverter.getInstance().localDateToString(sourceFlight.getDepartUnix()));

            sourceReturnLabel.setVisible(true);
            sourceReturnInfoLabel.setVisible(true);
            sourceReturnInfoLabel.setText(returnFlight.getSource());

            destinationReturnLabel.setVisible(true);
            destinationReturnInfoLabel.setVisible(true);
            destinationReturnInfoLabel.setText(returnFlight.getDestination());

            priceReturnLabel.setVisible(true);
            priceReturnInfoLabel.setVisible(true);
            priceReturnInfoLabel.setText(String.valueOf(returnFlight.getPrice()));

            departDateReturnLabel.setVisible(true);
            departDateReturnInfoLabel.setVisible(true);
            departDateReturnInfoLabel.setText(DateConverter.getInstance().localDateToString(returnFlight.getDepartUnix()));

            confirmButton.setVisible(true);
            listLabel.setText("");
        }

    }
    public void confirmPurchase(ActionEvent event) throws IOException
    {
        FlightDatabase flightDatabase = FlightDatabase.getInstance();
        flightDatabase.editSeats(sourceFlight.getFlightNum(),sourceFlight.getEmptySeats());
        flightDatabase.editSeats(returnFlight.getFlightNum(),returnFlight.getEmptySeats());

        ReservationDatabase db = ReservationDatabase.getInstance();

        String confirmationNumber = "";
        int last = 0;

        departLabel.setVisible(false);
        returnLabel.setVisible(false);

        sourceDepartLabel.setVisible(false);
        sourceDepartInfoLabel.setVisible(false);

        destinationDepartLabel.setVisible(false);
        destinationDepartInfoLabel.setVisible(false);

        priceDepartLabel.setVisible(false);
        priceDepartInfoLabel.setVisible(false);

        departDepartDateLabel.setVisible(false);
        departDepartDateInfoLabel.setVisible(false);

        sourceReturnLabel.setVisible(false);
        sourceReturnInfoLabel.setVisible(false);

        destinationReturnLabel.setVisible(false);
        destinationReturnInfoLabel.setVisible(false);

        priceReturnLabel.setVisible(false);
        priceReturnInfoLabel.setVisible(false);

        departDateReturnLabel.setVisible(false);
        departDateReturnInfoLabel.setVisible(false);

        confirmButton.setVisible(false);

        thankYouLabel.setVisible(true);
        confirmationLabel.setVisible(true);
        numberLabel.setVisible(true);

        last = db.lastCheckinNumber();
        System.out.println(last);

        db.insertReservation(me.getUser(),sourceFlight,returnFlight,last);
        confirmationNumber = KeyManager.generateConfirmationNumber(last);

        numberLabel.setText(confirmationNumber);


    }

}
