package org.project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

public class DashboardController {

    @FXML private VBox goalForm;
    @FXML private ComboBox<String> categoryPicker;
    @FXML private TextField limitField;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private Button setGoalButton;
    @FXML private Label errorMessage; //temp before implementing alerts

    private boolean goalIsSet = false;

    @FXML
    public void initialize() {
        categoryPicker.getItems().addAll(
                "All",
                "Restaurants & Dining",
                "Transportation",
                "Health",
                "Shopping & Entertainment",
                "Insurance",
                "Groceries"
        );

        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }

    @FXML
    private void openGoalForm() {
        boolean opened = goalForm.isVisible();
        goalForm.setVisible(!opened);
        goalForm.setManaged(!opened);
    }

    @FXML
    private void handleCreateGoal() {
        try {
            String category = categoryPicker.getValue();
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();

            //input validation. currently just printing the text bc I want to move displayAlerts in the transaction
            //..controller to an AlertsUtil for reusability
            errorMessage.setText("");   //temp

            if (category == null || category.isEmpty()) {
                errorMessage.setText("Please select a category!");
                return;
            }
            if (start == null || end == null) {
                errorMessage.setText("Please select both dates!");
                return;
            }
            if (start.isAfter(end)) {
                errorMessage.setText("Invalid start date!");
                return;
            }

            //when goal is saved, change button to edit goals, add logic for saving to database later
            goalIsSet = true;
            setGoalButton.setText("Edit Goal");
            goalForm.setVisible(false);
            goalForm.setManaged(false);

        } catch (NumberFormatException e) {
            errorMessage.setText("Please enter a valid number for limit.");
        }
    }
}

