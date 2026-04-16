package com.example.quanlyhocsinhmobile.ui.dat;

import android.app.Application;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;
import com.example.quanlyhocsinhmobile.data.repository.GiaoVienRepository;
import com.example.quanlyhocsinhmobile.data.repository.LopRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LopViewModel extends AndroidViewModel {
    private final LopRepository repository;
    private final MutableLiveData<List<Lop>> allLop = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public LopViewModel(@NonNull Application application) {
        super(application);
        repository = new LopRepository(application);
        loadAllLops();
    }

    public LiveData<List<Lop>> getAllLops() {
        return allLop;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void loadAllLops() {
        executor.execute(() -> {
            List<Lop> lops = repository.getAllLop();
            allLop.postValue(lops);
        });
    }
    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllLops();
        } else {
            executor.execute(() -> {
                List<Lop> Lops = repository.search(query);
                allLop.postValue(Lops);
            });
        }
    }
    public void insert(String maLop, String tenLop, String maGVCN, String nienKhoa) {

        if (maLop.isEmpty() || tenLop.isEmpty() || maGVCN.isEmpty() || nienKhoa.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        executor.execute(() -> {

            // Check mã lớp
            if (repository.checkMaLop(maLop) > 0) {
                toastMessage.postValue("Mã lớp đã tồn tại!");
                return;
            }

            // Check tên lớp
            if (repository.checkTenLop(tenLop) > 0) {
                toastMessage.postValue("Tên lớp đã tồn tại!");
                return;
            }

            // Check giáo viên tồn tại
            if (repository.checkGVCNTonTai(maGVCN) == 0) {
                toastMessage.postValue("Giáo viên chủ nhiệm không tồn tại!");
                return;
            }

            // Check giáo viên đã làm GVCN chưa
            if (repository.checkGVCNTonTai(maGVCN) > 0) {
                toastMessage.postValue("Giáo viên này đã là GVCN lớp khác!");
                return;
            }

            // Check niên khóa hợp lệ (optional)
            if (!nienKhoa.matches("\\d{4}-\\d{4}")) {
                toastMessage.postValue("Niên khóa phải dạng yyyy-yyyy (VD: 2024-2025)");
                return;
            }

            Lop lop = new Lop(maLop, tenLop, nienKhoa, maGVCN);
            repository.insert(lop);

            toastMessage.postValue("Thêm lớp thành công");
            loadAllLops();
        });
    }
    public void update(Lop selectedLop, String tenLop, String maGVCN, String nienKhoa) {

        if (selectedLop == null) {
            toastMessage.setValue("Vui lòng chọn lớp để sửa");
            return;
        }

        if (tenLop.isEmpty() || maGVCN.isEmpty() || nienKhoa.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập đầy đủ thông tin");
            return;
        }

        executor.execute(() -> {

            // Check trùng tên lớp (trừ chính nó)
            if (!selectedLop.getTenLop().equals(tenLop)
                    && repository.checkTenLop(tenLop) > 0) {
                toastMessage.postValue("Tên lớp đã tồn tại!");
                return;
            }

            // Check giáo viên tồn tại
            if (repository.checkGVCNTonTai(maGVCN) == 0) {
                toastMessage.postValue("Giáo viên chủ nhiệm không tồn tại!");
                return;
            }

            // Check GVCN đã chủ nhiệm lớp khác chưa (trừ chính lớp đang sửa)
            if (!selectedLop.getMaGVCN().equals(maGVCN)
                    && repository.checkGVCNTonTai(maGVCN) > 0) {
                toastMessage.postValue("Giáo viên này đã là GVCN lớp khác!");
                return;
            }

            // Check định dạng niên khóa
            if (!nienKhoa.matches("\\d{4}-\\d{4}")) {
                toastMessage.postValue("Niên khóa phải dạng yyyy-yyyy (VD: 2024-2025)");
                return;
            }

            // Update dữ liệu
            selectedLop.setTenLop(tenLop);
            selectedLop.setMaGVCN(maGVCN);
            selectedLop.setNienKhoa(nienKhoa);

            repository.update(selectedLop);

            toastMessage.postValue("Cập nhật lớp thành công");
            loadAllLops();
        });
    }
    public void delete(Lop selectedLop) {
        if (selectedLop == null) {
            toastMessage.setValue("Vui lòng chọn lớp để xóa");
            return;
        }

        executor.execute(() -> {
            repository.delete(selectedLop);

            toastMessage.postValue("Xóa lớp thành công");
            loadAllLops();
        });
    }
}
