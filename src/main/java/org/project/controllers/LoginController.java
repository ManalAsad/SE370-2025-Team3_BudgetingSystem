package org.project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.util.SceneHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private AnchorPane rootPane;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;

    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        rootPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void closeProgram(ActionEvent event) {
        stage.close();
    }

    @FXML
    private void handleLogin(ActionEvent event) {   //not the full login logic yet, all this does is switch scenes when clicking 'log in'
        try {
            stage.close(); //close login then
            //allow the switch from login window to dashboard
            Stage dashboardStage = new Stage();
            SceneHelper.switchScene(dashboardStage, "/fxml/dashboard.fxml",
                    "BudgetBuddy Dashboard", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}