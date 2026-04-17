package com.example.quanlyhocsinhmobile.ui.dai;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.quanlyhocsinhmobile.data.local.Model.TaiKhoan;
import com.example.quanlyhocsinhmobile.data.repository.TaiKhoanRepository;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class TaiKhoanViewModel extends AndroidViewModel {
    private final TaiKhoanRepository repository;
    private final MutableLiveData<List<TaiKhoan>> allTaiKhoans = new MutableLiveData<>();
    private final MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private final MutableLiveData<TaiKhoan> loginResult = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public TaiKhoanViewModel(@NonNull Application application) {
        super(application);
        repository = new TaiKhoanRepository(application);
        loadAllTaiKhoans();
    }
    public LiveData<List<TaiKhoan>> getAllTaiKhoans() {
        return allTaiKhoans;
    }
    public LiveData<String> getToastMessage() {
        return toastMessage;
    }
    public LiveData<TaiKhoan> getLoginResult() {
        return loginResult;
    }
    public void loadAllTaiKhoans() {
        executor.execute(() -> {
            List<TaiKhoan> list = repository.getAllTaiKhoan();
            allTaiKhoans.postValue(list);
        });
    }
    public void search(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadAllTaiKhoans();
        } else {
            executor.execute(() -> {
                List<TaiKhoan> list = repository.searchTaiKhoan(query);
                allTaiKhoans.postValue(list);
            });
        }
    }
    public void insert(String user, String pass, String quyen, String maND) {
        if (user.isEmpty() || pass.isEmpty() || quyen.isEmpty()) {
            toastMessage.setValue("Tên đăng nhập, mật khẩu và quyền không được để trống");
            return;
        }
        if (!quyen.equals("admin") && (maND == null || maND.isEmpty())) {
            toastMessage.setValue("Vui lòng chọn mã người dùng");
            return;
        }
        executor.execute(() -> {
            if (repository.checkTenDangNhap(user) > 0) {
                toastMessage.postValue("Tên đăng nhập đã tồn tại!");
                return;
            }
            TaiKhoan tk = new TaiKhoan();
            tk.setTenDangNhap(user);
            tk.setMatKhau(pass);
            tk.setQuyen(quyen);
            if (quyen.equals("admin")) {
                tk.setMaNguoiDung(null);
            } else {
                tk.setMaNguoiDung(maND);
            }
            repository.insert(tk);
            toastMessage.postValue("Thêm tài khoản thành công");
            loadAllTaiKhoans();
        });
    }
    public void update(TaiKhoan selectedTK, String pass, String quyen, String maND) {
        if (selectedTK == null) {
            toastMessage.setValue("Vui lòng chọn tài khoản để sửa");
            return;
        }
        if (pass.isEmpty() || quyen.isEmpty()) {
            toastMessage.setValue("Mật khẩu và quyền không được để trống");
            return;
        }
        if (!quyen.equals("admin") && (maND == null || maND.isEmpty())) {
            toastMessage.setValue("Vui lòng chọn mã người dùng");
            return;
        }
        executor.execute(() -> {
            selectedTK.setMatKhau(pass);
            selectedTK.setQuyen(quyen);
            if (quyen.equals("admin")) {
                selectedTK.setMaNguoiDung(null);
            } else {
                selectedTK.setMaNguoiDung(maND);
            }
            repository.update(selectedTK);
            toastMessage.postValue("Cập nhật thành công");
            loadAllTaiKhoans();
        });
    }
    public void delete(TaiKhoan selectedTK) {
        if (selectedTK == null) {
            toastMessage.setValue("Vui lòng chọn tài khoản để xóa");
            return;
        }
        executor.execute(() -> {
            repository.delete(selectedTK);
            toastMessage.postValue("Xóa tài khoản thành công");
            loadAllTaiKhoans();
        });
    }
    public void login(String user, String pass) {
        if (user.isEmpty() || pass.isEmpty()) {
            toastMessage.setValue("Vui lòng nhập tài khoản và mật khẩu");
            return;
        }
        executor.execute(() -> {
            TaiKhoan tk = repository.login(user, pass);
            if (tk != null) {
                loginResult.postValue(tk);
                toastMessage.postValue("Đăng nhập thành công!");
            } else {
                loginResult.postValue(null);
                toastMessage.postValue("Sai tài khoản hoặc mật khẩu");
            }
        });
    }
    public List<String> getAllMaGiaoVien() {
        return repository.getAllMaGV();
    }
    public List<String> getAllMaHocSinh() {
        return repository.getAllMaHS();
    }
}