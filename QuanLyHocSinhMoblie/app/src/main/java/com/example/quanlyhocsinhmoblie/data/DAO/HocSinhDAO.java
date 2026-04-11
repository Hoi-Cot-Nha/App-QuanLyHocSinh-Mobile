package com.example.quanlyhocsinhmoblie.data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlyhocsinhmoblie.data.Model.HocSinh;

import java.util.List;

@Dao
public interface HocSinhDAO {
    @Query("SELECT * FROM hocsinh")
    List<HocSinh> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HocSinh hocSinh);

    @Query("SELECT hoTen FROM hocsinh WHERE maHS = :maHS")
    String getTenHocSinh(String maHS);
}
