package model;

public class Barang {
    private int id;
    private String nama;
    private String kategori;
    private String lokasi;
    private String status;

    public Barang(int id, String nama, String kategori, String lokasi, String status) {
        this.id = id;
        this.nama = nama;
        this.kategori = kategori;
        this.lokasi = lokasi;
        this.status = status;
    }

    // Getters
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public String getLokasi() { return lokasi; }
    public String getStatus() { return status; }
}