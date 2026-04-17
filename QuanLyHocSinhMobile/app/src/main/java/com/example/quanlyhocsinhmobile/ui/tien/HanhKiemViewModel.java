package com.example.quanlyhocsinhmobile.ui.tien;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.HanhKiem;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.repository.HanhKiemRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class HanhKiemViewModel extends AndroidViewModel {
    private final HanhKiemRepository repository;
    private final MutableLiveData<List<HanhKiem.Display>> hanhKiemList = new MutableLiveData<>();
    private final MutableLiveData<List<Lop>> lopList = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    public HanhKiemViewModel(@NonNull Application application) {
        super(application);
        repository = new HanhKiemRepository(application);
        loadLops();
    }
    public LiveData<List<HanhKiem.Display>> getHanhKiemList() {
        return hanhKiemList;
    }
    public LiveData<List<Lop>> getLopList() {
        return lopList;
    }
    private void loadLops() {
        executorService.execute(() -> {
            lopList.postValue(repository.getAllLop());
            hanhKiemList.postValue(repository.filterHanhKiem("", 0, ""));
        });
    }
    public void filter(String maLop, int hocKy, String namHoc) {
        executorService.execute(() -> hanhKiemList.postValue(repository.filterHanhKiem(maLop, hocKy, namHoc)));
    }
    public void search(String query) {
        executorService.execute(() -> hanhKiemList.postValue(repository.searchHanhKiem(query)));
    }
    public void update(HanhKiem hanhKiem) {
        executorService.execute(() -> {
            repository.updateHanhKiem(hanhKiem);
        });
    }
}