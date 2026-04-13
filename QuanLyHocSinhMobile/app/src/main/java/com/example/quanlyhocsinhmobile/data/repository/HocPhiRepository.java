package com.example.quanlyhocsinhmobile.data.repository;

import android.app.Application;

import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.HocPhiDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.HocPhi;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;

import java.util.List;

public class HocPhiRepository {

    private HocPhiDAO hocPhiDAO;
    private LopDAO lopDAO;

    public HocPhiRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        hocPhiDAO = db.hocPhiDAO();
        lopDAO = db.lopDAO();
    }

    // Lấy danh sách lớp
    public List<Lop> getAllLop() {
        return lopDAO.getAll();
    }

    // Lọc học phí (HK + năm + lớp)
    public List<HocPhi.Display> filterHocPhi(int hocKy, String namHoc, String maLop) {
        return hocPhiDAO.filterHocPhi(hocKy, namHoc, maLop);
    }

    // Tìm kiếm học phí
    public List<HocPhi.Display> searchHocPhi(String query) {
        return hocPhiDAO.searchHocPhi("%" + query + "%");
    }

    // Cập nhật học phí
    public void update(HocPhi hocPhi) {
        hocPhiDAO.update(hocPhi);
    }

    // Thêm học phí
    public void insert(HocPhi hocPhi) {
        hocPhiDAO.insert(hocPhi);
    }

    // Xoá học phí
    public void delete(HocPhi hocPhi) {
        hocPhiDAO.delete(hocPhi);
    }
}