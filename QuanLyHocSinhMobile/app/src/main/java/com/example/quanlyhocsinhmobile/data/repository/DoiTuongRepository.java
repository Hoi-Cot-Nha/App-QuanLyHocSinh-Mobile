package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.DoiTuongDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.DoiTuongUuTien;
import java.util.List;
public class DoiTuongRepository {
    private DoiTuongDAO doiTuongDAO;
    public DoiTuongRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        doiTuongDAO = db.doiTuongDAO();
    }
    public List<DoiTuongUuTien> getAllDoiTuong() {
        return doiTuongDAO.getAll();
    }
    public List<DoiTuongUuTien> searchDoiTuong(String query) {
        return doiTuongDAO.search(query);
    }
    public void insert(DoiTuongUuTien doiTuong) {
        doiTuongDAO.insert(doiTuong);
    }
    public void update(DoiTuongUuTien doiTuong) {
        doiTuongDAO.update(doiTuong);
    }
    public void delete(DoiTuongUuTien doiTuong) {
        doiTuongDAO.delete(doiTuong);
    }
    public int checkMaDT(String maDT) {
        return doiTuongDAO.checkMaDT(maDT);
    }
    public int checkTenDT(String tenDT) {
        return doiTuongDAO.checkTenDT(tenDT);
    }
}