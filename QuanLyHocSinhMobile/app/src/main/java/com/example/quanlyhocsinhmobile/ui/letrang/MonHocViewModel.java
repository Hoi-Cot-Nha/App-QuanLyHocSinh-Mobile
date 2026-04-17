package com.example.quanlyhocsinhmobile.ui.letrang;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.repository.MonHocRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class MonHocViewModel extends AndroidViewModel {
    private final MonHocRepository repository;
    private final MutableLiveData<List<MonHoc>> allMonHocs = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public MonHocViewModel(@NonNull Application application) {
        super(application);
        repository = new MonHocRepository(application);
        loadAllMonHocs();
    }
    public LiveData<List<MonHoc>> getAllMonHocs() {
        return allMonHocs;
    }
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
    public void loadAllMonHocs() {
        executor.execute(() -> {
            List<MonHoc> monHocs = repository.getAllMonHoc();
            allMonHocs.postValue(monHocs);
        });
    }
    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllMonHocs();
        } else {
            executor.execute(() -> {
                List<MonHoc> monHocs = repository.searchMonHoc(query);
                allMonHocs.postValue(monHocs);
            });
        }
    }
    public void insert(String ma, String ten) {
        if (ma.isEmpty() || ten.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        executor.execute(() -> {
            if (repository.checkMaMonHoc(ma) > 0) {
                toastMessage.postValue("Mã môn học đã tồn tại!");
                return;
            }
            if (repository.checkTenMonHoc(ten) > 0) {
                toastMessage.postValue("Tên môn học đã tồn tại!");
                return;
            }
            MonHoc monHoc = new MonHoc(ma, ten);
            repository.insert(monHoc);
            toastMessage.postValue("Thêm môn học thành công");
            loadAllMonHocs();
        });
    }
    public void update(MonHoc selectedMonHoc, String ten) {
        if (selectedMonHoc == null) {
            toastMessage.setValue("Vui lòng chọn môn học để sửa");
            return;
        }
        if (ten.isEmpty()) {
            toastMessage.setValue("Tên môn học không được để trống");
            return;
        }
        executor.execute(() -> {
            if (!selectedMonHoc.getTenMH().equals(ten) && repository.checkTenMonHoc(ten) > 0) {
                toastMessage.postValue("Tên môn học đã tồn tại!");
                return;
            }
            selectedMonHoc.setTenMH(ten);
            repository.update(selectedMonHoc);
            toastMessage.postValue("Cập nhật thành công");
            loadAllMonHocs();
        });
    }
    public void delete(MonHoc selectedMonHoc) {
        if (selectedMonHoc == null) {
            toastMessage.setValue("Vui lòng chọn môn học để xóa");
            return;
        }
        executor.execute(() -> {
            repository.delete(selectedMonHoc);
            toastMessage.postValue("Xóa môn học thành công");
            loadAllMonHocs();
        });
    }
}