package org.project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.project.util.SceneHelper;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML public BorderPane rootPane;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleLogout(ActionEvent event) {  //upon clicking a button, logout (just closes stage and reopens login window)
        try {
            stage.setFullScreen(false);
            stage.close();
            Stage newStage = new Stage();
            SceneHelper.openLoginWindow(newStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbout() {    //the about section, currently just holds credits
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            VBox aboutBox = loader.load();
            //create an 'about' dialog box
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("About");
            dialog.getDialogPane().setContent(aboutBox);

            //create close button, make invisible to tie in with standard X
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);
            dialog.initOwner((Stage) rootPane.getScene().getWindow());

            dialog.show();
        }
        catch (Exception e) {
            showAlert("Failed to load About window!!!");
        }
    }

    private void showAlert(String message) {    //display an alert message
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        //alert must show in current stage
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        alert.initOwner(currentStage);
        alert.setOnShowing(e -> { //ensure that alert doesn't close the fullscreen when displaying
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setFullScreen(false);
        });

        alert.showAndWait();
    }
}