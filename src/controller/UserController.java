package controller;

import model.User;
import model.UserManager;
import utils.GameException;

import java.util.List;

/**
 * Controller xử lý quản lý người dùng
 */
public class UserController {
    
    private final UserManager userManager;
    
    public UserController() {
        this.userManager = new UserManager();
    }
    
    public List<User> getAllUsers() {
        return userManager.getAllUsers();
    }
    
    public boolean updateUser(User user) throws GameException {
        return userManager.updateUser(user);
    }
    
    public boolean deleteUser(int userId) throws GameException {
        return userManager.deleteUser(userId);
    }
    
    public boolean registerUser(User user) throws GameException {
        return userManager.register(user);
    }
    
    public boolean updateHighScore(int userId, int score) throws GameException {
        return userManager.updateHighScore(userId, score);
    }
}