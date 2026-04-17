package com.example.quanlyhocsinhmobile.ui.zMainForm;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.utils.LoginActivity;
import com.example.quanlyhocsinhmobile.ui.dat.GiaoVienActivity;
import com.example.quanlyhocsinhmobile.ui.dat.LopActivity;
import com.example.quanlyhocsinhmobile.ui.dat.ToBoMonActivity;
import com.example.quanlyhocsinhmobile.ui.letrang.MonHocActivity;
import com.example.quanlyhocsinhmobile.ui.letrang.PhongHocActivity;
import com.example.quanlyhocsinhmobile.ui.letrang.TKBActivity;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;
public class MainForm extends AppCompatActivity {
    private PhanQuyen phanQuyen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_form);
        phanQuyen = PhanQuyen.getInstance(this);
        setupButtons();
        apDungPhanQuyen();
    }
    private void apDungPhanQuyen() {
        String quyen = phanQuyen.getQuyen();
        if ("GiaoVien".equals(quyen)) {
            findViewById(R.id.btn_lop_hoc).setVisibility(View.GONE);
            findViewById(R.id.btn_to_bo_mon).setVisibility(View.GONE);
            findViewById(R.id.btn_mon_hoc).setVisibility(View.GONE);
            findViewById(R.id.btn_thiet_bi).setVisibility(View.GONE);
            findViewById(R.id.btn_hoc_phi).setVisibility(View.GONE);
            findViewById(R.id.btn_tai_khoan).setVisibility(View.GONE);
            findViewById(R.id.btn_chinh_sach).setVisibility(View.GONE);
            Button btnGiaoVien = findViewById(R.id.btn_giao_vien);
            if (btnGiaoVien != null) {
                btnGiaoVien.setText("HỒ SƠ GIÁO VIÊN");
            }
        } else if ("HocSinh".equals(quyen)) {
            View tvHoso = findViewById(R.id.tv_Hoso);
            if (tvHoso != null) tvHoso.setVisibility(View.GONE);
            findViewById(R.id.btn_lop_hoc).setVisibility(View.GONE);
            findViewById(R.id.btn_giao_vien).setVisibility(View.GONE);
            findViewById(R.id.btn_to_bo_mon).setVisibility(View.GONE);
            findViewById(R.id.btn_mon_hoc).setVisibility(View.GONE);
            findViewById(R.id.btn_thiet_bi).setVisibility(View.GONE);
            findViewById(R.id.btn_tai_khoan).setVisibility(View.GONE);
            findViewById(R.id.btn_chinh_sach).setVisibility(View.GONE);
        }
    }
    private void setupButtons() {
        findViewById(R.id.btn_lop_hoc).setOnClickListener(v -> {
            navigateTo(LopActivity.class, "Lớp học");
        });
        findViewById(R.id.btn_giao_vien).setOnClickListener(v -> {
            navigateTo(GiaoVienActivity.class, "Giáo viên");
        });
        findViewById(R.id.btn_to_bo_mon).setOnClickListener(v -> {
            navigateTo(ToBoMonActivity.class, "Tổ bộ môn");
        });
        findViewById(R.id.btn_mon_hoc).setOnClickListener(v -> {
            navigateTo(MonHocActivity.class, "Môn học");
        });
        findViewById(R.id.btn_lich_day).setOnClickListener(v -> {
            navigateTo(TKBActivity.class, "Lịch dạy");
        });
        findViewById(R.id.btn_thiet_bi).setOnClickListener(v -> {
            navigateTo(PhongHocActivity.class, "Phòng học");
        });
        findViewById(R.id.btn_diem_so).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.tien.DiemActivity.class, "Điểm số");
        });
        findViewById(R.id.btn_hanh_kiem).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.tien.HanhKiemActivity.class, "Hạnh kiểm");
        });
        findViewById(R.id.btn_lich_thi).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.tien.LichThiActivity.class, "Lịch thi");
        });
        findViewById(R.id.btn_hoc_phi).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.hatrang.HocPhiActivity.class, "Học phí");
        });
        findViewById(R.id.btn_thong_bao).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.hatrang.ThongBaoActivity.class, "Thông báo");
        });
        findViewById(R.id.btn_phuc_khao).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.hatrang.PhucKhaoActivity.class, "Phúc khảo");
        });
        findViewById(R.id.btn_ho_so_hs).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.dai.HocSinhActivity.class, "Hồ sơ học sinh");
        });
        findViewById(R.id.btn_tai_khoan).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.dai.TaiKhoanActivity.class, "Tài khoản");
        });
        findViewById(R.id.btn_chinh_sach).setOnClickListener(v -> {
            navigateTo(com.example.quanlyhocsinhmobile.ui.dai.DoiTuongActivity.class, "Chính sách");
        });
        findViewById(R.id.btn_dang_xuat).setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có chắc muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        phanQuyen.dangXuat();
                        Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainForm.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
    }
    private void navigateTo(Class<?> activityClass, String featureName) {
        try {
            startActivity(new Intent(MainForm.this, activityClass));
        } catch (ActivityNotFoundException e) {
            showToast("Không mở được chức năng " + featureName + ".");
        } catch (Exception e) {
            showToast("Chức năng " + featureName + " đang gặp lỗi.");
        }
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}