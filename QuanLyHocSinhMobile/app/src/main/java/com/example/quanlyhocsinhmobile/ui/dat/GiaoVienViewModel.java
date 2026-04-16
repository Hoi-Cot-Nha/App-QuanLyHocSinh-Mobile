package com.example.quanlyhocsinhmobile.ui.dat;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;
import com.example.quanlyhocsinhmobile.data.repository.GiaoVienRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GiaoVienViewModel extends AndroidViewModel {
    private final GiaoVienRepository repository;
    private final MutableLiveData<List<GiaoVien.Display>> allGiaoVien = new MutableLiveData<>();
    private final MutableLiveData<List<MonHoc>> monHocList = new MutableLiveData<>();
    private final MutableLiveData<List<ToHopMon>> toHopMonList = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public GiaoVienViewModel(@NonNull Application application) {
        super(application);
        repository = new GiaoVienRepository(application);
        loadAllGiaoViens();
    }
    public LiveData<List<GiaoVien.Display>> getAllGiaoViens() {
        return allGiaoVien;
    }
    public LiveData<List<MonHoc>> getMonHocList() {
        return monHocList;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public LiveData<List<ToHopMon>> getToBoMonList(){ return toHopMonList;}

     private void loadInitialData(){
         executor.execute(() -> {
             monHocList.postValue(repository.getAllMonHoc());
             toHopMonList.postValue(repository.getAllToHop());
             loadAllGiaoViens();
         });
     }

    public void loadAllGiaoViens() {
        executor.execute(() -> {
            List<GiaoVien.Display> giaoViens = repository.getAllGiaoVien();
            allGiaoVien.postValue(giaoViens);
        });
    }

    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllGiaoViens();
        } else {
            executor.execute(() -> {
                List<GiaoVien.Display> giaoViens = repository.search(query);
                allGiaoVien.postValue(giaoViens);
            });
        }
    }

    public void insert(String maGV, String tenGV, String ngaySinh, String sdt, String tenMaToHop, String tenMon) {
        if (maGV.isEmpty() || tenGV.isEmpty() || ngaySinh.isEmpty() || sdt.isEmpty() ||tenMaToHop.isEmpty() ||tenMon.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        executor.execute(() -> {
            if (repository.checkMaGiaoVien(maGV) > 0) {
                toastMessage.postValue("Mã giáo viên đã tồn tại!");
                return;
            }

            if (repository.checkSDT(sdt) > 0) {
                toastMessage.postValue("Số điện thoại đã tồn tại!");
                return;
            }

            GiaoVien giaoVien = new GiaoVien(maGV, tenGV, ngaySinh, sdt, tenMaToHop, tenMon);
            repository.insert(giaoVien);
            toastMessage.postValue("Thêm Giáo viên thành công");
            loadAllGiaoViens();

        });
    }
    public void update(GiaoVien selectedGiaoVien, String ten) {
        if (selectedGiaoVien == null) {
            toastMessage.setValue("Vui lòng chọn giáo viên để sửa");
            return;
        }
        if (ten.isEmpty()) {
            toastMessage.setValue("Tên giáo viên không được để trống");
            return;
        }

        executor.execute(() -> {
            if (!selectedGiaoVien.getMaGV().equals(ten) && repository.checkMaGiaoVien(ten) > 0) {
                toastMessage.postValue("Tên môn học đã tồn tại!");
                return;
            }

            selectedGiaoVien.setMaGV(ten);
            repository.update(selectedGiaoVien);
            toastMessage.postValue("Cập nhật thành công");
            loadAllGiaoViens();
        });
    }
    public void delete(GiaoVien selectedGiaoVien) {
        if (selectedGiaoVien == null) {
            toastMessage.setValue("Vui lòng chọn giáo viên để xoá ");
            return;
        }

        executor.execute(() -> {
            repository.delete(selectedGiaoVien);
            toastMessage.postValue("Xóa giáo viên thành công");
            loadAllGiaoViens();
        });
    }

}
