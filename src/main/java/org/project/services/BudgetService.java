package org.project.services;

import org.project.models.Budget;
import org.project.repositories.BudgetRepository;
import java.time.LocalDate;
import java.util.List;

public class BudgetService {
    private final BudgetRepository budgetRepo;

    public BudgetService(BudgetRepository budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Budget createBudget(int userId, String category, double amount,
                             LocalDate startDate, LocalDate endDate) throws Exception {
        if (amount <= 0) throw new IllegalArgumentException("Budget amount must be positive");
        if (startDate.isAfter(endDate)) throw new IllegalArgumentException("End date must be after start date");
        
        Budget budget = new Budget(userId, category, amount, startDate, endDate);
        return budgetRepo.save(budget);
    }

    public List<Budget> getUserBudgets(int userId) throws Exception {
        return budgetRepo.findByUserId(userId);
    }

    public List<Budget> getActiveBudgets(int userId) throws Exception {
        return budgetRepo.findActiveBudgets(userId, LocalDate.now());
    }

    public boolean hasBudgetForCategory(int userId, String category, LocalDate date) throws Exception {
        return !budgetRepo.findActiveBudgets(userId, date).stream()
                .anyMatch(b -> b.getCategory().equalsIgnoreCase(category));
    }
}