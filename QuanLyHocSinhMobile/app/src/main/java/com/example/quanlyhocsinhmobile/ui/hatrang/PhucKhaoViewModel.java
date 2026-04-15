package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import com.example.quanlyhocsinhmobile.data.local.Model.PhucKhao;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.repository.PhucKhaoRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhucKhaoViewModel extends AndroidViewModel {

    private final PhucKhaoRepository repo;
    private final MutableLiveData<List<PhucKhao.Display>> list = new MutableLiveData<>();
    private final MutableLiveData<List<Lop>> lopList = new MutableLiveData<>();
    private final MutableLiveData<List<MonHoc>> monHocList = new MutableLiveData<>();
    private final ExecutorService ex = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<HocSinh>> hocSinhList = new MutableLiveData<>();
    public LiveData<List<HocSinh>> getHocSinhList() {
        return hocSinhList;
    }

    public PhucKhaoViewModel(@NonNull Application application) {
        super(application);
        repo = new PhucKhaoRepository(application);
        load();
    }

    public LiveData<List<PhucKhao.Display>> getPhucKhaoList() {
        return list;
    }

    public LiveData<List<Lop>> getLopList() {
        return lopList;
    }

    public LiveData<List<MonHoc>> getMonHocList() {
        return monHocList;
    }

    private void load() {
        ex.execute(() -> {
            lopList.postValue(repo.getAllLop());
            monHocList.postValue(repo.getAllMonHoc());
            hocSinhList.postValue(repo.getAllHocSinh());
            list.postValue(repo.filterPhucKhao("", "", ""));
        });
    }

    public void filter(String maHS, String maMH, String trangThai) {
        ex.execute(() -> {
            list.postValue(repo.filterPhucKhao(maHS, maMH, trangThai));
        });
    }

    public void search(String query) {
        ex.execute(() -> {
            list.postValue(repo.searchPhucKhao("%" + query + "%"));
        });
    }

    public void insert(PhucKhao pk) {
        ex.execute(() -> {
            repo.insert(pk);
            list.postValue(repo.filterPhucKhao("", "", ""));
        });
    }

    public void update(PhucKhao pk) {
        ex.execute(() -> {
            repo.update(pk);
            list.postValue(repo.filterPhucKhao("", "", ""));
        });
    }

    public void delete(PhucKhao pk) {
        ex.execute(() -> {
            repo.delete(pk);
            list.postValue(repo.filterPhucKhao("", "", ""));
        });
    }
}