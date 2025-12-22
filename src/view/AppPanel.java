package view;

import dao.BarangDAO;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.*;
import model.Barang;
import model.User;
import util.StyleUtil;

public class AppPanel extends JPanel {
    private MainFrame mainFrame;
    private BarangDAO barangDAO;
    private User currentUser;
    private String activeFilterCategory = "Semua";

    // UI
    private JTextField txtInputId, txtInputNama, txtInputLokasi, txtSearch;
    private JComboBox<String> cmbKategori, cmbStatus;
    private StyleUtil.FlatButton btnSimpan, btnHapus, btnClear, btnKirimLaporan, btnBatalLaporan, btnToggleLapor;
    private JPanel panelFormCard, panelFormInput, panelBtnAdmin, panelBtnUser, panelFilterBar;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblWelcome, lblRoleSubtitle, lblFormTitleUser;

    public AppPanel(MainFrame frame) {
        this.mainFrame = frame;
        this.barangDAO = new BarangDAO();
        setLayout(new BorderLayout());
        setupUI();
    }

    public void initSession(User user) {
        this.currentUser = user;
        lblRoleSubtitle.setText("Selamat Datang, " + user.getUsername() + "!");
        setupLayoutByRole();

        // Reset Filter Visual saat login baru
        activeFilterCategory = "Semua";
        resetFilterButtons(); // Helper method baru
        refreshTable();
    }

