/**
 * VÍ DỤ THỰC HÀNH - ĐỂ HIỂU RÕ CÁC CỰ PHÁP KHÓ
 *
 * File này chứa các ví dụ cụ thể từ project, kèm theo giải thích chi tiết.
 * Hãy đọc kỹ từng phần để hiểu rõ hơn!
 */

// ============================================
// VÍ DỤ 1: ANNOTATION & ENTITY
// ============================================

/*
Trong file Diem.java, bạn sẽ thấy:

@Entity(tableName = "Diem",
        primaryKeys = {"maHS", "maMH", "hocKy"},
        foreignKeys = {...},
        indices = {@Index("maMH")})
public class Diem {
    @NonNull
    private String maHS;

    @NonNull
    @Ignore
    public Diem(String maHS, String maMH, int hocKy, ...) {
        this.maHS = maHS;
        ...
    }
}

GIẢI THÍCH:
- @Entity: Đây là một bảng trong database
- tableName = "Diem": Tên bảng là "Diem"
- primaryKeys = {"maHS", "maMH", "hocKy"}:
  * Không phải 1 cột làm PRIMARY KEY, mà là 3 cột kết hợp
  * Ví dụ:
    - Học sinh HS001 + Môn MH01 + Học kỳ 1 = 1 record duy nhất
    - Học sinh HS001 + Môn MH02 + Học kỳ 1 = 1 record khác
- @NonNull: Cột này BẮT BUỘC phải có giá trị, không được NULL
- @Ignore: Constructor này không dùng trong Room, chỉ dùng khi bạn tạo object tay
- indices = {@Index("maMH")}: Tạo chỉ mục trên cột maMH để tìm kiếm nhanh
*/

// ============================================
// VÍ DỤ 2: GENERIC PROGRAMMING
// ============================================

/*
Trong file ExcelHelper.java:

public static <T> void exportToExcel(Context context, String fileNamePrefix, String sheetName,
                                     String[] headers, List<T> data, BiConsumer<Row, T> rowMapper) {
    ...
}

GIẢI THÍCH:
- <T>: "T" đại diện cho MỌI TYPE - có thể là String, Integer, HocSinh, Diem, etc.
- Cách dùng:

  // Xuất danh sách HocSinh
  exportToExcel(context, "Students", "HocSinh",
                new String[]{"Mã", "Tên", "Lớp"},
                hocSinhList,
                (row, hs) -> { // T = HocSinh ở đây
                    row.createCell(0).setCellValue(hs.getMaHS());
                    row.createCell(1).setCellValue(hs.getHoTen());
                    row.createCell(2).setCellValue(hs.getMaLop());
                });

  // Xuất danh sách Diem
  exportToExcel(context, "Scores", "Diem",
                new String[]{"Mã HS", "Mã MH", "Điểm"},
                diemList,
                (row, diem) -> { // T = Diem ở đây
                    row.createCell(0).setCellValue(diem.getMaHS());
                    row.createCell(1).setCellValue(diem.getMaMH());
                    row.createCell(2).setCellValue(diem.getDiemTongKet());
                });

- BiConsumer<Row, T>: Functional Interface nhận 2 tham số
  * Row: dòng trong Excel
  * T: data object
  * Không return gì cả (void)
*/

// ============================================
// VÍ DỤ 3: SQL CROSS JOIN
// ============================================

