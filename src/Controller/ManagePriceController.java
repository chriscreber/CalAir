package Controller;

import Model.LocationDatabase;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagePriceController extends ComponentController implements Initializable {

    @FXML
    private MenuButton locationMenu;

    @FXML
    private TextField priceField;

    @FXML
    private Label responseLabel;

    private String location;

    public void submitPrice(ActionEvent event) throws IOException {
        float price = Float.valueOf(priceField.getText());
        LocationDatabase.getInstance().updatePrice(location, price);
        responseLabel.setText("Successfully Set New Base Price");


    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<MenuItem> locationMenuObList = locationMenu.getItems();

        for(MenuItem m: locationMenuObList) {
            m.setOnAction(event -> {
                System.out.println(m.getText());
                location = m.getText();
                locationMenu.setText(location);
                responseLabel.setText("");
            });
        }
    }
}
