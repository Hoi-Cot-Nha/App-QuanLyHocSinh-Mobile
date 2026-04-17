package com.example.quanlyhocsinhmobile.ui.dai;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import com.example.quanlyhocsinhmobile.data.repository.HocSinhRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class HocSinhViewModel extends AndroidViewModel {
    private final HocSinhRepository repo;
    private final MutableLiveData<List<HocSinh>> list = new MutableLiveData<>();
    private final MutableLiveData<String> msg = new MutableLiveData<>();
    private final ExecutorService ex = Executors.newSingleThreadExecutor();
    public HocSinhViewModel(@NonNull Application app) {
        super(app);
        repo = new HocSinhRepository(app);
        loadAll();
    }
    public LiveData<List<HocSinh>> getList() {
        return list;
    }
    public LiveData<String> getMsg() {
        return msg;
    }
    public void loadAll() {
        ex.execute(() -> list.postValue(repo.getAll()));
    }
    public void search(String q) {
        if (q == null || q.trim().isEmpty()) {
            loadAll();
        } else {
            ex.execute(() -> list.postValue(repo.search(q)));
        }
    }
    public void insert(HocSinh hs) {
        if (hs == null ||
                hs.getMaHS().trim().isEmpty() ||
                hs.getHoTen().trim().isEmpty()) {
            msg.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }
        ex.execute(() -> {
            if (repo.checkMa(hs.getMaHS()) > 0) {
                msg.postValue("Mã học sinh đã tồn tại!");
                return;
            }
            if (repo.checkTen(hs.getHoTen()) > 0) {
                msg.postValue("Tên học sinh đã tồn tại!");
                return;
            }
            repo.insert(hs);
            msg.postValue("Thêm học sinh thành công");
            loadAll();
        });
    }
    public void update(HocSinh hs) {
        if (hs == null) {
            msg.setValue("Vui lòng chọn học sinh để sửa");
            return;
        }
        if (hs.getHoTen().trim().isEmpty()) {
            msg.setValue("Tên học sinh không được để trống");
            return;
        }
        ex.execute(() -> {
            repo.update(hs);
            msg.postValue("Cập nhật thành công");
            loadAll();
        });
    }
    public void delete(HocSinh hs) {
        if (hs == null) {
            msg.setValue("Vui lòng chọn học sinh để xóa");
            return;
        }
        ex.execute(() -> {
            repo.delete(hs);
            msg.postValue("Xóa học sinh thành công");
            loadAll();
        });
    }
}