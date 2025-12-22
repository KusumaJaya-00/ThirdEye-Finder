package config;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String HOST = "192.168.31.167";
    private static final String PORT = "3306";
    private static final String NAME = "db_thirdeye";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + NAME;

    // Akses Database Client Server
    private static final String USER = "admin_thirdeye";
    private static final String PASS = "thirdeye123";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Konek Database: " + e.getMessage());
            return null;
        }
    }
}