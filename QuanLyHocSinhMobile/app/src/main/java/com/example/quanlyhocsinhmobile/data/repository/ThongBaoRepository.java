package com.example.quanlyhocsinhmobile.data.repository;

import android.app.Application;

import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.ThongBaoDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.ThongBao;

import java.util.List;

public class ThongBaoRepository {

    private ThongBaoDAO thongBaoDAO;

    public ThongBaoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        thongBaoDAO = db.thongBaoDAO();
    }

    // 🔹 Lấy tất cả thông báo
    public List<ThongBao> getAll() {
        return thongBaoDAO.getAll();
    }

    // 🔹 Lọc theo người gửi
    public List<ThongBao> filterThongBao(String nguoiGui) {
        return thongBaoDAO.filterThongBao(nguoiGui);
    }

    // 🔹 Tìm kiếm
    public List<ThongBao> searchThongBao(String query) {
        return thongBaoDAO.searchThongBao("%" + query + "%");
    }

    // 🔹 Filter + Search (giống HocPhi)
    public List<ThongBao> filter(String search, String nguoiGui) {
        String keyword = search.isEmpty() ? "" : "%" + search + "%";
        return thongBaoDAO.filter(keyword, nguoiGui);
    }

    // 🔹 Thêm
    public void insert(ThongBao thongBao) {
        thongBaoDAO.insert(thongBao);
    }

    // 🔹 Sửa
    public void update(ThongBao thongBao) {
        thongBaoDAO.update(thongBao);
    }

    // 🔹 Xoá
    public void delete(ThongBao thongBao) {
        thongBaoDAO.delete(thongBao);
    }
}