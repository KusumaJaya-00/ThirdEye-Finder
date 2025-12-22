package view;

import dao.UserDAO;
import model.User;
import util.StyleUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        setupUI();
    }

    private void setupUI() {
        setLayout(new GridLayout(1, 2));

        // ==========================================
        // 1. PANEL KIRI (BRANDING) - CENTERED
        // ==========================================
        JPanel leftPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(StyleUtil.COL_ORANGE_DECOR);
                g.fillRect(0, getHeight() - 8, getWidth(), 8);
            }
        };
        leftPanel.setBackground(StyleUtil.COL_HEADER_BG);

        // Konten Panel Kiri (Menggunakan BoxLayout di dalam GridBag agar center rapi)
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setOpaque(false);

        JLabel lblTitle = new JLabel("Third Eye Finder");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub1 = new JLabel("Sistem Manajemen Barang Hilang");
        sub1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub1.setForeground(new Color(200, 200, 200));
        sub1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub2 = new JLabel("& Temu yang Terintegrasi");
        sub2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub2.setForeground(new Color(200, 200, 200));
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
        // Penting: Tidak pakai setPreferredSize di container agar tidak bug hilang

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        // --- Header ---
        JLabel lblLog = new JLabel("Login Akun");
        lblLog.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLog.setForeground(StyleUtil.COL_HEADER_BG);
        formContainer.add(lblLog, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        gbc.insets = new Insets(0, 0, 30, 0);
        formContainer.add(sep, gbc);

        // --- Input User ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel lblU = new JLabel("Username");
        lblU.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblU.setForeground(Color.GRAY);
        formContainer.add(lblU, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtUser = new JTextField();
        StyleUtil.styleField(txtUser);
        txtUser.setPreferredSize(new Dimension(320, 35)); // Lebar Form fix 320px
        formContainer.add(txtUser, gbc);

        // --- Input Pass ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel lblP = new JLabel("Password");
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
        StyleUtil.FlatButton btnLogin = new StyleUtil.FlatButton("MASUK APLIKASI", StyleUtil.COL_GREEN);
        btnLogin.setPreferredSize(new Dimension(320, 45));
        formContainer.add(btnLogin, gbc);

        // --- Link Register ---
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        linkPanel.setBackground(Color.WHITE);

        JLabel txtAsk = new JLabel("Belum punya akun?");
        txtAsk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAsk.setForeground(Color.GRAY);

        JLabel txtLink = new JLabel("Daftar disini");
        txtLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtLink.setForeground(StyleUtil.COL_BLUE_SEARCH);
        txtLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        txtLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                txtUser.setText(""); txtPass.setText("");
                mainFrame.showCard("REGISTER");
            }
            public void mouseEntered(MouseEvent e) {
                txtLink.setForeground(StyleUtil.COL_BLUE_SEARCH.darker());
            }
            public void mouseExited(MouseEvent e) {
                txtLink.setForeground(StyleUtil.COL_BLUE_SEARCH);
            }
        });

        linkPanel.add(txtAsk); linkPanel.add(txtLink);
        formContainer.add(linkPanel, gbc);

        // Tambahkan formContainer ke rightPanel (Center Absolut)
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weightx = 1.0;
        gbcRight.weighty = 1.0;
        gbcRight.anchor = GridBagConstraints.CENTER; // Kunci ke tengah
        gbcRight.fill = GridBagConstraints.NONE;

        rightPanel.add(formContainer, gbcRight);

        // Action Logic (TIDAK DIUBAH)
        btnLogin.addActionListener(e -> {
            UserDAO dao = new UserDAO();
            User user = dao.login(txtUser.getText(), new String(txtPass.getPassword()));
            if(user != null) {
                txtUser.setText(""); txtPass.setText("");
                mainFrame.onLoginSuccess(user);
            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal! Cek Username/Password.");
            }
        });

        add(leftPanel);
        add(rightPanel);
    }
}