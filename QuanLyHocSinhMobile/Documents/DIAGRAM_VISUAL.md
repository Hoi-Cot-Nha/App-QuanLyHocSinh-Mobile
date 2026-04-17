# 🎨 DIAGRAM - CẤU TRÚC PROJECT TRỰC QUAN

## 1️⃣ KIẾN TRÚC TỔNG THỂ

```
┌─────────────────────────────────────────────────────────────┐
│                      ANDROID APP                           │
│              (QuanLyHocSinh - Quản lý học sinh)            │
└─────────────────────────────────────────────────────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
┌───────▼─────────┐ ┌─────────▼─────────┐ ┌───────▼──────────┐
│   Activity/     │ │   Fragment/       │ │   Service        │
│   Fragment      │ │   ViewModel       │ │                  │
│                 │ │                   │ │                  │
│ (User Interface)│ │ (Logic + State)   │ │ (Background)     │
└────────┬────────┘ └────────┬──────────┘ └────────┬─────────┘
         │                   │                    │
         │                   └────────┬────────────┘
         │                            │
         └────────┬───────────────────┘
                  │
         ┌────────▼──────────┐
         │   Repository      │
         │                   │
         │ (Abstraction)     │
         └────────┬──────────┘
                  │
         ┌────────▼──────────┐
         │   DAO Interface   │
         │                   │
         │ (@Query, @Insert) │
         └────────┬──────────┘
                  │
   ┌──────────────┼──────────────┐
   │              │              │
┌──▼────┐ ┌───────▼────┐ ┌──────▼────┐
│Insert │ │   Update   │ │  Delete   │
│@Insert│ │  @Update   │ │  @Delete  │
└───────┘ └────────────┘ └───────────┘
   │              │              │
   └──────────────┼──────────────┘
                  │
         ┌────────▼──────────┐
         │   SQLite DB       │
         │                   │
         │  (Device Storage) │
         └───────────────────┘
```

---

## 2️⃣ FILE STRUCTURE - CẤU TRÚC THƯ MỤC

```
QuanLyHocSinhMobile/
│
├── app/
│   ├── build.gradle.kts          ← Build configuration
│   │                               (Dependencies, SDK level, etc)
│   │
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/quanlyhocsinhmobile/
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── AppDatabase.java     ← Database config
│   │   │   │   │   │   ├── KhoiTaoDatabase.java ← Seed data
│   │   │   │   │   │   ├── DAO/                  ← Data Access
│   │   │   │   │   │   │   ├── TaiKhoanDAO.java
│   │   │   │   │   │   │   ├── DiemDAO.java
│   │   │   │   │   │   │   ├── HocSinhDAO.java
│   │   │   │   │   │   │   └── ...
│   │   │   │   │   │   └── Model/                ← Entities
│   │   │   │   │   │       ├── TaiKhoan.java    (@Entity)
│   │   │   │   │   │       ├── Diem.java        (@Entity)
│   │   │   │   │   │       ├── HocSinh.java     (@Entity)
│   │   │   │   │   │       └── ...
│   │   │   │   │   │
│   │   │   │   │   └── repository/               ← Business logic
│   │   │   │   │       ├── TaiKhoanRepository.java
│   │   │   │   │       ├── DiemRepository.java
│   │   │   │   │       ├── HocSinhRepository.java
│   │   │   │   │       └── ...
│   │   │   │   │
│   │   │   │   ├── ui/                           ← User Interface
│   │   │   │   │   ├── MainActivity.java
│   │   │   │   │   ├── zMainForm/
│   │   │   │   │   ├── tien/
│   │   │   │   │   └── ...
│   │   │   │   │
│   │   │   │   └── utils/                        ← Utilities
│   │   │   │       ├── ExcelHelper.java          (Export to Excel)
│   │   │   │       ├── FormatDate.java           (Date formatting)
│   │   │   │       ├── LoginActivity.java        (Login logic)
│   │   │   │       ├── PhanQuyen.java            (Authorization)
│   │   │   │       └── ...
│   │   │   │
│   │   │   └── res/                              ← Resources
│   │   │       ├── layout/                       (XML Layouts)
│   │   │       ├── drawable/                     (Images)
│   │   │       ├── values/                       (Strings, Colors)
│   │   │       └── ...
│   │   │
│   │   ├── androidTest/
│   │   └── test/
│   │
│   └── build/
│       └── generated/
│           └── ksp/
│               └── debug/
│                   └── java/...
│                       └── TaiKhoanDAO_Impl.java ← Generated!
│
├── gradle/
│   ├── gradle-daemon-jvm.properties
│   ├── libs.versions.toml          ← Dependency versions
│   └── wrapper/
│
├── build.gradle.kts                ← Root build
├── settings.gradle.kts             ← Project settings
├── gradle.properties
└── gradlew, gradlew.bat            ← Gradle wrapper

TÀI LIỆU MÌNH VỪA TẠO:
├── HUONG_DAN_CU_PHAP.md            ← Hướng dẫn chi tiết (30-45 phút)
├── VI_DU_THUC_HANH.java            ← Ví dụ thực hành (20-30 phút)
├── CHEAT_SHEET.md                  ← Tra cứu nhanh (5-10 phút)
└── README_HUONG_DAN.md             ← Bản chỉ dẫn này (Index)
```

