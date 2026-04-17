package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.PhongHocDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import java.util.List;
public class PhongHocRepository {
    private PhongHocDAO phongHocDAO;
    public PhongHocRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        phongHocDAO = db.phongHocDAO();
    }
    public List<PhongHoc> getAllPhongHoc() {
        return phongHocDAO.getAll();
    }
    public void insert(PhongHoc phongHoc) {
        AppDatabase.databaseWriteExecutor.execute(() -> phongHocDAO.insert(phongHoc));
    }
    public void update(PhongHoc phongHoc) {
        AppDatabase.databaseWriteExecutor.execute(() -> phongHocDAO.update(phongHoc));
    }
    public void delete(PhongHoc phongHoc) {
        AppDatabase.databaseWriteExecutor.execute(() -> phongHocDAO.delete(phongHoc));
    }
    public int checkMaPhong(String maPhong) {
        return phongHocDAO.checkMaPhong(maPhong);
    }
    public int checkTenPhong(String tenPhong) {
        return phongHocDAO.checkTenPhong(tenPhong);
    }
}