package view;

import controller.UserController;
import model.User;
import utils.GameException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

/**
 * Giao diện quản lý người dùng - CRUD.
 */
public class UserManagementView extends JFrame {
    
    private final UserController userController;
    private List<User> userList;
    private final UserTableModel tableModel;
    private JTable userTable;
    
    public UserManagementView() {
        this.userController = new UserController();
        this.tableModel = new UserTableModel();
        initialize();
        loadData();
    }
    
    private void initialize() {
        setTitle("Quản lý người dùng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createTitlePanel() {
        JLabel titleLabel = new JLabel("QUẢN LÝ NGƯỜI DÙNG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0x2C3E50));
        
        JPanel panel = new JPanel();
        panel.add(titleLabel);
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setRowHeight(25);
        userTable.getTableHeader().setReorderingAllowed(false);
        
        return new JScrollPane(userTable);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        panel.add(createButton("Thêm mới", new Color(0x4ECDC4), e -> addUser()));
        panel.add(createButton("Sửa", new Color(0xE67E22), e -> editUser()));
        panel.add(createButton("Xóa", new Color(0xE74C3C), e -> deleteUser()));
        panel.add(createButton("Làm mới", new Color(0x3498DB), e -> loadData()));
        panel.add(createButton("Quay lại", new Color(0x95A5A6), e -> goBack()));
        
        return panel;
    }
    
    private JButton createButton(String text, Color color, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 35));
        button.addActionListener(listener);
        return button;
    }
    
    private void loadData() {
        try {
            userList = userController.getAllUsers();
            tableModel.setUsers(userList);
        } catch (GameException e) {
            showError("Lỗi tải dữ liệu: " + e.getMessage());
        }
    }
    
    private void addUser() {
        UserDialog dialog = new UserDialog(this, null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
    
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Vui lòng chọn người dùng cần sửa!");
            return;
        }
        
        UserDialog dialog = new UserDialog(this, userList.get(selectedRow));
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }
    
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Vui lòng chọn người dùng cần xóa!");
            return;
        }
        
        User user = userList.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa người dùng '" + user.getUsername() + "'?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (userController.deleteUser(user.getId())) {
                    showMessage("Xóa thành công!");
                    loadData();
                }
            } catch (GameException e) {
                showError("Lỗi xóa: " + e.getMessage());
            }
        }
    }
    
    private void goBack() {
        dispose();
        new MainMenuView(utils.SessionManager.getInstance().getCurrentUser());
    }
    
    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Table Model cho JTable.
     */
    private static class UserTableModel extends AbstractTableModel {
        private static final String[] COLUMNS = {"ID", "Tên đăng nhập", "Họ tên", "Email", "Vai trò", "Điểm cao"};
        private List<User> users;
        
        void setUsers(List<User> users) {
            this.users = users;
            fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return users == null ? 0 : users.size();
        }
        
        @Override
        public int getColumnCount() {
            return COLUMNS.length;
        }
        
        @Override
        public String getColumnName(int column) {
            return COLUMNS[column];
        }
        
        @Override
        public Object getValueAt(int row, int col) {
            User user = users.get(row);
            switch (col) {
                case 0: return user.getId();
                case 1: return user.getUsername();
                case 2: return user.getFullName();
                case 3: return user.getEmail();
                case 4: return user.getRole();
                case 5: return user.getHighScore();
                default: return null;
            }
        }
    }
    
    /**
     * Dialog thêm/sửa người dùng.
     */
    private static class UserDialog extends JDialog {
        private final UserController userController;
        private final User user;
        private boolean saved;
        
        private JTextField usernameField;
        private JPasswordField passwordField;
        private JTextField fullNameField;
        private JTextField emailField;
        private JComboBox<String> roleCombo;
        
        UserDialog(JFrame parent, User user) {
            super(parent, user == null ? "Thêm người dùng" : "Sửa người dùng", true);
            this.userController = new UserController();
            this.user = user;
            this.saved = false;
            initialize();
        }
        
        private void initialize() {
            setSize(400, 350);
            setLocationRelativeTo(getParent());
            setLayout(new BorderLayout());
            
            add(createFormPanel(), BorderLayout.CENTER);
            add(createButtonPanel(), BorderLayout.SOUTH);
        }
        
        private JPanel createFormPanel() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            
            int row = 0;
            
            usernameField = addField(panel, gbc, row++, "Tên đăng nhập:");
            if (user != null) {
                usernameField.setText(user.getUsername());
                usernameField.setEnabled(false);
            }
            
            passwordField = addPasswordField(panel, gbc, row++, "Mật khẩu:");
            if (user != null) {
                passwordField.setEnabled(false);
            }
            
            fullNameField = addField(panel, gbc, row++, "Họ và tên:");
            if (user != null) {
                fullNameField.setText(user.getFullName());
            }
            
            emailField = addField(panel, gbc, row++, "Email:");
            if (user != null) {
                emailField.setText(user.getEmail());
            }
            
            gbc.gridy = row;
            gbc.gridx = 0;
            panel.add(new JLabel("Vai trò:"), gbc);
            gbc.gridx = 1;
            roleCombo = new JComboBox<>(new String[]{User.ROLE_USER, User.ROLE_ADMIN});
            if (user != null) {
                roleCombo.setSelectedItem(user.getRole());
            }
            panel.add(roleCombo, gbc);
            
            return panel;
        }
        
        private JTextField addField(JPanel panel, GridBagConstraints gbc, int row, String label) {
            gbc.gridy = row;
            gbc.gridx = 0;
            panel.add(new JLabel(label), gbc);
            gbc.gridx = 1;
            JTextField field = new JTextField(15);
            panel.add(field, gbc);
            return field;
        }
        
        private JPasswordField addPasswordField(JPanel panel, GridBagConstraints gbc, int row, String label) {
            gbc.gridy = row;
            gbc.gridx = 0;
            panel.add(new JLabel(label), gbc);
            gbc.gridx = 1;
            JPasswordField field = new JPasswordField(15);
            panel.add(field, gbc);
            return field;
        }
        
        private JPanel createButtonPanel() {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            
            JButton saveButton = new JButton("Lưu");
            JButton cancelButton = new JButton("Hủy");
            
            saveButton.addActionListener(e -> save());
            cancelButton.addActionListener(e -> dispose());
            
            panel.add(saveButton);
            panel.add(cancelButton);
            return panel;
        }
        
        private void save() {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                if (user == null) {
                    if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng nhập mật khẩu!", 
                            "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    User newUser = new User(username, password, fullName, email);
                    newUser.setRole(role);
                    userController.registerUser(newUser);
                } else {
                    user.setFullName(fullName);
                    user.setEmail(email);
                    user.setRole(role);
                    userController.updateUser(user);
                }
                
                saved = true;
                JOptionPane.showMessageDialog(this, "Lưu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (GameException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        boolean isSaved() {
            return saved;
        }
    }
}