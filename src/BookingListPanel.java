import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingListPanel extends JFrame {

    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton backButton;
    private JButton searchButton;

    // Column names for the bookings table
    private static final String[] COLUMN_NAMES = {
            "Name",
            "Email",
            "Phone",
            "Date of Stay",
            "Branch",
            "Room Type",
            "Add-ons",
            "Total Price"
    };

    public BookingListPanel() {
        setTitle("All Bookings");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        // Create top panel with search field and search button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(Color.WHITE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        searchLabel.setForeground(new Color(106, 114, 128)); // #6a7280 neutral gray

        searchField = new JTextField(30);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 16));

        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Poppins", Font.BOLD, 14));
        searchButton.setBackground(new Color(60, 120, 220));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topPanel.add(searchLabel);
        topPanel.add(searchField);
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        // Create the table with proper columns and no rows initially
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        bookingsTable = new JTable(tableModel);
        bookingsTable.setFillsViewportHeight(true);
        bookingsTable.setRowHeight(30);
        bookingsTable.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 16));
        bookingsTable.getTableHeader().setBackground(new Color(240, 240, 240));
        bookingsTable.getTableHeader().setForeground(new Color(30, 30, 30));
        bookingsTable.setFont(new Font("Poppins", Font.PLAIN, 15));
        bookingsTable.setSelectionBackground(new Color(173, 216, 230)); // light blue selection

        // Alternate row colors
        bookingsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color evenColor = new Color(255, 255, 255);
            private final Color oddColor = new Color(245, 245, 245);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(bookingsTable.getSelectionBackground());
                } else {
                    c.setBackground(row % 2 == 0 ? evenColor : oddColor);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                scrollPane.getBorder()
        ));
        add(scrollPane, BorderLayout.CENTER);

        // Create bottom panel with back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setBackground(Color.WHITE);
        backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Poppins", Font.BOLD, 14));
        backButton.setBackground(new Color(100, 150, 255));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load bookings into table
        loadBookings();

        // Add action listeners
        backButton.addActionListener(e -> {
            this.dispose();
            new HotelSystemGUI();
        });

        searchButton.addActionListener(e -> searchBookings());
        searchField.addActionListener(e -> searchBookings());

        setVisible(true);
    }

    // Load all bookings into the table
    private void loadBookings() {
        List<String> bookings = BookingStorage.getBookings();
        tableModel.setRowCount(0); // Clear existing rows

        if (bookings.isEmpty()) {
            // No bookings found row with colspan effect by clearing and showing message below table
            tableModel.addRow(new Object[] { "No bookings found.", "", "", "", "", "", "", "" });
        } else {
            for (String booking : bookings) {
                Object[] row = parseBookingSummary(booking);
                tableModel.addRow(row);
            }
        }
    }

    // Search bookings with a search term, filtering rows that contain the term in any column
    private void searchBookings() {
        String searchTerm = searchField.getText().trim().toLowerCase();
        List<String> bookings = BookingStorage.getBookings();
        tableModel.setRowCount(0); // Clear existing rows

        for (String booking : bookings) {
            Object[] row = parseBookingSummary(booking);
            boolean matchFound = false;
            for (Object cellValue : row) {
                if (cellValue != null && cellValue.toString().toLowerCase().contains(searchTerm)) {
                    matchFound = true;
                    break;
                }
            }
            if (matchFound) {
                tableModel.addRow(row);
            }
        }

        if (tableModel.getRowCount() == 0) {
            tableModel.addRow(new Object[]{"No bookings found.", "", "", "", "", "", "", ""});
        }
    }

    // Parse booking summary text to extract fields: Name, Email, Phone, Date of Stay, Branch, Room Type, Add-ons, Total Price
    private Object[] parseBookingSummary(String booking) {
        String name = extractLineValue(booking, "Name");
        String email = extractLineValue(booking, "Email");
        String phone = extractLineValue(booking, "Phone");
        String dateOfStay = extractLineValue(booking, "Date of Stay");
        String branch = extractLineValue(booking, "Branch");
        String roomType = extractLineValue(booking, "Room Type");
        String addons = extractLineValue(booking, "Add-ons");
        String totalPrice = extractLineValue(booking, "Total Price");

        return new Object[]{
                name != null ? name : "",
                email != null ? email : "",
                phone != null ? phone : "",
                dateOfStay != null ? dateOfStay : "",
                branch != null ? branch : "",
                roomType != null ? roomType : "",
                addons != null ? addons : "",
                totalPrice != null ? totalPrice : ""
        };
    }

    // Helper method to extract value for a line prefix, e.g., "Name: value"
    private String extractLineValue(String text, String prefix) {
        Pattern pattern = Pattern.compile("^" + Pattern.quote(prefix) + "\\s*:\\s*(.+)$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}

