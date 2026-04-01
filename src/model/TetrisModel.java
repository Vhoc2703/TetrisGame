package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Lớp Model chính của game Tetris.
 * Quản lý toàn bộ logic game và trạng thái.
 * Tuân thủ nguyên tắc đóng gói và Observer Pattern.
 */
public class TetrisModel {
    
    // Constants
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    private static final int[] SCORES = {0, 100, 300, 500, 800};
    
    // Game state
    private int[][] board;
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private int score;
    private int linesCleared;
    private int level;
    private boolean gameOver;
    private boolean paused;
    
    // References
    private User currentUser;
    private List<GameStateListener> listeners;
    
    /**
     * Khởi tạo model với người dùng hiện tại.
     */
    public TetrisModel(User user) {
        this.currentUser = user;
        this.board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        this.listeners = new ArrayList<>();
        initGame();
    }
    
    /**
     * Khởi tạo game mới.
     */
    public void initGame() {
        clearBoard();
        resetScores();
        createNewPieces();
        checkGameOver();
        notifyStateChanged();
    }
    
    /**
     * Xóa toàn bộ bảng game.
     */
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            Arrays.fill(board[i], 0);
        }
    }
    
    /**
     * Reset điểm số về 0.
     */
    private void resetScores() {
        score = 0;
        linesCleared = 0;
        level = 1;
        gameOver = false;
        paused = false;
    }
    
    /**
     * Tạo khối mới.
     */
    private void createNewPieces() {
        currentPiece = Tetromino.createRandom();
        nextPiece = Tetromino.createRandom();
    }
    
    /**
     * Kiểm tra game over ngay từ đầu.
     */
    private void checkGameOver() {
        if (collision()) {
            gameOver = true;
        }
    }
    
    /**
     * Di chuyển khối sang trái.
     */
    public boolean moveLeft() {
        if (isActionBlocked()) return false;
        
        currentPiece.moveLeft();
        if (collision()) {
            currentPiece.moveRight();
            return false;
        }
        notifyStateChanged();
        return true;
    }
    
    /**
     * Di chuyển khối sang phải.
     */
    public boolean moveRight() {
        if (isActionBlocked()) return false;
        
        currentPiece.moveRight();
        if (collision()) {
            currentPiece.moveLeft();
            return false;
        }
        notifyStateChanged();
        return true;
    }
    
    /**
     * Xoay khối.
     */
    public boolean rotate() {
        if (isActionBlocked()) return false;
        
        currentPiece.rotate();
        if (collision()) {
            currentPiece.rotateBack();
            return false;
        }
        notifyStateChanged();
        return true;
    }
    
    /**
     * Di chuyển khối xuống một bước.
     */
    public boolean moveDown() {
        if (isActionBlocked()) return false;
        
        currentPiece.moveDown();
        if (collision()) {
            currentPiece.moveUp();
            mergePiece();
            clearLines();
            spawnNewPiece();
            notifyStateChanged();
            return false;
        }
        notifyStateChanged();
        return true;
    }
    
    /**
     * Thả khối rơi thẳng xuống đáy.
     */
    public void hardDrop() {
        if (isActionBlocked()) return;
        
        while (!collision()) {
            currentPiece.moveDown();
        }
        currentPiece.moveUp();
        mergePiece();
        clearLines();
        spawnNewPiece();
        notifyStateChanged();
    }
    
    /**
     * Kiểm tra xem có bị chặn hành động không.
     */
    private boolean isActionBlocked() {
        return gameOver || paused;
    }
    
    /**
     * Kiểm tra va chạm giữa khối hiện tại và bảng.
     */
    private boolean collision() {
        int[][] shape = currentPiece.getShape();
        int pieceX = currentPiece.getX();
        int pieceY = currentPiece.getY();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col] != 0) {
                    int boardRow = pieceY + row;
                    int boardCol = pieceX + col;
                    
                    if (isOutOfBounds(boardRow, boardCol) || isOccupied(boardRow, boardCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Kiểm tra vị trí có nằm ngoài biên không.
     */
    private boolean isOutOfBounds(int row, int col) {
        return row >= BOARD_HEIGHT || col < 0 || col >= BOARD_WIDTH || row < 0;
    }
    
    /**
     * Kiểm tra ô đã có khối chưa.
     */
    private boolean isOccupied(int row, int col) {
        return row >= 0 && board[row][col] != 0;
    }
    
    /**
     * Gắn khối hiện tại vào bảng.
     */
    private void mergePiece() {
        int[][] shape = currentPiece.getShape();
        int pieceX = currentPiece.getX();
        int pieceY = currentPiece.getY();
        int color = currentPiece.getColor();
        
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col] != 0) {
                    int boardRow = pieceY + row;
                    int boardCol = pieceX + col;
                    if (isValidPosition(boardRow, boardCol)) {
                        board[boardRow][boardCol] = color;
                    }
                }
            }
        }
    }
    
    /**
     * Kiểm tra vị trí hợp lệ để gắn khối.
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < BOARD_HEIGHT && col >= 0 && col < BOARD_WIDTH;
    }
    
    /**
     * Xóa các dòng đã đầy và tính điểm.
     */
    private void clearLines() {
        int lines = 0;
        
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            if (isLineFull(row)) {
                lines++;
                shiftLinesDown(row);
                row++;
            }
        }
        
        if (lines > 0) {
            updateScore(lines);
            updateLevel();
            notifyScoreChanged();
        }
    }
    
    /**
     * Kiểm tra dòng đã đầy chưa.
     */
    private boolean isLineFull(int row) {
        for (int col = 0; col < BOARD_WIDTH; col++) {
            if (board[row][col] == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Dồn các dòng phía trên xuống.
     */
    private void shiftLinesDown(int startRow) {
        for (int row = startRow; row > 0; row--) {
            System.arraycopy(board[row - 1], 0, board[row], 0, BOARD_WIDTH);
        }
        Arrays.fill(board[0], 0);
    }
    
    /**
     * Cập nhật điểm số.
     */
    private void updateScore(int lines) {
        int points = SCORES[Math.min(lines, 4)] * level;
        score += points;
        linesCleared += lines;
    }
    
    /**
     * Cập nhật cấp độ.
     */
    private void updateLevel() {
        int newLevel = (linesCleared / 10) + 1;
        if (newLevel > level) {
            level = newLevel;
        }
    }
    
    /**
     * Tạo khối mới.
     */
    private void spawnNewPiece() {
        currentPiece = nextPiece;
        nextPiece = Tetromino.createRandom();
        
        if (collision()) {
            gameOver = true;
            notifyGameOver();
        }
        notifyStateChanged();
    }
    
    /**
     * Tạm dừng / tiếp tục game.
     */
    public void togglePause() {
        if (!gameOver) {
            paused = !paused;
            notifyStateChanged();
        }
    }
    
    // Getters
    public int[][] getBoard() { return board; }
    public Tetromino getCurrentPiece() { return currentPiece; }
    public Tetromino getNextPiece() { return nextPiece; }
    public int getScore() { return score; }
    public int getLinesCleared() { return linesCleared; }
    public int getLevel() { return level; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public User getCurrentUser() { return currentUser; }
    
    // Observer pattern methods
    public void addListener(GameStateListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(GameStateListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyStateChanged() {
        for (GameStateListener listener : listeners) {
            listener.onStateChanged();
        }
    }
    
    private void notifyScoreChanged() {
        for (GameStateListener listener : listeners) {
            listener.onScoreChanged(score, linesCleared, level);
        }
    }
    
    private void notifyGameOver() {
        for (GameStateListener listener : listeners) {
            listener.onGameOver();
        }
    }
}