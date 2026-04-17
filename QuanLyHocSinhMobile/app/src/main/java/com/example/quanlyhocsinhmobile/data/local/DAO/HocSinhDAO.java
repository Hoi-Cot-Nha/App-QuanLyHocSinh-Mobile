package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import java.util.List;
@Dao
public interface HocSinhDAO {
    @Query("SELECT * FROM HocSinh")
    List<HocSinh> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HocSinh hocSinh);
    @Update
    void update(HocSinh hocSinh);
    @Delete
    void delete(HocSinh hocSinh);
    @Query("SELECT COUNT(*) FROM HocSinh WHERE MaHS = :maHS")
    int checkMaHocSinh(String maHS);
    @Query("SELECT COUNT(*) FROM HocSinh WHERE HoTen = :hoTen")
    int checkTenHocSinh(String hoTen);
    @Query("SELECT HoTen FROM HocSinh WHERE MaHS = :maHS")
    String getTenHocSinh(String maHS);
    @Query("SELECT * FROM HocSinh WHERE MaHS LIKE '%' || :query || '%' OR HoTen LIKE '%' || :query || '%'")
    List<HocSinh> search(String query);
    @Query("SELECT * FROM HocSinh WHERE MaHS = :maHS LIMIT 1")
    HocSinh getHocSinhByMa(String maHS);
}