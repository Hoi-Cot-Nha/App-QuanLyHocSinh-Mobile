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

    // Lấy tất cả học phí (JOIN lấy tên HS + lớp)
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
            "FROM HocPhi " +
            "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop")
    List<HocPhi.Display> getAll();

    // Lấy học phí theo HS + học kỳ + năm học
    @Query("SELECT * FROM HocPhi WHERE maHS = :maHS AND hocKy = :hocKy AND namHoc = :namHoc")
    HocPhi getHocPhi(String maHS, int hocKy, String namHoc);

    // Lấy danh sách học phí theo học sinh
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

    // Tìm kiếm (theo mã HS, tên HS, lớp)
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
            "FROM HocPhi " +
            "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE HocPhi.maHS LIKE :search " +
            "OR HocSinh.HoTen LIKE :search " +
            "OR Lop.TenLop LIKE :search")
    List<HocPhi.Display> searchHocPhi(String search);

    // Lọc (học kỳ + năm học + lớp)
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
            "FROM HocPhi " +
            "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE (:hocKy = 0 OR HocPhi.hocKy = :hocKy) " +
            "AND (:namHoc = '' OR HocPhi.namHoc = :namHoc) " +
            "AND (:maLop = '' OR Lop.MaLop = :maLop)")
    List<HocPhi.Display> filterHocPhi(int hocKy, @NonNull String namHoc, @NonNull String maLop);
}