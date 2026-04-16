package com.example.quanlyhocsinhmobile.ui.dai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.DAO.TaiKhoanDAO;
import com.example.quanlyhocsinhmobile.data.local.Model.TaiKhoan;
import com.example.quanlyhocsinhmobile.ui.zMainForm.MainForm;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TaiKhoanDAO taiKhoanDAO;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private PhanQuyen phanQuyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        phanQuyen = PhanQuyen.getInstance(this);
        
        if (phanQuyen.daDangNhap()) {
            startActivity(new Intent(this, MainForm.class));
            finish();
            return;
        }
        
        setContentView(R.layout.dai_activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        taiKhoanDAO = AppDatabase.getDatabase(this).taiKhoanDAO();

        btnLogin.setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            TaiKhoan user = taiKhoanDAO.login(username, password);

            runOnUiThread(() -> {
                if (user != null) {
                    phanQuyen.luuPhienDangNhap(
                            user.getTenDangNhap(),
                            user.getQuyen(),
                            user.getMaNguoiDung()
                    );

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(LoginActivity.this, MainForm.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
