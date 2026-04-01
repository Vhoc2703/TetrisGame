package controller;

import javax.swing.Timer;

/**
 * Timer quản lý vòng lặp game.
 * Sử dụng javax.swing.Timer để đồng bộ với EDT.
 */
public class GameTimer {
    
    private Timer timer;
    private final GameController controller;
    
    public GameTimer(GameController controller) {
        this.controller = controller;
        this.timer = new Timer(controller.getFallDelay(), e -> controller.step());
    }
    
    public void start() {
        updateDelay();
        timer.start();
    }
    
    public void stop() {
        timer.stop();
    }
    
    public void updateDelay() {
        timer.setDelay(controller.getFallDelay());
    }
}