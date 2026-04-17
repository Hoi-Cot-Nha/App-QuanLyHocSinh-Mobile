# ✅ CHECKLIST - Kiểm Tra Spinner Implementation

---

## 📋 File Sửa - Kiểm Tra

### Layout (dat_activity_lop.xml)
- [x] ❌ Xóa EditText `et_nien_khoa`
- [x] ❌ Xóa EditText `et_giao_vien`
- [x] ✅ Thêm Spinner `sp_nien_khoa` (id chính xác)
- [x] ✅ Thêm Spinner `sp_giao_vien` (id chính xác)
- [x] ✅ Set background EditBox cho Spinner
- [x] ✅ Thêm TextView label cho mỗi Spinner

### LopDAO (Query)
- [x] ✅ Query `getAllNienKhoa()` - DISTINCT nienKhoa
- [x] ✅ Query `getAllGiaoVienForLop()` - SELECT g.maGV, g.hoTen
- [x] ✅ Class `GiaoVienInfo` với maGV, hoTen
- [x] ✅ toString() return hoTen
- [x] ✅ Constructor GiaoVienInfo

### LopRepository (Method)
- [x] ✅ Method `getAllNienKhoa()` - gọi DAO
- [x] ✅ Method `getAllGiaoVienForLop()` - gọi DAO

### LopViewModel (LiveData)
- [x] ✅ MutableLiveData `nienKhoaList`
- [x] ✅ MutableLiveData `giaoVienList`
- [x] ✅ Getter `getNienKhoaList()`
- [x] ✅ Getter `getGiaoVienList()`
- [x] ✅ Method `loadSpinnerData()` - load từ DB
- [x] ✅ Method thêm default item nếu list rỗng
- [x] ✅ postValue() sau khi load xong
- [x] ✅ Call `loadSpinnerData()` trong onCreate

### LopActivity (Logic)
- [x] ✅ Import Spinner, ArrayAdapter
- [x] ✅ Declare `sp_giao_vien`, `sp_nien_khoa` (private)
- [x] ✅ Declare adapter cho Spinner
- [x] ✅ removeImport `AdapterView` (unused)
- [x] ✅ findViewById() cho Spinner
- [x] ✅ Init ArrayAdapter cho Spinner
- [x] ✅ setAdapter() cho Spinner
- [x] ✅ getSelectedItem() trong btnAdd.onClick
- [x] ✅ Validate startsWith("--") trong ADD
- [x] ✅ getSelectedItem() trong btnSave.onClick
- [x] ✅ Validate startsWith("--") trong UPDATE
- [x] ✅ setSelection() trong setupRecyclerView
- [x] ✅ Observer LiveData dalam observeViewModel
- [x] ✅ Reset Spinner trong clearInputs()
- [x] ✅ Call loadSpinnerData() trong onCreate

---

## 🧪 Test Manual

### Test 1: Load Spinner Data
```
[ ] Mở LopActivity
[ ] Spinner "Niên khóa" có item? (từ DB)
[ ] Spinner "GVCN" có item? (từ DB)
[ ] Default item "-- Chọn..." xuất hiện?
```

### Test 2: Add Lớp Thành Công
```
[ ] Nhập Mã = "10A9"
[ ] Nhập Tên = "Lớp 10A9"
[ ] Chọn Niên khóa từ Spinner
[ ] Chọn GVCN từ Spinner
[ ] Click "THÊM"
[ ] Toast "Thêm lớp thành công"
[ ] Lớp xuất hiện trong danh sách
```

### Test 3: Validation - Không chọn NK
```
[ ] Nhập Mã = "10A10"
[ ] Nhập Tên = "Lớp 10A10"
[ ] Không chọn Niên khóa (default)
[ ] Chọn GVCN
[ ] Click "THÊM"
[ ] Toast "Vui lòng chọn đầy đủ thông tin"
```

### Test 4: Validation - Không chọn GVCN
```
[ ] Nhập Mã = "10A11"
[ ] Nhập Tên = "Lớp 10A11"
[ ] Chọn Niên khóa
[ ] Không chọn GVCN (default)
[ ] Click "THÊM"
[ ] Toast "Vui lòng chọn đầy đủ thông tin"
```

### Test 5: Click Row - Spinner Tự Select
```
[ ] Click chọn lớp từ danh sách
[ ] Spinner "Niên khóa" tự select đúng?
[ ] Spinner "GVCN" tự select đúng?
[ ] Tên GV hiển thị đúng?
```

### Test 6: Update Lớp
```
[ ] Click chọn lớp
[ ] Đổi Spinner Niên khóa
[ ] Đổi Spinner GVCN
[ ] Click "LƯU"
[ ] Toast "Cập nhật lớp thành công"
[ ] Danh sách update đúng?
```

