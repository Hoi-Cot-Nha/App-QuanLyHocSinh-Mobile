package com.example.quanlyhocsinhmobile.ui.tien;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.Diem;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.repository.DiemRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiemViewModel extends AndroidViewModel {
    private final DiemRepository repository;
    private final MutableLiveData<List<Diem.Display>> diemList = new MutableLiveData<>();
    private final MutableLiveData<List<Lop>> lopList = new MutableLiveData<>();
    private final MutableLiveData<List<MonHoc>> monHocList = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public DiemViewModel(@NonNull Application application) {
        super(application);
        repository = new DiemRepository(application);
        loadInitialData();
    }

    public LiveData<List<Diem.Display>> getDiemList() {
        return diemList;
    }

    public LiveData<List<Lop>> getLopList() {
        return lopList;
    }

    public LiveData<List<MonHoc>> getMonHocList() {
        return monHocList;
    }

    private void loadInitialData() {
        executorService.execute(() -> {
            lopList.postValue(repository.getAllLop());
            monHocList.postValue(repository.getAllMonHoc());
            // Tải dữ liệu tất cả điểm ngay khi vào
            diemList.postValue(repository.filterDiem("", 0, ""));
        });
    }

    public void filter(String maMH, int hocKy, String maLop) {
        executorService.execute(() -> diemList.postValue(repository.filterDiem(maMH, hocKy, maLop)));
    }

    public void search(String query) {
        executorService.execute(() -> diemList.postValue(repository.searchDiem(query)));
    }

    public void update(Diem diem) {
        executorService.execute(() -> repository.updateDiem(diem));
    }
}
