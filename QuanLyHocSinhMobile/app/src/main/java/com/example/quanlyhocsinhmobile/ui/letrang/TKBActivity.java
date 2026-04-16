package com.example.quanlyhocsinhmobile.ui.letrang;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.ThoiKhoaBieu;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;

import java.util.ArrayList;
import java.util.List;

public class TKBActivity extends AppCompatActivity {

    private Spinner spinnerClass, spinnerSubject, spinnerThu;
    private Button btnFilter;
    private RecyclerView rvTkb;

    private TextView tvTkbInfo, tvUpdateTitle, tvTitleTkb;
    private Spinner spUpdateTietBD, spUpdateTietKT, spUpdateClass, spUpdateSubject, spUpdateRoom, spUpdateTeacher, spUpdateThu;
    private Button btnAdd, btnSave, btnDelete, btnRefresh, btnExcel;
    private View cardUpdate, layoutTkbForm;

    private TKBViewModel viewModel;
    private TKBAdapter adapter;
    private PhanQuyen phanQuyen;
    
    private List<Lop> listLop = new ArrayList<>();
    private List<MonHoc> listMonHoc = new ArrayList<>();
    private List<GiaoVien.Display> listGiaoVien = new ArrayList<>();
    private List<PhongHoc> listPhongHoc = new ArrayList<>();

