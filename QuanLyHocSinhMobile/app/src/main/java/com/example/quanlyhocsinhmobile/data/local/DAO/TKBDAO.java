package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.ThoiKhoaBieu;
import java.util.List;
@Dao
public interface TKBDAO {
    @Query("SELECT ThoiKhoaBieu.*, Lop.TenLop as tenLop, MonHoc.TenMH as tenMH, GiaoVien.hoTen as tenGV, PhongHoc.tenPhong as tenPhong " +
           "FROM ThoiKhoaBieu " +
           "LEFT JOIN Lop ON ThoiKhoaBieu.maLop = Lop.MaLop " +
           "LEFT JOIN MonHoc ON ThoiKhoaBieu.maMH = MonHoc.MaMH " +
           "LEFT JOIN GiaoVien ON ThoiKhoaBieu.maGV = GiaoVien.maGV " +
           "LEFT JOIN PhongHoc ON ThoiKhoaBieu.maPhong = PhongHoc.maPhong")
    List<ThoiKhoaBieu.Display> getAll();
    @Query("SELECT ThoiKhoaBieu.*, Lop.TenLop as tenLop, MonHoc.TenMH as tenMH, GiaoVien.hoTen as tenGV, PhongHoc.tenPhong as tenPhong " +
           "FROM ThoiKhoaBieu " +
           "LEFT JOIN Lop ON ThoiKhoaBieu.maLop = Lop.MaLop " +
           "LEFT JOIN MonHoc ON ThoiKhoaBieu.maMH = MonHoc.MaMH " +
           "LEFT JOIN GiaoVien ON ThoiKhoaBieu.maGV = GiaoVien.maGV " +
           "LEFT JOIN PhongHoc ON ThoiKhoaBieu.maPhong = PhongHoc.maPhong " +
           "WHERE (:thu = 0 OR ThoiKhoaBieu.thu = :thu) " +
           "AND (:maLop = '' OR ThoiKhoaBieu.maLop = :maLop) " +
           "AND (:maMH = '' OR ThoiKhoaBieu.maMH = :maMH)")
    List<ThoiKhoaBieu.Display> filterTKB(int thu, String maLop, String maMH);
    @Query("SELECT COUNT(*) FROM ThoiKhoaBieu WHERE thu = :thu AND maLop = :maLop " +
           "AND maMH = :maMH AND maGV = :maGV AND maPhong = :maPhong " +
           "AND tietBatDau = :tbd AND tietKetThuc = :tkt")
    int checkDuplicate(int thu, String maLop, String maMH, String maGV, String maPhong, int tbd, int tkt);
    @Query("SELECT COUNT(*) FROM ThoiKhoaBieu WHERE thu = :thu " +
           "AND (maLop = :maLop OR maGV = :maGV OR maPhong = :maPhong) " +
           "AND (:tkt >= tietBatDau AND :tbd <= tietKetThuc) " +
           "AND (:id = 0 OR maTKB != :id)")
    int checkOverlap(int id, int thu, String maLop, String maGV, String maPhong, int tbd, int tkt);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ThoiKhoaBieu tkb);
    @Update
    void update(ThoiKhoaBieu tkb);
    @Delete
    void delete(ThoiKhoaBieu tkb);
}