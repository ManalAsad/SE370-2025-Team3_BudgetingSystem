package org.project.services;

import org.project.models.Budget;
import org.project.repositories.BudgetRepository;
import java.time.LocalDate;
import java.util.List;


//uses BudgetRepsoitory to interact with the database.
public class BudgetService {
    private final BudgetRepository budgetRepo;

    public BudgetService(BudgetRepository budgetRepo) {
        this.budgetRepo = budgetRepo;
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


        //checks for overlapping budgets
        if(hasActiveBudgetForCategory(userId, category, startDate, endDate)){
            throw new IllegalArgumentException("A budget for this category already exists in the given date range. ");
        }


        Budget budget = new Budget(userId, category, amount, startDate, endDate);
        return budgetRepo.save(budget);
    }


    //fetches all budgets for a given user and delegates to BudgetRespository
    public List<Budget> getUserBudgets(int userId) throws Exception {
        return budgetRepo.findByUserId(userId);
    }

    //fetches budgets active on the current date
    public List<Budget> getActiveBudgets(int userId) throws Exception {
        return budgetRepo.findActiveBudgets(userId, LocalDate.now(), LocalDate.now());
    }

    public List<Budget> getBudgetsByUserAndCategory(int userId, String category) throws Exception{
        return budgetRepo.findByUserAndCategory(userId, category);
    }
    
    //checks if the user already has an active budget for a given category on a specific date
    public boolean hasActiveBudgetForCategory(int userId, String category, LocalDate startDate, LocalDate endDate) throws Exception {
        return budgetRepo.exists(userId, category, startDate, endDate);
    }
}