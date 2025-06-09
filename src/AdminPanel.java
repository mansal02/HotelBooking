import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AdminPanel extends JFrame {

    private JComboBox<String> roomTypeCombo;
    private JTextField quantityField;
    private JButton updateButton, backButton;

    // Store availability in a map roomType -> quantity (initialized to 1)
    private static Map<String, Integer> roomAvailability = new HashMap<>();

    static {
        roomAvailability.put("Suite", 1);
        roomAvailability.put("Deluxe", 1);
        roomAvailability.put("Standard", 1);
    }

    public AdminPanel() {
        setTitle("Admin: Set Room Availability");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Use modern layout and styling
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        contentPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Set Room Availability");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 1;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Select Room Type:"), gbc);

        roomTypeCombo = new JComboBox<>(new String[] { "Suite", "Deluxe", "Standard" });
        roomTypeCombo.setFont(new Font("Poppins", Font.PLAIN, 16));
        gbc.gridx = 1;
        contentPanel.add(roomTypeCombo, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        contentPanel.add(new JLabel("Set Quantity Available:"), gbc);

        quantityField = new JTextField(10);
        quantityField.setFont(new Font("Poppins", Font.PLAIN, 16));
        gbc.gridx = 1;
        contentPanel.add(quantityField, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;

        updateButton = new JButton("Update Availability");
        styleButton(updateButton);
        contentPanel.add(updateButton, gbc);

        gbc.gridx = 1;
        backButton = new JButton("Back to Home");
        styleButton(backButton);
        contentPanel.add(backButton, gbc);

        add(contentPanel);

        // Pre-fill quantity field with current availability of selected room
        updateQuantityField();

        roomTypeCombo.addActionListener(e -> updateQuantityField());

        updateButton.addActionListener(e -> updateAvailability());

        backButton.addActionListener(e -> {
            this.dispose();
            new HotelSystemGUI();
        });

        setVisible(true);
    }

    private void updateQuantityField() {
        String roomType = (String) roomTypeCombo.getSelectedItem();
        quantityField.setText(String.valueOf(getAvailability(roomType)));
    }

    private void updateAvailability() {
        String roomType = (String) roomTypeCombo.getSelectedItem();
        String qtyStr = quantityField.getText().trim();

        if (qtyStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty < 0) {
                JOptionPane.showMessageDialog(this, "Quantity cannot be negative", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            roomAvailability.put(roomType, qty);
            JOptionPane.showMessageDialog(this, roomType + " availability updated to " + qty);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity number", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Static method to get availability (used in BookingPanel or elsewhere)
    public static int getAvailability(String roomType) {
        return roomAvailability.getOrDefault(roomType, 1);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 120, 220));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Poppins", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