    private ThoiKhoaBieu currentSelectedTKB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.letrang_activity_tkb);

        phanQuyen = PhanQuyen.getInstance(this);
        viewModel = new ViewModelProvider(this).get(TKBViewModel.class);

        initViews();
        setupRecyclerView();
        observeViewModel();
        apDungPhanQuyen();
    }

    private void apDungPhanQuyen() {
        if ("GiaoVien".equals(phanQuyen.getQuyen()) || "HocSinh".equals(phanQuyen.getQuyen())) {
            // Chỉ ẩn tiêu đề và phần Form nhập liệu, giữ lại Card để hiện nút Làm mới/Excel
            if (tvUpdateTitle != null) tvUpdateTitle.setVisibility(View.GONE);
            if (layoutTkbForm != null) layoutTkbForm.setVisibility(View.GONE);
            
            // Đảm bảo Card vẫn hiện để chứa các nút chức năng chung
            if (cardUpdate != null) cardUpdate.setVisibility(View.VISIBLE);
        }
        
        if ("HocSinh".equals(phanQuyen.getQuyen())) {
            if (spinnerClass != null) spinnerClass.setEnabled(false);
            viewModel.loadMaLopHocSinh(phanQuyen.getMaNguoiDung());
        }
    }

    private void initViews() {
        tvTitleTkb = findViewById(R.id.tv_title_tkb);
        spinnerClass = findViewById(R.id.spinner_class);
        spinnerSubject = findViewById(R.id.spinner_subject);
        spinnerThu = findViewById(R.id.spinner_thu);
        btnFilter = findViewById(R.id.btn_filter);
        rvTkb = findViewById(R.id.rv_tkb);
        rvTkb.setLayoutManager(new LinearLayoutManager(this));

        tvUpdateTitle = findViewById(R.id.tv_update_title);
        cardUpdate = findViewById(R.id.card_update);
        layoutTkbForm = findViewById(R.id.layout_tkb_form);
        
        tvTkbInfo = findViewById(R.id.tv_tkb_info);
        spUpdateTietBD = findViewById(R.id.spinner_tietBD);
        spUpdateTietKT = findViewById(R.id.spinner_tietKT);
        spUpdateClass = findViewById(R.id.spinner_update_class);
        spUpdateSubject = findViewById(R.id.spinner_update_subject);
        spUpdateRoom = findViewById(R.id.spinner_update_room);
        spUpdateTeacher = findViewById(R.id.spinner_update_teacher);
        spUpdateThu = findViewById(R.id.spinner_update_thu);

        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnExcel = findViewById(R.id.btn_excel);

        btnFilter.setOnClickListener(v -> performFilter());

        if (btnAdd != null) btnAdd.setOnClickListener(v -> handleAdd());
        if (btnSave != null) btnSave.setOnClickListener(v -> handleUpdate());
        if (btnDelete != null) btnDelete.setOnClickListener(v -> viewModel.delete(currentSelectedTKB));
        
        btnRefresh.setOnClickListener(v -> {
            if ("HocSinh".equals(phanQuyen.getQuyen())) {
                String maLop = viewModel.getMaLopHocSinh().getValue();
                viewModel.loadTKB(0, maLop != null ? maLop : "", "");
            } else {
                viewModel.loadTKB(0, "", "");
            }
            clearInputs();
        });

        if (btnExcel != null) btnExcel.setOnClickListener(v -> Toast.makeText(this, "Xuất file Excel...", Toast.LENGTH_SHORT).show());
    }

    private void performFilter() {
        String maLop = "";
        int lopPos = spinnerClass.getSelectedItemPosition();
        if (lopPos > 0 && !listLop.isEmpty()) maLop = listLop.get(lopPos - 1).getMaLop();
        
        if ("HocSinh".equals(phanQuyen.getQuyen())) {
            String studentClass = viewModel.getMaLopHocSinh().getValue();
            if (studentClass != null) maLop = studentClass;
        }

        String maMH = "";
        int monPos = spinnerSubject.getSelectedItemPosition();
        if (monPos > 0 && !listMonHoc.isEmpty()) maMH = listMonHoc.get(monPos - 1).getMaMH();

        int thuPos = spinnerThu.getSelectedItemPosition();
        int queryThu = (thuPos == 0) ? 0 : thuPos + 1;

        viewModel.loadTKB(queryThu, maLop, maMH);
    }

    private void handleAdd() {
        ThoiKhoaBieu tkb = new ThoiKhoaBieu();
        if (fillTKBFromInputs(tkb)) {
            viewModel.insert(tkb);
        }
    }

    private void handleUpdate() {
        if (currentSelectedTKB == null) {
            Toast.makeText(this, "Vui lòng chọn một dòng để sửa", Toast.LENGTH_SHORT).show();
            return;
        }
        ThoiKhoaBieu tkb = new ThoiKhoaBieu();
        tkb.setMaTKB(currentSelectedTKB.getMaTKB());
        if (fillTKBFromInputs(tkb)) {
            viewModel.update(tkb);
        }
    }

    private boolean fillTKBFromInputs(ThoiKhoaBieu tkb) {
        int lopPos = spUpdateClass.getSelectedItemPosition();
        int monPos = spUpdateSubject.getSelectedItemPosition();
        int gvPos = spUpdateTeacher.getSelectedItemPosition();
        int phongPos = spUpdateRoom.getSelectedItemPosition();
        int thuPos = spUpdateThu.getSelectedItemPosition();
        int tbdPos = spUpdateTietBD.getSelectedItemPosition();
        int tktPos = spUpdateTietKT.getSelectedItemPosition();

        if (lopPos == 0 || monPos == 0 || gvPos == 0 || phongPos == 0 || thuPos == 0 || tbdPos == 0 || tktPos == 0) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (tbdPos >= tktPos) {
            Toast.makeText(this, "Tiết kết thúc phải lớn hơn tiết bắt đầu", Toast.LENGTH_SHORT).show();
            return false;
        }

        tkb.setMaLop(listLop.get(lopPos - 1).getMaLop());
        tkb.setMaMH(listMonHoc.get(monPos - 1).getMaMH());
        tkb.setMaGV(listGiaoVien.get(gvPos - 1).getGiaoVien().getMaGV());
        tkb.setMaPhong(listPhongHoc.get(phongPos - 1).getMaPhong());
        tkb.setThu(thuPos + 1);
        tkb.setTietBatDau(tbdPos);
        tkb.setTietKetThuc(tktPos);
        
        return true;
    }

    private void setupRecyclerView() {
        adapter = new TKBAdapter(new ArrayList<>(), display -> {
            if ("GiaoVien".equals(phanQuyen.getQuyen()) || "HocSinh".equals(phanQuyen.getQuyen())) return;
            
            currentSelectedTKB = display.getThoiKhoaBieu();
            if (tvTkbInfo != null) tvTkbInfo.setText("Đang chọn: " + display.getTenMH() + " - Lớp " + display.getTenLop());
            
            setSpinnerSelection(spUpdateClass, display.getTenLop());
            setSpinnerSelection(spUpdateSubject, display.getTenMH());
            setSpinnerSelection(spUpdateTeacher, display.getTenGV());
            setSpinnerSelection(spUpdateRoom, display.getTenPhong());
            
            if (spUpdateTietBD != null) spUpdateTietBD.setSelection(currentSelectedTKB.getTietBatDau());
            if (spUpdateTietKT != null) spUpdateTietKT.setSelection(currentSelectedTKB.getTietKetThuc());
            if (spUpdateThu != null) spUpdateThu.setSelection(currentSelectedTKB.getThu() - 1);
        });
        rvTkb.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getTkbList().observe(this, list -> {
            if ("HocSinh".equals(phanQuyen.getQuyen())) {
                String maLop = viewModel.getMaLopHocSinh().getValue();
                if (maLop != null) {
                    List<ThoiKhoaBieu.Display> filtered = new ArrayList<>();
                    for (ThoiKhoaBieu.Display d : list) {
                        if (maLop.equals(d.getThoiKhoaBieu().getMaLop())) filtered.add(d);
                    }
                    adapter.setTkbList(filtered);
                } else {
                    adapter.setTkbList(list);
                }
            } else {
                adapter.setTkbList(list);
            }
        });
        
        viewModel.getMaLopHocSinh().observe(this, maLop -> {
            if ("HocSinh".equals(phanQuyen.getQuyen()) && maLop != null) {
                viewModel.loadTKB(0, maLop, "");
                setSpinnerSelectionByMaLop(spinnerClass, maLop);
            }
        });
        
        viewModel.getLops().observe(this, lops -> {
            this.listLop = lops;
            updateSpinner(spinnerClass, lops, "--- Lớp ---", true);
            if (spUpdateClass != null) updateSpinner(spUpdateClass, lops, "--- Chọn lớp ---", true);
            
            if ("HocSinh".equals(phanQuyen.getQuyen())) {
                String maLop = viewModel.getMaLopHocSinh().getValue();
                if (maLop != null) setSpinnerSelectionByMaLop(spinnerClass, maLop);
            }
        });

        viewModel.getMonHocs().observe(this, monHocs -> {
            this.listMonHoc = monHocs;
            updateSpinner(spinnerSubject, monHocs, "--- Môn ---", false);
            if (spUpdateSubject != null) updateSpinner(spUpdateSubject, monHocs, "--- Chọn môn ---", false);
        });

        viewModel.getGiaoViens().observe(this, gvs -> {
            this.listGiaoVien = gvs;
            List<String> names = new ArrayList<>();
            names.add("--- Chọn GV ---");
            for (GiaoVien.Display g : gvs) names.add(g.getGiaoVien().getHoTen());
            if (spUpdateTeacher != null) spUpdateTeacher.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names));
        });

        viewModel.getPhongHocs().observe(this, phongs -> {
            this.listPhongHoc = phongs;
            List<String> names = new ArrayList<>();
            names.add("--- Chọn phòng ---");
            for (PhongHoc p : phongs) names.add(p.getTenPhong());
            if (spUpdateRoom != null) spUpdateRoom.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names));
        });

        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (message.contains("thành công")) clearInputs();
            }
        });

        String[] listThu = {"--- Thứ ---", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        spinnerThu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listThu));
        
        String[] listThuUpdate = {"--- Chọn thứ ---", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        if (spUpdateThu != null) spUpdateThu.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listThuUpdate));

        String[] listTiet = {"--- Tiết ---", "Tiết 1", "Tiết 2", "Tiết 3", "Tiết 4", "Tiết 5", "Tiết 6", "Tiết 7", "Tiết 8", "Tiết 9", "Tiết 10"};
        if (spUpdateTietBD != null) spUpdateTietBD.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listTiet));
        if (spUpdateTietKT != null) spUpdateTietKT.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listTiet));
    }

    private void updateSpinner(Spinner spinner, List<?> data, String hint, boolean isLop) {
        if (spinner == null) return;
        List<String> names = new ArrayList<>();
        names.add(hint);
        for (Object item : data) {
            if (isLop) names.add(((Lop) item).getTenLop());
            else names.add(((MonHoc) item).getTenMH());
        }
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names));
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        if (spinner != null && spinner.getAdapter() instanceof ArrayAdapter) {
            ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
            int position = adapter.getPosition(value);
            if (position >= 0) spinner.setSelection(position);
        }
    }
    
    private void setSpinnerSelectionByMaLop(Spinner spinner, String maLop) {
        if (spinner != null && !listLop.isEmpty()) {
            for (int i = 0; i < listLop.size(); i++) {
                if (listLop.get(i).getMaLop().equals(maLop)) {
                    spinner.setSelection(i + 1);
                    break;
                }
            }
        }
    }

    private void clearInputs() {
        currentSelectedTKB = null;
        if (tvTkbInfo != null) tvTkbInfo.setText("Tiết học: --");
        if (spUpdateClass != null) spUpdateClass.setSelection(0);
        if (spUpdateSubject != null) spUpdateSubject.setSelection(0);
        if (spUpdateTeacher != null) spUpdateTeacher.setSelection(0);
        if (spUpdateRoom != null) spUpdateRoom.setSelection(0);
        if (spUpdateThu != null) spUpdateThu.setSelection(0);
        if (spUpdateTietBD != null) spUpdateTietBD.setSelection(0);
        if (spUpdateTietKT != null) spUpdateTietKT.setSelection(0);
    }
}
