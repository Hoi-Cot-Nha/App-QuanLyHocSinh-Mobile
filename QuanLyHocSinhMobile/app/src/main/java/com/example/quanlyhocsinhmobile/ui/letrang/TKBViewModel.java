package com.example.quanlyhocsinhmobile.ui.letrang;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.ThoiKhoaBieu;
import com.example.quanlyhocsinhmobile.data.repository.TKBRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TKBViewModel extends AndroidViewModel {
    private final TKBRepository repository;
    private final MutableLiveData<List<ThoiKhoaBieu.Display>> tkbList = new MutableLiveData<>();
    private final MutableLiveData<List<Lop>> lops = new MutableLiveData<>();
    private final MutableLiveData<List<MonHoc>> monHocs = new MutableLiveData<>();
    private final MutableLiveData<List<GiaoVien.Display>> giaoViens = new MutableLiveData<>();
    private final MutableLiveData<List<PhongHoc>> phongHocs = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final MutableLiveData<String> maLopHocSinh = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TKBViewModel(@NonNull Application application) {
        super(application);
        repository = new TKBRepository(application);
        loadMasterData();
    }

    public LiveData<List<ThoiKhoaBieu.Display>> getTkbList() { return tkbList; }
    public LiveData<List<Lop>> getLops() { return lops; }
    public LiveData<List<MonHoc>> getMonHocs() { return monHocs; }
    public LiveData<List<GiaoVien.Display>> getGiaoViens() { return giaoViens; }
    public LiveData<List<PhongHoc>> getPhongHocs() { return phongHocs; }
    public LiveData<String> getToastMessage() { return toastMessage; }
    public LiveData<String> getMaLopHocSinh() { return maLopHocSinh; }

    private void loadMasterData() {
        executor.execute(() -> {
            lops.postValue(repository.getAllLop());
            monHocs.postValue(repository.getAllMonHoc());
            giaoViens.postValue(repository.getAllGiaoVien());
            phongHocs.postValue(repository.getAllPhongHoc());
            loadTKB(0, "", "");
        });
    }

    public void loadMaLopHocSinh(String maHS) {
        executor.execute(() -> {
            HocSinh hs = AppDatabase.getDatabase(getApplication()).hocSinhDAO().getHocSinhByMa(maHS);
            if (hs != null) {
                maLopHocSinh.postValue(hs.getMaLop());
            }
        });
    }

    public void loadTKB(int thu, String maLop, String maMH) {
        executor.execute(() -> {
            tkbList.postValue(repository.filterTKB(thu, maLop, maMH));
        });
    }

    public void insert(ThoiKhoaBieu tkb) {
        executor.execute(() -> {
            if (repository.checkOverlap(0, tkb.getThu(), tkb.getMaLop(), tkb.getMaGV(), tkb.getMaPhong(), 
                                      tkb.getTietBatDau(), tkb.getTietKetThuc()) > 0) {
                toastMessage.postValue("Trùng lịch! Lớp, Giáo viên hoặc Phòng đã có lịch trong khoảng tiết này.");
                return;
            }
            repository.insert(tkb);
            toastMessage.postValue("Thêm mới thành công");
            loadTKB(0, "", "");
        });
    }

    public void update(ThoiKhoaBieu tkb) {
        executor.execute(() -> {
            if (repository.checkOverlap(tkb.getMaTKB(), tkb.getThu(), tkb.getMaLop(), tkb.getMaGV(), tkb.getMaPhong(), 
                                      tkb.getTietBatDau(), tkb.getTietKetThuc()) > 0) {
                toastMessage.postValue("Trùng lịch! Khoảng tiết mới xung đột với lịch đã có.");
                return;
            }
            repository.update(tkb);
            toastMessage.postValue("Cập nhật thành công");
            loadTKB(0, "", "");
        });
    }

    public void delete(ThoiKhoaBieu tkb) {
        if (tkb == null) {
            toastMessage.setValue("Vui lòng chọn một dòng để xóa");
            return;
        }
        executor.execute(() -> {
            repository.delete(tkb);
            toastMessage.postValue("Đã xóa thành công");
            loadTKB(0, "", "");
        });
    }
}