/*
Trong file KhoiTaoDatabase.java:

db.execSQL("INSERT INTO Diem (MaHS, MaMH, HocKy, Diem15p, Diem1Tiet, DiemGiuaKy, DiemCuoiKy) " +
        "SELECT h.MaHS, m.MaMH, 1, " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1) " +
        "FROM HocSinh h CROSS JOIN MonHoc m;");

GIẢI THÍCH CHI TIẾT:

1. Giả sử:
   - Bảng HocSinh có 50 bản ghi (học sinh)
   - Bảng MonHoc có 10 bản ghi (môn học)

2. CROSS JOIN h.MaHS, m.MaMH:
   - HocSinh 1 + MonHoc 1 = 1 dòng
   - HocSinh 1 + MonHoc 2 = 1 dòng
   - ...
   - HocSinh 1 + MonHoc 10 = 1 dòng (10 dòng total cho học sinh 1)
   - HocSinh 2 + MonHoc 1 = 1 dòng
   - ...
   - HocSinh 50 + MonHoc 10 = 1 dòng (cuối cùng)
   - TOTAL: 50 × 10 = 500 dòng

3. Điểm số: ROUND((ABS(RANDOM()) % 101) / 10.0, 1)
   - ABS(RANDOM()): Lấy số ngẫu nhiên dương
   - % 101: Chia dư lấy 0-100
   - / 10.0: Chia 10 để được 0.0 - 10.0
   - ROUND(..., 1): Làm tròn 1 chữ số thập phân
   - Ví dụ: 7.3, 8.5, 9.1

TƯƠNG ĐƯƠNG JAVA:
for (HocSinh hs : hocSinhList) {  // 50 lần
    for (MonHoc mh : monHocList) {  // 10 lần
        Diem d = new Diem();
        d.setMaHS(hs.getMaHS());
        d.setMaMH(mh.getMaMH());
        d.setDiem15p(Math.random() * 10);
        d.setDiem1Tiet(Math.random() * 10);
        d.setDiemGiuaKy(Math.random() * 10);
        d.setDiemCuoiKy(Math.random() * 10);
        diemDAO.insert(d);  // Insert 500 lần
    }
}

WHY SQL BETTER: 1 query SQL nhanh hơn 500 lần loop + insert
*/

// ============================================
// VÍ DỤ 4: SQL CASE...WHEN
// ============================================

/*
Trong file KhoiTaoDatabase.java:

db.execSQL("INSERT INTO HanhKiem (MaHS, HocKy, NamHoc, XepLoai, NhanXet) " +
        "SELECT MaHS, 1, '2025-2026', " +
        "CASE (ABS(RANDOM()) % 4) " +
        "  WHEN 0 THEN 'Tốt' " +
        "  WHEN 1 THEN 'Khá' " +
        "  WHEN 2 THEN 'Trung bình' " +
        "  ELSE 'Tốt' " +
        "END, " +
        "'Tuân thủ nội quy.' FROM HocSinh;");

GIẢI THÍCH:

CASE (ABS(RANDOM()) % 4):
  WHEN 0 THEN 'Tốt'           // Nếu bằng 0 → 'Tốt'
  WHEN 1 THEN 'Khá'           // Nếu bằng 1 → 'Khá'
  WHEN 2 THEN 'Trung bình'    // Nếu bằng 2 → 'Trung bình'
  ELSE 'Tốt'                  // Nếu không match → 'Tốt' (default)
END

TƯƠNG ĐƯƠNG JAVA:
String xepLoai;
int random = new Random().nextInt(4); // 0, 1, 2, 3
switch (random) {
    case 0: xepLoai = "Tốt"; break;
    case 1: xepLoai = "Khá"; break;
    case 2: xepLoai = "Trung bình"; break;
    default: xepLoai = "Tốt";
}

HOẶC DÙNG IF-ELSE:
String xepLoai;
if (random == 0) {
    xepLoai = "Tốt";
} else if (random == 1) {
    xepLoai = "Khá";
} else if (random == 2) {
    xepLoai = "Trung bình";
} else {
    xepLoai = "Tốt";
}

Kết quả cho tất cả 50 học sinh:
HS001 → Khá
HS002 → Tốt
HS003 → Trung bình
...
HS050 → Tốt
*/

// ============================================
// VÍ DỤ 5: LEFT JOIN & COALESCE
// ============================================

