package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.DiemDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.Diem;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import java.util.List;
public class DiemRepository {
    private DiemDAO diemDAO;
    private LopDAO lopDAO;
    private MonHocDAO monHocDAO;
    public DiemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        diemDAO = db.diemDAO();
        lopDAO = db.lopDAO();
        monHocDAO = db.monHocDAO();
    }
    public List<Lop> getAllLop() {
        return lopDAO.getAll();
    }
    public List<MonHoc> getAllMonHoc() {
        return monHocDAO.getAll();
    }
    public List<Diem.Display> filterDiem(String maMH, int hocKy, String maLop) {
        return diemDAO.filterDiem(maMH, hocKy, maLop);
    }
    public List<Diem.Display> searchDiem(String query) {
        return diemDAO.searchDiem("%" + query + "%");
    }
    public void updateDiem(Diem diem) {
        diemDAO.update(diem);
    }
}