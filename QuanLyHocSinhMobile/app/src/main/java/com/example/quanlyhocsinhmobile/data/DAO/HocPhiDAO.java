package com.example.quanlyhocsinhmobile.data.DAO;

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
    @Query("SELECT * FROM HocPhi")
    List<HocPhi> getAll();

    @Query("SELECT * FROM HocPhi WHERE maHS = :maHS")
    List<HocPhi> getHocPhiByHS(String maHS);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HocPhi hocPhi);

    @Update
    void update(HocPhi hocPhi);

    @Delete
    void delete(HocPhi hocPhi);
    
    @Query("SELECT HocPhi.*, HocSinh.HoTen as tenHS, Lop.TenLop as tenLop " +
           "FROM HocPhi " +
           "JOIN HocSinh ON HocPhi.maHS = HocSinh.MaHS " +
           "JOIN Lop ON HocSinh.MaLop = Lop.MaLop")
    List<HocPhi.Display> getAllDisplay();
}
