package com.example.quanlyhocsinhmobile.data.local.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;

import java.util.List;

@Dao
public interface GiaoVienDAO {
    @Query("SELECT GiaoVien.*, MonHoc.TenMH AS TenMH, ToHopMon.tenToHop AS tenToHop " +
            "FROM GiaoVien " +
            "LEFT JOIN MonHoc ON GiaoVien.MaMH = MonHoc.MaMH " +
            "LEFT JOIN ToHopMon ON GiaoVien.maToHop = ToHopMon.maToHop")
    List<GiaoVien.Display> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GiaoVien giaoVien);

    @Update
    void update(GiaoVien giaoVien);

    @Delete
    void delete(GiaoVien giaoVien);

    @Query("SELECT COUNT(*) FROM GiaoVien  WHERE MaGV = :maGV")
    int checkMaGiaoVien(String maGV);

    // int checkTenGiaoVien(String tenGV);
    @Query("SELECT  COUNT(*) FROM GiaoVien WHERE SDT = :sdt")
    int checkSDT(String sdt);

    // sua
    @Query("SELECT GiaoVien.*, ToHopMon.tenToHop, MonHoc.TenMH " +
            "FROM GiaoVien " +
            "LEFT JOIN ToHopMon ON GiaoVien.maToHop = ToHopMon.maToHop " +
            "LEFT JOIN MonHoc ON GiaoVien.maMH = MonHoc.maMH " +
            "WHERE GiaoVien.hoTen LIKE '%' || :query || '%' " +
            "OR GiaoVien.maGV LIKE '%' || :query || '%' " +
            "OR GiaoVien.sdt LIKE '%' || :query || '%' " +
            "OR GiaoVien.maToHop LIKE '%' || :query || '%' " +
            "OR MonHoc.TenMH LIKE '%' || :query || '%'")

     List<GiaoVien.Display> searchGiaoVien(String query);

     // ✅ Lấy mã GV tiếp theo (auto-generate)
     @Query("SELECT COALESCE(MAX(CAST(SUBSTR(MaGV, 3) AS INTEGER)), 0) FROM GiaoVien WHERE MaGV LIKE 'GV%'")
     int getMaxGVNumber();
}
