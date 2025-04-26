package org.project.services;

import org.project.models.*;
import org.project.repositories.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class BudgetService {
    private final BudgetRepository budgetRepo;
    private final TransactionRepository transactionRepo;
    private final NotificationRepository notificationRepo;

    public BudgetService(BudgetRepository budgetRepo, TransactionRepository transactionRepo, NotificationRepository notificationRepo) {
        this.budgetRepo = budgetRepo;
        this.transactionRepo = transactionRepo;
        this.notificationRepo = notificationRepo;
    }


    //validates input
    //creates a new budget
    //save it via "BudgeRepository"
    public Budget createBudget(int userId, String category, double amount, LocalDate startDate, LocalDate endDate) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Budget amount must be positive");
        }
        if(startDate == null || endDate == null){
            throw new IllegalArgumentException("Start and end date cannot be null.");
        }
        if (startDate.isAfter(endDate)){
            throw new IllegalArgumentException("End date must be after start date");
        }
        if (category == null || category.trim().isEmpty()){
            throw new IllegalArgumentException("Category can not be null or empty.");
        }


        Budget budget = new Budget(userId, category, amount, startDate, endDate);
        return budgetRepo.save(budget);
    }

    public void checkBudgetLimits(int userId) throws Exception {
            List<Budget> budgets = budgetRepo.findByUserId(userId);
            LocalDate today = LocalDate.now();
            for (Budget budget : budgets) {
                if (isBudgetActive(budget, today)) {
                    checkBudgetStatus(userId, budget, today);
                }
            }
    }




    private boolean isBudgetActive(Budget budget, LocalDate date) {
        return !date.isBefore(budget.getStartDate()) && !date.isAfter(budget.getEndDate());
    }

    private void checkBudgetStatus(int userId, Budget budget, LocalDate today) throws Exception {
        double totalSpent = calculateCategorySpending(userId, budget.getCategory(),
                budget.getStartDate(), budget.getEndDate());
        double budgetAmount = budget.getAmount();
        double percentageUsed = totalSpent / budgetAmount;

        if (percentageUsed >= 1.0) {
            createNotification(userId,
                    String.format("Budget exceeded for %s: $%.2f of $%.2f",
                            budget.getCategory(), totalSpent, budgetAmount));
        } else if (percentageUsed >= 0.9) {
            createNotification(userId,
                    String.format("Approaching budget limit for %s: %.0f%% used ($%.2f of $%.2f)",
                            budget.getCategory(), percentageUsed * 100, totalSpent, budgetAmount));
        }
    }

    private double calculateCategorySpending(int userId, String category,
                                             LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Transaction> transactions = transactionRepo.findByUserIdAndCategoryAndDateBetween(
                userId, category, startDate, endDate);
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    private void createNotification(int userId, String message) throws Exception {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setRead(false);
        notificationRepo.save(notification);
    }

}