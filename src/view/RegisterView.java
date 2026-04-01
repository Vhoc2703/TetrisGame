package view;

import controller.AuthController;
import model.User;
import utils.GameException;

import javax.swing.*;
import java.awt.*;

/**
 * Giao diện đăng ký tài khoản.
 */
public class RegisterView extends JFrame {
    
    private final AuthController authController;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;
    
    public RegisterView() {
        this.authController = new AuthController();
        initialize();
    }
    
    private void initialize() {
        setTitle("Đăng ký - Tetris Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);  // Tăng chiều cao lên 550 để chứa đủ 5 field
        setLocationRelativeTo(null);
        setResizable(false);
        
        add(createMainPanel());
        setupListeners();
        setVisible(true);
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Title - chiếm 2 cột
        JLabel titleLabel = createTitleLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Reset gridwidth cho các field
        gbc.gridwidth = 1;
        
        int row = 1;
        
        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createLabel("Tên đăng nhập:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);
        row++;
        
        // Họ và tên
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createLabel("Họ và tên:"), gbc);
        
        gbc.gridx = 1;
        fullNameField = new JTextField(15);
        panel.add(fullNameField, gbc);
        row++;
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        emailField = new JTextField(15);
        panel.add(emailField, gbc);
        row++;
        
        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        row++;
        
        // Xác nhận mật khẩu
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createLabel("Xác nhận mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        confirmField = new JPasswordField(15);
        panel.add(confirmField, gbc);
        row++;
        
        // Buttons - chiếm 2 cột
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        panel.add(createButtonPanel(), gbc);
        
        return panel;
    }
    
    private JLabel createTitleLabel() {
        JLabel label = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(0x4ECDC4));
        return label;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panel.setOpaque(false);
        
        registerButton = createButton("Đăng ký", new Color(0x4ECDC4));
        backButton = createButton("Quay lại", new Color(0x95A5A6));
        
        panel.add(registerButton);
        panel.add(backButton);
        return panel;
    }
    
    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }
    
    private void setupListeners() {
        registerButton.addActionListener(e -> handleRegister());
        backButton.addActionListener(e -> goBack());
    }
    
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmField.getPassword());
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        
        if (!validateInput(username, password, confirm, fullName, email)) {
            return;
        }
        
        User newUser = new User(username, password, fullName, email);
        
        try {
            if (authController.register(newUser)) {
                showMessage("Đăng ký thành công! Vui lòng đăng nhập.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                goBack();
            }
        } catch (GameException e) {
            showMessage(e.getMessage(), "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateInput(String username, String password, String confirm, String fullName, String email) {
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            showMessage("Vui lòng điền đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!password.equals(confirm)) {
            showMessage("Mật khẩu xác nhận không khớp!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (password.length() < 6) {
            showMessage("Mật khẩu phải có ít nhất 6 ký tự!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (!email.contains("@")) {
            showMessage("Email không hợp lệ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void goBack() {
        new LoginView();
        dispose();
    }
    
    private void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
    
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0, 0, new Color(0x1A1F2E), 
                getWidth(), getHeight(), new Color(0x2C3E50));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}