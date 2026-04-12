package com.example.quanlyhocsinhmobile.ui.tien;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.Controller_View.HanhKiemController;
import com.example.quanlyhocsinhmobile.data.Model.HanhKiem;
import com.example.quanlyhocsinhmobile.data.Model.Lop;

import java.util.ArrayList;
import java.util.List;

public class HanhKiemUI extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSemester, spinnerYear, spinnerXepLoai;
    private EditText etSearch, etNhanXet;
    private TextView tvMaHS, tvHoTen;
    private Button btnFilter, btnSearch, btnUpdate, btnExport;
    private RecyclerView rvHanhKiem;

    private HanhKiemController controller;
    private HanhKiemAdapter adapter;
    private List<HanhKiem.Display> currentList = new ArrayList<>();
    private List<Lop> listLop = new ArrayList<>();
    private HanhKiem selectedHanhKiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_hanhkiem);

        controller = new HanhKiemController(this);
        initViews();
        setupRecyclerView();
        setupSpinners();
        loadData();

        btnFilter.setOnClickListener(v -> loadData());

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString();
            if (!query.isEmpty()) {
                currentList = controller.searchHanhKiem(query);
                adapter.setList(currentList);
            } else {
                loadData();
            }
        });

        btnUpdate.setOnClickListener(v -> updateHanhKiem());

        btnExport.setOnClickListener(v -> controller.exportToExcel(currentList));
    }

    private void initViews() {
        spinnerClass = findViewById(R.id.spinner_class_hk);
        spinnerSemester = findViewById(R.id.spinner_semester_hk);
        spinnerYear = findViewById(R.id.spinner_year_hk);
        spinnerXepLoai = findViewById(R.id.spinner_xep_loai);
        etSearch = findViewById(R.id.et_search_hk);
        etNhanXet = findViewById(R.id.et_nhan_xet);
        tvMaHS = findViewById(R.id.tv_mahs_hk_update); // Sửa ID cho đúng layout
        tvHoTen = findViewById(R.id.tv_hoten_hk_update); // Sửa ID cho đúng layout
        btnFilter = findViewById(R.id.btn_filter_hk);
        btnSearch = findViewById(R.id.btn_search_hk);
        btnUpdate = findViewById(R.id.btn_update_hk);
        btnExport = findViewById(R.id.btn_export_hk);
        rvHanhKiem = findViewById(R.id.rv_hanh_kiem);

        rvHanhKiem.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView() {
        adapter = new HanhKiemAdapter(currentList, display -> {
            selectedHanhKiem = display.getHanhKiem();
            tvMaHS.setText("Mã HS: " + selectedHanhKiem.getMaHS());
            tvHoTen.setText("Học sinh: " + display.getTenHS());
            etNhanXet.setText(selectedHanhKiem.getNhanXet());
            
            // Set selection for Xếp loại
            String xl = selectedHanhKiem.getXepLoai();
            if (xl != null) {
                ArrayAdapter adapter = (ArrayAdapter) spinnerXepLoai.getAdapter();
                int pos = adapter.getPosition(xl);
                if (pos >= 0) spinnerXepLoai.setSelection(pos);
            }
        });
        rvHanhKiem.setAdapter(adapter);
    }

    private void setupSpinners() {
        // Lớp
        listLop = controller.getAllLop();
        List<String> lopNames = new ArrayList<>();
        lopNames.add("--- Tất cả lớp ---");
        for (Lop l : listLop) lopNames.add(l.getTenLop());
        spinnerClass.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, lopNames));

        // Học kỳ
        String[] semesters = {"--- Tất cả HK ---", "1", "2"};
        spinnerSemester.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, semesters));

        // Năm học (Ví dụ)
        String[] years = {"--- Tất cả năm ---", "2023-2024", "2024-2025"};
        spinnerYear.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, years));

        // Xếp loại
        String[] ratings = {"Tốt", "Khá", "Trung bình", "Yếu", "Kém"};
        spinnerXepLoai.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ratings));
    }

    private void loadData() {
        String maLop = "";
        int lopPos = spinnerClass.getSelectedItemPosition();
        if (lopPos > 0) maLop = listLop.get(lopPos - 1).getMaLop();

        int hocKy = 0;
        int hkPos = spinnerSemester.getSelectedItemPosition();
        if (hkPos == 1) hocKy = 1;
        else if (hkPos == 2) hocKy = 2;

        String namHoc = "";
        int yearPos = spinnerYear.getSelectedItemPosition();
        if (yearPos > 0) namHoc = spinnerYear.getSelectedItem().toString();

        currentList = controller.filterHanhKiem(maLop, hocKy, namHoc);
        adapter.setList(currentList);
    }

    private void updateHanhKiem() {
        if (selectedHanhKiem == null) {
            Toast.makeText(this, "Vui lòng chọn học sinh", Toast.LENGTH_SHORT).show();
            return;
        }
        String xl = spinnerXepLoai.getSelectedItem().toString();
        String nx = etNhanXet.getText().toString();
        controller.updateHanhKiem(selectedHanhKiem, xl, nx);
        loadData();
    }
}
