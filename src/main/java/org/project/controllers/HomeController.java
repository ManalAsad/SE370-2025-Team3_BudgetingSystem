package org.project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.project.util.SceneHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML public BorderPane rootPane;
    @FXML private VBox sidebar;
    @FXML private VBox sidebarContent;
    @FXML private StackPane contentPane;
    @FXML private Button dashboardNavBtn;
    @FXML private Button transactionsNavBtn;
    @FXML private Button reportsNavBtn;
    @FXML private VBox notificationSidebar;

    private boolean sidebarExpanded = false;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //init ubtton styles
        dashboardNavBtn.getStyleClass().add("nav-button");
        transactionsNavBtn.getStyleClass().add("nav-button");
        reportsNavBtn.getStyleClass().add("nav-button");

        setActiveButton(dashboardNavBtn);
        loadNotificationPanel();
    }
    private void loadNotificationPanel() {  //load notifs, Might just leave in the dashboard though, it could get annoying
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/notifications.fxml"));
            VBox notificationPanel = loader.load();
            notificationSidebar.getChildren().add(notificationPanel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openNotificationSidebar(ActionEvent event) {   //self explanatory
        notificationSidebar.setVisible(!notificationSidebar.isVisible());
        notificationSidebar.setManaged(notificationSidebar.isVisible());
    }

    @FXML
    private void openSidebar(ActionEvent event) { //self explanatory
        sidebarExpanded = !sidebarExpanded;
        sidebarContent.setVisible(sidebarExpanded);
        sidebarContent.setManaged(sidebarExpanded);

        if (sidebarExpanded) {
            sidebar.setPrefWidth(200);
        } else {
            sidebar.setPrefWidth(50);
        }
    }

    @FXML
    private void showDashboard(ActionEvent event) {
        loadContent("/fxml/dashboard.fxml");
        setActiveButton(dashboardNavBtn);
        collapseSidebarIfExpanded();
    }

    @FXML
    private void showTransactions(ActionEvent event) {
        loadContent("/fxml/transactions.fxml");
        setActiveButton(transactionsNavBtn);
        collapseSidebarIfExpanded();
    }

    @FXML
    private void showReports(ActionEvent event) {
        loadContent("/fxml/reports.fxml");
        setActiveButton(reportsNavBtn);
        collapseSidebarIfExpanded();
    }

    private void loadContent(String fxmlPath) { //load contents of home
        try {
            contentPane.getChildren().clear();
            Pane newContent = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentPane.getChildren().add(newContent);

            if (fxmlPath.equals("/fxml/reports.fxml")) {    //allow scrolling for reports tab only
                ScrollPane scrollPane = new ScrollPane(newContent);
                scrollPane.setFitToWidth(true);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                contentPane.getChildren().add(scrollPane);
            }
        }
        catch (Exception e) {
            showAlert("Failed to load content: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button activeButton) {
        //essentially just removes class the button to reset
        dashboardNavBtn.getStyleClass().remove("active-nav-button");
        transactionsNavBtn.getStyleClass().remove("active-nav-button");
        reportsNavBtn.getStyleClass().remove("active-nav-button");

        //this is what styles the button
        activeButton.getStyleClass().add("active-nav-button");
    }

    private void collapseSidebarIfExpanded() {
        if (sidebarExpanded) {
            openSidebar(null);
        }
    }

    public void setStage(Stage stage) {     //min stage size in case u close fullscreen
        this.stage = stage;
        stage.setMinHeight(500);
        stage.setMinWidth(700);
    }

    @FXML
    private void handleLogout(ActionEvent event) {  //logs out ********Not sure if there is logic required to fully 'log out' a registered user *********
        try {
            stage.setFullScreen(false);
            stage.close();
            Stage newStage = new Stage();
            SceneHelper.displayStartScreen(newStage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbout() { //displays the 'about' window, no breaking full screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            VBox aboutBox = loader.load();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("About");

            dialog.getDialogPane().setContent(aboutBox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.getDialogPane().lookupButton(ButtonType.CLOSE).setVisible(false);
            dialog.initOwner((Stage) rootPane.getScene().getWindow());
            dialog.show();
        }
        catch (Exception e) {
            showAlert("Failed to load About window!!!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        alert.initOwner(currentStage);
        alert.setOnShowing(e -> {
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setFullScreen(false);
        });
        alert.showAndWait();
    }
}

