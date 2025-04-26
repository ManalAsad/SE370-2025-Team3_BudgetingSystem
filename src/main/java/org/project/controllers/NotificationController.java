package org.project.controllers;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class NotificationController {
    @FXML
    private VBox notificationContents;

    @FXML
    private void closePanel() { //hide the panel from view
        notificationContents.getScene().lookup("#notificationContents").setVisible(false);
    }
}