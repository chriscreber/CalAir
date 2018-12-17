package Controller;

import Model.Flight;
import Model.FlightDatabase;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;

import java.io.IOException;

public class ManagerReportsController {

    @FXML
    private ListView<String> soldOutList;

    @FXML
    private ListView<String> delayedList;

    public void generateRepAction(ActionEvent event) throws IOException {

        ArrayList<Flight> soldOutFlights;
        ArrayList<Flight> delayedFlights;
        ArrayList<String> soldOutStrings = new ArrayList<String>();
        ArrayList<String> delayedStrings = new ArrayList<String>();

        FlightDatabase flightDatabase = FlightDatabase.getInstance();

        soldOutFlights = flightDatabase.getSoldOutFlights();
        delayedFlights = flightDatabase.getDelayedFlights();

        for (Flight f : soldOutFlights) {
            soldOutStrings.add(f.toString());
        }
        for(Flight f : delayedFlights){
            delayedStrings.add(f.toString());
        }

        soldOutList.setItems(FXCollections.observableArrayList(soldOutStrings));
        delayedList.setItems(FXCollections.observableArrayList(delayedStrings));

    }

}
