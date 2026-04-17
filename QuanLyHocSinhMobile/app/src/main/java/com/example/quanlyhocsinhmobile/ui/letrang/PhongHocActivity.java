package com.example.quanlyhocsinhmobile.ui.letrang;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
public class PhongHocActivity extends AppCompatActivity {
    private Spinner spinnerFilterType;
    private Button btnFilter;
    private RecyclerView rvPhongHoc;
    private TextView tvPhongInfo;
    private TextInputEditText etMaPhong, etTenPhong, etSucChua;
    private Spinner spinnerLoaiPhong;
    private Button btnAdd, btnSave, btnDelete, btnRefresh;
    private PhongHocViewModel viewModel;
    private PhongHocAdapter adapter;
    private PhongHoc selectedPhongHoc = null;
    private final String[] loaiPhongArray = {"Lý thuyết", "Thực hành", "Hội trường", "Phòng thí nghiệm"};
    private final String[] filterLoaiArray = {"Tất cả", "Lý thuyết", "Thực hành", "Hội trường", "Phòng thí nghiệm"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.letrang_activity_phonghoc);
        viewModel = new ViewModelProvider(this).get(PhongHocViewModel.class);
        initViews();
        setupRecyclerView();
        setupSpinners();
        observeViewModel();
    }
    private void initViews() {
        spinnerFilterType = findViewById(R.id.spinner_filter_type);
        btnFilter = findViewById(R.id.btn_filter);
        rvPhongHoc = findViewById(R.id.rv_phong_hoc);
        tvPhongInfo = findViewById(R.id.tv_phong_info);
        etMaPhong = findViewById(R.id.et_ma_phong);
        etTenPhong = findViewById(R.id.et_ten_phong);
        etSucChua = findViewById(R.id.et_suc_chua);
        spinnerLoaiPhong = findViewById(R.id.spinner_loai_phong);
        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnFilter.setOnClickListener(v -> viewModel.loadData(spinnerFilterType.getSelectedItem().toString()));
        btnAdd.setOnClickListener(v -> viewModel.insert(
                etMaPhong.getText().toString().trim(),
                etTenPhong.getText().toString().trim(),
                etSucChua.getText().toString().trim(),
                spinnerLoaiPhong.getSelectedItem().toString()
        ));
        btnSave.setOnClickListener(v -> viewModel.update(
                selectedPhongHoc,
                etTenPhong.getText().toString().trim(),
                etSucChua.getText().toString().trim(),
                spinnerLoaiPhong.getSelectedItem().toString()
        ));
        btnDelete.setOnClickListener(v -> viewModel.delete(selectedPhongHoc));
        btnRefresh.setOnClickListener(v -> {
            spinnerFilterType.setSelection(0);
            viewModel.loadData("Tất cả");
            clearInputs();
        });
    }
    private void setupRecyclerView() {
        adapter = new PhongHocAdapter(new ArrayList<>(), phongHoc -> {
            selectedPhongHoc = phongHoc;
            tvPhongInfo.setText("Đang chọn: " + phongHoc.getTenPhong());
            etMaPhong.setText(phongHoc.getMaPhong());
            etTenPhong.setText(phongHoc.getTenPhong());
            etSucChua.setText(String.valueOf(phongHoc.getSucChua()));
            for (int i = 0; i < loaiPhongArray.length; i++) {
                if (loaiPhongArray[i].equals(phongHoc.getLoaiPhong())) {
                    spinnerLoaiPhong.setSelection(i);
                    break;
                }
            }
            etMaPhong.setEnabled(false);
        });
        rvPhongHoc.setLayoutManager(new LinearLayoutManager(this));
        rvPhongHoc.setAdapter(adapter);
    }
    private void setupSpinners() {
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filterLoaiArray);
        spinnerFilterType.setAdapter(filterAdapter);
        ArrayAdapter<String> updateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loaiPhongArray);
        spinnerLoaiPhong.setAdapter(updateAdapter);
    }
    private void observeViewModel() {
        viewModel.getFilteredPhongHocs().observe(this, phongHocs -> {
            adapter.setList(phongHocs);
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
        selectedPhongHoc = null;
        tvPhongInfo.setText("Phòng học: --");
        etMaPhong.setText("");
        etTenPhong.setText("");
        etSucChua.setText("");
        spinnerLoaiPhong.setSelection(0);
        etMaPhong.setEnabled(true);
    }
}