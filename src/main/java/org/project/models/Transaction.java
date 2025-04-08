package org.project.models;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final ObjectProperty<LocalDate> date;
    private final DoubleProperty amount;
    private final StringProperty transactType;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

        //more property stuff
    public Transaction(LocalDate date, double amount, String transactType) {
        this.date = new SimpleObjectProperty<>(date);
        this.amount = new SimpleDoubleProperty(amount);
        this.transactType = new SimpleStringProperty(transactType);
    }

    //properties of category stuff
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }
    public StringProperty transactTypeProperty() {
        return transactType;
    }
    public DoubleProperty amountProperty() {
        return amount;
    }

    //get raw values ; FOR CALCULATIONS i think
    public LocalDate getDate() {
        return date.get();
    }
    public double getAmount() {
        return amount.get();
    }
    public String getTransactType() {
        return transactType.get();
    }


    public static Transaction submitManualTransaction(  //manual transaction data entry
            LocalDate date,
            String amountTyped,
            String selectedType,
            String manualInput
    ) throws IllegalArgumentException {
        if (date == null) {
            throw new IllegalArgumentException("Please select a date!");
        }

        if (amountTyped == null || amountTyped.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter an amount!");
        }

        if(selectedType == null) {
            throw new IllegalArgumentException("Please select a transaction type!");
        }

        if ("Other".equals(selectedType) && (manualInput == null || manualInput.trim().isEmpty())) {   //when other is chosen & field is empty
            throw new IllegalArgumentException("Please enter a transaction type!!");
        }

        double amount;
        try {
            amount = Double.parseDouble(amountTyped);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be a positive number!");
            }
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid number for the amount!");
        }

        String category = "Other".equals(selectedType) //if 'other' is the selected type
                ? manualInput.trim()    //take the manual input in this case
                : selectedType;     //otherwise use selected type

        return new Transaction(date, amount, category);
    }

    public String getFormattedDate() {      //formats dates in table
        return date.get().format(DATE_FORMATTER);
    }

    public String getFormattedAmount() {        //formats amount in table
        return String.format("$%.2f", amount.get());
    }
}