---

## 3️⃣ DATA FLOW - LUỒNG DỮ LIỆU

### Lấy danh sách học sinh (Get All)

```
┌──────────────────────┐
│   Activity/Fragment  │
│  (MainActivity.java) │
└──────────┬───────────┘
           │ repo.getAllHocSinh()
           │
┌──────────▼───────────────────┐
│   Repository                  │
│ (HocSinhRepository.java)      │
│                               │
│ public List<HocSinh> getAll() │
└──────────┬────────────────────┘
           │ hocSinhDAO.getAll()
           │
┌──────────▼──────────────────────────┐
│   DAO Interface                      │
│ (HocSinhDAO.java)                   │
│                                      │
│ @Query("SELECT * FROM HocSinh")     │
│ List<HocSinh> getAll();             │
└──────────┬───────────────────────────┘
           │ (Room generates code)
           │
┌──────────▼──────────────────────────┐
│   DAO Implementation                 │
│ (HocSinhDAO_Impl.java) - GENERATED! │
│                                      │
│ Execute SQL query                   │
└──────────┬───────────────────────────┘
           │
┌──────────▼───────────────────────────┐
│   SQLite Database                     │
│                                       │
│  HocSinh table:                       │
│  ┌───────────────────────────────┐   │
│  │ MaHS │ HoTen      │ MaLop    │   │
│  ├──────┼────────────┼──────────┤   │
│  │HS001 │ Nguyễn A   │ 10A1     │   │
│  │HS002 │ Trần B     │ 10A2     │   │
│  │HS003 │ Phạm C     │ 10A1     │   │
│  └───────────────────────────────┘   │
└─────────────────────────────────────┘
           │
           │ Return List<HocSinh>
           │
┌──────────▼──────────────────────────┐
│   Back to Activity                   │
│                                      │
│   list = repo.getAllHocSinh()        │
│   // list now has 3 HocSinh objects  │
└──────────────────────────────────────┘
```

### Thêm học sinh (Insert)

```
┌────────────────────────────────────┐
│   Activity/Fragment                 │
│   User clicks "Add Student"         │
│                                     │
│   HocSinh hs = new HocSinh();       │
│   hs.setMaHS("HS004");              │
│   hs.setHoTen("Lê D");              │
│   hs.setMaLop("10A3");              │
└──────────┬─────────────────────────┘
           │ repo.insert(hs)
           │
┌──────────▼──────────────────────┐
│   Repository                    │
│ (HocSinhRepository.java)       │
│                                 │
│ public void insert(HocSinh hs) │
└──────────┬──────────────────────┘
           │ hocSinhDAO.insert(hs)
           │
┌──────────▼────────────────────────────────┐
│   DAO Interface                           │
│ (HocSinhDAO.java)                        │
│                                           │
│ @Insert                                  │
│ void insert(HocSinh hs);                 │
└──────────┬─────────────────────────────────┘
           │ (Room generates INSERT SQL)
           │
┌──────────▼─────────────────────────────────┐
│   Generated DAO Implementation             │
│   (HocSinhDAO_Impl.java)                   │
│                                            │
│   INSERT INTO HocSinh                      │
│   (MaHS, HoTen, MaLop, ...)                │
│   VALUES ('HS004', 'Lê D', '10A3', ...)    │
└──────────┬──────────────────────────────────┘
           │
┌──────────▼────────────────────────────────┐
│   SQLite Database                        │
│                                          │
│   HocSinh table updated:                 │
│   HS001 │ Nguyễn A │ 10A1               │
│   HS002 │ Trần B   │ 10A2               │
│   HS003 │ Phạm C   │ 10A1               │
│   HS004 │ Lê D     │ 10A3  ← NEW!       │
└────────────────────────────────────────┘
```

