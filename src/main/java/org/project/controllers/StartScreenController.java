package org.project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.util.SceneHelper;


import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {
    @FXML private VBox startButtons;    //sign in and sign up buttons
    @FXML private VBox loginPage;
    @FXML private VBox signUpPage;
    @FXML private Button regretButton;  //go back to start screen
    @FXML private AnchorPane rootPane;

    //fields for loggging in
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;

    //fields for signing up
    @FXML private TextField newUsername;
    @FXML private PasswordField newPassword;
    @FXML private PasswordField confirmNewPassword; //make sure they are equal before signing in
    @FXML private Button signUpButton;

    @FXML private Button displayLoginPage;
    @FXML private Button displaySignUpPage;

    private Stage stage;
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //display the login page, do not allow any other page to show
        displayLoginPage.setOnAction(e -> {
            loginPage.setVisible(true);
            signUpPage.setVisible(false);
            startButtons.setVisible(false);
        });

        //display sign up page, do the same thing
        displaySignUpPage.setOnAction(e -> {
            signUpPage.setVisible(true);
            loginPage.setVisible(false);
            startButtons.setVisible(false);
        });

        loginButton.setOnAction(e -> handleLogin(e));
        signUpButton.setOnAction(e -> handleSignUp(e));
        //user changes mind. Back button
        regretButton.setOnAction(e -> displayStartScreen());    //go back button

        //for moving around the window
        rootPane.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        rootPane.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public void displayStartScreen() {
        startButtons.setVisible(true);
        loginPage.setVisible(false);
        signUpPage.setVisible(false);
        regretButton.setVisible(false);

        clearFields(); //just put here for ease when clicking regret button
    }

    private void clearFields() {
        username.clear();
        password.clear();
        newUsername.clear();
        newPassword.clear();
        confirmNewPassword.clear();
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
            SceneHelper.switchScene(dashboardStage, "/fxml/home.fxml",
                    "BudgetBuddy Dashboard", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML   //just like the login handling, it doesnt do anything yet
    private void handleSignUp(ActionEvent event) {
        try {
            stage.close();
            Stage dashboardStage = new Stage();
            SceneHelper.switchScene(dashboardStage, "/fxml/home.fxml",
                    "BudgetBuddy Dashboard", true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        /*validate all fields r filled
        if (username.isEmpty() || password.isEmpty() {
        //either display an alert or just display text like other apps
            return;
        }
        //check if passwords match

        //check if username is available DB, idk if user authentication is happening in UserService
        if (userService.authenticate(username, password)) {
            stage.close();
            SceneHelper.switchScene(dashboardStage, "/fxml/home.fxml", "BudgetBuddy Dashboard", true);
        //create new account DB */
    }
}
