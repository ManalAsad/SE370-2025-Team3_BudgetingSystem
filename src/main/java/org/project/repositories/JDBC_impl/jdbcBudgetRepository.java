package org.project.repositories.JDBC_impl;

import org.project.models.Budget;
import org.project.repositories.BudgetRepository;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class jdbcBudgetRepository implements BudgetRepository {
    private Connection connection = null;

    public jdbcBudgetRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Budget save(Budget budget) throws SQLException {
        String sql = "INSERT INTO budgets (user_id, category, amount, start_date, end_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, budget.getUserId());
            stmt.setString(2, budget.getCategory());
            stmt.setDouble(3, budget.getAmount());
            stmt.setDate(4, Date.valueOf(budget.getStartDate()));
            stmt.setDate(5, Date.valueOf(budget.getEndDate()));
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    budget.setBudgetId(rs.getInt(1));
                }
            }
        }
        return budget;
    }

    @Override
    public List<Budget> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM budgets WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return mapResultSet(stmt.executeQuery());
        }
    }

    @Override
    public List<Budget> findActiveBudgets(int userId, LocalDate date) throws SQLException {
        String sql = "SELECT * FROM budgets WHERE user_id = ? AND start_date <= ? AND end_date >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(date));
            stmt.setDate(3, Date.valueOf(date));
            
            return mapResultSet(stmt.executeQuery());
        }
    }

    @Override
    public boolean exists(int userId, String category, LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT 1 FROM budgets WHERE user_id = ? AND category = ? " +
                    "AND ((start_date BETWEEN ? AND ?) OR (end_date BETWEEN ? AND ?))";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            stmt.setDate(3, Date.valueOf(start));
            stmt.setDate(4, Date.valueOf(end));
            stmt.setDate(5, Date.valueOf(start));
            stmt.setDate(6, Date.valueOf(end));
            
            return stmt.executeQuery().next();
        }
    }

    private List<Budget> mapResultSet(ResultSet rs) throws SQLException {
        List<Budget> budgets = new ArrayList<>();
        while (rs.next()) {
            budgets.add(new Budget(
                rs.getInt("budget_id"),
                rs.getInt("user_id"),
                rs.getString("category"),
                rs.getDouble("amount"),
                rs.getDate("start_date").toLocalDate(),
                rs.getDate("end_date").toLocalDate()
            ));
        }
        return budgets;
    }

    @Override
    public Optional<Budget> findById(Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<Budget> findAll() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public void delete(Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Budget update(Budget entity) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Budget> findByUserAndCategory(int userId, String category) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUserAndCategory'");
    }
}