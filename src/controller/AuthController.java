package controller;

import model.User;
import model.UserManager;
import utils.GameException;

/**
 * Controller xử lý đăng nhập, đăng ký
 */
public class AuthController {
    
    private final UserManager userManager;
    
    public AuthController() {
        this.userManager = new UserManager();
    }
    
    public User login(String username, String password) throws GameException {
        return userManager.login(username, password);
    }
    
    public boolean register(User user) throws GameException {
        return userManager.register(user);
    }
}