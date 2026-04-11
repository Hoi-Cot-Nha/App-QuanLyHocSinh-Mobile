package com.example.quanlyhocsinhmobile.data.Connection;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.quanlyhocsinhmobile.data.DAO.*;
import com.example.quanlyhocsinhmobile.data.Model.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {
    ToHopMon.class, PhongHoc.class, MonHoc.class, DoiTuongUuTien.class,
    TaiKhoan.class, GiaoVien.class, Lop.class, HocSinh.class,
    ThoiKhoaBieu.class, Diem.class, HanhKiem.class, LichThi.class,
    HocPhi.class, ThongBao.class, PhucKhao.class
}, version = 7, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // --- 1. Khai báo các DAO ---
    public abstract DiemDAO diemDAO();
    public abstract HocSinhDAO hocSinhDAO();
    public abstract MonHocDAO monHocDAO();

    // --- 2. Cấu hình Singleton & Executor ---
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

    // --- 3. Callback khởi tạo dữ liệu mẫu ---
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // Gọi logic nạp dữ liệu từ file DatabaseInitializer riêng biệt
            databaseWriteExecutor.execute(() -> KhoiTaoDatabase.checkAndSeedData(db));
        }
    };
}
