package dao;

import config.DatabaseConnection;
import model.Barang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO {

    public List<Barang> getAll(String keyword, String category) {
        List<Barang> list = new ArrayList<>();
        String k = "%" + keyword + "%";
        // Query Search Semua Kolom + Filter Kategori
        String query = "SELECT * FROM barang WHERE (id LIKE ? OR nama LIKE ? OR lokasi LIKE ? OR kategori LIKE ? OR status LIKE ?) ";
        if (!category.equals("Semua")) query += "AND kategori = '" + category + "'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            for(int i=1; i<=5; i++) pst.setString(i, k);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Barang(
                        rs.getInt("id"), rs.getString("nama"), rs.getString("kategori"),
                        rs.getString("lokasi"), rs.getString("status")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void insert(Barang b) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO barang (nama, kategori, lokasi, status) VALUES (?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, b.getNama()); pst.setString(2, b.getKategori());
            pst.setString(3, b.getLokasi()); pst.setString(4, b.getStatus());
            pst.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void update(Barang b) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE barang SET nama=?, kategori=?, lokasi=?, status=? WHERE id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, b.getNama()); pst.setString(2, b.getKategori());
            pst.setString(3, b.getLokasi()); pst.setString(4, b.getStatus());
            pst.setInt(5, b.getId());
            pst.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void delete(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("DELETE FROM barang WHERE id=?");
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}