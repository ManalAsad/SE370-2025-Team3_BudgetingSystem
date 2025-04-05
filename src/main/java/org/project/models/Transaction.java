package org.project.models;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final IntegerProperty transactionId = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final DoubleProperty amount =new SimpleDoubleProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final StringProperty source = new SimpleStringProperty();
    
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

        //more property stuff
        //constructor
    public Transaction(LocalDate date, double amount, String category) {
        this(-1,-1, LocalDate.now(),0.0, "", "", "");
    }


    public Transaction(int transactionId, int userId, LocalDate date,
                      double amount, String category, String description, String source) {
        setTransactionId(transactionId);
        setUserId(userId);
        setDate(date);
        setAmount(amount);
        setCategory(category);
        setDescription(description);
        setSource(source);
    }


    //properties of category stuff
    public IntegerProperty transactionIdProperty() { return transactionId; }
    public IntegerProperty userIdProperty() { return userId; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty categoryProperty() { return category; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty sourceProperty() { return source; }


    //get raw values ; FOR CALCULATIONS
    public int getTransactionId() { return transactionId.get(); }
    public int getUserId() { return userId.get(); }
    public LocalDate getDate() { return date.get(); }
    public double getAmount() { return amount.get(); }
    public String getCategory() { return category.get(); }
    public String getDescription() { return description.get(); }
    public String getSource() { return source.get(); }



    //setters
    public void setTransactionId(int id) { transactionId.set(id); }
    public void setUserId(int id) { userId.set(id); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public void setAmount(double amount) { this.amount.set(amount); }
    public void setCategory(String category) { this.category.set(category); }
    public void setDescription(String description) { this.description.set(description); }
    public void setSource(String source) { this.source.set(source); }


    public static Transaction submitManualTransaction(  //manual transaction data entry
            LocalDate date,
            String amountTyped,
            String selectedType,
            String manualInput,
            String description,
            String source
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

        return new Transaction(date, amount, category)
                .withDescription(description)
                .withSource(source);
    }


    //builder-style methods

    public Transaction withDescription(String description) {
        setDescription(description);
        return this;
    }

    public Transaction withSource(String source) {
        setSource(source);
        return this;
    }

    public Transaction withUserId(int userId) {
        setUserId(userId);
        return this;
    }

    public String getFormattedDate() {      //formats dates in table
        return date.get().format(DATE_FORMATTER);
    }

    public String getFormattedAmount() {        //formats amount in table
        return String.format("$%.2f", amount.get());
    }

    @Override
    public String toString(){
        return String.format("Transaction[id=%d, user=%d, date=%s, amount=%.2f, category=%s]",
        getTransactionId(), getUserId(), getDate(), getAmount(), getCategory());
    }

}
