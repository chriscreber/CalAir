package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

public class EmployeeController extends ComponentController {
    @FXML
    private AnchorPane BasePane;
    private AnchorPane newPane;

    public void repAction(ActionEvent event) throws IOException
    {
        newPane = FXMLLoader.load(getClass().getResource("../View/ManagerReports.fxml"));
        BasePane.getChildren().setAll(newPane);

    }

    public void priceManAction(ActionEvent event) throws IOException
    {
        newPane = FXMLLoader.load(getClass().getResource("../View/ManagePrice.fxml"));
        BasePane.getChildren().setAll(newPane);

    }

    public void checkInAction(ActionEvent event) throws IOException
    {
        newPane = FXMLLoader.load(getClass().getResource("../View/CheckIn.fxml"));
        BasePane.getChildren().setAll(newPane);

    }
    public void schedFlightAction(ActionEvent event) throws IOException {
        newPane = FXMLLoader.load(getClass().getResource("../View/ScheduleFlight.fxml"));

        BasePane.getChildren().setAll(newPane);
    }

    public void purchaseTicAction(ActionEvent event) throws IOException {

        newPane = FXMLLoader.load(getClass().getResource("../View/PurchaseTicket.fxml"));
        BasePane.getChildren().setAll(newPane);

    }
    public void createEmpAction(ActionEvent event) throws IOException {
        newPane = FXMLLoader.load(getClass().getResource("../View/CreateEmployee.fxml"));

        BasePane.getChildren().setAll(newPane);

    }

    public void viewFlightAction(ActionEvent event) throws IOException
    {
        newPane = FXMLLoader.load(getClass().getResource("../View/ViewStatus.fxml"));
        BasePane.getChildren().setAll(newPane);
    }

    public void updateUserAction(ActionEvent event) throws IOException
    {
        newPane = FXMLLoader.load(getClass().getResource("../View/updateUser.fxml"));
        BasePane.getChildren().setAll(newPane);
    }

    public void updateFlightAction(ActionEvent event) throws IOException
    {
        newPane = FXMLLoader.load(getClass().getResource("../View/updateFlight.fxml"));
        BasePane.getChildren().setAll(newPane);
    }

    public void logoutAction(ActionEvent event) throws IOException
    {
        Stage stage = getStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/HomePage.fxml"));
        AnchorPane root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setStage(stage);
        Scene scene = new Scene(root, 450, 450);
        stage.setScene(scene);
        stage.show();
    }


}