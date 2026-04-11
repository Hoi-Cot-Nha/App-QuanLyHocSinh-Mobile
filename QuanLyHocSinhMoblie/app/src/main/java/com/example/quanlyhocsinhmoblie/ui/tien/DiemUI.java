package com.example.quanlyhocsinhmoblie.ui.tien;

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

import com.example.quanlyhocsinhmoblie.R;
import com.example.quanlyhocsinhmoblie.data.Connection.AppDatabase;
import com.example.quanlyhocsinhmoblie.data.DAO.DiemDAO;
import com.example.quanlyhocsinhmoblie.data.DAO.HocSinhDAO;
import com.example.quanlyhocsinhmoblie.data.DAO.MonHocDAO;
import com.example.quanlyhocsinhmoblie.data.Model.Diem;
import com.example.quanlyhocsinhmoblie.data.Model.HocSinh;
import com.example.quanlyhocsinhmoblie.data.Model.MonHoc;

import java.util.ArrayList;
import java.util.List;

public class DiemUI extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSubject, spinnerSemester;
    private EditText etSearch, et15p, et1Tiet, etGK, etCK;
    private TextView etMaHS, etHoTen;
    private Button btnFilter, btnSearch, btnUpdate, btnExport;
    private RecyclerView rvGrades;
    private DiemDAO diemDAO;
    private HocSinhDAO hocSinhDAO;
    private MonHocDAO monHocDAO;
    private DiemAdapter adapter;
    private List<Diem> currentList = new ArrayList<>();
    private Diem selectedDiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_quanlydiem);

        initViews();
        initDatabase();
        setupRecyclerView();
        setupSpinners();
        loadData();
        
        // Sự kiện Lọc
        btnFilter.setOnClickListener(v -> {
            loadData();
            Toast.makeText(this, "Đã cập nhật danh sách", Toast.LENGTH_SHORT).show();
        });

        // Sự kiện Tìm kiếm
        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString();
            if(!query.isEmpty()){
                List<Diem> results = diemDAO.searchDiem("%" + query + "%");
                adapter.setDiemList(results);
                Toast.makeText(this, "Tìm thấy " + results.size() + " kết quả", Toast.LENGTH_SHORT).show();
            } else {
                loadData();
            }
        });

        // Sự kiện Cập nhật điểm
        btnUpdate.setOnClickListener(v -> {
            updateScore();
        });
    }

    private void setupRecyclerView() {
        adapter = new DiemAdapter(currentList, diem -> {
            selectedDiem = diem;
            displaySelectedDiem();
        });
        rvGrades.setAdapter(adapter);
    }

    private void loadData() {
        String selectedSubject = null;
        if (spinnerSubject.getSelectedItemPosition() > 0) {
            // "Môn: Toán" -> "MH01" (Giả định mapping đơn giản cho bản demo)
            String subjectText = spinnerSubject.getSelectedItem().toString();
            if (subjectText.contains("Toán")) selectedSubject = "MH01";
            else if (subjectText.contains("Văn")) selectedSubject = "MH02";
            else if (subjectText.contains("Anh")) selectedSubject = "MH03";
        }

        int selectedSemester = 0;
        if (spinnerSemester.getSelectedItemPosition() > 0) {
            String semesterText = spinnerSemester.getSelectedItem().toString();
            if (semesterText.contains("1")) selectedSemester = 1;
            else if (semesterText.contains("2")) selectedSemester = 2;
        }

        if (selectedSubject == null && selectedSemester == 0) {
            currentList = diemDAO.getAll();
        } else {
            currentList = diemDAO.filterDiem(selectedSubject, selectedSemester);
        }
        adapter.setDiemList(currentList);
    }

    private void displaySelectedDiem() {
        if (selectedDiem != null) {
            etMaHS.setText("Mã HS: " + selectedDiem.getMaHS());
            etHoTen.setText("Học sinh: " + (selectedDiem.getTenHS() != null ? selectedDiem.getTenHS() : "---"));
            et15p.setText(String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiem15p()));
            et1Tiet.setText(String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiem1Tiet()));
            etGK.setText(String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiemGiuaKy()));
            etCK.setText(String.format(java.util.Locale.US, "%.1f", selectedDiem.getDiemCuoiKy()));
        }
    }

    private void updateScore() {
        if (selectedDiem == null) {
            Toast.makeText(this, "Vui lòng chọn một học sinh từ danh sách", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            selectedDiem.setDiem15p(Double.parseDouble(et15p.getText().toString()));
            selectedDiem.setDiem1Tiet(Double.parseDouble(et1Tiet.getText().toString()));
            selectedDiem.setDiemGiuaKy(Double.parseDouble(etGK.getText().toString()));
            selectedDiem.setDiemCuoiKy(Double.parseDouble(etCK.getText().toString()));

            diemDAO.update(selectedDiem);
            loadData();
            Toast.makeText(this, "Cập nhật điểm thành công!", Toast.LENGTH_SHORT).show();
            
            // Clear selection and inputs
            selectedDiem = null;
            etMaHS.setText("Mã HS: --");
            etHoTen.setText("Học sinh: --");
            et15p.setText("");
            et1Tiet.setText("");
            etGK.setText("");
            etCK.setText("");
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: Vui lòng nhập đúng định dạng điểm (ví dụ: 8.5)", Toast.LENGTH_SHORT).show();
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

        btnExport.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng Xuất Excel đang được phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    private void initDatabase() {
        AppDatabase db = AppDatabase.getDatabase(this);
        diemDAO = db.diemDAO();
        hocSinhDAO = db.hocSinhDAO();
        monHocDAO = db.monHocDAO();
        
//        // Thêm dữ liệu mẫu nếu DB trống
//        if (diemDAO.getAll().isEmpty()) {
//            // Thêm môn học
//            monHocDAO.insert(new MonHoc("MH01", "Toán"));
//            monHocDAO.insert(new MonHoc("MH02", "Văn"));
//            monHocDAO.insert(new MonHoc("MH03", "Anh"));
//
//            // Thêm học sinh
//            HocSinh hs1 = new HocSinh(); hs1.setMaHS("HS001"); hs1.setHoTen("Nguyễn Văn An"); hocSinhDAO.insert(hs1);
//            HocSinh hs2 = new HocSinh(); hs2.setMaHS("HS002"); hs2.setHoTen("Trần Thị Bình"); hocSinhDAO.insert(hs2);
//            HocSinh hs3 = new HocSinh(); hs3.setMaHS("HS003"); hs3.setHoTen("Lê Văn Cường"); hocSinhDAO.insert(hs3);
//            HocSinh hs4 = new HocSinh(); hs4.setMaHS("HS004"); hs4.setHoTen("Phạm Minh Đức"); hocSinhDAO.insert(hs4);
//
//            // Thêm điểm
//            diemDAO.insert(new Diem("HS001", "MH01", 1, 8.5, 7.0, 9.0, 8.0) {{
//                setTenHS(hocSinhDAO.getTenHocSinh(getMaHS()));
//                setTenMH(monHocDAO.getTenMonHoc(getMaMH()));
//            }});
//            diemDAO.insert(new Diem("HS002", "MH01", 1, 6.0, 7.5, 8.0, 7.0) {{
//                setTenHS(hocSinhDAO.getTenHocSinh(getMaHS()));
//                setTenMH(monHocDAO.getTenMonHoc(getMaMH()));
//            }});
//            diemDAO.insert(new Diem("HS003", "MH02", 1, 9.0, 9.0, 8.5, 9.5) {{
//                setTenHS(hocSinhDAO.getTenHocSinh(getMaHS()));
//                setTenMH(monHocDAO.getTenMonHoc(getMaMH()));
//            }});
//            diemDAO.insert(new Diem("HS004", "MH03", 1, 7.0, 8.0, 7.5, 8.5) {{
//                setTenHS(hocSinhDAO.getTenHocSinh(getMaHS()));
//                setTenMH(monHocDAO.getTenMonHoc(getMaMH()));
//            }});
//        }
    }

    private void setupSpinners() {
        String[] classes = {"--- Tất cả lớp ---", "Lớp: 10A1", "Lớp: 11A2", "Lớp: 12C3"};
        String[] subjects = {"--- Tất cả môn ---", "Môn: Toán", "Môn: Văn", "Môn: Anh"};
        String[] semesters = {"--- Tất cả HK ---", "HK: 1", "HK: 2"};

        spinnerClass.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, classes));
        spinnerSubject.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjects));
        spinnerSemester.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesters));
    }
}
