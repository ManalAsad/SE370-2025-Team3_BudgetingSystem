package org.project;

import javafx.application.Application;
import javafx.stage.Stage;
import org.project.util.SceneHelper;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneHelper.openLoginWindow(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}