package com.example.quanlyhocsinhmobile.data.local.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.local.Model.Lop;

import java.util.List;

@Dao
public interface LopDAO {


    @Query("SELECT Lop.* " +
            "FROM Lop ")
    List<Lop> getAll();


    // ================= CRUD =================
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lop lop);

    @Update
    void update(Lop lop);

    @Delete
    void delete(Lop lop);


    // ================= CHECK =================
    @Query("SELECT COUNT(*) FROM Lop WHERE MaLop = :maLop")
    int checkMaLop(String maLop);

    @Query("SELECT COUNT(*) FROM Lop WHERE TenLop = :tenLop")
    int checkTenLop(String tenLop);

    @Query("SELECT COUNT(*) FROM Lop WHERE maGVCN = :maGV")
    int checkGVCNTonTai(String maGV);


    // ================= SEARCH =================
    @Query("SELECT Lop.*" +
            "FROM Lop " +
            "WHERE Lop.TenLop LIKE :query " +
            "OR Lop.MaLop LIKE :query " +
            "OR Lop.nienKhoa LIKE :query " +
            "OR Lop.maGVCN LIKE :query")
    List<Lop> searchLop(String query);
}