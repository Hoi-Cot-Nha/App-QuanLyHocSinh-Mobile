package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.Model.PhongHoc;

import java.util.List;

@Dao
public interface PhongHocDAO {
    @Query("SELECT * FROM PhongHoc")
    List<PhongHoc> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PhongHoc phongHoc);

    @Update
    void update(PhongHoc phongHoc);

    @Delete
    void delete(PhongHoc phongHoc);
}