---

## 4️⃣ DATABASE SCHEMA - LƯỢC ĐỒ BẢNG

```
┌─────────────────────────────────────────────────────────────────┐
│                   QUANLYHOCSINH DATABASE (v18)                 │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────┐     ┌──────────────────────────────┐
│       HocSinh (50 records)  │────▶│ Lop (12 classes)            │
├─────────────────────────────┤     ├──────────────────────────────┤
│ MaHS (PK)                   │     │ MaLop (PK)                  │
│ HoTen                       │     │ TenLop                      │
│ NgaySinh                    │     │ NienKhoa                    │
│ MaLop (FK) ─────────────────┼────▶│ MaGVCN (FK→GiaoVien)       │
│ MaDT (FK) ──┐              │     └──────────────────────────────┘
└─────────────┼──────────────┘
              │
              ▼
        ┌─────────────────────────┐
        │ DoiTuongUuTien (3)      │
        ├─────────────────────────┤
        │ MaDT (PK)               │
        │ TenDT                   │
        │ TiLeGiamHocPhi          │
        └─────────────────────────┘

┌─────────────────────┐       ┌──────────────────────────────┐
│  TaiKhoan (3-4)    │       │  GiaoVien (12 records)      │
├─────────────────────┤       ├──────────────────────────────┤
│ TenDangNhap (PK)   │       │ MaGV (PK)                   │
│ MatKhau            │       │ HoTen                       │
│ Quyen              │       │ NgaySinh                    │
│ MaNguoiDung        │       │ MaToHop (FK→ToHopMon)       │
└─────────────────────┘       │ MaMH (FK→MonHoc)            │
                              └──────────────────────────────┘

┌──────────────────────┐   ┌──────────────────────────────┐
│  MonHoc (10)        │───│  ToHopMon (2)               │
├──────────────────────┤   ├──────────────────────────────┤
│ MaMH (PK)           │   │ MaToHop (PK)               │
│ TenMH               │   │ TenToHop                   │
└──────────────────────┘   └──────────────────────────────┘

┌───────────────────────────────────┐
│  Diem (50 HS × 10 MH = 500)      │
├───────────────────────────────────┤
│ MaHS (PK, FK→HocSinh)             │
│ MaMH (PK, FK→MonHoc)              │
│ HocKy (PK)                        │
│ Diem15p, Diem1Tiet               │
│ DiemGiuaKy, DiemCuoiKy           │
│ DiemTongKet                       │
└───────────────────────────────────┘

┌──────────────────────────────────┐  ┌─────────────────────────────┐
│  HanhKiem (50 HS)               │  │  HocPhi (50 HS)            │
├──────────────────────────────────┤  ├─────────────────────────────┤
│ MaHS (FK→HocSinh)               │  │ MaHS (FK→HocSinh)           │
│ HocKy                            │  │ HocKy                       │
│ NamHoc                           │  │ NamHoc                      │
│ XepLoai                          │  │ TongTien                    │
│ NhanXet                          │  │ MienGiam                    │
└──────────────────────────────────┘  │ PhaiDong                    │
                                      │ TrangThai                   │
                                      └─────────────────────────────┘

┌──────────────────────────────┐  ┌─────────────────────────────┐
│  ThongBao                     │  │  LichThi (10+ records)     │
├──────────────────────────────┤  ├─────────────────────────────┤
│ MaThongBao (PK)              │  │ MaLichThi (PK)             │
│ TieuDe                        │  │ TenKyThi                   │
│ NoiDung                       │  │ MaMH (FK→MonHoc)           │
│ NgayTao                       │  │ NgayThi                    │
│ NguoiGui                      │  │ GioBatDau, GioKetThuc      │
└──────────────────────────────┘  │ MaPhong (FK→PhongHoc)      │
                                  └─────────────────────────────┘

┌──────────────────────────────┐  ┌─────────────────────────────┐
│  PhongHoc (5)                │  │  ThoiKhoaBieu (10+)        │
├──────────────────────────────┤  ├─────────────────────────────┤
│ MaPhong (PK)                 │  │ MaLop (FK→Lop)             │
│ TenPhong                     │  │ MaMH (FK→MonHoc)           │
│ SucChua                      │  │ MaGV (FK→GiaoVien)         │
│ LoaiPhong                    │  │ MaPhong (FK→PhongHoc)      │
│ TinhTrang                    │  │ Thu, TietBatDau, TietKT   │
└──────────────────────────────┘  └─────────────────────────────┘

┌──────────────────────────────┐
│  PhucKhao (10+ records)      │
├──────────────────────────────┤
│ MaPhucKhao (PK)              │
│ MaHS (FK→HocSinh)            │
│ MaMH (FK→MonHoc)             │
│ LyDo                         │
│ NgayGui                      │
│ TrangThai                    │
└──────────────────────────────┘

FK = Foreign Key (Relationship)
PK = Primary Key
CASCADE = Auto update/delete
```