/*
Trong file KhoiTaoDatabase.java:

db.execSQL("INSERT INTO HocPhi (MaHS, HocKy, NamHoc, TongTien, MienGiam, PhaiDong, TrangThai) " +
        "SELECT hs.MaHS, 1, '2025-2026', 2000000, " +
        "COALESCE(dt.TiLeGiamHocPhi, 0) * 2000000, " +
        "2000000 - (COALESCE(dt.TiLeGiamHocPhi, 0) * 2000000), " +
        "CASE (ABS(RANDOM()) % 3) " +
        "  WHEN 0 THEN 'Chưa đóng' " +
        "  WHEN 1 THEN 'Đã đóng' " +
        "  ELSE 'Chưa đóng' " +
        "END " +
        "FROM HocSinh hs LEFT JOIN DoiTuongUuTien dt ON hs.MaDT = dt.MaDT;");

GIẢI THÍCH:

1. LEFT JOIN:
   - Lấy TẤT CẢ hàng từ HocSinh (trái)
   - Kết hợp với DoiTuongUuTien (phải) nếu có
   - Nếu không match → giá trị từ DoiTuongUuTien sẽ NULL

   Ví dụ:
   ┌─────────────────────────────────────────────────────────┐
   │ hs.MaHS │ hs.MaDT │ dt.TiLeGiamHocPhi (NULL if no match) │
   ├─────────────────────────────────────────────────────────┤
   │ HS001   │ DT00    │ 0.0                                  │ (thường)
   │ HS002   │ DT01    │ 0.5                                  │ (hộ nghèo)
   │ HS003   │ DT02    │ 1.0                                  │ (con thương binh)
   │ HS004   │ NULL    │ NULL                                 │ (không có đối tượng)
   └─────────────────────────────────────────────────────────┘

2. COALESCE(dt.TiLeGiamHocPhi, 0):
   - Nếu dt.TiLeGiamHocPhi = NULL → dùng 0
   - Nếu dt.TiLeGiamHocPhi = 0.5 → dùng 0.5
   - Nếu dt.TiLeGiamHocPhi = 1.0 → dùng 1.0

3. Tính toán:
   - MienGiam = TiLeGiamHocPhi * 2000000
     * Học sinh thường: 0.0 * 2000000 = 0
     * Hộ nghèo: 0.5 * 2000000 = 1000000
     * Thương binh: 1.0 * 2000000 = 2000000

   - PhaiDong = 2000000 - MienGiam
     * Thường: 2000000 - 0 = 2000000
     * Hộ nghèo: 2000000 - 1000000 = 1000000
     * Thương binh: 2000000 - 2000000 = 0

TƯƠNG ĐƯƠNG JAVA:
List<HocSinh> hocSinhList = hocSinhDAO.getAll();
List<DoiTuongUuTien> doiTuongList = doiTuongDAO.getAll();

for (HocSinh hs : hocSinhList) {
    DoiTuongUuTien dt = null;
    for (DoiTuongUuTien d : doiTuongList) {
        if (d.getMaDT().equals(hs.getMaDT())) {
            dt = d;
            break;
        }
    }

    double tileGiam = (dt != null) ? dt.getTiLeGiamHocPhi() : 0.0;
    double miemGiam = tileGiam * 2000000;
    double phaiDong = 2000000 - miemGiam;

    HocPhi hocPhi = new HocPhi();
    hocPhi.setMaHS(hs.getMaHS());
    hocPhi.setTongTien(2000000);
    hocPhi.setMienGiam(miemGiam);
    hocPhi.setPhaiDong(phaiDong);

    hocPhiDAO.insert(hocPhi);
}

WHY SQL BETTER:
- 1 query LEFT JOIN nhanh
- 50 lần loop + search = chậm
*/

// ============================================
// VÍ DỤ 6: LAMBDA & FUNCTIONAL
// ============================================

/*
Trong file ExcelHelper.java:

BiConsumer<Row, T> rowMapper được gọi như:

for (int i = 0; i < data.size(); i++) {
    Row row = sheet.createRow(i + 1);
    rowMapper.accept(row, data.get(i));
}

Cách dùng:
exportToExcel(context, "HocSinh", "Sheet1",
    new String[]{"Mã", "Tên", "Ngày sinh", "Lớp"},
    hocSinhList,
    (row, hs) -> {  // Lambda expression
        row.createCell(0).setCellValue(hs.getMaHS());
        row.createCell(1).setCellValue(hs.getHoTen());
        row.createCell(2).setCellValue(hs.getNgaySinh());
        row.createCell(3).setCellValue(hs.getMaLop());
    }
);

GIẢI THÍCH:
- (row, hs) -> { ... }: Lambda expression
- row: Row object
- hs: HocSinh object (type T)
- { ... }: Code block để mapping dữ liệu vào hàng

TƯƠNG ĐƯƠNG CÁCH CŨ (Anonymous Class):
exportToExcel(context, "HocSinh", "Sheet1",
    new String[]{"Mã", "Tên", "Ngày sinh", "Lớp"},
    hocSinhList,
    new BiConsumer<Row, HocSinh>() {
        @Override
        public void accept(Row row, HocSinh hs) {
            row.createCell(0).setCellValue(hs.getMaHS());
            row.createCell(1).setCellValue(hs.getHoTen());
            row.createCell(2).setCellValue(hs.getNgaySinh());
            row.createCell(3).setCellValue(hs.getMaLop());
        }
    }
);

Lambda ngắn gọn hơn rất nhiều!
*/

// ============================================
// VÍ DỤ 7: SINGLETON PATTERN & VOLATILE
// ============================================

