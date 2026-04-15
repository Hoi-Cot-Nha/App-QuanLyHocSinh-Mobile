package com.example.quanlyhocsinhmobile.data.repository;

import android.app.Application;

import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.PhucKhaoDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import com.example.quanlyhocsinhmobile.data.local.Model.PhucKhao;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.DAO.HocSinhDAO;

import java.util.List;

public class PhucKhaoRepository {

    private PhucKhaoDAO phucKhaoDAO;
    private LopDAO lopDAO;
    private MonHocDAO monHocDAO;
    private HocSinhDAO hocSinhDAO;

    public PhucKhaoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        phucKhaoDAO = db.phucKhaoDAO();
        lopDAO = db.lopDAO();
        monHocDAO = db.monHocDAO();
        hocSinhDAO = db.hocSinhDAO();
    }

    public List<Lop> getAllLop() {
        return lopDAO.getAll();
    }

    public List<MonHoc> getAllMonHoc() {
        return monHocDAO.getAll();
    }

    public List<PhucKhao.Display> getAllPhucKhao() {
        return phucKhaoDAO.getAll();
    }

    public List<HocSinh> getAllHocSinh() {
        return hocSinhDAO.getAll();
    }

    public List<PhucKhao.Display> filterPhucKhao(String maHS, String maMH, String trangThai) {
        return phucKhaoDAO.filterPhucKhao(maHS, maMH, trangThai);
    }

    public List<PhucKhao.Display> searchPhucKhao(String query) {
        return phucKhaoDAO.searchPhucKhao(query);
    }

    public void update(PhucKhao phucKhao) {
        phucKhaoDAO.update(phucKhao);
    }

    public void insert(PhucKhao phucKhao) {
        phucKhaoDAO.insert(phucKhao);
    }

    public void delete(PhucKhao phucKhao) {
        phucKhaoDAO.delete(phucKhao);
    }
}