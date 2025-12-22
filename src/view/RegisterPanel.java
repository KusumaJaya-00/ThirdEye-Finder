package view;

import dao.UserDAO;
import util.StyleUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridLayout(1, 2));

        // ==========================================
        // 1. PANEL KIRI (BRANDING BLUE) - CENTERED
        // ==========================================
        JPanel leftPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(StyleUtil.COL_ORANGE_DECOR);
                g.fillRect(0, getHeight() - 8, getWidth(), 8);
            }
        };
        leftPanel.setBackground(StyleUtil.COL_BLUE_SEARCH);

        // Konten Panel Kiri
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setOpaque(false);

        JLabel lblTitle = new JLabel("Bergabunglah");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub1 = new JLabel("Buat akun untuk melaporkan");
        sub1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub1.setForeground(new Color(220, 220, 220));
        sub1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub2 = new JLabel("atau mengklaim barang.");
        sub2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub2.setForeground(new Color(220, 220, 220));
        sub2.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftContent.add(lblTitle);
        leftContent.add(Box.createVerticalStrut(15));
        leftContent.add(sub1);
        leftContent.add(sub2);

        leftPanel.add(leftContent);

        // ==========================================
        // 2. PANEL KANAN (FORM INPUT) - FIXED CENTER
        // ==========================================
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        // Form Container
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);
        // FIX: Jangan set tinggi 0. Set width fix, tinggi biarkan flow (atau set angka aman).
        // Kita gunakan width 320, tinggi -1 (biarkan layout manager atur)
        // Cara paling aman: setPreferredSize hanya untuk lebar, atau biarkan konten.
        // Disini saya hapus setPreferredSize container dan atur ukuran komponen didalamnya saja.

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Biar komponen isi lebar container

        // --- Header ---
        JLabel lblReg = new JLabel("Registrasi");
        lblReg.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblReg.setForeground(StyleUtil.COL_BLUE_SEARCH);
        formContainer.add(lblReg, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        gbc.insets = new Insets(0, 0, 30, 0);
        formContainer.add(sep, gbc);

        // --- Input User ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel lblU = new JLabel("Buat Username");
        lblU.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblU.setForeground(Color.GRAY);
        formContainer.add(lblU, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtUser = new JTextField();
        StyleUtil.styleField(txtUser);
        txtUser.setPreferredSize(new Dimension(320, 35)); // Lebar Form ditentukan disini
        formContainer.add(txtUser, gbc);

        // --- Input Pass ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel lblP = new JLabel("Buat Password");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblP.setForeground(Color.GRAY);
        formContainer.add(lblP, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        txtPass = new JPasswordField();
        StyleUtil.styleField(txtPass);
        txtPass.setPreferredSize(new Dimension(320, 35));
        formContainer.add(txtPass, gbc);

        // --- Button ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        StyleUtil.FlatButton btnDaftar = new StyleUtil.FlatButton("DAFTAR SEKARANG", StyleUtil.COL_BLUE_SEARCH);
        btnDaftar.setPreferredSize(new Dimension(320, 45));
        formContainer.add(btnDaftar, gbc);

        // --- Link Login ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        linkPanel.setBackground(Color.WHITE);

        JLabel txtBack = new JLabel("Kembali ke halaman");
        txtBack.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBack.setForeground(Color.GRAY);

        JLabel txtLink = new JLabel("Login");
        txtLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtLink.setForeground(StyleUtil.COL_GREY_BTN);
        txtLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        txtLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                txtUser.setText(""); txtPass.setText("");
                mainFrame.showCard("LOGIN");
            }
        });

        linkPanel.add(txtBack); linkPanel.add(txtLink);
        formContainer.add(linkPanel, gbc);


        // Tambahkan formContainer ke rightPanel (Posisi Center Absolut)
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weightx = 1.0;
        gbcRight.weighty = 1.0;
        gbcRight.anchor = GridBagConstraints.CENTER; // Kunci ke tengah
        gbcRight.fill = GridBagConstraints.NONE; // Jangan stretch container

        rightPanel.add(formContainer, gbcRight);

        // Action Listener Logic
        btnDaftar.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword()).trim();
            if(u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!"); return;
            }
            UserDAO dao = new UserDAO();
            if(dao.register(u, p)) {
                JOptionPane.showMessageDialog(this, "Registrasi Berhasil!");
                txtUser.setText(""); txtPass.setText("");
                mainFrame.showCard("LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, "Username sudah terdaftar!");
            }
        });

        add(leftPanel);
        add(rightPanel);
    }
}