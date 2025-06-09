import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;
import org.jdatepicker.impl.*; // Import JDatePicker

public class EditDuplicateBooking extends JFrame {
    private JTextField nameField, emailField, phoneField;
    private JComboBox<String> branchCombo, roomTypeCombo;
    private JCheckBox mealCheck, wifiCheck, laundryCheck;
    private JTextArea summaryArea;
    private JButton saveBtn, cancelBtn;
    private JDatePickerImpl datePicker; // JDatePicker for date selection
    private JLabel roomImageLabel; // Label to display room images

    private final Map<String, Integer> roomPrices = Map.of(
            "Suite", 200,
            "Deluxe", 150,
            "Standard", 100);

    private final int mealPrice = 20;
    private final int wifiPrice = 10;
    private final int laundryPrice = 15;

    private String originalBookingSummary = null;

    public EditDuplicateBooking() {
        setTitle("Edit Duplicate Booking");
        setSize(900, 750); // Increased size for better layout
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(Color.WHITE);

        // Create main container with padding
        JPanel mainContainer = new JPanel();
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setLayout(new GridBagLayout());
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

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
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add form fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Customer Name:"), gbc);
        nameField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Phone Number:"), gbc);
        phoneField = new JTextField();
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Date of Stay:"), gbc);
        datePicker = createDatePicker();
        gbc.gridx = 1;
        formPanel.add(datePicker, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Branch:"), gbc);
        branchCombo = new JComboBox<>(new String[] { "CityHotel", "BeachResort" });
        gbc.gridx = 1;
        formPanel.add(branchCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Room Type:"), gbc);
        roomTypeCombo = new JComboBox<>(new String[] { "Suite", "Deluxe", "Standard" });
        gbc.gridx = 1;
        formPanel.add(roomTypeCombo, gbc);

        // Add-ons
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(new JLabel("Add-ons:"), gbc);
        JPanel addonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        mealCheck = new JCheckBox("Meals (+$20)");
        wifiCheck = new JCheckBox("High-Speed WiFi (+$10)");
        laundryCheck = new JCheckBox("Laundry (+$15)");
        addonPanel.add(mealCheck);
        addonPanel.add(wifiCheck);
        addonPanel.add(laundryCheck);
        gbc.gridx = 1;
        formPanel.add(addonPanel, gbc);

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
        saveBtn = new JButton("Save Booking");
        cancelBtn = new JButton("Cancel");
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        saveBtn.addActionListener(e -> saveBooking());
        cancelBtn.addActionListener(e -> {
            this.dispose();
            new DuplicateBookingPanel();
        });

        return btnPanel;
    }

    private void saveBooking() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Date date = (Date) datePicker.getModel().getValue();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || date == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Email validation (simple regex)
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address!", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (date.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Date cannot be in the past!", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update or add booking
        String newSummary = summaryArea.getText();

        java.util.List<String> bookings = BookingStorage.getBookings();
        if (originalBookingSummary != null) {
            // Replace the original booking summary with the updated one
            int index = bookings.indexOf(originalBookingSummary);
            if (index >= 0) {
                bookings.set(index, newSummary);
            } else {
                bookings.add(newSummary);
            }
        } else {
            bookings.add(newSummary);
        }

        JOptionPane.showMessageDialog(this, "Booking saved successfully.");
        this.dispose();
        // Ensure DuplicateBookingPanel exists in your project
        try {
            new DuplicateBookingPanel();
        } catch (Exception ex) {
            // Handle or log error if DuplicateBookingPanel is not found
        }
    }

    private boolean isValidEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);
        return VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches();
    }

    public void populateFields(String bookingInfo) {
        clearFields();
        originalBookingSummary = bookingInfo;
        try {
            String[] lines = bookingInfo.split("\n");
            for (String line : lines) {
                if (line.startsWith("Name:")) {
                    nameField.setText(line.substring(5).trim());
                } else if (line.startsWith("Email:")) {
                    emailField.setText(line.substring(6).trim());
                } else if (line.startsWith("Phone:")) {
                    phoneField.setText(line.substring(6).trim());
                } else if (line.startsWith("Date of Stay:")) {
                    String dateStr = line.substring(13).trim();
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    datePicker.getModel().setDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));
                    datePicker.getModel().setSelected(true);
                } else if (line.startsWith("Branch:")) {
                    String branch = line.substring(7).trim();
                    branchCombo.setSelectedItem(branch);
                } else if (line.startsWith("Room Type:")) {
                    String room = line.substring(10).trim();
                    roomTypeCombo.setSelectedItem(room);
                } else if (line.startsWith("Add-ons:")) {
                    String addons = line.substring(7).trim().toLowerCase();
                    mealCheck.setSelected(addons.contains("meals"));
                    wifiCheck.setSelected(addons.contains("wifi"));
                    laundryCheck.setSelected(addons.contains("laundry"));
                }
            }
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Failed to parse booking info. Invalid date format.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to parse booking info.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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

        // Load and set the image, check if file exists
        java.io.File imgFile = new java.io.File(imagePath);
        if (imgFile.exists()) {
            ImageIcon roomImage = new ImageIcon(imagePath);
            roomImageLabel.setIcon(roomImage);
            roomImageLabel.setText(""); // Clear text if any
        } else {
            roomImageLabel.setIcon(null);
            roomImageLabel.setText("No Image Available");
        }
    }

    private void updateSummary() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        Date date = (Date) datePicker.getModel().getValue();
        String branch = (String) branchCombo.getSelectedItem();
        String roomType = (String) roomTypeCombo.getSelectedItem();

        int price = roomPrices.getOrDefault(roomType, 0);
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

        String dateStr = (date != null) ? new SimpleDateFormat("yyyy-MM-dd").format(date) : "";
        String summary = String.format(
                "Booking Summary:\n" +
                        "Name: %s\nEmail: %s\nPhone: %s\nDate of Stay: %s\nBranch: %s\nRoom Type: %s\nAdd-ons: %s\nTotal Price: $%d\n",
                name, email, phone, dateStr, branch, roomType, addonStr, price);
        summaryArea.setText(summary);
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        datePicker.getModel().setValue(null);
        branchCombo.setSelectedIndex(0);
        roomTypeCombo.setSelectedIndex(0);
        mealCheck.setSelected(false);
        wifiCheck.setSelected(false);
        laundryCheck.setSelected(false);
        summaryArea.setText("");
        originalBookingSummary = null;
        roomImageLabel.setIcon(null); // Clear the image
    }

    // Add listeners to update summary in real-time
    private void addListenersToUpdateSummary() {
        nameField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updateSummary));
        emailField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updateSummary));
        phoneField.getDocument().addDocumentListener(new SimpleDocumentListener(this::updateSummary));
        mealCheck.addActionListener(e -> updateSummary());
        wifiCheck.addActionListener(e -> updateSummary());
        laundryCheck.addActionListener(e -> updateSummary());
        branchCombo.addActionListener(e -> updateSummary());
        roomTypeCombo.addActionListener(e -> updateSummary());
        datePicker.getModel().addChangeListener(e -> updateSummary());
    }

    // Dummy implementation for updateAvailableLabel (since no label is defined)
    private void updateAvailableLabel() {
        // You can implement logic here if you have an availability label to update.
        // For now, this is a placeholder to avoid compilation errors.
    }

    // Helper class for document listener
    private static class SimpleDocumentListener implements javax.swing.event.DocumentListener {
        private final Runnable onChange;

        public SimpleDocumentListener(Runnable onChange) {
            this.onChange = onChange;
        }

        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            onChange.run();
        }

        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            onChange.run();
        }

        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            onChange.run();
        }
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
