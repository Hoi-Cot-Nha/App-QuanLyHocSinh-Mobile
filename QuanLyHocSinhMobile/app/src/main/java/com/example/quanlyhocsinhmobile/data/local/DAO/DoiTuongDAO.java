package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.DoiTuongUuTien;
import java.util.List;
@Dao
public interface DoiTuongDAO {
    @Query("SELECT * FROM DoiTuongUuTien")
    List<DoiTuongUuTien> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DoiTuongUuTien doiTuong);
    @Update
    void update(DoiTuongUuTien doiTuong);
    @Delete
    void delete(DoiTuongUuTien doiTuong);
    @Query("SELECT COUNT(*) FROM DoiTuongUuTien WHERE maDT = :maDT")
    int checkMaDT(String maDT);
    @Query("SELECT COUNT(*) FROM DoiTuongUuTien WHERE tenDT = :tenDT")
    int checkTenDT(String tenDT);
    @Query("SELECT tenDT FROM DoiTuongUuTien WHERE maDT = :maDT")
    String getTenDT(String maDT);
    @Query("SELECT * FROM DoiTuongUuTien WHERE maDT LIKE '%' || :query || '%' OR tenDT LIKE '%' || :query || '%'")
    List<DoiTuongUuTien> search(String query);
    @Query("SELECT maDT FROM DoiTuongUuTien")
    List<String> getAllMaDT();
}