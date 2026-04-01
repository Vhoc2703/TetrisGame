package view;

import controller.GameController;
import controller.KeyHandler;
import model.*;
import utils.SessionManager;

import javax.swing.*;
import java.awt.*;

/**
 * Giao diện game chính.
 */
public class GameView extends JFrame implements GameStateListener {
    
    private final GameController controller;
    private final TetrisModel model;
    private final GamePanel gamePanel;
    private final ScorePanel scorePanel;
    private final NextPiecePanel nextPiecePanel;
    private final GameMenuBar menuBar;
    
    private boolean scoreSaved;
    
    public GameView(User user) {
        this.model = new TetrisModel(user);
        this.model.addListener(this);
        this.controller = new GameController(model);
        
        this.gamePanel = new GamePanel(model, controller);
        this.scorePanel = new ScorePanel(model);
        this.nextPiecePanel = new NextPiecePanel(model);
        this.menuBar = new GameMenuBar(controller, model);
        
        this.scoreSaved = false;
        
        initialize();
    }
    
    private void initialize() {
        setTitle("Tetris Game - " + model.getCurrentUser().getUsername());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        setJMenuBar(menuBar);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
        
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        
        controller.startGame();
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.stopGame();
                new MainMenuView(SessionManager.getInstance().getCurrentUser());
            }
        });
        
        setVisible(true);
    }
    
    private JPanel createCenterPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(0x2C3E50));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        rightPanel.add(scorePanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(nextPiecePanel);
        rightPanel.add(Box.createVerticalGlue());
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        centerPanel.setBackground(new Color(0x2C3E50));
        centerPanel.add(gamePanel);
        centerPanel.add(rightPanel);
        
        return centerPanel;
    }
    
    private JLabel createStatusBar() {
        JLabel statusBar = new JLabel("  SPACE: Pause | R: Restart | ↑: Rotate | ↓: Soft Drop | SPACE: Hard Drop");
        statusBar.setForeground(Color.WHITE);
        statusBar.setBackground(new Color(0x34495E));
        statusBar.setOpaque(true);
        statusBar.setFont(new Font("Monospaced", Font.PLAIN, 11));
        return statusBar;
    }
    
    @Override
    public void onStateChanged() {
        gamePanel.repaint();
        scorePanel.updateDisplay();
        nextPiecePanel.repaint();
    }
    
    @Override
    public void onScoreChanged(int score, int lines, int level) {
        scorePanel.updateDisplay();
    }
    
    @Override
    public void onGameOver() {
        if (!scoreSaved) {
            saveScore();
        }
        
        int choice = JOptionPane.showConfirmDialog(this, 
            "Game Over!\nĐiểm của bạn: " + model.getScore() + 
            "\n\nBạn có muốn chơi lại không?", 
            "Kết thúc game", 
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            exitToMenu();
        }
    }
    
    private void saveScore() {
        scoreSaved = true;
        int finalScore = model.getScore();
        String playerName = model.getCurrentUser().getUsername();
        
        HighScoreManager hsm = new HighScoreManager();
        int rank = hsm.addScore(playerName, finalScore);
        
        StringBuilder message = new StringBuilder();
        message.append("🎮 GAME OVER!\n\n");
        message.append("📊 Điểm: ").append(finalScore).append("\n");
        message.append("📈 Dòng: ").append(model.getLinesCleared()).append("\n");
        message.append("⭐ Cấp độ: ").append(model.getLevel()).append("\n\n");
        
        if (rank > 0) {
            message.append("🎉 CHÚC MỪNG! Bạn đã lọt vào TOP ").append(rank);
        } else {
            message.append("💪 Cố gắng hơn lần sau!");
        }
        
        JOptionPane.showMessageDialog(this, message.toString(), "Kết quả", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void restartGame() {
        scoreSaved = false;
        controller.restartGame();
    }
    
    private void exitToMenu() {
        dispose();
        new MainMenuView(SessionManager.getInstance().getCurrentUser());
    }
}