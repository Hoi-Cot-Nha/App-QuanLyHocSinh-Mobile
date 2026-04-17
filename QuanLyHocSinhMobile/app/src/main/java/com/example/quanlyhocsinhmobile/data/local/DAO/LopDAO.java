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
    @Query("SELECT * FROM Lop")
    List<Lop> getAll();

    @Query("SELECT Lop.*, GiaoVien.hoTen AS tenGV " +
            "FROM Lop LEFT JOIN GiaoVien ON Lop.maGVCN = GiaoVien.maGV")
    List<Lop.Display> getAllWithTenGVCN();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lop lop);

    @Update
    void update(Lop lop);

    @Delete
    void delete(Lop lop);

    @Query("SELECT * FROM Lop WHERE MaLop LIKE '%' || :query || '%' OR TenLop LIKE '%' || :query || '%' OR nienKhoa LIKE '%' || :query || '%' OR maGVCN LIKE '%' || :query || '%'")
    List<Lop> searchLop(String query);

    @Query("SELECT Lop.*, GiaoVien.hoTen AS tenGV " +
            "FROM Lop LEFT JOIN GiaoVien ON Lop.maGVCN = GiaoVien.maGV " +
            "WHERE Lop.MaLop LIKE '%' || :query || '%' " +
            "OR Lop.TenLop LIKE '%' || :query || '%' " +
            "OR Lop.nienKhoa LIKE '%' || :query || '%' " +
            "OR Lop.maGVCN LIKE '%' || :query || '%' " +
            "OR GiaoVien.hoTen LIKE '%' || :query || '%'")
    List<Lop.Display> searchLopWithTenGVCN(String query);

    @Query("SELECT COUNT(*) FROM Lop WHERE MaLop = :maLop")
    int checkMaLop(String maLop);

    @Query("SELECT COUNT(*) FROM Lop WHERE TenLop = :tenLop")
    int checkTenLop(String tenLop);

    @Query("SELECT COUNT(*) FROM GiaoVien WHERE maGV = :maGV")
    int checkGiaoVienTonTai(String maGV);

    @Query("SELECT COUNT(*) FROM Lop WHERE maGVCN = :maGV")
    int checkGVCNDaPhanCong(String maGV);

    @Query("SELECT COUNT(*) FROM Lop WHERE maGVCN = :maGV")
    int checkGVCNTonTai(String maGV);

    @Query("SELECT MaLop FROM Lop")
    List<String> getAllMaLop();

    // ✅ Lấy danh sách niên khóa (distinct)
    @Query("SELECT DISTINCT nienKhoa FROM Lop ORDER BY nienKhoa DESC")
    List<String> getAllNienKhoa();

    // ✅ Lấy danh sách giáo viên (CHƯA là GVCN của lớp nào)
    @Query("SELECT DISTINCT g.maGV, g.hoTen FROM GiaoVien g " +
            "WHERE g.maGV NOT IN (SELECT DISTINCT maGVCN FROM Lop WHERE maGVCN IS NOT NULL) " +
            "ORDER BY g.hoTen ASC")
    List<GiaoVienInfo> getAllGiaoVienForLop();

    // ✅ Class helper để mapping dữ liệu từ query
    class GiaoVienInfo {
        public String maGV;
        public String hoTen;

        public GiaoVienInfo(String maGV, String hoTen) {
            this.maGV = maGV;
            this.hoTen = hoTen;
        }

        @Override
        public String toString() {
            return hoTen;
        }
    }
}
