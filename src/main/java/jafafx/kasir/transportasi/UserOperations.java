package jafafx.kasir.transportasi;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserOperations {
    private Connection connection;

    public UserOperations() throws SQLException {
        connection = DatabaseConnection.getConnection();
        if (connection == null) {
            throw new SQLException("Koneksi ke database gagal.");
        }
    }

    // Register
    public boolean registerUser(String username, String password, String role) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("User registered successfully!");
                return true;
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Username sudah digunakan.");
        } catch (SQLException e) {
            System.err.println("Gagal melakukan register: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Login
    public boolean loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Note: password harus di-hash di dunia nyata!
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Login successful!");
                    return true;
                } else {
                    System.out.println("Invalid username or password.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal melakukan login: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Logout
    public void logoutUser() {
        // Contoh logout tanpa session
        System.out.println("User logged out successfully!");
    }

    // Get Profile
    public User getProfile(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil profil: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Update Password
    public void updatePassword(String username, String newPassword) {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPassword); // Note: password sebaiknya di-hash
            stmt.setString(2, username);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Password updated successfully!");
            } else {
                System.out.println("User tidak ditemukan. Password tidak diubah.");
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengubah password: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

