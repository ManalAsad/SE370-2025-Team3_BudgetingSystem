package org.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;

public class ReportsController {

    @FXML private ComboBox<String> timePeriodCombo;
    @FXML private StackPane reportContainer;

    @FXML
    private void initialize() {
        //use later
    }

    @FXML
    private void handleGenerateReport() {
        String selectedPeriod = timePeriodCombo.getValue();
        if (selectedPeriod != null) {
            //this will be used later
        }
    }
}