package utils;

/**
 * Custom Exception cho game.
 * Đáp ứng yêu cầu tự định nghĩa Exception.
 */
public class GameException extends Exception {
    
    public GameException() {
        super();
    }
    
    public GameException(String message) {
        super(message);
    }
    
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public GameException(Throwable cause) {
        super(cause);
    }
}