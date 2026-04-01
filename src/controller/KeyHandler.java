package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Xử lý sự kiện bàn phím.
 */
public class KeyHandler extends KeyAdapter {
    
    private final GameController controller;
    
    public KeyHandler(GameController controller) {
        this.controller = controller;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                controller.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                controller.moveRight();
                break;
            case KeyEvent.VK_DOWN:
                controller.moveDown();
                break;
            case KeyEvent.VK_UP:
                controller.rotate();
                break;
            case KeyEvent.VK_SPACE:
                controller.hardDrop();
                break;
            case KeyEvent.VK_P:
                controller.pauseGame();
                break;
            case KeyEvent.VK_R:
                controller.restartGame();
                break;
        }
    }
}