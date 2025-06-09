import javax.swing.*;
import java.awt.*;

public class HotelSystemGUI extends JFrame {

    public HotelSystemGUI() {
        setTitle("Hotel Booking System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set a background color
        getContentPane().setBackground(new Color(240, 240, 240));

        JLabel welcomeLabel = new JLabel("Welcome to the Hotel Booking System", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(50, 50, 50));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton bookingBtn = createStyledButton("1. Make New Booking");
        JButton adminBtn = createStyledButton("2. Admin: Set Room Availability");
        JButton showBookingBtn = createStyledButton("3. Show All Bookings");
        JButton duplicateBtn = createStyledButton("4. Duplicate Booking");

        // Add action listeners to buttons
        bookingBtn.addActionListener(e -> new BookingPanel());
        adminBtn.addActionListener(e -> new AdminLoginPanel());
        showBookingBtn.addActionListener(e -> new BookingListPanel());
        duplicateBtn.addActionListener(e -> new DuplicateBookingPanel());

        buttonPanel.add(bookingBtn);
        buttonPanel.add(adminBtn);
        buttonPanel.add(showBookingBtn);
        buttonPanel.add(duplicateBtn);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 16));
        button.setBackground(new Color(100, 150, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText("Click to " + text.toLowerCase());

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelSystemGUI::new);
    }
}
