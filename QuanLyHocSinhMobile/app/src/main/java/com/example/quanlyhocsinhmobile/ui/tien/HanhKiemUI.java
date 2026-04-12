package com.example.quanlyhocsinhmobile.ui.tien;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
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
import com.example.quanlyhocsinhmobile.data.DAO.HanhKiemDAO;
import com.example.quanlyhocsinhmobile.data.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.Model.HanhKiem;
import com.example.quanlyhocsinhmobile.data.Model.Lop;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HanhKiemUI extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSemester, spinnerYear, spinnerXepLoai;
    private EditText etSearch, etNhanXet;
    private TextView tvMaHS, tvHoTen;
    private Button btnFilter, btnSearch, btnUpdate, btnExport;
    private RecyclerView rvHanhKiem;

    private HanhKiemDAO hanhKiemDAO;
    private LopDAO lopDAO;

    private HanhKiemAdapter adapter;
    private List<HanhKiem.Display> currentList = new ArrayList<>();
    private List<Lop> listLop = new ArrayList<>();
    private HanhKiem selectedHanhKiem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_hanhkiem);

        initDatabase();
        initViews();
        setupRecyclerView();
        setupSpinners();
        loadData();

        btnFilter.setOnClickListener(v -> loadData());

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString();
            if (!query.isEmpty()) {
                currentList = hanhKiemDAO.searchHanhKiem("%" + query + "%");
                adapter.setList(currentList);
            } else {
                loadData();
            }
        });

        btnUpdate.setOnClickListener(v -> updateHanhKiem());

        btnExport.setOnClickListener(v -> exportToExcel());
    }

    private void initDatabase() {
        AppDatabase db = AppDatabase.getDatabase(this);
        hanhKiemDAO = db.hanhKiemDAO();
        lopDAO = db.lopDAO();
    }

    private void initViews() {
        spinnerClass = findViewById(R.id.spinner_class_hk);
        spinnerSemester = findViewById(R.id.spinner_semester_hk);
        spinnerYear = findViewById(R.id.spinner_year_hk);
        spinnerXepLoai = findViewById(R.id.spinner_xep_loai);
        etSearch = findViewById(R.id.et_search_hk);
        etNhanXet = findViewById(R.id.et_nhan_xet);
        tvMaHS = findViewById(R.id.tv_mahs_hk_update);
        tvHoTen = findViewById(R.id.tv_hoten_hk_update);
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
            
            String xl = selectedHanhKiem.getXepLoai();
            if (xl != null) {
                ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinnerXepLoai.getAdapter();
                int pos = spinnerAdapter.getPosition(xl);
                if (pos >= 0) spinnerXepLoai.setSelection(pos);
            }
        });
        rvHanhKiem.setAdapter(adapter);
    }

    private void setupSpinners() {
        listLop = lopDAO.getAll();
        List<String> lopNames = new ArrayList<>();
        lopNames.add("--- Tất cả lớp ---");
        for (Lop l : listLop) lopNames.add(l.getTenLop());
        spinnerClass.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, lopNames));

        String[] semesters = {"--- Tất cả HK ---", "1", "2"};
        spinnerSemester.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, semesters));

        String[] years = {"--- Tất cả năm ---", "2023-2024", "2024-2025"};
        spinnerYear.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, years));

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

        currentList = hanhKiemDAO.filterHanhKiem(maLop, hocKy, namHoc);
        adapter.setList(currentList);
    }

    private void updateHanhKiem() {
        if (selectedHanhKiem == null) {
            Toast.makeText(this, "Vui lòng chọn học sinh", Toast.LENGTH_SHORT).show();
            return;
        }
        selectedHanhKiem.setXepLoai(spinnerXepLoai.getSelectedItem().toString());
        selectedHanhKiem.setNhanXet(etNhanXet.getText().toString());
        hanhKiemDAO.update(selectedHanhKiem);
        Toast.makeText(this, "Cập nhật hạnh kiểm thành công", Toast.LENGTH_SHORT).show();
        loadData();
    }

    private void exportToExcel() {
        if (currentList == null || currentList.isEmpty()) {
            Toast.makeText(this, "Danh sách trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("HanhKiem");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã HS");
        headerRow.createCell(1).setCellValue("Họ Tên");
        headerRow.createCell(2).setCellValue("Lớp");
        headerRow.createCell(3).setCellValue("Học Kỳ");
        headerRow.createCell(4).setCellValue("Năm Học");
        headerRow.createCell(5).setCellValue("Xếp Loại");
        headerRow.createCell(6).setCellValue("Nhận Xét");

        for (int i = 0; i < currentList.size(); i++) {
            HanhKiem.Display display = currentList.get(i);
            HanhKiem hk = display.getHanhKiem();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(hk.getMaHS());
            row.createCell(1).setCellValue(display.getTenHS());
            row.createCell(2).setCellValue(display.getTenLop());
            row.createCell(3).setCellValue(hk.getHocKy());
            row.createCell(4).setCellValue(hk.getNamHoc());
            row.createCell(5).setCellValue(hk.getXepLoai());
            row.createCell(6).setCellValue(hk.getNhanXet());
        }

        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "HanhKiem_" + System.currentTimeMillis() + ".xlsx";
            File file = new File(downloadDir, fileName);
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            Toast.makeText(this, "Đã lưu tại Download: " + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xuất Excel", Toast.LENGTH_SHORT).show();
        }
    }
}
