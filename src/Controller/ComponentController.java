package Controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public abstract class ComponentController {
    private FXMLLoader loader;
    private Stage stage;

    public void setLoader(FXMLLoader loader) { this.loader = loader; }
    public void setStage(Stage stage) { this.stage = stage; }

    public Stage getStage() { return this.stage; }
}
