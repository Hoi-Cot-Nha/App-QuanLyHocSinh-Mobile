package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.GiaoVienDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.ToBoMonDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;
import java.util.List;
import java.util.concurrent.CountDownLatch;
public class GiaoVienRepository {
    private GiaoVienDAO giaovienDAO;
    private MonHocDAO monHocDAO;
    private ToBoMonDAO toBoMonDAO;
    public GiaoVienRepository(GiaoVienDAO giaovienDAO) {
        this.giaovienDAO = giaovienDAO;
    }
    public GiaoVienRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        giaovienDAO = db.giaoVienDAO();
        monHocDAO = db.monHocDAO();
        toBoMonDAO = db.toBoMonDAO();
    }
    public List<GiaoVien.Display> getAllGiaoVien() {
        return giaovienDAO.getAll();
    }
    public List<MonHoc> getAllMonHoc() {
        return monHocDAO.getAll();
    }
    public List<ToHopMon> getAllToHop() { return toBoMonDAO.getAll(); }
    public List<GiaoVien.Display> search(String query) {
        return giaovienDAO.searchGiaoVien(query);
    }
    public void insert(GiaoVien giaoVien) {
        AppDatabase.databaseWriteExecutor.execute(() -> giaovienDAO.insert(giaoVien));
    }
    public void insertAndWait(GiaoVien giaoVien) {
        CountDownLatch latch = new CountDownLatch(1);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                giaovienDAO.insert(giaoVien);
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void update(GiaoVien giaoVien){
        AppDatabase.databaseWriteExecutor.execute(() -> giaovienDAO.update(giaoVien));
    }
    public void updateAndWait(GiaoVien giaoVien) {
        CountDownLatch latch = new CountDownLatch(1);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                giaovienDAO.update(giaoVien);
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public void delete(GiaoVien giaoVien) {
        AppDatabase.databaseWriteExecutor.execute(() -> giaovienDAO.delete(giaoVien));
    }
    public void deleteAndWait(GiaoVien giaoVien) {
        CountDownLatch latch = new CountDownLatch(1);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                giaovienDAO.delete(giaoVien);
            } finally {
                latch.countDown();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    public int checkMaGiaoVien(String maGV) {
        return giaovienDAO.checkMaGiaoVien(maGV);
    }
    public int checkSDT(String sdt){
        return giaovienDAO.checkSDT(sdt);
    }
    public List<GiaoVien.Display> searchGiaoVien(String query){
        return giaovienDAO.searchGiaoVien(query);
    }
    public String getNextMaGV() {
        int maxNum = giaovienDAO.getMaxGVNumber();
        return String.format("GV%02d", maxNum + 1);
    }
}