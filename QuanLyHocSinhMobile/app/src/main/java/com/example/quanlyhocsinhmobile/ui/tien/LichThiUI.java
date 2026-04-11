package com.example.quanlyhocsinhmobile.ui.tien;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.quanlyhocsinhmobile.data.Model.LichThiDisplay;
import com.example.quanlyhocsinhmobile.data.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.Model.PhongHoc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private Spinner spinnerMon, spinnerPhong;
    private Button btnSearch;
    private RecyclerView rvLichThi;

    private FloatingActionButton fabOptions, fabAdd, fabSave, fabDelete, fabRefresh, fabExcel;
    private View layoutCircularMenu;
    private boolean isMenuOpen = false;

    private LichThiDAO lichThiDAO;
    private MonHocDAO monHocDAO;
    private PhongHocDAO phongHocDAO;
    private LichThiAdapter adapter;
    private List<LichThiDisplay> currentList = new ArrayList<>();
    private LichThi selectedLichThi;

    private List<MonHoc> listMonHoc = new ArrayList<>();
    private List<PhongHoc> listPhongHoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_lichthi);

        initViews();
        initDatabase();
        setupRecyclerView();
        loadSpinners();
        loadData();
        setupMenuActions();

        //ivBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString();
            if (!query.isEmpty()) {
                currentList = lichThiDAO.searchLichThi("%" + query + "%");
                adapter.setLichThiList(currentList);
            } else {
                loadData();
            }
        });
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search_lichthi);
        etTenKyThi = findViewById(R.id.et_form_tenkythi);
        etNgayThi = findViewById(R.id.et_form_ngaythi);
        etGioBD = findViewById(R.id.et_form_giobd);
        etGioKT = findViewById(R.id.et_form_giokt);
        spinnerMon = findViewById(R.id.spinner_form_mon);
        spinnerPhong = findViewById(R.id.spinner_form_phong);
        btnSearch = findViewById(R.id.btn_search_lichthi);
        rvLichThi = findViewById(R.id.rv_lichthi);

        fabOptions = findViewById(R.id.fab_options);
        fabAdd = findViewById(R.id.fab_add);
        fabSave = findViewById(R.id.fab_save);
        fabDelete = findViewById(R.id.fab_delete);
        fabRefresh = findViewById(R.id.fab_refresh);
        fabExcel = findViewById(R.id.fab_excel);
        layoutCircularMenu = findViewById(R.id.layout_circular_menu);

        rvLichThi.setLayoutManager(new LinearLayoutManager(this));

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

    private void setupMenuActions() {
        fabOptions.setOnClickListener(v -> toggleMenu());
        layoutCircularMenu.setOnClickListener(v -> toggleMenu());

        fabAdd.setOnClickListener(v -> {
            addNewLichThi();
            toggleMenu();
        });

        fabSave.setOnClickListener(v -> {
            updateLichThi();
            toggleMenu();
        });

        fabDelete.setOnClickListener(v -> {
            deleteLichThi();
            toggleMenu();
        });

        fabRefresh.setOnClickListener(v -> {
            refreshForm();
            toggleMenu();
        });

        fabExcel.setOnClickListener(v -> {
            exportToExcel();
            toggleMenu();
        });
    }

    private void exportToExcel() {
        if (currentList.isEmpty()) {
            Toast.makeText(this, "Danh sách trống, không thể xuất!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("LichThi");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã LT");
        headerRow.createCell(1).setCellValue("Tên Kỳ Thi");
        headerRow.createCell(2).setCellValue("Môn Học");
        headerRow.createCell(3).setCellValue("Phòng");
        headerRow.createCell(4).setCellValue("Ngày Thi");
        headerRow.createCell(5).setCellValue("Giờ Bắt Đầu");
        headerRow.createCell(6).setCellValue("Giờ Kết Thúc");

        // Data
        for (int i = 0; i < currentList.size(); i++) {
            LichThiDisplay display = currentList.get(i);
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
            File cachePath = new File(getExternalFilesDir(null), "exports");
            if (!cachePath.exists()) cachePath.mkdirs();
            File file = new File(cachePath, "LichThi_" + System.currentTimeMillis() + ".xlsx");
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

    private void toggleMenu() {
        isMenuOpen = !isMenuOpen;
        if (isMenuOpen) {
            layoutCircularMenu.setVisibility(View.VISIBLE);
            fabOptions.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        } else {
            layoutCircularMenu.setVisibility(View.GONE);
            fabOptions.setImageResource(android.R.drawable.ic_menu_manage);
        }
    }

    private void initDatabase() {
        AppDatabase db = AppDatabase.getDatabase(this);
        lichThiDAO = db.lichThiDAO();
        monHocDAO = db.monHocDAO();
        phongHocDAO = db.phongHocDAO();
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
        for (MonHoc mh : listMonHoc) monHocNames.add(mh.getTenMH());
        spinnerMon.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monHocNames));

        listPhongHoc = phongHocDAO.getAll();
        List<String> phongHocNames = new ArrayList<>();
        for (PhongHoc ph : listPhongHoc) phongHocNames.add(ph.getTenPhong());
        spinnerPhong.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongHocNames));
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
        String tenKyThi = etTenKyThi.getText().toString();
        if (tenKyThi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên kỳ thi để thêm mới", Toast.LENGTH_SHORT).show();
            return;
        }
        
        LichThi newLich = new LichThi();
        newLich.setTenKyThi(tenKyThi);
        newLich.setNgayThi(etNgayThi.getText().toString());
        newLich.setGioBatDau(etGioBD.getText().toString());
        newLich.setGioKetThuc(etGioKT.getText().toString());
        
        if (spinnerMon.getSelectedItemPosition() >= 0 && !listMonHoc.isEmpty()) {
            MonHoc selectedMon = listMonHoc.get(spinnerMon.getSelectedItemPosition());
            newLich.setMaMH(selectedMon.getMaMH());
        }
        if (spinnerPhong.getSelectedItemPosition() >= 0 && !listPhongHoc.isEmpty()) {
            PhongHoc selectedPhong = listPhongHoc.get(spinnerPhong.getSelectedItemPosition());
            newLich.setMaPhong(selectedPhong.getMaPhong());
        }

        lichThiDAO.insert(newLich);
        loadData();
        refreshForm();
        Toast.makeText(this, "Thêm lịch thi mới thành công", Toast.LENGTH_SHORT).show();
    }

    private void updateLichThi() {
        if (selectedLichThi == null) {
            Toast.makeText(this, "Vui lòng chọn lịch thi từ danh sách để sửa", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedLichThi.setTenKyThi(etTenKyThi.getText().toString());
        selectedLichThi.setNgayThi(etNgayThi.getText().toString());
        selectedLichThi.setGioBatDau(etGioBD.getText().toString());
        selectedLichThi.setGioKetThuc(etGioKT.getText().toString());

        if (spinnerMon.getSelectedItemPosition() >= 0 && !listMonHoc.isEmpty()) {
            MonHoc selectedMon = listMonHoc.get(spinnerMon.getSelectedItemPosition());
            selectedLichThi.setMaMH(selectedMon.getMaMH());
        }

        if (spinnerPhong.getSelectedItemPosition() >= 0 && !listPhongHoc.isEmpty()) {
            PhongHoc selectedPhong = listPhongHoc.get(spinnerPhong.getSelectedItemPosition());
            selectedLichThi.setMaPhong(selectedPhong.getMaPhong());
        }

        lichThiDAO.update(selectedLichThi);
        loadData();
        Toast.makeText(this, "Cập nhật lịch thi thành công", Toast.LENGTH_SHORT).show();
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
