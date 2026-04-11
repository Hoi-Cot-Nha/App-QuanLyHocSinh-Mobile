package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.Model.Diem;
import com.example.quanlyhocsinhmobile.data.Model.DiemDisplay;

import java.util.List;

@Dao
public interface DiemDAO {

    @Query("SELECT Diem.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
           "FROM Diem " +
           "LEFT JOIN HocSinh ON Diem.maHS = HocSinh.MaHS " +
           "LEFT JOIN MonHoc ON Diem.maMH = MonHoc.MaMH " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop")
    List<DiemDisplay> getAll();

    @Query("SELECT * FROM Diem WHERE maHS = :maHS AND maMH = :maMH AND hocKy = :hocKy")
    Diem getDiem(String maHS, String maMH, int hocKy);

    @Query("SELECT Diem.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
           "FROM Diem " +
           "LEFT JOIN HocSinh ON Diem.maHS = HocSinh.MaHS " +
           "LEFT JOIN MonHoc ON Diem.maMH = MonHoc.MaMH " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
           "WHERE Diem.maHS = :maHS")
    List<DiemDisplay> getDiemByHS(String maHS);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Diem diem);

    @Update
    void update(Diem diem);

    @Delete
    void delete(Diem diem);

    @Query("SELECT Diem.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
           "FROM Diem " +
           "LEFT JOIN HocSinh ON Diem.maHS = HocSinh.MaHS " +
           "LEFT JOIN MonHoc ON Diem.maMH = MonHoc.MaMH " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
           "WHERE Diem.maHS LIKE :search OR MonHoc.TenMH LIKE :search OR Lop.TenLop LIKE :search")
    List<DiemDisplay> searchDiem(String search);

    @Query("SELECT Diem.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
           "FROM Diem " +
           "LEFT JOIN HocSinh ON Diem.maHS = HocSinh.MaHS " +
           "LEFT JOIN MonHoc ON Diem.maMH = MonHoc.MaMH " +
           "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
           "WHERE (:maMH IS NULL OR Diem.maMH = :maMH) AND (:hocKy = 0 OR Diem.hocKy = :hocKy)")
    List<DiemDisplay> filterDiem(String maMH, int hocKy);
}
