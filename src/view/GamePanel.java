package view;

import controller.GameController;
import controller.KeyHandler;
import model.TetrisModel;
import model.Tetromino;

import javax.swing.*;
import java.awt.*;

/**
 * Panel vẽ game - xử lý đồ họa.
 */
public class GamePanel extends JPanel {
    
    private static final int BLOCK_SIZE = 30;
    
    private final TetrisModel model;
    private final KeyHandler keyHandler;
    
    public GamePanel(TetrisModel model, GameController controller) {
        this.model = model;
        this.keyHandler = new KeyHandler(controller);
        
        setPreferredSize(new Dimension(
            TetrisModel.BOARD_WIDTH * BLOCK_SIZE,
            TetrisModel.BOARD_HEIGHT * BLOCK_SIZE
        ));
        setBackground(new Color(0x1A1F2E));
        setBorder(BorderFactory.createLineBorder(new Color(0x4ECDC4), 3));
        
        addKeyListener(keyHandler);
        setFocusable(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        drawBoard(g2d);
        drawCurrentPiece(g2d);
        drawGrid(g2d);
        drawStatusOverlay(g2d);
    }
    
    private void drawBoard(Graphics2D g2d) {
        int[][] board = model.getBoard();
        for (int row = 0; row < TetrisModel.BOARD_HEIGHT; row++) {
            for (int col = 0; col < TetrisModel.BOARD_WIDTH; col++) {
                if (board[row][col] != 0) {
                    drawBlock(g2d, col * BLOCK_SIZE, row * BLOCK_SIZE, board[row][col]);
                }
            }
        }
    }
    
    private void drawCurrentPiece(Graphics2D g2d) {
        Tetromino piece = model.getCurrentPiece();
        if (piece == null) return;
        
        int[][] shape = piece.getShape();
        int x = piece.getX();
        int y = piece.getY();
        int color = piece.getColor();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col] != 0) {
                    drawBlock(g2d, (x + col) * BLOCK_SIZE, (y + row) * BLOCK_SIZE, color);
                }
            }
        }
    }
    
    private void drawBlock(Graphics2D g2d, int x, int y, int colorValue) {
        Color color = new Color(colorValue);
        Color lightColor = color.brighter();
        Color darkColor = color.darker();
        
        GradientPaint gradient = new GradientPaint(x, y, lightColor, 
            x + BLOCK_SIZE, y + BLOCK_SIZE, darkColor);
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
        
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
        
        g2d.setColor(new Color(255, 255, 255, 80));
        g2d.drawLine(x + 1, y + 1, x + BLOCK_SIZE - 2, y + 1);
    }
    
    private void drawGrid(Graphics2D g2d) {
        g2d.setColor(new Color(0x2C3E50));
        for (int i = 0; i <= TetrisModel.BOARD_WIDTH; i++) {
            g2d.drawLine(i * BLOCK_SIZE, 0, i * BLOCK_SIZE, TetrisModel.BOARD_HEIGHT * BLOCK_SIZE);
        }
        for (int i = 0; i <= TetrisModel.BOARD_HEIGHT; i++) {
            g2d.drawLine(0, i * BLOCK_SIZE, TetrisModel.BOARD_WIDTH * BLOCK_SIZE, i * BLOCK_SIZE);
        }
    }
    
    private void drawStatusOverlay(Graphics2D g2d) {
        if (model.isPaused()) {
            drawOverlayText(g2d, "PAUSED", new Color(0x4ECDC4));
        } else if (model.isGameOver()) {
            drawOverlayText(g2d, "GAME OVER", new Color(0xE74C3C));
        }
    }
    
    private void drawOverlayText(Graphics2D g2d, String text, Color color) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        g2d.setColor(color);
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(text, x, y);
    }
}