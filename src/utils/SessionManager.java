package utils;

import model.User;

/**
 * Quản lý session người dùng hiện tại.
 * Sử dụng Singleton Pattern.
 */
public class SessionManager {
    
    private static SessionManager instance;
    private User currentUser;
    
    private SessionManager() {}
    
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public void clearSession() {
        this.currentUser = null;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean isAdmin() {
        return currentUser != null && User.ROLE_ADMIN.equals(currentUser.getRole());
    }
}