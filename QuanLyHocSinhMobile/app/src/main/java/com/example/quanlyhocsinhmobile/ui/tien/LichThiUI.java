package com.example.quanlyhocsinhmobile.ui.tien;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import com.example.quanlyhocsinhmobile.Controller_View.LichThiController;
import com.example.quanlyhocsinhmobile.data.Model.LichThi;
import com.example.quanlyhocsinhmobile.data.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.Model.PhongHoc;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private LichThiController controller;
    private LichThiAdapter adapter;
    private List<LichThi.Display> currentList = new ArrayList<>();
    private LichThi selectedLichThi;

    private List<MonHoc> listMonHoc = new ArrayList<>();
    private List<PhongHoc> listPhongHoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tien_lichthi);

        initViews();
        initController();
        setupRecyclerView();
        loadSpinners();
        loadData();
        setupMenuActions();

        btnSearch.setOnClickListener(v -> {
            String query = etSearch.getText().toString();
            if (!query.isEmpty()) {
                currentList = controller.searchLichThi(query);
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
        controller.exportToExcel(currentList);
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

    private void initController() {
        controller = new LichThiController(this);
    }

    private void setupRecyclerView() {
        adapter = new LichThiAdapter(currentList, display -> {
            selectedLichThi = display.getLichThi();
            displaySelectedLichThi();
        });
        rvLichThi.setAdapter(adapter);
    }

    private void loadSpinners() {
        listMonHoc = controller.getAllMonHoc();
        List<String> monHocNames = new ArrayList<>();
        for (MonHoc mh : listMonHoc) monHocNames.add(mh.getTenMH());
        spinnerMon.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monHocNames));

        listPhongHoc = controller.getAllPhongHoc();
        List<String> phongHocNames = new ArrayList<>();
        for (PhongHoc ph : listPhongHoc) phongHocNames.add(ph.getTenPhong());
        spinnerPhong.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, phongHocNames));
    }

    private void loadData() {
        currentList = controller.getAllLichThi();
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
        controller.insertLichThi(
                etTenKyThi.getText().toString(),
                etNgayThi.getText().toString(),
                etGioBD.getText().toString(),
                etGioKT.getText().toString(),
                spinnerMon.getSelectedItemPosition(),
                listMonHoc,
                spinnerPhong.getSelectedItemPosition(),
                listPhongHoc
        );
        loadData();
        refreshForm();
    }

    private void updateLichThi() {
        controller.updateLichThi(
                selectedLichThi,
                etTenKyThi.getText().toString(),
                etNgayThi.getText().toString(),
                etGioBD.getText().toString(),
                etGioKT.getText().toString(),
                spinnerMon.getSelectedItemPosition(),
                listMonHoc,
                spinnerPhong.getSelectedItemPosition(),
                listPhongHoc
        );
        loadData();
    }

    private void deleteLichThi() {
        if (selectedLichThi == null) {
            Toast.makeText(this, "Vui lòng chọn lịch thi để xóa", Toast.LENGTH_SHORT).show();
            return;
        }

        controller.deleteLichThi(selectedLichThi);
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
