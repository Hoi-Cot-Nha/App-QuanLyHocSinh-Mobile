package com.example.quanlyhocsinhmobile.ui.dai;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.DoiTuongUuTien;
import com.example.quanlyhocsinhmobile.data.repository.DoiTuongRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class DoiTuongViewModel extends AndroidViewModel {
    private final DoiTuongRepository repository;
    private final MutableLiveData<List<DoiTuongUuTien>> allDoiTuongs = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public DoiTuongViewModel(@NonNull Application application) {
        super(application);
        repository = new DoiTuongRepository(application);
        loadAllDoiTuongs();
    }
    public LiveData<List<DoiTuongUuTien>> getAllDoiTuongs() {
        return allDoiTuongs;
    }
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
    public void loadAllDoiTuongs() {
        executor.execute(() -> {
            List<DoiTuongUuTien> doiTuongs = repository.getAllDoiTuong();
            allDoiTuongs.postValue(doiTuongs);
        });
    }
    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllDoiTuongs();
        } else {
            executor.execute(() -> {
                List<DoiTuongUuTien> doiTuongs = repository.searchDoiTuong(query);
                allDoiTuongs.postValue(doiTuongs);
            });
        }
    }
    public void insert(String ma, String ten, String tiLeStr) {
        if (ma.isEmpty() || ten.isEmpty() || tiLeStr.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        try {
            Double tiLe = Double.parseDouble(tiLeStr);
            if (tiLe < 0 || tiLe > 1) {
                toastMessage.setValue("Tỉ lệ phải nằm trong khoảng 0 → 1");
                return;
            }
            executor.execute(() -> {
                if (repository.checkMaDT(ma) > 0) {
                    toastMessage.postValue("Mã đối tượng đã tồn tại!");
                    return;
                }
                if (repository.checkTenDT(ten) > 0) {
                    toastMessage.postValue("Tên đối tượng đã tồn tại!");
                    return;
                }
                DoiTuongUuTien doiTuong = new DoiTuongUuTien();
                doiTuong.setMaDT(ma);
                doiTuong.setTenDT(ten);
                doiTuong.setTiLeGiamHocPhi(tiLe);
                repository.insert(doiTuong);
                toastMessage.postValue("Thêm đối tượng thành công");
                loadAllDoiTuongs();
            });
        } catch (NumberFormatException e) {
            toastMessage.setValue("Tỉ lệ giảm phải là một số thực (ví dụ: 0.5)");
        }
    }
    public void update(DoiTuongUuTien selectedDT, String ten, String tiLeStr) {
        if (selectedDT == null) {
            toastMessage.setValue("Vui lòng chọn đối tượng để sửa");
            return;
        }
        if (ten.isEmpty() || tiLeStr.isEmpty()) {
            toastMessage.setValue("Thông tin không được để trống");
            return;
        }
        try {
            Double tiLe = Double.parseDouble(tiLeStr);
            if (tiLe < 0 || tiLe > 1) {
                toastMessage.setValue("Tỉ lệ phải nằm trong khoảng 0 → 1");
                return;
            }
            executor.execute(() -> {
                if (!selectedDT.getTenDT().equals(ten) && repository.checkTenDT(ten) > 0) {
                    toastMessage.postValue("Tên đối tượng đã tồn tại!");
                    return;
                }
                selectedDT.setTenDT(ten);
                selectedDT.setTiLeGiamHocPhi(tiLe);
                repository.update(selectedDT);
                toastMessage.postValue("Cập nhật thành công");
                loadAllDoiTuongs();
            });
        } catch (NumberFormatException e) {
            toastMessage.setValue("Tỉ lệ giảm không hợp lệ");
        }
    }
    public void delete(DoiTuongUuTien selectedDT) {
        if (selectedDT == null) {
            toastMessage.setValue("Vui lòng chọn đối tượng để xóa");
            return;
        }
        executor.execute(() -> {
            repository.delete(selectedDT);
            toastMessage.postValue("Xóa đối tượng thành công");
            loadAllDoiTuongs();
        });
    }
}