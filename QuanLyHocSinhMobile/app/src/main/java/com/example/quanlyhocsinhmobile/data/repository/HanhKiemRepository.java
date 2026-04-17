package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.HanhKiemDAO;
import com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.HanhKiem;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import java.util.List;
public class HanhKiemRepository {
    private HanhKiemDAO hanhKiemDAO;
    private LopDAO lopDAO;
    public HanhKiemRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        hanhKiemDAO = db.hanhKiemDAO();
        lopDAO = db.lopDAO();
    }
    public List<Lop> getAllLop() {
        return lopDAO.getAll();
    }
    public List<HanhKiem.Display> filterHanhKiem(String maLop, int hocKy, String namHoc) {
        return hanhKiemDAO.filterHanhKiem(maLop, hocKy, namHoc);
    }
    public List<HanhKiem.Display> searchHanhKiem(String query) {
        return hanhKiemDAO.searchHanhKiem("%" + query + "%");
    }
    public void updateHanhKiem(HanhKiem hanhKiem) {
        hanhKiemDAO.update(hanhKiem);
    }
}