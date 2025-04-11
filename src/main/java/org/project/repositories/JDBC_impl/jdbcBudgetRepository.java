package org.project.repositories.JDBC_impl;

import org.project.models.Budget;
import org.project.repositories.BudgetRepository;

//import com.mysql.cj.protocol.ResultsetRow;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/*jdbc  of budget repository that handles database operation for budget entities */

public class jdbcBudgetRepository implements BudgetRepository {
    private Connection connection = null;

    public jdbcBudgetRepository(Connection connection) {
        this.connection = connection;
    }

    //inserts a new budget record
    @Override
    public Budget save(Budget budget) throws SQLException {
        String sql = "INSERT INTO budgets (user_id, category, amount, start_date, end_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // set parameters for the save statements
            //stmt.setInt(1, budget.getBudgetId());
            stmt.setInt(1, budget.getUserId());
            stmt.setString(2, budget.getCategory());
            stmt.setDouble(3, budget.getAmount());
            stmt.setDate(4, Date.valueOf(budget.getStartDate()));
            stmt.setDate(5, Date.valueOf(budget.getEndDate()));
            
            stmt.executeUpdate();
            
            // retrieve auto generated ID
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
    public List<Budget> findActiveBudgets(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT * FROM budgets WHERE user_id = ? AND start_date <= ? AND end_date >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            
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

    
    @Override
    public Optional<Budget> findById(Integer id) throws Exception {
        String sql = "SELECT * FROM budgets WHERE budget_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToBudget(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Budget> findAll() throws Exception {
        String sql = "SELECT * FROM budgets";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return mapResultSet(rs);
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM budgets WHERE budget_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Budget update(Budget entity) throws Exception {
        String sql = "UPDATE budgets SET user_id = ?, category = ?, amount = ?, " +
        "start_date = ?, end_date = ? WHERE budget_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, entity.getUserId());
            stmt.setString(2, entity.getCategory());
            stmt.setDouble(3, entity.getAmount());
            stmt.setDate(4, Date.valueOf(entity.getStartDate()));
            stmt.setDate(5, Date.valueOf(entity.getEndDate()));
            stmt.setInt(6, entity.getBudgetId());

            stmt.executeUpdate();
        }
        return entity;
    }

    @Override
    public List<Budget> findByUserAndCategory(int userId, String category) throws Exception {
        String sql = "SELECT * FROM budgets WHERE user_id = ? AND category = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            try (ResultSet rs = stmt.executeQuery()) {
                return mapResultSet(rs);
            }
        }
    }

    private Budget mapRowToBudget(ResultSet rs) throws SQLException {
        return new Budget(
            rs.getInt("budget_id"),
            rs.getInt("user_id"),
            rs.getString("category"),
            rs.getDouble("amount"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate()
        );
    }

    private List<Budget> mapResultSet(ResultSet rs) throws SQLException {
        List<Budget> budgets = new ArrayList<>();
        while (rs.next()) {
            budgets.add(mapRowToBudget(rs));
        }
        return budgets;
    }
    
}

