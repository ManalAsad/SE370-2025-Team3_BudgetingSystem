package org.project.repositories;

import org.project.models.Budget;
import java.time.LocalDate;
import java.util.List;

public interface BudgetRepository extends BaseRepository<Budget, Integer> {
    List<Budget> findByUserId(int userId) throws Exception;
    List<Budget> findByUserAndCategory(int userId, String category) throws Exception;
    List<Budget> findActiveBudgets(int userId, LocalDate date) throws Exception;
    boolean exists(int userId, String category, LocalDate start, LocalDate end) throws Exception;
}