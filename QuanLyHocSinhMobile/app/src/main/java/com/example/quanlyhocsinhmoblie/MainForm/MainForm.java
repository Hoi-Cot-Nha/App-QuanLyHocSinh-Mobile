package com.example.quanlyhocsinhmoblie.MainForm;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyhocsinhmoblie.R;

public class MainForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main_form);

        setupButtons();
    }

    private void setupButtons() {
        // Group 1
        findViewById(R.id.btn_classes).setOnClickListener(v -> showToast("Quản lý lớp học"));
        findViewById(R.id.btn_teachers).setOnClickListener(v -> showToast("Quản lý giáo viên"));
        findViewById(R.id.btn_departments).setOnClickListener(v -> showToast("Quản lý tổ bộ môn"));

        // Group 2
        findViewById(R.id.btn_subjects).setOnClickListener(v -> showToast("Quản lý môn học"));
        findViewById(R.id.btn_schedule).setOnClickListener(v -> showToast("Thời khóa biểu / Lịch dạy"));
        findViewById(R.id.btn_facilities).setOnClickListener(v -> showToast("Phòng học & Thiết bị"));

        // Group 3
        findViewById(R.id.btn_grades).setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(MainForm.this, com.example.quanlyhocsinhmoblie.ui.tien.DiemUI.class);
            startActivity(intent);
        });
        findViewById(R.id.btn_conduct).setOnClickListener(v -> showToast("Quản lý hạnh kiểm"));
        findViewById(R.id.btn_exams).setOnClickListener(v -> showToast("Quản lý lịch thi"));

        // Group 4
        findViewById(R.id.btn_tuition).setOnClickListener(v -> showToast("Quản lý học phí"));
        findViewById(R.id.btn_notifications).setOnClickListener(v -> showToast("Quản lý thông báo"));

        // Group 5
        findViewById(R.id.btn_student_info).setOnClickListener(v -> showToast("Hồ sơ học sinh (Chi tiết)"));
        findViewById(R.id.btn_users).setOnClickListener(v -> showToast("Quản lý tài khoản user"));
        findViewById(R.id.btn_policy).setOnClickListener(v -> showToast("Đối tượng chính sách"));
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            showToast("Đăng xuất");
            finish();
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
