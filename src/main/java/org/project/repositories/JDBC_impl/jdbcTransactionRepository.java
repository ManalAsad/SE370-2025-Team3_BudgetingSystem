package org.project.repositories.JDBC_impl;

import org.project.models.Transaction;
import org.project.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class jdbcTransactionRepository implements TransactionRepository {
    private Connection connection = null;

    public jdbcTransactionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Transaction save(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, amount, category, transaction_date, description, source) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setBigDecimal(2, BigDecimal.valueOf(transaction.getAmount()));
            stmt.setString(3, transaction.getCategory());
            stmt.setDate(4, Date.valueOf(transaction.getDate()));
            stmt.setString(5, transaction.getDescription());
            stmt.setString(6, transaction.getSource());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setTransactionId(rs.getInt(1));
                }
            }
        }
        return transaction;
    }

    @Override
    public List<Transaction> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        return queryTransactions(sql, userId);
    }

    @Override
    public List<Transaction> findByCategory(int userId, String category) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND category = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            return executeQuery(stmt);
        }
    }

    @Override
    public BigDecimal getTotalSpentByCategory(int userId, String category, 
                                           LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM transactions " +
                     "WHERE user_id = ? AND category = ? " +
                     "AND transaction_date BETWEEN ? AND ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            stmt.setDate(3, Date.valueOf(start));
            stmt.setDate(4, Date.valueOf(end));
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }

    private List<Transaction> queryTransactions(String sql, Object... params) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return executeQuery(stmt);
        }
    }

    private List<Transaction> executeQuery(PreparedStatement stmt) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            transactions.add(new Transaction(
                rs.getInt("transaction_id"),
                rs.getInt("user_id"),
                rs.getDate("transaction_date").toLocalDate(),
                rs.getDouble("amount"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getString("source")
            ));
        }
        return transactions;
    }

    @Override
    public Optional<Transaction> findById(Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<Transaction> findAll() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public void delete(Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public Transaction update(Transaction entity) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Transaction> findByDateRange(int userId, LocalDate startDate, LocalDate endDate) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByDateRange'");
    }
}