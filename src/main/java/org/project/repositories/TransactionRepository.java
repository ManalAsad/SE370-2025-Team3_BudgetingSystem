package org.project.repositories;

import org.project.models.Transaction;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends BaseRepository<Transaction, Integer> {
    List<Transaction> findByUserId(int userId) throws Exception;
    List<Transaction> findByCategory(int userId, String category) throws Exception;
    List<Transaction> findByDateRange(int userId, LocalDate startDate, LocalDate endDate) throws Exception;
    BigDecimal getTotalSpentByCategory(int userId, String category, LocalDate start, LocalDate end) throws Exception;
    List<Transaction> findByUserIdAndCategoryAndDateBetween(int userId, String category, LocalDate start, LocalDate end) throws SQLException;
}