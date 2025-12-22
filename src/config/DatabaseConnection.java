package config;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 * DatabaseConnection
 * Kelas utilitas untuk mengatur koneksi ke database MySQL.
 * Menggunakan JDBC Driver untuk menghubungkan aplikasi Java dengan database server.
 */
public class DatabaseConnection {
    // KONFIGURASI DATABASE 
    private static final String HOST = "192.168.31.206"; // IP Address Server Database
    private static final String PORT = "3306";           // Port Default MySQL
    private static final String NAME = "db_thirdeye";    // Nama Database

    // URL Koneksi lengkap
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + NAME;

    // KREDENSIAL AKSES 
    // Username dan Password untuk login ke database
    private static final String USER = "admin_thirdeye";
    private static final String PASS = "thirdeye123";

    /**
     * Method static untuk mendapatkan objek Connection yang aktif.
     * Method ini akan dipanggil oleh DAO setiap kali butuh akses ke database.
     * * @return Connection object jika berhasil, atau null jika gagal.
     */
    public static Connection getConnection() {
        try {
            // Load JDBC Driver (Penting untuk memastikan driver MySQL terbaca)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Buat Koneksi menggunakan DriverManager
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            // Jika koneksi gagal (misal server mati, password salah), tampilkan pesan error
            JOptionPane.showMessageDialog(null, "Gagal Konek Database: " + e.getMessage());
            return null;
        }
    }
}