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

    public List<ThongBao> getAll() {
        return thongBaoDAO.getAll();
    }

    public List<ThongBao> searchThongBao(String query) {
        return thongBaoDAO.searchThongBao("%" + query + "%");
    }

    public void insert(ThongBao thongBao) {
        thongBaoDAO.insert(thongBao);
    }

    public void update(ThongBao thongBao) {
        thongBaoDAO.update(thongBao);
    }

    public void delete(ThongBao thongBao) {
        thongBaoDAO.delete(thongBao);
    }
}