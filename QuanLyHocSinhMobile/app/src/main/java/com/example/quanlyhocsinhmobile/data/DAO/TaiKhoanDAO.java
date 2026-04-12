package com.example.quanlyhocsinhmobile.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.Model.TaiKhoan;

import java.util.List;

@Dao
public interface TaiKhoanDAO {
    @Query("SELECT * FROM TaiKhoan")
    List<TaiKhoan> getAll();

    @Query("SELECT * FROM TaiKhoan WHERE TenDangNhap = :tenDN AND MatKhau = :matKhau")
    TaiKhoan login(String tenDN, String matKhau);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaiKhoan taiKhoan);

    @Update
    void update(TaiKhoan taiKhoan);

    @Delete
    void delete(TaiKhoan taiKhoan);
}
