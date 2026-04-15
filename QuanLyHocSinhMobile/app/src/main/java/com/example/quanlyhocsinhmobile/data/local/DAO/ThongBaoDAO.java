package com.example.quanlyhocsinhmobile.data.local.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quanlyhocsinhmobile.data.local.Model.ThongBao;

import java.util.List;

@Dao
public interface ThongBaoDAO {

    // Lấy tất cả thông báo
    @Query("SELECT * FROM ThongBao ORDER BY maTB ASC")
    List<ThongBao> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ThongBao thongBao);

    @Update
    void update(ThongBao thongBao);

    @Delete
    void delete(ThongBao thongBao);

    // Tìm kiếm (theo tiêu đề + nội dung + người gửi)
    @Query("SELECT * FROM ThongBao " +
            "WHERE tieuDe LIKE :search " +
            "OR noiDung LIKE :search " +
            "OR nguoiGui LIKE :search " +
            "ORDER BY maTB ")
    List<ThongBao> searchThongBao(String search);

    // Lọc theo người gửi
    @Query("SELECT * FROM ThongBao " +
            "WHERE (:nguoiGui = '' OR nguoiGui = :nguoiGui) " +
            "ORDER BY maTB ")
    List<ThongBao> filterThongBao(String nguoiGui);

    // Lọc + tìm kiếm (giống filter + search học phí)
    @Query("SELECT * FROM ThongBao " +
            "WHERE (:search = '' OR tieuDe LIKE :search OR noiDung LIKE :search) " +
            "AND (:nguoiGui = '' OR nguoiGui = :nguoiGui) " +
            "ORDER BY maTB ")
    List<ThongBao> filter(String search, String nguoiGui);
}