package view;

import model.TetrisModel;
import model.Tetromino;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel hiển thị khối tiếp theo.
 */
public class NextPiecePanel extends JPanel {
    
    private static final int BLOCK_SIZE = 25;
    
    private final TetrisModel model;
    
    public NextPiecePanel(TetrisModel model) {
        this.model = model;
        setPreferredSize(new Dimension(140, 140));
        setBackground(new Color(0x2C3E50));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0x4ECDC4), 2),
            "KHỐI TIẾP THEO",
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 12),
            new Color(0x4ECDC4)
        ));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        Tetromino nextPiece = model.getNextPiece();
        if (nextPiece == null) return;
        
        int[][] shape = nextPiece.getShape();
        int color = nextPiece.getColor();
        
        int pieceWidth = shape[0].length * BLOCK_SIZE;
        int pieceHeight = shape.length * BLOCK_SIZE;
        int startX = (getWidth() - pieceWidth) / 2;
        int startY = (getHeight() - pieceHeight) / 2;
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col] != 0) {
                    drawBlock(g2d, startX + col * BLOCK_SIZE, startY + row * BLOCK_SIZE, color);
                }
            }
        }
    }
    
    private void drawBlock(Graphics2D g2d, int x, int y, int colorValue) {
        g2d.setColor(new Color(colorValue));
        g2d.fillRect(x, y, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
    }
}