package com.example.quanlyhocsinhmobile.data.local.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.quanlyhocsinhmobile.data.local.Model.TaiKhoan;
import java.util.List;
@Dao
public interface TaiKhoanDAO {
    @Query("SELECT * FROM TaiKhoan")
    List<TaiKhoan> getAll();
    @Query("SELECT * FROM TaiKhoan WHERE tenDangNhap LIKE '%' || :query || '%' OR quyen LIKE '%' || :query || '%' OR maNguoiDung LIKE '%' || :query || '%'")
    List<TaiKhoan> search(String query);
    @Query("SELECT * FROM TaiKhoan WHERE TenDangNhap = :tenDN AND MatKhau = :matKhau")
    TaiKhoan login(String tenDN, String matKhau);
    @Query("SELECT COUNT(*) FROM TaiKhoan WHERE tenDangNhap = :tenDangNhap")
    int checkTenDangNhap(String tenDangNhap);
    @Query("SELECT quyen FROM TaiKhoan WHERE tenDangNhap = :tenDangNhap LIMIT 1")
    String getQuyen(String tenDangNhap);
    @Query("SELECT MaGV FROM GiaoVien")
    List<String> getAllMaGV();
    @Query("SELECT MaHS FROM HocSinh")
    List<String> getAllMaHS();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TaiKhoan taiKhoan);
    @Update
    void update(TaiKhoan taiKhoan);
    @Delete
    void delete(TaiKhoan taiKhoan);
}