---

## 5️⃣ LIFECYCLE - VÒNG ĐỜI

### Application Startup

```
1. App launched
2. AppDatabase.getDatabase(context) called
   │
   ├─▶ Check if INSTANCE == null
   │   ├─▶ YES: synchronized block
   │   │   ├─▶ Double-check: INSTANCE still null?
   │   │   │   ├─▶ YES: Create Room.databaseBuilder()
   │   │   │   │   ├─▶ addCallback(sRoomDatabaseCallback)
   │   │   │   │   ├─▶ fallbackToDestructiveMigration()
   │   │   │   │   ├─▶ allowMainThreadQueries()
   │   │   │   │   └─▶ build()
   │   │   │   │
   │   │   │   └─▶ NO: Return existing INSTANCE
   │   │   └─▶ Exit synchronized
   │   │
   │   └─▶ NO: Return INSTANCE (already created)
   │
3. Database created (or loaded if exists)
4. onOpen() callback triggered
   │
   └─▶ KhoiTaoDatabase.checkAndSeedData(db)
       ├─▶ Check if data exists
       ├─▶ If empty: Seed sample data (50 HS, 10 MH, etc)
       └─▶ Seed TaiKhoan (login accounts)

5. Repository created
6. Activity can use repo.getAll(), repo.insert(), etc.
```

### Query Execution Flow

```
repo.getAllHocSinh()
    │
    ├─▶ HocSinhRepository.getAllHocSinh()
    │   │
    │   └─▶ hocSinhDAO.getAll()
    │       │
    │       ├─▶ Room generates TaiKhoanDAO_Impl
    │       │
    │       └─▶ Execute SQL:
    │           "SELECT * FROM HocSinh"
    │
    ├─▶ SQLite executes query
    │   │
    │   ├─▶ Get cursor from database
    │   ├─▶ moveToFirst() → navigate rows
    │   ├─▶ getString(index) → extract values
    │   └─▶ Create HocSinh objects
    │
    └─▶ Return List<HocSinh>
```

---

## 6️⃣ GENERIC PROGRAMMING CONCEPT

### Export to Excel - Type Safe

```
ExcelHelper.exportToExcel<T>()

Case 1: Export HocSinh List
─────────────────────────────
exportToExcel(context, "Students", "Students",
    headers: ["Mã", "Tên", "Lớp"],
    data: List<HocSinh>,  // T = HocSinh
    rowMapper: (row, hs: HocSinh) -> {
        row.createCell(0).setCellValue(hs.getMaHS());
        row.createCell(1).setCellValue(hs.getHoTen());
        row.createCell(2).setCellValue(hs.getMaLop());
    }
)

Output Excel:
┌─────┬───────────┬────────┐
│ Mã  │ Tên       │ Lớp    │
├─────┼───────────┼────────┤
│HS01│ Nguyễn A │ 10A1  │
│HS02│ Trần B   │ 10A2  │
└─────┴───────────┴────────┘

Case 2: Export Diem List
─────────────────────────
exportToExcel(context, "Scores", "Scores",
    headers: ["Mã HS", "Mã MH", "Điểm"],
    data: List<Diem>,  // T = Diem
    rowMapper: (row, d: Diem) -> {
        row.createCell(0).setCellValue(d.getMaHS());
        row.createCell(1).setCellValue(d.getMaMH());
        row.createCell(2).setCellValue(d.getDiemTongKet());
    }
)

Output Excel:
┌───────┬───────┬───────┐
│ Mã HS │ Mã MH │ Điểm  │
├───────┼───────┼───────┤
│HS001 │ MH01 │ 8.5   │
│HS001 │ MH02 │ 9.0   │
└───────┴───────┴───────┘

TƯƠNG ĐƯƠNG JAVA (không generic):
─────────────────────────────────
// Phải viết 2 method riêng:
exportToExcelHocSinh(List<HocSinh>) {...}
exportToExcelDiem(List<Diem>) {...}
// Lặp code rất nhiều!

// Với Generic: 1 method làm hết!
```

