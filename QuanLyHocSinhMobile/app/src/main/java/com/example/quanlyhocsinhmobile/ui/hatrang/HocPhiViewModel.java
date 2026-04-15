package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyhocsinhmobile.data.local.Model.HocPhi;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.repository.HocPhiRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HocPhiViewModel extends AndroidViewModel {

    private final HocPhiRepository repo;
    private final MutableLiveData<List<HocPhi.Display>> list = new MutableLiveData<>();
    private final MutableLiveData<List<Lop>> lopList = new MutableLiveData<>();
    private final ExecutorService ex = Executors.newSingleThreadExecutor();

    public HocPhiViewModel(@NonNull Application app) {
        super(app);
        repo = new HocPhiRepository(app);
        load();
    }

    public LiveData<List<HocPhi.Display>> getHocPhiList() {
        return list;
    }

    public LiveData<List<Lop>> getLopList() {
        return lopList;
    }

    private void load() {
        ex.execute(() -> {
            lopList.postValue(repo.getAllLop());
            list.postValue(repo.filterHocPhi(0, "", ""));
        });
    }

    public void filter(int hk, String nam, String lop) {
        ex.execute(() -> list.postValue(repo.filterHocPhi(hk, nam, lop)));
    }

    public void update(HocPhi hp) {
        ex.execute(() -> {
            repo.update(hp);
            list.postValue(repo.filterHocPhi(0, "", ""));
        });
    }

}