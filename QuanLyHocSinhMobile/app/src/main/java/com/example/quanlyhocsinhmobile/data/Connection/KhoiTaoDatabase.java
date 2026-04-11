package com.example.quanlyhocsinhmobile.data.Connection;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.Locale;

public class KhoiTaoDatabase {

    public static void checkAndSeedData(@NonNull SupportSQLiteDatabase db) {
        if (isTableEmpty(db, "HocSinh")) {
            seedData(db);
        }
    }

    private static boolean isTableEmpty(SupportSQLiteDatabase db, String tableName) {
        try (Cursor cursor = db.query("SELECT COUNT(*) FROM " + tableName)) {
            if (cursor != null && cursor.moveToFirst()) {
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
            // 1. DANH MỤC CƠ BẢN
            db.execSQL("INSERT INTO DoiTuongUuTien (maDT, tenDT, tiLeGiamHocPhi) VALUES ('DT01', 'Hộ nghèo', 0.5), ('DT02', 'Con thương binh', 1.0);");
            db.execSQL("INSERT INTO ToHopMon (maToHop, tenToHop) VALUES ('KHTN', 'Khoa học Tự nhiên'), ('KHXH', 'Khoa học Xã hội'), ('CB', 'Cơ bản');");
            db.execSQL("INSERT INTO MonHoc (maMH, tenMH) VALUES " +
                    "('MH01', 'Toán học'), ('MH02', 'Ngữ văn'), ('MH03', 'Tiếng Anh'), " +
                    "('MH04', 'Vật lý'), ('MH05', 'Hóa học'), ('MH06', 'Sinh học'), " +
                    "('MH07', 'Lịch sử'), ('MH08', 'Địa lý'), ('MH09', 'Giáo dục công dân'), ('MH10', 'Tin học');");
            db.execSQL("INSERT INTO PhongHoc (maPhong, tenPhong, sucChua, loaiPhong, tinhTrang) VALUES " +
                    "('P101', 'Phòng 101', 45, 'Lý thuyết', 'Trống'), ('P102', 'Phòng 102', 45, 'Lý thuyết', 'Trống'), " +
                    "('P201', 'Phòng 201', 45, 'Lý thuyết', 'Trống'), ('LAB1', 'Máy tính 1', 50, 'Thực hành', 'Trống');");

            // 2. NHÂN SỰ
            db.execSQL("INSERT INTO GiaoVien (maGV, hoTen, ngaySinh, sdt) VALUES " +
                    "('GV01', 'Nguyễn Bá Đạt', '1985-05-20', '0901234567'), ('GV02', 'Trần Thu Trang', '1990-11-15', '0912345678');");
            db.execSQL("INSERT INTO Lop (maLop, tenLop, nienKhoa, maGVCN) VALUES " +
                    "('L10A1', '10A1', '2023-2026', 'GV01'), ('L10A2', '10A2', '2023-2026', 'GV02'), ('L11A1', '11A1', '2022-2025', 'GV01');");

            // 100 HỌC SINH
            for (int i = 1; i <= 100; i++) {
                String id = String.format(Locale.US, "HS%03d", i);
                String hoTen = "Học sinh " + i;
                if (i == 1) hoTen = "Nguyễn Thị Mai";
                else if (i == 2) hoTen = "Trần Thị Lan";
                else if (i == 3) hoTen = "Lê Văn Tuấn";
                else if (i == 4) hoTen = "Phạm Văn Minh";
                else if (i == 5) hoTen = "Hoàng Thị Thu";
                
                String lop = (i % 3 == 0) ? "L10A1" : (i % 3 == 1 ? "L10A2" : "L11A1");
                String dt = (i % 10 == 0) ? "'DT02'" : "null";
                
                db.execSQL("INSERT INTO HocSinh (maHS, hoTen, ngaySinh, gioiTinh, diaChi, maLop, maDT) VALUES " +
                        "('" + id + "', '" + hoTen + "', '2007-01-01', 'Nam', 'Địa chỉ " + i + "', '" + lop + "', " + dt + ");");
            }

            // 3. ĐIỂM (Tạo điểm cho tất cả HS và 10 môn học)
            db.execSQL("INSERT INTO Diem (maHS, maMH, hocKy, diem15p, diem1Tiet, diemGiuaKy, diemCuoiKy) " +
                    "SELECT h.maHS, m.maMH, 1, (ABS(RANDOM()) % 101) / 10.0, (ABS(RANDOM()) % 101) / 10.0, " +
                    "(ABS(RANDOM()) % 101) / 10.0, (ABS(RANDOM()) % 101) / 10.0 " +
                    "FROM HocSinh h CROSS JOIN MonHoc m;");
            
            db.execSQL("UPDATE Diem SET diemTongKet = ROUND((diem15p + diem1Tiet * 2 + diemGiuaKy * 2 + diemCuoiKy * 3) / 8.0, 1);");

            // 4. HẠNH KIỂM & HỌC PHÍ
            db.execSQL("INSERT INTO HanhKiem (maHS, hocKy, namHoc, xepLoai, nhanXet) " +
                    "SELECT maHS, 1, '2025-2026', 'Tốt', 'Ngoan.' FROM HocSinh;");
            
            db.execSQL("INSERT INTO HocPhi (maHS, hocKy, namHoc, tongTien, mienGiam, phaiDong, trangThai) " +
                    "SELECT hs.maHS, 1, '2025-2026', 2000000, COALESCE(dt.tiLeGiamHocPhi, 0) * 2000000, " +
                    "2000000 - (COALESCE(dt.tiLeGiamHocPhi, 0) * 2000000), 'Chưa đóng' " +
                    "FROM HocSinh hs LEFT JOIN DoiTuongUuTien dt ON hs.maDT = dt.maDT;");

            // 5. HỆ THỐNG
            db.execSQL("INSERT INTO TaiKhoan (tenDangNhap, matKhau, quyen, maNguoiDung) VALUES ('admin', '123456', 'Admin', 'AD01');");
            db.execSQL("INSERT INTO ThongBao (tieuDe, noiDung, nguoiGui) VALUES ('Chào mừng', 'Chào mừng năm học mới.', 'AD01');");
            db.execSQL("INSERT INTO PhucKhao (maHS, maMH, lyDo, trangThai) VALUES ('HS001', 'MH01', 'Phúc khảo điểm thi.', 'Đang chờ xử lý');");
            
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
