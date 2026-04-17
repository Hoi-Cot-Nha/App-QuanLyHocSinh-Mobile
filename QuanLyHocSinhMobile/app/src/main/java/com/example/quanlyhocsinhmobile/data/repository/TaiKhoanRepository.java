package com.example.quanlyhocsinhmobile.data.repository;
import android.app.Application;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.TaiKhoanDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.TaiKhoan;
import java.util.List;
public class TaiKhoanRepository {
    private TaiKhoanDAO taiKhoanDAO;
    public TaiKhoanRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taiKhoanDAO = db.taiKhoanDAO(); 
    }
    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanDAO.getAll();
    }
    public List<TaiKhoan> searchTaiKhoan(String query) {
        return taiKhoanDAO.search(query);
    }
    public void insert(TaiKhoan taiKhoan) {
        taiKhoanDAO.insert(taiKhoan);
    }
    public void update(TaiKhoan taiKhoan) {
        taiKhoanDAO.update(taiKhoan);
    }
    public void delete(TaiKhoan taiKhoan) {
        taiKhoanDAO.delete(taiKhoan);
    }
    public int checkTenDangNhap(String user) {
        return taiKhoanDAO.checkTenDangNhap(user);
    }
    public TaiKhoan login(String user, String pass) {
        return taiKhoanDAO.login(user, pass);
    }
    public String getQuyen(String user) {
        return taiKhoanDAO.getQuyen(user);
    }
    public List<String> getAllMaGV() {
        return taiKhoanDAO.getAllMaGV();
    }
    public List<String> getAllMaHS() {
        return taiKhoanDAO.getAllMaHS();
    }
}