# 📚 HƯỚNG DẪN CÁC CỰ PHÁP KHÓ HIỂU TRONG PROJECT QUẢN LÝ HỌC SINH

---

## 📋 MỤC LỤC
1. [Kotlin Gradle DSL](#1-kotlin-gradle-dsl)
2. [Java Annotations (Room Database)](#2-java-annotations-room-database)
3. [Repository Pattern & Dependency Injection](#3-repository-pattern--dependency-injection)
4. [Generic Programming](#4-generic-programming)
5. [SQL Advanced & Database Callbacks](#5-sql-advanced--database-callbacks)
6. [Functional Programming](#6-functional-programming)
7. [Inner Classes & Static Nested Classes](#7-inner-classes--static-nested-classes)
8. [Multi-line Strings & String Concatenation](#8-multi-line-strings--string-concatenation)

---

## 1. KOTLIN GRADLE DSL
**File: `app/build.gradle.kts`**

### 1.1 Cú pháp `alias(libs.plugins.xxx)`
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}
```

**Giải thích:**
- **Version Catalog**: Thay vì viết version cứng, Gradle dùng một tệp `gradle/libs.versions.toml` để quản lý.
- **`alias(libs.plugins.xxx)`**: Đây là cách viết mới trong Gradle 8.0+, thay thế cho cách cũ:
  ```kotlin
  // Cách cũ
  plugins {
      id "com.android.application"
      id "kotlin-android"
  }
  ```
- **Lợi ích**: Dễ cập nhật version, không lặp code, IDE autocomplete tốt hơn.

### 1.2 Cú pháp gán giá trị với `=`
```kotlin
namespace = "com.example.quanlyhocsinhmobile"
compileSdk = 35
applicationId = "com.example.quanlyhocsinhmobile"
```

**Giải thích:**
- Đây là cú pháp DSL (Domain Specific Language) của Gradle Kotlin.
- `namespace =` thay thế `packageName` (deprecated).
- SDK level càng cao, API càng mới nhưng hỗ trợ thiết bị cũ hơn.

### 1.3 Cú pháp `platform()` và `implementation()`
```kotlin
implementation(platform(libs.compose.bom))
implementation(libs.ui)
```

**Giải thích:**
- **`platform()`**: BOM (Bill of Materials) là một tệp đặc biệt chứa danh sách các dependency tương thích.
- Thay vì ghi version riêng cho mỗi library Compose, BOM tự động quản lý.
- **`implementation()`**: Thêm dependency vào compile + runtime, nhưng không export cho các module khác.

### 1.4 Cú pháp `ksp()` thay vì `kapt()`
```kotlin
ksp(libs.room.compiler)
```

**Giải thích:**
- **KSP** (Kotlin Symbol Processing) = phiên bản mới, nhanh hơn kapt.
- **KAPT** (Kotlin Annotation Processing Tool) = phiên bản cũ, chậm hơn.
- KSP dùng cho Room, Hilt, Moshi... để generate code tự động.

---

## 2. JAVA ANNOTATIONS (ROOM DATABASE)
**File: `TaiKhoanDAO_Impl.java` (Generated)**

### 2.1 `@Database` Annotation
```java
@Database(entities = {
    ToHopMon.class, PhongHoc.class, MonHoc.class, DoiTuongUuTien.class,
    TaiKhoan.class, GiaoVien.class, Lop.class, HocSinh.class,
    ThoiKhoaBieu.class, Diem.class, HanhKiem.class, LichThi.class,
    HocPhi.class, ThongBao.class, PhucKhao.class
}, version = 18, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
}
```

**Giải thích:**
- **`@Database`**: Khai báo class này là database chính.
- **`entities = {...}`**: Danh sách tất cả các bảng trong database.
- **`version = 18`**: Phiên bản schema. Khi thay đổi structure bảng → tăng version này.
- **`exportSchema = false`**: Không xuất schema.json vào output (dùng `true` nếu muốn version control).

### 2.2 `@Entity` Annotation
```java
@Entity(tableName = "Diem",
        primaryKeys = {"maHS", "maMH", "hocKy"},
        foreignKeys = {
            @ForeignKey(entity = HocSinh.class, 
                       parentColumns = "MaHS", 
                       childColumns = "maHS", 
                       onDelete = ForeignKey.CASCADE, 
                       onUpdate = ForeignKey.CASCADE),
            @ForeignKey(entity = MonHoc.class, 
                       parentColumns = "MaMH", 
                       childColumns = "maMH", 
                       onDelete = ForeignKey.CASCADE, 
                       onUpdate = ForeignKey.CASCADE)
        },
        indices = {@Index("maMH")})
public class Diem {
}
```

**Giải thích:**
- **`@Entity`**: Đánh dấu class này tương ứng với bảng SQL.
- **`primaryKeys = {"maHS", "maMH", "hocKy"}`**: Composite Primary Key (3 cột làm khóa chính).
- **`@ForeignKey`**: Ràng buộc khóa ngoại.
  - `entity = HocSinh.class`: Tham chiếu tới bảng HocSinh.
  - `parentColumns = "MaHS"`: Cột trong bảng HocSinh.
  - `childColumns = "maHS"`: Cột trong bảng Diem.
  - `onDelete = ForeignKey.CASCADE`: Xóa trong HocSinh → xóa liên tục trong Diem.
  - `onUpdate = ForeignKey.CASCADE`: Cập nhật trong HocSinh → cập nhật liên tục trong Diem.
- **`indices = {@Index("maMH")}`**: Tạo index cho cột maMH (tìm kiếm nhanh).

### 2.3 `@NonNull` Annotation
```java
@NonNull
private String maHS;

@NonNull
public String getMaHS() { return maHS; }
public void setMaHS(@NonNull String maHS) { this.maHS = maHS; }
```

**Giải thích:**
- **`@NonNull`**: Khai báo biến này không được `null`.
- Nếu là **Primitive Type** (int, double): Tự động NOT NULL.
- Nếu là **Object** (String, Integer): Cần khai báo @NonNull rõ ràng.
- **IDE sẽ cảnh báo** nếu bạn gán null cho biến có @NonNull.

### 2.4 `@Ignore` Annotation
```java
@Ignore
public Diem(String maHS, String maMH, int hocKy, Double diem15p, 
            Double diem1Tiet, Double diemGiuaKy, Double diemCuoiKy) {
    // Constructor custom
}
```

**Giải thích:**
- **`@Ignore`**: Bỏ qua constructor này khi Room generate code.
- **Room mặc định**: Dùng constructor không tham số.
- **Khi cần**: Custom constructor, dùng `@Ignore` để Room không ảnh hưởng.

### 2.5 `@Embedded` Annotation
```java
public static class Display {
    @Embedded
    private Diem diem;
    private String tenHS;
}
```

**Giải thích:**
- **`@Embedded`**: Nhúng tất cả các cột của Diem vào Display.
- **Ví dụ**: Query trả về Display sẽ tự động mapping cột SQL vào Diem object.
- **Dùng để**: JOIN nhiều bảng mà không cần thêm entity.

---

## 3. REPOSITORY PATTERN & DEPENDENCY INJECTION
**File: `TaiKhoanRepository.java`**

### 3.1 Repository Pattern là gì?
```java
public class TaiKhoanRepository {
    private TaiKhoanDAO taiKhoanDAO;

    public TaiKhoanRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taiKhoanDAO = db.taiKhoanDAO();
    }

    // Lấy toàn bộ danh sách tài khoản
    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanDAO.getAll();
    }

    // Thêm tài khoản mới
    public void insert(TaiKhoan taiKhoan) {
        taiKhoanDAO.insert(taiKhoan);
    }
}
```

**Giải thích:**
- **Repository**: Lớp trung gian giữa UI và Database.
- **Lợi ích**:
  - **Decoupling**: UI không biết DB như thế nào.
  - **Tái sử dụng**: Nhiều Activity/Fragment dùng chung Repository.
  - **Testing dễ**: Mock Repository thay vì DB thật.

### 3.2 Dependency Injection (DI)
```java
public TaiKhoanRepository(Application application) {
    AppDatabase db = AppDatabase.getDatabase(application);
    taiKhoanDAO = db.taiKhoanDAO();
}
```

**Giải thích:**
- **DI**: Truyền dependency qua constructor thay vì tạo inside object.
- **Cách này**:
  ```java
  // ❌ Không tốt - Tight coupling
  public class BadRepository {
      private AppDatabase db = new AppDatabase(); // Error!
  }

  // ✅ Tốt - Loose coupling (Dependency Injection)
  public class GoodRepository {
      private AppDatabase db;
      public GoodRepository(AppDatabase db) {
          this.db = db;
      }
  }
  ```

---

## 4. GENERIC PROGRAMMING
**File: `ExcelHelper.java`**

### 4.1 Generic Method với `<T>`
```java
public static <T> void exportToExcel(Context context, String fileNamePrefix, String sheetName, 
                                     String[] headers, List<T> data, BiConsumer<Row, T> rowMapper) {
```

**Giải thích:**
- **`<T>`**: Type parameter - đại diện cho BẤT KỲ loại dữ liệu nào.
- **Dùng được cho**:
  - `List<String>` → T = String
  - `List<HocSinh>` → T = HocSinh
  - `List<Diem>` → T = Diem
  - Etc.
- **Lợi ích**: 1 method dùng được cho nhiều loại dữ liệu.

### 4.2 `BiConsumer<Row, T>`
```java
BiConsumer<Row, T> rowMapper
```

**Giải thích:**
- **`BiConsumer`**: Functional interface nhận 2 tham số, không return.
- **Cú pháp**:
  ```
  BiConsumer<Type1, Type2>
  ```
- **Ví dụ sử dụng**:
  ```java
  exportToExcel(context, "HocSinh", "Học sinh", 
                new String[]{"Mã", "Tên", "Lớp"},
                hocSinhList,
                (row, hs) -> {  // Lambda expression
                    row.createCell(0).setCellValue(hs.getMaHS());
                    row.createCell(1).setCellValue(hs.getHoTen());
                    row.createCell(2).setCellValue(hs.getMaLop());
                });
  ```

---

## 5. SQL ADVANCED & DATABASE CALLBACKS
**File: `KhoiTaoDatabase.java`**

### 5.1 `try-with-resources`
```java
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
```

**Giải thích:**
- **`try (...)`**: Automatically close resource khi block kết thúc.
- **Before** (Java 6):
  ```java
  Cursor cursor = null;
  try {
      cursor = db.query("...");
      // ...
  } finally {
      if (cursor != null) cursor.close(); // Phải close tay
  }
  ```
- **Now** (Java 7+):
  ```java
  try (Cursor cursor = db.query("...")) {
      // cursor automatically closed
  }
  ```

### 5.2 `||` (OR) trong SQL Query
```java
private static boolean isSeedDataMissing(SupportSQLiteDatabase db) {
    return isTableEmpty(db, "MonHoc")
            || isTableEmpty(db, "PhongHoc")
            || isTableEmpty(db, "GiaoVien")
            || isTableEmpty(db, "Lop")
            || isTableEmpty(db, "HocSinh");
}
```

**Giải thích:**
- **`||`**: OR logic - nếu 1 trong các điều kiện TRUE → return TRUE.
- **Meaning**: "Nếu thiếu bất kỳ bảng nào → seed dữ liệu".

### 5.3 `CROSS JOIN` trong SQL
```java
db.execSQL("INSERT INTO Diem (MaHS, MaMH, HocKy, Diem15p, Diem1Tiet, DiemGiuaKy, DiemCuoiKy) " +
        "SELECT h.MaHS, m.MaMH, 1, " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), ROUND((ABS(RANDOM()) % 101) / 10.0, 1), " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), ROUND((ABS(RANDOM()) % 101) / 10.0, 1) " +
        "FROM HocSinh h CROSS JOIN MonHoc m;");
```

**Giải thích:**
- **`CROSS JOIN`**: Kết hợp mỗi hàng của bảng này với TẤT CẢ hàng của bảng kia.
- **Ví dụ**:
  - HocSinh: 50 học sinh
  - MonHoc: 10 môn học
  - CROSS JOIN: 50 × 10 = 500 hàng
- **Meaning**: "Mỗi học sinh có điểm cho tất cả 10 môn học".

### 5.4 `CASE...WHEN...THEN...ELSE` trong SQL
```java
db.execSQL("INSERT INTO HanhKiem (MaHS, HocKy, NamHoc, XepLoai, NhanXet) " +
        "SELECT MaHS, 1, '2025-2026', " +
        "CASE (ABS(RANDOM()) % 4) " +
        "  WHEN 0 THEN 'Tốt' " +
        "  WHEN 1 THEN 'Khá' " +
        "  WHEN 2 THEN 'Trung bình' " +
        "  ELSE 'Tốt' " +
        "END, " +
        "'Tuân thủ nội quy.' FROM HocSinh;");
```

**Giải thích:**
- **`CASE...WHEN...ELSE`**: Như `if-else` trong SQL.
- **Ví dụ trên**: Random giá trị 0-3 → gán rating xếp loại.
- **Tương đương Java**:
  ```java
  String xepLoai;
  int random = new Random().nextInt(4);
  switch (random) {
      case 0: xepLoai = "Tốt"; break;
      case 1: xepLoai = "Khá"; break;
      case 2: xepLoai = "Trung bình"; break;
      default: xepLoai = "Tốt";
  }
  ```

### 5.5 `LEFT JOIN` với `COALESCE()`
```java
db.execSQL("INSERT INTO HocPhi (MaHS, HocKy, NamHoc, TongTien, MienGiam, PhaiDong, TrangThai) " +
        "SELECT hs.MaHS, 1, '2025-2026', 2000000, " +
        "COALESCE(dt.TiLeGiamHocPhi, 0) * 2000000, " +
        "2000000 - (COALESCE(dt.TiLeGiamHocPhi, 0) * 2000000), " +
        "CASE (ABS(RANDOM()) % 3) WHEN 0 THEN 'Chưa đóng' WHEN 1 THEN 'Đã đóng' ELSE 'Chưa đóng' END " +
        "FROM HocSinh hs LEFT JOIN DoiTuongUuTien dt ON hs.MaDT = dt.MaDT;");
```

**Giải thích:**
- **`LEFT JOIN`**: Lấy TẤT CẢ hàng từ bảng trái (HocSinh), và matching hàng từ bảng phải.
- **`COALESCE(dt.TiLeGiamHocPhi, 0)`**: Nếu null → dùng 0.
  - Nếu join không tìm thấy matching → NULL
  - COALESCE prevents crash, dùng default value
- **Meaning**: "Tất cả học sinh có học phí, dù có hoặc không có đối tượng ưu tiên".

### 5.6 `RoomDatabase.Callback`
```java
private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
    @Override
    public void onOpen(@NonNull SupportSQLiteDatabase db) {
        super.onOpen(db);
        databaseWriteExecutor.execute(() -> KhoiTaoDatabase.checkAndSeedData(db));
    }
};
```

**Giải thích:**
- **`Callback`**: Được gọi khi database events xảy ra.
- **`onOpen()`**: Gọi lần ĐẦUTIÊN khi database được mở.
- **`databaseWriteExecutor.execute()`**: Chạy trên background thread (không block UI).
- **Lambda `() -> ...`**: Functional expression, thay thế Anonymous Class.

---

## 6. FUNCTIONAL PROGRAMMING
**File: Various**

### 6.1 Lambda Expressions
```java
// Cách cũ - Anonymous Class
BiConsumer<Row, HocSinh> rowMapper = new BiConsumer<Row, HocSinh>() {
    @Override
    public void accept(Row row, HocSinh hs) {
        row.createCell(0).setCellValue(hs.getMaHS());
    }
};

// Cách mới - Lambda
BiConsumer<Row, HocSinh> rowMapper = (row, hs) -> {
    row.createCell(0).setCellValue(hs.getMaHS());
};

// Nếu chỉ 1 dòng
BiConsumer<Row, HocSinh> rowMapper = (row, hs) -> row.createCell(0).setCellValue(hs.getMaHS());
```

**Giải thích:**
- **Lambda**: Cú pháp ngắn gọn cho anonymous function.
- **Cú pháp**: `(tham_so_1, tham_so_2) -> { code }`
- **Nếu 1 line**: Có thể bỏ `{}` và `;`

### 6.2 Method Reference
```java
// Lambda
list.forEach(item -> System.out.println(item));

// Method Reference
list.forEach(System.out::println);
```

**Giải thích:**
- **`::`**: Method reference operator.
- **`System.out::println`**: Tham chiếu tới method println.
- **Tương đương**: Gọi `println(item)` cho mỗi item.

---

## 7. INNER CLASSES & STATIC NESTED CLASSES
**File: `Diem.java`**

### 7.1 Static Nested Class
```java
public class Diem {
    // ...
    
    public static class Display {
        @Embedded
        private Diem diem;
        private String tenHS;
        private String tenMH;
        private String tenLop;
        
        public Display() {}
        // Getters/Setters
    }
}
```

**Giải thích:**
- **Static Nested Class**: Class bên trong class khác, nhưng là static.
- **Đặc điểm**:
  - Không có access tới instance variables của parent class.
  - Tạo object: `new Diem.Display()` (không cần Diem instance).
- **Dùng để**: Nhóm các class liên quan, tránh pollution namespace.

### 7.2 Inner Class (Non-static)
```java
// Ví dụ (không có trong project, nhưng khác biệt)
public class Outer {
    private int x = 10;
    
    public class Inner {
        public void test() {
            System.out.println(x); // Có thể access x
        }
    }
}

// Tạo object
Outer outer = new Outer();
Outer.Inner inner = outer.new Inner(); // Cần Outer instance
```

**Khác biệt**:
| | Static Nested | Inner (Non-static) |
|---|---|---|
| Tạo object | `new Outer.Inner()` | `outer.new Inner()` |
| Access parent | ❌ Không | ✅ Có (instance fields) |
| Memory | Nhẹ | Nặng (giữ reference) |

---

## 8. MULTI-LINE STRINGS & STRING CONCATENATION
**File: `KhoiTaoDatabase.java`**

### 8.1 String Concatenation trong SQL
```java
db.execSQL("INSERT INTO Diem (MaHS, MaMH, HocKy, Diem15p, Diem1Tiet, DiemGiuaKy, DiemCuoiKy) " +
        "SELECT h.MaHS, m.MaMH, 1, " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), ROUND((ABS(RANDOM()) % 101) / 10.0, 1), " +
        "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), ROUND((ABS(RANDOM()) % 101) / 10.0, 1) " +
        "FROM HocSinh h CROSS JOIN MonHoc m;");
```

**Giải thích:**
- **`+` operator**: Nối các string lại với nhau.
- **Tại sao split dòng**: 
  - Dễ đọc
  - Giữ code line dưới 100 ký tự (coding standard)
- **Chuỗi liền**: Các string adjacent tự động nối (Java compiler optimize).

### 8.2 String Formatting
```java
String fileName = fileNamePrefix + "_" + System.currentTimeMillis() + ".xlsx";
```

**Giải thích:**
- **`System.currentTimeMillis()`**: Thời gian hiện tại (millisecond).
- **Ví dụ output**: `DanhSachHS_1713350000000.xlsx`
- **Dùng để**: Tạo filename unique, tránh ghi đè file cũ.

### 8.3 Placeholder & String.format()
```java
// Không có trong project, nhưng hay dùng
String message = String.format("Học sinh %s thuộc lớp %s", "Nguyễn A", "10A1");
// Output: "Học sinh Nguyễn A thuộc lớp 10A1"

// Hoặc dùng + concatenation
String message = "Học sinh " + name + " thuộc lớp " + className;
```

---

## 📌 TÓMLƯỢC CÁC CỰ PHÁP CHÍNH

| Cú pháp | File | Ý nghĩa |
|---|---|---|
| `@Database` | AppDatabase.java | Khai báo database |
| `@Entity` | Diem.java | Khai báo bảng |
| `@ForeignKey` | Diem.java | Ràng buộc khóa ngoại |
| `Repository` | TaiKhoanRepository.java | Lớp trung gian UI-DB |
| `<T>` | ExcelHelper.java | Generic type |
| `CROSS JOIN` | KhoiTaoDatabase.java | Kết hợp hai bảng |
| `CASE...WHEN` | KhoiTaoDatabase.java | Conditional logic trong SQL |
| `LEFT JOIN` | KhoiTaoDatabase.java | Join với NULL values |
| `Lambda` | ExcelHelper.java | Functional expression |
| `try-with-resources` | KhoiTaoDatabase.java | Auto close resource |

---

## 🔗 TÀI LIỆU THAM KHẢO

- **Room Database**: https://developer.android.com/training/data-storage/room
- **Kotlin DSL**: https://docs.gradle.org/current/userguide/kotlin_dsl.html
- **Java Generics**: https://docs.oracle.com/javase/tutorial/java/generics/
- **SQL Joins**: https://www.w3schools.com/sql/sql_join.asp
- **Lambda Expressions**: https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html

---

## ❓ CÂU HỎI THƯỜNG GẶP

**Q1: Tại sao dùng Repository thay vì call DAO trực tiếp?**
> A: Repository là abstraction layer, giúp test dễ hơn và code maintainable. Nếu sau này đổi data source (SQLite → REST API), chỉ cần sửa Repository.

**Q2: `@Ignore` constructor có tác dụng gì?**
> A: Room auto-generate code mapping SQL → Object. Nếu có constructor custom, dùng `@Ignore` để Room không ảnh hưởng.

**Q3: Tại sao dùng `CROSS JOIN` thay vì loop in Java?**
> A: Database optimize hơn, 1 query SQL nhanh hơn 50 loop × 10 insert.

**Q4: Lambda vs Anonymous Class khác gì?**
> A: Lambda ngắn gọn, có thể compile thành bytecode tối ưu. Anonymous Class là cách cũ, phức tạp hơn.

**Q5: `try-with-resources` tự động close gì?**
> A: Bất kỳ object nào implement `AutoCloseable` interface (như Cursor, FileOutputStream).

---

## 🎯 KIẾN NGHỊ TIẾP THEO

1. **Học thêm**: Design Patterns (Observer, Factory, Singleton)
2. **Tối ưu**: Thêm pagination cho danh sách (limit/offset)
3. **Testing**: Viết unit test cho Repository
4. **Async**: Dùng LiveData/Flow thay vì synchronous query

---

**Tài liệu này cập nhật: 17/04/2026**

