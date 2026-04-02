package model;

import java.util.Random;

/**
 * Lớp đại diện cho một khối Tetris.
 * Mỗi khối có hình dạng, màu sắc và vị trí riêng.
 */
public class Tetromino {
    
    // Định nghĩa 7 loại khối cơ bản
    private static final int[][][] SHAPES = {
        // I
        {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        },
        // O
        {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // T
        {
            {0, 0, 0, 0},
            {0, 1, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // L
        {
            {0, 0, 0, 0},
            {0, 0, 1, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // J
        {
            {0, 0, 0, 0},
            {1, 0, 0, 0},
            {1, 1, 1, 0},
            {0, 0, 0, 0}
        },
        // S
        {
            {0, 0, 0, 0},
            {0, 1, 1, 0},
            {1, 1, 0, 0},
            {0, 0, 0, 0}
        },
        // Z
        {
            {0, 0, 0, 0},
            {1, 1, 0, 0},
            {0, 1, 1, 0},
            {0, 0, 0, 0}
        }
    };
    
    // Màu sắc tương ứng cho từng loại khối
    private static final int[] COLORS = {
        0x00FFFF, // I - Cyan
        0xFFFF00, // O - Yellow
        0xAA00FF, // T - Purple
        0xFFA500, // L - Orange
        0x0000FF, // J - Blue
        0x00FF00, // S - Green
        0xFF0000  // Z - Red
    };
    
    private int[][] shape;
    private int x;
    private int y;
    private int color;
    private int type;
    
    /**
     * Tạo khối ngẫu nhiên.
     */
    public static Tetromino createRandom() {
        Random random = new Random();
        int type = random.nextInt(SHAPES.length);
        return new Tetromino(type);
    }
    
    /**
     * Tạo khối theo loại chỉ định.
     */
    public static Tetromino createPiece(int type) {
        return new Tetromino(type);
    }
    
    private Tetromino(int type) {
        this.type = type;
        this.shape = copyShape(SHAPES[type]);
        this.color = COLORS[type];
        this.x = TetrisModel.BOARD_WIDTH / 2 - shape[0].length / 2;
        this.y = 0;
    }
    
    /**
     * Sao chép mảng shape để tránh tham chiếu.
     */
    private int[][] copyShape(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
    
    /**
     * Xoay khối 90 độ theo chiều kim đồng hồ.
     */
    public void rotate() {
        int size = shape.length;
        int[][] rotated = new int[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotated[j][size - 1 - i] = shape[i][j];
            }
        }
        shape = rotated;
    }
    
    /**
     * Xoay ngược lại để phục hồi trạng thái cũ.
     */
    public void rotateBack() {
        int size = shape.length;
        int[][] rotated = new int[size][size];
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotated[size - 1 - j][i] = shape[i][j];
            }
        }
        shape = rotated;
    }
    
    // Movement methods
    public void moveLeft()  { x--; }
    public void moveRight() { x++; }
    public void moveDown()  { y++; }
    public void moveUp()    { y--; }
    
    // Getters
    public int[][] getShape() { return shape; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getColor() { return color; }
    public int getType() { return type; }
}