package model;

import utils.GameException;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Quản lý người dùng - Lưu bằng File Binary (users.dat)
 * Đáp ứng yêu cầu File I/O của đề bài
 */
public class UserManager {
    
    private static final String USER_FILE = "users.dat";
    private List<User> users;
    private AtomicInteger nextId;
    
    public UserManager() {
        this.users = new ArrayList<>();
        this.nextId = new AtomicInteger(1);
        loadUsersFromFile();
    }
    
    /**
     * Đăng nhập
     */
    public User login(String username, String password) throws GameException {
        validateCredentials(username, password);
        
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                user.setLastLogin(new Date());
                saveUsersToFile();
                return user;
            }
        }
        throw new GameException("Sai tên đăng nhập hoặc mật khẩu!");
    }
    
    /**
     * Đăng ký tài khoản mới
     */
    public boolean register(User user) throws GameException {
        validateUserData(user);
        checkDuplicateUser(user);
        
        user.setId(nextId.getAndIncrement());
        user.setCreatedAt(new Date());
        users.add(user);
        saveUsersToFile();
        return true;
    }
    
    /**
     * Lấy tất cả người dùng
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    /**
     * Cập nhật thông tin người dùng
     */
    public boolean updateUser(User updatedUser) throws GameException {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == updatedUser.getId()) {
                users.set(i, updatedUser);
                saveUsersToFile();
                return true;
            }
        }
        throw new GameException("Không tìm thấy người dùng!");
    }
    
    /**
     * Xóa người dùng
     */
    public boolean deleteUser(int userId) throws GameException {
        boolean removed = users.removeIf(u -> u.getId() == userId);
        if (removed) {
            saveUsersToFile();
            return true;
        }
        throw new GameException("Không tìm thấy người dùng!");
    }
    
    /**
     * Cập nhật điểm cao
     */
    public boolean updateHighScore(int userId, int score) throws GameException {
        for (User user : users) {
            if (user.getId() == userId) {
                if (score > user.getHighScore()) {
                    user.setHighScore(score);
                    saveUsersToFile();
                }
                return true;
            }
        }
        return false;
    }
    
    // ========== PRIVATE METHODS ==========
    
    private void validateCredentials(String username, String password) throws GameException {
        if (username == null || username.trim().isEmpty()) {
            throw new GameException("Tên đăng nhập không được để trống!");
        }
        if (password == null || password.isEmpty()) {
            throw new GameException("Mật khẩu không được để trống!");
        }
    }
    
    private void validateUserData(User user) throws GameException {
        if (user == null) {
            throw new GameException("Thông tin người dùng không hợp lệ!");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new GameException("Tên đăng nhập không được để trống!");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new GameException("Mật khẩu không được để trống!");
        }
        if (user.getPassword().length() < 6) {
            throw new GameException("Mật khẩu phải có ít nhất 6 ký tự!");
        }
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new GameException("Họ tên không được để trống!");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new GameException("Email không hợp lệ!");
        }
    }
    
    private void checkDuplicateUser(User user) throws GameException {
        for (User existing : users) {
            if (existing.getUsername().equals(user.getUsername())) {
                throw new GameException("Tên đăng nhập đã tồn tại!");
            }
            if (existing.getEmail().equals(user.getEmail())) {
                throw new GameException("Email đã được sử dụng!");
            }
        }
    }
    
    /**
     * Đọc danh sách users từ file nhị phân
     */
    @SuppressWarnings("unchecked")
    private void loadUsersFromFile() {
        File file = new File(USER_FILE);
        if (!file.exists()) {
            createDefaultUsers();
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = (List<User>) ois.readObject();
            // Tìm max id để tiếp tục đánh số
            int maxId = users.stream().mapToInt(User::getId).max().orElse(0);
            nextId.set(maxId + 1);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Lỗi đọc file users.dat: " + e.getMessage());
            createDefaultUsers();
        }
    }
    
    /**
     * Lưu danh sách users xuống file nhị phân
     */
    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.err.println("Lỗi lưu file users.dat: " + e.getMessage());
        }
    }
    
    /**
     * Tạo dữ liệu mặc định cho lần chạy đầu tiên
     */
    private void createDefaultUsers() {
        users.clear();
        
        // Admin mặc định
        User admin = new User("admin", "admin123", "Quản trị viên", "admin@tetris.com");
        admin.setId(nextId.getAndIncrement());
        admin.setRole(User.ROLE_ADMIN);
        admin.setHighScore(5000);
        admin.setCreatedAt(new Date());
        users.add(admin);
        
        // User mặc định
        User player1 = new User("player1", "123456", "Nguyễn Văn An", "an@email.com");
        player1.setId(nextId.getAndIncrement());
        player1.setHighScore(2500);
        player1.setCreatedAt(new Date());
        users.add(player1);
        
        User player2 = new User("player2", "123456", "Trần Thị Bình", "binh@email.com");
        player2.setId(nextId.getAndIncrement());
        player2.setHighScore(1800);
        player2.setCreatedAt(new Date());
        users.add(player2);
        
        User player3 = new User("player3", "123456", "Lê Văn Cường", "cuong@email.com");
        player3.setId(nextId.getAndIncrement());
        player3.setHighScore(1200);
        player3.setCreatedAt(new Date());
        users.add(player3);
        
        saveUsersToFile();
    }
}