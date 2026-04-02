package view;

import controller.GameController;
import model.TetrisModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Menu bar cho game.
 */
public class GameMenuBar extends JMenuBar {
    
    private final GameController controller;
    private final TetrisModel model;
    
    public GameMenuBar(GameController controller, TetrisModel model) {
        this.controller = controller;
        this.model = model;
        initialize();
    }
    
    private void initialize() {
        add(createGameMenu());
        add(createHelpMenu());
    }
    
    private JMenu createGameMenu() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        
        JMenuItem newGameItem = new JMenuItem("New Game", KeyEvent.VK_N);
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        newGameItem.addActionListener(e -> controller.restartGame());
        
        JMenuItem pauseItem = new JMenuItem("Pause", KeyEvent.VK_P);
        pauseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
        pauseItem.addActionListener(e -> controller.pauseGame());
        
        JMenuItem exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGameItem);
        gameMenu.add(pauseItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        
        return gameMenu;
    }
    
    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Trợ giúp");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        JMenuItem controlsItem = new JMenuItem("Hướng dẫn", KeyEvent.VK_C);
        controlsItem.addActionListener(e -> showHelp());
        
        JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(controlsItem);
        helpMenu.addSeparator();
        helpMenu.add(aboutItem);
        
        return helpMenu;
    }
    
    private void showHelp() {
        String helpText = """
            ========== HƯỚNG DẪN CHƠI ==========
            
            MỤC TIÊU:
            Sắp xếp các khối rơi để tạo thành dòng ngang.
            
            ĐIỀU KHIỂN:
            • ← → : Di chuyển trái/phải
            • ↑   : Xoay khối
            • ↓   : Rơi nhanh
            • SPACE : Rơi thẳng đáy / Pause
            • R   : Game mới
            
            TÍNH ĐIỂM:
            1 dòng: 100 × level
            2 dòng: 300 × level
            3 dòng: 500 × level
            4 dòng: 800 × level (TETRIS!)
            """;
        
        JTextArea textArea = new JTextArea(helpText);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(0x2C3E50));
        textArea.setForeground(Color.WHITE);
        
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), 
            "Hướng dẫn", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAbout() {
        String aboutText = """
            TETRIS GAME
            
            Phiên bản: 1.0
            Ngôn ngữ: Java (JDK 17+)
            Kiến trúc: MVC
            
            Bài tập lớn môn Lập trình Java
            """;
        
        JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}