package com.example.quanlyhocsinhmobile.data.local;
import android.database.Cursor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
public class KhoiTaoDatabase {
    private static final String TAG = "KhoiTaoDatabase";
    public static void checkAndSeedData(@NonNull SupportSQLiteDatabase db) {
        try {
            if (isSeedDataMissing(db)) {
                seedData(db);
            }
            seedTaiKhoanIfNeeded(db);
        } catch (Exception e) {
            Log.e(TAG, "Loi khoi tao du lieu", e);
        }
    }
    private static boolean isSeedDataMissing(SupportSQLiteDatabase db) {
        return isTableEmpty(db, "MonHoc")
                || isTableEmpty(db, "PhongHoc")
                || isTableEmpty(db, "GiaoVien")
                || isTableEmpty(db, "Lop")
                || isTableEmpty(db, "HocSinh");
    }
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
    private static void seedData(SupportSQLiteDatabase db) {
        db.beginTransaction();
        try {
            db.execSQL("INSERT INTO DoiTuongUuTien (MaDT, TenDT, TiLeGiamHocPhi) VALUES " +
                    "('DT00', 'Thường', 0.0), ('DT01', 'Hộ nghèo', 0.5), ('DT02', 'Con thương binh', 1.0);");
            db.execSQL("INSERT INTO ToHopMon (MaToHop, TenToHop) VALUES " +
                    "('KHTN', 'Khoa học Tự nhiên (Lý, Hóa, Sinh)'), ('KHXH', 'Khoa học Xã hội (Sử, Địa, GDCD)');");
            db.execSQL("INSERT INTO MonHoc (MaMH, TenMH) VALUES " +
                    "('MH01', 'Toán học'), ('MH02', 'Ngữ văn'), ('MH03', 'Tiếng Anh'), " +
                    "('MH04', 'Vật lý'), ('MH05', 'Hóa học'), ('MH06', 'Sinh học'), " +
                    "('MH07', 'Lịch sử'), ('MH08', 'Địa lý'), ('MH09', 'Giáo dục công dân'), ('MH10', 'Tin học');");
            db.execSQL("INSERT INTO PhongHoc (MaPhong, TenPhong, SucChua, LoaiPhong, TinhTrang) VALUES " +
                    "('P101', 'Phòng 101', 45, 'Lý thuyết', 'Trống'), " +
                    "('P102', 'Phòng 102', 45, 'Lý thuyết', 'Trống'), " +
                    "('P201', 'Phòng 201', 45, 'Lý thuyết', 'Trống'), " +
                    "('LAB1', 'Máy tính 1', 50, 'Thực hành', 'Trống'), " +
                    "('LAB2', 'Thí nghiệm Hóa Sinh', 40, 'Thực hành', 'Trống');");
            db.execSQL("INSERT INTO GiaoVien (MaGV, HoTen, NgaySinh, SDT, MaToHop, MaMH) VALUES " +
                    "('GV01', 'Nguyễn Bá Đạt', '1985-05-20', '0901234567', 'KHTN', 'MH01'), " + 
                    "('GV02', 'Trần Thu Trang', '1990-11-15', '0912345678', 'KHXH', 'MH02'), " + 
                    "('GV03', 'Phạm Minh Tuấn', '1988-02-10', '0987654321', 'KHXH', 'MH03'), " + 
                    "('GV04', 'Lê Thị Mai', '1992-07-25', '0977123456', 'KHTN', 'MH04'), " + 
                    "('GV05', 'Hoàng Văn Nam', '1980-12-30', '0966234567', 'KHTN', 'MH05'), " + 
                    "('GV06', 'Vũ Phương Thảo', '1995-04-12', '0955345678', 'KHTN', 'MH06'), " + 
                    "('GV07', 'Đặng Quốc Bảo', '1987-09-05', '0944456789', 'KHXH', 'MH07'), " + 
                    "('GV08', 'Bùi Tuyết Nhung', '1991-01-20', '0933567890', 'KHXH', 'MH08'), " + 
                    "('GV09', 'Ngô Gia Huy', '1984-06-15', '0922678901', 'KHXH', 'MH09'), " + 
                    "('GV10', 'Đỗ Thùy Linh', '1993-08-28', '0911789012', 'KHTN', 'MH10'), " + 
                    "('GV11', 'Trương Công Định', '1982-03-14', '0900890123', 'KHTN', 'MH01'), " + 
                    "('GV12', 'Phan Thanh Bình', '1989-10-10', '0899901234', 'KHXH', 'MH02');"); 
            db.execSQL("INSERT INTO Lop (MaLop, TenLop, NienKhoa, MaGVCN) VALUES " +
                    "('10A1', '10A1', '2023-2026', 'GV01'), ('10A2', '10A2', '2023-2026', 'GV02'), " +
                    "('10A3', '10A3', '2023-2026', 'GV03'), ('10A4', '10A4', '2023-2026', 'GV04'), " +
                    "('10A5', '10A5', '2023-2026', 'GV05'), ('10A6', '10A6', '2023-2026', 'GV06'), " +
                    "('10A7', '10A7', '2023-2026', 'GV07'), ('11A1', '11A1', '2022-2025', 'GV08'), " +
                    "('11A2', '11A2', '2022-2025', 'GV09'), ('12A1', '12A1', '2021-2024', 'GV10'), " +
                    "('12A2', '12A2', '2021-2024', 'GV11'), ('12A3', '12A3', '2021-2024', 'GV12');");
            db.execSQL("INSERT INTO HocSinh (MaHS, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop, MaDT) VALUES " +
                    "('HS001', 'Nguyễn Thị Mai', '2008-10-01', 'Nữ', 'Hà Nội', '10A1', 'DT00')," +
                    "('HS002', 'Trần Thị Lan', '2008-02-20', 'Nữ', 'Bắc Ninh', '10A1', 'DT01')," +
                    "('HS003', 'Lê Văn Tuấn', '2008-01-28', 'Nam', 'Thái Nguyên', '10A2', 'DT02')," +
                    "('HS004', 'Phạm Văn Minh', '2008-05-03', 'Nam', 'Hải Phòng', '10A2', 'DT00')," +
                    "('HS005', 'Hoàng Thị Thu', '2008-07-11', 'Nữ', 'Nam Định', '10A3', 'DT00')," +
                    "('HS006', 'Vũ Thị Hoa', '2008-01-15', 'Nữ', 'Thái Bình', '10A3', 'DT02')," +
                    "('HS007', 'Võ Văn Dũng', '2008-10-13', 'Nam', 'Hưng Yên', '10A4', 'DT00')," +
                    "('HS008', 'Đặng Văn Thành', '2008-05-24', 'Nam', 'Hải Dương', '10A4', 'DT00')," +
                    "('HS009', 'Bùi Văn Hùng', '2008-09-27', 'Nam', 'Vĩnh Phúc', '10A5', 'DT00')," +
                    "('HS010', 'Đỗ Thị Hằng', '2008-07-08', 'Nữ', 'Hà Nam', '10A5', 'DT02')," +
                    "('HS011', 'Nguyễn Thị Phương', '2008-04-19', 'Nữ', 'Hà Nội', '10A6', 'DT00')," +
                    "('HS012', 'Trần Văn Đạt', '2008-03-21', 'Nam', 'Bắc Ninh', '10A6', 'DT01')," +
                    "('HS013', 'Lê Văn Khoa', '2008-05-26', 'Nam', 'Thái Nguyên', '10A7', 'DT00')," +
                    "('HS014', 'Phạm Văn Hải', '2008-04-01', 'Nam', 'Hải Phòng', '10A7', 'DT00')," +
                    "('HS015', 'Hoàng Văn Nam', '2008-05-22', 'Nam', 'Nam Định', '10A1', 'DT00')," +
                    "('HS016', 'Vũ Văn Phong', '2008-04-06', 'Nam', 'Thái Bình', '10A2', 'DT00')," +
                    "('HS017', 'Võ Thị Ngọc', '2008-11-26', 'Nữ', 'Hưng Yên', '10A3', 'DT00');");
            db.execSQL("INSERT INTO HocSinh (MaHS, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop, MaDT) VALUES " +
                    "('HS018', 'Đặng Văn Sơn', '2007-10-06', 'Nam', 'Hải Dương', '11A1', 'DT02')," +
                    "('HS019', 'Bùi Văn Thắng', '2007-12-03', 'Nam', 'Vĩnh Phúc', '11A1', 'DT00')," +
                    "('HS020', 'Đỗ Văn Long', '2007-12-01', 'Nam', 'Hà Nam', '11A1', 'DT00')," +
                    "('HS021', 'Nguyễn Thị Trâm', '2007-06-16', 'Nữ', 'Hà Nội', '11A1', 'DT02')," +
                    "('HS022', 'Trần Văn Kiên', '2007-03-27', 'Nam', 'Bắc Ninh', '11A2', 'DT00')," +
                    "('HS023', 'Lê Thị Hương', '2007-03-06', 'Nữ', 'Thái Nguyên', '11A2', 'DT02')," +
                    "('HS024', 'Phạm Thị Oanh', '2007-04-04', 'Nữ', 'Hải Phòng', '11A2', 'DT02')," +
                    "('HS025', 'Hoàng Thị Yến', '2007-09-25', 'Nữ', 'Nam Định', '11A2', 'DT02')," +
                    "('HS026', 'Vũ Thị Hà', '2007-03-20', 'Nữ', 'Thái Bình', '11A1', 'DT01')," +
                    "('HS027', 'Võ Văn Lâm', '2007-11-30', 'Nam', 'Hưng Yên', '11A2', 'DT00')," +
                    "('HS028', 'Đặng Văn Tú', '2007-04-07', 'Nam', 'Hải Dương', '11A1', 'DT01')," +
                    "('HS029', 'Bùi Thị Thảo', '2007-06-15', 'Nữ', 'Vĩnh Phúc', '11A2', 'DT00')," +
                    "('HS030', 'Đỗ Văn Tiến', '2007-03-21', 'Nam', 'Hà Nam', '11A1', 'DT00')," +
                    "('HS031', 'Nguyễn Văn Quân', '2007-03-24', 'Nam', 'Hà Nội', '11A2', 'DT00');");
            db.execSQL("INSERT INTO HocSinh (MaHS, HoTen, NgaySinh, GioiTinh, DiaChi, MaLop, MaDT) VALUES " +
                    "('HS032', 'Trần Văn Bình', '2006-02-11', 'Nam', 'Bắc Ninh', '12A1', 'DT00')," +
                    "('HS033', 'Lê Văn Tâm', '2006-07-11', 'Nam', 'Thái Nguyên', '12A1', 'DT00')," +
                    "('HS034', 'Phạm Thị Linh', '2006-07-21', 'Nữ', 'Hải Phòng', '12A1', 'DT00')," +
                    "('HS035', 'Hoàng Văn Tùng', '2006-12-17', 'Nam', 'Nam Định', '12A2', 'DT00')," +
                    "('HS036', 'Vũ Văn Toàn', '2006-12-05', 'Nam', 'Thái Bình', '12A2', 'DT00')," +
                    "('HS037', 'Võ Văn Quyết', '2006-05-17', 'Nam', 'Hưng Yên', '12A2', 'DT00')," +
                    "('HS038', 'Đặng Văn Nghĩa', '2006-08-23', 'Nam', 'Hải Dương', '12A3', 'DT00')," +
                    "('HS039', 'Bùi Văn Trọng', '2006-02-05', 'Nam', 'Vĩnh Phúc', '12A3', 'DT00')," +
                    "('HS040', 'Đỗ Văn Cường', '2006-11-23', 'Nam', 'Hà Nam', '12A3', 'DT00')," +
                    "('HS041', 'Nguyễn Thị Quyên', '2006-04-29', 'Nữ', 'Hà Nội', '12A1', 'DT00')," +
                    "('HS042', 'Trần Văn Hoàng', '2006-06-16', 'Nam', 'Bắc Ninh', '12A2', 'DT00')," +
                    "('HS043', 'Lê Thị Thu', '2006-05-22', 'Nữ', 'Thái Nguyên', '12A3', 'DT00')," +
                    "('HS044', 'Phạm Thị Tuyết', '2006-08-06', 'Nữ', 'Hải Phòng', '12A1', 'DT00')," +
                    "('HS045', 'Hoàng Văn Khang', '2006-11-21', 'Nam', 'Nam Định', '12A2', 'DT00')," +
                    "('HS046', 'Vũ Văn Hưng', '2006-03-08', 'Nam', 'Thái Bình', '12A3', 'DT00')," +
                    "('HS047', 'Võ Văn Huy', '2006-05-29', 'Nam', 'Hưng Yên', '12A1', 'DT00')," +
                    "('HS048', 'Đặng Văn Hiếu', '2006-09-22', 'Nam', 'Hải Dương', '12A2', 'DT02')," +
                    "('HS049', 'Bùi Văn Hào', '2006-12-11', 'Nam', 'Vĩnh Phúc', '12A3', 'DT02')," +
                    "('HS050', 'Đỗ Thị Trang', '2006-06-25', 'Nữ', 'Hà Nam', '12A1', 'DT00');");
            db.execSQL("INSERT INTO Diem (MaHS, MaMH, HocKy, Diem15p, Diem1Tiet, DiemGiuaKy, DiemCuoiKy) " +
                    "SELECT h.MaHS, m.MaMH, 1, " +
                    "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), ROUND((ABS(RANDOM()) % 101) / 10.0, 1), " +
                    "ROUND((ABS(RANDOM()) % 101) / 10.0, 1), ROUND((ABS(RANDOM()) % 101) / 10.0, 1) " +
                    "FROM HocSinh h CROSS JOIN MonHoc m;");
            db.execSQL("UPDATE Diem SET DiemTongKet = ROUND((Diem15p + Diem1Tiet * 2 + DiemGiuaKy * 2 + DiemCuoiKy * 3) / 8.0, 1);");
            db.execSQL("INSERT INTO HanhKiem (MaHS, HocKy, NamHoc, XepLoai, NhanXet) " +
                    "SELECT MaHS, 1, '2025-2026', " +
                    "CASE (ABS(RANDOM()) % 4) WHEN 0 THEN 'Tốt' WHEN 1 THEN 'Khá' WHEN 2 THEN 'Trung bình' ELSE 'Tốt' END, " +
                    "'Tuân thủ nội quy.' FROM HocSinh;");
            db.execSQL("INSERT INTO HocPhi (MaHS, HocKy, NamHoc, TongTien, MienGiam, PhaiDong, TrangThai) " +
                    "SELECT hs.MaHS, 1, '2025-2026', 2000000, " +
                    "COALESCE(dt.TiLeGiamHocPhi, 0) * 2000000, " +
                    "2000000 - (COALESCE(dt.TiLeGiamHocPhi, 0) * 2000000), " +
                    "CASE (ABS(RANDOM()) % 3) WHEN 0 THEN 'Chưa đóng' WHEN 1 THEN 'Đã đóng' ELSE 'Chưa đóng' END " +
                    "FROM HocSinh hs LEFT JOIN DoiTuongUuTien dt ON hs.MaDT = dt.MaDT;");
            db.execSQL("INSERT OR IGNORE INTO TaiKhoan (TenDangNhap, MatKhau, Quyen, MaNguoiDung) VALUES " +
                    "('admin', '123456', 'Admin', 'AD01'), ('gv01', '123456', 'GiaoVien', 'GV01'), ('hs001', '123456', 'HocSinh', 'HS001');");
            db.execSQL("INSERT INTO ThongBao (TieuDe, NoiDung, ngayTao, NguoiGui) VALUES " +
                    "('Lịch nghỉ lễ', 'Nghỉ lễ 30/4 từ ngày 30/04 đến 03/05','2026-03-01', 'AD01');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Giữa Kỳ 1', 'MH02', '2026-10-15', '09:30', '11:00', 'P102');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Giữa Kỳ 1', 'MH03', '2026-10-16', '07:30', '09:00', 'P201');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Giữa Kỳ 1', 'MH04', '2026-10-16', '13:30', '15:00', 'P101');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Cuối Kỳ 1', 'MH01', '2026-12-20', '07:30', '09:30', 'P101');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Cuối Kỳ 1', 'MH05', '2026-12-21', '08:00', '10:00', 'P201');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Cuối Kỳ 1', 'MH06', '2026-12-22', '14:00', '16:00', 'P102');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Khảo Sát', 'MH07', '2026-09-05', '07:30', '08:15', 'P201');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Giữa Kỳ 2', 'MH01', '2026-03-10', '07:30', '09:00', 'P101');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Giữa Kỳ 2', 'MH08', '2026-03-11', '13:30', '15:00', 'P102');");
            db.execSQL("INSERT INTO LichThi (TenKyThi, MaMH, NgayThi, GioBatDau, GioKetThuc, MaPhong) VALUES ('Thi Cuối Kỳ 2', 'MH02', '2026-05-20', '07:30', '09:30', 'P101');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS002', 'MH02', 'Cộng nhầm điểm tổng trắc nghiệm.', '2026-03-02', 'Đã duyệt');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS003', 'MH05', 'Em bị thiếu cột điểm chuyên cần.', '2026-03-05', 'Đang chờ xử lý');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS004', 'MH01', 'Bài thi bị thất lạc khi chấm.', '2026-03-10', 'Từ chối');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS005', 'MH03', 'Sai sót trong khâu nhập điểm vào hệ thống.', '2026-03-12', 'Đã duyệt');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS001', 'MH04', 'Muốn kiểm tra lại bài thi cuối kỳ.', '2026-12-25', 'Đang chờ xử lý');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS008', 'MH02', 'Điểm thi thực hành chưa chính xác.', '2026-03-15', 'Đang chờ xử lý');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS009', 'MH06', 'Máy chấm trắc nghiệm nhận diện sai mã đề.', '2026-03-18', 'Đã duyệt');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS010', 'MH07', 'Điểm thành phần chưa được cập nhật.', '2026-04-01', 'Đang chờ xử lý');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS002', 'MH08', 'Nhầm lẫn tên học sinh cùng lớp.', '2026-04-05', 'Từ chối');");
            db.execSQL("INSERT INTO PhucKhao (MaHS, MaMH, LyDo, NgayGui, TrangThai) VALUES ('HS012', 'MH01', 'Đề nghị xem lại câu 4 phần tự luận.', '2026-04-10', 'Đang chờ xử lý');");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('10A1', 'MH02', 'GV02', 'P101', 2, 3, 5);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('10A1', 'MH03', 'GV03', 'P102', 3, 1, 2);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('10A2', 'MH01', 'GV01', 'P201', 3, 3, 4);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('10A2', 'MH04', 'GV04', 'P201', 4, 1, 3);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('11A1', 'MH05', 'GV05', 'P201', 4, 6, 8);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('11A1', 'MH01', 'GV01', 'P101', 5, 1, 2);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('12A1', 'MH06', 'GV06', 'LAB2', 5, 3, 5);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('12A1', 'MH07', 'GV07', 'P102', 6, 1, 2);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('10A1', 'MH08', 'GV08', 'LAB1', 6, 6, 9);");
            db.execSQL("INSERT INTO ThoiKhoaBieu (MaLop, MaMH, MaGV, MaPhong, Thu, TietBatDau, TietKetThuc) VALUES ('11A2', 'MH02', 'GV02', 'P101', 7, 2, 4);");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Loi seed du lieu mau", e);
        } finally {
            db.endTransaction();
        }
    }
    private static void seedTaiKhoanIfNeeded(SupportSQLiteDatabase db) {
        db.execSQL("INSERT OR IGNORE INTO TaiKhoan (TenDangNhap, MatKhau, Quyen, MaNguoiDung) VALUES " +
                "('admin', '123456', 'Admin', 'AD01'), ('gv01', '123456', 'GiaoVien', 'GV01'), ('hs001', '123456', 'HocSinh', 'HS001');");
    }
}