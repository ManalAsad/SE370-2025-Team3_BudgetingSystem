package org.project.models;

import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final IntegerProperty transactionId;
    private final IntegerProperty userId;
    private final DoubleProperty amount;
    private final StringProperty category;
    private final ObjectProperty<LocalDate> date;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

        //more property stuff
    public Transaction(int transaction_id, int userId, LocalDate date, double amount, String category) {
        this.date = new SimpleObjectProperty<>(date);
        this.amount = new SimpleDoubleProperty(amount);
        this.category = new SimpleStringProperty(category);
        this.transactionId = new SimpleIntegerProperty(transaction_id);
        this.userId = new SimpleIntegerProperty(userId);
    }

    //properties of category stuff
    public IntegerProperty transactionIdProperty() { 
        return transactionId; 
    }
    public IntegerProperty userIdProperty() { 
        return userId; 
    }
    public ObjectProperty<LocalDate> dateProperty() { 
        return date; 
    }
    public DoubleProperty amountProperty() { 
        return amount; 
    }
    public StringProperty CategoryTypeProperty() { 
        return category; 
    }

    //getters
    public String getCategory() {
        return category.get();
    }
    public int getUserId() { 
        return userId.get(); 
    }
    public int getTransactionId() { 
        return transactionId.get();
    }
    public LocalDate getDate() { 
        return date.get(); 
    }
    public double getAmount() { 
        return amount.get(); 
    }

    //setters
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public void setAmount(double amount) { 
        this.amount.set(amount); 
    }
    
    public void setCategory(String category) { 
        this.category.set(category); 
    }

    public void setTransactionId(int id) { 
        this.transactionId.set(id); 
    }
    public void setUserId(int id) { 
      this.userId.set(id); 
    }

    public static Transaction submitManualTransaction(                          //manual transaction data entry        
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

        return new Transaction(0, userId, date, amount, category);
    }

    public String getFormattedDate() {      //formats dates in table
        return date.get().format(DATE_FORMATTER);
    }

    public String getFormattedAmount() {        //formats amount in table
        return String.format("$%.2f", amount.get());
    }
}
