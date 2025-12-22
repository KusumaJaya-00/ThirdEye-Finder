package view;

import dao.UserDAO;
import util.Const;
import util.StyleUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * RegisterPanel adalah halaman pendaftaran akun baru.
 * Layout-nya didesain simetris dengan LoginPanel:
 * - Panel Kiri: Branding & Info
 * - Panel Kanan: Form Input User & Password
 */

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        setupUI();
    }

    private void setupUI() {
        // Layout utama dibagi menjadi 2 kolom (Kiri & Kanan)
        setLayout(new GridLayout(1, 2));

        // 1. PANEL KIRI (BRANDING)
        // Menggunakan GridBagLayout agar konten di dalamnya otomatis rata tengah (Center)
        JPanel leftPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(StyleUtil.COL_ORANGE_DECOR);
                g.fillRect(0, getHeight() - 8, getWidth(), 8);
            }
        };
        leftPanel.setBackground(StyleUtil.COL_BLUE_PANEL);

        // Kontainer Panel Kiri
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setOpaque(false);

        // Judul & Deskripsi
        JLabel lblTitle = new JLabel(Const.HEADER_REGISTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub1 = new JLabel(Const.APP_DESC_REG_1);
        sub1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub1.setForeground(new Color(220, 220, 220));
        sub1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub2 = new JLabel(Const.APP_DESC_REG_2);
        sub2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub2.setForeground(new Color(220, 220, 220));
        sub2.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftContent.add(lblTitle);
        leftContent.add(Box.createVerticalStrut(15));
        leftContent.add(sub1);
        leftContent.add(sub2);

        leftPanel.add(leftContent);

        // 2. PANEL KANAN (FORM INPUT)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        // Kontainer Form
        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0); // Jarak antar elemen vertikal
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        // Header 
        JLabel lblReg = StyleUtil.createTitleLabel(Const.TITLE_REGISTER, StyleUtil.COL_BLUE_PANEL);
        formContainer.add(lblReg, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        gbc.insets = new Insets(0, 0, 30, 0);
        formContainer.add(sep, gbc);

        // Label Input User 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formContainer.add(StyleUtil.createFieldLabel("Buat " + Const.LBL_USERNAME), gbc);

        // Input User 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtUser = new JTextField();
        StyleUtil.styleField(txtUser);
        txtUser.setPreferredSize(new Dimension(320, 35));
        formContainer.add(txtUser, gbc);

        // Label Input Password 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formContainer.add(StyleUtil.createFieldLabel("Buat " + Const.LBL_PASSWORD), gbc);

        // Input Password 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        txtPass = new JPasswordField();
        StyleUtil.styleField(txtPass);
        txtPass.setPreferredSize(new Dimension(320, 35));
        formContainer.add(txtPass, gbc);

        // Button Register 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        StyleUtil.FlatButton btnDaftar = new StyleUtil.FlatButton(Const.BTN_REGISTER, StyleUtil.COL_BLUE_PANEL);
        btnDaftar.setPreferredSize(new Dimension(320, 45));
        formContainer.add(btnDaftar, gbc);

        // Link kembali ke Login 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        linkPanel.setBackground(Color.WHITE);

        JLabel txtBack = StyleUtil.createSubtitleLabel(Const.TXT_HAVE_ACCOUNT);
        txtBack.setForeground(Color.GRAY);

        JLabel txtLink = new JLabel(Const.LINK_LOGIN);
        txtLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtLink.setForeground(StyleUtil.COL_BLUE_SEARCH);
        txtLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Navigasi kembali ke halaman Login
        txtLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                txtUser.setText(""); txtPass.setText("");
                mainFrame.showCard("LOGIN");
            }
        });

        linkPanel.add(txtBack); linkPanel.add(txtLink);
        formContainer.add(linkPanel, gbc);


        // Tambahkan formContainer ke rightPanel
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weightx = 1.0;
        gbcRight.weighty = 1.0;
        gbcRight.anchor = GridBagConstraints.CENTER;
        gbcRight.fill = GridBagConstraints.NONE;

        rightPanel.add(formContainer, gbcRight);

        // Logika Action Register 
        btnDaftar.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword()).trim();

            // Validasi input kosong
            if(u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, Const.MSG_EMPTY); return;
            }

            // Proses register ke database
            UserDAO dao = new UserDAO();
            if(dao.register(u, p)) {
                JOptionPane.showMessageDialog(this, Const.MSG_REG_SUCCESS);
                txtUser.setText(""); txtPass.setText("");
                mainFrame.showCard("LOGIN");
            } else {
                JOptionPane.showMessageDialog(this, Const.MSG_REG_FAIL);
            }
        });

        add(leftPanel);
        add(rightPanel);
    }
}