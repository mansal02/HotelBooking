import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;
import org.jdatepicker.impl.*; // Import JDatePicker

public class BookingPanel extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JComboBox<String> branchCombo, roomTypeCombo;
    private JCheckBox mealCheck, wifiCheck, laundryCheck;
    private JTextArea summaryArea;
    private JButton submitBtn, backBtn;
    private JLabel availableLabel; // Label to show available rooms
    private JDatePickerImpl checkInDatePicker; // JDatePicker for check-in date selection
    private JDatePickerImpl checkOutDatePicker; // JDatePicker for check-out date selection
    private JLabel roomImageLabel; // Label to display room images

    private final Map<String, Integer> roomPrices = Map.of(
            "Suite", 200,
            "Deluxe", 150,
            "Standard", 100);
    private final int mealPrice = 20;
    private final int wifiPrice = 10;
    private final int laundryPrice = 15;

    public BookingPanel() {
        setTitle("Make New Booking");
        setSize(900, 750); // Width increased for split layout
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(20, 20));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Create main container with padding and max width
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setLayout(new GridBagLayout());
        mainContainer.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));
        add(mainContainer, BorderLayout.CENTER);

        GridBagConstraints gbcMain = new GridBagConstraints();
        gbcMain.insets = new Insets(10, 10, 10, 10);
        gbcMain.fill = GridBagConstraints.BOTH;
        gbcMain.weighty = 1;
        gbcMain.weightx = 0.5;

        // Left panel for form inputs
        JPanel formPanel = createFormPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)));

        // Right panel holds image and summary stacked vertically
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new BorderLayout(20, 20));

        // Image container card
        JPanel imageCard = new JPanel(new BorderLayout());
        imageCard.setPreferredSize(new Dimension(320, 320));
        imageCard.setBackground(Color.WHITE);
        imageCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        roomImageLabel = new JLabel("");
        roomImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageCard.add(roomImageLabel, BorderLayout.CENTER);

        // Summary container card
        JPanel summaryCard = new JPanel(new BorderLayout());
        summaryCard.setPreferredSize(new Dimension(320, 300));
        summaryCard.setBackground(Color.WHITE);
        summaryCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        summaryArea = new JTextArea();
        summaryArea.setEditable(false);
        summaryArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setBackground(new Color(248, 248, 248));
        summaryCard.add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        rightPanel.add(imageCard, BorderLayout.NORTH);
        rightPanel.add(summaryCard, BorderLayout.SOUTH);

        // Add panels to main container side by side
        gbcMain.gridx = 0;
        gbcMain.weightx = 0.6;
        mainContainer.add(formPanel, gbcMain);

        gbcMain.gridx = 1;
        gbcMain.weightx = 0.4;
        mainContainer.add(rightPanel, gbcMain);

        // Create button panel - placed below form panel spanning full width
        JPanel btnPanel = createButtonPanel();
        btnPanel.setBackground(Color.WHITE);
        gbcMain.gridx = 0;
        gbcMain.gridy = 1;
        gbcMain.gridwidth = 2;
        gbcMain.weightx = 1;
        gbcMain.weighty = 0;
        gbcMain.fill = GridBagConstraints.NONE;
        gbcMain.anchor = GridBagConstraints.CENTER;
        mainContainer.add(btnPanel, gbcMain);

        // Initialize summary and image
        updateSummary();
        updateRoomImage();

        // Add listeners for real-time summary updates
        addListenersToUpdateSummary();
        // Add listeners for available label and image update
        roomTypeCombo.addActionListener(e -> {
            updateAvailableLabel();
            updateRoomImage();
        });
        branchCombo.addActionListener(e -> updateAvailableLabel());

        // Initialize available label
        updateAvailableLabel();

        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer Name:"), gbc);
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        // Phone Number
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        phoneField = new JTextField();
        phoneField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Check-in Date
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Check-in Date:"), gbc);
        checkInDatePicker = createDatePicker();
        gbc.gridx = 1;
        formPanel.add(checkInDatePicker, gbc);

        // Check-out Date
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Check-out Date:"), gbc);
        checkOutDatePicker = createDatePicker();
        gbc.gridx = 1;
        formPanel.add(checkOutDatePicker, gbc);

        // Branch
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Branch:"), gbc);
        branchCombo = new JComboBox<>(new String[] { "CityHotel", "BeachResort" });
        branchCombo.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(branchCombo, gbc);

        // Room Type
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Room Type:"), gbc);
        roomTypeCombo = new JComboBox<>(new String[] { "Suite", "Deluxe", "Standard" });
        roomTypeCombo.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        formPanel.add(roomTypeCombo, gbc);

        // Available label
        availableLabel = new JLabel("Available: " + AdminPanel.getAvailability("Suite") + " Suites");
        availableLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(availableLabel, gbc);
        gbc.gridwidth = 1;

        // Add-ons labels and checkboxes
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Add Meals (+RM 20):"), gbc);
        mealCheck = new JCheckBox();
        gbc.gridx = 1;
        formPanel.add(mealCheck, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Add High-Speed WiFi (+RM 10):"), gbc);
        wifiCheck = new JCheckBox();
        gbc.gridx = 1;
        formPanel.add(wifiCheck, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Add Laundry (+RM 15):"), gbc);
        laundryCheck = new JCheckBox();
        gbc.gridx = 1;
        formPanel.add(laundryCheck, gbc);

        return formPanel;
    }

    private JDatePickerImpl createDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        return new JDatePickerImpl(new JDatePanelImpl(model, p), new DateLabelFormatter());
    }

    private JPanel createButtonPanel() {
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS)); // Use BoxLayout for horizontal alignment
        submitBtn = new JButton("Submit Booking");
        backBtn = new JButton("Back to Home");
        submitBtn.setPreferredSize(new Dimension(160, 44));
        backBtn.setPreferredSize(new Dimension(160, 44));
        btnPanel.add(Box.createHorizontalGlue()); // Add glue for spacing
        btnPanel.add(submitBtn);
        btnPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add space between buttons
        btnPanel.add(backBtn);
        btnPanel.add(Box.createHorizontalGlue()); // Add glue for spacing

        submitBtn.addActionListener(e -> processBooking());
        backBtn.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to go back? Unsaved changes will be lost.", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                this.dispose(); // Dispose of the current BookingPanel
                new HotelSystemGUI(); // Create a new instance of HotelSystemGUI
            }
        });

        return btnPanel;
    }

    private void updateAvailableLabel() {
        String roomType = (String) roomTypeCombo.getSelectedItem();
        int available = AdminPanel.getAvailability(roomType);
        availableLabel.setText("Available: " + available + " " + roomType + (available == 1 ? " room" : " rooms"));
    }

    private void updateRoomImage() {
        String roomType = (String) roomTypeCombo.getSelectedItem();
        String imagePath = "";

        switch (roomType) {
            case "Suite":
                imagePath = "lib/image/suite_image.jpg"; // Replace with actual image path
                break;
            case "Deluxe":
                imagePath = "lib/image/deluxe_image.jpg"; // Replace with actual image path
                break;
            case "Standard":
                imagePath = "lib/image/standard_image.jpg"; // Replace with actual image path
                break;
        }

        ImageIcon originalIcon = new ImageIcon(imagePath);
        // Scale image to fit nicely within 300x300 box
        Image scaledImg = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        roomImageLabel.setIcon(scaledIcon);
        roomImageLabel.setText(""); // Clear text
    }

    private void addListenersToUpdateSummary() {
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateSummary();
            }
        };
        nameField.addKeyListener(keyAdapter);
        emailField.addKeyListener(keyAdapter);
        phoneField.addKeyListener(keyAdapter);

        checkInDatePicker.getModel().addChangeListener(e -> updateSummary());
        checkOutDatePicker.getModel().addChangeListener(e -> updateSummary());
        branchCombo.addActionListener(e -> {
            updateAvailableLabel();
            updateSummary();
        });
        roomTypeCombo.addActionListener(e -> {
            updateAvailableLabel();
            updateRoomImage();
            updateSummary();
        });
        mealCheck.addActionListener(e -> updateSummary());
        wifiCheck.addActionListener(e -> updateSummary());
        laundryCheck.addActionListener(e -> updateSummary());
    }

    private void updateSummary() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Date checkInDate = (Date) checkInDatePicker.getModel().getValue();
        Date checkOutDate = (Date) checkOutDatePicker.getModel().getValue();
        String branch = (String) branchCombo.getSelectedItem();
        String roomType = (String) roomTypeCombo.getSelectedItem();

        if (checkInDate == null || checkOutDate == null) {
            summaryArea.setText("Please select check-in and check-out dates.");
            return;
        }

        int price = roomPrices.get(roomType);
        StringBuilder addons = new StringBuilder();
        if (mealCheck.isSelected()) {
            price += mealPrice;
            addons.append("Meals, ");
        }
        if (wifiCheck.isSelected()) {
            price += wifiPrice;
            addons.append("High-Speed WiFi, ");
        }
        if (laundryCheck.isSelected()) {
            price += laundryPrice;
            addons.append("Laundry, ");
        }

        String addonStr = addons.length() > 0 ? addons.substring(0, addons.length() - 2) : "None";

        String summary = String.format(
                "Booking Summary:\n\n" +
                        "Name: %s\nEmail: %s\nPhone: %s\n\nCheck-in Date: %s\nCheck-out Date: %s\n\nBranch: %s\nRoom Type: %s\nAdd-ons: %s\n\nTotal Price: RM %d\n",
                name, email, phone,
                new SimpleDateFormat("yyyy-MM-dd").format(checkInDate),
                new SimpleDateFormat("yyyy-MM-dd").format(checkOutDate),
                branch, roomType, addonStr, price);

        summaryArea.setText(summary);
    }

    private void processBooking() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Date checkInDate = (Date) checkInDatePicker.getModel().getValue();
        Date checkOutDate = (Date) checkOutDatePicker.getModel().getValue();
        String branch = (String) branchCombo.getSelectedItem();
        String roomType = (String) roomTypeCombo.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || checkInDate == null || checkOutDate == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkInDate.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Check-in date cannot be in the past!", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (checkOutDate.before(checkInDate)) {
            JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date!", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int price = roomPrices.get(roomType);
        StringBuilder addons = new StringBuilder();
        if (mealCheck.isSelected()) {
            price += mealPrice;
            addons.append("Meals, ");
        }
        if (wifiCheck.isSelected()) {
            price += wifiPrice;
            addons.append("High-Speed WiFi, ");
        }
        if (laundryCheck.isSelected()) {
            price += laundryPrice;
            addons.append("Laundry, ");
        }

        String addonStr = addons.length() > 0 ? addons.substring(0, addons.length() - 2) : "None";

        String summary = String.format(
                "Booking Summary:\n\n" +
                        "Name: %s\nEmail: %s\nPhone: %s\n\nCheck-in Date: %s\nCheck-out Date: %s\n\nBranch: %s\nRoom Type: %s\nAdd-ons: %s\n\nTotal Price: RM %d\n",
                name, email, phone,
                new SimpleDateFormat("yyyy-MM-dd").format(checkInDate),
                new SimpleDateFormat("yyyy-MM-dd").format(checkOutDate),
                branch, roomType, addonStr, price);

        summaryArea.setText(summary);
        BookingStorage.addBooking(summary);

        // Show success message
        JOptionPane.showMessageDialog(this, "Booking submitted successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE);

    private boolean isValidEmail(String email) {
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches();
    }

    // DateLabelFormatter class for formatting the date in JDatePicker
    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}