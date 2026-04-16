package com.example.quanlyhocsinhmobile.data.local.DAO;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.local.Model.HanhKiem;

import java.util.List;

@Dao
public interface  HanhKiemDAO {

    @Query("SELECT HanhKiem.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
           "FROM HanhKiem " +
           "LEFT JOIN HocSinh ON HanhKiem.maHS = HocSinh.MaHS " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop")
    List<HanhKiem.Display> getAll();

    @Query("SELECT HanhKiem.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
           "FROM HanhKiem " +
           "LEFT JOIN HocSinh ON HanhKiem.maHS = HocSinh.MaHS " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
           "WHERE (:maLop = '' OR Lop.MaLop = :maLop) " +
           "AND (:hocKy = 0 OR HanhKiem.hocKy = :hocKy) " +
           "AND (:namHoc = '' OR HanhKiem.namHoc = :namHoc)")
    List<HanhKiem.Display> filterHanhKiem(@NonNull String maLop, int hocKy, @NonNull String namHoc);

    @Query("SELECT HanhKiem.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
           "FROM HanhKiem " +
           "LEFT JOIN HocSinh ON HanhKiem.maHS = HocSinh.MaHS " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
           "WHERE HocSinh.HoTen LIKE :query OR HanhKiem.maHS LIKE :query")
    List<HanhKiem.Display> searchHanhKiem(String query);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HanhKiem hanhKiem);

    @Update
    void update(HanhKiem hanhKiem);

    @Delete
    void delete(HanhKiem hanhKiem);
}
