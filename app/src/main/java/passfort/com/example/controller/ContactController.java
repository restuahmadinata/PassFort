package passfort.com.example.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import passfort.com.example.config.DatabaseConnection;
import passfort.models.*;

public class ContactController {

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT NOT NULL UNIQUE,"
                + " password TEXT NOT NULL,"
                + " role TEXT NOT NULL,"
                + " fullName TEXT NOT NULL"
                + ");";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("Table created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertUser(String username, String password, String role, String fullName) throws SQLException {
        String sql = "INSERT INTO users(username, password, role, fullName) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, fullName);
            pstmt.executeUpdate();
            System.out.println("User data inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public void insertUserToAppDatabase(int userId, String username, String password, String apps) throws SQLException {
        String sql = "INSERT INTO UserAppData(userId, username, password, apps) VALUES(?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.connectToAppDatabase();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, username);
            pstmt.setString(3, password);
            pstmt.setString(4, apps);
            pstmt.executeUpdate();
            System.out.println("User data inserted successfully into AppDatabase.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
    
    
    public String[] getUserDataForApp(String appName) throws SQLException {
        String sql = "SELECT username, password FROM UserAppData WHERE apps = ?";
        try (Connection conn = DatabaseConnection.connectToAppDatabase();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, appName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{rs.getString("username"), rs.getString("password")};
                } else {
                    return null; 
                }
            }
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
            return 0; 
        } 
    
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    int userId = rs.getInt("id");
    
                    if ("Admin".equals(role)) {
                        return -userId;
                    }
                    return userId;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1; 
    }
    

    public boolean deleteUserByUsername(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            String role = getUserRole(username);
            if ("Admin".equals(role)) {
                System.out.println("Admin account cannot be deleted.");
                return false;
            }
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public ObservableList<User> selectAllUsers() {
        String sql = "SELECT id, username, password, role FROM users";
        ObservableList<User> users = FXCollections.observableArrayList();
    
        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
    
                User user;
                if ("Admin".equals(role)) {
                    user = new Admin(id, username, password);
                } else {
                    user = new Regular(id, username, password);
                }
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

    public void updateUserPasswordForApp(String username, String newPassword, String app, int userId) {
        String sql = "UPDATE UserAppData SET password = ? WHERE username = ? AND apps = ? AND userId = ?";
        try (Connection conn = DatabaseConnection.connectToAppDatabase();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);
            pstmt.setString(3, app);
            pstmt.setInt(4, userId);
            pstmt.executeUpdate();
            System.out.println("Password updated successfully for user: " + username + " and app: " + app);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean checkUserExistsForApp(String username, String app, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM UserAppData WHERE username = ? AND apps = ? AND userId = ?";
        try (Connection conn = DatabaseConnection.connectToAppDatabase();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, app);
            pstmt.setInt(3, userId);
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
    
    public void deleteUserFromAppDatabase(String username, String app, int userId) throws SQLException {
        String sql = "DELETE FROM UserAppData WHERE username = ? AND apps = ? AND userId = ?";
        try (Connection conn = DatabaseConnection.connectToAppDatabase();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, app);
            pstmt.setInt(3, userId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User data deleted successfully from AppDatabase.");
            } else {
                System.out.println("No matching data found to delete.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public ObservableList<UserAppData> selectAllUserAppData() {
        String sql = "SELECT userId, username, password, apps FROM UserAppData";
        ObservableList<UserAppData> userAppDataList = FXCollections.observableArrayList();
    
        try (Connection conn = DatabaseConnection.connectToAppDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String apps = rs.getString("apps");
    
                UserAppData userAppData = new UserAppData(userId, username, password, apps);
                userAppDataList.add(userAppData);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userAppDataList;
    }

    public ObservableList<UserAppData> selectUserAppDataByUserId(int uId) {
        String sql = "SELECT userId, username, password, apps FROM UserAppData WHERE userId = ?";
        ObservableList<UserAppData> userAppDataList = FXCollections.observableArrayList();
    
        try (Connection conn = DatabaseConnection.connectToAppDatabase();            
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, uId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int userId = rs.getInt("userId");
                        String username = rs.getString("username");
                        String password = rs.getString("password");
                        String apps = rs.getString("apps");
            
                        UserAppData userAppData = new UserAppData(userId, username, password, apps);
                        userAppDataList.add(userAppData);
                    }
                }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userAppDataList;
    }

    public String getUserFullName(int userId) {
        String sql = "SELECT fullName FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("fullName");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void updateUserPassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            System.out.println("User password updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public boolean verifyPassword(int userId, String inputPassword) {
        String sql = "SELECT password FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    return inputPassword.equals(storedPassword);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    
    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}

