package view;

import model.User;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Giao diện menu chính sau khi đăng nhập.
 */
public class MainMenuView extends JFrame {
    
    private final User currentUser;
    
    private JButton playButton;
    private JButton highScoreButton;
    private JButton manageUsersButton;
    private JButton logoutButton;
    
    public MainMenuView(User user) {
        this.currentUser = user;
        SessionManager.getInstance().setCurrentUser(user);
        initialize();
    }
    
    private void initialize() {
        setTitle("Tetris Game - Menu chính");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        add(createMainPanel());
        setupListeners();
        setVisible(true);
    }
    
    private JPanel createMainPanel() {
        JPanel panel = new GradientPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        panel.add(createHeaderPanel(), BorderLayout.NORTH);
        panel.add(createButtonPanel(), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("TETRIS GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(0x4ECDC4));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel welcomeLabel = new JLabel("Chào mừng, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel roleLabel = new JLabel("Vai trò: " + currentUser.getRole());
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        roleLabel.setForeground(new Color(0xE67E22));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(roleLabel);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 20, 10, 20);
        
        playButton = createMenuButton("🎮 BẮT ĐẦU CHƠI", new Color(0x4ECDC4));
        highScoreButton = createMenuButton("🏆 BẢNG XẾP HẠNG", new Color(0xE67E22));
        
        gbc.gridy = 0;
        panel.add(playButton, gbc);
        
        gbc.gridy = 1;
        panel.add(highScoreButton, gbc);
        
        int nextRow = 2;
        if (currentUser.isAdmin()) {
            manageUsersButton = createMenuButton("👥 QUẢN LÝ NGƯỜI DÙNG", new Color(0x9B59B6));
            gbc.gridy = nextRow++;
            panel.add(manageUsersButton, gbc);
        }
        
        logoutButton = createMenuButton("🚪 ĐĂNG XUẤT", new Color(0xE74C3C));
        gbc.gridy = nextRow;
        panel.add(logoutButton, gbc);
        
        return panel;
    }
    
    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new HoverEffect(color));
        return button;
    }
    
    private void setupListeners() {
        playButton.addActionListener(e -> startGame());
        highScoreButton.addActionListener(e -> showHighScores());
        logoutButton.addActionListener(e -> logout());
        
        if (manageUsersButton != null) {
            manageUsersButton.addActionListener(e -> openUserManagement());
        }
    }
    
    private void startGame() {
        new GameView(currentUser).setVisible(true);
        dispose();
    }
    
    private void showHighScores() {
        new HighScoreView().setVisible(true);
    }
    
    private void openUserManagement() {
        new UserManagementView().setVisible(true);
        dispose();
    }
    
    private void logout() {
        SessionManager.getInstance().clearSession();
        new LoginView();
        dispose();
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
    
    private static class HoverEffect extends java.awt.event.MouseAdapter {
        private final Color originalColor;
        
        HoverEffect(Color color) {
            this.originalColor = color;
        }
        
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) {
            ((JButton) e.getSource()).setBackground(originalColor.darker());
        }
        
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) {
            ((JButton) e.getSource()).setBackground(originalColor);
        }
    }
}