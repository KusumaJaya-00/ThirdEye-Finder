package view;

import dao.UserDAO;
import model.User;
import util.Const;
import util.StyleUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginPanel adalah halaman pertama yang muncul saat aplikasi dijalankan.
 * Berfungsi untuk autentikasi user (Login) sebelum masuk ke dashboard.
 * Layout dibagi menjadi dua: Panel Kiri (Branding) dan Panel Kanan (Form).
 */

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginPanel(MainFrame frame) {
        this.mainFrame = frame;
        setupUI();
    }

    private void setupUI() {
        // Layout utama dibagi 2 kolom: Kiri (Branding) & Kanan (Form)
        setLayout(new GridLayout(1, 2));

        // 1. PANEL KIRI (BRANDING)
        // Menggunakan GridBagLayout default agar konten di dalamnya otomatis di tengah
        JPanel leftPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(StyleUtil.COL_ORANGE_DECOR);
                g.fillRect(0, getHeight() - 8, getWidth(), 8);
            }
        };
        leftPanel.setBackground(StyleUtil.COL_HEADER_BG);

        // Container untuk teks branding (disusun vertikal dengan BoxLayout)
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        leftContent.setOpaque(false);

        // Judul Besar Aplikasi
        JLabel lblTitle = new JLabel(Const.APP_NAME);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sub-judul baris 1
        JLabel sub1 = new JLabel(Const.APP_DESC_1);
        sub1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub1.setForeground(new Color(200, 200, 200));
        sub1.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Sub-judul baris 2
        JLabel sub2 = new JLabel(Const.APP_DESC_2);
        sub2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        sub2.setForeground(new Color(200, 200, 200));
        sub2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Menambahkan komponen ke container kiri dengan jarak (strut)
        leftContent.add(lblTitle);
        leftContent.add(Box.createVerticalStrut(15));
        leftContent.add(sub1);
        leftContent.add(sub2);

        // Masukkan konten ke panel kiri (otomatis center karena GridBagLayout)
        leftPanel.add(leftContent);

        // 2. PANEL KANAN (FORM INPUT)
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        JPanel formContainer = new JPanel(new GridBagLayout());
        formContainer.setBackground(Color.WHITE);

        // Konfigurasi layout (GridBagConstraints) untuk form
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;

        // Header Form 
        JLabel lblLog = StyleUtil.createTitleLabel(Const.TITLE_LOGIN, StyleUtil.COL_HEADER_BG);
        formContainer.add(lblLog, gbc);

        gbc.gridy++;
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(230, 230, 230));
        gbc.insets = new Insets(0, 0, 30, 0);
        formContainer.add(sep, gbc);

        // Input Username 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formContainer.add(StyleUtil.createFieldLabel(Const.LBL_USERNAME), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        txtUser = new JTextField();
        StyleUtil.styleField(txtUser);
        txtUser.setPreferredSize(new Dimension(320, 35));
        formContainer.add(txtUser, gbc);

        // Input Password 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        formContainer.add(StyleUtil.createFieldLabel(Const.LBL_PASSWORD), gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        txtPass = new JPasswordField();
        StyleUtil.styleField(txtPass);
        txtPass.setPreferredSize(new Dimension(320, 35));
        formContainer.add(txtPass, gbc);

        // Button Login 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        StyleUtil.FlatButton btnLogin = new StyleUtil.FlatButton(Const.BTN_LOGIN, StyleUtil.COL_GREEN);
        btnLogin.setPreferredSize(new Dimension(320, 45));
        formContainer.add(btnLogin, gbc);

        // Link Register 
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);

        // Panel kecil untuk menampung teks "Belum punya akun? Daftar disini"
        JPanel linkPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        linkPanel.setBackground(Color.WHITE);

        JLabel txtAsk = StyleUtil.createSubtitleLabel(Const.TXT_NO_ACCOUNT);
        txtAsk.setForeground(Color.GRAY);

        JLabel txtLink = new JLabel(Const.LINK_REGISTER);
        txtLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtLink.setForeground(StyleUtil.COL_BLUE_SEARCH);
        txtLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Interaksi klik pada link "Daftar disini"
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

        // Menempatkan Form Container di Tengah Panel Kanan 
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.weightx = 1.0;
        gbcRight.weighty = 1.0;
        gbcRight.anchor = GridBagConstraints.CENTER;
        gbcRight.fill = GridBagConstraints.NONE;

        rightPanel.add(formContainer, gbcRight);

        // Logika Action Tombol Login 
        btnLogin.addActionListener(e -> {
            UserDAO dao = new UserDAO();
            // Cek kredensial ke database
            User user = dao.login(txtUser.getText(), new String(txtPass.getPassword()));
            if(user != null) {
                txtUser.setText(""); txtPass.setText("");
                mainFrame.onLoginSuccess(user);
            } else {
                JOptionPane.showMessageDialog(this, Const.MSG_LOGIN_FAIL);
            }
        });

        add(leftPanel);
        add(rightPanel);
    }
}