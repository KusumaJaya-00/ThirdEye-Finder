package dao;

import config.DatabaseConnection;
import model.Barang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BarangDAO (Data Access Object)
 * Kelas ini bertanggung jawab untuk menangani semua operasi database (CRUD) yang berkaitan dengan tabel 'barang'.
 */

public class BarangDAO {

    // SQL QUERY CONSTANTS 
    private static final String SQL_INSERT = "INSERT INTO barang (nama, kategori, lokasi, status) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE barang SET nama=?, kategori=?, lokasi=?, status=? WHERE id=?";
    private static final String SQL_DELETE = "DELETE FROM barang WHERE id=?";

    // Query dasar untuk mengambil data
    private static final String SQL_SELECT_BASE = "SELECT * FROM barang WHERE 1=1";

    public List<Barang> getAll(String keyword, String category) {
        List<Barang> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQL_SELECT_BASE);

        // 1. FILTER KEYWORD (SEARCH)
        // Mengecek apakah user mengetikkan sesuatu di search bar
        boolean isSearching = (keyword != null && !keyword.isEmpty());
        if (isSearching) {
            sql.append(" AND (id LIKE ? OR nama LIKE ? OR kategori LIKE ? OR lokasi LIKE ? OR status LIKE ?)");
        }

        // 2. FILTER KATEGORI
        // Mengecek apakah user memilih kategori spesifik (selain "Semua")
        boolean isFiltering = (category != null && !"Semua".equalsIgnoreCase(category));
        if (isFiltering) {
            sql.append(" AND kategori = ?");
        }

        // Mengurutkan data dari yang terbaru (ID terbesar)
        sql.append(" ORDER BY id DESC");

        // Eksekusi Query
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            // Mapping Parameter Search
            if (isSearching) {
                String searchPattern = "%" + keyword + "%";
                ps.setString(index++, searchPattern); // Cek ID
                ps.setString(index++, searchPattern); // Cek Nama
                ps.setString(index++, searchPattern); // Cek Kategori
                ps.setString(index++, searchPattern); // Cek Lokasi
                ps.setString(index++, searchPattern); // Cek Status
            }

            // Mapping Parameter Kategori
            if (isFiltering) {
                ps.setString(index++, category);
            }

            // Ambil hasil query dan masukkan ke dalam List
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Barang b = new Barang(
                            rs.getInt("id"),
                            rs.getString("nama"),
                            rs.getString("kategori"),
                            rs.getString("lokasi"),
                            rs.getString("status")
                    );
                    list.add(b);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Menyimpan data barang baru ke database.
     * @param b Objek barang yang akan disimpan
     * @return true jika berhasil disimpan
     */
    public boolean insert(Barang b) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            // Set nilai parameter sesuai urutan di SQL_INSERT
            ps.setString(1, b.getNama());
            ps.setString(2, b.getKategori());
            ps.setString(3, b.getLokasi());
            ps.setString(4, b.getStatus());

            return ps.executeUpdate() > 0; // Mengembalikan true jika ada baris yang terpengaruh
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Memperbarui data barang yang sudah ada berdasarkan ID.
     * @param b Objek barang dengan data terbaru
     * @return true jika berhasil diupdate
     */
    public boolean update(Barang b) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, b.getNama());
            ps.setString(2, b.getKategori());
            ps.setString(3, b.getLokasi());
            ps.setString(4, b.getStatus());
            ps.setInt(5, b.getId()); // ID digunakan sebagai kunci WHERE

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Menghapus data barang dari database berdasarkan ID.
     * @param id ID barang yang akan dihapus
     * @return true jika berhasil dihapus
     */
    public boolean delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}