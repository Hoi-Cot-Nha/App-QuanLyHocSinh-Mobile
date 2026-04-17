package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.PhucKhao;
import java.util.List;
@Dao
public interface PhucKhaoDAO {
    @Query("SELECT PhucKhao.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
            "FROM PhucKhao " +
            "LEFT JOIN HocSinh ON PhucKhao.maHS = HocSinh.MaHS " +
            "LEFT JOIN MonHoc ON PhucKhao.maMH = MonHoc.MaMH " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop")
    List<PhucKhao.Display> getAll();
    @Query("SELECT * FROM PhucKhao WHERE maPK = :maPK")
    PhucKhao getPhucKhaoById(int maPK);
    @Query("SELECT PhucKhao.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
            "FROM PhucKhao " +
            "LEFT JOIN HocSinh ON PhucKhao.maHS = HocSinh.MaHS " +
            "LEFT JOIN MonHoc ON PhucKhao.maMH = MonHoc.MaMH " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE PhucKhao.maHS = :maHS")
    List<PhucKhao.Display> getPhucKhaoByHS(String maHS);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PhucKhao phucKhao);
    @Update
    void update(PhucKhao phucKhao);
    @Delete
    void delete(PhucKhao phucKhao);
    @Query("SELECT PhucKhao.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
            "FROM PhucKhao " +
            "LEFT JOIN HocSinh ON PhucKhao.maHS = HocSinh.MaHS " +
            "LEFT JOIN MonHoc ON PhucKhao.maMH = MonHoc.MaMH " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE PhucKhao.maHS LIKE :search " +
            "OR HocSinh.HoTen LIKE :search " +
            "OR MonHoc.TenMH LIKE :search")
    List<PhucKhao.Display> searchPhucKhao(String search);
    @Query("SELECT PhucKhao.*, HocSinh.HoTen as tenHS, MonHoc.TenMH as tenMH, Lop.TenLop as tenLop " +
            "FROM PhucKhao " +
            "LEFT JOIN HocSinh ON PhucKhao.maHS = HocSinh.MaHS " +
            "LEFT JOIN MonHoc ON PhucKhao.maMH = MonHoc.MaMH " +
            "LEFT JOIN Lop ON HocSinh.MaLop = Lop.MaLop " +
            "WHERE (:maHS = '' OR PhucKhao.maHS = :maHS) " +
            "AND (:maMH = '' OR PhucKhao.maMH = :maMH) " +
            "AND (:trangThai = '' OR PhucKhao.trangThai = :trangThai)")
    List<PhucKhao.Display> filterPhucKhao(@NonNull String maHS,
                                          @NonNull String maMH,
                                          @NonNull String trangThai);
}