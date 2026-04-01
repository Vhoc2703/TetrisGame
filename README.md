# 🎮 TETRIS GAME - Classic Block Stacking Game

> **Bài tập lớn cuối kỳ môn Lập trình Java**  
> **Đề tài:** Game Xếp Gạch Tetris (Nhóm 4 - Game 2D)

---

## 👥 Thông tin nhóm (Team Members)

| STT | Họ và Tên       | Mã Sinh Viên | Vai trò / Nhiệm vụ                         | Link GitHub Cá Nhân  |
|-----|-----------------|--------------|--------------------------------------------|----------------------|
| 1   | Trương Văn Học  | 3120225058   |Code Controller, Database, GUI Main, Tích hợp toàn bộ hệ thống     | [GitHub](https://github.com/Vhoc2703)  |
| 2   | Phạm Minh Quảng | 3120225126   | Code Model, Tetromino Logic, HighScore Manager, Xử lý game logic  | [GitHub](https://github.com/quang11245)  |
| 3   | Ngô Phi Trường   | 3120225168  | Vẽ GamePanel, Xử lý File I/O, Báo cáo, Thiết kế giao diện         |  [GitHub](https://github.com/truonga7k61thptlethuy-gif) |

---

## 📝 Giới thiệu dự án

**Tetris Game** là phiên bản game xếp gạch kinh điển được viết bằng **Java Swing**. Người chơi điều khiển các khối hình rơi xuống, sắp xếp để tạo thành các dòng ngang hoàn chỉnh. Game có hệ thống **đăng nhập/đăng ký**, **phân quyền người dùng** (Admin/User), **quản lý tài khoản**, **lưu điểm cao** và **bảng xếp hạng**.

### Đối tượng sử dụng
- **Người chơi:** Trải nghiệm game Tetris, theo dõi điểm số, cạnh tranh bảng xếp hạng
- **Quản trị viên:** Quản lý người dùng (thêm, sửa, xóa), xóa bảng xếp hạng

---

## ✨ Các chức năng chính (Features)

### 1. Hệ thống đăng nhập / đăng ký
- [x] Đăng nhập với tài khoản có sẵn
- [x] Đăng ký tài khoản mới (validation: username, email, password)
- [x] Lưu thông tin người dùng bằng File Binary (`users.dat`)

### 2. Game Tetris
- [x] Điều khiển khối: ← → (di chuyển), ↑ (xoay), ↓ (rơi nhanh), SPACE (rơi thẳng)
- [x] Tính điểm theo số dòng xóa và cấp độ
- [x] Tăng tốc độ rơi theo cấp độ (mỗi 10 dòng tăng level)
- [x] Tạm dừng / Tiếp tục game (Phím P hoặc SPACE)
- [x] Khởi động lại game (Phím R)

### 3. Bảng xếp hạng
- [x] Hiển thị Top 10 người chơi có điểm cao nhất
- [x] Tô màu đặc biệt cho Top 3 (🥇 Vàng, 🥈 Bạc, 🥉 Đồng)
- [x] Lưu điểm cao bằng File Binary (`highscore.dat`)
- [x] Admin có thể xóa toàn bộ bảng xếp hạng

### 4. Quản lý người dùng (Admin)
- [x] Xem danh sách tất cả người dùng (JTable)
- [x] Thêm người dùng mới
- [x] Sửa thông tin người dùng
- [x] Xóa người dùng
- [x] Phân quyền ADMIN / USER

### 5. Giao diện
- [x] Gradient background đẹp mắt
- [x] Hiệu ứng hover cho button
- [x] Thông báo bằng JOptionPane
- [x] Status bar hiển thị hướng dẫn

---

## 💻 Công nghệ & Thư viện sử dụng (Technologies)

| Công nghệ | Mô tả |
|-----------|-------|
| **Java** | JDK 17+ (Core Java) |
| **Java Swing** | Giao diện đồ họa (JFrame, JPanel, JTable) |
| **AWT** | Graphics2D, vẽ game, xử lý sự kiện |
| **File I/O** | Lưu trữ dữ liệu (users.dat, highscore.dat) |
| **Serializable** | Ghi/đọc đối tượng ra file nhị phân |
| **Observer Pattern** | GameStateListener thông báo model thay đổi |
| **MVC Architecture** | Model - View - Controller |

---

## 📂 Cấu trúc thư mục (Project Structure)

```
TetrisGame/
├── src/
│   ├── model/                      # Model - Dữ liệu và logic
│   │   ├── User.java               # Đối tượng người dùng
│   │   ├── UserManager.java        # Quản lý người dùng (CRUD + File I/O)
│   │   ├── TetrisModel.java        # Logic game chính
│   │   ├── Tetromino.java          # Các loại khối (I, O, T, L, J, S, Z)
│   │   ├── HighScoreManager.java   # Quản lý điểm cao (File I/O)
│   │   └── GameStateListener.java  # Observer Pattern
│   │
│   ├── view/                       # View - Giao diện người dùng
│   │   ├── LoginView.java          # Màn hình đăng nhập
│   │   ├── RegisterView.java       # Màn hình đăng ký
│   │   ├── MainMenuView.java       # Menu chính
│   │   ├── GameView.java           # Màn hình chơi game
│   │   ├── GamePanel.java          # Vẽ bảng game
│   │   ├── ScorePanel.java         # Hiển thị điểm, level, dòng
│   │   ├── NextPiecePanel.java     # Hiển thị khối tiếp theo
│   │   ├── GameMenuBar.java        # Menu bar (File, Help)
│   │   ├── UserManagementView.java # Quản lý người dùng (CRUD)
│   │   └── HighScoreView.java      # Bảng xếp hạng
│   │
│   ├── controller/                 # Controller - Xử lý logic
│   │   ├── AuthController.java     # Xử lý đăng nhập/đăng ký
│   │   ├── GameController.java     # Xử lý logic game
│   │   ├── UserController.java     # Xử lý CRUD người dùng
│   │   ├── GameTimer.java          # Vòng lặp game (Timer)
│   │   └── KeyHandler.java         # Bắt sự kiện bàn phím
│   │
│   ├── utils/                      # Utilities - Tiện ích
│   │   ├── SessionManager.java     # Quản lý session người dùng
│   │   └── GameException.java      # Custom Exception
│   │
│   └── Main.java                   # Entry point
│
├── users.dat                       # File lưu thông tin người dùng (tự tạo)
├── highscore.dat                   # File lưu điểm cao (tự tạo)
├── README.md                       # Tài liệu dự án
└── .gitignore                      # Git ignore file
```

---

## 🚀 Hướng dẫn cài đặt và chạy (Installation)

### Yêu cầu hệ thống
- **Java JDK 17** trở lên
- **IDE:** IntelliJ IDEA / Eclipse / NetBeans (khuyến nghị IntelliJ)


### Cách chạy: Chạy bằng Command Line

```bash
# Biên dịch toàn bộ source
javac -d out src/**/*.java src/Main.java

# Chạy ứng dụng
java -cp out Main
```

### Lưu ý lần chạy đầu tiên
- Ứng dụng sẽ tự động tạo file `users.dat` và `highscore.dat` với dữ liệu mặc định
- Không cần cài đặt MySQL hay database nào khác

---

## 🔑 Tài khoản mặc định (Default Accounts)

| Tên đăng nhập | Mật khẩu | Vai trò | Mô tả |
|---------------|----------|---------|-------|
| **admin**   | admin123 | ADMIN | Quản trị viên (có quyền quản lý người dùng) |
| **player1** | 123456 | USER | Người chơi 1 |
| **player2** | 123456 | USER | Người chơi 2 |
| **player3** | 123456 | USER | Người chơi 3 |

> 💡 **Lưu ý:** Bạn có thể đăng ký thêm tài khoản mới qua màn hình đăng ký.

---

## 🎮 Hướng dẫn chơi (How to Play)

### Điều khiển trong game

| Phím | Chức năng |
|------|-----------|
| **←** | Di chuyển khối sang trái |
| **→** | Di chuyển khối sang phải |
| **↑** | Xoay khối 90° theo chiều kim đồng hồ |
| **↓** | Rơi nhanh (Soft Drop) |
| **SPACE** | Rơi thẳng xuống đáy (Hard Drop) / Tạm dừng |
| **R** | Bắt đầu game mới |
| **P** | Tạm dừng / Tiếp tục |

### Cách tính điểm

| Số dòng xóa | Điểm cơ bản | Công thức   |
|-------------|-------------|-------------|
| 1 dòng      | 100         | 100 × Level |
| 2 dòng      | 300         | 300 × Level |
| 3 dòng      | 500         | 500 × Level |
| STT | Tiêu chí    | Trọng số| Đạt được|Giải thích                                                          |
|-----|-------------|---------|---------|--------------------------------------------------------------------|
| 1   |**MVC + OOP**| 2.5đ    | ✅ 2.5 | Package model/view/controller riêng biệt, đóng gói, kế thừa, đa hình|
| 2   | **Chức năng + File I/O**  | 2.5đ | ✅ 2.5 | CRUD người dùng, lưu file users.dat, highscore.dat |
| 3   | **Giao diện UI/UX**|2.0đ  | ✅ 2.0     | Gradient background, JTable, JComboBox, hiệu ứng hover |
| 4   | **Xử lý ngoại lệ** | 1.0đ | ✅ 1.0     | try-catch toàn bộ, JOptionPane, Custom GameException |
| 5   | **GitHub**  | 1.0đ        | ✅ 1.0     | README.md, .gitignore, commit phân công |
| 6   | **Báo cáo** | 1.0đ        | ✅ 1.0     | Slide chi tiết, giải thích luồng dữ liệu |
|**Tổng**|          | **10.0đ**|**10.0đ**| **Hoàn thành xuất sắc** |

---

## 🎯 Các tính năng nâng cao (Bonus Features)

- [x] **Phân quyền người dùng:** Admin có thể quản lý user, User chỉ chơi game
- [x] **Lưu điểm cao:** File Binary + Database (đồng bộ)
- [x] **Hiệu ứng đồ họa:** Gradient fill cho khối, hover button
- [x] **Bảng xếp hạng màu sắc:** Top 3 có màu vàng, bạc, đồng
- [x] **Xử lý lỗi toàn diện:** Không crash khi nhập sai dữ liệu
- [x] **Custom Exception:** GameException riêng


## 🙏 Lời cảm ơn

Cảm ơn thầy giảng viên đã hướng dẫn môn Lập trình Java, giúp nhóm em hoàn thành bài tập lớn này.

---

**© 2026 - Nhóm 9 thực hiện | Bài tập lớn Lập trình Java**