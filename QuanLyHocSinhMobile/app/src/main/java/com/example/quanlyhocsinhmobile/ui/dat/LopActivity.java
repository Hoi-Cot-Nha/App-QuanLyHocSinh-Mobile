package com.example.quanlyhocsinhmobile.ui.dat;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.DAO.LopDAO;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class LopActivity extends AppCompatActivity {
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rv_lop_hoc;
    private TextView tv_lop_hoc_info;
    private TextInputEditText et_ma_lop, et_ten_lop;
    private Spinner sp_giao_vien, sp_nien_khoa;
    private Button btnAdd, btnSave, btnDelete, btnRefresh;
    private NestedScrollView nsvLopContent;

    private LopViewModel viewModel;
    private LopAdapter adapter;
    private Lop selectedLop = null;
    private String selectedTenGVCN = null;

    // ✅ Adapter cho Spinner
    private ArrayAdapter<String> nienKhoaAdapter;
    private ArrayAdapter<LopDAO.GiaoVienInfo> giaoVienAdapter;
    private final List<LopDAO.GiaoVienInfo> baseGiaoVienOptions = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dat_activity_lop);

        viewModel = new ViewModelProvider(this).get(LopViewModel.class);

        initViews();
        setupRecyclerView();
        observeViewModel();
        // ✅ Load dữ liệu cho Spinner
        viewModel.loadSpinnerData();
    }
    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);

        rv_lop_hoc = findViewById(R.id.rv_lop_hoc);
        tv_lop_hoc_info = findViewById(R.id.tv_lop_hoc_info);
        nsvLopContent = findViewById(R.id.nsv_lop_content);

        et_ma_lop = findViewById(R.id.et_ma_lop);
        et_ten_lop = findViewById(R.id.et_ten_lop);

        // ✅ Thay EditText bằng Spinner
        sp_giao_vien = findViewById(R.id.sp_giao_vien);
        sp_nien_khoa = findViewById(R.id.sp_nien_khoa);

        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        btnRefresh = findViewById(R.id.btn_refresh);

        // ✅ Init Adapter cho Spinner
        nienKhoaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        nienKhoaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_nien_khoa.setAdapter(nienKhoaAdapter);

        giaoVienAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        giaoVienAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_giao_vien.setAdapter(giaoVienAdapter);

        // 🔍 SEARCH
        btnSearch.setOnClickListener(v ->
                viewModel.search(etSearch.getText().toString().trim())
        );

        // ➕ ADD
        btnAdd.setOnClickListener(v -> {
            String ma = et_ma_lop.getText().toString().trim();
            String ten = et_ten_lop.getText().toString().trim();

            // ✅ Lấy giá trị từ Spinner
            String nienKhoa = (String) sp_nien_khoa.getSelectedItem();
            LopDAO.GiaoVienInfo selectedGV = (LopDAO.GiaoVienInfo) sp_giao_vien.getSelectedItem();
            String maGVCN = selectedGV != null ? selectedGV.maGV : "";

            if (ma.isEmpty() || ten.isEmpty() || nienKhoa == null || nienKhoa.isEmpty() || nienKhoa.startsWith("--") ||
                maGVCN.isEmpty() || selectedGV == null || selectedGV.maGV.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.insert(ma, ten, maGVCN, nienKhoa);
        });

        // ✏️ UPDATE
        btnSave.setOnClickListener(v -> {
            if (selectedLop == null) {
                Toast.makeText(this, "Chưa chọn lớp", Toast.LENGTH_SHORT).show();
                return;
            }

            String ten = et_ten_lop.getText().toString().trim();

            // ✅ Lấy giá trị từ Spinner
            String nienKhoa = (String) sp_nien_khoa.getSelectedItem();
            LopDAO.GiaoVienInfo selectedGV = (LopDAO.GiaoVienInfo) sp_giao_vien.getSelectedItem();
            String maGVCN = selectedGV != null ? selectedGV.maGV : "";

            if (ten.isEmpty() || nienKhoa == null || nienKhoa.isEmpty() || nienKhoa.startsWith("--") || maGVCN.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.update(selectedLop, ten, maGVCN, nienKhoa);
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

            syncGiaoVienSpinnerForSelectedLop();

            et_ma_lop.setEnabled(false);

            // ✅ Tự cuộn xuống phần thông tin lớp học sau khi chọn dòng
            if (nsvLopContent != null) {
                nsvLopContent.post(() -> nsvLopContent.smoothScrollTo(0, tv_lop_hoc_info.getTop()));
            }
        });

        rv_lop_hoc.setLayoutManager(new LinearLayoutManager(this));
        rv_lop_hoc.setHasFixedSize(true);
        rv_lop_hoc.setAdapter(adapter);
    }

    private void rebuildGiaoVienSpinnerFromBase() {
        giaoVienAdapter.clear();
        giaoVienAdapter.addAll(baseGiaoVienOptions);
        giaoVienAdapter.notifyDataSetChanged();
    }

    private void syncGiaoVienSpinnerForSelectedLop() {
        rebuildGiaoVienSpinnerFromBase();

        if (selectedLop == null) {
            if (sp_giao_vien.getCount() > 0) sp_giao_vien.setSelection(0);
            return;
        }

        boolean foundGVCN = false;
        for (int i = 0; i < giaoVienAdapter.getCount(); i++) {
            LopDAO.GiaoVienInfo item = giaoVienAdapter.getItem(i);
            if (item != null && item.maGV.equals(selectedLop.getMaGVCN())) {
                sp_giao_vien.setSelection(i);
                foundGVCN = true;
                break;
            }
        }

        // Nếu lớp đang chọn có GVCN đã bị lọc khỏi danh sách "chưa chủ nhiệm",
        // thêm tạm duy nhất 1 item sau khi đã reset từ danh sách gốc.
        if (!foundGVCN && selectedLop.getMaGVCN() != null && !selectedLop.getMaGVCN().isEmpty()) {
            String tenHienThi = (selectedTenGVCN == null || selectedTenGVCN.trim().isEmpty())
                    ? selectedLop.getMaGVCN()
                    : selectedTenGVCN;
            LopDAO.GiaoVienInfo currentGVCN = new LopDAO.GiaoVienInfo(selectedLop.getMaGVCN(), tenHienThi);
            giaoVienAdapter.add(currentGVCN);
            giaoVienAdapter.notifyDataSetChanged();
            sp_giao_vien.setSelection(giaoVienAdapter.getCount() - 1);
        }
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

        // ✅ Observer dữ liệu Spinner
        viewModel.getNienKhoaList().observe(this, nienKhoas -> {
            if (nienKhoas != null) {
                nienKhoaAdapter.clear();
                nienKhoaAdapter.addAll(nienKhoas);
                nienKhoaAdapter.notifyDataSetChanged();
            }
        });

        viewModel.getGiaoVienList().observe(this, giaoViens -> {
            if (giaoViens != null) {
                baseGiaoVienOptions.clear();
                baseGiaoVienOptions.addAll(giaoViens);
                syncGiaoVienSpinnerForSelectedLop();
            }
        });
    }
    private void clearInputs() {
        selectedLop = null;
        selectedTenGVCN = null;
        tv_lop_hoc_info.setText("Lớp Học: --");
        et_ma_lop.setText("");
        et_ten_lop.setText("");

        // ✅ Reset Spinner
        rebuildGiaoVienSpinnerFromBase();
        if (sp_giao_vien.getCount() > 0) {
            sp_giao_vien.setSelection(0);
        }
        if (sp_nien_khoa.getCount() > 0) {
            sp_nien_khoa.setSelection(0);
        }

        et_ma_lop.setEnabled(true);
        etSearch.setText("");
    }
}
