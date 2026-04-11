package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlyhocsinhmobile.data.Model.MonHoc;

import java.util.List;

@Dao
public interface MonHocDAO {
    @Query("SELECT * FROM monhoc")
    List<MonHoc> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MonHoc monHoc);

    @Query("SELECT TenMH FROM monhoc WHERE MaMH = :maMH")
    String getTenMonHoc(String maMH);
}
