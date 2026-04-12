package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.Model.ThongBao;

import java.util.List;

@Dao
public interface ThongBaoDAO {
    @Query("SELECT * FROM ThongBao ORDER BY maTB DESC")
    List<ThongBao> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ThongBao thongBao);

    @Update
    void update(ThongBao thongBao);

    @Delete
    void delete(ThongBao thongBao);
}
