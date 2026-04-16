package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyhocsinhmobile.data.local.Model.ThongBao;
import com.example.quanlyhocsinhmobile.databinding.HatrangActivityThongbaoBinding;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;

import java.util.ArrayList;

public class ThongBaoActivity extends AppCompatActivity {

    private HatrangActivityThongbaoBinding binding;
    private ThongBaoViewModel viewModel;
    private ThongBaoAdapter adapter;
    private ThongBao selectedThongBao;
    private PhanQuyen phanQuyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HatrangActivityThongbaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phanQuyen = PhanQuyen.getInstance(this);
        viewModel = new ViewModelProvider(this).get(ThongBaoViewModel.class);

        setupRecyclerView();
        observeViewModel();
        setupClick();
        apDungPhanQuyen();
    }

    private void apDungPhanQuyen() {
        String quyen = phanQuyen.getQuyen();
        if ("GiaoVien".equals(quyen) || "HocSinh".equals(quyen)) {
            // Đổi tiêu đề: "THÔNG BÁO"
            if (binding.tvTitleThongbao != null) {
                binding.tvTitleThongbao.setText("THÔNG BÁO");
            }
            // Ẩn phần 3: THÊM THÔNG BÁO
            if (binding.tvAddTitle != null) binding.tvAddTitle.setVisibility(View.GONE);
            if (binding.cardAdd != null) binding.cardAdd.setVisibility(View.GONE);
        }

        if ("HocSinh".equals(quyen)) {
            // Ẩn phần THÊM THÔNG BÁO cho học sinh
            if (binding.tvAddTitle != null) binding.tvAddTitle.setVisibility(View.GONE);
            if (binding.cardAdd != null) binding.cardAdd.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView() {
        binding.rvThongbao.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ThongBaoAdapter(new ArrayList<>(), tb -> {
            if ("GiaoVien".equals(phanQuyen.getQuyen()) || "HocSinh".equals(phanQuyen.getQuyen())) return;
            selectedThongBao = tb;
            showSelected(tb);
        });
        binding.rvThongbao.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel.getThongBaoList().observe(this, list -> {
            adapter.setList(list);
        });
    }

    private void setupClick() {
        binding.btnFilterTb.setOnClickListener(v -> {
            String search = binding.etSearchTb.getText().toString();
            viewModel.search(search);
        });

        binding.btnAddTb.setOnClickListener(v -> insertThongBao());
        binding.btnUpdateTb.setOnClickListener(v -> updateThongBao());
        binding.btnDeleteTb.setOnClickListener(v -> deleteThongBao());
    }

    private void deleteThongBao() {
        if (selectedThongBao == null) {
            Toast.makeText(this, "Chọn thông báo!", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.delete(selectedThongBao);
        Toast.makeText(this, "Xoá thành công!", Toast.LENGTH_SHORT).show();
        binding.etTieude.setText("");
        binding.etNoidung.setText("");
        binding.etNguoigui.setText("");
        selectedThongBao = null;
    }

    private void updateThongBao() {
        if (selectedThongBao == null) {
            Toast.makeText(this, "Chọn thông báo!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            selectedThongBao.setTieuDe(binding.etTieude.getText().toString());
            selectedThongBao.setNoiDung(binding.etNoidung.getText().toString());
            selectedThongBao.setNguoiGui(binding.etNguoigui.getText().toString());
            viewModel.update(selectedThongBao);
            Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
            binding.etTieude.setText("");
            binding.etNoidung.setText("");
            binding.etNguoigui.setText("");
            selectedThongBao = null;
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi sửa!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSelected(ThongBao tb) {
        binding.etTieude.setText(tb.getTieuDe());
        binding.etNoidung.setText(tb.getNoiDung());
        binding.etNguoigui.setText(tb.getNguoiGui());
    }

    private void insertThongBao() {
        try {
            String tieude = binding.etTieude.getText().toString();
            String noidung = binding.etNoidung.getText().toString();
            String nguoiGui = binding.etNguoigui.getText().toString();
            if (tieude.isEmpty() || noidung.isEmpty()) {
                Toast.makeText(this, "Nhập đầy đủ dữ liệu!", Toast.LENGTH_SHORT).show();
                return;
            }
            ThongBao tb = new ThongBao();
            tb.setTieuDe(tieude);
            tb.setNoiDung(noidung);
            tb.setNguoiGui(nguoiGui);
            tb.setNgayTao(getCurrentDate());
            viewModel.insert(tb);
            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            binding.etTieude.setText("");
            binding.etNoidung.setText("");
            binding.etNguoigui.setText("");
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi thêm dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }
    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}
