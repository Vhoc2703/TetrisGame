package view;

import model.TetrisModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel hiển thị thông tin game: điểm, dòng, cấp độ.
 */
public class ScorePanel extends JPanel {
    
    private final TetrisModel model;
    private final JLabel scoreLabel;
    private final JLabel linesLabel;
    private final JLabel levelLabel;
    
    public ScorePanel(TetrisModel model) {
        this.model = model;
        this.scoreLabel = new JLabel("0");
        this.linesLabel = new JLabel("0");
        this.levelLabel = new JLabel("1");
        
        initialize();
    }
    
    private void initialize() {
        setLayout(new GridLayout(3, 2, 10, 15));
        setBackground(new Color(0x2C3E50));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x4ECDC4), 2),
            "THÔNG TIN",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            new Color(0x4ECDC4)
        ));
        
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font valueFont = new Font("Monospaced", Font.BOLD, 20);
        
        add(createLabel("ĐIỂM:", labelFont));
        add(createValueLabel(scoreLabel, valueFont));
        
        add(createLabel("DÒNG:", labelFont));
        add(createValueLabel(linesLabel, valueFont));
        
        add(createLabel("CẤP ĐỘ:", labelFont));
        add(createValueLabel(levelLabel, valueFont));
        
        setPreferredSize(new Dimension(180, 120));
    }
    
    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(Color.WHITE);
        return label;
    }
    
    private JLabel createValueLabel(JLabel label, Font font) {
        label.setFont(font);
        label.setForeground(new Color(0x4ECDC4));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }
    
    public void updateDisplay() {
        scoreLabel.setText(String.valueOf(model.getScore()));
        linesLabel.setText(String.valueOf(model.getLinesCleared()));
        levelLabel.setText(String.valueOf(model.getLevel()));
    }
}