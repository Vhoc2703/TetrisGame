package view;

import model.HighScoreManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.List;

/**
 * Giao diện hiển thị bảng xếp hạng.
 */
public class HighScoreView extends JFrame {
    
    private final HighScoreManager highScoreManager;
    private final HighScoreTableModel tableModel;
    
    public HighScoreView() {
        this.highScoreManager = new HighScoreManager();
        this.tableModel = new HighScoreTableModel();
        initialize();
        loadData();
    }
    
    private void initialize() {
        setTitle("🏆 Bảng xếp hạng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        mainPanel.add(createTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0x1A1F2E));
        
        JLabel titleLabel = new JLabel("🏆 BẢNG XẾP HẠNG CAO NHẤT 🏆");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0xE67E22));
        
        panel.add(titleLabel);
        return panel;
    }
    
    private JScrollPane createTablePanel() {
        JTable table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setShowGrid(true);
        table.setGridColor(new Color(0x4ECDC4));
        table.setDefaultRenderer(Object.class, new HighScoreCellRenderer());
        
        return new JScrollPane(table);
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        
        JButton refreshButton = new JButton("🔄 Làm mới");
        refreshButton.addActionListener(e -> loadData());
        
        JButton closeButton = new JButton("✖ Đóng");
        closeButton.addActionListener(e -> dispose());
        
        panel.add(refreshButton);
        panel.add(closeButton);
        
        // Admin có thể xóa điểm
        if (utils.SessionManager.getInstance().isAdmin()) {
            JButton clearButton = new JButton("🗑 Xóa tất cả");
            clearButton.addActionListener(e -> clearAllScores());
            panel.add(clearButton);
        }
        
        return panel;
    }
    
    private void loadData() {
        List<HighScoreManager.HighScoreEntry> scores = highScoreManager.getHighScores();
        tableModel.setData(scores);
    }
    
    private void clearAllScores() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa TOÀN BỘ bảng xếp hạng?",
            "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            highScoreManager.clearAllScores();
            loadData();
            JOptionPane.showMessageDialog(this, "Đã xóa toàn bộ bảng xếp hạng!", 
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Table Model cho bảng xếp hạng.
     */
    private static class HighScoreTableModel extends AbstractTableModel {
        private static final String[] COLUMNS = {"🏅 Hạng", "👤 Người chơi", "🎯 Điểm", "📅 Ngày"};
        private List<HighScoreManager.HighScoreEntry> data;
        
        void setData(List<HighScoreManager.HighScoreEntry> data) {
            this.data = data;
            fireTableDataChanged();
        }
        
        @Override
        public int getRowCount() {
            return data == null ? 0 : data.size();
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
            HighScoreManager.HighScoreEntry entry = data.get(row);
            switch (col) {
                case 0:
                    switch (row) {
                        case 0: return "🥇 1";
                        case 1: return "🥈 2";
                        case 2: return "🥉 3";
                        default: return String.valueOf(row + 1);
                    }
                case 1: return entry.getPlayerName();
                case 2: return String.format("%,d", entry.getScore());
                case 3: return String.format("%td/%tm/%tY", entry.getDate(), entry.getDate(), entry.getDate());
                default: return null;
            }
        }
    }
    
    /**
     * Cell Renderer tô màu cho top 3.
     */
    private static class HighScoreCellRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                if (row == 0) {
                    c.setBackground(new Color(0xFFD700));
                    c.setForeground(new Color(0x8B4513));
                } else if (row == 1) {
                    c.setBackground(new Color(0xC0C0C0));
                    c.setForeground(Color.BLACK);
                } else if (row == 2) {
                    c.setBackground(new Color(0xCD7F32));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(0x2C3E50));
                    c.setForeground(Color.WHITE);
                }
                c.setFont(new Font("Arial", row < 3 ? Font.BOLD : Font.PLAIN, row < 3 ? 14 : 12));
            }
            
            setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }
}