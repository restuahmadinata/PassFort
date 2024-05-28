package passfort.com.example.controller;

import passfort.com.example.config.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContactController {

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL"
                + ");";
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("User data inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public boolean isUsernameTaken(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
        return false;
    }

    public boolean checkUserExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int authenticateUser(String username, String password) {
        if (!checkUserExists(username)) {
            return 0; // Account not found
        }

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return 2; // Authentication successful
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 1; // Incorrect password
    }

    public boolean deleteUserByUsername(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public ObservableList<User> selectAllUsers() {
        String sql = "SELECT id, username, password FROM users";
        ObservableList<User> users = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    public void updateUser(int id, String username, String password) {
        String sql = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("User data updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("User data deleted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static class User {
        private int id;
        private String username;
        private String password;

        public User(int id, String username, String password) {
            this.id = id;
            this.username = username;
            this.password = password;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
