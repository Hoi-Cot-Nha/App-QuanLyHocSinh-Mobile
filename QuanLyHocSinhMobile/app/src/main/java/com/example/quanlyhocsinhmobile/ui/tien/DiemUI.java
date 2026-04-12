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
import com.example.quanlyhocsinhmobile.Controller_View.DiemController;
import com.example.quanlyhocsinhmobile.data.Model.Diem;
import com.example.quanlyhocsinhmobile.data.Model.Lop;
import com.example.quanlyhocsinhmobile.data.Model.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class DiemUI extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSubject, spinnerSemester;
    private EditText etSearch, et15p, et1Tiet, etGK, etCK;
    private TextView etMaHS, etHoTen;
    private Button btnFilter, btnSearch, btnUpdate, btnExport;
    private RecyclerView rvGrades;

    private DiemController controller;
    private DiemAdapter adapter;
    private List<Diem.Display> currentList = new ArrayList<>();
    private List<Lop> listLop = new ArrayList<>();
    private List<MonHoc> listMonHoc = new ArrayList<>();
    private Diem selectedDiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_quanlydiem);

        controller = new DiemController(this);
        initViews();
        setupRecyclerView();
        setupSpinners();
        loadData();
        
        btnFilter.setOnClickListener(v -> {
            loadData();
            Toast.makeText(this, "Đã cập nhật danh sách", Toast.LENGTH_SHORT).show();
        });

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString();
            if(!query.isEmpty()){
                currentList = controller.searchDiem(query);
                adapter.setDiemList(currentList);
                Toast.makeText(this, "Tìm thấy " + currentList.size() + " kết quả", Toast.LENGTH_SHORT).show();
            } else {
                loadData();
            }
        });

        btnUpdate.setOnClickListener(v -> {
            updateScore();
        });

        btnExport.setOnClickListener(v -> {
            controller.exportToExcel(currentList);
        });
    }

    private void setupRecyclerView() {
        adapter = new DiemAdapter(currentList, display -> {
            selectedDiem = display.getDiem();
            displaySelectedDiem(display);
        });
        rvGrades.setAdapter(adapter);
    }

    private void loadData() {
        String maMH = "";
        int subjectPos = spinnerSubject.getSelectedItemPosition();
        if (subjectPos > 0 && !listMonHoc.isEmpty()) {
            maMH = listMonHoc.get(subjectPos - 1).getMaMH();
        }

        int hocKy = 0;
        int semesterPos = spinnerSemester.getSelectedItemPosition();
        if (semesterPos == 1) hocKy = 1;
        else if (semesterPos == 2) hocKy = 2;

        String maLop = "";
        int lopPos = spinnerClass.getSelectedItemPosition();
        if (lopPos > 0 && !listLop.isEmpty()) {
            maLop = listLop.get(lopPos - 1).getMaLop();
        }

        currentList = controller.filterDiem(maMH, hocKy, maLop);
        adapter.setDiemList(currentList);
    }

    private void displaySelectedDiem(Diem.Display display) {
        if (selectedDiem != null) {
            etMaHS.setText("Mã HS: " + selectedDiem.getMaHS());
            etHoTen.setText("Học sinh: " + (display.getTenHS() != null ? display.getTenHS() : "---"));
            et15p.setText(selectedDiem.getDiem15p() != null ? String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiem15p()) : "");
            et1Tiet.setText(selectedDiem.getDiem1Tiet() != null ? String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiem1Tiet()) : "");
            etGK.setText(selectedDiem.getDiemGiuaKy() != null ? String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiemGiuaKy()) : "");
            etCK.setText(selectedDiem.getDiemCuoiKy() != null ? String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiemCuoiKy()) : "");
        }
    }

    private void updateScore() {
        if (selectedDiem == null) {
            Toast.makeText(this, "Vui lòng chọn một học sinh từ danh sách", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = controller.updateDiem(
                selectedDiem,
                et15p.getText().toString(),
                et1Tiet.getText().toString(),
                etGK.getText().toString(),
                etCK.getText().toString()
        );

        if (success) {
            loadData();
            Toast.makeText(this, "Cập nhật điểm thành công!", Toast.LENGTH_SHORT).show();
            
            selectedDiem = null;
            etMaHS.setText("Mã HS: --");
            etHoTen.setText("Học sinh: --");
            et15p.setText("");
            et1Tiet.setText("");
            etGK.setText("");
            etCK.setText("");
        }
    }

    private void initViews() {
        spinnerClass = findViewById(R.id.spinner_class);
        spinnerSubject = findViewById(R.id.spinner_subject);
        spinnerSemester = findViewById(R.id.spinner_semester);
        etSearch = findViewById(R.id.et_search);
        etMaHS = findViewById(R.id.et_mahs_update);
        etHoTen = findViewById(R.id.et_hoten_update);
        et15p = findViewById(R.id.et_diem15p);
        et1Tiet = findViewById(R.id.et_diem1tiet);
        etGK = findViewById(R.id.et_diemgk);
        etCK = findViewById(R.id.et_diemck);
        btnFilter = findViewById(R.id.btn_filter);
        btnSearch = findViewById(R.id.btn_search);
        btnUpdate = findViewById(R.id.btn_update_score);
        btnExport = findViewById(R.id.btn_export_excel);
        rvGrades = findViewById(R.id.rv_grades);
        
        rvGrades.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSpinners() {
        // Load Lớp
        listLop = controller.getAllLop();
        List<String> lopNames = new ArrayList<>();
        lopNames.add("--- Tất cả lớp ---");
        for (Lop l : listLop) lopNames.add("Lớp: " + l.getTenLop());
        spinnerClass.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, lopNames));

        // Load Môn học
        listMonHoc = controller.getAllMonHoc();
        List<String> monNames = new ArrayList<>();
        monNames.add("--- Tất cả môn ---");
        for (MonHoc m : listMonHoc) monNames.add("Môn: " + m.getTenMH());
        spinnerSubject.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, monNames));

        // Học kỳ (Cố định 1 & 2)
        String[] semesters = {"--- Tất cả HK ---", "HK: 1", "HK: 2"};
        spinnerSemester.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, semesters));
    }
}