---

## 7️⃣ SQL JOIN VISUALIZATION

### CROSS JOIN (All combinations)

```
HocSinh:           MonHoc:
┌──────┐           ┌──────┐
│HS001 │           │ MH01 │
│HS002 │           │ MH02 │
│HS003 │           │ MH03 │
└──────┘           └──────┘

CROSS JOIN Result: (HS001,MH01) (HS001,MH02) (HS001,MH03)
                   (HS002,MH01) (HS002,MH02) (HS002,MH03)
                   (HS003,MH01) (HS003,MH02) (HS003,MH03)
                   = 3 × 3 = 9 combinations

Visual:
   MH01  MH02  MH03
HS01  ✓     ✓     ✓
HS02  ✓     ✓     ✓
HS03  ✓     ✓     ✓
```

### LEFT JOIN (All from left + matching from right)

```
HocSinh:           DoiTuongUuTien:
┌──────────────┐   ┌────────────────────┐
│MaHS│MaDT    │   │MaDT │TiLeGiam      │
├──────────────┤   ├────────────────────┤
│HS01│DT00    │   │DT00 │ 0.0 (thường) │
│HS02│DT01    │   │DT01 │ 0.5 (nghèo)  │
│HS03│DT02    │   │DT02 │ 1.0 (binh)   │
│HS04│NULL    │   └────────────────────┘
└──────────────┘

LEFT JOIN Result:
┌──────┬─────┬─────────────┐
│MaHS │MaDT │TiLeGiamHocPhi│
├──────┼─────┼─────────────┤
│HS01 │DT00 │ 0.0         │
│HS02 │DT01 │ 0.5         │
│HS03 │DT02 │ 1.0         │
│HS04 │NULL │ NULL ← COALESCE→0.0
└──────┴─────┴─────────────┘

Visual (X = found, O = not found in right):
┌──────┬────────────────┐
│Left  │Right           │
├──────┼────────────────┤
│HS01 │ X (matched)     │
│HS02 │ X (matched)     │
│HS03 │ X (matched)     │
│HS04 │ O (not matched) │
└──────┴────────────────┘
ALL rows from LEFT are kept!
```

---

## 8️⃣ THREAD-SAFE SINGLETON

```
WITHOUT Singleton (❌ BAD):
─────────────────────────
Thread 1: new AppDatabase() → Create instance A
Thread 2: new AppDatabase() → Create instance B
Result: 2 instances, memory leak, sync issues!

WITH Singleton + synchronized + volatile (✅ GOOD):
────────────────────────────────────────────────
Thread 1: getDatabase(ctx)
          ├─ Check: INSTANCE == null? → YES
          ├─ Lock (synchronized)
          ├─ Double-check: INSTANCE == null? → YES
          ├─ Create instance A
          └─ Unlock

Thread 2: getDatabase(ctx)
          ├─ Check: INSTANCE == null? → NO (cached in volatile)
          ├─ Return instance A (fast! no lock!)

Result: Only 1 instance, efficient locking!

Timeline:
────────
Time    Thread 1                Thread 2
─────────────────────────────────────────────
t0      Check null → YES        Check null → YES
t1      Lock                    Wait (blocked)
t2      Create instance A
t3      Unlock                  Lock
t4                              Check null → NO
t5                              Return instance A
t6                              Unlock

KEY: "volatile" makes INSTANCE visible to all threads instantly
     "synchronized" prevents race condition during creation
     "double-check" reduces lock contention
```

---

## 9️⃣ REPOSITORY PATTERN BENEFITS

