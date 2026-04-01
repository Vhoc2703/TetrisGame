package controller;

import model.TetrisModel;

/**
 * Controller điều khiển logic game.
 * Xử lý sự kiện từ view và cập nhật model.
 */
public class GameController {
    
    private final TetrisModel model;
    private GameTimer gameTimer;
    
    public GameController(TetrisModel model) {
        this.model = model;
        this.gameTimer = new GameTimer(this);
    }
    
    public void startGame() {
        gameTimer.start();
    }
    
    public void stopGame() {
        gameTimer.stop();
    }
    
    public void restartGame() {
        gameTimer.stop();
        model.initGame();
        gameTimer.start();
    }
    
    public void pauseGame() {
        if (model.isGameOver()) return;
        
        model.togglePause();
        if (model.isPaused()) {
            gameTimer.stop();
        } else {
            gameTimer.start();
        }
    }
    
    public void step() {
        if (model.isGameOver() || model.isPaused()) return;
        model.moveDown();
    }
    
    public int getFallDelay() {
        int level = model.getLevel();
        int delay = 500 - (level - 1) * 30;
        return Math.max(80, delay);
    }
    
    // Movement delegates
    public void moveLeft()   { model.moveLeft(); }
    public void moveRight()  { model.moveRight(); }
    public void moveDown()   { model.moveDown(); }
    public void rotate()     { model.rotate(); }
    public void hardDrop()   { model.hardDrop(); }
}