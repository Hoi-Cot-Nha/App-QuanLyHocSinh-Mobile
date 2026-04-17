package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.HocSinhDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import java.util.List;
public class HocSinhRepository {
    private final HocSinhDAO dao;
    public HocSinhRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.hocSinhDAO();
    }
    public List<HocSinh> getAll() {
        return dao.getAll();
    }
    public List<HocSinh> search(String q) {
        return dao.search(q);
    }
    public void insert(HocSinh hs) {
         dao.insert(hs);
    }
    public void update(HocSinh hs) {
         dao.update(hs);
    }
    public void delete(HocSinh hs) {
         dao.delete(hs);
    }
    public int checkMa(String ma) {
        return dao.checkMaHocSinh(ma);
    }
    public int checkTen(String ten) {
        return dao.checkTenHocSinh(ten);
    }
}