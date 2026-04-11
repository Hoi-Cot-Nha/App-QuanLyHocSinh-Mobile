package com.example.quanlyhocsinhmobile.MainForm;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyhocsinhmobile.R;

public class MainForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_form);

        setupButtons();
    }

    private void setupButtons() {
        // Group 1
        findViewById(R.id.btn_lop_hoc).setOnClickListener(v -> showToast("Quản lý lớp học"));
        findViewById(R.id.btn_giao_vien).setOnClickListener(v -> showToast("Quản lý giáo viên"));
        findViewById(R.id.btn_to_bo_mon).setOnClickListener(v -> showToast("Quản lý tổ bộ môn"));

        // Group 2
        findViewById(R.id.btn_mon_hoc).setOnClickListener(v -> showToast("Quản lý môn học"));
        findViewById(R.id.btn_lich_day).setOnClickListener(v -> showToast("Thời khóa biểu / Lịch dạy"));
        findViewById(R.id.btn_thiet_bi).setOnClickListener(v -> showToast("Phòng học & Thiết bị"));

        // Group 3
        findViewById(R.id.btn_diem_so).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainForm.this, com.example.quanlyhocsinhmobile.ui.tien.DiemUI.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_hanh_kiem).setOnClickListener(v -> showToast("Quản lý hạnh kiểm"));
        findViewById(R.id.btn_lich_thi).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainForm.this, com.example.quanlyhocsinhmobile.ui.tien.LichThiUI.class);
            startActivity(intent);
        });

        // Group 4
        findViewById(R.id.btn_hoc_phi).setOnClickListener(v -> showToast("Quản lý học phí"));
        findViewById(R.id.btn_thong_bao).setOnClickListener(v -> showToast("Quản lý thông báo"));
        findViewById(R.id.btn_phuc_khao).setOnClickListener(v -> showToast("Quản lý phúc khảo"));

        // Group 5
        findViewById(R.id.btn_ho_so_hs).setOnClickListener(v -> showToast("Hồ sơ học sinh (Chi tiết)"));
        findViewById(R.id.btn_tai_khoan).setOnClickListener(v -> showToast("Quản lý tài khoản user"));
        findViewById(R.id.btn_chinh_sach).setOnClickListener(v -> showToast("Đối tượng chính sách"));
        findViewById(R.id.btn_dang_xuat).setOnClickListener(v -> {
            showToast("Đăng xuất");
            finish();
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
