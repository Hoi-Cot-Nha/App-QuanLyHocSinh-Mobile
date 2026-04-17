# 🎯 HƯỚNG DẪN - Chuyển Niên Khóa & GVCN Thành ComboBox

**Ngày:** 17/04/2026  
**Status:** ✅ HOÀN THÀNH

---

## 📝 Các Thay Đổi Được Thực Hiện

### 1. Layout XML (dat_activity_lop.xml)
✅ **Thay đổi:** EditText → Spinner
- ❌ Xóa: `et_nien_khoa` (TextInputEditText)
- ❌ Xóa: `et_giao_vien` (TextInputEditText)
- ✅ Thêm: `sp_nien_khoa` (Spinner)
- ✅ Thêm: `sp_giao_vien` (Spinner)

### 2. LopDAO (Thêm Query)
✅ **Thêm 2 phương thức mới:**
```java
// Lấy danh sách niên khóa unique từ database
List<String> getAllNienKhoa()

// Lấy danh sách giáo viên từ database
List<GiaoVienInfo> getAllGiaoVienForLop()
```

✅ **Thêm class helper:**
```java
class GiaoVienInfo {
    String maGV;
    String hoTen;
}
```

### 3. LopRepository (Thêm Method)
✅ **Thêm 2 phương thức mới:**
```java
List<String> getAllNienKhoa()
List<LopDAO.GiaoVienInfo> getAllGiaoVienForLop()
```

### 4. LopViewModel (Thêm LiveData)
✅ **Thêm:**
```java
MutableLiveData<List<String>> nienKhoaList
MutableLiveData<List<LopDAO.GiaoVienInfo>> giaoVienList

// Method load dữ liệu
void loadSpinnerData()

// Getter
getLiveData<List<String>> getNienKhoaList()
getLiveData<List<LopDAO.GiaoVienInfo>> getGiaoVienList()
```

### 5. LopActivity (Thay đổi logic)
✅ **Thay đổi:**
- ❌ Xóa: `et_giao_vien`, `et_nien_khoa` (EditText)
- ✅ Thêm: `sp_giao_vien`, `sp_nien_khoa` (Spinner)
- ✅ Thêm: Adapter cho Spinner
- ✅ Cập nhật: initViews() - Setup Spinner
- ✅ Cập nhật: setupRecyclerView() - Set Spinner từ selected item
- ✅ Cập nhật: observeViewModel() - Observer Spinner LiveData
- ✅ Cập nhật: clearInputs() - Reset Spinner

---

## 🎯 Luồng Hoạt Động

### Khi Activity Load:
```
1. LopActivity.onCreate()
2. viewModel.loadSpinnerData()
3. Executor load từ DB:
   - getAllNienKhoa() → list niên khóa
   - getAllGiaoVienForLop() → list GV
4. postValue() → update LiveData
5. Observer nhận dữ liệu
6. Adapter populate Spinner
```

### Khi User Click "Thêm":
```
1. Lấy giá trị từ Spinner:
   - nienKhoa = sp_nien_khoa.getSelectedItem()
   - giaoVien = sp_giao_vien.getSelectedItem()
2. Extract data:
   - nienKhoa (String)
   - maGVCN = giaoVien.maGV
3. Validate + Insert
```

### Khi User Click Row:
```
1. setupRecyclerView listener trigger
2. Set Spinner position:
   - Find giáo viên by maGVCN
   - Find niên khóa by value
   - sp_giao_vien.setSelection(index)
   - sp_nien_khoa.setSelection(index)
```

---

## 📊 Database Query

### getAllNienKhoa():
```sql
SELECT DISTINCT nienKhoa FROM Lop ORDER BY nienKhoa DESC
```
**Kết quả:** List<String>
```
["2024-2025", "2023-2024", "2022-2023"]
```

