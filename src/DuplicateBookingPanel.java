import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DuplicateBookingPanel extends JFrame {

    private JPanel bookingButtonPanel;
    private JPanel bookingDetailsPanel; // Changed to JPanel
    private JTextArea bookingDetailsArea;
    private JLabel roomImageLabel; // JLabel for the room image
    private JButton duplicateBtn, deleteBtn, homeBtn, searchBtn;
    private JTextField searchField;
    private JButton selectedBookingButton; // To keep track of the selected booking button

    public DuplicateBookingPanel() {
        setTitle("Duplicate Booking");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Search Panel
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(15);
        searchBtn = new JButton("Search");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        add(searchPanel, BorderLayout.NORTH);

        bookingButtonPanel = new JPanel();
        bookingButtonPanel.setLayout(new GridLayout(0, 1, 10, 10)); // Vertical layout for buttons with spacing
        JScrollPane buttonScrollPane = new JScrollPane(bookingButtonPanel);
        buttonScrollPane.setPreferredSize(new Dimension(250, 0));
        add(buttonScrollPane, BorderLayout.WEST);

        // Booking details panel
        bookingDetailsPanel = new JPanel();
        bookingDetailsPanel.setLayout(new BorderLayout());
        bookingDetailsArea = new JTextArea();
        bookingDetailsArea.setEditable(false);
        bookingDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailScrollPane = new JScrollPane(bookingDetailsArea);
        bookingDetailsPanel.add(detailScrollPane, BorderLayout.CENTER);

        roomImageLabel = new JLabel(); // Initialize the JLabel for the image
        bookingDetailsPanel.add(roomImageLabel, BorderLayout.SOUTH); // Add image label to the panel
        add(bookingDetailsPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        duplicateBtn = new JButton("Duplicate Booking");
        deleteBtn = new JButton("Delete Booking");
        homeBtn = new JButton("Back to Home");
        btnPanel.add(duplicateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(homeBtn);
        add(btnPanel, BorderLayout.SOUTH);

        // Action Listeners
        duplicateBtn.addActionListener(e -> duplicateBooking());
        deleteBtn.addActionListener(e -> deleteBooking());
        homeBtn.addActionListener(e -> {
            this.dispose();
            new HotelSystemGUI();
        });
        searchBtn.addActionListener(e -> refreshBookingButtons());

        refreshBookingButtons();

        setVisible(true);
    }

    private void selectBooking(JButton bookingButton, String bookingInfo) {
        if (selectedBookingButton != null) {
            selectedBookingButton.setBackground(null);
        }
        selectedBookingButton = bookingButton;
        selectedBookingButton.setBackground(Color.LIGHT_GRAY);
        bookingDetailsArea.setText(bookingInfo);
        // Load and display the associated room image
        displayRoomImage(bookingInfo);
    }

    private void displayRoomImage(String bookingInfo) {
        String roomType = getRoomTypeFromBookingInfo(bookingInfo);
        if (roomType != null) {
            roomType = roomType.toLowerCase();
        }

        if (roomType == null || roomType.isEmpty()) {
            bookingDetailsArea.append("\nRoom image not available (room type missing).");
            roomImageLabel.setIcon(null); // Clear the image if not available
            return;
        }

        String imagePath = "lib/image/" + roomType + "_image.jpg"; // Adjust path as needed

        try {
            ImageIcon roomImage = new ImageIcon(imagePath);
            if (roomImage.getIconWidth() > 0) {
                roomImageLabel.setIcon(roomImage); // Set the image in the JLabel
            } else {
                bookingDetailsArea.append("\nRoom image not available.");
                roomImageLabel.setIcon(null); // Clear the image if not available
            }
        } catch (Exception e) {
            bookingDetailsArea.append("\nRoom image not available.");
            roomImageLabel.setIcon(null); // Clear the image if not available
        }
    }

    // Logic copied from BookingListPanel to extract room type
    private String getRoomTypeFromBookingInfo(String bookingInfo) {
        for (String line : bookingInfo.split("\n")) {
            if (line.toLowerCase().startsWith("room type:")) {
                return line.substring("room type:".length()).trim();
            }
        }
        return null;
    }

    private void duplicateBooking() {
        if (selectedBookingButton == null) {
            JOptionPane.showMessageDialog(this, "Please select a booking to duplicate.");
            return;
        }

        String bookingInfo = bookingDetailsArea.getText();
        // Open BookingPanel prefilled
        EditDuplicateBooking bookingPanel = new EditDuplicateBooking();
        bookingPanel.populateFields(bookingInfo);
        bookingPanel.setVisible(true);
        this.dispose();
    }

    private void deleteBooking() {
        if (selectedBookingButton == null) {
            JOptionPane.showMessageDialog(this, "Please select a booking to delete.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this booking?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            int index = Integer.parseInt(selectedBookingButton.getText().split("#")[1].split(" ")[0]) - 1;
            List<String> bookings = BookingStorage.getBookings();
            if (index >= 0 && index < bookings.size()) {
                bookings.remove(index);
            }

            bookingDetailsArea.setText("");
            roomImageLabel.setIcon(null); // Clear the image when booking is deleted
            selectedBookingButton = null;
            refreshBookingButtons();
            JOptionPane.showMessageDialog(this, "Booking deleted successfully!");
        }
    }

    private void refreshBookingButtons() {
        bookingButtonPanel.removeAll();
        List<String> bookings = BookingStorage.getBookings();
        String searchQuery = searchField.getText().toLowerCase();

        for (int i = 0; i < bookings.size(); i++) {
            String bookingInfo = bookings.get(i);
            String brief = getBriefDetail(bookingInfo);
            if (brief.toLowerCase().contains(searchQuery)) { // Filter based on search query
                JButton btn = new JButton("Booking #" + (i + 1) + " - " + brief);
                btn.setHorizontalAlignment(SwingConstants.LEFT);
                btn.addActionListener(e -> selectBooking(btn, bookingInfo));
                bookingButtonPanel.add(btn);
            }
        }

        bookingButtonPanel.revalidate();
        bookingButtonPanel.repaint();

        // If any bookings exist, select the first by default
        if (!bookings.isEmpty()) {
            Component firstButton = bookingButtonPanel.getComponent(0);
            if (firstButton instanceof JButton) {
                ((JButton) firstButton).doClick();
            }
        } else {
            bookingDetailsArea.setText("No bookings available.");
            roomImageLabel.setIcon(null); // Clear the image if no bookings
        }
    }

    private String getBriefDetail(String bookingInfo) {
        try {
            String[] lines = bookingInfo.split("\n");
            String nameLine = lines[1];
            String dateLine = lines[4];

            String name = nameLine.split(": ", 2)[1];
            String date = dateLine.split(": ", 2)[1];
            return name + " on " + date;
        } catch (Exception e) {
            return "Details unavailable";
        }
    }
}
