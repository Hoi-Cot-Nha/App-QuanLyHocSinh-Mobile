package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.Model.HocPhi;

import java.util.List;

@Dao
public interface HocPhiDAO {

    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
           "FROM HocPhi " +
           "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop")
    List<HocPhi.Display> getAll();

    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
           "FROM HocPhi " +
           "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
           "WHERE (:maLop = '' OR Lop.MaLop = :maLop) " +
           "AND (:hocKy = 0 OR HocPhi.hocKy = :hocKy) " +
           "AND (:namHoc = '' OR HocPhi.namHoc = :namHoc) " +
           "AND (:trangThai = '' OR HocPhi.trangThai = :trangThai)")
    List<HocPhi.Display> filterHocPhi(@NonNull String maLop, int hocKy, @NonNull String namHoc, @NonNull String trangThai);

    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
           "FROM HocPhi " +
           "LEFT JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
           "WHERE HocSinh.HoTen LIKE :query OR HocPhi.maHS LIKE :query")
    List<HocPhi.Display> searchHocPhi(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HocPhi hocPhi);

    @Update
    void update(HocPhi hocPhi);

    @Delete
    void delete(HocPhi hocPhi);
}
