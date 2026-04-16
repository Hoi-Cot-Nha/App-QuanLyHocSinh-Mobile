package com.example.quanlyhocsinhmobile.ui.tien;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.HanhKiem;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.databinding.TienActivityHanhkiemBinding;
import com.example.quanlyhocsinhmobile.utils.ExcelHelper;
import com.example.quanlyhocsinhmobile.utils.PhanQuyen;

import java.util.ArrayList;
import java.util.List;

public class HanhKiemActivity extends AppCompatActivity {

    private TienActivityHanhkiemBinding binding;
    private HanhKiemViewModel viewModel;
    private HanhKiemAdapter adapter;
    private List<Lop> listLop = new ArrayList<>();
    private HanhKiem selectedHanhKiem;
    private PhanQuyen phanQuyen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TienActivityHanhkiemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phanQuyen = PhanQuyen.getInstance(this);
        viewModel = new ViewModelProvider(this).get(HanhKiemViewModel.class);
        
        setupRecyclerView();
        setupSpinners();
        observeViewModel();
        setupClickListeners();
        apDungPhanQuyen();
    }

    private void apDungPhanQuyen() {
        String quyen = phanQuyen.getQuyen();
        if ("HocSinh".equals(quyen)) {
            // Đổi tiêu đề cho học sinh
            View titleView = ((android.view.ViewGroup)((android.view.ViewGroup)binding.getRoot()).getChildAt(0)).getChildAt(0);
            if (titleView instanceof android.widget.TextView) {
                ((android.widget.TextView) titleView).setText("HẠNH KIỂM");
            }

            // Ẩn phần cập nhật hạnh kiểm
            binding.tvUpdateTitleHk.setVisibility(View.GONE);
            binding.cardUpdateHk.setVisibility(View.GONE);
            
            // Chỉ thấy hạnh kiểm của mình
            String maHS = phanQuyen.getMaNguoiDung();
            if (maHS != null && !maHS.isEmpty()) {
                viewModel.search(maHS);
                // Ẩn thanh tìm kiếm và bộ lọc để học sinh chỉ thấy hạnh kiểm của mình
                binding.cardSearchHk.setVisibility(View.GONE);
                binding.tvSearchTitleHk.setVisibility(View.GONE);
            }
        }
    }

    private void setupRecyclerView() {
        binding.rvHanhKiem.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HanhKiemAdapter(new ArrayList<>(), display -> {
            if ("HocSinh".equals(phanQuyen.getQuyen())) return;
            selectedHanhKiem = display.getHanhKiem();
            binding.tvMahsHkUpdate.setText("Mã HS: " + selectedHanhKiem.getMaHS());
            binding.tvHotenHkUpdate.setText("Học sinh: " + display.getTenHS());
            binding.etNhanXet.setText(selectedHanhKiem.getNhanXet());
            
            String xl = selectedHanhKiem.getXepLoai();
            if (xl != null) {
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spinnerXepLoai.getAdapter();
                int pos = adapter.getPosition(xl);
                if (pos >= 0) binding.spinnerXepLoai.setSelection(pos);
            }
        });
        binding.rvHanhKiem.setAdapter(adapter);
    }

    private void setupSpinners() {
        String[] semesters = {"--- Tất cả HK ---", "1", "2"};
        binding.spinnerSemesterHk.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, semesters));

        String[] years = {"--- Tất cả năm ---", "2023-2024", "2024-2025"};
        binding.spinnerYearHk.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, years));

        String[] ratings = {"Tốt", "Khá", "Trung bình", "Yếu", "Kém"};
        binding.spinnerXepLoai.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ratings));
    }

    private void observeViewModel() {
        viewModel.getLopList().observe(this, lops -> {
            this.listLop = lops;
            List<String> names = new ArrayList<>();
            names.add("--- Tất cả lớp ---");
            for (Lop l : lops) names.add(l.getTenLop());
            binding.spinnerClassHk.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, names));
        });

        viewModel.getHanhKiemList().observe(this, list -> {
            adapter.setList(list);
        });
    }

    private void setupClickListeners() {
        binding.btnFilterHk.setOnClickListener(v -> {
            String maLop = "";
            int lopPos = binding.spinnerClassHk.getSelectedItemPosition();
            if (lopPos > 0) maLop = listLop.get(lopPos - 1).getMaLop();

            int hocKy = binding.spinnerSemesterHk.getSelectedItemPosition();
            String namHoc = binding.spinnerYearHk.getSelectedItemPosition() > 0 ? 
                            binding.spinnerYearHk.getSelectedItem().toString() : "";

            viewModel.filter(maLop, hocKy, namHoc);
        });

        binding.btnSearchHk.setOnClickListener(v -> {
            String query = binding.etSearchHk.getText().toString();
            if (!query.isEmpty()) viewModel.search(query);
            else Toast.makeText(this, "Nhập từ khóa!", Toast.LENGTH_SHORT).show();
        });

        binding.btnUpdateHk.setOnClickListener(v -> {
            if (selectedHanhKiem == null) {
                Toast.makeText(this, "Vui lòng chọn học sinh", Toast.LENGTH_SHORT).show();
                return;
            }
            selectedHanhKiem.setXepLoai(binding.spinnerXepLoai.getSelectedItem().toString());
            selectedHanhKiem.setNhanXet(binding.etNhanXet.getText().toString());
            viewModel.update(selectedHanhKiem);
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            binding.btnFilterHk.performClick(); // Refresh list
        });

        binding.btnExportHk.setOnClickListener(v -> exportToExcel());
    }

    private void exportToExcel() {
        ExcelHelper.exportToExcel(this, "HanhKiem", "HanhKiem",
            new String[]{"Mã HS", "Họ Tên", "Lớp", "Học Kỳ", "Năm Học", "Xếp Loại", "Nhận Xét"},
            adapter.getList(), (row, display) -> {
                HanhKiem hk = display.getHanhKiem();
                row.createCell(0).setCellValue(hk.getMaHS());
                row.createCell(1).setCellValue(display.getTenHS());
                row.createCell(2).setCellValue(display.getTenLop());
                row.createCell(3).setCellValue(hk.getHocKy());
                row.createCell(4).setCellValue(hk.getNamHoc());
                row.createCell(5).setCellValue(hk.getXepLoai());
                row.createCell(6).setCellValue(hk.getNhanXet());
            });
    }
}
