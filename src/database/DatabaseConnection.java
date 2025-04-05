package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/budget_buddy";
    private static final String USER = "root";
    private static final String PASSWORD = "seproject";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
            return null;
        }
    }
}