### getAllGiaoVienForLop():
```sql
SELECT DISTINCT g.maGV, g.hoTen 
FROM GiaoVien g 
LEFT JOIN Lop l ON g.maGV = l.maGVCN 
ORDER BY g.hoTen ASC
```
**Kết quả:** List<GiaoVienInfo>
```
GiaoVienInfo(maGV="GV01", hoTen="Nguyễn Bá Đạt")
GiaoVienInfo(maGV="GV02", hoTen="Trần Thị Hoa")
...
```

---

## 🔄 Cách Hoạt Động Spinner

### Adapter:
```java
// Niên khóa (SimpleAdapter for String)
ArrayAdapter<String> nienKhoaAdapter = new ArrayAdapter<>(
    context, 
    android.R.layout.simple_spinner_item, 
    listNienKhoa
);

// Giáo viên (SimpleAdapter for GiaoVienInfo)
ArrayAdapter<GiaoVienInfo> giaoVienAdapter = new ArrayAdapter<>(
    context,
    android.R.layout.simple_spinner_item,
    listGiaoVien
);
```

### Lấy Selected Item:
```java
// Niên khóa
String nienKhoa = (String) sp_nien_khoa.getSelectedItem();

// Giáo viên
GiaoVienInfo gv = (GiaoVienInfo) sp_giao_vien.getSelectedItem();
String maGVCN = gv.maGV;  // Lấy mã
String tenGV = gv.hoTen;   // Lấy tên
```

### Set Selected Item:
```java
// By index
sp_giao_vien.setSelection(index);
sp_nien_khoa.setSelection(index);

// Find index then set
for (int i = 0; i < adapter.getCount(); i++) {
    if (adapter.getItem(i).equals(value)) {
        sp.setSelection(i);
        break;
    }
}
```

---

## ✅ Kiểm Tra

### Trước Fix ❌
```
- Nhập Niên khóa: "2024-2025" (EditText)
- Nhập GVCN: "GV01" hoặc "Nguyễn Bá Đạt" (EditText)
- Dễ nhập sai format
- Không có validation format
```

### Sau Fix ✅
```
- Chọn Niên khóa từ Spinner (dropdown list)
- Chọn GVCN từ Spinner (dropdown list với tên GV)
- Không thể nhập sai format
- Format validated từ database
```

---

## 🚀 Cách Test

### Test 1: Load Spinner Data
```
1. Mở LopActivity
2. ✅ Kiểm tra: Spinner "Niên khóa" có dữ liệu không?
3. ✅ Kiểm tra: Spinner "GVCN" có dữ liệu không?
4. ✅ Kiểm tra: Dữ liệu lấy từ database đúng không?
```

### Test 2: Thêm Lớp
```
1. Nhập Mã = "10A9"
2. Nhập Tên = "Lớp 10A9"
3. Chọn Niên khóa từ Spinner
4. Chọn GVCN từ Spinner
5. Click "THÊM"
6. ✅ Kiểm tra: Lớp được thêm vào database?
7. ✅ Kiểm tra: Dữ liệu hiển thị ngay?
```

### Test 3: Sửa Lớp
```
1. Click chọn lớp từ danh sách
2. ✅ Kiểm tra: Spinner tự động select đúng item?
3. Thay đổi Niên khóa hoặc GVCN
4. Click "LƯU"
5. ✅ Kiểm tra: Dữ liệu được update đúng?
```

### Test 4: Validation
```
1. Nhập Mã = "10A10"
2. Nhập Tên = "Lớp 10A10"
3. KHÔNG chọn Niên khóa (chọn "-- Chọn niên khóa --")
4. Click "THÊM"
5. ✅ Kiểm tra: Hiển thị lỗi "Vui lòng chọn đầy đủ thông tin"?
```

---

## 📝 Lưu Ý Quan Trọng

### 1. Dữ Liệu Spinner Lấy Từ Database
```
- Niên khóa: Từ bảng Lop (column nienKhoa) DISTINCT
- GVCN: Từ bảng GiaoVien (JOIN với Lop)
```

