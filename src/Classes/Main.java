package Classes;

import Controllers.Arrival;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxmls/Arrival.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 400, 300));
        Arrival controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("ArriDep");
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
