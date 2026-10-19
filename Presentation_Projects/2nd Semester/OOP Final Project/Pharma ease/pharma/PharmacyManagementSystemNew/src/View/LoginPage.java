package View;

import javax.swing.*;
import Controller.UserController;
import Model.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("PharmaEase - Sign In");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout()); // Center everything
        setUndecorated(false);

        ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("bg4.jpg")); // make sure this exists
                                                                                              // in resources
        Image bgImage = bgIcon.getImage();

        BackgroundPanel backgroundPanel = new BackgroundPanel(bgImage, 1f, true); // full opacity
        backgroundPanel.setLayout(new GridBagLayout()); // to center mainPanel
        setContentPane(backgroundPanel);

        // Central Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(400, 400));
        mainPanel.setBackground(new Color(219, 252, 217, 200)); // slight transparency
        mainPanel.setLayout(null); // Manual positioning

        JLabel titleLabel = new JLabel("Sign In");
        titleLabel.setForeground(new Color(0, 150, 20));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(140, 20, 200, 50);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(40, 90, 100, 25);
        JTextField usernameField = new JTextField();
        usernameField.setBounds(120, 90, 200, 30);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(40, 140, 100, 25);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(120, 140, 200, 30);

        JButton loginBtn = new JButton("Sign In");
        loginBtn.setBounds(160, 190, 80, 30);
        loginBtn.setForeground(Color.white);
        loginBtn.setBackground(new Color(0, 180, 20));
        loginBtn.setBorder(null);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);

        JLabel signupMsg = new JLabel("Don't have an account:");
        signupMsg.setBounds(120, 230, 150, 30);
        signupMsg.setFont(new Font("Arial", Font.PLAIN, 10));

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBounds(210, 235, 80, 20);
        signupBtn.setFont(new Font("Arial", Font.BOLD, 11));
        signupBtn.setContentAreaFilled(false);
        signupBtn.setBorderPainted(false);
        signupBtn.setFocusPainted(false);
        signupBtn.setOpaque(false);
        signupBtn.setBackground(new Color(0, 0, 0, 0));
        signupBtn.setForeground(new Color(0, 102, 204)); // Blue link style
        signupBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add components
        mainPanel.add(titleLabel);
        mainPanel.add(userLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passLabel);
        mainPanel.add(passwordField);

        mainPanel.add(loginBtn);
        mainPanel.add(signupMsg);
        mainPanel.add(signupBtn);

        // Center the panel in JFrame
        add(mainPanel, new GridBagConstraints());

        // Login button logic
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName = usernameField.getText().trim();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars).trim();

                if (userName.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter credentials", "Missing Info",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (new UserController().validateLogin(userName, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful");
                    java.util.Arrays.fill(passwordChars, '0');

                    UserController controlUserRole = new UserController();
                    User user = controlUserRole.getUserByUsername(userName);

                    dispose(); // Close login page

                    if (user.getUserTypeId() == 1) {
                        new AdminDashboard(user);
                    } else if (user.getUserTypeId() == 2) {
                        new SellerDashBoard(user);
                    } else if (user.getUserTypeId() == 3) {
                        SwingUtilities.invokeLater(() -> {
                            try {
                                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            } catch (Exception f) {
                                f.printStackTrace();
                            }
                            new CustomerDashboard().setVisible(true);
                        });
                    } else {
                        JOptionPane.showMessageDialog(null, "User type not found");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Sign up button logic
        signupBtn.addActionListener(e -> {
            dispose();
            new SignUpPage();
        });

        setVisible(true);
    }
}