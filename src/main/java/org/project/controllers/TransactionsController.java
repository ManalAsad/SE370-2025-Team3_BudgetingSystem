package org.project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.project.models.Transaction;
import javafx.stage.Modality;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> amountColumn;
    @FXML private TableColumn<Transaction, String> transactTypeColumn;

    @FXML private DatePicker dateField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> transactTypeField;
    @FXML private TextField customtransactType;

    @FXML private Label fileStatus; //to implement in file upload handling (error, success, no file, etc)


    private final ObservableList<Transaction> transactionData = FXCollections.observableArrayList();

    private final ObservableList<String> categories = FXCollections.observableArrayList( //contents of dropdown menu
            "Restaurants & Dining",
            "Shopping",
            "Health",
            "Insurance",
            "Transportation",
            "Entertainment",
            "Bills",
            "Other"  //drop down menu moment!!
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactTypeField.setItems(categories);     //create dropdown menu
        transactTypeField.getSelectionModel().selectFirst();

        //table rows
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("formattedAmount"));
        transactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactType"));

        transactionsTable.setItems(transactionData);
        transactionsTable.setPlaceholder(new Label(" Empty :( "));

        //two states of this custom text box, currently not visisble
        customtransactType.setVisible(false);
        customtransactType.setManaged(false);

        customtransactType.textProperty().addListener((obs, oldVal, newVal) -> { //
            if (!newVal.isEmpty()) {
                transactTypeField.getSelectionModel().select("Other");
            }
        });
    }

    @FXML
    private void handleCategorySelection(ActionEvent event) {   //when user picks 'other', a new text box appears
        String selected = transactTypeField.getSelectionModel().getSelectedItem();
        boolean displayCustomType = "Other".equals(selected);

        //display textbox
        customtransactType.setVisible(displayCustomType);
        customtransactType.setManaged(displayCustomType);

        if (displayCustomType) { //when user clicks on 'other', focus and prepare for type
            customtransactType.requestFocus();
        } else {    //otherwise go back
            customtransactType.clear();
        }
    }

    @FXML
    private void handleAddTransaction() {   //when adding a new transaction manually
        try {
            Transaction newTransaction = Transaction.submitManualTransaction(   //fields that are needed
                    dateField.getValue(),
                    amountField.getText(),
                    transactTypeField.getValue(),
                    customtransactType.getText()
            );


            transactionData.add(newTransaction);
            clearManualEntryFields();

        } catch (IllegalArgumentException e) {
            displayAlert("Invalid Input!!", e.getMessage());
        }
    }

    @FXML
    private void handleClearFields() { //literally just clears the fields
        clearManualEntryFields();
    }

    private void clearManualEntryFields() {  //this will also clear list from DB later?
        dateField.setValue(null);
        amountField.clear();
        transactTypeField.getSelectionModel().selectFirst();
        customtransactType.clear();
        customtransactType.setVisible(false);
        customtransactType.setManaged(false);
    }


    @FXML
    private void handleSaveChanges() {  //this will save changes to DB
        if (!transactionData.isEmpty()) {
            displayAlert("Save Ready", "The transaction(s) have been saved! (not actually)");
        } else {
            displayAlert("Empty Field", "There is nothing to save!");
        }
    }

    @FXML
    private void handleFileUpload() {   //no backend logic yet
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV file", "*.csv"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
    }

    @FXML
    private void handleCancel() {   //this cancels the save. Right now it just clears the table bc im not sure how this works in DB
        transactionData.clear();
        clearManualEntryFields();
    }
    @FXML
    private void handleDelete() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete this field?");

        //alert pops up, dashboard will still stay visible
        Stage alertStage = (Stage) confirmation.getDialogPane().getScene().getWindow();
        alertStage.initOwner(transactionsTable.getScene().getWindow());
        alertStage.initModality(Modality.WINDOW_MODAL);

        confirmation.getDialogPane().getStyleClass().add("alert-dialog");

        //when press ok, clear field (it actually doesn't do anything yet)
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                clearManualEntryFields();
            }
        });
    }

    private void displayAlert(String title, String message) {   //display alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Stage stage = (Stage) transactionsTable.getScene().getWindow(); //getscene
        alert.initOwner(stage);     //alert is child of main window
        alert.initModality(Modality.APPLICATION_MODAL); //locks application

        alert.setOnShowing(e -> {   //configure the alert
            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();    //get the alert stage
            alertStage.setFullScreen(false);    //does not appear in fullscreen bc it will mess with dashbaord
        });

        alert.show();
    }
}