    private void setupUI() {
        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(StyleUtil.COL_HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,3,0, new Color(243, 156, 18)),
                new EmptyBorder(15, 25, 15, 25)));

        JPanel headText = new JPanel(new GridLayout(2,1)); headText.setOpaque(false);
        lblWelcome = new JLabel("Third Eye Finder");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 18)); lblWelcome.setForeground(Color.WHITE);
        lblRoleSubtitle = new JLabel("Selamat Datang");
        lblRoleSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12)); lblRoleSubtitle.setForeground(new Color(200,200,200));
        headText.add(lblWelcome); headText.add(lblRoleSubtitle);

        StyleUtil.FlatButton btnLogout = new StyleUtil.FlatButton("Keluar / Logout", StyleUtil.COL_RED);
        btnLogout.setPreferredSize(new Dimension(150, 30)); btnLogout.setMinimumSize(new Dimension(150, 30));
        btnLogout.addActionListener(e -> mainFrame.showCard("LOGIN"));
        header.add(headText, BorderLayout.CENTER); header.add(btnLogout, BorderLayout.EAST);

        // --- BODY ---
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(StyleUtil.COL_BG_APP);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        // WRAPPER FORM
        JPanel formWrapper = new JPanel();
        formWrapper.setLayout(new BoxLayout(formWrapper, BoxLayout.Y_AXIS));
        formWrapper.setOpaque(false);

        btnToggleLapor = new StyleUtil.FlatButton("BATALKAN LAPORAN", StyleUtil.COL_RED);
        btnToggleLapor.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnToggleLapor.setMaximumSize(new Dimension(Integer.MAX_VALUE, StyleUtil.COMP_HEIGHT));
        btnToggleLapor.setPreferredSize(new Dimension(200, StyleUtil.COMP_HEIGHT));

        panelFormCard = new JPanel(new BorderLayout());
        panelFormCard.setBackground(Color.WHITE);
        panelFormCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220,220,220), 1),
                new EmptyBorder(20, 20, 20, 20)));

        lblFormTitleUser = new JLabel("LAPORAN KEHILANGAN BARANG");
        lblFormTitleUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFormTitleUser.setForeground(StyleUtil.COL_ORANGE_TEXT);
        lblFormTitleUser.setBorder(new EmptyBorder(0,0,15,0));

        panelFormInput = new JPanel(new GridBagLayout());
        panelFormInput.setOpaque(false);

        txtInputId = new JTextField();
        txtInputNama = new JTextField(); StyleUtil.styleField(txtInputNama);
        txtInputLokasi = new JTextField(); StyleUtil.styleField(txtInputLokasi);
        cmbKategori = new JComboBox<>(new String[]{"Elektronik", "Dokumen", "Pribadi", "Kendaraan", "Lain-lain"});
        StyleUtil.styleField(cmbKategori);
        cmbStatus = new JComboBox<>(new String[]{"Hilang", "Ditemukan", "Dikembalikan"});
        StyleUtil.styleField(cmbStatus);

        panelBtnAdmin = new JPanel(new GridLayout(1, 3, 15, 0)); panelBtnAdmin.setOpaque(false);
        btnSimpan = new StyleUtil.FlatButton("SIMPAN BARU", StyleUtil.COL_GREEN);
        btnHapus = new StyleUtil.FlatButton("HAPUS", StyleUtil.COL_RED);
        btnClear = new StyleUtil.FlatButton("BATAL", StyleUtil.COL_GREY_BTN);
        panelBtnAdmin.add(btnSimpan); panelBtnAdmin.add(btnHapus); panelBtnAdmin.add(btnClear);

        panelBtnUser = new JPanel(new GridLayout(1, 2, 15, 0)); panelBtnUser.setOpaque(false);
        btnKirimLaporan = new StyleUtil.FlatButton("KIRIM LAPORAN", StyleUtil.COL_GREEN);
        btnBatalLaporan = new StyleUtil.FlatButton("BATAL", StyleUtil.COL_GREY_BTN);
        panelBtnUser.add(btnKirimLaporan); panelBtnUser.add(btnBatalLaporan);

        JPanel topForm = new JPanel(new BorderLayout()); topForm.setOpaque(false);
        topForm.add(lblFormTitleUser, BorderLayout.NORTH);
        topForm.add(panelFormInput, BorderLayout.CENTER);
        panelFormCard.add(topForm, BorderLayout.CENTER);

        formWrapper.add(btnToggleLapor);
        formWrapper.add(Box.createVerticalStrut(10));
        formWrapper.add(panelFormCard);

        // --- FILTER & SEARCH ---
        JPanel toolsPanel = new JPanel(new BorderLayout(0, 10));
        toolsPanel.setOpaque(false); toolsPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        panelFilterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelFilterBar.setOpaque(false);
        String[] cats = {"Semua", "Elektronik", "Dokumen", "Pribadi", "Kendaraan", "Lain-lain"};

        for(String cat : cats) {
            StyleUtil.FilterButton fb = new StyleUtil.FilterButton(cat);
            if(cat.equals("Semua")) fb.setActive(true); 

            fb.addActionListener(e -> {
                for(Component c : panelFilterBar.getComponents()) {
                    if(c instanceof StyleUtil.FilterButton) {
                        ((StyleUtil.FilterButton)c).setActive(false);
                    }
                }
                fb.setActive(true);
                activeFilterCategory = cat;
                refreshTable();
            });
            panelFilterBar.add(fb);
        }

        txtSearch = new JTextField(); StyleUtil.styleField(txtSearch);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StyleUtil.COL_BLUE_SEARCH, 1),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)));

        toolsPanel.add(panelFilterBar, BorderLayout.NORTH);
        toolsPanel.add(txtSearch, BorderLayout.CENTER);

        // --- TABLE ---
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(StyleUtil.COL_TABLE_HEADER);
        titleBar.setBorder(new EmptyBorder(10, 15, 10, 15));
        JLabel t1 = new JLabel("Daftar Barang"); t1.setForeground(Color.WHITE); t1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleBar.add(t1, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new String[]{"ID", "Nama Barang", "Kategori", "Lokasi", "Status"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(45); table.setShowVerticalLines(false);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(Color.WHITE); scroll.setBorder(BorderFactory.createEmptyBorder());
        tableCard.add(titleBar, BorderLayout.NORTH); tableCard.add(scroll, BorderLayout.CENTER);

        // --- ADD TO MAIN ---
        mainContent.add(formWrapper);
        mainContent.add(toolsPanel);
        mainContent.add(Box.createVerticalStrut(10));
        mainContent.add(tableCard);

        add(header, BorderLayout.NORTH); add(mainContent, BorderLayout.CENTER);

        // --- LISTENERS ---
        btnSimpan.addActionListener(e -> simpanData());
        btnHapus.addActionListener(e -> hapusData());
        btnClear.addActionListener(e -> clearForm());
        btnKirimLaporan.addActionListener(e -> simpanData());
        btnBatalLaporan.addActionListener(e -> toggleUserForm(false));
        btnToggleLapor.addActionListener(e -> toggleUserForm(!panelFormCard.isVisible()));

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshTable(); }
            public void removeUpdate(DocumentEvent e) { refreshTable(); }
            public void changedUpdate(DocumentEvent e) { refreshTable(); }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    loadDataToForm(row);
                    if (currentUser.getRole().equals("user") && col == 5) { // WA Claim
                        Object status = table.getValueAt(row, 4);
                        if(status != null && "Ditemukan".equals(status.toString())) {
                            klaimWA(table.getValueAt(row, 1).toString());
                        }
                    }
                }
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

    private void setupLayoutByRole() {
        boolean isUser = currentUser.getRole().equals("user");

        if (isUser) {
            String[] cols = {"ID", "Nama Barang", "Kategori", "Lokasi", "Status", "Aksi"};
            tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; }};
            table.setModel(tableModel);
            table.getColumnModel().getColumn(5).setCellRenderer(new AksiButtonRenderer());
        } else {
            String[] cols = {"ID", "Nama Barang", "Kategori", "Lokasi", "Status"};
            tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; }};
            table.setModel(tableModel);
        }
        table.getColumnModel().getColumn(4).setCellRenderer(new StyleUtil.StatusBadgeRenderer());

        panelFormInput.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 0.5;

        if (isUser) {
            lblFormTitleUser.setVisible(true);
            btnToggleLapor.setVisible(true);
            gbc.gridy=0; gbc.gridx=0; panelFormInput.add(new JLabel("Nama Barang"), gbc);
            gbc.gridx=1; gbc.insets=new Insets(0,15,0,0); panelFormInput.add(new JLabel("Kategori"), gbc);
            gbc.gridy=1; gbc.insets=new Insets(5,0,15,0); gbc.gridx=0; panelFormInput.add(txtInputNama, gbc);
            gbc.gridx=1; gbc.insets=new Insets(5,15,15,0); panelFormInput.add(cmbKategori, gbc);
            gbc.gridy=2; gbc.insets=new Insets(0,0,0,0); gbc.gridwidth=2; gbc.gridx=0; panelFormInput.add(new JLabel("Lokasi"), gbc);
            gbc.gridy=3; gbc.insets=new Insets(5,0,20,0); panelFormInput.add(txtInputLokasi, gbc);
            gbc.gridy=4; gbc.insets=new Insets(0,0,0,0); panelFormInput.add(panelBtnUser, gbc);
            toggleUserForm(false);
        } else {
            lblFormTitleUser.setVisible(false);
            btnToggleLapor.setVisible(false);
            panelFormCard.setVisible(true);
            gbc.gridy=0; gbc.gridx=0; panelFormInput.add(new JLabel("Nama Barang"), gbc);
            gbc.gridx=1; gbc.insets=new Insets(0,15,0,0); panelFormInput.add(new JLabel("Kategori"), gbc);
            gbc.gridy=1; gbc.insets=new Insets(5,0,15,0); gbc.gridx=0; panelFormInput.add(txtInputNama, gbc);
            gbc.gridx=1; gbc.insets=new Insets(5,15,15,0); panelFormInput.add(cmbKategori, gbc);
            gbc.gridy=2; gbc.insets=new Insets(0,0,0,0); gbc.gridx=0; panelFormInput.add(new JLabel("Lokasi"), gbc);
            gbc.gridx=1; gbc.insets=new Insets(0,15,0,0); panelFormInput.add(new JLabel("Status"), gbc);
            gbc.gridy=3; gbc.insets=new Insets(5,0,20,0); gbc.gridx=0; panelFormInput.add(txtInputLokasi, gbc);
            gbc.gridx=1; gbc.insets=new Insets(5,15,20,0); panelFormInput.add(cmbStatus, gbc);
            gbc.gridy=4; gbc.gridx=0; gbc.gridwidth=2; gbc.insets=new Insets(0,0,0,0); panelFormInput.add(panelBtnAdmin, gbc);
        }
        panelFormInput.revalidate(); panelFormInput.repaint();
        clearForm();
    }

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

    private void loadDataToForm(int row) {
        try {
            int id = Integer.parseInt(table.getValueAt(row, 0).toString());
            txtInputId.setText(String.valueOf(id));
            txtInputNama.setText(table.getValueAt(row, 1).toString());
            cmbKategori.setSelectedItem(table.getValueAt(row, 2));
            txtInputLokasi.setText(table.getValueAt(row, 3).toString());
            cmbStatus.setSelectedItem(table.getValueAt(row, 4));

            if(currentUser.getRole().equals("admin")) {
                panelFormCard.setVisible(true);
                btnSimpan.setText("UPDATE DATA"); btnSimpan.setCustomColor(StyleUtil.COL_BLUE_SEARCH);
            }
        } catch(Exception e) {}
    }

    private void simpanData() {
        Barang b = new Barang(
                txtInputId.getText().isEmpty() ? 0 : Integer.parseInt(txtInputId.getText()),
                txtInputNama.getText(),
                (String)cmbKategori.getSelectedItem(),
                txtInputLokasi.getText(),
                currentUser.getRole().equals("admin") ? (String)cmbStatus.getSelectedItem() : "Hilang"
        );

        if(b.getNama().isEmpty() || b.getLokasi().isEmpty()) return;

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
        btnSimpan.setText("SIMPAN BARU"); btnSimpan.setCustomColor(StyleUtil.COL_GREEN);
    }

    private void toggleUserForm(boolean show) {
        panelFormCard.setVisible(show);
        if(show) {
            btnToggleLapor.setText("BATALKAN LAPORAN"); btnToggleLapor.setCustomColor(StyleUtil.COL_RED);
            clearForm();
        } else {
            btnToggleLapor.setText("LAPOR BARANG HILANG"); btnToggleLapor.setCustomColor(Color.decode("#e67e22"));
        }
    }

    private void klaimWA(String nama) {
        try {
            String encoded = URLEncoder.encode(nama, StandardCharsets.UTF_8.toString());
            Desktop.getDesktop().browse(new URI("https://wa.me/62812345678?text=Halo%20Admin,%20Saya%20ingin%20klaim%20barang:%20" + encoded));
        } catch(Exception e) { e.printStackTrace(); }
    }

    class AksiButtonRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            String status = (String) table.getValueAt(row, 4);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
            p.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            if ("Ditemukan".equals(status)) {
                JButton btn = new JButton("Klaim WA");
                btn.setBackground(StyleUtil.COL_WA); btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
                btn.setBorderPainted(false); btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(90, 25));
                p.add(btn);
            }
            return p;
        }
    }
}