/*
Trong file AppDatabase.java:

private static volatile AppDatabase INSTANCE;
private static final int NUMBER_OF_THREADS = 4;
public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

public static AppDatabase getDatabase(final Context context) {
    if (INSTANCE == null) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, "quanlyhocsinh_db")
                        .addCallback(sRoomDatabaseCallback)
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
    }
    return INSTANCE;
}

GIẢI THÍCH:

1. Singleton Pattern:
   - Chỉ có 1 instance của AppDatabase trong toàn app
   - Tất cả Activities/Fragments share cùng database instance

2. volatile keyword:
   - Đảm bảo tất cả threads thấy giá trị mới nhất của INSTANCE
   - Không cache stale value trong CPU register

3. Double-checked locking:
   - if (INSTANCE == null) {...}: Check 1 lần (cheap)
   - synchronized {...}: Lock thread-safe check 2 lần
   - Giảm lock contention, performance tốt hơn

4. Tại sao cần:
   - Nếu không synchronized: 2 threads có thể tạo 2 database instances (bad!)
   - Nếu synchronized hết: Mỗi getDatabase call bị lock (slow!)

FLOW:
Thread 1: if (INSTANCE == null) → TRUE → lock → if (null) → create → unlock
Thread 2: if (INSTANCE == null) → FALSE → SKIP (không lock, nhanh!)

5. ExecutorService:
   - newFixedThreadPool(4): Pool 4 threads để chạy background tasks
   - Dùng cho database writes, không block UI

6. allowMainThreadQueries():
   - ⚠️ CẢNH BÁO: Cho phép query trên main thread
   - ❌ KHÔNG NÊN: Database operations sẽ freeze UI
   - ✅ NÊN: Chạy trên background thread với ExecutorService
*/

// ============================================
// VÍ DỤ 8: TRY-WITH-RESOURCES
// ============================================

/*
Trong file KhoiTaoDatabase.java:

private static boolean isTableEmpty(SupportSQLiteDatabase db, String tableName) {
    try (Cursor cursor = db.query("SELECT COUNT(*) FROM " + tableName)) {
        if (cursor.moveToFirst()) {
            return cursor.getInt(0) == 0;
        }
    } catch (Exception e) {
        return true;
    }
    return true;
}

GIẢI THÍCH:

1. try (Cursor cursor = ...):
   - Cursor implement AutoCloseable interface
   - Tự động close khi khỏi block try

2. CÁCH CŨ (Java 6):
   Cursor cursor = null;
   try {
       cursor = db.query("SELECT COUNT(*) FROM " + tableName);
       if (cursor.moveToFirst()) {
           return cursor.getInt(0) == 0;
       }
   } catch (Exception e) {
       return true;
   } finally {
       if (cursor != null) cursor.close();  // Phải close tay
   }
   return true;

3. CÁCH MỚI (Java 7+):
   try (Cursor cursor = db.query(...)) {
       if (cursor.moveToFirst()) {
           return cursor.getInt(0) == 0;
       }
   } catch (Exception e) {
       return true;
   }
   return true;
   // cursor automatically closed!

LỢI ÍCH:
- Không quên close resource
- Code sạch, dễ đọc
- Prevent resource leak (memory leak)
*/

// ============================================
// VÍ DỤ 9: FOREIGN KEY & CASCADE
// ============================================

