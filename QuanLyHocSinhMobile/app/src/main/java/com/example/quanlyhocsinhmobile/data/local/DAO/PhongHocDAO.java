package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import java.util.List;
@Dao
public interface PhongHocDAO {
    @Query("SELECT maPhong, tenPhong, sucChua, loaiPhong, " +
           "CASE WHEN EXISTS (SELECT 1 FROM ThoiKhoaBieu WHERE ThoiKhoaBieu.maPhong = PhongHoc.maPhong) " +
           "THEN 'Đang sử dụng' ELSE 'Trống' END AS tinhTrang " +
           "FROM PhongHoc")
    List<PhongHoc> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PhongHoc phongHoc);
    @Update
    void update(PhongHoc phongHoc);
    @Delete
    void delete(PhongHoc phongHoc);
    @Query("SELECT COUNT(*) FROM PhongHoc WHERE maPhong = :ma")
    int checkMaPhong(String ma);
    @Query("SELECT COUNT(*) FROM PhongHoc WHERE tenPhong = :ten")
    int checkTenPhong(String ten);
}