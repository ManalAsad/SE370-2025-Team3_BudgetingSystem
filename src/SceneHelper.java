package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

/*
    This class provides a static function to streamline
    the process of switching a scene for a stage
*/
public class SceneHelper
{
    public static void switchScenes(Class caller, ActionEvent event, String filePath,
                                    double width, double height, String title) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(caller.getResource(filePath));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
