package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.LichThiDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.PhongHocDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.LichThi;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import java.util.List;
public class LichThiRepository {
    private LichThiDAO lichThiDAO;
    private MonHocDAO monHocDAO;
    private PhongHocDAO phongHocDAO;
    public LichThiRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        lichThiDAO = db.lichThiDAO();
        monHocDAO = db.monHocDAO();
        phongHocDAO = db.phongHocDAO();
    }
    public List<MonHoc> getAllMonHoc() {
        return monHocDAO.getAll();
    }
    public List<PhongHoc> getAllPhongHoc() {
        return phongHocDAO.getAll();
    }
    public List<LichThi.Display> getAllLichThi() {
        return lichThiDAO.getAll();
    }
    public List<LichThi.Display> filterLichThi(String maMH, String maPhong) {
        return lichThiDAO.filterLichThi(maMH, maPhong);
    }
    public List<LichThi.Display> searchLichThi(String query) {
        return lichThiDAO.searchLichThi("%" + query + "%");
    }
    public void insert(LichThi lichThi) {
        lichThiDAO.insert(lichThi);
    }
    public boolean hasConflict(LichThi lichThi) {
        return lichThiDAO.countConflicts(
                lichThi.getMaPhong(),
                lichThi.getNgayThi(),
                lichThi.getGioBatDau(),
                lichThi.getGioKetThuc()) > 0;
    }
    public boolean hasConflictExcludeId(LichThi lichThi) {
        return lichThiDAO.countConflictsExcludeId(
                lichThi.getMaLT(),
                lichThi.getMaPhong(),
                lichThi.getNgayThi(),
                lichThi.getGioBatDau(),
                lichThi.getGioKetThuc()) > 0;
    }
    public void update(LichThi lichThi) {
        lichThiDAO.update(lichThi);
    }
    public void delete(LichThi lichThi) {
        lichThiDAO.delete(lichThi);
    }
}