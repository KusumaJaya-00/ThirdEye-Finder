package view;

import dao.BarangDAO;
import model.Barang;
import model.User;
import util.Const;
import util.StyleUtil;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AppPanel extends JPanel {
    private MainFrame mainFrame;
    private BarangDAO barangDAO;
    private User currentUser;
    private String activeFilterCategory = "Semua";

    // UI Components
    private JTextField txtInputId, txtInputNama, txtInputLokasi, txtSearch;
    private JComboBox<String> cmbKategori, cmbStatus;
    private StyleUtil.FlatButton btnSimpan, btnHapus, btnClear, btnKirimLaporan, btnBatalLaporan, btnToggleLapor;
    private JPanel panelFormCard, panelFormInput, panelBtnAdmin, panelBtnUser, panelFilterBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblWelcome, lblRoleSubtitle, lblFormTitleUser;

    // Layout helper
    private JPanel mainContent;
    private GridBagConstraints gbcMain;

    public AppPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.barangDAO = new BarangDAO();
        setLayout(new BorderLayout());
        setupUI();
    }

    public void initSession(User user) {
        this.currentUser = user;
        lblRoleSubtitle.setText(Const.TXT_WELCOME + ", " + user.getUsername() + " (" + user.getRole().toUpperCase() + ")");
        setupLayoutByRole();
        activeFilterCategory = "Semua";
        resetFilterButtons();
        refreshTable();
    }

    private void setupUI() {
        setupHeader();
        setupMainContentLayout();
        setupFormSection();
        setupFilterSearchSection();
        setupTableSection();
        setupListeners();
    }

    // Bagian Header (Judul Aplikasi & Tombol Logout)
    private void setupHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtil.COL_HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,3,0, StyleUtil.COL_ORANGE_DECOR),
                new EmptyBorder(15, 25, 15, 25)));

        JPanel headText = new JPanel(new GridLayout(2,1)); headText.setOpaque(false);

        lblWelcome = StyleUtil.createHeaderLabel(Const.APP_NAME);
        lblRoleSubtitle = StyleUtil.createSubtitleLabel(Const.TXT_WELCOME);

        headText.add(lblWelcome); headText.add(lblRoleSubtitle);

        StyleUtil.FlatButton btnLogout = new StyleUtil.FlatButton(Const.TXT_BTN_LOGOUT, StyleUtil.COL_RED);
        btnLogout.setPreferredSize(new Dimension(150, 30)); btnLogout.setMinimumSize(new Dimension(150, 30));
        btnLogout.addActionListener(e -> mainFrame.showCard("LOGIN"));
        header.add(headText, BorderLayout.CENTER); header.add(btnLogout, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void setupMainContentLayout() {
        mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(StyleUtil.COL_BG_APP);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        gbcMain = new GridBagConstraints();
        gbcMain.fill = GridBagConstraints.HORIZONTAL;
        gbcMain.anchor = GridBagConstraints.NORTH;
        gbcMain.weightx = 1.0;
        gbcMain.gridx = 0;

        add(mainContent, BorderLayout.CENTER);
    }

    // Bagian Form Input (Logic toggle & card layout berbasis role)
    private void setupFormSection() {
        JPanel formWrapper = new JPanel();
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
        formWrapper.setOpaque(false);

        btnToggleLapor = new StyleUtil.FlatButton(Const.TXT_BTN_CANCEL_REPORT, StyleUtil.COL_RED);
        btnToggleLapor.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnToggleLapor.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtil.COMP_HEIGHT));
        btnToggleLapor.setPreferredSize(new Dimension(200, StyleUtil.COMP_HEIGHT));

        panelFormCard = new JPanel(new BorderLayout());
        panelFormCard.setBackground(Color.WHITE);
        panelFormCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1),
                new EmptyBorder(20, 20, 20, 20)));

        lblFormTitleUser = StyleUtil.createFieldLabel(Const.TITLE_FORM_USER);
        lblFormTitleUser.setForeground(StyleUtil.COL_ORANGE_TEXT);
        lblFormTitleUser.setBorder(new EmptyBorder(0,0,15,0));

        panelFormInput = new JPanel(new GridBagLayout());
        panelFormInput.setOpaque(false);

        // Inisialisasi Komponen Form
        txtInputId = new JTextField();
        txtInputNama = new JTextField(); StyleUtil.styleField(txtInputNama);
        txtInputLokasi = new JTextField(); StyleUtil.styleField(txtInputLokasi);

        cmbKategori = new JComboBox<>(Const.OPT_KATEGORI);
        StyleUtil.styleField(cmbKategori);
        cmbStatus = new JComboBox<>(Const.OPT_STATUS);
        StyleUtil.styleField(cmbStatus);

        // Panel Tombol untuk Admin (Simpan/Hapus)
        panelBtnAdmin = new JPanel(new GridLayout(1, 3, 15, 0)); panelBtnAdmin.setOpaque(false);
        btnSimpan = new StyleUtil.FlatButton(Const.TXT_BTN_SAVE, StyleUtil.COL_GREEN);
        btnHapus = new StyleUtil.FlatButton(Const.TXT_BTN_DELETE, StyleUtil.COL_RED);
        btnClear = new StyleUtil.FlatButton(Const.TXT_BTN_CANCEL, StyleUtil.COL_GREY_BTN);
        panelBtnAdmin.add(btnSimpan); panelBtnAdmin.add(btnHapus); panelBtnAdmin.add(btnClear);

        // Panel Tombol untuk User Biasa (Kirim Laporan)
        panelBtnUser = new JPanel(new GridLayout(1, 2, 15, 0)); panelBtnUser.setOpaque(false);
        btnKirimLaporan = new StyleUtil.FlatButton(Const.TXT_BTN_SEND, StyleUtil.COL_GREEN);
        btnBatalLaporan = new StyleUtil.FlatButton(Const.TXT_BTN_CANCEL, StyleUtil.COL_GREY_BTN);
        panelBtnUser.add(btnKirimLaporan); panelBtnUser.add(btnBatalLaporan);

        JPanel topForm = new JPanel(new BorderLayout()); topForm.setOpaque(false);
        topForm.add(lblFormTitleUser, BorderLayout.NORTH);
        topForm.add(panelFormInput, BorderLayout.CENTER);
        panelFormCard.add(topForm, BorderLayout.CENTER);

        formWrapper.add(btnToggleLapor);
        formWrapper.add(Box.createVerticalStrut(10));
        formWrapper.add(panelFormCard);

        gbcMain.gridy = 0;
        mainContent.add(formWrapper, gbcMain);
    }

    // Bagian Filter Kategori & Search Bar
    private void setupFilterSearchSection() {
        JPanel toolsPanel = new JPanel(new GridBagLayout());
        toolsPanel.setOpaque(false);
        toolsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        GridBagConstraints gbcTools = new GridBagConstraints();
        gbcTools.gridx = 0; gbcTools.gridy = 0;
        gbcTools.weightx = 1.0;
        gbcTools.fill = GridBagConstraints.HORIZONTAL;
        gbcTools.anchor = GridBagConstraints.WEST;

        panelFilterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelFilterBar.setOpaque(false);
        panelFilterBar.setBorder(new EmptyBorder(0,0,12,0));

        JPanel filterWrapper = new JPanel(new BorderLayout());
        filterWrapper.setOpaque(false);
        filterWrapper.add(panelFilterBar, BorderLayout.WEST);

        // Membuat tombol filter secara dinamis dari util.Const
        for(String cat : Const.OPT_FILTER) {
            StyleUtil.FilterButton fb = new StyleUtil.FilterButton(cat);
            if(cat.equals("Semua")) fb.setActive(true);
            fb.addActionListener(e -> {
                // Reset semua tombol lain saat satu diklik
                for(Component c : panelFilterBar.getComponents()) {
                    if(c instanceof StyleUtil.FilterButton) ((StyleUtil.FilterButton)c).setActive(false);
                }
                fb.setActive(true);
                activeFilterCategory = cat; // Set filter aktif
                refreshTable(); // Refresh tabel sesuai kategori
            });
            panelFilterBar.add(fb);
        }

        toolsPanel.add(filterWrapper, gbcTools);

        txtSearch = new JTextField();
        StyleUtil.styleField(txtSearch);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleUtil.COL_BLUE_SEARCH, 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        gbcTools.gridy = 1;
        gbcTools.insets = new Insets(10, 0, 0, 0);
        toolsPanel.add(txtSearch, gbcTools);

        gbcMain.gridy = 1;
        mainContent.add(toolsPanel, gbcMain);
    }

    private void setupTableSection() {
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(StyleUtil.COL_TABLE_HEADER);
        titleBar.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel t1 = StyleUtil.createFieldLabel(Const.TITLE_TABLE);
        t1.setForeground(Color.WHITE);
        titleBar.add(t1, BorderLayout.WEST);

        // Init Table Model (Default Admin Cols)
        tableModel = new DefaultTableModel(Const.COLS_ADMIN, 0);
        table = new JTable(tableModel);
        table.setRowHeight(45); table.setShowVerticalLines(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE); scroll.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(titleBar, BorderLayout.NORTH); tableCard.add(scroll, BorderLayout.CENTER);

        gbcMain.gridy = 2;
        gbcMain.weighty = 1.0;
        gbcMain.fill = GridBagConstraints.BOTH;
        mainContent.add(tableCard, gbcMain);
    }

    private void setupListeners() {
        // Listener tombol-tombol CRUD
        btnSimpan.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());
        btnKirimLaporan.addActionListener(e -> simpanData());
        btnBatalLaporan.addActionListener(e -> toggleUserForm(false));
        btnToggleLapor.addActionListener(e -> toggleUserForm(!panelFormCard.isVisible()));

        // Listener Search Bar (Realtime Search saat mengetik)
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });

        // Listener Klik Tabel
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    loadDataToForm(row);
                    if (currentUser.getRole().equals("user") && col == 5) { // WA Claim
                        Object status = table.getValueAt(row, 4);
                        if(status != null && "Ditemukan".equalsIgnoreCase(status.toString().trim())) {
                            klaimWA(table.getValueAt(row, 1).toString());
                        }
                    }
                }
            }
        });

        // MOUSE MOTION LISTENER UNTUK CURSOR TANGAN PADA BADGE KLAIM WA 
        table.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                // Cek apakah user sedang login sebagai 'user', kolomnya kolom aksi (5), dan row valid
                if (currentUser != null && currentUser.getRole().equals("user") && col == 5 && row >= 0) {
                    Object status = table.getValueAt(row, 4);
                    if (status != null && "Ditemukan".equalsIgnoreCase(status.toString().trim())) {
                        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
                        return;
                    }
                }
                table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void resetFilterButtons() {
        for(Component c : panelFilterBar.getComponents()) {
            if(c instanceof StyleUtil.FilterButton) {
                StyleUtil.FilterButton fb = (StyleUtil.FilterButton) c;
                if(fb.getText().equals("Semua")) fb.setActive(true);
                else fb.setActive(false);
            }
        }
    }

    // LOGIKA GANTI ROLE (ADMIN/USER) 
    private void setupLayoutByRole() {
        boolean isUser = currentUser.getRole().equals("user");

        // Setup Table Model sesuai role. Jika user, tambahkan kolom aksi (klaim WA)
        String[] cols = isUser ? Const.COLS_USER : Const.COLS_ADMIN;
        tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; }};
        table.setModel(tableModel);

        // Set Custom Renderer untuk badge Status & tombol Aksi
        table.getColumnModel().getColumn(4).setCellRenderer(new StyleUtil.StatusBadgeRenderer());
        if (isUser) {
            table.getColumnModel().getColumn(5).setCellRenderer(new StyleUtil.AksiButtonRenderer());
        }

        aturLebarKolom();

        panelFormInput.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 0.5;

        if (isUser) {
            // Tampilan Form USER (Simpel, tanpa edit Status)
            lblFormTitleUser.setVisible(true);
            btnToggleLapor.setVisible(true);
            gbc.gridy=0; gbc.gridx=0; panelFormInput.add(StyleUtil.createFieldLabel(Const.LBL_NAMA), gbc);
            gbc.gridx=1; gbc.insets=new Insets(0,15,0,0); panelFormInput.add(StyleUtil.createFieldLabel(Const.LBL_KATEGORI), gbc);
            gbc.gridy=1; gbc.insets=new Insets(5,0,15,0); gbc.gridx=0; panelFormInput.add(txtInputNama, gbc);
            gbc.gridx=1; gbc.insets=new Insets(5,15,15,0); panelFormInput.add(cmbKategori, gbc);
            gbc.gridy=2; gbc.insets=new Insets(0,0,0,0); gbc.gridwidth=2; gbc.gridx=0; panelFormInput.add(StyleUtil.createFieldLabel(Const.LBL_LOKASI), gbc);
            gbc.gridy=3; gbc.insets=new Insets(5,0,20,0); panelFormInput.add(txtInputLokasi, gbc);
            gbc.gridy=4; gbc.insets=new Insets(0,0,0,0); panelFormInput.add(panelBtnUser, gbc);
            toggleUserForm(false); // Default sembunyikan form lapor
        } else {
            // Tampilan Form ADMIN (Full Control, bisa edit Status)
            lblFormTitleUser.setVisible(false);
            btnToggleLapor.setVisible(false);
            panelFormCard.setVisible(true); // Form selalu terlihat
            gbc.gridy=0; gbc.gridx=0; panelFormInput.add(StyleUtil.createFieldLabel(Const.LBL_NAMA), gbc);
            gbc.gridx=1; gbc.insets=new Insets(0,15,0,0); panelFormInput.add(StyleUtil.createFieldLabel(Const.LBL_KATEGORI), gbc);
            gbc.gridy=1; gbc.insets=new Insets(5,0,15,0); gbc.gridx=0; panelFormInput.add(txtInputNama, gbc);
            gbc.gridx=1; gbc.insets=new Insets(5,15,15,0); panelFormInput.add(cmbKategori, gbc);
            gbc.gridy=2; gbc.insets=new Insets(0,0,0,0); gbc.gridx=0; panelFormInput.add(StyleUtil.createFieldLabel(Const.LBL_LOKASI), gbc);
            gbc.gridx=1; gbc.insets=new Insets(0,15,0,0); panelFormInput.add(StyleUtil.createFieldLabel(Const.LBL_STATUS), gbc);
            gbc.gridy=3; gbc.insets=new Insets(5,0,20,0); gbc.gridx=0; panelFormInput.add(txtInputLokasi, gbc);
            gbc.gridx=1; gbc.insets=new Insets(5,15,20,0); panelFormInput.add(cmbStatus, gbc);
            gbc.gridy=4; gbc.gridx=0; gbc.gridwidth=2; gbc.insets=new Insets(0,0,0,0); panelFormInput.add(panelBtnAdmin, gbc);
        }
        panelFormInput.revalidate(); panelFormInput.repaint();
        clearForm();
    }

    // Mengatur lebar kolom secara spesifik agar tampilan tabel rapi.
    private void aturLebarKolom() {
        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(40); cm.getColumn(0).setMinWidth(40); cm.getColumn(0).setMaxWidth(80);
        cm.getColumn(1).setPreferredWidth(220); cm.getColumn(1).setMinWidth(140); cm.getColumn(1).setMaxWidth(280);
        cm.getColumn(2).setPreferredWidth(80); cm.getColumn(2).setMinWidth(60); cm.getColumn(2).setMaxWidth(120);
        cm.getColumn(3).setPreferredWidth(180);
        cm.getColumn(4).setPreferredWidth(160); cm.getColumn(4).setMinWidth(140); cm.getColumn(4).setMaxWidth(180);
        if (currentUser.getRole().equals("user")) {
            cm.getColumn(5).setPreferredWidth(160); cm.getColumn(5).setMinWidth(140); cm.getColumn(5).setMaxWidth(180);
        }
    }

    // Mengambil data dari DAO dan memasukkannya ke tabel (Refresh)
    private void refreshTable() {
        if(tableModel == null) return;
        tableModel.setRowCount(0);
        List<Barang> data = barangDAO.getAll(txtSearch.getText(), activeFilterCategory);
        for(Barang b : data) {
            if(currentUser.getRole().equals("user")) {
                tableModel.addRow(new Object[]{b.getId(), b.getNama(), b.getKategori(), b.getLokasi(), b.getStatus(), ""});
            } else {
                tableModel.addRow(new Object[]{b.getId(), b.getNama(), b.getKategori(), b.getLokasi(), b.getStatus()});
            }
        }
    }

    // Mengisi form input dari data baris tabel yang diklik (Untuk Edit)
    private void loadDataToForm(int row) {
        try {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            txtInputId.setText(String.valueOf(id));
            txtInputNama.setText(table.getValueAt(row, 1).toString());
            cmbKategori.setSelectedItem(table.getValueAt(row, 2));
            txtInputLokasi.setText(table.getValueAt(row, 3).toString());
            cmbStatus.setSelectedItem(table.getValueAt(row, 4));

            // Jika Admin, ubah tombol Simpan jadi Update
            if(currentUser.getRole().equals("admin")) {
                panelFormCard.setVisible(true);
                btnSimpan.setText(Const.TXT_BTN_UPDATE); btnSimpan.setCustomColor(StyleUtil.COL_BLUE_SEARCH);
            }
        } catch(Exception e) {}
    }

    // Logika Simpan Data (Create/Update)
    private void simpanData() {
        Barang b = new Barang(
                txtInputId.getText().isEmpty() ? 0 : Integer.parseInt(txtInputId.getText()),
                txtInputNama.getText(),
                (String)cmbKategori.getSelectedItem(),
                txtInputLokasi.getText(),
                currentUser.getRole().equals("admin") ? (String)cmbStatus.getSelectedItem() : "Hilang"
        );

        if(b.getNama().isEmpty() || b.getLokasi().isEmpty()) return;

        // Cek ID: Kosong = Insert Baru, Ada Isi = Update Data Lama
        if(txtInputId.getText().isEmpty()) {
            barangDAO.insert(b);
            JOptionPane.showMessageDialog(this, "Berhasil Disimpan!");
        } else {
            barangDAO.update(b);
            JOptionPane.showMessageDialog(this, "Berhasil Diupdate!");
        }
        clearForm(); refreshTable();
        if(currentUser.getRole().equals("user")) toggleUserForm(false);
    }

    private void hapusData() {
        if(txtInputId.getText().isEmpty()) return;
        barangDAO.delete(Integer.parseInt(txtInputId.getText()));
        clearForm(); refreshTable();
    }

    private void clearForm() {
        txtInputId.setText(""); txtInputNama.setText(""); txtInputLokasi.setText("");
        cmbKategori.setSelectedIndex(0); cmbStatus.setSelectedIndex(0);
        table.clearSelection();
        btnSimpan.setText(Const.TXT_BTN_SAVE); btnSimpan.setCustomColor(StyleUtil.COL_GREEN);
    }

    private void toggleUserForm(boolean show) {
        panelFormCard.setVisible(show);
        if(show) {
            btnToggleLapor.setText(Const.TXT_BTN_CANCEL_REPORT); btnToggleLapor.setCustomColor(StyleUtil.COL_RED);
            clearForm();
        } else {
            btnToggleLapor.setText(Const.TXT_BTN_SHOW_REPORT); btnToggleLapor.setCustomColor(Color.decode("#e67e22"));
        }
    }

    // Membuka link WhatsApp Web untuk klaim barang
    private void klaimWA(String nama) {
        try {
            JOptionPane.showMessageDialog(this, "Third Eye Finder akan membuka Whatsapp Web untuk mengirim pesan klaim barang kepada admin.");
            String encoded = URLEncoder.encode(nama, StandardCharsets.UTF_8.toString());
            Desktop.getDesktop().browse(new URI("https://wa.me/62812345678?text=Halo%20Admin,%20Saya%20ingin%20klaim%20barang:%20" + encoded));
        } catch(Exception e) { e.printStackTrace(); }
    }
}