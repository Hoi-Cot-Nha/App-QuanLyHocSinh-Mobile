package com.example.quanlyhocsinhmobile.data.local.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;

import java.util.List;

@Dao
public interface HocSinhDAO {

    // Lấy tất cả học sinh
    @Query("SELECT * FROM HocSinh")
    List<HocSinh> getAll();

    // Thêm học sinh
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HocSinh hocSinh);

    // Cập nhật học sinh
    @Update
    void update(HocSinh hocSinh);

    // Xóa học sinh
    @Delete
    void delete(HocSinh hocSinh);

    // Kiểm tra trùng mã học sinh
    @Query("SELECT COUNT(*) FROM HocSinh WHERE MaHS = :maHS")
    int checkMaHocSinh(String maHS);

    // Kiểm tra trùng tên học sinh
    @Query("SELECT COUNT(*) FROM HocSinh WHERE HoTen = :hoTen")
    int checkTenHocSinh(String hoTen);

    // Lấy tên theo mã
    @Query("SELECT HoTen FROM HocSinh WHERE MaHS = :maHS")
    String getTenHocSinh(String maHS);

    // Tìm kiếm (theo mã + tên)
    @Query("SELECT * FROM HocSinh WHERE MaHS LIKE '%' || :query || '%' OR HoTen LIKE '%' || :query || '%'")
    List<HocSinh> search(String query);

    @Query("SELECT * FROM HocSinh WHERE MaHS = :maHS LIMIT 1")
    HocSinh getHocSinhByMa(String maHS);
}