/*
Trong file Diem.java:

@ForeignKey(entity = HocSinh.class,
           parentColumns = "MaHS",
           childColumns = "maHS",
           onDelete = ForeignKey.CASCADE,
           onUpdate = ForeignKey.CASCADE),

GIẢI THÍCH:

1. Foreign Key:
   - Ràng buộc referential integrity
   - Diem.maHS phải tồn tại trong HocSinh.MaHS
   - Không cho phép dữ liệu "mồ côi"

2. CASCADE (Tự động cập nhật liên tục):

   a) onDelete = CASCADE:
      Nếu xóa học sinh → tự động xóa tất cả điểm của HS đó

      DELETE FROM HocSinh WHERE MaHS = 'HS001'
      → Tự động: DELETE FROM Diem WHERE maHS = 'HS001'

   b) onUpdate = CASCADE:
      Nếu sửa mã học sinh → tự động update Diem

      UPDATE HocSinh SET MaHS = 'HS001_NEW' WHERE MaHS = 'HS001'
      → Tự động: UPDATE Diem SET maHS = 'HS001_NEW' WHERE maHS = 'HS001'

3. NGƯỢC LẠI (Nếu không CASCADE):
   onDelete = RESTRICT:
      Không cho xóa học sinh nếu có điểm
      Lỗi: "Cannot delete or update a parent row"

   onDelete = SET_NULL:
      Xóa học sinh → điểm thành NULL (nếu cột nullable)

4. TƯƠNG ĐƯƠNG SQL:
   ALTER TABLE Diem ADD CONSTRAINT fk_diem_hocsinh
   FOREIGN KEY (maHS) REFERENCES HocSinh(MaHS)
   ON DELETE CASCADE
   ON UPDATE CASCADE;

5. VÍ DỤ THỰC TẾ:
   HocSinh table:
   MaHS  | HoTen
   HS001 | Nguyễn A
   HS002 | Trần B

   Diem table:
   MaHS  | MaMH  | DiemTongKet
   HS001 | MH01  | 8.5
   HS001 | MH02  | 9.0
   HS002 | MH01  | 7.5

   Nếu xóa: DELETE FROM HocSinh WHERE MaHS = 'HS001'
   → Tự động xóa: DELETE FROM Diem WHERE maHS = 'HS001'
   Kết quả:
   - HS001 biến mất
   - 2 dòng điểm của HS001 biến mất
   - Chỉ còn điểm HS002 MH01 = 7.5
*/

// ============================================
// VÍ DỤ 10: REPOSITORY + DEPENDENCY INJECTION
// ============================================

/*
Trong file TaiKhoanRepository.java:

public class TaiKhoanRepository {
    private TaiKhoanDAO taiKhoanDAO;

    public TaiKhoanRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taiKhoanDAO = db.taiKhoanDAO();
    }

    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanDAO.getAll();
    }

    public void insert(TaiKhoan taiKhoan) {
        taiKhoanDAO.insert(taiKhoan);
    }
}

GIẢI THÍCH:

1. Dependency Injection:
   - Truyền dependency qua constructor thay vì tạo inside class
   - AppDatabase không được new inside, mà được pass vào

2. CÁC CẤP:
   ┌──────────────────────────┐
   │ Activity/Fragment (UI)   │
   ├──────────────────────────┤
   │ Repository               │ ← Trung gian
   ├──────────────────────────┤
   │ DAO                      │ ← Truy vấn DB
   ├──────────────────────────┤
   │ Room Database            │ ← SQLite
   └──────────────────────────┘

3. CÁC LAYER TRONG PROJECT:

   Activity:
   ──────
   TaiKhoanRepository repo = new TaiKhoanRepository(this);
   List<TaiKhoan> list = repo.getAllTaiKhoan();

   Repository:
   ──────────
   public List<TaiKhoan> getAllTaiKhoan() {
       return taiKhoanDAO.getAll();  // ← Call DAO
   }

   DAO:
   ───
   @Query("SELECT * FROM TaiKhoan")
   public abstract List<TaiKhoan> getAll();

   Database:
   ────────
   AppDatabase -> Room tạo implementation (TaiKhoanDAO_Impl)

4. LỢI ÍCH:
   - Nếu muốn thay Database (SQLite → Firebase): Chỉ sửa Repository
   - Testing: Mock Repository thay vì DB thật
   - Maintainable: Mỗi layer có trách nhiệm riêng
*/

/*
TÓMLƯỢC TẤT CẢ CỰ PHÁP:

┌─────────────────────────────────────────────────────────────────────┐
│ 1. @Entity, @Database, @Ignore       → Room ORM Annotations        │
│ 2. @ForeignKey, CASCADE               → Database Relationships     │
│ 3. @NonNull, @Embedded                → Type & Structure Control   │
│ 4. <T> Generic Programming            → Reusable Code Template     │
│ 5. CROSS JOIN, LEFT JOIN              → SQL Set Operations         │
│ 6. CASE...WHEN...ELSE                 → SQL Conditional Logic      │
│ 7. COALESCE                           → SQL NULL Handling          │
│ 8. Lambda Expressions                 → Functional Programming     │
│ 9. try-with-resources                 → Auto Resource Cleanup      │
│ 10. Singleton + volatile              → Thread-safe Initialization │
│ 11. Repository Pattern                → Architecture Layer         │
│ 12. Dependency Injection              → Loose Coupling             │
└─────────────────────────────────────────────────────────────────────┘

HỌC THÊM:
- Room Database: https://developer.android.com/training/data-storage/room
- SQL Tutorials: https://www.w3schools.com/sql/
- Lambda & Stream: https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
*/

