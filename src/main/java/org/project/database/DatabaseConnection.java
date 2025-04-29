package org.project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String URL = "Enter your database url";
        String USER = "Enter your database user";
        String PASSWORD = "Enter your database password";

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
