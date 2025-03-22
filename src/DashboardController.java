package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable
{
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

    }

    @FXML
    protected void upload(ActionEvent event)
    {
        FileChooser fileChooser = new FileChooser();
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // the .csv can be changed to .txt if you want to test a different file format
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma Delimited (*.csv)", "*.csv"));

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        // read the contents of the file
        if (selectedFile != null) {
            // print the path of the file selected
            System.out.println(selectedFile.getPath());
        }
    }

    @FXML
    private void logout(ActionEvent event) throws Exception
    {
        SceneHelper.switchScenes(getClass(), event, "/fxml/login.fxml",
                                 600, 500, "Budget Buddy");
    }
}
