package View;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.PageAttributes.ColorType;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainContentPanel;
    private JTextField customerIdField, custFirstNameField, custLastNameField, custDobField;
    private JTable medicinesTable;
    private DefaultTableModel medicinesTableModel;
    private JTextField searchMedicineField;
    private JTable ordersTable;
    private DefaultTableModel ordersTableModel;
    private JButton currentActiveSideButton = null;
    private JLabel[] dashboardValueLabels = new JLabel[4]; // For direct update of dashboard values

    // GREEN THEME COLOR SCHEME
    private static final Color C_PRIMARY_THEME = new Color(39, 174, 96);
    private static final Color C_SECONDARY_THEME = new Color(46, 204, 113);
    private static final Color C_ACCENT_THEME = new Color(243, 156, 18);

    private static final Color C_SUCCESS = C_PRIMARY_THEME;
    private static final Color C_DANGER = new Color(192, 57, 43);
    private static final Color C_WARNING = C_ACCENT_THEME;

    private static final Color C_BACKGROUND_MAIN = new Color(236, 240, 241);
    private static final Color C_BACKGROUND_SIDE_PANEL = new Color(44, 62, 80);
    private static final Color C_BACKGROUND_PANEL_CONTENT = Color.WHITE;

    private static final Color C_TEXT_ON_DARK_PRIMARY = Color.WHITE;
    private static final Color C_TEXT_ON_DARK_SECONDARY = new Color(189, 195, 199);

    private static final Color C_TEXT_ON_LIGHT_PRIMARY = new Color(44, 62, 80);
    private static final Color C_TEXT_ON_LIGHT_SECONDARY = new Color(127, 140, 141);

    private static final Color C_BORDER_LIGHT = new Color(210, 215, 211);
    private static final Color C_DISABLED_FIELD_BG = new Color(224, 224, 224);
    private static final Color C_TEXT_FIELD_BG = Color.WHITE;
    private static final Color C_TABLE_GRID = new Color(220, 220, 220);
    private static final Color C_TABLE_SELECTION_BG = new Color(160, 210, 180);
    private static final Color C_TABLE_SELECTION_FG = C_TEXT_ON_LIGHT_PRIMARY;
    private static final Color C_TABLE_ALT_ROW_BG = new Color(248, 252, 249);

    // Fonts
    private static final Font HEADING_FONT = new Font("Segoe UI", Font.BOLD, 32);
    private static final Font SUB_HEADING_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font BILL_FONT = new Font("Consolas", Font.PLAIN, 13);

    private static final String DEFAULT_CUSTOMER_ID = "CUST-001";

    // In-memory data stores
    private List<Object[]> allMedicinesData;
    private List<Object[]> allOrdersData;
    private List<List<Object[]>> allOrderItems = new ArrayList<>();

    public CustomerDashboard() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        allMedicinesData = new ArrayList<>();
        allOrdersData = new ArrayList<>();

        initializeFrame();
        createHeader();
        createSidePanel();
        initializeMainContentPanelAndCards();
        loadSampleData();
    }

    private void initializeFrame() {
        setTitle("PharmaCare - Customer Dashboard");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(C_BACKGROUND_MAIN);
    }

    private void createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(C_PRIMARY_THEME);
        headerPanel.setBorder(new EmptyBorder(15, 30, 15, 30));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        titlePanel.setOpaque(false);

        // Simplified Header Icon using nested JPanels for colors
        JPanel iconOuter = new JPanel(new GridBagLayout()); // To center the inner part
        iconOuter.setPreferredSize(new Dimension(40, 40));
        iconOuter.setBackground(C_TEXT_ON_DARK_PRIMARY); // Outer color, was C_TEXT_ON_DARK_PRIMARY
        iconOuter.setOpaque(true);

        JPanel iconInner = new JPanel();
        iconInner.setPreferredSize(new Dimension(20, 20));
        iconInner.setBackground(C_SECONDARY_THEME); // Inner color
        iconInner.setOpaque(true);
        iconOuter.add(iconInner);

        JLabel title = new JLabel("PHARMACARE");
        title.setFont(HEADING_FONT);
        title.setForeground(C_TEXT_ON_DARK_PRIMARY);
        title.setBorder(new EmptyBorder(0, 10, 0, 0));

        titlePanel.add(iconOuter);
        titlePanel.add(title);

        JLabel dateLabel = new JLabel(new SimpleDateFormat("EEE, MMM d yyyy | hh:mm a").format(new Date()));
        dateLabel.setFont(LABEL_FONT.deriveFont(Font.PLAIN, 15f));
        dateLabel.setForeground(new Color(220, 250, 220));

        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createSidePanel() {
        JPanel sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(260, getHeight()));
        sidePanel.setBackground(C_BACKGROUND_SIDE_PANEL);
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel menuTitleLabel = new JLabel("MAIN MENU");
        menuTitleLabel.setFont(SUB_HEADING_FONT.deriveFont(18f));
        menuTitleLabel.setForeground(C_TEXT_ON_DARK_SECONDARY);
        menuTitleLabel.setBorder(new EmptyBorder(20, 25, 25, 25));
        menuTitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidePanel.add(menuTitleLabel);

        String[] buttons = { "Dashboard", "Customer Profile", "Medicine Catalog", "Order Management", "Billing",
                "Exit" };
        for (int i = 0; i < buttons.length; i++) {
            JButton button = createMenuButton(buttons[i]);
            button.addActionListener(new NavigationListener());
            sidePanel.add(button);
            sidePanel.add(Box.createRigidArea(new Dimension(0, 3)));
            if (i == 0) { // Set Dashboard as active initially
                currentActiveSideButton = button;
            }
        }

        sidePanel.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("PharmaCare © " + new SimpleDateFormat("yyyy").format(new Date()));
        footer.setFont(LABEL_FONT.deriveFont(Font.ITALIC, 12f));
        footer.setForeground(C_TEXT_ON_DARK_SECONDARY.darker());
        footer.setBorder(new EmptyBorder(0, 25, 15, 25));
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidePanel.add(footer);

        add(sidePanel, BorderLayout.WEST);

        if (currentActiveSideButton != null) {
            setActiveSideButton(currentActiveSideButton); // Apply active style
        }
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT.deriveFont(15f));
        button.setForeground(Color.black);
        button.setBackground(C_BACKGROUND_SIDE_PANEL);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, C_BACKGROUND_SIDE_PANEL), // Left indicator border,
                                                                                      // initially same color as
                                                                                      // background
                new EmptyBorder(15, 25, 15, 20)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setOpaque(true); // Important for background color to show
        button.setContentAreaFilled(true); // Ensure background is painted
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // To make all buttons same height in BoxLayout

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (button != currentActiveSideButton) {
                    button.setBackground(C_PRIMARY_THEME.darker());
                    button.setForeground(Color.black);
                }
            }

            public void mouseExited(MouseEvent e) {
                if (button != currentActiveSideButton) {
                    button.setBackground(C_BACKGROUND_SIDE_PANEL);
                    button.setForeground(Color.black);
                }
            }
        });
        return button;
    }

    private void setActiveSideButton(JButton activeButton) {
        // Reset previous active button
        if (currentActiveSideButton != null && currentActiveSideButton != activeButton) {
            currentActiveSideButton.setBackground(C_BACKGROUND_SIDE_PANEL);
            currentActiveSideButton.setForeground(Color.black);
            currentActiveSideButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 5, 0, 0, C_BACKGROUND_SIDE_PANEL), // Reset indicator
                    new EmptyBorder(15, 25, 15, 20)));
        }

        // Set new active button
        activeButton.setBackground(C_PRIMARY_THEME.darker()); // Active background
        activeButton.setForeground(Color.black); // Active text color
        activeButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, C_ACCENT_THEME), // Active indicator (e.g., orange strip on
                                                                             // left)
                new EmptyBorder(15, 25, 15, 20)));
        currentActiveSideButton = activeButton;
    }

    private void initializeMainContentPanelAndCards() {
        cardLayout = new CardLayout(20, 20); // Gaps around the card
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding for the whole content area
        mainContentPanel.setBackground(C_BACKGROUND_MAIN); // Background for the content area

        mainContentPanel.add(createDashboardPanel(), "DASHBOARD");
        mainContentPanel.add(createProfilePanel(), "PROFILE");
        mainContentPanel.add(createMedicinesPanel(), "MEDICINES");
        mainContentPanel.add(createOrdersPanel(), "ORDERS");
        mainContentPanel.add(createBillingPanel(), "BILLING");

        add(mainContentPanel, BorderLayout.CENTER);
        cardLayout.show(mainContentPanel, "DASHBOARD"); // Default view
    }

    // Helper to create a styled panel for cards (like dashboard items, profile
    // form)
    private JPanel createStyledCardPanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(C_BACKGROUND_PANEL_CONTENT); // White background for card content
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER_LIGHT, 1), // Light border
                new EmptyBorder(20, 20, 20, 20) // Inner padding
        ));
        return card;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2x2 grid with gaps
        panel.setOpaque(false); // Transparent to show mainContentPanel's background
        panel.setBackground(C_BACKGROUND_MAIN);

        String[] titles = { "Total Customers", "Available Medicines", "Total Orders", "Total Revenue" };
        String[] initialValues = { "0", "0", "0", "₹0.00" };
        Color[] iconBgColors = { // Background colors for the icon holders
                C_SECONDARY_THEME, C_SUCCESS, C_ACCENT_THEME, new Color(142, 68, 173) // Purple
        };
        String[] iconTexts = { "👥", "💊", "🛒", "💰" }; // Emojis as icons

        for (int i = 0; i < 4; i++) {
            JPanel card = createStyledCardPanel(); // Use helper for consistent card styling

            JLabel titleLabel = new JLabel(titles[i]);
            titleLabel.setFont(LABEL_FONT.deriveFont(18f));
            titleLabel.setForeground(Color.black);

            JLabel valueLabel = new JLabel(initialValues[i]);
            valueLabel.setFont(SUB_HEADING_FONT.deriveFont(Font.BOLD, 28f));
            valueLabel.setForeground(iconBgColors[i].darker()); // Text color matching icon theme
            dashboardValueLabels[i] = valueLabel; // Store reference for easy update

            // Simplified Icon: JPanel as background holder, JLabel for emoji
            JPanel iconHolder = new JPanel(new GridBagLayout()); // Use GridBagLayout to center emoji
            iconHolder.setPreferredSize(new Dimension(60, 60));
            iconHolder.setBackground(iconBgColors[i]);
            iconHolder.setOpaque(true);

            JLabel emojiLabel = new JLabel(iconTexts[i]);
            emojiLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 24)); // Font for emojis
            emojiLabel.setForeground(C_TEXT_ON_DARK_PRIMARY); // White emoji text
            iconHolder.add(emojiLabel);

            JPanel textPanel = new JPanel(new BorderLayout(0, 5));
            textPanel.setOpaque(false);
            textPanel.add(titleLabel, BorderLayout.NORTH);
            textPanel.add(valueLabel, BorderLayout.CENTER);

            // Container for the icon, ensuring it's on the left with padding
            JPanel iconContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            iconContainerPanel.setOpaque(false);
            iconContainerPanel.add(iconHolder);
            iconContainerPanel.setBorder(new EmptyBorder(0, 0, 0, 15)); // Margin to the right of icon

            card.add(iconContainerPanel, BorderLayout.WEST);
            card.add(textPanel, BorderLayout.CENTER);

            panel.add(card);
        }
        return panel;
    }

    private void loadDashboardData() {
        updateDashboardValue(0, "1"); // Total Customers (Sample, could be dynamic)

        int availableMedicinesCount = 0;
        if (allMedicinesData != null) {
            for (Object[] med : allMedicinesData) {
                if (med != null && med.length > 3 && med[3] instanceof Integer && (Integer) med[3] > 0) {
                    availableMedicinesCount++;
                }
            }
        }
        updateDashboardValue(1, String.valueOf(availableMedicinesCount));

        updateDashboardValue(2, String.valueOf(allOrdersData != null ? allOrdersData.size() : 0));

        double totalRevenue = 0;
        if (allOrdersData != null) {
            for (Object[] order : allOrdersData) {
                if (order != null && order.length > 3 && order[3] instanceof Double) {
                    totalRevenue += (Double) order[3];
                }
            }
        }
        updateDashboardValue(3, String.format("₹%.2f", totalRevenue));
    }

    // Simplified method to update dashboard values using the stored labels
    private void updateDashboardValue(int index, String value) {
        if (index >= 0 && index < dashboardValueLabels.length && dashboardValueLabels[index] != null) {
            dashboardValueLabels[index].setText(value);
        }
    }

    private JPanel createProfilePanel() {
        JPanel profileSectionPanel = new JPanel(new BorderLayout(0, 20));
        profileSectionPanel.setBackground(C_BACKGROUND_MAIN); // Match content area background
        profileSectionPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Outer padding for the section

        // Header for the profile section (Title + Save Button)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Bottom margin

        JLabel profileTitleLabel = new JLabel("CUSTOMER PROFILE");
        profileTitleLabel.setFont(SUB_HEADING_FONT);
        profileTitleLabel.setForeground(C_TEXT_ON_LIGHT_PRIMARY);
        headerPanel.add(profileTitleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton saveBtn = createActionButton("Save Profile", C_SUCCESS, Color.black);
        saveBtn.addActionListener(e -> saveCustomerProfile());
        buttonPanel.add(saveBtn);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        profileSectionPanel.add(headerPanel, BorderLayout.NORTH);

        // Form housed in a styled card
        JPanel formCardPanel = createStyledCardPanel();
        formCardPanel.setLayout(new BorderLayout()); // Content within the card

        JPanel formGridPanel = new JPanel(new GridLayout(0, 2, 20, 15)); // Grid for labels and fields
        formGridPanel.setOpaque(false); // Transparent to show card's background
        formGridPanel.setBorder(new EmptyBorder(15, 15, 15, 15)); // Padding inside the form grid

        String[] labels = { "Customer ID:", "First Name:", "Last Name:", "Date of Birth (YYYY-MM-DD):" };
        customerIdField = createStyledTextField();
        custFirstNameField = createStyledTextField();
        custLastNameField = createStyledTextField();
        custDobField = createStyledTextField();
        JTextField[] fields = { customerIdField, custFirstNameField, custLastNameField, custDobField };

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(LABEL_FONT);
            label.setForeground(Color.black);
            formGridPanel.add(label);
            formGridPanel.add(fields[i]);
        }

        customerIdField.setText(DEFAULT_CUSTOMER_ID);
        customerIdField.setEditable(false);
        customerIdField.setBackground(C_DISABLED_FIELD_BG);
        customerIdField.setForeground(Color.black);

        loadCustomerProfile(); // Load initial/sample profile data

        formCardPanel.add(formGridPanel, BorderLayout.CENTER);
        profileSectionPanel.add(formCardPanel, BorderLayout.CENTER);

        return profileSectionPanel;
    }

    private void loadCustomerProfile() {
        // Sample data, in a real app this would load from a data source
        customerIdField.setText(DEFAULT_CUSTOMER_ID);
        custFirstNameField.setText("Ahmad");
        custLastNameField.setText("Khan");
        custDobField.setText("1990-01-01");
    }

    private void saveCustomerProfile() {
        String firstName = custFirstNameField.getText().trim();
        String lastName = custLastNameField.getText().trim();
        String dob = custDobField.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "First name and last name cannot be empty.", "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Simple regex for YYYY-MM-DD format or allow empty
        if (!dob.matches("\\d{4}-\\d{2}-\\d{2}") && !dob.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Date of Birth must be in YYYY-MM-DD format or empty.",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // In a real app, save this data
        JOptionPane.showMessageDialog(this, "Profile saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(LABEL_FONT);
        textField.setBackground(C_TEXT_FIELD_BG);
        textField.setForeground(Color.black);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER_LIGHT, 1),
                new EmptyBorder(8, 10, 8, 10) // Padding inside the text field
        ));
        textField.setCaretColor(C_PRIMARY_THEME); // Caret color matches theme
        return textField;
    }

    private JButton createActionButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT.deriveFont(14f));
        button.setForeground(Color.black);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);

        // Simple hover effect
        Color hoverBgColor = bgColor.brighter();
        if (hoverBgColor.equals(bgColor))
            hoverBgColor = bgColor.darker(); // If already too bright

        Color finalHoverBgColor = hoverBgColor;
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(finalHoverBgColor);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private void styleTable(JTable table, JScrollPane scrollPane) {
        table.setRowHeight(38);
        table.setFont(TABLE_FONT);
        table.setForeground(Color.black);
        table.getTableHeader().setFont(TABLE_HEADER_FONT);
        table.getTableHeader().setBackground(C_PRIMARY_THEME.darker());
        table.getTableHeader().setForeground(Color.black);
        table.setGridColor(C_TABLE_GRID);
        table.setSelectionBackground(C_TABLE_SELECTION_BG);
        table.setSelectionForeground(Color.black);
        table.setFillsViewportHeight(true); // Table uses entire height of scroll pane
        scrollPane.setBorder(BorderFactory.createLineBorder(C_BORDER_LIGHT));
        scrollPane.getViewport().setBackground(C_BACKGROUND_PANEL_CONTENT); // BG for empty part of table
        table.getTableHeader().setReorderingAllowed(false); // Prevent column reordering

        // Alternate row coloring
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? C_BACKGROUND_PANEL_CONTENT : C_TABLE_ALT_ROW_BG);
                }
                // Reset border for general cells if any specific renderer added one
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(new EmptyBorder(0, 5, 0, 5)); // Basic padding
                }
                return c;
            }
        });
    }

    private JPanel createMedicinesPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15)); // Gaps between components
        panel.setBackground(C_BACKGROUND_MAIN);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header: Title and Search
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel medCatalogTitleLabel = new JLabel("MEDICINE CATALOG");
        medCatalogTitleLabel.setFont(SUB_HEADING_FONT);
        medCatalogTitleLabel.setForeground(Color.black);
        headerPanel.add(medCatalogTitleLabel, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(LABEL_FONT);
        searchLabel.setForeground(Color.black);
        searchMedicineField = createStyledTextField();
        searchMedicineField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                filterMedicinesTable();
            }

            public void removeUpdate(DocumentEvent e) {
                filterMedicinesTable();
            }

            public void changedUpdate(DocumentEvent e) {
                filterMedicinesTable();
            }
        });
        JButton searchBtn = createActionButton("Search", C_SECONDARY_THEME, C_TEXT_ON_DARK_PRIMARY);
        searchBtn.addActionListener(e -> filterMedicinesTable()); // Explicit search button click

        searchPanel.add(searchLabel);
        searchPanel.add(searchMedicineField);
        searchPanel.add(searchBtn);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Medicines Table
        String[] columns = { "ID", "Medicine Name", "Price (₹)", "Stock", "Category" };
        medicinesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            } // Non-editable cells
        };
        medicinesTable = new JTable(medicinesTableModel);
        JScrollPane scrollPane = new JScrollPane(medicinesTable);
        styleTable(medicinesTable, scrollPane); // Apply common styling

        // Custom renderers for specific columns
        medicinesTable.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer()); // Price
        medicinesTable.getColumnModel().getColumn(3).setCellRenderer(new StockRenderer()); // Stock
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons below table
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0)); // Top margin
        JButton orderBtn = createActionButton("Place New Order", C_ACCENT_THEME, Color.black);
        orderBtn.addActionListener(e -> placeOrderForSelectedMedicines());
        buttonPanel.add(orderBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Inner class for Currency Rendering
    class CurrencyRenderer extends DefaultTableCellRenderer {
        public CurrencyRenderer() {
            super();
            setHorizontalAlignment(SwingConstants.RIGHT);
            setBorder(new EmptyBorder(0, 5, 0, 10)); // Padding for currency values
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            // Let the general renderer from styleTable handle background for non-selected
            // rows
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (value instanceof Double) {
                setText(String.format("₹ %.2f", (Double) value));
            }
            if (isSelected) {
                c.setForeground(C_TABLE_SELECTION_FG);
                c.setBackground(C_TABLE_SELECTION_BG);
            } else {
                c.setForeground(C_TEXT_ON_LIGHT_PRIMARY);
                c.setBackground(row % 2 == 0 ? C_BACKGROUND_PANEL_CONTENT : C_TABLE_ALT_ROW_BG);
            }
            return c;
        }
    }

    // Inner class for Stock Rendering
    class StockRenderer extends DefaultTableCellRenderer {
        public StockRenderer() {
            super();
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            int stock = (value instanceof Integer) ? (Integer) value : 0;
            String text = String.valueOf(stock);
            Color fgColor = C_TEXT_ON_LIGHT_PRIMARY;

            if (stock <= 0) {
                fgColor = C_DANGER;
                text = stock + " (Out of Stock)";
            } else if (stock < 10) {
                fgColor = C_DANGER.darker();
                text = stock + " (Low Stock)";
            } else if (stock < 30) {
                fgColor = C_WARNING.darker();
            } else {
                fgColor = C_SUCCESS.darker();
            }
            setText(text);

            if (isSelected) {
                c.setForeground(C_TABLE_SELECTION_FG);
                c.setBackground(C_TABLE_SELECTION_BG);
            } else {
                c.setForeground(fgColor); // Specific text color based on stock
                c.setBackground(row % 2 == 0 ? C_BACKGROUND_PANEL_CONTENT : C_TABLE_ALT_ROW_BG);
            }
            return c;
        }
    }

    private JPanel createOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(C_BACKGROUND_MAIN);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel orderMgmtTitleLabel = new JLabel("ORDER MANAGEMENT");
        orderMgmtTitleLabel.setFont(SUB_HEADING_FONT);
        orderMgmtTitleLabel.setForeground(C_TEXT_ON_LIGHT_PRIMARY);
        headerPanel.add(orderMgmtTitleLabel, BorderLayout.WEST);
        panel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = { "Order ID", "Date", "Customer", "Total Amount", "Status" };
        ordersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        ordersTable = new JTable(ordersTableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        styleTable(ordersTable, scrollPane); // Apply common styling

        // Custom renderers for specific columns
        ordersTable.getColumnModel().getColumn(3).setCellRenderer(new CurrencyRenderer()); // Total Amount
        ordersTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() { // Status
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            } // Initializer block for alignment

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;
                Color fgColor = C_TEXT_ON_LIGHT_PRIMARY;
                if ("Completed".equalsIgnoreCase(status))
                    fgColor = C_SUCCESS.darker();
                else if ("Pending".equalsIgnoreCase(status))
                    fgColor = C_WARNING.darker();
                else if ("Cancelled".equalsIgnoreCase(status))
                    fgColor = C_DANGER;

                if (isSelected) {
                    c.setForeground(C_TABLE_SELECTION_FG);
                    c.setBackground(C_TABLE_SELECTION_BG);
                } else {
                    c.setForeground(fgColor);
                    c.setBackground(row % 2 == 0 ? C_BACKGROUND_PANEL_CONTENT : C_TABLE_ALT_ROW_BG);
                }
                return c;
            }
        });
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton detailsBtn = createActionButton("View Details", C_SECONDARY_THEME, C_TEXT_ON_DARK_PRIMARY);
        detailsBtn.addActionListener(e -> showOrderDetails());
        JButton printBtn = createActionButton("Print Bill", C_PRIMARY_THEME, C_TEXT_ON_DARK_PRIMARY);
        printBtn.addActionListener(e -> printSelectedBill());
        JButton deleteBtn = createActionButton("Delete Order", C_DANGER, C_TEXT_ON_DARK_PRIMARY);
        deleteBtn.addActionListener(e -> deleteSelectedOrder());
        buttonPanel.add(detailsBtn);
        buttonPanel.add(printBtn);
        buttonPanel.add(deleteBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createBillingPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(C_BACKGROUND_MAIN);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel billingTitleLabel = new JLabel("BILLING SYSTEM");
        billingTitleLabel.setFont(SUB_HEADING_FONT);
        billingTitleLabel.setForeground(C_TEXT_ON_LIGHT_PRIMARY);
        headerPanel.add(billingTitleLabel, BorderLayout.WEST);
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel billCard = createStyledCardPanel(); // Use styled card for bill display area
        billCard.setLayout(new BorderLayout());

        JTextArea billArea = new JTextArea();
        billArea.setFont(BILL_FONT);
        billArea.setEditable(false);
        billArea.setBackground(C_BACKGROUND_PANEL_CONTENT); // Match card background
        billArea.setForeground(C_TEXT_ON_LIGHT_PRIMARY.darker());
        billArea.setText(generateSampleBillText()); // Show a sample bill by default
        billArea.setLineWrap(true);
        billArea.setWrapStyleWord(true);
        billArea.setMargin(new Insets(10, 10, 10, 10)); // Padding inside text area

        JScrollPane scrollPane = new JScrollPane(billArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // No border for scroll pane itself
        billCard.add(scrollPane, BorderLayout.CENTER);
        panel.add(billCard, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton printBtn = createActionButton("Print Bill", C_PRIMARY_THEME, C_TEXT_ON_DARK_PRIMARY);
        printBtn.addActionListener(e -> printGenericBillFromBillingPanel(billArea.getText()));
        JButton saveBtn = createActionButton("Save to File", C_SECONDARY_THEME, C_TEXT_ON_DARK_PRIMARY);
        saveBtn.addActionListener(e -> saveGenericBillFromBillingPanel(billArea.getText()));
        buttonPanel.add(printBtn);
        buttonPanel.add(saveBtn);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private String generateBillText(String orderId, String customerName, String orderDate, double totalAmount,
            List<Object[]> items) {
        StringBuilder bill = new StringBuilder();
        String lineSeparator = "------------------------------------------------\n";
        bill.append("            PHARMACARE PHARMACY\n");
        bill.append("         Your Health, Our Priority\n");
        bill.append(lineSeparator);
        bill.append(String.format(" Invoice No: %s\n", orderId));
        bill.append(String.format(" Date:       %s\n", orderDate));
        bill.append(String.format(" Customer:   %s\n", customerName));
        bill.append(lineSeparator);
        bill.append(String.format(" %-28s %7s %5s %11s\n", "Item", "Price", "Qty", "Subtotal"));
        bill.append(lineSeparator);

        if (items != null && !items.isEmpty()) {
            for (Object[] item : items) {
                // Item: [Name (String), Price (Double), Qty (Integer), Subtotal (Double)]
                String medName = (String) item[0];
                double price = (Double) item[1];
                int qty = (Integer) item[2];
                double subtotal = (Double) item[3];
                bill.append(String.format(" %-28.28s %7.2f %5d %11.2f\n", medName, price, qty, subtotal));
            }
        } else {
            // Placeholder if items list is empty, e.g., for the generic bill panel
            bill.append(String.format(" %-28.28s %7.2f %5d %11.2f\n", "Sample Item A", 25.60, 1, 25.60));
            bill.append(String.format(" %-28.28s %7.2f %5d %11.2f\n", "Sample Item B", 14.40, 1, 14.40));
        }

        bill.append(lineSeparator);
        bill.append(String.format("%44s %11.2f\n", "Total:", totalAmount));
        bill.append("================================================\n");
        bill.append("          Thank you for your purchase!\n");
        bill.append("         We value your health and trust\n");
        return bill.toString();
    }

    private String generateSampleBillText() {
        List<Object[]> sampleItemsForBill = new ArrayList<>();
        sampleItemsForBill.add(new Object[] { "Panadol Extra", 3.50, 2, 7.00 });
        sampleItemsForBill.add(new Object[] { "Brufen 400mg", 4.20, 1, 4.20 });
        return generateBillText("INV-SAMPLE-001", "Walk-in Customer",
                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),
                11.20, sampleItemsForBill);
    }

    private void loadSampleData() {
        allMedicinesData.clear();
        allMedicinesData.add(new Object[] { "M001", "Panadol Extra", 3.50, 250, "Pain Relief" });
        allMedicinesData.add(new Object[] { "M002", "Brufen 400mg", 4.20, 180, "Pain Relief" });
        allMedicinesData.add(new Object[] { "M003", "Disprin Regular", 1.00, 5, "Pain Relief" });
        allMedicinesData.add(new Object[] { "M004", "Amoxil Syrup 125mg/5ml", 10.00, 0, "Antibiotic" });
        allMedicinesData.add(new Object[] { "M005", "Surbex Z Multivitamins", 9.75, 25, "Vitamins" });
        allMedicinesData.add(new Object[] { "M006", "Vitamin C 500mg (Chewable)", 8.50, 150, "Vitamins" });
        allMedicinesData.add(new Object[] { "M007", "Aspirin Cardio 75mg", 2.30, 300, "Cardiovascular" });
        allMedicinesData.add(new Object[] { "M008", "Herbal Cough Syrup", 6.75, 45, "Cough & Cold" });
        refreshMedicinesTableData();

        allOrdersData.clear();
        allOrderItems.clear();

        // Sample order 1
        List<Object[]> order1Items = new ArrayList<>();
        order1Items.add(new Object[] { "Panadol Extra", 3.50, 2, 7.00 });
        order1Items.add(new Object[] { "Brufen 400mg", 4.20, 1, 4.20 });
        allOrdersData.add(new Object[] { "ORD001", "2023-12-10 10:30", "Kashif Ahmed", 11.20, "Completed" });
        allOrderItems.add(order1Items);

        // Sample order 2
        List<Object[]> order2Items = new ArrayList<>();
        order2Items.add(new Object[] { "Surbex Z Multivitamins", 9.75, 1, 9.75 });
        order2Items.add(new Object[] { "Vitamin C 500mg (Chewable)", 8.50, 1, 8.50 });
        allOrdersData.add(new Object[] { "ORD002", "2023-12-11 14:15", "Sana Jamil", 18.25, "Completed" });
        allOrderItems.add(order2Items);

        // Current pending order
        List<Object[]> order3Items = new ArrayList<>();
        order3Items.add(new Object[] { "Herbal Cough Syrup", 6.75, 2, 13.50 });
        order3Items.add(new Object[] { "Aspirin Cardio 75mg", 2.30, 1, 2.30 });
        allOrdersData.add(new Object[] { "ORD003", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()),
                "Ali Raza", 15.80, "Pending" });
        allOrderItems.add(order3Items);

        // Cancelled order
        List<Object[]> order4Items = new ArrayList<>();
        order4Items.add(new Object[] { "Disprin Regular", 1.00, 3, 3.00 });
        allOrdersData.add(new Object[] { "ORD004", "2023-12-16 11:00", "Fatima Ali", 3.00, "Cancelled" });
        allOrderItems.add(order4Items);

        refreshOrdersTableData();

        SwingUtilities.invokeLater(this::loadDashboardData); // Load dashboard after UI is set up
        loadCustomerProfile(); // Also load profile data
    }

    private void refreshMedicinesTableData() {
        if (medicinesTableModel == null)
            return;
        medicinesTableModel.setRowCount(0); // Clear existing rows
        if (allMedicinesData != null) {
            for (Object[] row : allMedicinesData) {
                medicinesTableModel.addRow(row);
            }
        }
    }

    private void filterMedicinesTable() {
        String search = searchMedicineField.getText().toLowerCase().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(medicinesTableModel);
        medicinesTable.setRowSorter(sorter);
        if (search.length() == 0) {
            sorter.setRowFilter(null); // No filter
        } else {
            try {
                // Case-insensitive search, quote to treat search string literally
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(search)));
            } catch (java.util.regex.PatternSyntaxException e) {
                // Handle invalid regex patterns if Pattern.quote wasn't perfect (unlikely for
                // simple text)
                sorter.setRowFilter(null);
            }
        }
    }

    private void refreshOrdersTableData() {
        if (ordersTableModel == null)
            return;
        ordersTableModel.setRowCount(0); // Clear existing rows
        if (allOrdersData != null) {
            for (Object[] row : allOrdersData) {
                ordersTableModel.addRow(row);
            }
        }
    }

    private void placeOrderForSelectedMedicines() {
        int[] selectedViewRows = medicinesTable.getSelectedRows();
        if (selectedViewRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select medicines to order.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog orderDialog = new JDialog(this, "Place New Order", true); // Modal dialog
        orderDialog.setSize(650, 550);
        orderDialog.setLocationRelativeTo(this);
        orderDialog.setLayout(new BorderLayout());
        orderDialog.getContentPane().setBackground(C_BACKGROUND_MAIN);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(C_PRIMARY_THEME);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        JLabel titleLabelDialog = new JLabel("CONFIRM ORDER ITEMS");
        titleLabelDialog.setFont(SUB_HEADING_FONT.deriveFont(18f));
        titleLabelDialog.setForeground(C_TEXT_ON_DARK_PRIMARY);
        headerPanel.add(titleLabelDialog);
        orderDialog.add(headerPanel, BorderLayout.NORTH);

        JPanel itemsPanel = new JPanel(new BorderLayout(0, 10));
        itemsPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        itemsPanel.setBackground(C_BACKGROUND_PANEL_CONTENT);

        String[] columns = { "Med ID", "Medicine Name", "Price (₹)", "Quantity" };
        DefaultTableModel orderModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            } // Only quantity editable

            @Override
            public Class<?> getColumnClass(int columnIndex) { // For correct editor/renderer
                if (columnIndex == 2)
                    return Double.class;
                if (columnIndex == 3)
                    return Integer.class;
                return String.class;
            }
        };

        for (int viewRow : selectedViewRows) {
            int modelRow = medicinesTable.convertRowIndexToModel(viewRow);
            if (modelRow < 0 || modelRow >= medicinesTableModel.getRowCount())
                continue;

            String id = (String) medicinesTableModel.getValueAt(modelRow, 0);
            String name = (String) medicinesTableModel.getValueAt(modelRow, 1);
            double price = (Double) medicinesTableModel.getValueAt(modelRow, 2);
            int currentStock = (Integer) medicinesTableModel.getValueAt(modelRow, 3);

            if (currentStock > 0) { // Only add if in stock
                orderModel.addRow(new Object[] { id, name, price, 1 }); // Default quantity 1
            }
        }

        if (orderModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No valid medicines selected or all are out of stock.", "No Items",
                    JOptionPane.WARNING_MESSAGE);
            orderDialog.dispose();
            return;
        }

        JTable orderTable = new JTable(orderModel);
        JScrollPane scrollPane = new JScrollPane(orderTable);
        styleTable(orderTable, scrollPane); // Apply common table styling
        orderTable.getColumnModel().getColumn(2).setCellRenderer(new CurrencyRenderer()); // Price

        // Spinner for Quantity column
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)); // Min 1, Max 100
        ((JSpinner.DefaultEditor) quantitySpinner.getEditor()).getTextField().setColumns(3);
        orderTable.getColumnModel().getColumn(3).setCellEditor(new SpinnerEditor(quantitySpinner));
        itemsPanel.add(scrollPane, BorderLayout.CENTER);

        JLabel totalLabel = new JLabel("Total: ₹0.00");
        totalLabel.setFont(BUTTON_FONT.deriveFont(Font.BOLD, 18f));
        totalLabel.setForeground(C_PRIMARY_THEME);
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        totalLabel.setBorder(new EmptyBorder(10, 0, 0, 5));

        // Calculate total dynamically
        Runnable calculateTotal = () -> {
            double total = 0;
            for (int i = 0; i < orderModel.getRowCount(); i++) {
                double price = (Double) orderModel.getValueAt(i, 2);
                int qty = (Integer) orderModel.getValueAt(i, 3);
                total += price * qty;
            }
            totalLabel.setText(String.format("Total: ₹%.2f", total));
        };
        orderModel.addTableModelListener(e -> calculateTotal.run()); // Update total on table change
        calculateTotal.run(); // Initial calculation

        itemsPanel.add(totalLabel, BorderLayout.SOUTH);
        orderDialog.add(itemsPanel, BorderLayout.CENTER);

        JPanel buttonPanelDialog = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanelDialog.setBackground(C_BACKGROUND_MAIN);
        buttonPanelDialog.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton cancelBtn = createActionButton("Cancel", new Color(120, 120, 120), C_TEXT_ON_DARK_PRIMARY);
        cancelBtn.addActionListener(e -> orderDialog.dispose());

        JButton confirmBtn = createActionButton("Confirm Order", C_SUCCESS, C_TEXT_ON_DARK_PRIMARY);
        confirmBtn.addActionListener(e -> {
            double finalTotal = 0;
            List<Object[]> currentOrderItems = new ArrayList<>();

            // Validate quantities against stock before processing
            for (int i = 0; i < orderModel.getRowCount(); i++) {
                String medId = (String) orderModel.getValueAt(i, 0);
                int qtyOrdered = (Integer) orderModel.getValueAt(i, 3);

                for (Object[] med : allMedicinesData) {
                    if (med[0].equals(medId)) {
                        int stock = (Integer) med[3];
                        if (qtyOrdered > stock) {
                            JOptionPane.showMessageDialog(orderDialog,
                                    "Cannot order " + qtyOrdered + " of " + med[1] +
                                            ". Only " + stock + " available.",
                                    "Insufficient Stock", JOptionPane.ERROR_MESSAGE);
                            return; // Stop processing
                        }
                        break;
                    }
                }
            }

            // Process order if all validations pass
            for (int i = 0; i < orderModel.getRowCount(); i++) {
                String medId = (String) orderModel.getValueAt(i, 0);
                String name = (String) orderModel.getValueAt(i, 1);
                double price = (Double) orderModel.getValueAt(i, 2);
                int qty = (Integer) orderModel.getValueAt(i, 3);
                double subtotal = price * qty;
                finalTotal += subtotal;

                currentOrderItems.add(new Object[] { name, price, qty, subtotal });

                // Update stock in allMedicinesData
                for (int j = 0; j < allMedicinesData.size(); j++) {
                    Object[] med = allMedicinesData.get(j);
                    if (med[0].equals(medId)) {
                        int newStock = (Integer) med[3] - qty;
                        med[3] = newStock;
                        allMedicinesData.set(j, med); // Update the list
                        break;
                    }
                }
            }

            String orderId = "ORD" + String.format("%03d", allOrdersData.size() + 1);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            String customer = custFirstNameField.getText() + " " + custLastNameField.getText(); // From profile

            allOrdersData.add(new Object[] { orderId, date, customer, finalTotal, "Completed" }); // Default to
                                                                                                  // completed
            allOrderItems.add(currentOrderItems);

            refreshOrdersTableData();
            refreshMedicinesTableData(); // Reflect stock changes
            loadDashboardData(); // Update dashboard stats

            JOptionPane.showMessageDialog(orderDialog,
                    "Order placed successfully!\nOrder ID: " + orderId + "\nTotal: ₹"
                            + String.format("%.2f", finalTotal),
                    "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);

            orderDialog.dispose();
        });

        buttonPanelDialog.add(cancelBtn);
        buttonPanelDialog.add(confirmBtn);
        orderDialog.add(buttonPanelDialog, BorderLayout.SOUTH);

        orderDialog.setVisible(true);
    }

    // Inner class for JSpinner as TableCellEditor
    static class SpinnerEditor extends AbstractCellEditor implements TableCellEditor {
        private final JSpinner spinner;

        public SpinnerEditor(JSpinner spinner) {
            this.spinner = spinner;
            // Optional: make spinner react to single click
            // JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
            // editor.getTextField().addFocusListener(new FocusAdapter() {
            // public void focusGained(FocusEvent fe) {
            // SwingUtilities.invokeLater(() -> editor.getTextField().selectAll());
            // }
            // });
        }

        @Override
        public Object getCellEditorValue() {
            return spinner.getValue();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            spinner.setValue(value != null ? value : 1); // Set current value
            return spinner;
        }
    }

    private void showOrderDetails() {
        int selectedViewRow = ordersTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to view details.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = ordersTable.convertRowIndexToModel(selectedViewRow);
        if (modelRow < 0 || modelRow >= allOrdersData.size())
            return; // Should not happen if selection is valid

        Object[] orderRecord = allOrdersData.get(modelRow);
        String orderId = (String) orderRecord[0];
        String customer = (String) orderRecord[2];
        String date = (String) orderRecord[1];
        double total = (Double) orderRecord[3];
        String status = (String) orderRecord[4];

        List<Object[]> items = allOrderItems.get(modelRow); // Get corresponding items
        StringBuilder itemsDetails = new StringBuilder();
        for (Object[] item : items) {
            // Item: [Name (String), Price (Double), Qty (Integer), Subtotal (Double)]
            itemsDetails.append(String.format("- %s (Qty: %d, Price: ₹%.2f, Sub: ₹%.2f)\n",
                    item[0], item[2], item[1], item[3]));
        }

        String statusColor = "black"; // Default
        if ("Completed".equalsIgnoreCase(status))
            statusColor = "green";
        else if ("Pending".equalsIgnoreCase(status))
            statusColor = "#b8860b"; // DarkGoldenrod like
        else if ("Cancelled".equalsIgnoreCase(status))
            statusColor = "red";

        String message = String.format(
                "<html><body style='width: 350px; font-family: Segoe UI, sans-serif;'>" +
                        "<b>Order ID:</b> %s<br>" +
                        "<b>Customer:</b> %s<br>" +
                        "<b>Date:</b> %s<br>" +
                        "<b>Status:</b> <font color='%s'><b>%s</b></font><br>" +
                        "<b>Total Amount:</b> ₹%.2f<br><br>" +
                        "<b>Items:</b><br><div style='font-family: Consolas, monospace; font-size: 9pt;'>%s</div>" + // Use
                                                                                                                     // pre
                                                                                                                     // for
                                                                                                                     // formatting
                        "</body></html>",
                orderId, customer, date,
                statusColor, status.toUpperCase(),
                total, itemsDetails.toString().replace("\n", "<br>") // Convert newlines for HTML
        );

        JOptionPane.showMessageDialog(this, message, "Order Details - " + orderId, JOptionPane.INFORMATION_MESSAGE);
    }

    private void printSelectedBill() {
        int selectedViewRow = ordersTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to print its bill.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = ordersTable.convertRowIndexToModel(selectedViewRow);
        if (modelRow < 0 || modelRow >= allOrdersData.size())
            return;

        Object[] orderRecord = allOrdersData.get(modelRow);
        String orderId = (String) orderRecord[0];
        String customerName = (String) orderRecord[2];
        String orderDate = (String) orderRecord[1];
        double totalAmount = (Double) orderRecord[3];

        List<Object[]> items = allOrderItems.get(modelRow); // Get items for this order
        String billContentString = generateBillText(orderId, customerName, orderDate, totalAmount, items);

        showBillDialog(billContentString, "Pharmacy Bill - " + orderId, orderId);
    }

    private void showBillDialog(String billContent, String dialogTitle, String defaultFileNameBase) {
        JDialog billDialog = new JDialog(this, dialogTitle, true);
        billDialog.setSize(500, 600);
        billDialog.setLocationRelativeTo(this);
        billDialog.setLayout(new BorderLayout());
        billDialog.getContentPane().setBackground(C_BACKGROUND_MAIN);

        JTextArea billTextArea = new JTextArea(billContent);
        billTextArea.setFont(BILL_FONT);
        billTextArea.setEditable(false);
        billTextArea.setBackground(C_BACKGROUND_PANEL_CONTENT);
        billTextArea.setForeground(C_TEXT_ON_LIGHT_PRIMARY.darker());
        billTextArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane billScrollPane = new JScrollPane(billTextArea);
        billScrollPane.setBorder(BorderFactory.createEmptyBorder());
        billDialog.add(billScrollPane, BorderLayout.CENTER);

        JPanel buttonPanelDialog = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanelDialog.setBackground(C_BACKGROUND_MAIN);
        buttonPanelDialog.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton printActualBtn = createActionButton("Print", C_PRIMARY_THEME, C_TEXT_ON_DARK_PRIMARY);
        printActualBtn.addActionListener(e -> {
            try {
                billTextArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(billDialog, "Error printing: " + ex.getMessage(), "Print Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton saveFileBtn = createActionButton("Save to File", C_SECONDARY_THEME, C_TEXT_ON_DARK_PRIMARY);
        saveFileBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save Bill");
            fc.setSelectedFile(new File(defaultFileNameBase + ".txt"));
            if (fc.showSaveDialog(billDialog) == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                    fw.write(billContent);
                    JOptionPane.showMessageDialog(billDialog,
                            "Bill saved successfully to " + fc.getSelectedFile().getName(), "Save Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(billDialog, "Error saving: " + ioe.getMessage(), "Save Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton closeBtn = createActionButton("Close", new Color(120, 120, 120), C_TEXT_ON_DARK_PRIMARY);
        closeBtn.addActionListener(e -> billDialog.dispose());

        buttonPanelDialog.add(printActualBtn);
        buttonPanelDialog.add(saveFileBtn);
        buttonPanelDialog.add(closeBtn);
        billDialog.add(buttonPanelDialog, BorderLayout.SOUTH);
        billDialog.setVisible(true);
    }

    private void deleteSelectedOrder() {
        int selectedViewRow = ordersTable.getSelectedRow();
        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.", "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = ordersTable.convertRowIndexToModel(selectedViewRow);
        if (modelRow < 0 || modelRow >= allOrdersData.size())
            return;

        String orderId = (String) allOrdersData.get(modelRow)[0];
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete order " + orderId + "?\nThis action cannot be undone.",
                "Confirm Delete Order", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            allOrdersData.remove(modelRow);
            allOrderItems.remove(modelRow); // Important: also remove associated items
            refreshOrdersTableData();
            loadDashboardData(); // Update dashboard counts/revenue
            JOptionPane.showMessageDialog(this, "Order " + orderId + " has been deleted.", "Order Deleted",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void printGenericBillFromBillingPanel(String billContent) {
        JTextArea tempBillArea = new JTextArea(billContent); // Use current content of the billing panel's text area
        try {
            tempBillArea.print();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error printing: " + ex.getMessage(), "Print Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveGenericBillFromBillingPanel(String billContent) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save General Bill");
        fc.setSelectedFile(
                new File("General_Bill_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".txt"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                fw.write(billContent);
                JOptionPane.showMessageDialog(this, "Bill saved successfully to " + fc.getSelectedFile().getName(),
                        "Save Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Error saving: " + ioe.getMessage(), "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Inner class for Side Panel Navigation
    class NavigationListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            setActiveSideButton(clickedButton); // Highlight the clicked button
            String command = clickedButton.getActionCommand(); // Get button text

            switch (command) {
                case "Dashboard":
                    cardLayout.show(mainContentPanel, "DASHBOARD");
                    loadDashboardData(); // Refresh dashboard data on view
                    break;
                case "Customer Profile":
                    cardLayout.show(mainContentPanel, "PROFILE");
                    loadCustomerProfile(); // Refresh profile data on view
                    break;
                case "Medicine Catalog":
                    cardLayout.show(mainContentPanel, "MEDICINES");
                    refreshMedicinesTableData(); // Refresh medicine list
                    break;
                case "Order Management":
                    cardLayout.show(mainContentPanel, "ORDERS");
                    refreshOrdersTableData(); // Refresh order list
                    break;
                case "Billing":
                    cardLayout.show(mainContentPanel, "BILLING");
                    // Optionally, update the billing text area if it needs to be dynamic based on
                    // last order etc.
                    // For now, it shows a static sample or the last generated bill if logic was
                    // added.
                    break;
                case "Exit":
                    if (JOptionPane.showConfirmDialog(CustomerDashboard.this,
                            "Are you sure you want to exit PharmaCare?", "Confirm Exit",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                    break;
            }
        }
    }
}