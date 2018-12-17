package Controller;

import Model.Operator;
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

public class LoginController extends ComponentController {
    private User user = null;
    private Operator me = Operator.getInstance();

    @FXML
    private Label invalidLogin;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    public void submitLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("username: " + username + "\tpassword: " + password);

        UserDatabase db = UserDatabase.getInstance();

        if ((this.user = db.login(username, password)) == null) {
            invalidLogin.setText("Invalid login credentials");
        } else {
            this.me.setUser(this.user);
            openUserMenu();
        }
    }

    public void openUserMenu() throws IOException
    {
        FXMLLoader loader = null;
        Stage stage = getStage();
        System.out.println("loginStage: " + stage);
        AnchorPane root = null;

        if (this.user.isAdmin())
        {
            loader = new FXMLLoader(getClass().getResource("../View/EmployeeHome.fxml"));
            root = loader.load();
            EmployeeController employeeController = loader.getController();
            System.out.println("employeeController = " + employeeController);
            employeeController.setStage(stage);

            Scene employeeScene = new Scene(root, 750, 500);
            stage.setScene(employeeScene);
            stage.show();
        }
        else if(!(this.user.isAdmin()))
        {
            loader = new FXMLLoader(getClass().getResource("../View/CustomerHome.fxml"));
            root = loader.load();
            CustomerController customerController = loader.getController();
            System.out.println("customerController = " + customerController);
            customerController.setStage(stage);

            Scene employeeScene = new Scene(root, 750, 500);
            stage.setScene(employeeScene);
            stage.show();
        }
        else {
            invalidLogin.setText("Username or password incorrect");
        }

    }



    public void submitRegister(ActionEvent event) throws IOException
    {
        openRegisterMenu();
    }

    public void openRegisterMenu() throws IOException
    {
        FXMLLoader loader = null;
        Stage stage = getStage();
        System.out.println("registerStage: " + stage);


        loader = new FXMLLoader(getClass().getResource("../View/CreateUser.fxml"));


        AnchorPane root = loader.load();

        CreateUserController createUserController = loader.getController();
        System.out.println("createUserController = " + createUserController);
        createUserController.setStage(stage);

        Scene createUserScene = new Scene(root, 750, 500);
        stage.setScene(createUserScene);
        stage.show();
    }

}
