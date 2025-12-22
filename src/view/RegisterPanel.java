package view;

import dao.UserDAO;
import util.StyleUtil;
import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public RegisterPanel(MainFrame frame) {
        this.mainFrame = frame;
        setupUI();
    }

    private void setupUI() {
        setBackground(StyleUtil.COL_BG_APP);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(360, 450));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JLabel title = new JLabel("REGISTER", SwingConstants.CENTER);
        title.setOpaque(true); title.setBackground(StyleUtil.COL_BLUE_SEARCH);
        title.setForeground(Color.WHITE); title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setPreferredSize(new Dimension(0, 50));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        txtUser = new JTextField(); StyleUtil.styleField(txtUser);
        txtPass = new JPasswordField(); StyleUtil.styleField(txtPass);
        StyleUtil.FlatButton btnDaftar = new StyleUtil.FlatButton("DAFTAR SEKARANG", StyleUtil.COL_BLUE_SEARCH);

        JButton btnLink = new JButton("Kembali ke Login");
        btnLink.setContentAreaFilled(false); btnLink.setBorderPainted(false); btnLink.setForeground(Color.GRAY);
        btnLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLink.addActionListener(e -> {
            txtUser.setText(""); txtPass.setText("");
            mainFrame.showCard("LOGIN");
        });

        gbc.gridy=0; gbc.insets=new Insets(0,0,5,0); form.add(new JLabel("Buat Username"), gbc);
        gbc.gridy=1; gbc.insets=new Insets(0,0,15,0); form.add(txtUser, gbc);
        gbc.gridy=2; gbc.insets=new Insets(0,0,5,0); form.add(new JLabel("Buat Password"), gbc);
        gbc.gridy=3; gbc.insets=new Insets(0,0,20,0); form.add(txtPass, gbc);
        gbc.gridy=4; gbc.insets=new Insets(0,0,10,0); form.add(btnDaftar, gbc);
        gbc.gridy=5; form.add(btnLink, gbc);

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

        card.add(title, BorderLayout.NORTH); card.add(form, BorderLayout.CENTER);
        add(card);
    }
}