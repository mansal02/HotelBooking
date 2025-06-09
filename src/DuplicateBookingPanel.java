import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DuplicateBookingPanel extends JFrame {

    private JPanel bookingButtonPanel;
    private JTextArea bookingDetailsArea;
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

        bookingDetailsArea = new JTextArea();
        bookingDetailsArea.setEditable(false);
        bookingDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailScrollPane = new JScrollPane(bookingDetailsArea);
        add(detailScrollPane, BorderLayout.CENTER);

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
        // Extract room type or ID from bookingInfo to determine the image
        String roomType = extractRoomType(bookingInfo);
        String imagePath = "images/" + roomType + ".png"; // Assuming images are stored in an "images" folder

        // Create a new JLabel to display the image
        JLabel imageLabel = new JLabel();
        try {
            ImageIcon roomImage = new ImageIcon(imagePath);
            imageLabel.setIcon(roomImage);
            bookingDetailsArea.setText(bookingDetailsArea.getText() + "\n\nRoom Image:");
            bookingDetailsArea.append("\n" + imageLabel.getIcon().toString()); // Placeholder for image display
        } catch (Exception e) {
            bookingDetailsArea.append("\nRoom image not available.");
        }
    }

    private String extractRoomType(String bookingInfo) {
        // Logic to extract room type from bookingInfo
        // This is a placeholder; adjust according to your bookingInfo format
        return "roomType"; // Replace with actual extraction logic
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
