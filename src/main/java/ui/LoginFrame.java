package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import data.GymDatabase;

/**
 * Login screen. Validates against hardcoded credentials (admin / 1234). A wrong
 * attempt shows a small persistent message under whichever field is incorrect
 * (username, password, or both) and keeps what was typed. Press Enter to sign in.
 */
public class LoginFrame extends JFrame {
    private static final Color ERROR_COLOR = new Color(0xE5, 0x73, 0x73);
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1234";

    private RoundedTextField usernameField;
    private RoundedPasswordField passwordField;
    private JLabel usernameError;
    private JLabel passwordError;

    private GymDatabase database;

    public LoginFrame(GymDatabase database) {
        this.database = database;

        setTitle("Gym Management System - Login");
        setIconImages(AppStyle.createAppIcons()); // gold dumbbell taskbar icon
        setSize(470, 450);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(AppStyle.BACKGROUND_COLOR);
        setContentPane(background);

        // Centered card
        RoundedPanel card = new RoundedPanel(AppStyle.SIDEBAR_COLOR, 22);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(AppStyle.GOLD_COLOR, 22, 1, new java.awt.Insets(0, 0, 0, 0)),
                BorderFactory.createEmptyBorder(28, 32, 28, 32)));
        card.setPreferredSize(new Dimension(360, 360));

        JLabel titleLabel = new JLabel("Gym Management");
        titleLabel.setFont(AppStyle.TITLE_FONT);
        titleLabel.setForeground(AppStyle.GOLD_COLOR);
        titleLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sign in to continue");
        subtitleLabel.setFont(AppStyle.LABEL_FONT);
        subtitleLabel.setForeground(AppStyle.SUB_TEXT_COLOR);
        subtitleLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);

        JLabel usernameLabel = AppStyle.createFormLabel("Username");
        usernameLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        usernameField = new RoundedTextField();
        usernameField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        usernameError = createErrorLabel();

        JLabel passwordLabel = AppStyle.createFormLabel("Password");
        passwordLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        passwordField = new RoundedPasswordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        passwordError = createErrorLabel();

        // Password field + eye toggle in one row
        JPanel passwordRow = new JPanel(new BorderLayout(8, 0));
        passwordRow.setOpaque(false);
        passwordRow.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        passwordRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        passwordRow.add(passwordField, BorderLayout.CENTER);
        passwordRow.add(new PasswordToggleButton(passwordField), BorderLayout.EAST);

        // Golden, full-width login button (Enter still works too)
        RoundedButton loginButton = new RoundedButton("Login",
                AppStyle.GOLD_COLOR, new Color(0xFF, 0xD8, 0x7C), new Color(0xE2, 0xB0, 0x42));
        loginButton.setForeground(AppStyle.DARK_TEXT_COLOR);
        loginButton.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(160, 36));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        loginButton.addActionListener(e -> validateLogin());

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(3));
        card.add(usernameError);
        card.add(Box.createVerticalStrut(12));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordRow);
        card.add(Box.createVerticalStrut(3));
        card.add(passwordError);
        card.add(Box.createVerticalStrut(18));
        card.add(loginButton);

        background.add(card);

        // Enter on username jumps to password; Enter on password logs in.
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> validateLogin());
    }

    private JLabel createErrorLabel() {
        JLabel label = new JLabel(" "); // a space reserves the line so nothing shifts
        label.setFont(new Font(AppStyle.FONT_FAMILY, Font.PLAIN, 12));
        label.setForeground(ERROR_COLOR);
        label.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        return label;
    }

    private void validateLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        boolean usernameOk = username.equals(USERNAME);
        boolean passwordOk = password.equals(PASSWORD);

        if (usernameOk && passwordOk) {
            // Valid login -> hand the shared database to ui.DashboardFrame and close this window.
            DashboardFrame dashboardFrame = new DashboardFrame(database);
            dashboardFrame.setVisible(true);
            dispose();
            return;
        }

        // Flag only the field(s) that are wrong; keep what the user typed.
        usernameError.setText(usernameOk ? " " : "Incorrect Username");
        passwordError.setText(passwordOk ? " " : "Incorrect Password");
    }
}