### 2. toString() Của GiaoVienInfo
```java
@Override
public String toString() {
    return hoTen;  // Hiển thị tên, không phải mã
}
```
=> Spinner sẽ hiển thị tên GV, nhưng code lấy mã từ `.maGV`

### 3. Default Item
```java
// Thêm option mặc định
"-- Chọn niên khóa --"
"-- Chọn giáo viên --"

// Validation check
if (value.startsWith("--")) {
    // Invalid
}
```

### 4. Selection After Load Data
```
- Khi adapter update, spinner selection có thể reset
- Cần gọi setSelection(0) để mặc định chọn item đầu tiên
```

---

## 🔧 Troubleshooting

### Lỗi 1: Spinner không có dữ liệu
```
Nguyên nhân: Database không có dữ liệu, hoặc query lỗi
Fix: 
1. Kiểm tra bảng Lop có dữ liệu không?
2. Kiểm tra bảng GiaoVien có dữ liệu không?
3. Xem Logcat có exception gì?
```

### Lỗi 2: Spinner hiển thị sai dữ liệu
```
Nguyên nhân: toString() không được implement đúng, hoặc adapter wrong
Fix:
1. Kiểm tra GiaoVienInfo.toString() return hoTen không?
2. Kiểm tra adapter có add dữ liệu không?
3. Kiểm tra adapter.notifyDataSetChanged() được gọi không?
```

### Lỗi 3: Chọn item rồi mở lại không giữ selection
```
Nguyên nhân: setSelection() gọi sau adapter update
Fix:
1. Gọi setSelection() sau khi adapter update xong
2. Hoặc gọi trong onItemSelected listener của Spinner
```

### Lỗi 4: Lỗi "Vui lòng chọn đầy đủ thông tin" liên tục
```
Nguyên nhân: Validation check quá strict
Fix:
1. Kiểm tra condition validation
2. Loại bỏ duplicate check
3. Check startsWith("--") để skip default item
```

---

## 📚 Code Reference

### GiaoVienInfo toString()
```java
class GiaoVienInfo {
    public String maGV;
    public String hoTen;

    public GiaoVienInfo(String maGV, String hoTen) {
        this.maGV = maGV;
        this.hoTen = hoTen;
    }

    @Override
    public String toString() {
        return hoTen;  // ← Spinner sẽ hiển thị cái này
    }
}
```

### Lấy Selected Item
```java
// Niên khóa (String)
String nienKhoa = (String) sp_nien_khoa.getSelectedItem();

// Giáo viên (Object, phải cast)
LopDAO.GiaoVienInfo selectedGV = (LopDAO.GiaoVienInfo) sp_giao_vien.getSelectedItem();
String maGVCN = selectedGV.maGV;
String tenGV = selectedGV.hoTen;
```

### Observer LiveData
```java
viewModel.getNienKhoaList().observe(this, nienKhoas -> {
    if (nienKhoas != null) {
        nienKhoaAdapter.clear();
        nienKhoaAdapter.addAll(nienKhoas);
        nienKhoaAdapter.notifyDataSetChanged();
    }
});

viewModel.getGiaoVienList().observe(this, giaoViens -> {
    if (giaoViens != null) {
        giaoVienAdapter.clear();
        giaoVienAdapter.addAll(giaoViens);
        giaoVienAdapter.notifyDataSetChanged();
    }
});
```

---

## ✨ Kết Luận

✅ **Hoàn thành chuyển:**
- Niên khóa: EditText → Spinner (lấy từ database)
- GVCN: EditText → Spinner (lấy từ database)

✅ **Lợi ích:**
- Không nhập sai format
- Dữ liệu luôn valid
- UX tốt hơn (dropdown thay vì typing)
- Easier maintenance

✅ **Test:** Rebuild & test theo hướng dẫn trên

---

**Status:** ✅ READY FOR TESTING  
**Ngày:** 17/04/2026  
**Project:** QuanLyHocSinh Mobile

