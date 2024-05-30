package passfort.com.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection connect() {
        String url = "jdbc:sqlite:UserDatabase.db";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static Connection connectToAppDatabase() {
        String url = "jdbc:sqlite:AppDatabase.db";  // Update the URL to point to AppDatabase.db

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to AppDatabase established.");
        } catch (SQLException e) {
            System.out.println("Connection error:" + e.getMessage());
        }
        return conn;
    }
}
