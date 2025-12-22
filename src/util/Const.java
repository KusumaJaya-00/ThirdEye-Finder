package util;

public class Const {
    // APP INFO 
    public static final String APP_NAME = "Third Eye Finder";
    public static final String TXT_WELCOME = "Selamat Datang";

    // Deskripsi Branding (Login/Register)
    public static final String APP_DESC_1 = "Sistem Manajemen Barang Hilang";
    public static final String APP_DESC_2 = "& Temu yang Terintegrasi";
    public static final String APP_DESC_REG_1 = "Buat akun untuk melaporkan";
    public static final String APP_DESC_REG_2 = "atau mengklaim barang.";
    public static final String HEADER_REGISTER = "Bergabunglah";

    // TITLES & LABELS 
    public static final String TITLE_LOGIN = "Login Akun";
    public static final String TITLE_REGISTER = "Registrasi";
    public static final String TITLE_FORM_USER = "LAPOR BARANG HILANG";
    public static final String TITLE_TABLE = "Daftar Barang";

    public static final String LBL_USERNAME = "Username";
    public static final String LBL_PASSWORD = "Password";
    public static final String LBL_NAMA = "Nama Barang";
    public static final String LBL_KATEGORI = "Kategori";
    public static final String LBL_LOKASI = "Lokasi";
    public static final String LBL_STATUS = "Status";

    // BUTTON TEXTS 
    public static final String BTN_LOGIN = "MASUK APLIKASI";
    public static final String BTN_REGISTER = "DAFTAR SEKARANG";
    public static final String TXT_BTN_LOGOUT = "Keluar / Logout";
    public static final String TXT_BTN_CANCEL_REPORT = "BATALKAN LAPORAN"; // Saat form terbuka
    public static final String TXT_BTN_SHOW_REPORT = "LAPOR BARANG HILANG"; // Saat form tertutup
    public static final String TXT_BTN_SAVE = "SIMPAN BARU";
    public static final String TXT_BTN_UPDATE = "UPDATE DATA";
    public static final String TXT_BTN_DELETE = "HAPUS";
    public static final String TXT_BTN_CANCEL = "BATAL";
    public static final String TXT_BTN_SEND = "KIRIM LAPORAN";

    // LINKS & MESSAGES 
    public static final String TXT_NO_ACCOUNT = "Belum punya akun?";
    public static final String LINK_REGISTER = "Daftar disini";
    public static final String TXT_HAVE_ACCOUNT = "Kembali ke halaman";
    public static final String LINK_LOGIN = "Login";

    public static final String MSG_LOGIN_FAIL = "Login Gagal! Cek Username/Password.";
    public static final String MSG_REG_SUCCESS = "Registrasi Berhasil!";
    public static final String MSG_REG_FAIL = "Username sudah terdaftar!";
    public static final String MSG_EMPTY = "Data tidak boleh kosong!";

    // DATA OPTIONS 
    public static final String[] OPT_KATEGORI = { "Elektronik", "Dokumen", "Pribadi", "Kendaraan", "Lain-lain" };
    public static final String[] OPT_STATUS = { "Hilang", "Ditemukan", "Dikembalikan" };
    public static final String[] OPT_FILTER = { "Semua", "Elektronik", "Dokumen", "Pribadi", "Kendaraan", "Lain-lain" };

    // TABLE COLUMNS 
    public static final String[] COLS_ADMIN = {"ID", "Nama Barang", "Kategori", "Lokasi", "Status"};
    public static final String[] COLS_USER  = {"ID", "Nama Barang", "Kategori", "Lokasi", "Status", "Aksi"};
}