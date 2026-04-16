package com.example.quanlyhocsinhmobile.ui.dai;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.AppDatabase;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import com.example.quanlyhocsinhmobile.utils.FormatDate;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HocSinhActivity extends AppCompatActivity {

    private EditText etSearch;
    private TextInputEditText ma, ten, etNs, gt, dc;
    private Spinner spLop, spDT;
    private Button btnAdd, btnSave, btnDelete, btnSearch, btnRefresh;
    private RecyclerView rv;
    private PhanQuyen phanQuyen;

    private HocSinhViewModel vm;
    private HocSinhAdapter adapter;
    private HocSinh selected;

    private List<String> listLop = new ArrayList<>();
    private List<String> listDT = new ArrayList<>();

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.dai_activity_hocsinh);

        phanQuyen = PhanQuyen.getInstance(this);
        vm = new ViewModelProvider(this).get(HocSinhViewModel.class);

        initViews();
        loadSpinner();
        setupRecyclerView();
        observeViewModel();
        apDungPhanQuyen();
    }

    private void apDungPhanQuyen() {
        if ("GiaoVien".equals(phanQuyen.getQuyen())) {
            if (btnAdd != null) btnAdd.setVisibility(View.GONE);
            if (btnDelete != null) btnDelete.setVisibility(View.GONE);
        }
        
        if ("HocSinh".equals(phanQuyen.getQuyen())) {
            // Học sinh ẩn nút thêm và xóa
            if (btnAdd != null) btnAdd.setVisibility(View.GONE);
            if (btnDelete != null) btnDelete.setVisibility(View.GONE);
            
            // Không được sửa mã lớp và mã đối tượng
            if (spLop != null) spLop.setEnabled(false);
            if (spDT != null) spDT.setEnabled(false);
            
            // Chỉ thấy hồ sơ của chính mình
            String maHS = phanQuyen.getMaNguoiDung();
            if (maHS != null) {
                vm.search(maHS);
                // Ẩn tìm kiếm
                if (etSearch != null) etSearch.setVisibility(View.GONE);
                if (btnSearch != null) btnSearch.setVisibility(View.GONE);
            }
        }
    }

    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        ma = findViewById(R.id.et_ma);
        ten = findViewById(R.id.et_ten);
        etNs = findViewById(R.id.et_ns);
        gt = findViewById(R.id.et_gt);
        dc = findViewById(R.id.et_dc);
        spLop = findViewById(R.id.sp_lop);
        spDT = findViewById(R.id.sp_dt);
        rv = findViewById(R.id.rv);

        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        btnSearch = findViewById(R.id.btn_search);
        btnRefresh = findViewById(R.id.btn_refresh);

        etNs.setOnClickListener(v -> {
            etNs.setError(null);
            Calendar c = Calendar.getInstance();
            int defaultYear = 2009;

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, y, m, d) -> {
                        String display = String.format("%02d/%02d/%d", d, m + 1, y);
                        String normalized = FormatDate.normalizeDateToStorage(display);

                        etNs.setText(display);
                        etNs.setTag(normalized);
                        etNs.setError(null);
                    },
                    defaultYear,
                    c.get(Calendar.MONTH),
                    1
            );

            // 👉 GIỚI HẠN NĂM: 2008 - 2010
            Calendar minDate = Calendar.getInstance();
            minDate.set(2008, 0, 1); // 01/01/2008

            Calendar maxDate = Calendar.getInstance();
            maxDate.set(2010, 11, 31); // 31/12/2010

            dialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

            dialog.show();
        });

        // ===== BUTTON EVENTS =====
        btnAdd.setOnClickListener(v -> {
            if (validateInput()) {
                vm.insert(getData());
            }
        });

        btnSave.setOnClickListener(v -> {
            if (selected != null) {
                HocSinh hs = getData();
                hs.setMaHS(selected.getMaHS());
                vm.update(hs);
            } else {
                Toast.makeText(this, "Chọn học sinh để sửa", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (selected != null) {
                new AlertDialog.Builder(this)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có chắc muốn xóa học sinh này không?")
                        .setPositiveButton("XÓA", (dialog, which) -> vm.delete(selected))
                        .setNegativeButton("HỦY", null)
                        .show();
            } else {
                Toast.makeText(this, "Chọn học sinh để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        btnSearch.setOnClickListener(v ->
                vm.search(etSearch.getText().toString().trim())
        );

        btnRefresh.setOnClickListener(v -> {
            vm.loadAll();
            clearForm();
        });
    }

    // ================= RECYCLER VIEW =================
    private void setupRecyclerView() {
        adapter = new HocSinhAdapter(new ArrayList<>(), hs -> {
            selected = hs;

            ma.setText(hs.getMaHS());
            ten.setText(hs.getHoTen());

            if (hs.getNgaySinh() != null && !hs.getNgaySinh().trim().isEmpty()) {
                etNs.setText(FormatDate.formatDateForDisplay(hs.getNgaySinh()));
                etNs.setTag(FormatDate.normalizeDateToStorage(hs.getNgaySinh()));
            }

            gt.setText(hs.getGioiTinh());
            dc.setText(hs.getDiaChi());

            spLop.setSelection(listLop.indexOf(hs.getMaLop()));
            spDT.setSelection(listDT.indexOf(hs.getMaDT()));

            ma.setEnabled(false);
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    // ================= OBSERVE =================
    private void observeViewModel() {
        vm.getList().observe(this, adapter::setList);

        vm.getMsg().observe(this, m -> {
            if (m != null) {
                Toast.makeText(this, m, Toast.LENGTH_SHORT).show();
                if (m.contains("thành công")) {
                    clearForm();
                }
            }
        });
    }

    // ================= LOAD SPINNER =================
    private void loadSpinner() {
        new Thread(() -> {
            listLop = AppDatabase.getDatabase(this).lopDAO().getAllMaLop();
            listDT = AppDatabase.getDatabase(this).doiTuongDAO().getAllMaDT();

            runOnUiThread(() -> {
                ArrayAdapter<String> adLop = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, listLop);
                adLop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLop.setAdapter(adLop);

                ArrayAdapter<String> adDT = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, listDT);
                adDT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDT.setAdapter(adDT);
            });
        }).start();
    }

    // ================= GET DATA =================
    private HocSinh getData() {
        HocSinh hs = new HocSinh();

        hs.setMaHS(ma.getText().toString().trim());
        hs.setHoTen(ten.getText().toString().trim());
        hs.setNgaySinh(etNs.getTag() != null ? etNs.getTag().toString() : "");
        hs.setGioiTinh(gt.getText().toString().trim());
        hs.setDiaChi(dc.getText().toString().trim());
        hs.setMaLop(spLop.getSelectedItem().toString());
        hs.setMaDT(spDT.getSelectedItem().toString());

        return hs;
    }

    // ================= VALIDATE =================
    private boolean validateInput() {

        if (ma.getText().toString().trim().isEmpty()) {
            ma.setError("Không được để trống mã học sinh");
            ma.requestFocus();
            return false;
        }

        if (ten.getText().toString().trim().isEmpty()) {
            ten.setError("Không được để trống tên");
            ten.requestFocus();
            return false;
        }

        if (etNs.getTag() == null || etNs.getTag().toString().isEmpty()) {
            etNs.setError("Chọn ngày sinh");
            etNs.requestFocus();
            return false;
        }

        if (gt.getText().toString().trim().isEmpty()) {
            gt.setError("Không được để trống giới tính");
            gt.requestFocus();
            return false;
        }

        if (dc.getText().toString().trim().isEmpty()) {
            dc.setError("Không được để trống địa chỉ");
            dc.requestFocus();
            return false;
        }

        if (spLop.getSelectedItem() == null) {
            Toast.makeText(this, "Chọn lớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spDT.getSelectedItem() == null) {
            Toast.makeText(this, "Chọn đối tượng", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    private void clearForm() {
        selected = null;
        ma.setText("");
        ten.setText("");
        etNs.setText("");
        etNs.setTag(null);
        gt.setText("");
        dc.setText("");
        spLop.setSelection(0);
        spDT.setSelection(0);

        if (!"HocSinh".equals(phanQuyen.getQuyen())) {
            ma.setEnabled(true);
        }
        etSearch.setText("");
    }
}
