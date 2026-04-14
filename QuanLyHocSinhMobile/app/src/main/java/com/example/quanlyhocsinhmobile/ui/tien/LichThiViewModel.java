package com.example.quanlyhocsinhmobile.ui.tien;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.LichThi;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import com.example.quanlyhocsinhmobile.data.repository.LichThiRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LichThiViewModel extends AndroidViewModel {
    private final LichThiRepository repository;
    private final MutableLiveData<List<LichThi.Display>> lichThiList = new MutableLiveData<>();
    private final MutableLiveData<List<MonHoc>> monHocList = new MutableLiveData<>();
    private final MutableLiveData<List<PhongHoc>> phongHocList = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LichThiViewModel(@NonNull Application application) {
        super(application);
        repository = new LichThiRepository(application);
        loadInitialData();
    }

    public LiveData<List<LichThi.Display>> getLichThiList() {
        return lichThiList;
    }

    public LiveData<List<MonHoc>> getMonHocList() {
        return monHocList;
    }

    public LiveData<List<PhongHoc>> getPhongHocList() {
        return phongHocList;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    private void loadInitialData() {
        executorService.execute(() -> {
            monHocList.postValue(repository.getAllMonHoc());
            phongHocList.postValue(repository.getAllPhongHoc());
            loadAll();
        });
    }

    public void loadAll() {
        executorService.execute(() -> {
            lichThiList.postValue(repository.getAllLichThi());
        });
    }

    public void filter(String maMH, String maPhong) {
        executorService.execute(() -> {
            lichThiList.postValue(repository.filterLichThi(maMH, maPhong));
        });
    }

    public void search(String query) {
        executorService.execute(() -> {
            lichThiList.postValue(repository.searchLichThi(query));
        });
    }

    public void insert(LichThi lichThi) {
        executorService.execute(() -> {
            if (repository.hasConflict(lichThi)) {
                toastMessage.postValue("Phòng thi đã có lịch trong khung giờ này");
                return;
            }
            repository.insert(lichThi);
            toastMessage.postValue("Thêm lịch thi thành công");
            loadAll();
        });
    }

    public void update(LichThi lichThi) {
        executorService.execute(() -> {
            if (repository.hasConflictExcludeId(lichThi)) {
                toastMessage.postValue("Phòng thi đã có lịch trong khung giờ này");
                return;
            }
            repository.update(lichThi);
            toastMessage.postValue("Cập nhật lịch thi thành công");
            loadAll();
        });
    }

    public void delete(LichThi lichThi) {
        executorService.execute(() -> {
            repository.delete(lichThi);
            toastMessage.postValue("Xóa lịch thi thành công");
            loadAll();
        });
    }
}
