package com.example.quanlyhocsinhmobile.ui.dai;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.DoiTuongUuTien;
import com.example.quanlyhocsinhmobile.ui.dai.DoiTuongViewModel;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
public class DoiTuongActivity extends AppCompatActivity {
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvDoiTuong;
    private TextView tvInfo;
    private TextInputEditText etMa, etTen, etTiLe;
    private Button btnAdd, btnSave, btnDelete, btnRefresh;
    private DoiTuongViewModel viewModel;
    private DoiTuongAdapter adapter;
    private DoiTuongUuTien selectedDoiTuong = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dai_activity_doituong);
        viewModel = new ViewModelProvider(this).get(DoiTuongViewModel.class);
        initViews();
        setupRecyclerView();
        observeViewModel();
    }
    private void initViews() {
        etSearch = findViewById(R.id.et_search_dt);
        btnSearch = findViewById(R.id.btn_search_dt);
        rvDoiTuong = findViewById(R.id.rv_dt);
        tvInfo = findViewById(R.id.tv_info_dt);
        etMa = findViewById(R.id.et_ma_dt);
        etTen = findViewById(R.id.et_ten_dt);
        etTiLe = findViewById(R.id.et_tile_dt);
        btnAdd = findViewById(R.id.btn_add_dt);
        btnSave = findViewById(R.id.btn_save_dt);
        btnDelete = findViewById(R.id.btn_delete_dt);
        btnRefresh = findViewById(R.id.btn_refresh_dt);
        btnSearch.setOnClickListener(v -> viewModel.search(etSearch.getText().toString().trim()));
        btnAdd.setOnClickListener(v -> viewModel.insert(
                etMa.getText().toString().trim(),
                etTen.getText().toString().trim(),
                etTiLe.getText().toString().trim()
        ));
        btnSave.setOnClickListener(v -> viewModel.update(
                selectedDoiTuong,
                etTen.getText().toString().trim(),
                etTiLe.getText().toString().trim()
        ));
        btnDelete.setOnClickListener(v -> {
            if (selectedDoiTuong == null) {
                Toast.makeText(this, "Vui lòng chọn đối tượng để xóa", Toast.LENGTH_SHORT).show();
                return;
            }
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa đối tượng này không?")
                    .setPositiveButton("XÓA", (dialog, which) -> {
                        viewModel.delete(selectedDoiTuong);
                    })
                    .setNegativeButton("HỦY", null)
                    .show();
        });
        btnRefresh.setOnClickListener(v -> {
            viewModel.loadAllDoiTuongs();
            clearInputs();
        });
    }
    private void setupRecyclerView() {
        adapter = new DoiTuongAdapter(new ArrayList<>(), doiTuong -> {
            selectedDoiTuong = doiTuong;
            tvInfo.setText("Đối tượng: " + doiTuong.getTenDT());
            etMa.setText(doiTuong.getMaDT());
            etTen.setText(doiTuong.getTenDT());
            etTiLe.setText(String.valueOf(doiTuong.getTiLeGiamHocPhi()));
            etMa.setEnabled(false); 
        });
        rvDoiTuong.setLayoutManager(new LinearLayoutManager(this));
        rvDoiTuong.setAdapter(adapter);
    }
    private void observeViewModel() {
        viewModel.getAllDoiTuongs().observe(this, doiTuongs -> {
            adapter.setList(doiTuongs);
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
        selectedDoiTuong = null;
        tvInfo.setText("Thông tin đối tượng: --");
        etMa.setText("");
        etTen.setText("");
        etTiLe.setText("");
        etMa.setEnabled(true);
        etSearch.setText("");
    }
}