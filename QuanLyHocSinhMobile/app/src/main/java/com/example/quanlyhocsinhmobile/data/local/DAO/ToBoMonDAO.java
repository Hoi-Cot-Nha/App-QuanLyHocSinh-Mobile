package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;
import java.util.List;
@Dao
public interface ToBoMonDAO {
    @Query("SELECT * FROM ToHopMon")
    List<ToHopMon> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ToHopMon toHopMon);
    @Update
    void update(ToHopMon toHopMon);
    @Delete
    void delete(ToHopMon toHopMon);
    @Query("SELECT COUNT(*) FROM ToHopMon  WHERE MaToHop = :maToHop")
    int checkMaToHop(String maToHop);
    @Query("SELECT COUNT(*) FROM ToHopMon  WHERE TenToHop = :tenToHop")
    int checkTenToHop(String tenToHop);
    @Query("SELECT * FROM ToHopMon " +
            "WHERE MaToHop LIKE '%' || :query || '%' " +
            "OR TenToHop LIKE '%' || :query || '%' ")
    List<ToHopMon> searchToHop(String query);
}