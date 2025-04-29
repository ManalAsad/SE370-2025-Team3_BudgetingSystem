package org.project.controllers;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.project.util.RippleAnimation;
import org.project.util.SceneHelper;
import org.project.services.UserAuthenticationService;

import java.net.URL;
import java.util.ResourceBundle;

public class StartScreenController implements Initializable {
    @FXML private VBox startButtons;//sign in and sign up buttons
    @FXML private VBox loginPage;
    @FXML private VBox signUpPage;
    @FXML private Button regretButton;  //go back to start screen
    @FXML private AnchorPane rootPane;
    @FXML private Text welcome;

    //fields for logging in
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;

    //image + ripple animations
    @FXML private ImageView lilyIcon;
    @FXML private ImageView lotus;
    @FXML private Pane ripple1;
    @FXML private Pane ripple2;
    @FXML private Pane rippleCentered;

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
        ripple1.setVisible(true);
        rippleCentered.setVisible(true);
        displayLoginPage.setOnAction(e -> {
            loginPage.setVisible(true);
            signUpPage.setVisible(false);
            startButtons.setVisible(false);
            regretButton.setVisible(true);
            lilyIcon.setVisible(false);
            welcome.setVisible(false);
            ripple1.setVisible(false);
            ripple2.setVisible(false);
            rippleCentered.setVisible(false);
            lotus.setVisible(false);
        });

        //display sign up page, do the same thing
        displaySignUpPage.setOnAction(e -> {
            signUpPage.setVisible(true);
            loginPage.setVisible(false);
            startButtons.setVisible(false);
            regretButton.setVisible(true);
            lilyIcon.setVisible(false);
            welcome.setVisible(false);
            ripple1.setVisible(false);
            ripple2.setVisible(false);
            rippleCentered.setVisible(false);
            lotus.setVisible(false);
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
        RippleAnimation.create(100, 100, 35, 58, Duration.seconds(5), ripple1);

        //run later, ensures that this ripple animation is at the same pos as the lotus
        Platform.runLater(() -> {
            double centerX = lotus.getLayoutX() + lotus.getFitHeight() / 2;
            double centerY = lotus.getLayoutY() + lotus.getFitHeight() / 2;

            RippleAnimation.create(centerX, centerY, 25, 38, Duration.seconds(9), ripple2);
        });

        //load other content before running this task(ripple behind lilypad)
        Platform.runLater(() -> {
            double iconSize = lilyIcon.getFitHeight();
            double centerX = rippleCentered.getHeight() / 2;
            double centerY = rippleCentered.getWidth() / 2;

            double initRad = iconSize * 0.4;
            double finRad = iconSize * 0.56;

            RippleAnimation.create(centerX, centerY, initRad, finRad, Duration.seconds(10), rippleCentered);
        });

        //make lotus look like it's bobbing up and down
        TranslateTransition bobbingLotus = new TranslateTransition(Duration.seconds(3), lotus);
        bobbingLotus.setByY(2);
        bobbingLotus.setCycleCount(Animation.INDEFINITE);
        bobbingLotus.setAutoReverse(true);
        bobbingLotus.setInterpolator(Interpolator.EASE_BOTH);
        bobbingLotus.play();

        //rotate the lotus a little bit too :o
        RotateTransition spinningLotus = new RotateTransition(Duration.seconds(8), lotus);
        spinningLotus.setByAngle(6);
        spinningLotus.setCycleCount(Animation.INDEFINITE);
        spinningLotus.setAutoReverse(true);
        spinningLotus.setInterpolator(Interpolator.EASE_BOTH);
        spinningLotus.play();
    }

    public void displayStartScreen() {
        startButtons.setVisible(true);
        loginPage.setVisible(false);
        signUpPage.setVisible(false);
        regretButton.setVisible(false);
        lilyIcon.setVisible(true);
        welcome.setVisible(true);
        ripple1.setVisible(true);
        ripple2.setVisible(true);
        rippleCentered.setVisible(true);
        lotus.setVisible(true);

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
    private void handleLogin(ActionEvent event) {
        try {
            UserAuthenticationService authService = new UserAuthenticationService();

            if (authService.authenticate(username.getText(), password.getText())) {
                stage.close(); // close login
                Stage dashboardStage = new Stage(); // allow the switch from login window to dashboard
                SceneHelper.switchScene(dashboardStage, "/fxml/home.fxml",
                        "BudgetBuddy Dashboard", true);
            }
            else {
                // display an alert or text indicating
                // that login was unsuccessful
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            if (newPassword.getText().equals(confirmNewPassword.getText())) {
                UserAuthenticationService authService = new UserAuthenticationService();

                if (authService.createAccount(newUsername.getText(), newPassword.getText())) {
                    // account creation was successful, display dashboard
                    stage.close();
                    Stage dashboardStage = new Stage();
                    SceneHelper.switchScene(dashboardStage, "/fxml/home.fxml",
                            "BudgetBuddy Dashboard", true);
                }
                else {
                    // display an alert or text indicating
                    // that a new account could not be created
                }
            }
            else {
                // display an alert or text indicating
                // that passwords entered do not match
            }
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
