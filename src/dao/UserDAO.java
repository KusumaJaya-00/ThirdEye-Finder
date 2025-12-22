package dao;

import config.DatabaseConnection;
import model.User;
import java.sql.*;

public class UserDAO {

    public User login(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT role FROM users WHERE username=? AND password=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username); pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return new User(username, rs.getString("role"));
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean register(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Cek Duplikat
            PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            check.setString(1, username);
            if (check.executeQuery().next()) return false; // Gagal, user ada

            // 2. Insert Baru
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username); pst.setString(2, password);
            return pst.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
}