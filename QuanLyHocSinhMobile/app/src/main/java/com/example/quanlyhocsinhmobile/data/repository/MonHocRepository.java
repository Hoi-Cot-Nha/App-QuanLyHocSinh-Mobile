package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import java.util.List;
public class MonHocRepository {
    private MonHocDAO monHocDAO;
    public MonHocRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        monHocDAO = db.monHocDAO();
    }
    public List<MonHoc> getAllMonHoc() {
        return monHocDAO.getAll();
    }
    public List<MonHoc> searchMonHoc(String query) {
        return monHocDAO.search(query);
    }
    public void insert(MonHoc monHoc) {
        AppDatabase.databaseWriteExecutor.execute(() -> monHocDAO.insert(monHoc));
    }
    public void update(MonHoc monHoc) {
        AppDatabase.databaseWriteExecutor.execute(() -> monHocDAO.update(monHoc));
    }
    public void delete(MonHoc monHoc) {
        AppDatabase.databaseWriteExecutor.execute(() -> monHocDAO.delete(monHoc));
    }
    public int checkMaMonHoc(String maMH) {
        return monHocDAO.checkMaMonHoc(maMH);
    }
    public int checkTenMonHoc(String tenMH) {
        return monHocDAO.checkTenMonHoc(tenMH);
    }
}