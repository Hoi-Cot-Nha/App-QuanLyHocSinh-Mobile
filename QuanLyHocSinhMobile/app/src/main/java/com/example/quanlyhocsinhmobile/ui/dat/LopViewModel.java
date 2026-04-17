package com.example.quanlyhocsinhmobile.ui.dat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.repository.LopRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LopViewModel extends AndroidViewModel {
    private final LopRepository repository;
    private final MutableLiveData<List<Lop.Display>> allLop = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    // ✅ Thêm LiveData cho Spinner
    private final MutableLiveData<List<String>> nienKhoaList = new MutableLiveData<>();
    private final MutableLiveData<List<com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO.GiaoVienInfo>> giaoVienList = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public LopViewModel(@NonNull Application application) {
        super(application);
        repository = new LopRepository(application);
        loadAllLops();
    }

    public LiveData<List<Lop.Display>> getAllLops() {
        return allLop;
    }

    public LiveData<String> getToastMessage() {
        return toastMessage;
    }

    // ✅ Getter cho Niên khóa & Giáo viên
    public LiveData<List<String>> getNienKhoaList() {
        return nienKhoaList;
    }

    public LiveData<List<com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO.GiaoVienInfo>> getGiaoVienList() {
        return giaoVienList;
    }

    // ✅ Load dữ liệu cho Spinner
    public void loadSpinnerData() {
        executor.execute(() -> {
            List<String> nienKhoas = repository.getAllNienKhoa();
            List<com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO.GiaoVienInfo> giaoViens = repository.getAllGiaoVienForLop();

            // Thêm tùy chọn mặc định
            if (nienKhoas != null && nienKhoas.isEmpty()) {
                nienKhoas.add(0, "-- Chọn niên khóa --");
            }
            if (giaoViens != null && giaoViens.isEmpty()) {
                giaoViens.add(0, new com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO.GiaoVienInfo("", "-- Chọn giáo viên --"));
            }

            nienKhoaList.postValue(nienKhoas);
            giaoVienList.postValue(giaoViens);
        });
    }

    public void loadAllLops() {
        executor.execute(() -> {
            List<Lop.Display> lops = repository.getAllLop();
            allLop.postValue(lops);
        });
    }
    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllLops();
        } else {
            executor.execute(() -> {
                List<Lop.Display> lops = repository.search(query);
                allLop.postValue(lops);
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
            if (repository.checkGiaoVienTonTai(maGVCN) == 0) {
                toastMessage.postValue("Giáo viên chủ nhiệm không tồn tại!");
                return;
            }

            // Check giáo viên đã làm GVCN chưa
            if (repository.checkGVCNDaPhanCong(maGVCN) > 0) {
                toastMessage.postValue("Giáo viên này đã là GVCN lớp khác!");
                return;
            }

            // Check niên khóa hợp lệ (optional)
            if (!nienKhoa.matches("\\d{4}-\\d{4}")) {
                toastMessage.postValue("Niên khóa phải dạng yyyy-yyyy (VD: 2024-2025)");
                return;
            }

            // Constructor Lop nhận thứ tự: (maLop, tenLop, maGVCN, nienKhoa)
            Lop lop = new Lop(maLop, tenLop, maGVCN, nienKhoa);
            repository.insertAndWait(lop); // Đợi insert xong rồi mới load

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
            if (repository.checkGiaoVienTonTai(maGVCN) == 0) {
                toastMessage.postValue("Giáo viên chủ nhiệm không tồn tại!");
                return;
            }

            // Check GVCN đã chủ nhiệm lớp khác chưa (trừ chính lớp đang sửa)
            if (!selectedLop.getMaGVCN().equals(maGVCN)
                    && repository.checkGVCNDaPhanCong(maGVCN) > 0) {
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

            repository.updateAndWait(selectedLop); // Đợi update xong rồi mới load

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
            repository.deleteAndWait(selectedLop); // Đợi delete xong rồi mới load

            toastMessage.postValue("Xóa lớp thành công");
            loadAllLops();
        });
    }
}
