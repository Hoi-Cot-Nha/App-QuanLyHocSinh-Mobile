package com.example.quanlyhocsinhmoblie.data.Connection;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.quanlyhocsinhmoblie.data.DAO.*;
import com.example.quanlyhocsinhmoblie.data.Model.*;

@Database(entities = {
    Diem.class, 
    HocSinh.class, 
    MonHoc.class
    // Tạm thời ẩn các entity khác để sửa lỗi MissingType
    /*
    Lop.class, Giaovien.class, 
    HanhKiem.class, LichThi.class, TKB.class, Hocphi.class, ToBoMon.class,
    LopGVCN.class, PhongHoc.class, Phuckhao.class, TaiKhoan.class, 
    Thongbao.class, DoiTuongUuTien.class
    */
}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract DiemDAO diemDAO();
    public abstract HocSinhDAO hocSinhDAO();
    public abstract MonHocDAO monHocDAO();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "quanlyhocsinh_db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration() // Thêm cái này để tự động cập nhật DB khi đổi entity
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
