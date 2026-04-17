package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.HocPhi;
import java.util.List;
@Dao
public interface HocPhiDAO {
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
            "FROM HocPhi " +
            "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop")
    List<HocPhi.Display> getAll();
    @Query("SELECT * FROM HocPhi WHERE maHS = :maHS AND hocKy = :hocKy AND namHoc = :namHoc")
    HocPhi getHocPhi(String maHS, int hocKy, String namHoc);
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
            "FROM HocPhi " +
            "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE HocPhi.maHS = :maHS")
    List<HocPhi.Display> getHocPhiByHS(String maHS);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HocPhi hocPhi);
    @Update
    void update(HocPhi hocPhi);
    @Delete
    void delete(HocPhi hocPhi);
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
            "FROM HocPhi " +
            "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE HocPhi.maHS LIKE :search " +
            "OR HocSinh.HoTen LIKE :search " +
            "OR Lop.TenLop LIKE :search")
    List<HocPhi.Display> searchHocPhi(String search);
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
            "FROM HocPhi " +
            "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE (:hocKy = 0 OR HocPhi.hocKy = :hocKy) " +
            "AND (:namHoc = '' OR HocPhi.namHoc = :namHoc) " +
            "AND (:maLop = '' OR Lop.MaLop = :maLop)")
    List<HocPhi.Display> filterHocPhi(int hocKy, @NonNull String namHoc, @NonNull String maLop);
}