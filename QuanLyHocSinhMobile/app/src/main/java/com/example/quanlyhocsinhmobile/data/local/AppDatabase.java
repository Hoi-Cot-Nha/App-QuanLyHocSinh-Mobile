package com.example.quanlyhocsinhmobile.data.local;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quanlyhocsinhmobile.data.local.DAO.*;
import com.example.quanlyhocsinhmobile.data.local.Model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
    ToHopMon.class, PhongHoc.class, MonHoc.class, DoiTuongUuTien.class,
    TaiKhoan.class, GiaoVien.class, Lop.class, HocSinh.class,
    ThoiKhoaBieu.class, Diem.class, HanhKiem.class, LichThi.class,
    HocPhi.class, ThongBao.class, PhucKhao.class
}, version = 18, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // --- 1. Khai báo các DAO ---
    public abstract DiemDAO diemDAO();
    public abstract HocSinhDAO hocSinhDAO();
    public abstract MonHocDAO monHocDAO();
    public abstract LichThiDAO lichThiDAO();
    public abstract PhongHocDAO phongHocDAO();
    public abstract LopDAO lopDAO();
    public abstract HanhKiemDAO hanhKiemDAO();
    public abstract HocPhiDAO hocPhiDAO();
    public abstract TKBDAO tkbDAO();
    public abstract GiaoVienDAO giaoVienDAO();

    public abstract ToBoMonDAO toBoMonDAO();
    public abstract ThongBaoDAO thongBaoDAO();
    public abstract PhucKhaoDAO phucKhaoDAO();
    public abstract DoiTuongDAO doiTuongDAO();
    public abstract TaiKhoanDAO taiKhoanDAO();

    // Singleton & Executor
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

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            databaseWriteExecutor.execute(() -> KhoiTaoDatabase.checkAndSeedData(db));
        }
    };
}
