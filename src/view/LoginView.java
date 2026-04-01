package view;

import controller.AuthController;
import model.User;
import utils.GameException;

import javax.swing.*;
import java.awt.*;

/**
 * Giao diện đăng nhập.
 */
public class LoginView extends JFrame {
    
    private final AuthController authController;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    
    public LoginView() {
        this.authController = new AuthController();
        initialize();
    }
    
    private void initialize() {
        setTitle("Đăng nhập - Tetris Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        
        add(createMainPanel());
        setupListeners();
        setVisible(true);
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = createTitleLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Username
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(createLabel("Tên đăng nhập:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        panel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createLabel("Mật khẩu:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        panel.add(passwordField, gbc);
        
        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(createButtonPanel(), gbc);
        
        return panel;
    }
    
    private JLabel createTitleLabel() {
        JLabel label = new JLabel("TETRIS GAME", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 28));
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
        
        loginButton = createButton("Đăng nhập", new Color(0x4ECDC4));
        registerButton = createButton("Đăng ký", new Color(0xE67E22));
        
        panel.add(loginButton);
        panel.add(registerButton);
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
        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegister());
        passwordField.addActionListener(e -> handleLogin());
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            User user = authController.login(username, password);
            showMessage("Đăng nhập thành công! Chào mừng " + user.getFullName(), 
                       "Thành công", JOptionPane.INFORMATION_MESSAGE);
            new MainMenuView(user);
            dispose();
        } catch (GameException e) {
            showMessage(e.getMessage(), "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegister() {
        new RegisterView();
        dispose();
    }
    
    private void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
    
    /**
     * Panel có gradient background.
     */
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