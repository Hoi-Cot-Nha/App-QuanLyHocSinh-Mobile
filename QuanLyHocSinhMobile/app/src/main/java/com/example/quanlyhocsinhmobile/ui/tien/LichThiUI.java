package com.example.quanlyhocsinhmobile.ui.tien;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.Connection.AppDatabase;
import com.example.quanlyhocsinhmobile.data.DAO.LichThiDAO;
import com.example.quanlyhocsinhmobile.data.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.DAO.PhongHocDAO;
import com.example.quanlyhocsinhmobile.data.Model.LichThi;
import com.example.quanlyhocsinhmobile.data.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.Model.PhongHoc;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LichThiUI extends AppCompatActivity {

    private EditText etSearch, etNgayThi, etGioBD, etGioKT, etTenKyThi;
    private Spinner spinnerMon, spinnerPhong, spinnerFilterMon, spinnerFilterPhong;
    private Button btnSearch, btnFilter, btnAdd, btnSave, btnDelete, btnRefresh, btnExport;
    private RecyclerView rvLichThi;

    private LichThiDAO lichThiDAO;
    private MonHocDAO monHocDAO;
    private PhongHocDAO phongHocDAO;
    
    private LichThiAdapter adapter;
    private List<LichThi.Display> currentList = new ArrayList<>();
    private LichThi selectedLichThi;

    private List<MonHoc> listMonHoc = new ArrayList<>();
    private List<PhongHoc> listPhongHoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_lichthi);

        initDatabase();
        initViews();
        setupRecyclerView();
        loadSpinners();
        loadData();
        setupButtonActions();
    }

    private void initDatabase() {
        AppDatabase db = AppDatabase.getDatabase(this);
        lichThiDAO = db.lichThiDAO();
        monHocDAO = db.monHocDAO();
        phongHocDAO = db.phongHocDAO();
    }

    private void initViews() {
        // Section 1: Filter
        spinnerFilterMon = findViewById(R.id.spinner_filter_mon);
        spinnerFilterPhong = findViewById(R.id.spinner_filter_phong);
        btnFilter = findViewById(R.id.btn_filter_lichthi);
        btnRefresh = findViewById(R.id.btn_refresh_lichthi);

        // Section 2: Search
        etSearch = findViewById(R.id.et_search_lichthi);
        btnSearch = findViewById(R.id.btn_search_lichthi);

        // Section 3: List
        rvLichThi = findViewById(R.id.rv_lichthi);
        rvLichThi.setLayoutManager(new LinearLayoutManager(this));

        // Section 4: Form
        etTenKyThi = findViewById(R.id.et_form_tenkythi);
        etNgayThi = findViewById(R.id.et_form_ngaythi);
        etGioBD = findViewById(R.id.et_form_giobd);
        etGioKT = findViewById(R.id.et_form_giokt);
        spinnerMon = findViewById(R.id.spinner_form_mon);
        spinnerPhong = findViewById(R.id.spinner_form_phong);
        
        btnSave = findViewById(R.id.btn_save_lichthi);
        btnAdd = findViewById(R.id.btn_add_lichthi);
        btnDelete = findViewById(R.id.btn_delete_lichthi);
        btnExport = findViewById(R.id.btn_export_excel_lichthi);

        setupDateTimePickers();
    }

    private void setupDateTimePickers() {
        etNgayThi.setFocusable(false);
        etNgayThi.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
                etNgayThi.setText(date);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        etGioBD.setFocusable(false);
        etGioBD.setOnClickListener(v -> showTimePicker(etGioBD));

        etGioKT.setFocusable(false);
        etGioKT.setOnClickListener(v -> showTimePicker(etGioKT));
    }

    private void showTimePicker(EditText editText) {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editText.setText(time);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    private void setupButtonActions() {
        btnFilter.setOnClickListener(v -> {
            String maMH = "";
            int monPos = spinnerFilterMon.getSelectedItemPosition();
            if (monPos > 0) maMH = listMonHoc.get(monPos - 1).getMaMH();

            String maPhong = "";
            int phongPos = spinnerFilterPhong.getSelectedItemPosition();
            if (phongPos > 0) maPhong = listPhongHoc.get(phongPos - 1).getMaPhong();

            currentList = lichThiDAO.filterLichThi(maMH, maPhong);
            adapter.setLichThiList(currentList);
        });

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString();
            if (!query.isEmpty()) {
                currentList = lichThiDAO.searchLichThi("%" + query + "%");
                adapter.setLichThiList(currentList);
            } else {
                loadData();
            }
        });

        btnAdd.setOnClickListener(v -> addNewLichThi());
        btnSave.setOnClickListener(v -> updateLichThi());
        btnDelete.setOnClickListener(v -> deleteLichThi());
        btnRefresh.setOnClickListener(v -> {
            refreshForm();
            loadData();
            if (spinnerFilterMon.getAdapter().getCount() > 0) spinnerFilterMon.setSelection(0);
            if (spinnerFilterPhong.getAdapter().getCount() > 0) spinnerFilterPhong.setSelection(0);
            etSearch.setText("");
        });
        btnExport.setOnClickListener(v -> exportToExcel());
    }

    private void exportToExcel() {
        if (currentList == null || currentList.isEmpty()) {
            Toast.makeText(this, "Danh sách trống, không thể xuất!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("LichThi");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã LT");
        headerRow.createCell(1).setCellValue("Tên Kỳ Thi");
        headerRow.createCell(2).setCellValue("Môn Học");
        headerRow.createCell(3).setCellValue("Phòng");
        headerRow.createCell(4).setCellValue("Ngày Thi");
        headerRow.createCell(5).setCellValue("Giờ Bắt Đầu");
        headerRow.createCell(6).setCellValue("Giờ Kết Thúc");

        for (int i = 0; i < currentList.size(); i++) {
            LichThi.Display display = currentList.get(i);
            LichThi lt = display.getLichThi();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(lt.getMaLT());
            row.createCell(1).setCellValue(lt.getTenKyThi());
            row.createCell(2).setCellValue(display.getTenMH() != null ? display.getTenMH() : lt.getMaMH());
            row.createCell(3).setCellValue(display.getTenPhong() != null ? display.getTenPhong() : lt.getMaPhong());
            row.createCell(4).setCellValue(lt.getNgayThi());
            row.createCell(5).setCellValue(lt.getGioBatDau());
            row.createCell(6).setCellValue(lt.getGioKetThuc());
        }

        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadDir.exists()) downloadDir.mkdirs();

            String fileName = "LichThi_" + System.currentTimeMillis() + ".xlsx";
            File file = new File(downloadDir, fileName);
            
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            Toast.makeText(this, "Đã lưu tại thư mục Download: " + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi xuất Excel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        adapter = new LichThiAdapter(currentList, display -> {
            selectedLichThi = display.getLichThi();
            displaySelectedLichThi();
        });
        rvLichThi.setAdapter(adapter);
    }

    private void loadSpinners() {
        listMonHoc = monHocDAO.getAll();
        List<String> monHocNames = new ArrayList<>();
        List<String> filterMonNames = new ArrayList<>();
        filterMonNames.add("-- Tất cả môn học --");
        for (MonHoc mh : listMonHoc) {
            monHocNames.add(mh.getTenMH());
            filterMonNames.add(mh.getTenMH());
        }
        spinnerMon.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monHocNames));
        spinnerFilterMon.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filterMonNames));

        listPhongHoc = phongHocDAO.getAll();
        List<String> phongHocNames = new ArrayList<>();
        List<String> filterPhongNames = new ArrayList<>();
        filterPhongNames.add("-- Tất cả phòng --");
        for (PhongHoc ph : listPhongHoc) {
            phongHocNames.add(ph.getTenPhong());
            filterPhongNames.add(ph.getTenPhong());
        }
        spinnerPhong.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongHocNames));
        spinnerFilterPhong.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filterPhongNames));
    }

    private void loadData() {
        currentList = lichThiDAO.getAll();
        adapter.setLichThiList(currentList);
    }

    private void displaySelectedLichThi() {
        if (selectedLichThi != null) {
            etTenKyThi.setText(selectedLichThi.getTenKyThi());
            etNgayThi.setText(selectedLichThi.getNgayThi());
            etGioBD.setText(selectedLichThi.getGioBatDau());
            etGioKT.setText(selectedLichThi.getGioKetThuc());

            for (int i = 0; i < listMonHoc.size(); i++) {
                if (listMonHoc.get(i).getMaMH().equals(selectedLichThi.getMaMH())) {
                    spinnerMon.setSelection(i);
                    break;
                }
            }
            for (int i = 0; i < listPhongHoc.size(); i++) {
                if (listPhongHoc.get(i).getMaPhong().equals(selectedLichThi.getMaPhong())) {
                    spinnerPhong.setSelection(i);
                    break;
                }
            }
        }
    }

    private void addNewLichThi() {
        String ten = etTenKyThi.getText().toString();
        if (ten.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên kỳ thi", Toast.LENGTH_SHORT).show();
            return;
        }

        LichThi newLich = new LichThi();
        newLich.setTenKyThi(ten);
        newLich.setNgayThi(etNgayThi.getText().toString());
        newLich.setGioBatDau(etGioBD.getText().toString());
        newLich.setGioKetThuc(etGioKT.getText().toString());

        int monPos = spinnerMon.getSelectedItemPosition();
        if (monPos >= 0 && !listMonHoc.isEmpty()) {
            newLich.setMaMH(listMonHoc.get(monPos).getMaMH());
        }

        int phongPos = spinnerPhong.getSelectedItemPosition();
        if (phongPos >= 0 && !listPhongHoc.isEmpty()) {
            newLich.setMaPhong(listPhongHoc.get(phongPos).getMaPhong());
        }

        lichThiDAO.insert(newLich);
        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        
        loadData();
        refreshForm();
    }

    private void updateLichThi() {
        if (selectedLichThi == null) {
            Toast.makeText(this, "Chưa chọn lịch thi", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedLichThi.setTenKyThi(etTenKyThi.getText().toString());
        selectedLichThi.setNgayThi(etNgayThi.getText().toString());
        selectedLichThi.setGioBatDau(etGioBD.getText().toString());
        selectedLichThi.setGioKetThuc(etGioKT.getText().toString());

        int monPos = spinnerMon.getSelectedItemPosition();
        if (monPos >= 0 && !listMonHoc.isEmpty()) {
            selectedLichThi.setMaMH(listMonHoc.get(monPos).getMaMH());
        }

        int phongPos = spinnerPhong.getSelectedItemPosition();
        if (phongPos >= 0 && !listPhongHoc.isEmpty()) {
            selectedLichThi.setMaPhong(listPhongHoc.get(phongPos).getMaPhong());
        }

        lichThiDAO.update(selectedLichThi);
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        loadData();
    }

    private void deleteLichThi() {
        if (selectedLichThi == null) {
            Toast.makeText(this, "Vui lòng chọn lịch thi để xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        lichThiDAO.delete(selectedLichThi);
        refreshForm();
        loadData();
        Toast.makeText(this, "Đã xóa lịch thi", Toast.LENGTH_SHORT).show();
    }

    private void refreshForm() {
        selectedLichThi = null;
        etTenKyThi.setText("");
        etNgayThi.setText("");
        etGioBD.setText("");
        etGioKT.setText("");
        if (spinnerMon.getAdapter().getCount() > 0) spinnerMon.setSelection(0);
        if (spinnerPhong.getAdapter().getCount() > 0) spinnerPhong.setSelection(0);
    }
}
