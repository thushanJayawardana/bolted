package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppInitializer extends Application { //inheritance
    public static void main(String[] args) {
        launch(args);
    }
    @Override//polymorphism
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(FXMLLoader.load(this.getClass().getResource("/view/login_form.fxml"))));
        stage.centerOnScreen();
        stage.setTitle("Login Page");
        stage.show();
    }
}
