# 📝 CHEAT SHEET - TÓM TẮT NHANH

## 🎯 BẢNG THAM KHẢO NHANH

### Annotations
| Annotation | Nơi dùng | Ý nghĩa |
|---|---|---|
| `@Database` | Class | Khai báo database chính |
| `@Entity` | Class | Khai báo bảng SQL |
| `@PrimaryKey` | Field | Khóa chính |
| `@ColumnInfo` | Field | Tên cột khác tên field |
| `@NonNull` | Field/Param | Không được null |
| `@Ignore` | Field/Constructor | Bỏ qua khi mapping |
| `@ForeignKey` | @Entity | Ràng buộc khóa ngoại |
| `@Index` | @Entity | Tạo chỉ mục tìm kiếm |
| `@Embedded` | Field | Nhúng object khác |
| `@Query` | Method | Câu lệnh SQL custom |
| `@Insert` | Method | Insert dữ liệu |
| `@Update` | Method | Update dữ liệu |
| `@Delete` | Method | Delete dữ liệu |

---

### SQL Cú pháp
| Cú pháp | Ví dụ | Ý nghĩa |
|---|---|---|
| `SELECT ... FROM` | `SELECT * FROM HocSinh` | Lấy dữ liệu |
| `WHERE` | `WHERE MaHS = 'HS001'` | Điều kiện |
| `ORDER BY` | `ORDER BY HoTen ASC` | Sắp xếp |
| `LIMIT` | `LIMIT 10` | Giới hạn số dòng |
| `COUNT(*)` | `COUNT(*)` | Đếm dòng |
| `CROSS JOIN` | `FROM h CROSS JOIN m` | Tích Descartes |
| `LEFT JOIN` | `LEFT JOIN dt ON ...` | Join với NULL |
| `INNER JOIN` | `INNER JOIN ...` | Join strict |
| `CASE...WHEN` | `CASE WHEN x=0 THEN 'A'` | If-else trong SQL |
| `COALESCE` | `COALESCE(x, 0)` | Null handling |
| `INSERT` | `INSERT INTO ...` | Thêm dữ liệu |
| `UPDATE` | `UPDATE ... SET` | Cập nhật |
| `DELETE` | `DELETE FROM WHERE` | Xóa |

---

### Java Cú pháp
| Cú pháp | Ví dụ | Ý nghĩa |
|---|---|---|
| Generic `<T>` | `<T> void method(List<T>)` | Type parameter |
| Lambda | `(x, y) -> x + y` | Anonymous function |
| Method Reference | `System.out::println` | Reference method |
| try-with | `try (Cursor c = ...)` | Auto close |
| `?:` ternary | `x > 0 ? "+" : "-"` | If-else inline |
| Annotation `@` | `@Override` | Metadata |
| `::` | `String::valueOf` | Reference |
| `||` OR | `a \|\| b` | Logical OR |
| `&&` AND | `a && b` | Logical AND |

---

## 🔍 QUICK REFERENCE BY FILE

### 1️⃣ AppDatabase.java
```java
// Singleton pattern
private static volatile AppDatabase INSTANCE;

// Double-checked locking
public static AppDatabase getDatabase(Context context) {
    if (INSTANCE == null) {
        synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(...)
                    .addCallback(callback)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
            }
        }
    }
    return INSTANCE;
}
```

**Key points:**
- `volatile`: Ensure thread-safe visibility
- `synchronized`: Prevent race condition
- `addCallback()`: Hook database events
- `allowMainThreadQueries()`: ⚠️ Không nên dùng

---

### 2️⃣ Diem.java
```java
@Entity(
    tableName = "Diem",
    primaryKeys = {"maHS", "maMH", "hocKy"},  // Composite key
    foreignKeys = {
        @ForeignKey(entity = HocSinh.class, 
                   parentColumns = "MaHS",
                   childColumns = "maHS",
                   onDelete = CASCADE,
                   onUpdate = CASCADE)
    },
    indices = {@Index("maMH")}
)
public class Diem {
    @NonNull private String maHS;
    
    @Ignore
    public Diem(...) { }  // Custom constructor, ignored by Room
    
    public static class Display {
        @Embedded
        private Diem diem;  // Flatten Diem into Display
    }
}
```

**Key points:**
- `primaryKeys`: Multiple columns as key
- `@ForeignKey`: Enforce referential integrity
- `CASCADE`: Auto update/delete child
- `@Ignore`: Skip this constructor
- `@Embedded`: Flatten nested object

---

### 3️⃣ ExcelHelper.java
```java
public static <T> void exportToExcel(
    Context context, 
    String fileNamePrefix, 
    String sheetName, 
    String[] headers, 
    List<T> data,                          // Generic list
    BiConsumer<Row, T> rowMapper           // Functional interface
) {
    // Usage:
    // exportToExcel(ctx, "HS", "Sheet", 
    //     new String[]{"Mã", "Tên"},
    //     hocSinhList,
    //     (row, hs) -> {  // Lambda!
    //         row.createCell(0).setCellValue(hs.getMaHS());
    //     });
}
```

**Key points:**
- `<T>`: Work with any type
- `BiConsumer<Row, T>`: Accept 2 params, no return
- Lambda: `(a, b) -> { code }`

---

### 4️⃣ KhoiTaoDatabase.java

#### try-with-resources
```java
try (Cursor cursor = db.query("SELECT COUNT(*) FROM " + tableName)) {
    if (cursor.moveToFirst()) {
        return cursor.getInt(0) == 0;
    }
} catch (Exception e) {
    return true;
}
// cursor auto-closed!
```

