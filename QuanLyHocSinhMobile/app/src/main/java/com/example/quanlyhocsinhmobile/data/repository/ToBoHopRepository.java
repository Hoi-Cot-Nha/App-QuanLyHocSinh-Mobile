package com.example.quanlyhocsinhmobile.data.repository;

import android.app.Application;

import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.ToBoMonDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;

import java.util.List;

public class ToBoHopRepository {

    private ToBoMonDAO toBoMonDAO;

    public  ToBoHopRepository(ToBoMonDAO getToBoMonDAO){
        this.toBoMonDAO = toBoMonDAO;
    }
    public ToBoHopRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        toBoMonDAO = db.toBoMonDAO();
    }

    public List<ToHopMon> getAllToHop() {

        return toBoMonDAO.getAll();
    }
    public List<ToHopMon> search(String query) {

        return toBoMonDAO.searchToHop(query);
    }
    public void insert(ToHopMon toHopMon) {
        AppDatabase.databaseWriteExecutor.execute(() -> toBoMonDAO.insert(toHopMon));
    }

    public void update(ToHopMon toHopMon){
        AppDatabase.databaseWriteExecutor.execute(() -> toBoMonDAO.update(toHopMon));
    }
    public void delete(ToHopMon toHopMon) {
        AppDatabase.databaseWriteExecutor.execute(() -> toBoMonDAO.delete(toHopMon));
    }

    public int checkMaToHop(String maToHop) {
        return toBoMonDAO.checkMaToHop(maToHop);
    }

    public int checkTenToHop(String tenToHop){
        return toBoMonDAO.checkTenToHop(tenToHop);
    }
}
