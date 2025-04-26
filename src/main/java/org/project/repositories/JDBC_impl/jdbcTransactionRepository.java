package org.project.repositories.JDBC_impl;

import org.project.models.Transaction;
import org.project.repositories.TransactionRepository;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class jdbcTransactionRepository implements TransactionRepository {
    private final Connection connection;

    public jdbcTransactionRepository(Connection connection) {
        this.connection = connection;
    }
    @Override
    public Transaction save(Transaction transaction) throws SQLException {
        if (transaction.getTransactionId() <= 0) {
            return insert(transaction);
        } else {
            return update(transaction);
        }
    }
    private Transaction insert(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, date, amount, category, description) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setDate(2, Date.valueOf(transaction.getDate()));
            stmt.setDouble(3, transaction.getAmount());
            stmt.setString(4, transaction.getTransactType());
            //stmt.setString(5, transaction.getDescription());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setTransactionId(rs.getInt(1));
                }
            }
            return transaction;
        }
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

    @Override
    public List<Transaction> findByUserIdAndCategoryAndDateBetween(int userId, String category, LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND category = ? " +
                "AND date BETWEEN ? AND ? ORDER BY date DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, category);
            stmt.setDate(3, Date.valueOf(start));
            stmt.setDate(4, Date.valueOf(end));

            return executeQuery(stmt);
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
                    rs.getString("category")
                    //rs.getString("description"),
                    //rs.getString("source")
            ));
        }
        return transactions;
    }

    @Override
    public Optional<Transaction> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new Transaction(
                        rs.getInt("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getDate("transaction_date").toLocalDate(),
                        rs.getDouble("amount"),
                        rs.getString("category")
                        //rs.getString("description"),
                        //rs.getString("source")
                ));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findAll() throws SQLException {
        String sql = "SELECT * FROM transactions";
        return queryTransactions(sql);
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Transaction update(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET user_id= ? ,amount = ?, category = ?, transaction_date = ? " +
                "WHERE transaction_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getUserId());
            stmt.setBigDecimal(1, BigDecimal.valueOf(transaction.getAmount()));
            stmt.setString(2, transaction.getTransactType());
            stmt.setDate(3, Date.valueOf(transaction.getDate()));
            //stmt.setString(5, entity.getDescription());
            //stmt.setString(6, transaction.getSource());
            stmt.setInt(5, transaction.getTransactionId());

            stmt.executeUpdate();
        }
        return transaction;
    }

    @Override
    public List<Transaction> findByDateRange(int userId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE user_id = ? AND transaction_date BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            return executeQuery(stmt);
        }
    }
}