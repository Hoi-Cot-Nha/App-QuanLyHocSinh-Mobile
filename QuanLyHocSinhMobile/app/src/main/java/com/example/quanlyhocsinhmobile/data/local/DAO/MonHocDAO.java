package com.example.quanlyhocsinhmobile.data.local.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;

import java.util.List;

@Dao
public interface MonHocDAO {
    @Query("SELECT * FROM monhoc")
    List<MonHoc> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MonHoc monHoc);

    @Update
    void update(MonHoc monHoc);

    @Delete
    void delete(MonHoc monHoc);

    @Query("SELECT COUNT(*) FROM monhoc WHERE MaMH = :maMH")
    int checkMaMonHoc(String maMH);

    @Query("SELECT COUNT(*) FROM monhoc WHERE TenMH = :tenMH")
    int checkTenMonHoc(String tenMH);

    @Query("SELECT * FROM monhoc WHERE MaMH LIKE '%' || :query || '%' OR TenMH LIKE '%' || :query || '%'")
    List<MonHoc> search(String query);
}
