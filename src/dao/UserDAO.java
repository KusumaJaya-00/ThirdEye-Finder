package dao;

import config.DatabaseConnection;
import model.User;
import java.sql.*;

/**
 * UserDAO (Data Access Object)
 * Kelas ini menangani interaksi dengan tabel 'users' di database.
 * Fokus utamanya adalah Autentikasi (Login) dan Pendaftaran (Register).
 */

public class UserDAO {

    // SQL CONSTANTS 
    // Query Login: Mengambil role user jika username & password cocok
    private static final String SQL_LOGIN = "SELECT role FROM users WHERE username=? AND password=?";
    // Query Cek Duplikat: Mengecek apakah username sudah terpakai
    private static final String SQL_CHECK_EXISTS = "SELECT id FROM users WHERE username = ?";
    // Query Register: Menambahkan user baru dengan default role 'user'
    private static final String SQL_REGISTER = "INSERT INTO users (username, password, role) VALUES (?, ?, 'user')";

    /**
     * Melakukan proses Login user.
     * @param username Username yang diinput
     * @param password Password yang diinput
     * @return Objek User jika login berhasil, atau null jika gagal
     */
    public User login(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_LOGIN)) {

            // Set parameter query
            ps.setString(1, username);
            ps.setString(2, password);

            // Eksekusi query dan cek hasilnya
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Jika ada hasil, berarti login sukses.
                    // Kembalikan objek User dengan Role yang didapat dari DB.
                    return new User(username, rs.getString("role"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Login gagal (user tidak ditemukan / password salah)
    }

    /**
     * Mendaftarkan user baru ke database.
     * @param username Username baru
     * @param password Password baru
     * @return true jika registrasi berhasil, false jika gagal (misal username sudah ada)
     */
    public boolean register(String username, String password) {
        //Cek duplikasi username
        if (isUserExists(username)) {
            return false;
        }

        // Lanjutkan registrasi
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_REGISTER)) {

            ps.setString(1, username);
            ps.setString(2, password);


            // executeUpdate mengembalikan jumlah baris yang terpengaruh
            // Jika > 0 berarti data berhasil masuk
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // HELPER METHOD 
    // Method bantuan untuk mengecek keberadaan username di database.
    private boolean isUserExists(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_CHECK_EXISTS)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // True jika data ditemukan (username sudah ada)
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Fail-safe: Anggap user ada jika terjadi error DB untuk mencegah duplikasi
        }
    }
}