package Controller;

import Model.User;
import Model.UserDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class UpdateUserController extends ComponentController
{
    @FXML
    private TextField searchUsernameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private Label firstNameLabel;

    @FXML
    private CheckBox flightAttendentBox;

    @FXML
    private CheckBox customerBox;

    @FXML
    private CheckBox frontDeskBox;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label doesNotExist;

    @FXML
    private Label emailLabel;

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label invalidEntryLabel;

    @FXML
    private Label enterName;

    @FXML
    private Button searchButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private Button updateUserButton;

    @FXML
    private DatePicker birthDateField;

    User user = null;

    public void searchUser(ActionEvent event) throws IOException
    {
        UserDatabase db = UserDatabase.getInstance();
        String username = searchUsernameField.getText();
        long time = 0;
        user = db.getUser(username);

        if(user!= null)
        {
            invalidEntryLabel.setVisible(false);
            doesNotExist.setVisible(false);
            searchUsernameField.setVisible(false);
            enterName.setVisible(false);
            searchButton.setVisible(false);

            deleteUserButton.setVisible(true);
            updateUserButton.setVisible(true);

            firstNameField.setVisible(true);
            firstNameField.setText(user.getFirstName());

            usernameField.setVisible(true);
            usernameField.setText(user.getUsername());

            emailField.setVisible(true);
            emailField.setText(user.getEmail());


            LocalDate date = user.getBirthDate();
            birthDateField.setValue(date);
            birthDateField.setVisible(true);

            lastNameField.setVisible(true);
            lastNameField.setText(user.getLastName());

            flightAttendentBox.setVisible(true);
            customerBox.setVisible(true);
            frontDeskBox.setVisible(true);

            if(user.getRole() == 0)
            {
                customerBox.setSelected(true);
                flightAttendentBox.setSelected(false);
                frontDeskBox.setSelected(false);
            }
            else if(user.getRole() == 1)
            {
                flightAttendentBox.setSelected(false);
                frontDeskBox.setSelected(true);
                customerBox.setSelected(false);
            }
            else if(user.getRole() == 2)
            {
                frontDeskBox.setSelected(false);
                customerBox.setSelected(false);
                flightAttendentBox.setSelected(true);
            }

            firstNameLabel.setVisible(true);
            lastNameLabel.setVisible(true);
            emailLabel.setVisible(true);
            usernameLabel.setVisible(true);

        }
        else
            {
            doesNotExist.setVisible(true);
        }
    }

    public void updateUser(ActionEvent event) throws IOException{

        FXMLLoader loader = null;
        Stage stage = getStage();
        AnchorPane root = null;

        String username = "";
        String firstName ="";
        String lastName = "";
        String email= "";
        LocalDate birthDate = null;
        int role = -1;
        int checker = 0;

        UserDatabase db = UserDatabase.getInstance();

        username = usernameField.getText();
        firstName = firstNameField.getText();
        lastName = lastNameField.getText();
        email = emailField.getText();
        birthDate = birthDateField.getValue();


        if (frontDeskBox.isSelected() == true)
        {
            role = 1;
            checker = checker + 4;
        }

        if (flightAttendentBox.isSelected() ==  true) {
            role = 2;
            checker = checker +4;
        }

        if (customerBox.isSelected() == true) {
            role = 0;
            checker = checker + 4;
        }
        if(checker > 4)

        {
            invalidEntryLabel.setText("Cannot select multiple roles please select one");
            invalidEntryLabel.setVisible(true);
            return;
        }

        if (db.usernameExists(username) && !username.equals(user.getUsername())) {
            invalidEntryLabel.setVisible(true);
            invalidEntryLabel.setText("Username already exits please select another username");
            return;
        }

        else if (username.equals("")  || firstName.equals("")|| lastName.equals("") || email.equals("") || role == -1 || birthDate == null) {
            invalidEntryLabel.setText("Invalid Inputs");
            invalidEntryLabel.setVisible(true);
            return;
        }
        else
        {
            User temp = new User(username,firstName,lastName,email,birthDate,role);
            db.editUser(temp,user.getUsername());
            restorePage();
        }

        User temp = new User(username,firstName,lastName,email, birthDate, role);
        db.editUser(temp,user.getUsername());
        System.out.println(temp.getUsername());
        restorePage();
    }

    public void deleteUser(ActionEvent event) throws IOException
    {
        UserDatabase db = UserDatabase.getInstance();
        db.deleteUserEntry(user.getUsername());
        restorePage();
    }

    public void restorePage()
    {
        doesNotExist.setVisible(false);
        searchUsernameField.setVisible(true);
        searchUsernameField.setPromptText("Username");
        enterName.setVisible(true);
        searchButton.setVisible(true);
        deleteUserButton.setVisible(false);
        updateUserButton.setVisible(false);
        firstNameField.setVisible(false);
        usernameField.setVisible(false);
        emailField.setVisible(false);
        birthDateField.setVisible(false);
        lastNameField.setVisible(false);
        frontDeskBox.setVisible(false);
        customerBox.setVisible(false);
        flightAttendentBox.setVisible(false);
        firstNameLabel.setVisible(false);
        lastNameLabel.setVisible(false);
        emailLabel.setVisible(false);
        usernameLabel.setVisible(false);
        birthDateField.setVisible(false);
        invalidEntryLabel.setVisible(false);

    }

}


