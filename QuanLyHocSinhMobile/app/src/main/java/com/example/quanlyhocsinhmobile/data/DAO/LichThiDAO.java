package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.Model.LichThi;

import java.util.List;

@Dao
public interface LichThiDAO {

    @Query("SELECT LichThi.*, MonHoc.TenMH as tenMH, PhongHoc.TenPhong as tenPhong " +
           "FROM LichThi " +
           "LEFT JOIN MonHoc ON LichThi.maMH = MonHoc.MaMH " +
           "LEFT JOIN PhongHoc ON LichThi.maPhong = PhongHoc.MaPhong")
    List<LichThi.Display> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LichThi lichThi);

    @Update
    void update(LichThi lichThi);

    @Delete
    void delete(LichThi lichThi);

    @Query("SELECT LichThi.*, MonHoc.TenMH as tenMH, PhongHoc.TenPhong as tenPhong " +
           "FROM LichThi " +
           "LEFT JOIN MonHoc ON LichThi.maMH = MonHoc.MaMH " +
           "LEFT JOIN PhongHoc ON LichThi.maPhong = PhongHoc.MaPhong " +
           "WHERE LichThi.tenKyThi LIKE :search OR MonHoc.TenMH LIKE :search")
    List<LichThi.Display> searchLichThi(String search);

    @Query("SELECT LichThi.*, MonHoc.TenMH as tenMH, PhongHoc.TenPhong as tenPhong " +
           "FROM LichThi " +
           "LEFT JOIN MonHoc ON LichThi.maMH = MonHoc.MaMH " +
           "LEFT JOIN PhongHoc ON LichThi.maPhong = PhongHoc.MaPhong " +
           "WHERE (:maMH IS NULL OR :maMH = '' OR LichThi.maMH = :maMH) " +
           "AND (:maPhong IS NULL OR :maPhong = '' OR LichThi.maPhong = :maPhong)")
    List<LichThi.Display> filterLichThi(String maMH, String maPhong);
}
