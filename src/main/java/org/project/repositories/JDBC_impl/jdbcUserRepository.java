package org.project.repositories.JDBC_impl;

import org.project.models.User;
import org.project.repositories.UserRepository;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class jdbcUserRepository implements UserRepository {
    private final Connection connection;

    public jdbcUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
            }
        }
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapUser(rs)) : Optional.empty();
        }
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            return stmt.executeQuery().next();
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password")
        );
    }

    @Override
    public Optional<User> findById(Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public List<User> findAll() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public void delete(Integer id) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public User update(User entity) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

}