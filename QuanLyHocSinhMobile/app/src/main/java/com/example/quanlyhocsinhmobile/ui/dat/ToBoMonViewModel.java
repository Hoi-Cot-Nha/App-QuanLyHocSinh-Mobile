package com.example.quanlyhocsinhmobile.ui.dat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;
import com.example.quanlyhocsinhmobile.data.repository.ToBoHopRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ToBoMonViewModel extends AndroidViewModel {

    private final ToBoHopRepository repository;
    private final MutableLiveData<List<ToHopMon>>  allToHop= new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public ToBoMonViewModel(@NonNull Application application) {
        super(application);
        repository = new ToBoHopRepository(application);
        loadAllToHops();
    }

    public LiveData<List<ToHopMon>> getAllToHop() {
        return allToHop;
    }
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
    public void loadAllToHops() {
        executor.execute(() -> {
            List<ToHopMon> toHopMons = repository.getAllToHop();
            allToHop.postValue(toHopMons);
        });
    }
    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllToHops();
        } else {
            executor.execute(() -> {
                List<ToHopMon> toHopMons = repository.search(query);
                allToHop.postValue(toHopMons);
            });
        }
    }
    public void insert(String maToHop, String tenToHop) {
        if (maToHop.isEmpty() || tenToHop.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        executor.execute(() -> {
            if (repository.checkMaToHop(maToHop) > 0) {
                toastMessage.postValue("Mã tổ hợp đã tồn tại!");
                return;
            }

            if (repository.checkTenToHop(tenToHop) > 0) {
                toastMessage.postValue("Tên tổ hợp đã tồn tại!");
                return;
            }

            ToHopMon toHopMon = new ToHopMon(maToHop, tenToHop);
            repository.insert(toHopMon);

            toastMessage.postValue("Thêm tổ bộ môn thành công");
            loadAllToHops();
        });
    }

    public void update(ToHopMon selectedToHop, String tenToHop) {
        if (selectedToHop == null) {
            toastMessage.setValue("Vui lòng chọn tổ bộ môn để sửa");
            return;
        }

        if (tenToHop.isEmpty()) {
            toastMessage.setValue("Tên tổ bộ môn không được để trống");
            return;
        }

        executor.execute(() -> {
            if (!selectedToHop.getTenToHop().equals(tenToHop)
                    && repository.checkTenToHop(tenToHop) > 0) {
                toastMessage.postValue("Tên tổ bộ môn đã tồn tại!");
                return;
            }

            selectedToHop.setTenToHop(tenToHop);
            repository.update(selectedToHop);

            toastMessage.postValue("Cập nhật thành công");
            loadAllToHops();
        });
    }

    public void delete(ToHopMon selectedToHop) {
        if (selectedToHop == null) {
            toastMessage.setValue("Vui lòng chọn tổ bộ môn để xóa");
            return;
        }

        executor.execute(() -> {
            repository.delete(selectedToHop);
            toastMessage.postValue("Xóa tổ bộ môn thành công");
            loadAllToHops();
        });
    }


}
