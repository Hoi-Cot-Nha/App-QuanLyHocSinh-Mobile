package com.example.quanlyhocsinhmobile.data.repository;

import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.GiaoVienDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.PhongHocDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.TKBDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.ThoiKhoaBieu;
import java.util.List;

public class TKBRepository {
    private TKBDAO tkbDAO;
    private LopDAO lopDAO;
    private MonHocDAO monHocDAO;
    private GiaoVienDAO giaoVienDAO;
    private PhongHocDAO phongHocDAO;

    public TKBRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        tkbDAO = db.tkbDAO();
        lopDAO = db.lopDAO();
        monHocDAO = db.monHocDAO();
        giaoVienDAO = db.giaoVienDAO();
        phongHocDAO = db.phongHocDAO();
    }

    public List<ThoiKhoaBieu.Display> filterTKB(int thu, String maLop, String maMH) {
        return tkbDAO.filterTKB(thu, maLop, maMH);
    }

    public List<Lop> getAllLop() { return lopDAO.getAll(); }
    public List<MonHoc> getAllMonHoc() { return monHocDAO.getAll(); }
    public List<GiaoVien.Display> getAllGiaoVien() { return giaoVienDAO.getAll(); }
    public List<PhongHoc> getAllPhongHoc() { return phongHocDAO.getAll(); }

    public void insert(ThoiKhoaBieu tkb) {
        AppDatabase.databaseWriteExecutor.execute(() -> tkbDAO.insert(tkb));
    }

    public void update(ThoiKhoaBieu tkb) {
        AppDatabase.databaseWriteExecutor.execute(() -> tkbDAO.update(tkb));
    }

    public void delete(ThoiKhoaBieu tkb) {
        AppDatabase.databaseWriteExecutor.execute(() -> tkbDAO.delete(tkb));
    }

    public int checkOverlap(int maTKB, int thu, String maLop, String maGV, String maPhong, int tietBD, int tietKT) {
        return tkbDAO.checkOverlap(maTKB, thu, maLop, maGV, maPhong, tietBD, tietKT);
    }
}
