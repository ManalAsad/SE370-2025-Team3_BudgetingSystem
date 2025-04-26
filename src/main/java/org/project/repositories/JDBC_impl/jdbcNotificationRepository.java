package org.project.repositories.JDBC_impl;
import org.project.models.Notification;
import org.project.repositories.NotificationRepository;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class jdbcNotificationRepository implements NotificationRepository{
    private final Connection connection;

    public jdbcNotificationRepository(Connection connection){
        this.connection =connection;
    }


    public Notification save(Notification notification) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, message, timestamp, is_read) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getTimestamp()));
            stmt.setBoolean(4, notification.isRead());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    notification.setNotificationId(rs.getInt(1));
                }
            }
            return notification;
        }
    }

    @Override
    public List<Notification> findByUserId(int userId) throws SQLException{
        String sql = "SELECT * FROM notifications where user_id = ? ORDER BY timestamp DESC";
        return queryNotifications(sql, userId);
    }

    @Override
    public List<Notification> findByUserIdAndReadFalse(int userId) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = false ORDER BY timestamp DESC";
        return queryNotifications(sql, userId);
    }

    //@Override
    /*public Notification save(Notification notification)throws SQLException{
        String sql = "INSERT INTO notifications (user_id, message, timestamp, read VALUES (?,?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1, notification.getNotificationId());
            stmt.setString(2,notification.getMessage());
            stmt.setTimestamp(3,Timestamp.valueOf(notification.getTimestamp()));
            stmt.setBoolean(4,notification.isRead());
            stmt.executeUpdate();

            try(ResultSet rs =stmt.getGeneratedKeys()){
                if(rs.next()){
                    notification.setNotificationId(rs.getInt(1));
                }
            }
            return notification;
        }
    }*/

    @Override
    public Optional<Notification> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? Optional.of(mapResultSetToNotification(rs)) : Optional.empty();
        }
    }

    @Override
    public List<Notification> findAll() throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        }
        return notifications;
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Notification update(Notification notification) throws SQLException {
        String sql = "UPDATE notifications SET user_id = ?, message = ?, timestamp = ?, is_read = ? " +
                "WHERE notification_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notification.getUserId());
            stmt.setString(2, notification.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(notification.getTimestamp()));
            stmt.setBoolean(4, notification.isRead());
            stmt.setInt(5, notification.getNotificationId());

            stmt.executeUpdate();
            return notification;
        }
    }

    @Override
    public void markAsRead(int notificationId) throws SQLException{
        String sql = "UPDATE notifications SET read = true WHERE notification_id = ?";
        try(PreparedStatement stmt =connection.prepareStatement(sql)){
            stmt.setInt(1,notificationId);
            stmt.executeUpdate();
        }
    }

    // Helper methods
    private List<Notification> queryNotifications(String sql, Object... params) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notifications.add(mapResultSetToNotification(rs));
            }
        }
        return notifications;
    }
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
        return new Notification(
                rs.getInt("notification_id"),
                rs.getInt("user_id"),
                rs.getString("message"),
                rs.getTimestamp("timestamp").toLocalDateTime(),
                rs.getBoolean("is_read")
        );
    }
    private List<Notification> executeQuery(PreparedStatement stmt) throws SQLException{
        List<Notification> notifications = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();

        while (rs.next()){
            notifications.add(new Notification(
                    rs.getInt("notification_id"),
                    rs.getInt("user_id"),
                    rs.getString("message"),
                    rs.getTimestamp("timestamp").toLocalDateTime(),
                    rs.getBoolean("is-read")
            ));
        }
        return notifications;
    }
}
