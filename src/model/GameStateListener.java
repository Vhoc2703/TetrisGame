package model;

/**
 * Interface listener cho Observer Pattern.
 * Thông báo khi model có thay đổi để view cập nhật.
 */
public interface GameStateListener {
    
    /**
     * Được gọi khi trạng thái game thay đổi.
     */
    void onStateChanged();
    
    /**
     * Được gọi khi điểm số thay đổi.
     */
    void onScoreChanged(int score, int lines, int level);
    
    /**
     * Được gọi khi game kết thúc.
     */
    void onGameOver();
}