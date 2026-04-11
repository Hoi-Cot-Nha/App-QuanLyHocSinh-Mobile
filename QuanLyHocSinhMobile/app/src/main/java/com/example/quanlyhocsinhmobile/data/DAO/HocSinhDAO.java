package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlyhocsinhmobile.data.Model.HocSinh;

import java.util.List;

@Dao
public interface HocSinhDAO {
    @Query("SELECT * FROM HocSinh")
    List<HocSinh> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HocSinh hocSinh);

    @Query("SELECT HoTen FROM HocSinh WHERE MaHS = :maHS")
    String getTenHocSinh(String maHS);
}
