package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException{
        String URL = "jdbc:mysql://localhost:3307/budget_buddy";
        String USER = "root";
        String PASSWORD = "seproject";

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
    


   /* public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL successfully!");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection failed!");
            return null;
        }
    }*/ 
