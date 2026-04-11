package com.example.quanlyhocsinhmoblie.data.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmoblie.data.Model.Diem;

import java.util.List;

@Dao
public interface DiemDAO {

    @Query("SELECT * FROM Diem")
    List<Diem> getAll();

    @Query("SELECT * FROM Diem WHERE maHS = :maHS AND maMH = :maMH AND hocKy = :hocKy")
    Diem getDiem(String maHS, String maMH, int hocKy);

    @Query("SELECT * FROM Diem WHERE maHS = :maHS")
    List<Diem> getDiemByHS(String maHS);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Diem diem);

    @Update
    void update(Diem diem);

    @Delete
    void delete(Diem diem);

    @Query("SELECT * FROM Diem WHERE maHS LIKE :search OR maMH LIKE :search OR tenHS LIKE :search")
    List<Diem> searchDiem(String search);

    @Query("SELECT * FROM Diem WHERE (:maMH IS NULL OR maMH = :maMH) AND (:hocKy = 0 OR hocKy = :hocKy)")
    List<Diem> filterDiem(String maMH, int hocKy);
}
