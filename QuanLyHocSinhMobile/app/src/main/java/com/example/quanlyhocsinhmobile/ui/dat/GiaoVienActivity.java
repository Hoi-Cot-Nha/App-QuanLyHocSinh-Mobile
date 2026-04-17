package com.example.quanlyhocsinhmobile.ui.dat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;
import com.example.quanlyhocsinhmobile.databinding.DatActivityGiaovienBinding;
import com.example.quanlyhocsinhmobile.utils.FormatDate;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;
import java.util.ArrayList;
import java.util.List;
public class GiaoVienActivity extends AppCompatActivity {
    private DatActivityGiaovienBinding binding;
    private GiaoVienViewModel viewModel;
    private GiaoVienAdapter adapter;
    private GiaoVien.Display selectedGiaoVien;
    private PhanQuyen phanQuyen;
    private ArrayAdapter<ToHopMon> toHopAdapter;
    private ArrayAdapter<MonHoc> monHocAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DatActivityGiaovienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        phanQuyen = PhanQuyen.getInstance(this);
        viewModel = new ViewModelProvider(this).get(GiaoVienViewModel.class);
        setupRecyclerView();
        observeViewModel();
        setupClickListeners();
        apDungPhanQuyen();
        viewModel.loadSpinnerData();
    }
    private void apDungPhanQuyen() {
        if ("GiaoVien".equals(phanQuyen.getQuyen())) {
            if (binding.tvTitleGiaovien != null) {
                binding.tvTitleGiaovien.setText("HỒ SƠ GIÁO VIÊN");
            }
            binding.tvGiaoVienInfo.setText("HỒ SƠ GIÁO VIÊN");
            binding.btnAdd.setVisibility(View.GONE);
            binding.btnDelete.setVisibility(View.GONE);
            binding.etMaGiaoVien.setEnabled(false);
            binding.spMaToHop.setEnabled(false);
            binding.spTenMon.setEnabled(false);
            String maGV = phanQuyen.getMaNguoiDung();
            if (maGV != null && !maGV.isEmpty()) {
                viewModel.search(maGV);
            }
            if (binding.tvFilterTitle != null) binding.tvFilterTitle.setVisibility(View.GONE);
            if (binding.cardSearch != null) binding.cardSearch.setVisibility(View.GONE);
        } else {
            binding.etMaGiaoVien.setEnabled(false);
        }
    }
    private void setupRecyclerView() {
        binding.rvGiaoVien.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GiaoVienAdapter(new ArrayList<>(), display -> {
            selectedGiaoVien = display;
            displaySelected();
        });
        binding.rvGiaoVien.setAdapter(adapter);
    }
    private void observeViewModel() {
        viewModel.getAllGiaoViens().observe(this, list -> {
            adapter.setList(list);
        });
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (message.contains("thành công")) {
                    clearForm();
                }
            }
        });
        viewModel.getToHopMonList().observe(this, toHops -> {
            if (toHops != null) {
                toHopAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, toHops);
                toHopAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spMaToHop.setAdapter(toHopAdapter);
            }
        });
        viewModel.getMonHocList().observe(this, monHocs -> {
            if (monHocs != null) {
                monHocAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monHocs);
                monHocAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spTenMon.setAdapter(monHocAdapter);
            }
        });
    }
    private void setupClickListeners() {
        binding.btnSearch.setOnClickListener(v -> {
            String q = binding.etSearch.getText().toString();
            if (!q.isEmpty()) viewModel.search(q.trim());
            else viewModel.loadAllGiaoViens();
        });
        binding.btnAdd.setOnClickListener(v -> {
            GiaoVien gv = getFormData();
            if (gv != null) {
                viewModel.insert(gv);
            }
        });
        binding.btnSave.setOnClickListener(v -> {
            if (selectedGiaoVien == null) return;
            GiaoVien gv = getFormData();
            if (gv != null) {
                gv.setMaGV(selectedGiaoVien.getGiaoVien().getMaGV());
                viewModel.update(gv);
            }
        });
        binding.btnDelete.setOnClickListener(v -> {
            if (selectedGiaoVien != null) {
                viewModel.delete(selectedGiaoVien.getGiaoVien());
                clearForm();
            }
        });
        binding.btnRefresh.setOnClickListener(v -> {
            clearForm();
            if ("GiaoVien".equals(phanQuyen.getQuyen())) {
                viewModel.search(phanQuyen.getMaNguoiDung());
            } else {
                viewModel.loadAllGiaoViens();
            }
        });
    }
    private GiaoVien getFormData() {
        String ma = ""; 
        String ten = binding.etTenGv.getText().toString().trim();
        String ngaySinhInput = binding.etNgaySinh.getText().toString().trim();
        String sdt = binding.etSdt.getText().toString().trim();
        ToHopMon selectedToHop = (ToHopMon) binding.spMaToHop.getSelectedItem();
        MonHoc selectedMonHoc = (MonHoc) binding.spTenMon.getSelectedItem();
        String maToHop = selectedToHop != null ? selectedToHop.getMaToHop() : "";
        String maMH = selectedMonHoc != null ? selectedMonHoc.getMaMH() : "";
        String ngaySinh = FormatDate.normalizeDateToStorage(ngaySinhInput);
        if (ten.isEmpty()) {
            Toast.makeText(this, "Nhập tên giáo viên", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (maToHop.isEmpty() || maMH.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn tổ hợp và môn học", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!ngaySinhInput.isEmpty() && ngaySinh == null) {
            Toast.makeText(this, "Ngày sinh không đúng định dạng dd/MM/yyyy", Toast.LENGTH_SHORT).show();
            return null;
        }
        GiaoVien gv = new GiaoVien();
        gv.setMaGV(ma);  
        gv.setHoTen(ten);
        gv.setNgaySinh(ngaySinh);
        gv.setSdt(sdt);
        gv.setMaToHop(maToHop);
        gv.setMaMH(maMH);
        return gv;
    }
    private void displaySelected() {
        if (selectedGiaoVien == null) return;
        GiaoVien gv = selectedGiaoVien.getGiaoVien();
        binding.etMaGiaoVien.setText(gv.getMaGV());
        binding.etTenGv.setText(gv.getHoTen());
        binding.etNgaySinh.setText(FormatDate.formatDateForDisplay(gv.getNgaySinh()));
        binding.etSdt.setText(gv.getSdt());
        if (toHopAdapter != null) {
            for (int i = 0; i < toHopAdapter.getCount(); i++) {
                if (toHopAdapter.getItem(i).getMaToHop().equals(gv.getMaToHop())) {
                    binding.spMaToHop.setSelection(i);
                    break;
                }
            }
        }
        if (monHocAdapter != null) {
            for (int i = 0; i < monHocAdapter.getCount(); i++) {
                if (monHocAdapter.getItem(i).getMaMH().equals(gv.getMaMH())) {
                    binding.spTenMon.setSelection(i);
                    break;
                }
            }
        }
    }
    private void clearForm() {
        selectedGiaoVien = null;
        binding.etMaGiaoVien.setText("");
        binding.etTenGv.setText("");
        binding.etNgaySinh.setText("");
        binding.etSdt.setText("");
        if (binding.spMaToHop.getCount() > 0) binding.spMaToHop.setSelection(0);
        if (binding.spTenMon.getCount() > 0) binding.spTenMon.setSelection(0);
    }
}