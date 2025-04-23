package org.project.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.project.controllers.HomeController;
import org.project.controllers.LoginController;
import java.io.IOException;

public class SceneHelper {
    public static void switchScene(Stage stage, String fxmlPath, String title, boolean fullscreen) throws IOException { //make a scene change
        try {
            FXMLLoader loader = new FXMLLoader(SceneHelper.class.getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (controller instanceof HomeController) {    //dashboard set
                ((HomeController) controller).setStage(stage);
            }

            //set the dashboard deets, opens as fullscreen
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.centerOnScreen();
            stage.setFullScreen(fullscreen);
            stage.show();
        }
        catch (IOException e) { //exception cathcing
            System.err.println("Error loading FXML: " + fxmlPath);
            throw e;
        }
    }

    public static void displayStartScreen(Stage stage) throws IOException { //moved login window opening logic to scene helper
        FXMLLoader loader = new FXMLLoader(SceneHelper.class.getResource("/fxml/login.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(root);  //set stage transparent, display scene
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.setTitle("Budget Buddy Login");

        stage.show();
    }
}
