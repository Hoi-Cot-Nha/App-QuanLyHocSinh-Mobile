package com.example.quanlyhocsinhmobile.data.repository;

import android.app.Application;

import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;

import java.util.List;

public class LopRepository {
    private LopDAO lopDAO;
    public LopRepository(LopDAO lopDAO) {
        this.lopDAO = lopDAO;
    }

    public LopRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        lopDAO = db.lopDAO();
    }

    public List<Lop.Display> getAllLop() {
        return lopDAO.getAllWithTenGVCN();
    }

    public List<Lop.Display> search(String query) {
        return lopDAO.searchLopWithTenGVCN(query);
    }
    public void insert(Lop lop) {
        AppDatabase.databaseWriteExecutor.execute(() -> lopDAO.insert(lop));
    }
    public void update(Lop lop){
        AppDatabase.databaseWriteExecutor.execute(() -> lopDAO.update(lop));
    }

    public void delete(Lop lop){
        AppDatabase.databaseWriteExecutor.execute(() -> lopDAO.delete(lop));
    }

    public int checkMaLop(String maLop) {
        return lopDAO.checkMaLop(maLop);
    }
    public int checkTenLop(String tenLop){
        return lopDAO.checkTenLop(tenLop);
    }

    public int checkGVCNTonTai(String maGV){
        return lopDAO.checkGVCNTonTai(maGV);
    }

    public int checkGiaoVienTonTai(String maGV) {
        return lopDAO.checkGiaoVienTonTai(maGV);
    }

    public int checkGVCNDaPhanCong(String maGV) {
        return lopDAO.checkGVCNDaPhanCong(maGV);
    }

}
