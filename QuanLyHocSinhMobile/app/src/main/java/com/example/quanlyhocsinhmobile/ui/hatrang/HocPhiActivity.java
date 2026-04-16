package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.HocPhi;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.databinding.HatrangActivityHocphiBinding;
import com.example.quanlyhocsinhmobile.utils.ExcelHelper;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;

import java.util.ArrayList;
import java.util.List;

public class HocPhiActivity extends AppCompatActivity {

    private HatrangActivityHocphiBinding binding;
    private HocPhiViewModel viewModel;
    private HocPhiAdapter adapter;
    private List<Lop> listLop = new ArrayList<>();
    private HocPhi selectedHocPhi;
    private PhanQuyen phanQuyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HatrangActivityHocphiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phanQuyen = PhanQuyen.getInstance(this);
        viewModel = new ViewModelProvider(this).get(HocPhiViewModel.class);

        setupRecyclerView();
        setupSpinners();
        observeViewModel();
        setupClick();
        apDungPhanQuyen();
    }

    private void apDungPhanQuyen() {
        String quyen = phanQuyen.getQuyen();
        if ("HocSinh".equals(quyen)) {
            // Đổi tiêu đề cho học sinh
            if (binding.tvTitleHocphi != null) {
                binding.tvTitleHocphi.setText("HỌC PHÍ");
            }

            View root = binding.getRoot();
            if (root instanceof android.view.ViewGroup) {
                android.view.ViewGroup group = (android.view.ViewGroup) ((android.view.ViewGroup) root).getChildAt(1); // NestedScrollView
                android.view.ViewGroup content = (android.view.ViewGroup) group.getChildAt(0); // LinearLayout
                
                // Ẩn 2 view cuối (Tiêu đề và Card cập nhật)
                int count = content.getChildCount();
                if (count >= 2) {
                    content.getChildAt(count - 1).setVisibility(View.GONE); // Card
                    content.getChildAt(count - 2).setVisibility(View.GONE); // TextView Tiêu đề
                }
                
                // Ẩn bộ lọc và chỉ hiện học phí của mình
                String maHS = phanQuyen.getMaNguoiDung();
                if (maHS != null) {
                    viewModel.filter(0, "", ""); // Load all then we filter in search if needed, but best is a dedicated search
                    // Tạm thời ẩn bộ lọc
                    content.getChildAt(0).setVisibility(View.GONE); // BỘ LỌC text
                    content.getChildAt(1).setVisibility(View.GONE); // BỘ LỌC card
                }
            }
        }
    }

    private void setupRecyclerView() {
        binding.rvHocphi.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HocPhiAdapter(new ArrayList<>(), display -> {
            if ("HocSinh".equals(phanQuyen.getQuyen())) return;
            selectedHocPhi = display.getHocPhi();
            showSelected(display);
        });
        binding.rvHocphi.setAdapter(adapter);
    }

    private void setupSpinners() {
        String[] hk = {"--- Tất cả HK ---", "HK 1", "HK 2"};
        binding.spinnerHocky.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, hk));

        String[] nam = {"--- Tất cả năm ---", "2023-2024", "2024-2025", "2025-2026"};
        binding.spinnerNamhoc.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, nam));

        String[] trangThai = {"Trạng thái","Chưa đóng", "Đã đóng"};
        binding.spinnerTrangthai.setAdapter(
                new ArrayAdapter<>(this, R.layout.spinner_item, trangThai)
        );
    }

    private void observeViewModel() {
        viewModel.getLopList().observe(this, lops -> {
            listLop = lops;
            List<String> names = new ArrayList<>();
            names.add("--- Tất cả lớp ---");
            for (Lop l : lops) names.add(l.getTenLop());
            binding.spinnerLop.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, names));
        });

        viewModel.getHocPhiList().observe(this, list -> {
            if ("HocSinh".equals(phanQuyen.getQuyen())) {
                String maHS = phanQuyen.getMaNguoiDung();
                List<HocPhi.Display> filtered = new ArrayList<>();
                for (HocPhi.Display d : list) {
                    if (d.getHocPhi().getMaHS().equals(maHS)) filtered.add(d);
                }
                adapter.setList(filtered);
            } else {
                adapter.setList(list);
            }
        });
    }

    private void setupClick() {

        binding.btnFilterHp.setOnClickListener(v -> {
            int hocKy = binding.spinnerHocky.getSelectedItemPosition();

            String maLop = "";
            int pos = binding.spinnerLop.getSelectedItemPosition();
            if (pos > 0) maLop = listLop.get(pos - 1).getMaLop();

            String namHoc = "";
            int namPos = binding.spinnerNamhoc.getSelectedItemPosition();
            if (namPos > 0) {
                namHoc = binding.spinnerNamhoc.getSelectedItem().toString();
            }

            viewModel.filter(hocKy, namHoc, maLop);
        });
        binding.btnSaveHp.setOnClickListener(v -> {
            updateHocPhi();
        });
    }

    private void showSelected(HocPhi.Display d) {
        binding.etTongtien.setText(d.getHocPhi().getTongTien()+"");
        binding.etMiengiam.setText(d.getHocPhi().getMienGiam()+"");
        binding.etPhaidong.setText(d.getHocPhi().getPhaiDong()+"");

        String tt = d.getHocPhi().getTrangThai();
        if (tt.equals("Chưa đóng")) {
            binding.spinnerTrangthai.setSelection(1);
        } else if (tt.equals("Đã đóng")) {
            binding.spinnerTrangthai.setSelection(2);
        } else {
            binding.spinnerTrangthai.setSelection(0); // Trạng thái (mặc định)
        }
    }

    private void updateHocPhi() {
        if (selectedHocPhi == null) {
            Toast.makeText(this, "Chọn học sinh!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double tong = Double.parseDouble(binding.etTongtien.getText().toString());
            double mg = Double.parseDouble(binding.etMiengiam.getText().toString());

            selectedHocPhi.setTongTien(tong);
            selectedHocPhi.setMienGiam(mg);
            selectedHocPhi.setPhaiDong(tong - mg);
            selectedHocPhi.setTrangThai(
                    binding.spinnerTrangthai.getSelectedItem().toString()
            );

            viewModel.update(selectedHocPhi);
            viewModel.filter(0,"","");

            Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();

            //Reset lại form
            binding.etTongtien.setText("");
            binding.etMiengiam.setText("");
            binding.etPhaidong.setText("");
            binding.spinnerTrangthai.setSelection(0);

            //bỏ chọn
            selectedHocPhi = null;

        } catch (Exception e) {
            Toast.makeText(this, "Sai dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }
}
