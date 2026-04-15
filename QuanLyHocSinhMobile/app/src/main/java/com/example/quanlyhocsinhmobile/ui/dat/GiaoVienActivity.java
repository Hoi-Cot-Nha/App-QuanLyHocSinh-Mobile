package com.example.quanlyhocsinhmobile.ui.dat;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.databinding.DatActivityGiaovienBinding;

import java.util.ArrayList;

public class GiaoVienActivity extends AppCompatActivity {

    private DatActivityGiaovienBinding binding;
    private GiaoVienViewModel viewModel;
    private GiaoVienAdapter adapter;

    private GiaoVien.Display selectedGiaoVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DatActivityGiaovienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(GiaoVienViewModel.class);

        setupRecyclerView();
        observeViewModel();
        setupClickListeners();
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

        // 🔍 Search
        binding.btnSearch.setOnClickListener(v -> {
            String q = binding.etSearch.getText().toString();
            if (!q.isEmpty()) viewModel.search("%" + q + "%");
            else viewModel.loadAllGiaoViens();
        });

        // ➕ Add
        binding.btnAdd.setOnClickListener(v -> {
            GiaoVien gv = getFormData(); // ✅ SỬA Ở ĐÂY
            if (gv != null) {
                viewModel.insert(gv); // ✅ insert GiaoVien
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                clearForm();
            }
        });

        // 💾 Save
        binding.btnSave.setOnClickListener(v -> {
            if (selectedGiaoVien == null) return;

            GiaoVien gv = getFormData();
            if (gv != null) {
                gv.setMaGV(selectedGiaoVien.getGiaoVien().getMaGV());
                viewModel.update(gv);
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        });

        // ❌ Delete
        binding.btnDelete.setOnClickListener(v -> {
            if (selectedGiaoVien != null) {
                viewModel.delete(selectedGiaoVien.getGiaoVien());
                clearForm();
            }
        });

        // 🔄 Refresh
        binding.btnRefresh.setOnClickListener(v -> {
            clearForm();
            viewModel.loadAllGiaoViens();
        });
    }

    // ✅ LUÔN trả về GiaoVien (KHÔNG phải Display)
    private GiaoVien getFormData() {

        String ma = binding.etMaGiaoVien.getText().toString().trim();
        String ten = binding.etTenGv.getText().toString().trim();
        String ngaySinh = binding.etNgaySinh.getText().toString().trim();
        String sdt = binding.etSdt.getText().toString().trim();
        String maToHop = binding.etMaToHop.getText().toString().trim();
        String maMH = binding.etTenMon.getText().toString().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            Toast.makeText(this, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show();
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
        binding.etNgaySinh.setText(gv.getNgaySinh());
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