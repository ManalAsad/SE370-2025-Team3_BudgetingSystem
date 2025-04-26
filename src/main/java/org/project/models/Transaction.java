
package org.project.models;

//import java.math.BigDecimal;
import javafx.beans.property.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final IntegerProperty transactionId = new SimpleIntegerProperty();
    private final IntegerProperty userId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final StringProperty transactType = new SimpleStringProperty();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // Constructors
    public Transaction() {
       this(-1, -1, LocalDate.now(), 0.0, "");
    }

    public Transaction(int transactionId, int userId, LocalDate date, double amount, String transactType) {
        setTransactionId(transactionId);
        setUserId(userId);
        setDate(date);
        setAmount(amount);
        setTransactType(transactType);
    }

    // Property accessors
    public IntegerProperty transactionId() { return transactionId; }
    public IntegerProperty userIdProperty() { return userId; }
    public ObjectProperty<LocalDate> dateProperty() { return date; }
    public DoubleProperty amountProperty() { return amount; }
    public StringProperty transactTypeProperty() { return transactType; }

    // Getters
    public int getTransactionId(){ return transactionId.get(); }
    public int getUserId() { return userId.get(); }
    public LocalDate getDate() { return date.get(); }
    public double getAmount() { return amount.get(); }
    public String getTransactType() { return transactType.get(); }

    // Setters
    public void setTransactionId(int transactionId){
        this.transactionId.set(transactionId);
    }
    public void setUserId(int userId) { this.userId.set(userId); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public void setAmount(double amount) { this.amount.set(amount); }
    public void setTransactType(String transactType) { this.transactType.set(transactType); }

    public static Transaction submitManualTransaction(int userId, LocalDate date, String amountStr, String selectedType, String customType) {
        if (date == null || amountStr == null || amountStr.isEmpty()) {
            throw new IllegalArgumentException("Date and amount are required.");
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if(amount <= 0){
                throw new IllegalArgumentException("Amount must be positive.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Amount must be a valid number.");
        }

        String finalType = selectedType.equals("Other") ? customType : selectedType;
        if (finalType == null || finalType.isEmpty()) {
            throw new IllegalArgumentException("Transaction type is required.");
        }

        return new Transaction(-1, userId, date, amount, finalType);
    }

    public String getFormattedDate() {
        return getDate().format(DATE_FORMATTER);
    }

    public String getFormattedAmount() {
        return String.format("$%.2f",getAmount());
    }

    @Override
    public String toString() {
        return String.format("Transaction[id=%d, user=%d, type=%s, amount=%.2f, date=%s]",
                getTransactionId(), getUserId(), getTransactType(), getAmount(), getFormattedDate());
    }
}

