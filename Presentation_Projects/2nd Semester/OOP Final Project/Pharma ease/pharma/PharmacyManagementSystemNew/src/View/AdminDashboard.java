package View;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import com.sun.jdi.connect.spi.Connection;

import Controller.AdminController;
import Controller.CustomerController;
import Controller.DBConnection;
import Controller.MedicineController;
import Controller.OrderController;
import Controller.SellerController;
import Controller.UserController;
import Model.Admin;
import Model.Customer;
import Model.Medicine;
import Model.Order;
import Model.OrderDetail;
import Model.Seller;
import Model.User;
import java.util.List;
import java.util.ArrayList;

public class AdminDashboard extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField contactField;
    private JTextField emailField;
    private JTextField addressField;

    private JButton editProfileButton, saveProfileButton;

    private static final Color COLOR_MAIN_BACKGROUND = new Color(245, 245, 245);
    private static final Color COLOR_SIDEBAR_BACKGROUND = new Color(34, 40, 49);
    private static final Color COLOR_SIDEBAR_BUTTON = new Color(48, 71, 94);
    private static final Color COLOR_SIDEBAR_BUTTON_HOVER = new Color(68, 91, 114);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_DARK_TEXT = new Color(34, 40, 49);
    private static final Font FONT_SIDEBAR_BUTTON = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FONT_WELCOME_TEXT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font FONT_WELCOME_SUBTEXT = new Font("Segoe UI", Font.PLAIN, 16);

    private User user;
    private Admin admin;
    private Seller seller;

    public AdminDashboard(User user) {

        this.user = user;
        admin = new Admin();
        admin = new AdminController().getAdminByUserID(user.getUserId());

        seller = new Seller();
        seller = new SellerController().getSellerByUserId(user.getUserId());

        setupFrame();
        createSidebar();
        createPanels();
        setVisible(true);
    }

    private void setupFrame() {
        setTitle("Pharmacy Management System");
        setSize(1280, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_MAIN_BACKGROUND);
        setLayout(new BorderLayout());
    }

    private void createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(COLOR_SIDEBAR_BACKGROUND);
        sidebar.setPreferredSize(new Dimension(240, getHeight()));
        sidebar.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("<html><div style='text-align:center; color:#FFFFFF; padding:10px;'>"
                + "<span style='font-size:20px; font-weight:bold;'>WELCOME</span><br>"
                + "<span style='font-size:16px;'>ADMIN PANEL</span></div></html>");
        welcomeLabel.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, new Color(50, 56, 69)),
                new EmptyBorder(20, 10, 20, 10)));
        sidebar.add(welcomeLabel, BorderLayout.NORTH);

        String[] buttons = { "Dashboard", "Profile", "Sellers", "Customers", "Medicines", "Logout" };
        JPanel btnPanel = new JPanel(new GridLayout(buttons.length, 1, 0, 10));
        btnPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        btnPanel.setBackground(COLOR_SIDEBAR_BACKGROUND);

        for (String btnText : buttons) {
            JButton btn = createNavButton(btnText);
            btn.addActionListener(e -> handleNavigation(btnText));
            btnPanel.add(btn);
        }

        sidebar.add(btnPanel, BorderLayout.CENTER);
        add(sidebar, BorderLayout.WEST);
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(COLOR_WHITE);
        btn.setBackground(COLOR_SIDEBAR_BUTTON);
        btn.setFont(FONT_SIDEBAR_BUTTON);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBorder(new CompoundBorder(
                new LineBorder(new Color(70, 80, 90), 1),
                new EmptyBorder(10, 15, 10, 15)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            Color originalBg = btn.getBackground();

            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_SIDEBAR_BUTTON_HOVER);
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(originalBg);
            }
        });
        return btn;
    }

    private void createPanels() {
        mainPanel.setBackground(COLOR_MAIN_BACKGROUND);
        mainPanel.add(createHomePanel(), "Dashboard");
        mainPanel.add(createProfilePanel(), "Profile");
        mainPanel.add(createViewPanel("Sellers"), "Sellers");
        mainPanel.add(createViewPanel("Customers"), "Customers");
        mainPanel.add(createMedicinesPanel(), "Medicines");
        mainPanel.add(createOrdersPanel(), "Orders");

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 245, 250));

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setOpaque(false);
        addStatsCard(statsPanel, "Total Sellers", "" + new SellerController().totalSeller(), new Color(26, 188, 156),
                "seller_icon.png");
        addStatsCard(statsPanel, "Total Customers", "" + new CustomerController().totalCustomer(),
                new Color(52, 152, 219), "/images/customer_icon.png");
        addStatsCard(statsPanel, "Total Medicines", "" + new MedicineController().totalMedicines(),
                new Color(155, 89, 182), "/images/medicine_icon.png");
        panel.add(statsPanel, BorderLayout.NORTH);

        JPanel centerContentPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        centerContentPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        centerContentPanel.setOpaque(false);

        centerContentPanel.add(createColorfulInfoCard("Recent Sales Activity",
                "View detailed sales reports, analyze trends, and monitor daily transactions for the past week.",
                new Color(232, 245, 253), new Color(40, 60, 80)));
        centerContentPanel.add(createColorfulInfoCard("Stock Alerts",
                "Low stock items: Paracetamol (10 units), Amoxicillin (5 units). 3 items expiring soon. Consider reordering.",
                new Color(253, 237, 237), new Color(80, 40, 40)));
        centerContentPanel.add(createColorfulInfoCard("User Management",
                "Quick access to add new sellers, view customer details, or manage user roles and permissions. 2 pending approvals.",
                new Color(237, 253, 238), new Color(40, 80, 40)));
        centerContentPanel.add(createColorfulInfoCard("System Notifications",
                "Last system backup: Today at 2:00 AM. All services are running optimally. Scheduled maintenance next Sunday.",
                new Color(254, 245, 231), new Color(80, 60, 40)));
        panel.add(centerContentPanel, BorderLayout.CENTER);

        JPanel quickActionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        quickActionsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Quick Actions",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 18), COLOR_DARK_TEXT));
        quickActionsPanel.setBackground(new Color(220, 225, 235));

        JButton addSellerBtn = new JButton("Add New Seller");
        JButton addMedicineBtn = new JButton("Add New Medicine");
        JButton viewOrdersBtn = new JButton("View Orders");

        addSellerBtn.addActionListener(e -> openAddSellerDialog());
        addMedicineBtn.addActionListener(e -> openAddMedicineDialog());
        viewOrdersBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "Orders");
        });

        styleQuickActionButton(addSellerBtn);
        styleQuickActionButton(addMedicineBtn);
        styleQuickActionButton(viewOrdersBtn);

        quickActionsPanel.add(addSellerBtn);
        quickActionsPanel.add(addMedicineBtn);
        quickActionsPanel.add(viewOrdersBtn);
        panel.add(quickActionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_MAIN_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Orders");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(COLOR_DARK_TEXT);
        panel.add(title, BorderLayout.NORTH);

        String[] columnNames = { "Order ID", "Customer", "Medicine", "Quantity", "Total", "Date" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Assuming you have a method to get DB connection
        java.sql.Connection conn = DBConnection.getConnection();
        OrderController orderController = new OrderController(conn);

        // Fetch orders
        List<OrderDetail> details = orderController.getOrderDetails();
        System.out.println("Fetched rows: " + details.size()); // Debugging line
        for (OrderDetail d : details) {
            model.addRow(new Object[] {
                    d.getOrderId(),
                    d.getCustomerName(),
                    d.getMedicineName(),
                    d.getQuantity(),
                    d.getTotalAmount(),
                    d.getOrderDate()
            });
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void styleQuickActionButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(78, 101, 124));
        button.setForeground(COLOR_WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            Color originalBg = button.getBackground();

            public void mouseEntered(MouseEvent e) {
                button.setBackground(originalBg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
    }

    private JPanel createColorfulInfoCard(String title, String content, Color bgColor, Color textColor) {
        JPanel cardPanel = new JPanel(new BorderLayout(10, 10));
        cardPanel.setBackground(bgColor);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                new EmptyBorder(20, 25, 20, 25)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(textColor);
        cardPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setForeground(textColor.darker());
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setOpaque(false);
        contentArea.setEditable(false);
        contentArea.setFocusable(false);
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        cardPanel.add(scrollPane, BorderLayout.CENTER);

        return cardPanel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));
        panel.setBackground(new Color(248, 249, 250));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(230, 240, 255));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 210, 220)));

        JLabel headerLabel = new JLabel("Admin Profile Management");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(COLOR_DARK_TEXT);
        headerLabel.setIcon(loadImageIcon("/images/profile_settings_icon.png", 32, 32));
        headerLabel.setIconTextGap(10);
        headerPanel.add(headerLabel);
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Personal Information",
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 16), new Color(50, 50, 50)),
                new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        String[] allFieldLabels = { "Admin ID:", "Full Name:", "Username:", "Role:", "Contact:", "Email:", "Address:" };
        String[] allFieldValues = { "" + admin.getUserId(), admin.getFirstName() + " " + admin.getLastName(),
                "" + user.getUsername(), "" + admin.getRole(), "" + user.getPhone(), user.getEmail(),
                "" + user.getAddress() };

        for (int i = 0; i < allFieldLabels.length; i++) {
            JLabel currentLabel = new JLabel(allFieldLabels[i]);
            currentLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            gbc.fill = GridBagConstraints.NONE;
            formPanel.add(currentLabel, gbc);

            JTextField currentTextField = new JTextField(allFieldValues[i], 25);
            currentTextField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            currentTextField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 200)),
                    new EmptyBorder(5, 8, 5, 8)));

            if (allFieldLabels[i].equals("Admin ID:") || allFieldLabels[i].equals("Role:")) {
                currentTextField.setEditable(false);
                currentTextField.setBackground(new Color(230, 235, 240));
                currentTextField.setToolTipText("This field cannot be changed.");
            } else {
                currentTextField.setEditable(false);
                currentTextField.setBackground(new Color(240, 245, 250));
                if (allFieldLabels[i].equals("Full Name:")) {
                    fullNameField = currentTextField;
                } else if (allFieldLabels[i].equals("Username:")) {
                    usernameField = currentTextField;
                } else if (allFieldLabels[i].equals("Contact:")) {
                    contactField = currentTextField;
                } else if (allFieldLabels[i].equals("Email:")) {
                    emailField = currentTextField;
                } else if (allFieldLabels[i].equals("Address:")) {
                    addressField = currentTextField;
                }
            }
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            formPanel.add(currentTextField, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = allFieldLabels.length;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        formPanel.add(Box.createVerticalGlue(), gbc);

        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(formScrollPane, BorderLayout.CENTER);

        JPanel southOuterPanel = new JPanel(new BorderLayout(0, 10));
        southOuterPanel.setOpaque(false);
        southOuterPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        editProfileButton = new JButton("Edit Profile");
        styleProfileButton(editProfileButton);
        editProfileButton.setIcon(loadImageIcon("/images/edit_icon.png", 16, 16));

        saveProfileButton = new JButton("Save Changes");
        styleProfileButton(saveProfileButton);
        saveProfileButton.setIcon(loadImageIcon("/images/save_icon.png", 16, 16));
        saveProfileButton.setEnabled(false);

        editProfileButton.addActionListener(e -> toggleProfileEditState(true));

        saveProfileButton.addActionListener(e -> {
            toggleProfileEditState(false);
            if (!new UserController().usernameExists(usernameField.getText().trim())) {
                user.setUsername(usernameField.getText().trim());
                user.setAddress(addressField.getText());
                user.setEmail(emailField.getText());
                user.setPhone(contactField.getText());

                String fullName = fullNameField.getText().trim();

                String firstName = "";
                String lastName = "";

                if (!fullName.isEmpty()) {
                    String[] parts = fullName.split("\\s+");

                    if (parts.length >= 2) {
                        firstName = parts[0];
                        lastName = parts[parts.length - 1];
                    } else {

                        firstName = fullName;
                        lastName = "";
                    }
                }

                admin.setFirstName(firstName);
                admin.setLastName(lastName);

                new AdminController().updateAdmin(admin);
                new UserController().updateUser(user);

                JOptionPane.showMessageDialog(this,
                        "Profile updated successfully!", "Update Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Username already exists. Please try a different one.", "Username Taken",
                        JOptionPane.WARNING_MESSAGE);

            }

        });

        buttonPanel.add(editProfileButton);
        buttonPanel.add(saveProfileButton);
        southOuterPanel.add(buttonPanel, BorderLayout.NORTH);

        JPanel footerInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerInfoPanel.setOpaque(false);
        footerInfoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                new EmptyBorder(10, 0, 5, 0)));
        JLabel lastLoginLabel = new JLabel("Last Login: Today at 09:30 AM  |  Profile Last Updated: 2023-10-26");
        lastLoginLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lastLoginLabel.setForeground(Color.DARK_GRAY);
        footerInfoPanel.add(lastLoginLabel);
        southOuterPanel.add(footerInfoPanel, BorderLayout.CENTER);

        panel.add(southOuterPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void toggleProfileEditState(boolean editable) {
        if (fullNameField != null) {
            fullNameField.setEditable(editable);
            fullNameField.setBackground(editable ? Color.WHITE : new Color(240, 245, 250));
        }
        if (usernameField != null) {
            usernameField.setEditable(editable);
            usernameField.setBackground(editable ? Color.WHITE : new Color(240, 245, 250));
        }
        if (contactField != null) {
            contactField.setEditable(editable);
            contactField.setBackground(editable ? Color.WHITE : new Color(240, 245, 250));
        }
        if (emailField != null) {
            emailField.setEditable(editable);
            emailField.setBackground(editable ? Color.WHITE : new Color(240, 245, 250));
        }
        if (addressField != null) {
            addressField.setEditable(editable);
            addressField.setBackground(editable ? Color.WHITE : new Color(240, 245, 250));
        }

        saveProfileButton.setEnabled(editable);
        editProfileButton.setEnabled(!editable);
    }

    private void styleProfileButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(COLOR_WHITE);
        button.setBackground(new Color(52, 152, 219));
        button.setBorder(new EmptyBorder(8, 18, 8, 18));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            Color originalBg = button.getBackground();

            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled())
                    button.setBackground(originalBg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                if (button.isEnabled())
                    button.setBackground(originalBg);
            }
        });
    }

    private JPanel createViewPanel(String type) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_MAIN_BACKGROUND);

        String[] columns = { "ID", "FirstName", "LastName", "E-mail", "Phone", "Pharmacy Name", "License Number" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        SellerController sellerController = new SellerController();
        List<Seller> sellerList = sellerController.getAllSellers();

        for (Seller seller : sellerList) {
            model.addRow(new Object[] {
                    seller.getSellerId(),
                    seller.getFirstName(),
                    seller.getLastName(),
                    seller.getEmail(),
                    seller.getPhone(),
                    seller.getPharmacyName(),
                    seller.getLicenseNumber()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 235, 240));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        TitledBorder titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(), type + " List");
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        titledBorder.setTitleColor(COLOR_DARK_TEXT);
        scrollPane.setBorder(titledBorder);

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionButtonPanel.setBackground(COLOR_MAIN_BACKGROUND);

        // Create a final local copy of 'type' to use inside lambda
        final String panelType = type;

        JButton addBtn = new JButton("Add New " + panelType.substring(0, panelType.length() - 1));
        styleQuickActionButton(addBtn);

        // Add action listener to open the add dialog based on panelType
        // ...
        addBtn.addActionListener(e -> {
            switch (panelType) {
                case "Sellers":
                    openAddSellerDialog();
                    break;
                case "Customers":
                    openAddCustomerDialog();
                    break;
                case "Medicines":
                    openAddMedicineDialog();
                    break;
                default:
                    JOptionPane.showMessageDialog(panel, "Add action not implemented for " + panelType);
                    break;
            }
        });

        JButton refreshBtn = new JButton("⟳ Refresh");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBackground(new Color(39, 174, 96));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setBorder(new EmptyBorder(10, 20, 10, 20));
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0);
            if (panelType.equals("Sellers")) {
                SellerController sc = new SellerController();
                for (Seller s : sc.getAllSellers()) {
                    model.addRow(new Object[] { s.getSellerId(), s.getFirstName(), s.getLastName(), s.getEmail(),
                            s.getPhone(), s.getPharmacyName(), s.getLicenseNumber() });
                }
            } else if (panelType.equals("Customers")) {
                CustomerController cc = new CustomerController();
                for (Customer c : cc.getAllCustomers()) {
                    model.addRow(new Object[] { c.getUserId(), c.getFirstName(), c.getLastName(), c.getEmail(),
                            c.getPhone() });
                }
            }
        });
        actionButtonPanel.add(refreshBtn);

        actionButtonPanel.add(addBtn);
        panel.add(actionButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void openAddSellerDialog() {
        JDialog dialog = new JDialog(this, "Add New Seller", true);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField pharmacyNameField = new JTextField(20);
        JTextField licenseField = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.add(new JLabel("First Name:"));
        form.add(firstNameField);
        form.add(new JLabel("Last Name:"));
        form.add(lastNameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);
        form.add(new JLabel("Username:"));
        form.add(usernameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(new JLabel("Pharmacy Name:"));
        form.add(pharmacyNameField);
        form.add(new JLabel("License Number:"));
        form.add(licenseField);

        panel.add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String pharmacyName = pharmacyNameField.getText().trim();
            String licenseNumber = licenseField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create user account first
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setAddress("");

            UserController userController = new UserController();
            boolean userCreated = userController.createUser(newUser, "Seller");

            if (!userCreated) {
                JOptionPane.showMessageDialog(dialog, "Failed to create user. Username may already exist.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Now create seller linked to that user
            User createdUser = userController.getUserByUsername(username);
            Seller newSeller = new Seller();
            newSeller.setUserId(createdUser.getUserId());
            newSeller.setFirstName(firstName);
            newSeller.setLastName(lastName);
            newSeller.setPharmacyName(pharmacyName);
            newSeller.setLicenseNumber(licenseNumber);

            boolean sellerCreated = new SellerController().addSeller(newSeller);
            if (sellerCreated) {
                JOptionPane.showMessageDialog(dialog, "Seller added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save seller details.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openAddMedicineDialog() {
        JDialog dialog = new JDialog(this, "Add New Medicine", true);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField manufacturerField = new JTextField(20);
        JCheckBox prescriptionCheck = new JCheckBox("Requires Prescription");
        JTextField priceField = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Description:"));
        form.add(descriptionField);
        form.add(new JLabel("Manufacturer:"));
        form.add(manufacturerField);
        form.add(new JLabel("Requires Prescription:"));
        form.add(prescriptionCheck);
        form.add(new JLabel("Unit Price:"));
        form.add(priceField);

        panel.add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String manufacturer = manufacturerField.getText().trim();
            boolean requiresPrescription = prescriptionCheck.isSelected();
            String priceText = priceField.getText().trim();

            if (name.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and Price are required.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            BigDecimal price;
            try {
                price = new BigDecimal(priceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Price must be a valid number.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Medicine newMed = new Medicine();
            newMed.setName(name);
            newMed.setDescription(description);
            newMed.setManufacturer(manufacturer);
            newMed.setRequiresPrescription(requiresPrescription);
            newMed.setUnitPrice(price);

            new MedicineController().addMedicine(newMed); // Make sure you have this method

            JOptionPane.showMessageDialog(dialog, "Medicine added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();

            // Refresh medicines table if needed
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openAddCustomerDialog() {
        // Similar dialog for customer add form, depending on your Customer model fields
        JDialog dialog = new JDialog(this, "Add New Customer", true);
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField phoneField = new JTextField(20);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.add(new JLabel("First Name:"));
        form.add(firstNameField);
        form.add(new JLabel("Last Name:"));
        form.add(lastNameField);
        form.add(new JLabel("Email:"));
        form.add(emailField);
        form.add(new JLabel("Phone:"));
        form.add(phoneField);

        panel.add(form, BorderLayout.CENTER);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter First and Last name.", "Validation Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Customer newCustomer = new Customer();
            newCustomer.setFirstName(firstName);
            newCustomer.setLastName(lastName);
            newCustomer.setEmail(email);
            newCustomer.setPhone(phone);

            boolean success = new CustomerController().createCustomer(newCustomer);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Customer added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                // Optionally refresh customer list/table here
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(dialog, "Customer added successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();

            // Refresh customers table if you have one
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createMedicinesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(COLOR_MAIN_BACKGROUND);

        String[] columns = { "ID", "Name", "Description", "Manufacture", "requires_prescription", "unit_price" };
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        MedicineController medicine = new MedicineController();
        List<Medicine> medicines = medicine.getAllMedicines();

        for (Medicine med : medicines) {
            model.addRow(new Object[] {
                    "MED-" + String.format("%03d", med.getMedicineId()),
                    med.getName(), med.getDescription(), med.getManufacturer(), med.isRequiresPrescription(),
                    med.getUnitPrice()
            });
        }

        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(230, 235, 240));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollPane = new JScrollPane(table);
        TitledBorder titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(), "Medicines List");
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        titledBorder.setTitleColor(COLOR_DARK_TEXT);
        scrollPane.setBorder(titledBorder);

        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionButtonPanel.setBackground(COLOR_MAIN_BACKGROUND);

        JButton addMedBtn = new JButton("Add New Medicine");
        styleQuickActionButton(addMedBtn);
        addMedBtn.addActionListener(e -> openAddMedicineDialog()); // <-- Here

        actionButtonPanel.add(addMedBtn);
        panel.add(actionButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private ImageIcon loadImageIcon(String path, int width, int height) {
        try {
            java.net.URL imgURL = getClass().getResource(path);
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            } else {
                System.err.println("Couldn't find image file: " + path + " (Is it in src/images/ folder?)");
            }
        } catch (Exception e) {
            System.err.println("Error loading image icon: " + path + " - " + e.getMessage());
        }
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = placeholder.createGraphics();
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawRect(0, 0, width - 1, height - 1);
        g2d.setFont(new Font("Arial", Font.PLAIN, Math.min(10, height / 2)));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "N/A";
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, (width - textWidth) / 2, (height - fm.getHeight()) / 2 + fm.getAscent());
        g2d.dispose();
        return new ImageIcon(placeholder);
    }

    private void addStatsCard(JPanel container, String title, String value, Color bg, String iconPath) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(bg);
        card.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(bg.darker(), 1),
                new EmptyBorder(20, 20, 20, 20)));

        if (iconPath != null && !iconPath.isEmpty()) {
            JLabel iconLabel = new JLabel(loadImageIcon(iconPath, 48, 48));
            iconLabel.setVerticalAlignment(SwingConstants.CENTER);
            card.add(iconLabel, BorderLayout.WEST);
        }

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(COLOR_WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(COLOR_WHITE);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(8));
        textPanel.add(valueLabel);

        card.add(textPanel, BorderLayout.CENTER);
        container.add(card);
    }

    private void handleNavigation(String command) {
        if ("Logout".equals(command)) {
            int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginPage();
            }
        } else {
            cardLayout.show(mainPanel, command);
        }
    }
}