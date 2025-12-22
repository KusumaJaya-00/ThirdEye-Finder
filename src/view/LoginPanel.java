package view;

import dao.UserDAO;
import model.User;
import util.StyleUtil;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginPanel(MainFrame frame) {
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

        JLabel title = new JLabel("LOGIN", SwingConstants.CENTER);
        title.setOpaque(true); title.setBackground(StyleUtil.COL_HEADER_BG);
        title.setForeground(Color.WHITE); title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setPreferredSize(new Dimension(0, 50));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;

        txtUser = new JTextField(); StyleUtil.styleField(txtUser);
        txtPass = new JPasswordField(); StyleUtil.styleField(txtPass);
        StyleUtil.FlatButton btnLogin = new StyleUtil.FlatButton("MASUK APLIKASI", StyleUtil.COL_GREEN);

        JButton btnLink = new JButton("Belum punya akun? Daftar disini.");
        btnLink.setContentAreaFilled(false); btnLink.setBorderPainted(false); btnLink.setForeground(StyleUtil.COL_BLUE_SEARCH);
        btnLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLink.addActionListener(e -> {
            txtUser.setText(""); txtPass.setText("");
            mainFrame.showCard("REGISTER");
        });

        gbc.gridy=0; form.add(new JLabel("Username"), gbc);
        gbc.gridy=1; gbc.insets=new Insets(0,0,15,0); form.add(txtUser, gbc);
        gbc.gridy=2; gbc.insets=new Insets(0,0,5,0); form.add(new JLabel("Password"), gbc);
        gbc.gridy=3; gbc.insets=new Insets(0,0,20,0); form.add(txtPass, gbc);
        gbc.gridy=4; gbc.insets=new Insets(0,0,10,0); form.add(btnLogin, gbc);
        gbc.gridy=5; form.add(btnLink, gbc);

        btnLogin.addActionListener(e -> {
            UserDAO dao = new UserDAO();
            User user = dao.login(txtUser.getText(), new String(txtPass.getPassword()));
            if(user != null) {
                txtUser.setText(""); txtPass.setText("");
                mainFrame.onLoginSuccess(user);
            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal! Username/Password salah.");
            }
        });

        card.add(title, BorderLayout.NORTH); card.add(form, BorderLayout.CENTER);
        add(card);
    }
}