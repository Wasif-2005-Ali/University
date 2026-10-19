package View;

import java.awt.*;
import java.awt.event.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.Border;

import com.toedter.calendar.JDateChooser;
import Controller.UserController;
import Model.Customer;
import Model.Seller;
import Model.User;

public class SignUpPage extends JFrame {

    public SignUpPage() {
        setTitle("PharmaEase - Sign Up");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(null); // only if you're manually positioning mainPanel
        // getContentPane().setBackground(Color.BLACK);

        ImageIcon bgIcon = new ImageIcon(getClass().getClassLoader().getResource("signupbg.jpg")); // make sure this
                                                                                                   // exists in
                                                                                                   // resources
        Image bgImage = bgIcon.getImage();

        BackgroundPanel backgroundPanel = new BackgroundPanel(bgImage, 0.9f, true); // full opacity
        setContentPane(backgroundPanel);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        mainPanel.setBackground(new Color(222, 252, 243));
        backgroundPanel.add(mainPanel);

        backgroundPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = backgroundPanel.getWidth();
                int h = backgroundPanel.getHeight();
                int panelW = 450;
                int panelH = Math.min(750, h - 40);
                mainPanel.setBounds(w - panelW - 40, (h - panelH) / 2, panelW, panelH);
                backgroundPanel.revalidate();
                backgroundPanel.repaint();
            }
        });
        mainPanel.setBounds(760, 70, 450, 750);

        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setForeground(new Color(138, 200, 170));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 45));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;

        JTextField firstNameField = new JTextField(20);
        addField(mainPanel, gbc, "First Name:", firstNameField, 1);

        JTextField lastNameField = new JTextField(20);
        addField(mainPanel, gbc, "Last Name:", lastNameField, 2);

        JTextField usernameField = new JTextField(20);
        addField(mainPanel, gbc, "Username:", usernameField, 3);

        JPasswordField passwordField = new JPasswordField(20);
        addField(mainPanel, gbc, "Password:", passwordField, 4);

        JTextField emailField = new JTextField(20);
        addField(mainPanel, gbc, "E-mail:", emailField, 5);

        JDateChooser birthDateChooser = new JDateChooser();
        birthDateChooser.setPreferredSize(new Dimension(200, 30));
        addField(mainPanel, gbc, "Birth Date:", birthDateChooser, 6);

        JTextField phoneField = new JTextField(20);
        addField(mainPanel, gbc, "Phone No:", phoneField, 7);

        JTextField addressField = new JTextField(20);
        addField(mainPanel, gbc, "Address:", addressField, 8);

        JTextField pharmacyNameField = new JTextField(20);
        JLabel pharmacyLabel = new JLabel("Pharmacy Name:");
        addField(mainPanel, gbc, pharmacyLabel, pharmacyNameField, 9);

        JTextField licenseField = new JTextField(20);
        JLabel licenseLabel = new JLabel("License No:");
        addField(mainPanel, gbc, licenseLabel, licenseField, 10);

        JComboBox<String> roleBox = new JComboBox<>(new String[] { "Customer", "Seller" });
        addField(mainPanel, gbc, "Role:", roleBox, 11);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBackground(new Color(138, 200, 190));
        registerBtn.setFocusPainted(false);
        registerBtn.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(registerBtn, gbc);

        JLabel loginMsg = new JLabel("Already have an account:");
        JButton signInBtn = new JButton("← Back to Login");
        signInBtn.setBorderPainted(true);
        signInBtn.setContentAreaFilled(true);
        signInBtn.setFocusPainted(false);
        signInBtn.setBackground(new Color(52, 152, 219));
        signInBtn.setForeground(Color.WHITE);
        signInBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signInBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(230, 252, 243, 0));
        loginPanel.add(loginMsg);
        loginPanel.add(signInBtn);

        gbc.gridy = 13;
        mainPanel.add(loginPanel, gbc);

        pharmacyLabel.setVisible(false);
        pharmacyNameField.setVisible(false);
        licenseLabel.setVisible(false);
        licenseField.setVisible(false);

        roleBox.addActionListener(e -> {
            boolean isSeller = roleBox.getSelectedItem().equals("Seller");
            pharmacyLabel.setVisible(isSeller);
            pharmacyNameField.setVisible(isSeller);
            licenseLabel.setVisible(isSeller);
            licenseField.setVisible(isSeller);
        });

        roleBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, false, false);
                c.setBackground(new Color(255, 255, 255));
                c.setForeground(new Color(0, 0, 0));
                return c;
            }
        });
        roleBox.setBackground(new Color(222, 252, 243)); // light teal background
        roleBox.setForeground(new Color(50, 80, 70));

        registerBtn.addActionListener(e -> {
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars).trim();
            String passwordRegex = "^(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";

            if (firstNameField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() ||
                    usernameField.getText().trim().isEmpty() ||
                    password.isEmpty() ||
                    emailField.getText().trim().isEmpty() ||
                    phoneField.getText().trim().isEmpty() ||
                    addressField.getText().trim().isEmpty() ||
                    birthDateChooser.getDate() == null ||
                    (roleBox.getSelectedItem().equals("Seller") &&
                            (pharmacyNameField.getText().trim().isEmpty()
                                    || licenseField.getText().trim().isEmpty()))) {

                JOptionPane.showMessageDialog(null, "Please fill all required fields.");
                return;
            }

            if (!password.matches(passwordRegex)) {
                JOptionPane.showMessageDialog(null,
                        "Password must be at least 8 characters long,\ninclude one uppercase letter and one special character.",
                        "Weak Password", JOptionPane.WARNING_MESSAGE);
                return;
            }

            UserController userController = new UserController();
            User user = new User();
            user.setUsername(usernameField.getText());
            user.setPassword(password);
            user.setEmail(emailField.getText());
            user.setPhone(phoneField.getText());
            user.setAddress(addressField.getText());

            if (userController.usernameExists(usernameField.getText())) {
                JOptionPane.showMessageDialog(null, "Username already taken. Please choose another.");
                return;
            }

            boolean userCreated = userController.createUser(user, roleBox.getSelectedItem().toString());
            if (!userCreated) {
                JOptionPane.showMessageDialog(null, "Failed to create base user.");
                return;
            }

            User createdUser = userController.getUserByUsername(usernameField.getText());
            if (createdUser == null) {
                JOptionPane.showMessageDialog(null, "Failed to retrieve user after creation.");
                return;
            }

            boolean roleSaved = false;
            String selectedRole = roleBox.getSelectedItem().toString();

            switch (selectedRole) {
                case "Customer":
                    Customer customer = new Customer();
                    customer.setUserId(createdUser.getUserId());
                    customer.setFirstName(firstNameField.getText());
                    customer.setLastName(lastNameField.getText());
                    Date utilDate = birthDateChooser.getDate();
                    String formattedDate = utilDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    customer.setDateOfBirth(formattedDate);
                    roleSaved = new Controller.CustomerController().createCustomer(customer);
                    break;

                case "Seller":
                    Seller seller = new Seller();
                    seller.setUserId(createdUser.getUserId());
                    seller.setFirstName(firstNameField.getText());
                    seller.setLastName(lastNameField.getText());
                    seller.setPharmacyName(pharmacyNameField.getText());
                    seller.setLicenseNumber(licenseField.getText());
                    roleSaved = new Controller.SellerController().addSeller(seller);
                    break;
            }

            if (roleSaved) {
                JOptionPane.showMessageDialog(null, "Registered successfully!");
                dispose();
                new LoginPage();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to save role-specific data.");
            }
        });

        signInBtn.addActionListener(e -> {
            dispose();
            new LoginPage();
        });

        // setContentPane(mainPanel);
        setVisible(true);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, JLabel label, JComponent field, int yPos) {
        gbc.gridx = 0;
        gbc.gridy = yPos;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    // public static void main(String[] args) {
    // new SignUpPage();
    // }
}
