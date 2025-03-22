package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable
{
    @FXML
    private AnchorPane rootPane; // root pane to move window around
    @FXML
    private TextField username; // textfield for username
    @FXML
    private PasswordField password; // password field
    @FXML
    private Button loginButton; // login button

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    @FXML
    protected void toDashboard(ActionEvent event) throws Exception
    {
        SceneHelper.switchScenes(getClass(), event, "/fxml/dashboard.fxml",
                                 600, 500, "Dashboard");
    }
}