#### CROSS JOIN (Nested loop in SQL)
```java
// Mỗi HS + tất cả MH = nhiều hàng
"SELECT h.MaHS, m.MaMH FROM HocSinh h CROSS JOIN MonHoc m"
// 50 HS × 10 MH = 500 rows
```

#### CASE...WHEN (If-else in SQL)
```java
"CASE (ABS(RANDOM()) % 4) " +
"  WHEN 0 THEN 'Tốt' " +
"  WHEN 1 THEN 'Khá' " +
"  WHEN 2 THEN 'Trung bình' " +
"  ELSE 'Tốt' " +
"END"
```

#### LEFT JOIN + COALESCE (Safe null handling)
```java
// Tất cả HS + matching DoiTuong (nếu có)
"FROM HocSinh hs " +
"LEFT JOIN DoiTuongUuTien dt ON hs.MaDT = dt.MaDT " +
"SELECT COALESCE(dt.TiLeGiamHocPhi, 0) * 2000000"  // Default 0 if null
```

---

### 5️⃣ TaiKhoanRepository.java

**Architecture Layers:**
```
┌─────────────────┐
│    Activity     │ ← User Interface
├─────────────────┤
│   Repository    │ ← Business Logic
├─────────────────┤
│      DAO        │ ← Database Access
├─────────────────┤
│   Room / DB     │ ← SQLite
└─────────────────┘
```

**Dependency Injection:**
```java
public TaiKhoanRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);  // DI
    taiKhoanDAO = db.taiKhoanDAO();
}
// ✅ Loose coupling
// ❌ Tránh: new AppDatabase() inside class
```

---

## 🚀 COMMONLY USED PATTERNS

### Pattern 1: Read All
```java
// DAO
@Query("SELECT * FROM TaiKhoan")
List<TaiKhoan> getAll();

// Repository
public List<TaiKhoan> getAllTaiKhoan() {
    return taiKhoanDAO.getAll();
}

// Activity
TaiKhoanRepository repo = new TaiKhoanRepository(this);
List<TaiKhoan> list = repo.getAllTaiKhoan();
for (TaiKhoan tk : list) {
    // Process...
}
```

### Pattern 2: Search/Filter
```java
// DAO
@Query("SELECT * FROM TaiKhoan WHERE tenDangNhap LIKE '%' || :query || '%'")
List<TaiKhoan> search(String query);

// Repository
public List<TaiKhoan> searchTaiKhoan(String query) {
    return taiKhoanDAO.search(query);
}

// Activity
List<TaiKhoan> results = repo.searchTaiKhoan("admin");
```

### Pattern 3: CRUD Operations
```java
// Create
TaiKhoan tk = new TaiKhoan();
repo.insert(tk);

// Read
List<TaiKhoan> list = repo.getAllTaiKhoan();

// Update
tk.setMatKhau("newPassword");
repo.update(tk);

// Delete
repo.delete(tk);
```

### Pattern 4: Generic Export
```java
// Export danh sách HocSinh to Excel
ExcelHelper.exportToExcel(context, "HocSinh", "Sheet1",
    new String[]{"Mã", "Tên", "Lớp"},
    hocSinhList,
    (row, hs) -> {
        row.createCell(0).setCellValue(hs.getMaHS());
        row.createCell(1).setCellValue(hs.getHoTen());
        row.createCell(2).setCellValue(hs.getMaLop());
    }
);
```

---

## ⚠️ COMMON MISTAKES

| ❌ Sai | ✅ Đúng |
|---|---|
| `private AppDatabase db = new AppDatabase()` | `new AppDatabase().getDatabase(context)` |
| `allowMainThreadQueries()` trong production | Dùng `databaseWriteExecutor` |
| Không close Cursor | Dùng `try-with-resources` |
| Quên `@Entity` trên model | Add annotation |
| Query SQL có lỗi cú pháp | Test SQL trước |
| Sync database access trên UI thread | Chạy background |
| Quên implement equals() | Cần thiết cho JOIN |
| NULL value không handle | Dùng `@NonNull` hoặc `COALESCE` |

---

## 📚 LEARNING PATH

```
1️⃣ Basics
   ├─ Java Syntax (loops, conditions, OOP)
   ├─ SQL Basics (SELECT, WHERE, ORDER BY)
   └─ Android Basics (Activities, Fragments)

2️⃣ Intermediate
   ├─ Room Database
   ├─ Repository Pattern
   ├─ Annotations (@Entity, @Query, etc)
   └─ SQL Joins & Aggregation

3️⃣ Advanced
   ├─ Generics & Type Parameters
   ├─ Functional Programming (Lambda, Streams)
   ├─ Concurrency & Threading
   ├─ Dependency Injection (Hilt)
   └─ Architecture Patterns (MVVM, MVI)
```

---

## 🔗 LINKS

- Room Database: https://developer.android.com/training/data-storage/room
- SQL Tutorial: https://www.w3schools.com/sql/
- Java Generics: https://docs.oracle.com/javase/tutorial/java/generics/
- Lambda: https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
- Android Docs: https://developer.android.com/docs

---

## 💡 TIPS

1. **Debug SQL**: Thêm `.allowMainThreadQueries()` để test, sau đó move background
2. **Test Query**: Dùng adb shell + sqlite3 để test SQL queries
3. **Understand JOIN**: Draw diagram trước khi viết query
4. **Generic**: Bắt đầu với `List<T>` trước khi dùng advanced generics
5. **Lambda**: Không phải dùng khắp nơi, dùng khi có functional interface
6. **Migration**: Tăng `@Database(version)` khi thay đổi schema
7. **Performance**: Dùng indices (`@Index`) cho cột thường query

---

**Last updated: 17/04/2026**

