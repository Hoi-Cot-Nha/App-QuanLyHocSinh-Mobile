package com.example.quanlyhocsinhmobile.ui.dat;

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
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class LopActivity extends AppCompatActivity {
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rv_lop_hoc;
    private TextView tv_lop_hoc_info;
    private TextInputEditText et_ma_lop, et_ten_lop, et_giao_vien, et_nien_khoa;
    private Button btnAdd, btnSave, btnDelete, btnRefresh;

    private LopViewModel viewModel;
    private LopAdapter adapter;
    private Lop selectedLop = null;
    private String selectedTenGVCN = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dat_activity_lop);

        viewModel = new ViewModelProvider(this).get(LopViewModel.class);

        initViews();
        setupRecyclerView();
        observeViewModel();
    }
    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        rv_lop_hoc = findViewById(R.id.rv_lop_hoc);
        tv_lop_hoc_info = findViewById(R.id.tv_lop_hoc_info);

        et_ma_lop = findViewById(R.id.et_ma_lop);
        et_ten_lop = findViewById(R.id.et_ten_lop); // ❗ FIX sai id

        et_giao_vien = findViewById(R.id.et_giao_vien);
        et_nien_khoa = findViewById(R.id.et_nien_khoa);

        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save); // đổi tên biến cho đúng logic
        btnDelete = findViewById(R.id.btn_delete);
        btnRefresh = findViewById(R.id.btn_refresh);

        // 🔍 SEARCH
        btnSearch.setOnClickListener(v ->
                viewModel.search(etSearch.getText().toString().trim())
        );

        // ➕ ADD
        btnAdd.setOnClickListener(v -> {
            String ma = et_ma_lop.getText().toString().trim();
            String ten = et_ten_lop.getText().toString().trim();
            String nienKhoa = et_nien_khoa.getText().toString().trim();
            String maGVCN = et_giao_vien.getText().toString().trim();

            if (ma.isEmpty() || ten.isEmpty() || nienKhoa.isEmpty() || maGVCN.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.insert(ma, ten, maGVCN, nienKhoa); // ❗ FIX thứ tự
        });

        // ✏️ UPDATE
        btnSave.setOnClickListener(v -> {
            if (selectedLop == null) {
                Toast.makeText(this, "Chưa chọn lớp", Toast.LENGTH_SHORT).show();
                return;
            }

            String ten = et_ten_lop.getText().toString().trim();
            String maGVCNInput = et_giao_vien.getText().toString().trim();
            String nienKhoa = et_nien_khoa.getText().toString().trim();

            // Nếu ô GV vẫn là tên hiển thị của dòng đang chọn, dùng lại mã GVCN gốc để update.
            String maGVCN = maGVCNInput;
            if (selectedTenGVCN != null && selectedTenGVCN.equalsIgnoreCase(maGVCNInput)) {
                maGVCN = selectedLop.getMaGVCN();
            }

            viewModel.update(selectedLop, ten, maGVCN, nienKhoa); // ❗ FIX thiếu param
        });


        btnDelete.setOnClickListener(v -> {
            if (selectedLop == null) {
                Toast.makeText(this, "Chưa chọn lớp", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.delete(selectedLop);
        });


        btnRefresh.setOnClickListener(v -> {
            viewModel.loadAllLops();
            clearInputs();
        });
    }

    private void setupRecyclerView() {
        adapter = new LopAdapter(new ArrayList<>(), display -> {

            if (display.getLop() == null) return;

            selectedLop = display.getLop();
            selectedTenGVCN = display.getTenGV();

            tv_lop_hoc_info.setText("Lớp : " + selectedLop.getTenLop());

            et_ma_lop.setText(selectedLop.getMaLop());
            et_ten_lop.setText(selectedLop.getTenLop());
            et_giao_vien.setText(
                    selectedTenGVCN == null || selectedTenGVCN.trim().isEmpty()
                            ? selectedLop.getMaGVCN()
                            : selectedTenGVCN
            );
            et_nien_khoa.setText(selectedLop.getNienKhoa());


            et_ma_lop.setEnabled(false); // khóa mã GV khi chọn
        });

        rv_lop_hoc.setLayoutManager(new LinearLayoutManager(this));
        rv_lop_hoc.setHasFixedSize(true);
        rv_lop_hoc.setAdapter(adapter);
    }
    private void observeViewModel() {
        viewModel.getAllLops().observe(this, (List<Lop.Display> displays) -> {
            adapter.setList(displays);
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
        selectedLop = null;
        selectedTenGVCN = null;
        tv_lop_hoc_info.setText("Lớp Học: --");
        et_ma_lop.setText("");
        et_ten_lop.setText("");
        et_giao_vien.setText("");
        et_nien_khoa.setText("");

        et_ma_lop.setEnabled(true);
        etSearch.setText("");
    }
}
