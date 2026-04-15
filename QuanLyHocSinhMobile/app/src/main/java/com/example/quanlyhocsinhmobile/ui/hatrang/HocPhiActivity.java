package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

public class HocPhiActivity extends AppCompatActivity {

    private HatrangActivityHocphiBinding binding;
    private HocPhiViewModel viewModel;
    private HocPhiAdapter adapter;
    private List<Lop> listLop = new ArrayList<>();
    private HocPhi selectedHocPhi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HatrangActivityHocphiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(HocPhiViewModel.class);

        setupRecyclerView();
        setupSpinners();
        observeViewModel();
        setupClick();
    }

    private void setupRecyclerView() {
        binding.rvHocphi.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HocPhiAdapter(new ArrayList<>(), display -> {
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
            adapter.setList(list);
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

            if ( mg>tong ){
                Toast.makeText(this,"Miễn giảm không hợp lệ!",Toast.LENGTH_SHORT).show();
                return;
            }
            selectedHocPhi.setPhaiDong(tong - mg);

            selectedHocPhi.setTrangThai(
                    binding.spinnerTrangthai.getSelectedItem().toString()
            );

            viewModel.update(selectedHocPhi);
            viewModel.filter(0,"","");

            Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();

            binding.etTongtien.setText("");
            binding.etMiengiam.setText("");
            binding.etPhaidong.setText("");
            binding.spinnerTrangthai.setSelection(0);

            selectedHocPhi = null;

        } catch (Exception e) {
            Toast.makeText(this, "Sai dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }
}