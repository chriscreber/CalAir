package Controller;

import Model.PasswordValidation;
import Model.User;
import Model.UserDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

public class CreateUserController extends ComponentController {

    private User user;

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private DatePicker birthDateField;
    @FXML
    private Label errorLabel;

    public void submitBack(ActionEvent event) throws IOException
    {
        openHomePageMenu();
    }

    public void openHomePageMenu() throws IOException {
        FXMLLoader loader = null;
        Stage stage = getStage();
        System.out.println("homePageStage: " + stage);


        loader = new FXMLLoader(getClass().getResource("../View/HomePage.fxml"));


        AnchorPane root = loader.load();

        LoginController loginController = loader.getController();
        System.out.println("loginController = " + loginController);
        loginController.setStage(stage);

        Scene loginScene = new Scene(root, 450, 500);
        stage.setScene(loginScene);
        stage.show();
    }

    public void submitSubmit(ActionEvent event) throws IOException
    {
        UserDatabase db = UserDatabase.getInstance();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        LocalDate birthDate = birthDateField.getValue();
        ZoneId zoneId;
        long birthDateUnix = 0;
        if(birthDate != null) {
            zoneId = ZoneId.systemDefault();
            birthDateUnix = birthDate.atStartOfDay(zoneId).toEpochSecond();
        }
        int role = 0;

        if(!PasswordValidation.isValidPassword(password))
        {
            errorLabel.setText("Password must contain multiple case letters, and have more than 5 characters");
            errorLabel.setVisible(true);
        }
        else if(!PasswordValidation.passwordMatch(passwordField.getText(),confirmPasswordField.getText()))
        {
            errorLabel.setText("Passwords do not match");
            errorLabel.setVisible(true);
        }
        else if (db.getUser(username) != null)
        {
            errorLabel.setVisible(true);
            errorLabel.setText("Username already exits please select another username");
        }
        else if (username.equals("")  || firstName.equals("")|| lastName.equals("") || email.equals("") || birthDateUnix == 0) {
            errorLabel.setText("Invalid Inputs");
            errorLabel.setVisible(true);
        }
        else {
            System.out.println("Inserting user...");
            db.insertUser(username, password, firstName, lastName, email, birthDateUnix, role);
            openCustomerHomeMenu();
        }


    }

    public void openCustomerHomeMenu() throws IOException {
        FXMLLoader loader = null;
        Stage stage = getStage();
        System.out.println("CustomerMenuStage: " + stage);


        loader = new FXMLLoader(getClass().getResource("../View/CustomerHome.fxml"));


        AnchorPane root = loader.load();

        CustomerController customerController = loader.getController();
        System.out.println("customerController = " + customerController);
        customerController.setStage(stage);

        Scene customerScene = new Scene(root, 750, 500);
        stage.setScene(customerScene);
        stage.show();
    }


}
