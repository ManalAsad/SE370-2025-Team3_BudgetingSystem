package org.project.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import org.project.models.Transaction;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class TransactionsController implements Initializable {

    @FXML private ListView<Transaction> transactionsListView;
    @FXML private DatePicker dateField;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> transactTypeField;
    @FXML private TextField customtransactType;
    @FXML private Button deleteSelectedBtn;
    @FXML private Button processFileBtn;

    private final ObservableList<Transaction> transactionData = FXCollections.observableArrayList();
    private final ObservableList<String> categories = FXCollections.observableArrayList(    //dropdow menu list of categories
            "Restaurants & Dining",
            "Shopping",
            "Health",
            "Insurance",
            "Transportation",
            "Entertainment",
            "Bills",
            "Other"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactTypeField.setItems(categories);
        transactTypeField.getSelectionModel().selectFirst();

        //list for transactions
        transactionsListView.setItems(transactionData);
        transactionsListView.setCellFactory(param -> new TransactionList());
        transactionsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        transactionsListView.setPlaceholder(new Label("No transactions added yet :'("));

        //hide the 'other' custom type
        customtransactType.setVisible(false);
        customtransactType.setManaged(false);

        //selection updates the delete button
        transactionsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            deleteSelectedBtn.setDisable(transactionsListView.getSelectionModel().getSelectedItems().isEmpty());
        });

        //display the custom field when 'other' is seleceted
        transactTypeField.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean displayCustomType = "Other".equals(newVal);
            customtransactType.setVisible(displayCustomType);
            customtransactType.setManaged(displayCustomType);

            if (displayCustomType) {
                customtransactType.requestFocus();  //focus on box when selected
            } else {
                customtransactType.clear();
            }
        });

    }

    @FXML
    private void handleAddTransaction() {       //add transactions, currently only manual
        try {
            Transaction newTransaction = Transaction.submitManualTransaction(
                    dateField.getValue(),
                    amountField.getText(),
                    transactTypeField.getValue(),
                    customtransactType.getText()
            );

            transactionData.add(newTransaction);
            clearManualEntryFields();

        } catch (IllegalArgumentException e) {
            displayAlert("Invalid Input", e.getMessage());
        }
    }

    @FXML
    private void handleFileUpload() {       //FILECHOOSER moment :D!!
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Transaction File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File selectedFile = fileChooser.showOpenDialog(transactionsListView.getScene().getWindow());    //opens up file thingy
        if (selectedFile != null) {
            processFileBtn.setDisable(true);
        }
    }

    /*private List<Transaction> parseFile(File file) {
        // Parsing logic goes here... i think, or maybe services
    }*/

    @FXML
    private void handleDeleteSelected() {       //when click delete, either delete or no option selected
        ObservableList<Transaction> selectedTransactions =
                transactionsListView.getSelectionModel().getSelectedItems();

        if (selectedTransactions.isEmpty()) {
            displayAlert("No Selection", "Please select transactions to delete");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);   //confirmation for user deletion
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete " + selectedTransactions.size() + " transaction(s)?");
        confirmation.setContentText("This action cannot be undone.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                transactionData.removeAll(selectedTransactions);
            }
        });
    }

    @FXML
    private void handleSaveChanges() {
        if (!transactionData.isEmpty()) {
            displayAlert("Save Successful!", "Your transactions have been saved (not really)");
        } else {
            displayAlert("No Data!", "There is nothing to save >:(!!");
        }
    }

    @FXML
    private void handleClearFields() {
        clearManualEntryFields();
    }

    private void clearManualEntryFields() {
        dateField.setValue(null);
        amountField.clear();
        transactTypeField.getSelectionModel().selectFirst();
        customtransactType.clear();
    }

    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(transactionsListView.getScene().getWindow());
        alert.show();
    }

    //list for display item, STILL UPDATING DONT MOVE THIS TO ANOTHER CLASS YET,
    private static class TransactionList extends ListCell<Transaction> {
        @Override
        protected void updateItem(Transaction transaction, boolean empty) {
            super.updateItem(transaction, empty);

            if (empty || transaction == null) {
                setText(null);
                setGraphic(null);
            }
            else {  //set up list
                HBox container = new HBox(10);
                Label dateLabel = new Label(transaction.getFormattedDate());
                Label typeLabel = new Label(transaction.getCategory());
                Label amountLabel = new Label(transaction.getFormattedAmount());

                container.getChildren().addAll(dateLabel, amountLabel, typeLabel);
                setGraphic(container);
            }
        }
    }
}
