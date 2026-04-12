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
import com.example.quanlyhocsinhmobile.Controller_View.HocPhiController;
import com.example.quanlyhocsinhmobile.data.Model.HocPhi;
import com.example.quanlyhocsinhmobile.data.Model.Lop;

import java.util.ArrayList;
import java.util.List;

public class HocPhiUI extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSemester, spinnerYear, spinnerStatus, spinnerUpdateStatus;
    private EditText etSearch;
    private TextView tvInfo;
    private Button btnFilter, btnSearch, btnUpdate, btnExport;
    private RecyclerView rvHocPhi;

    private HocPhiController controller;
    private HocPhiAdapter adapter;
    private List<HocPhi.Display> currentList = new ArrayList<>();
    private List<Lop> listLop = new ArrayList<>();
    private HocPhi selectedHocPhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_hocphi);

        controller = new HocPhiController(this);
        initViews();
        setupRecyclerView();
        setupSpinners();
        loadData();

        btnFilter.setOnClickListener(v -> loadData());
        btnSearch.setOnClickListener(v -> search());
        btnUpdate.setOnClickListener(v -> updateStatus());
        btnExport.setOnClickListener(v -> controller.exportToExcel(currentList));
    }

    private void initViews() {
        spinnerClass = findViewById(R.id.spinner_class_hp);
        spinnerSemester = findViewById(R.id.spinner_semester_hp);
        spinnerYear = findViewById(R.id.spinner_year_hp);
        spinnerStatus = findViewById(R.id.spinner_status_hp);
        spinnerUpdateStatus = findViewById(R.id.spinner_update_status);
        etSearch = findViewById(R.id.et_search_hp);
        tvInfo = findViewById(R.id.tv_hp_info);
        btnFilter = findViewById(R.id.btn_filter_hp);
        btnSearch = findViewById(R.id.btn_search_hp);
        btnUpdate = findViewById(R.id.btn_update_hp);
        btnExport = findViewById(R.id.btn_export_hp);
        rvHocPhi = findViewById(R.id.rv_hoc_phi);
        rvHocPhi.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView() {
        adapter = new HocPhiAdapter(currentList, display -> {
            selectedHocPhi = display.getHocPhi();
            tvInfo.setText("Học sinh: " + display.getTenHS() + " (" + selectedHocPhi.getMaHS() + ")");
            
            ArrayAdapter adapter = (ArrayAdapter) spinnerUpdateStatus.getAdapter();
            int pos = adapter.getPosition(selectedHocPhi.getTrangThai());
            if (pos >= 0) spinnerUpdateStatus.setSelection(pos);
        });
        rvHocPhi.setAdapter(adapter);
    }

    private void setupSpinners() {
        listLop = controller.getAllLop();
        List<String> lopNames = new ArrayList<>();
        lopNames.add("--- Tất cả lớp ---");
        for (Lop l : listLop) lopNames.add(l.getTenLop());
        spinnerClass.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, lopNames));

        spinnerSemester.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, new String[]{"--- Tất cả HK ---", "1", "2"}));
        spinnerYear.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, new String[]{"--- Tất cả năm ---", "2023-2024", "2024-2025"}));
        
        String[] statusList = {"--- Tất cả trạng thái ---", "Chưa đóng", "Đã đóng", "Miễn giảm"};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, statusList));
        
        String[] updateStatusList = {"Chưa đóng", "Đã đóng", "Miễn giảm"};
        spinnerUpdateStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, updateStatusList));
    }

    private void loadData() {
        String maLop = "";
        if (spinnerClass.getSelectedItemPosition() > 0) maLop = listLop.get(spinnerClass.getSelectedItemPosition() - 1).getMaLop();

        int hocKy = 0;
        if (spinnerSemester.getSelectedItemPosition() == 1) hocKy = 1;
        else if (spinnerSemester.getSelectedItemPosition() == 2) hocKy = 2;

        String namHoc = "";
        if (spinnerYear.getSelectedItemPosition() > 0) namHoc = spinnerYear.getSelectedItem().toString();

        String trangThai = "";
        if (spinnerStatus.getSelectedItemPosition() > 0) trangThai = spinnerStatus.getSelectedItem().toString();

        currentList = controller.filterHocPhi(maLop, hocKy, namHoc, trangThai);
        adapter.setList(currentList);
    }

    private void search() {
        String q = etSearch.getText().toString();
        if (q.isEmpty()) loadData();
        else {
            currentList = controller.searchHocPhi(q);
            adapter.setList(currentList);
        }
    }

    private void updateStatus() {
        if (selectedHocPhi == null) {
            Toast.makeText(this, "Chọn học sinh từ danh sách", Toast.LENGTH_SHORT).show();
            return;
        }
        controller.updateHocPhi(selectedHocPhi, spinnerUpdateStatus.getSelectedItem().toString());
        loadData();
    }
}
