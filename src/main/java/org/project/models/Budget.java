package org.project.models;

import javafx.beans.property.*;

//import java.math.BigDecimal;
import java.time.LocalDate;

public class Budget {
    private final IntegerProperty budgetId = new SimpleIntegerProperty();     //Primary key
    private final IntegerProperty userId = new SimpleIntegerProperty();        // foreign key : links to the user who owns this budget
    private final StringProperty category = new SimpleStringProperty();         // like "food", "rent", etc
    private final DoubleProperty amount = new SimpleDoubleProperty();           // the budget amount for this category
    private final ObjectProperty<LocalDate> startDate = new SimpleObjectProperty<>();   //start of budget period
    private final ObjectProperty<LocalDate> endDate = new SimpleObjectProperty<>();     //end of budget period

    // Constructors (with all fields), default values
    public Budget() {
        this(-1, -1, "", 0.0, LocalDate.now(), LocalDate.now().plusMonths(1));
    }

    //constructors (without ID for when you are saving/creating  a new budget)
    public Budget(int userId, String category, double amount, LocalDate startDate, LocalDate endDate) {
        this(-1, userId, category, amount, startDate, endDate);
    }

    public Budget(int budgetId, int userId, String category, double amount, LocalDate startDate, LocalDate endDate) {
        setBudgetId(budgetId);
        setUserId(userId);
        setCategory(category);
        setAmount(amount);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    // Property Accessors
    public IntegerProperty budgetIdProperty() { return budgetId; }
    public IntegerProperty userIdProperty() { return userId; }
    public StringProperty categoryProperty() { return category; }
    public DoubleProperty amountProperty() { return amount; }
    public ObjectProperty<LocalDate> startDateProperty() { return startDate; }
    public ObjectProperty<LocalDate> endDateProperty() { return endDate; }

    // Getters
    public int getBudgetId() { return budgetId.get(); }
    public int getUserId() { return userId.get(); }
    public String getCategory() { return category.get(); }
    public double getAmount() { return amount.get(); }
    public LocalDate getStartDate() { return startDate.get(); }
    public LocalDate getEndDate() { return endDate.get(); }

    // Setters
    public void setBudgetId(int id) { budgetId.set(id); }
    public void setUserId(int id) { userId.set(id); }
    public void setCategory(String category) { this.category.set(category); }
    public void setAmount(double amount) { this.amount.set(amount); }
    public void setStartDate(LocalDate date) { startDate.set(date); }
    public void setEndDate(LocalDate date) { endDate.set(date); }

    // Helper methods( checks if the budget is currently active by comparing the current date with the budget's start and end dates)
    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !today.isBefore(getStartDate()) && !today.isAfter(getEndDate());
    }

    //provides a string representation of the budget
    @Override
    public String toString() {
        return String.format("Budget[id=%d, user=%d, category=%s, amount=%.2f, period=%s to %s]",
                getBudgetId(), getUserId(), getCategory(), getAmount(), 
                getStartDate(), getEndDate());
    }
}