```
WITHOUT Repository (Tightly coupled):
──────────────────────────────────────
Activity
  │
  ├─▶ Create DAO directly
  ├─▶ Call DAO methods
  ├─▶ Handle errors
  ├─▶ Convert data
  └─▶ Manage transactions
  │
  └─▶ Problem if database changes!
      Must modify ALL activities


WITH Repository (Loosely coupled):
────────────────────────────────────
Activity
  │
  └─▶ Use Repository
      │
      └─▶ Repository
          ├─▶ Manages DAO
          ├─▶ Handles errors
          ├─▶ Converts data
          └─▶ Manages transactions
          │
          └─▶ Problem if database changes?
              Only modify Repository!
              Activities unaffected!


Benefit:
────────
Change from SQLite → Firebase:
  Before: Modify 20+ activities
  After: Modify 1 repository

Testing:
  Before: Need real database
  After: Mock repository


Analogy:
────────
WITHOUT Repository = Calling restaurant kitchen directly
  → Chef changes recipe → All customers confused!

WITH Repository = Ordering through waiter (repository)
  → Chef changes recipe → Waiter adapts → Customers happy!
```

---

## 🔟 SQL CASE...WHEN (If-Else in SQL)

```
Simple IF-ELSE:
───────────────
SELECT HocSinh.*,
CASE (ABS(RANDOM()) % 4)
  WHEN 0 THEN 'Tốt'
  WHEN 1 THEN 'Khá'
  WHEN 2 THEN 'Trung bình'
  ELSE 'Yếu'
END AS XepLoai
FROM HocSinh;

Output:
┌──────┬─────────────┬──────────────┐
│MaHS │ HoTen       │ XepLoai      │
├──────┼─────────────┼──────────────┤
│HS001│ Nguyễn A   │ Tốt          │
│HS002│ Trần B     │ Khá          │
│HS003│ Phạm C     │ Trung bình   │
│HS004│ Lê D       │ Yếu          │
└──────┴─────────────┴──────────────┘


Complex IF-ELSE:
────────────────
SELECT HocSinh.*,
CASE
  WHEN DiemTongKet >= 8.0 THEN 'Giỏi'
  WHEN DiemTongKet >= 6.5 THEN 'Khá'
  WHEN DiemTongKet >= 5.0 THEN 'Trung bình'
  ELSE 'Yếu'
END AS XepLoai
FROM (
  SELECT MaHS, AVG(DiemTongKet) AS DiemTongKet
  FROM Diem
  GROUP BY MaHS
);

Output:
┌──────┬──────────────┬──────────────┐
│MaHS │ DiemTongKet │ XepLoai      │
├──────┼──────────────┼──────────────┤
│HS001│ 8.5         │ Giỏi         │
│HS002│ 7.2         │ Khá          │
│HS003│ 5.5         │ Trung bình   │
│HS004│ 4.0         │ Yếu          │
└──────┴──────────────┴──────────────┘
```

---

## 📊 SUMMARY TABLE

| Concept | File | Purpose | Example |
|---------|------|---------|---------|
| **@Entity** | Diem.java | Table definition | `@Entity(tableName="Diem")` |
| **@ForeignKey** | Diem.java | Relationship | `@ForeignKey(entity=HocSinh.class)` |
| **CASCADE** | Diem.java | Auto sync | `onDelete = CASCADE` |
| **Repository** | TaiKhoanRepository.java | Business layer | `repo.insert(tk)` |
| **Generic <T>** | ExcelHelper.java | Type safe | `<T> void export(List<T>)` |
| **CROSS JOIN** | KhoiTaoDatabase.java | All combinations | 50 HS × 10 MH = 500 |
| **CASE...WHEN** | KhoiTaoDatabase.java | SQL if-else | `CASE WHEN...THEN` |
| **LEFT JOIN** | KhoiTaoDatabase.java | Safe join | `LEFT JOIN with NULL` |
| **Lambda** | ExcelHelper.java | Functional | `(row, hs) -> {...}` |
| **Singleton** | AppDatabase.java | Single instance | `synchronized getDatabase()` |
| **try-with** | KhoiTaoDatabase.java | Auto close | `try (Cursor c = ...)` |

---

**🎨 Hy vọng các diagram này giúp bạn hình dung rõ hơn!**

Đọc lại:
1. **README_HUONG_DAN.md** - Bản chỉ dẫn
2. **HUONG_DAN_CU_PHAP.md** - Chi tiết
3. **VI_DU_THUC_HANH.java** - Ví dụ
4. **CHEAT_SHEET.md** - Tra cứu nhanh
5. **DIAGRAM_VISUAL.md** - File này (Diagram)

