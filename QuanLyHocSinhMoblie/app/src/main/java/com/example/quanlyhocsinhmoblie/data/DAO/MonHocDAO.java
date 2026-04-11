package com.example.quanlyhocsinhmoblie.data.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.quanlyhocsinhmoblie.data.Model.MonHoc;

import java.util.List;

@Dao
public interface MonHocDAO {
    @Query("SELECT * FROM monhoc")
    List<MonHoc> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MonHoc monHoc);

    @Query("SELECT tenMH FROM monhoc WHERE maMH = :maMH")
    String getTenMonHoc(String maMH);
}
