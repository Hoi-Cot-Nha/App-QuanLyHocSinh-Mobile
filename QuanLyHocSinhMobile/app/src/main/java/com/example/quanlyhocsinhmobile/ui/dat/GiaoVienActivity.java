package com.example.quanlyhocsinhmobile.ui.dat;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.databinding.DatActivityGiaovienBinding;
import com.example.quanlyhocsinhmobile.utils.FormatDate;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;

import java.util.ArrayList;

public class GiaoVienActivity extends AppCompatActivity {

    private DatActivityGiaovienBinding binding;
    private GiaoVienViewModel viewModel;
    private GiaoVienAdapter adapter;

    private GiaoVien.Display selectedGiaoVien;
    private PhanQuyen phanQuyen;

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
    }

    private void apDungPhanQuyen() {
        if ("GiaoVien".equals(phanQuyen.getQuyen())) {
            // Sử dụng các ID đúng đã có trong file XML
            if (binding.tvTitleGiaovien != null) {
                binding.tvTitleGiaovien.setText("HỒ SƠ GIÁO VIÊN");
            }
            binding.tvGiaoVienInfo.setText("HỒ SƠ GIÁO VIÊN");
            
            // Ẩn nút thêm, xóa
            binding.btnAdd.setVisibility(View.GONE);
            binding.btnDelete.setVisibility(View.GONE);
            
            // Chỉ cho sửa tên, ngày sinh, sdt. Khóa các trường khác.
            binding.etMaGiaoVien.setEnabled(false);
            binding.etMaToHop.setEnabled(false);
            binding.etTenMon.setEnabled(false);
            
            // Chỉ load hồ sơ của chính giáo viên đó
            String maGV = phanQuyen.getMaNguoiDung();
            if (maGV != null && !maGV.isEmpty()) {
                viewModel.search(maGV);
            }
            if (binding.tvFilterTitle != null) binding.tvFilterTitle.setVisibility(View.GONE);
            if (binding.cardSearch != null) binding.cardSearch.setVisibility(View.GONE);
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
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                clearForm();
            }
        });

        binding.btnSave.setOnClickListener(v -> {
            if (selectedGiaoVien == null) return;

            GiaoVien gv = getFormData();
            if (gv != null) {
                gv.setMaGV(selectedGiaoVien.getGiaoVien().getMaGV());
                viewModel.update(gv);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
        String ma = binding.etMaGiaoVien.getText().toString().trim();
        String ten = binding.etTenGv.getText().toString().trim();
        String ngaySinhInput = binding.etNgaySinh.getText().toString().trim();
        String sdt = binding.etSdt.getText().toString().trim();
        String maToHop = binding.etMaToHop.getText().toString().trim();
        String maMH = binding.etTenMon.getText().toString().trim();
        String ngaySinh = FormatDate.normalizeDateToStorage(ngaySinhInput);

        if (ma.isEmpty() || ten.isEmpty()) {
            Toast.makeText(this, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
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
        binding.etMaToHop.setText(gv.getMaToHop());
        binding.etTenMon.setText(gv.getMaMH());
    }

    private void clearForm() {
        selectedGiaoVien = null;
        binding.etMaGiaoVien.setText("");
        binding.etTenGv.setText("");
        binding.etNgaySinh.setText("");
        binding.etSdt.setText("");
        binding.etMaToHop.setText("");
        binding.etTenMon.setText("");
    }
}
