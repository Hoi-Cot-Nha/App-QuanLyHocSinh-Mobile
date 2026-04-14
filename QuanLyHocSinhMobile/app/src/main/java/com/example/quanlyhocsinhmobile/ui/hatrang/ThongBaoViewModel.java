package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyhocsinhmobile.data.local.Model.ThongBao;
import com.example.quanlyhocsinhmobile.data.repository.ThongBaoRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThongBaoViewModel extends AndroidViewModel {

    private final ThongBaoRepository repo;
    private final MutableLiveData<List<ThongBao>> list = new MutableLiveData<>();
    private final ExecutorService ex = Executors.newSingleThreadExecutor();

    public ThongBaoViewModel(@NonNull Application app) {
        super(app);
        repo = new ThongBaoRepository(app);
        load();
    }

    // 🔹 Lấy danh sách thông báo
    public LiveData<List<ThongBao>> getThongBaoList() {
        return list;
    }

    // 🔹 Load dữ liệu ban đầu
    private void load() {
        ex.execute(() -> list.postValue(repo.getAll()));
    }

    // 🔹 Filter theo người gửi
    public void filter(String nguoiGui) {
        ex.execute(() -> list.postValue(repo.filterThongBao(nguoiGui)));
    }

    // 🔹 Search
    public void search(String search) {
        ex.execute(() -> list.postValue(repo.searchThongBao(search)));
    }

    // 🔹 Filter + Search (giống HocPhi)
    public void filter(String search, String nguoiGui) {
        ex.execute(() -> list.postValue(repo.filter(search, nguoiGui)));
    }

    // 🔹 Thêm
    public void insert(ThongBao tb) {
        ex.execute(() -> {
            repo.insert(tb);
            list.postValue(repo.getAll());
        });
    }

    // 🔹 Sửa
    public void update(ThongBao tb) {
        ex.execute(() -> {
            repo.update(tb);
            list.postValue(repo.getAll());
        });
    }

    // 🔹 Xoá
    public void delete(ThongBao tb) {
        ex.execute(() -> {
            repo.delete(tb);
            list.postValue(repo.getAll());
        });
    }
}