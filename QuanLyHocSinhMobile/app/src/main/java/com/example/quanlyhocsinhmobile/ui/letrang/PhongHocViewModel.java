package com.example.quanlyhocsinhmobile.ui.letrang;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import com.example.quanlyhocsinhmobile.data.repository.PhongHocRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class PhongHocViewModel extends AndroidViewModel {
    private final PhongHocRepository repository;
    private final MutableLiveData<List<PhongHoc>> filteredPhongHocs = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public PhongHocViewModel(@NonNull Application application) {
        super(application);
        repository = new PhongHocRepository(application);
        loadData("Tất cả");
    }
    public LiveData<List<PhongHoc>> getFilteredPhongHocs() {
        return filteredPhongHocs;
    }
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
    public void loadData(String selectedType) {
        executor.execute(() -> {
            List<PhongHoc> all = repository.getAllPhongHoc();
            if (selectedType.equals("Tất cả")) {
                filteredPhongHocs.postValue(all);
            } else {
                List<PhongHoc> filtered = new ArrayList<>();
                for (PhongHoc p : all) {
                    if (p.getLoaiPhong().equals(selectedType)) {
                        filtered.add(p);
                    }
                }
                filteredPhongHocs.postValue(filtered);
            }
        });
    }
    public void insert(String ma, String ten, String sucChuaStr, String loai) {
        if (ma.isEmpty() || ten.isEmpty() || sucChuaStr.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        executor.execute(() -> {
            if (repository.checkMaPhong(ma) > 0) {
                toastMessage.postValue("Mã phòng đã tồn tại!");
                return;
            }
            if (repository.checkTenPhong(ten) > 0) {
                toastMessage.postValue("Tên phòng đã tồn tại!");
                return;
            }
            PhongHoc phongHoc = new PhongHoc();
            phongHoc.setMaPhong(ma);
            phongHoc.setTenPhong(ten);
            phongHoc.setSucChua(Integer.parseInt(sucChuaStr));
            phongHoc.setLoaiPhong(loai);
            phongHoc.setTinhTrang("Trống");
            repository.insert(phongHoc);
            toastMessage.postValue("Thêm phòng học thành công");
            loadData("Tất cả");
        });
    }
    public void update(PhongHoc selectedPhongHoc, String ten, String sucChuaStr, String loai) {
        if (selectedPhongHoc == null) {
            toastMessage.setValue("Vui lòng chọn phòng học để sửa");
            return;
        }
        if (ten.isEmpty() || sucChuaStr.isEmpty()) {
            toastMessage.setValue("Thông tin không được để trống");
            return;
        }
        executor.execute(() -> {
            if (!selectedPhongHoc.getTenPhong().equals(ten) && repository.checkTenPhong(ten) > 0) {
                toastMessage.postValue("Tên phòng đã tồn tại!");
                return;
            }
            selectedPhongHoc.setTenPhong(ten);
            selectedPhongHoc.setSucChua(Integer.parseInt(sucChuaStr));
            selectedPhongHoc.setLoaiPhong(loai);
            repository.update(selectedPhongHoc);
            toastMessage.postValue("Cập nhật thành công");
            loadData("Tất cả");
        });
    }
    public void delete(PhongHoc selectedPhongHoc) {
        if (selectedPhongHoc == null) {
            toastMessage.setValue("Vui lòng chọn phòng học để xóa");
            return;
        }
        executor.execute(() -> {
            repository.delete(selectedPhongHoc);
            toastMessage.postValue("Xóa phòng học thành công");
            loadData("Tất cả");
        });
    }
}