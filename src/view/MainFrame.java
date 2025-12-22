package view;

import model.User;
import util.Const;
import util.StyleUtil;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContainer;

    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private AppPanel appPanel;

    public MainFrame() {
        setTitle(Const.APP_NAME);
        setSize(1000, 850);
        setMinimumSize(new Dimension(850, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        StyleUtil.setGlobalFont();

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        appPanel = new AppPanel(this);

        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(registerPanel, "REGISTER");
        mainContainer.add(appPanel, "APP");

        add(mainContainer);
    }

    public void showCard(String name) { cardLayout.show(mainContainer, name); }

    public void onLoginSuccess(User user) {
        appPanel.initSession(user);
        showCard("APP");
    }
}