package com.example.quanlyhocsinhmobile.ui.dai;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.TaiKhoan;
import com.example.quanlyhocsinhmobile.ui.dai.TaiKhoanViewModel;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
public class TaiKhoanActivity extends AppCompatActivity {
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvTaiKhoan;
    private TextView tvInfo;
    private TextInputEditText etUsername, etPassword, etMaND;
    private Spinner spQuyen;
    private Button btnAdd, btnSave, btnDelete, btnRefresh;
    private TaiKhoanViewModel viewModel;
    private TaiKhoanAdapter adapter;
    private TaiKhoan selectedTaiKhoan = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dai_activity_taikhoan);
        viewModel = new ViewModelProvider(this).get(TaiKhoanViewModel.class);
        initViews();
        setupRecyclerView();
        observeViewModel();
    }
    private void initViews() {
        etSearch = findViewById(R.id.et_search_tk);
        btnSearch = findViewById(R.id.btn_search_tk);
        rvTaiKhoan = findViewById(R.id.rv_tai_khoan);
        tvInfo = findViewById(R.id.tv_tk_info);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etMaND = findViewById(R.id.et_ma_nd);
        spQuyen = findViewById(R.id.sp_quyen);
        btnAdd = findViewById(R.id.btn_add_tk);
        btnSave = findViewById(R.id.btn_save_tk);
        btnDelete = findViewById(R.id.btn_delete_tk);
        btnRefresh = findViewById(R.id.btn_refresh_tk);
        btnSearch.setOnClickListener(v -> viewModel.search(etSearch.getText().toString().trim()));
        btnAdd.setOnClickListener(v -> viewModel.insert(
                etUsername.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                spQuyen.getSelectedItem().toString(),
                etMaND.getText().toString().trim()
        ));
        btnSave.setOnClickListener(v -> viewModel.update(
                selectedTaiKhoan,
                etPassword.getText().toString().trim(),
                spQuyen.getSelectedItem().toString(),
                etMaND.getText().toString().trim()
        ));
        btnDelete.setOnClickListener(v -> {
            if (selectedTaiKhoan == null) {
                Toast.makeText(this, "Vui lòng chọn tài khoản để xóa", Toast.LENGTH_SHORT).show();
                return;
            }
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa tài khoản này không?")
                    .setPositiveButton("XÓA", (dialog, which) -> {
                        viewModel.delete(selectedTaiKhoan);
                    })
                    .setNegativeButton("HỦY", null)
                    .show();
        });
        btnRefresh.setOnClickListener(v -> {
            viewModel.loadAllTaiKhoans();
            clearInputs();
        });
        String[] quyenList = {"Admin", "GiaoVien", "HocSinh"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                quyenList
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spQuyen.setAdapter(spinnerAdapter);
    }
    private void setupRecyclerView() {
        adapter = new TaiKhoanAdapter(new ArrayList<>(), taiKhoan -> {
            selectedTaiKhoan = taiKhoan;
            tvInfo.setText("Tài khoản: " + taiKhoan.getTenDangNhap());
            etUsername.setText(taiKhoan.getTenDangNhap());
            etPassword.setText(taiKhoan.getMatKhau());
            String quyen = taiKhoan.getQuyen();
            switch (quyen) {
                case "Admin":
                    spQuyen.setSelection(0);
                    break;
                case "GiaoVien":
                    spQuyen.setSelection(1);
                    break;
                case "HocSinh":
                    spQuyen.setSelection(2);
                    break;
            }
            etMaND.setText(taiKhoan.getMaNguoiDung() != null ? taiKhoan.getMaNguoiDung() : "");
            etUsername.setEnabled(false); 
        });
        rvTaiKhoan.setLayoutManager(new LinearLayoutManager(this));
        rvTaiKhoan.setAdapter(adapter);
    }
    private void observeViewModel() {
        viewModel.getAllTaiKhoans().observe(this, list -> {
            if (list != null) {
                adapter.setList(list);
            }
        });
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (message.contains("thành công")) {
                    clearInputs();
                }
            }
        });
    }
    private void clearInputs() {
        selectedTaiKhoan = null;
        tvInfo.setText("Thông tin tài khoản: --");
        etUsername.setText("");
        etPassword.setText("");
        spQuyen.setSelection(0);
        etMaND.setText("");
        etUsername.setEnabled(true); 
        etSearch.setText("");
    }
}