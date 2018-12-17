package Driver;

// JavaFX packages
import Controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// our packages
import Model.Database;

public class Driver extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Driver.class.getResource("../View/HomePage.fxml"));
        AnchorPane root = loader.load();
        Scene home = new Scene(root, 450, 500);
        LoginController controller = loader.getController();

        controller.setStage(primaryStage);

        primaryStage.setScene(home);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Database db = Database.getInstance();

        launch(args);

        db.disconnect();
    }
}