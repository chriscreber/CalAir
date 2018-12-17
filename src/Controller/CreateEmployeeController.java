package Controller;

import Model.PasswordValidation;
import Model.UserDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

public class CreateEmployeeController extends ComponentController
{
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private TextField emailField;

    @FXML
    private DatePicker birthDateField;

    @FXML
    private CheckBox frontDeskCheck;

    @FXML
    private CheckBox flightAttendentCheck;

    @FXML
    private Label errorLabel;

    public void submitInfo(ActionEvent event) throws IOException
    {
        UserDatabase db = UserDatabase.getInstance();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        LocalDate birthDate = null;
        long birthDateUnix = 0;
        birthDate = birthDateField.getValue();
        ZoneId zoneId;
        int role = -1;
        int temp = 0;

        if(birthDate != null)
        {
            zoneId = ZoneId.systemDefault();
            birthDateUnix = birthDate.atStartOfDay(zoneId).toEpochSecond();
        }

        errorLabel.setVisible(false);

        if(!PasswordValidation.isValidPassword(password))
        {
            errorLabel.setText("Password must contain case letters, and have more than 5 characters");
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
        else {
            errorLabel.setVisible(false);


            if (frontDeskCheck.isSelected() == true)
            {
                role = 1;
                temp = temp + 4;
            }
            if (flightAttendentCheck.isSelected() == true)
            {
                role = 2;
                temp = temp + 4;
            }
            if(temp > 4)
            {
                errorLabel.setText("Cannot select both roles please select one");
                errorLabel.setVisible(true);
            }
            else if (username.equals("")  || firstName.equals("")|| lastName.equals("") || email.equals("")
                     || role == -1 || birthDateUnix == 0) {
                errorLabel.setText("Invalid Inputs");
                errorLabel.setVisible(true);
            }
            else {
                db.insertUser(username, password, firstName, lastName, email, birthDateUnix, role);
                    errorLabel.setVisible(false);
                    errorLabel.setVisible(false);
                    usernameField.setText(null);
                    firstNameField.setText(null);
                    lastNameField.setText(null);
                    emailField.setText(null);
                    passwordField.setText(null);
                    confirmPasswordField.setText(null);
                    birthDateField.setValue(null);
                    frontDeskCheck.setSelected(false);
                    flightAttendentCheck.setSelected(false);
                }
        }

    }
}
