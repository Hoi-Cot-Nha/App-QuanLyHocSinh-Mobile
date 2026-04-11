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
import com.example.quanlyhocsinhmobile.data.Connection.AppDatabase;
import com.example.quanlyhocsinhmobile.data.DAO.DiemDAO;
import com.example.quanlyhocsinhmobile.data.DAO.HocSinhDAO;
import com.example.quanlyhocsinhmobile.data.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.Model.Diem;
import com.example.quanlyhocsinhmobile.data.Model.DiemDisplay;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private List<DiemDisplay> currentList = new ArrayList<>();
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
                currentList = diemDAO.searchDiem("%" + query + "%");
                adapter.setDiemList(currentList);
                Toast.makeText(this, "Tìm thấy " + currentList.size() + " kết quả", Toast.LENGTH_SHORT).show();
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
        adapter = new DiemAdapter(currentList, display -> {
            selectedDiem = display.getDiem();
            displaySelectedDiem(display);
        });
        rvGrades.setAdapter(adapter);
    }

    private void loadData() {
        String selectedSubject = null;
        if (spinnerSubject.getSelectedItemPosition() > 0) {
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

    private void displaySelectedDiem(DiemDisplay display) {
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

        try {
            selectedDiem.setDiem15p(Double.parseDouble(et15p.getText().toString()));
            selectedDiem.setDiem1Tiet(Double.parseDouble(et1Tiet.getText().toString()));
            selectedDiem.setDiemGiuaKy(Double.parseDouble(etGK.getText().toString()));
            selectedDiem.setDiemCuoiKy(Double.parseDouble(etCK.getText().toString()));
            selectedDiem.setDiemTongKet(selectedDiem.calculateDiemTongKet());

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
            exportToExcel();
        });
    }

    private void exportToExcel() {
        if (currentList.isEmpty()) {
            Toast.makeText(this, "Danh sách trống, không thể xuất!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BangDiem");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã HS");
        headerRow.createCell(1).setCellValue("Họ Tên");
        headerRow.createCell(2).setCellValue("Môn");
        headerRow.createCell(3).setCellValue("HK");
        headerRow.createCell(4).setCellValue("15p");
        headerRow.createCell(5).setCellValue("1 Tiết");
        headerRow.createCell(6).setCellValue("Giữa Kỳ");
        headerRow.createCell(7).setCellValue("Cuối Kỳ");
        headerRow.createCell(8).setCellValue("Trung Bình");

        // Data
        for (int i = 0; i < currentList.size(); i++) {
            DiemDisplay display = currentList.get(i);
            Diem d = display.getDiem();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(d.getMaHS());
            row.createCell(1).setCellValue(display.getTenHS());
            row.createCell(2).setCellValue(display.getTenMH());
            row.createCell(3).setCellValue(d.getHocKy());
            row.createCell(4).setCellValue(d.getDiem15p() != null ? d.getDiem15p() : 0.0);
            row.createCell(5).setCellValue(d.getDiem1Tiet() != null ? d.getDiem1Tiet() : 0.0);
            row.createCell(6).setCellValue(d.getDiemGiuaKy() != null ? d.getDiemGiuaKy() : 0.0);
            row.createCell(7).setCellValue(d.getDiemCuoiKy() != null ? d.getDiemCuoiKy() : 0.0);
            row.createCell(8).setCellValue(d.getDiemTongKet() != null ? d.getDiemTongKet() : 0.0);
        }

        try {
            File cachePath = new File(getExternalFilesDir(null), "exports");
            if (!cachePath.exists()) cachePath.mkdirs();
            File file = new File(cachePath, "BangDiem_" + System.currentTimeMillis() + ".xlsx");
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            Toast.makeText(this, "Đã lưu tại: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xuất Excel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initDatabase() {
        AppDatabase db = AppDatabase.getDatabase(this);
        diemDAO = db.diemDAO();
        hocSinhDAO = db.hocSinhDAO();
        monHocDAO = db.monHocDAO();
    }

    private void setupSpinners() {
        String[] classes = {"--- Tất cả lớp ---", "Lớp: 10A1", "Lớp: 11A2", "Lớp: 12C3"};
        String[] subjects = {"--- Tất cả môn ---", "Môn: Toán", "Môn: Văn", "Môn: Anh"};
        String[] semesters = {"--- Tất cả HK ---", "HK: 1", "HK: 2"};

        spinnerClass.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, classes));
        spinnerSubject.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, subjects));
        spinnerSemester.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, semesters));
    }
}