### Test 7: Delete Lớp
```
[ ] Click chọn lớp
[ ] Click "XÓA"
[ ] Toast "Xóa lớp thành công"
[ ] Lớp biến mất khỏi danh sách?
```

### Test 8: Refresh
```
[ ] Nhập Mã, Tên
[ ] Chọn Spinner items
[ ] Click "LÀM MỚI"
[ ] Form clear?
[ ] Spinner reset về default?
```

---

## 🔍 Code Check

### LopDAO - GiaoVienInfo
```java
class GiaoVienInfo {
    public String maGV;
    public String hoTen;
    
    @Override
    public String toString() {
        return hoTen;  // ← MUST return hoTen
    }
}
```
- [ ] toString() return hoTen (không phải maGV)
- [ ] public field maGV, hoTen
- [ ] Constructor đúng

### LopActivity - getSelectedItem
```java
// Niên khóa
String nienKhoa = (String) sp_nien_khoa.getSelectedItem();

// GVCN
GiaoVienInfo gv = (GiaoVienInfo) sp_giao_vien.getSelectedItem();
String maGVCN = gv.maGV;  // ← Extract maGV
```
- [ ] Cast type đúng
- [ ] Lấy maGV từ object, không phải toString()

### LopActivity - Validation
```java
if (nienKhoa == null || nienKhoa.isEmpty() || nienKhoa.startsWith("--") ||
    maGVCN.isEmpty() || selectedGV == null || selectedGV.maGV.isEmpty()) {
    Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
}
```
- [ ] Check null trước khi .isEmpty()
- [ ] Check startsWith("--") để detect default item
- [ ] Check selectedGV != null trước lấy maGV

### LopActivity - setSelection
```java
for (int i = 0; i < giaoVienAdapter.getCount(); i++) {
    LopDAO.GiaoVienInfo item = giaoVienAdapter.getItem(i);
    if (item != null && item.maGV.equals(selectedLop.getMaGVCN())) {
        sp_giao_vien.setSelection(i);
        break;
    }
}
```
- [ ] Check item != null trước .maGV
- [ ] Compare maGV với selectedLop.getMaGVCN()
- [ ] setSelection(index) gọi đúng

---

## 🚨 Common Issues

### Issue 1: Spinner không hiển thị dữ liệu
```
Nguyên nhân: 
  - Database không có dữ liệu
  - Query lỗi
  - Adapter không update

Fix:
  [ ] Kiểm tra bảng Lop có dữ liệu?
  [ ] Kiểm tra bảng GiaoVien có dữ liệu?
  [ ] Xem Logcat: có exception gì?
  [ ] Kiểm tra adapter.notifyDataSetChanged() được gọi?
```

### Issue 2: Spinner hiển thị sai dữ liệu
```
Nguyên nhân:
  - toString() implement sai
  - getSelectedItem() lấy sai

Fix:
  [ ] Kiểm tra GiaoVienInfo.toString() return hoTen?
  [ ] Kiểm tra cast type đúng không? (GiaoVienInfo)
  [ ] Kiểm tra adapter.getItem(i) return object đúng?
```

### Issue 3: Chọn item rồi mở lại không giữ selection
```
Nguyên nhân:
  - setSelection() gọi trước adapter update
  - Spinner không bind properly

Fix:
  [ ] Gọi setSelection() sau adapter.addAll()
  [ ] Gọi setSelection() sau adapter.notifyDataSetChanged()
```

### Issue 4: Lỗi "Vui lòng chọn đầy đủ thông tin" liên tục
```
Nguyên nhân:
  - Validation logic sai
  - getSelectedItem() return null

Fix:
  [ ] Kiểm tra null trước isEmpty()
  [ ] Kiểm tra startsWith("--") logic
  [ ] Debug print getSelectedItem() value
```

---

## 📊 Status

### Trước
- ❌ EditText Niên khóa
- ❌ EditText GVCN
- ❌ Dễ nhập sai
- ❌ Format validation không chặt

### Sau
- ✅ Spinner Niên khóa
- ✅ Spinner GVCN  
- ✅ Không thể nhập sai
- ✅ Data luôn từ DB

---

## 🎯 Final Checklist

- [ ] Rebuild project xong, no errors
- [ ] App chạy lên được
- [ ] Spinner có dữ liệu
- [ ] Thêm lớp được
- [ ] Sửa lớp được
- [ ] Xóa lớp được
- [ ] Validation hoạt động
- [ ] Spinner tự select khi click row
- [ ] Form clear khi refresh

---

**Status:** ✅ Ready for rebuild & test  
**Ngày:** 17/04/2026

