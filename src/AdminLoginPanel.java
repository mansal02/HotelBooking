import javax.swing.*;
import java.awt.*;

public class AdminLoginPanel extends JFrame {

    private JPasswordField passwordField;
    private JButton loginButton, backButton;

    public AdminLoginPanel() {
        setTitle("Admin Login");
        setSize(380, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Background panel with padding and background color
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Gradual background gradient
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(245, 245, 255);
                Color color2 = new Color(200, 210, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        add(backgroundPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 26));
        titleLabel.setForeground(new Color(30, 30, 60));

        JLabel promptLabel = new JLabel("Enter Admin Password:");
        promptLabel.setFont(new Font("Poppins", Font.PLAIN, 16));
        promptLabel.setForeground(new Color(50, 50, 80));

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 16));

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Poppins", Font.BOLD, 16));
        loginButton.setBackground(new Color(60, 120, 220));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backButton = new JButton("Back to Home");
        backButton.setFont(new Font("Poppins", Font.PLAIN, 14));
        backButton.setBackground(new Color(200, 200, 210));
        backButton.setForeground(new Color(80, 80, 90));
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(promptLabel, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        backgroundPanel.add(passwordField, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        backgroundPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        backgroundPanel.add(backButton, gbc);

        loginButton.addActionListener(e -> checkPassword());
        backButton.addActionListener(e -> {
            this.dispose();
            new HotelSystemGUI();
        });

        // Enter key triggers login
        passwordField.addActionListener(e -> checkPassword());

        setVisible(true);
    }

    private void checkPassword() {
        String input = new String(passwordField.getPassword());
        if ("admin123".equals(input)) {
            JOptionPane.showMessageDialog(this, "Access Granted");
            this.dispose();
            new AdminPanel();
        } else {
            JOptionPane.showMessageDialog(this, "Incorrect Password", "Access Denied", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
