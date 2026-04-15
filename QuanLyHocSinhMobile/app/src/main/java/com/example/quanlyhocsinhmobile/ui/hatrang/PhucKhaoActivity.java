package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.local.Model.PhucKhao;
import com.example.quanlyhocsinhmobile.databinding.HatrangActivityPhuckhaoBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhucKhaoActivity extends AppCompatActivity {

    private HatrangActivityPhuckhaoBinding binding;
    private PhucKhaoViewModel viewModel;
    private PhucKhaoAdapter adapter;

    private List<HocSinh> listHocSinh = new ArrayList<>();
    private List<MonHoc> listMonHoc = new ArrayList<>();
    private List<String> listMaHS = new ArrayList<>();
    private List<String> listTenMH = new ArrayList<>();

    private PhucKhao selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HatrangActivityPhuckhaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(PhucKhaoViewModel.class);

        setupRecyclerView();
        setupSpinner();
        observe();
        setupClick();
    }

    private void setupRecyclerView() {
        binding.rvPhuckhao.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PhucKhaoAdapter(new ArrayList<>(), d -> {
            selected = d.getPhucKhao();
            showSelected(d);
        });
        binding.rvPhuckhao.setAdapter(adapter);
    }

    private void setupSpinner() {
        String[] tt = {"Trạng thái", "Đang chờ", "Đã duyệt", "Từ chối"};
        binding.spinnerTrangthaiPk.setAdapter(
                new ArrayAdapter<>(this, R.layout.spinner_item, tt)
        );
    }

    private void observe() {

        viewModel.getHocSinhList().observe(this, hsList -> {
            listMaHS.clear();
            listMaHS.add("Mã Học Sinh");
            for (HocSinh hs : hsList) listMaHS.add(hs.getMaHS());

            binding.spinnerMahsPk.setAdapter(
                    new ArrayAdapter<>(this, R.layout.spinner_item, listMaHS)
            );
        });

        viewModel.getMonHocList().observe(this, mons -> {
            listTenMH.clear();
            listTenMH.add("Tên Môn Học");
            for (MonHoc m : mons) listTenMH.add(m.getTenMH());

            binding.spinnerTenmhPk.setAdapter(
                    new ArrayAdapter<>(this, R.layout.spinner_item, listTenMH)
            );
        });

        viewModel.getHocSinhList().observe(this, HocSinh -> {
            listHocSinh = HocSinh;
            List<String> names = new ArrayList<>();
            names.add("Mã học sinh");
            for (HocSinh hocSinh : HocSinh) names.add(hocSinh.getMaHS());

            binding.spinnerLocMahs.setAdapter(
                    new ArrayAdapter<>(this, R.layout.spinner_item, names)
            );
        });

        viewModel.getMonHocList().observe(this, mons -> {
            listMonHoc = mons;
            List<String> names = new ArrayList<>();
            names.add("Tên môn");
            for (MonHoc m : mons) names.add(m.getTenMH());

            binding.spinnerLocMonhoc.setAdapter(
                    new ArrayAdapter<>(this, R.layout.spinner_item, names)
            );
        });

        viewModel.getPhucKhaoList().observe(this, list -> {
            adapter.setList(list);
        });
    }

    private void setupClick() {

        binding.btnFilterPk.setOnClickListener(v -> {
            String mahs = "";
            if (binding.spinnerLocMahs.getSelectedItemPosition() > 0) {
                mahs = listHocSinh.get(binding.spinnerLocMahs.getSelectedItemPosition() - 1).getMaHS();
            }

            String maMH = "";
            if (binding.spinnerLocMonhoc.getSelectedItemPosition() > 0) {
                maMH = listMonHoc.get(binding.spinnerLocMonhoc.getSelectedItemPosition() - 1).getMaMH();
            }

            viewModel.filter(mahs, maMH, "");
        });

        binding.btnAddPk.setOnClickListener(v -> add());
        binding.btnUpdatePk.setOnClickListener(v -> update());
        binding.btnDeletePk.setOnClickListener(v -> delete());
    }

    private void showSelected(PhucKhao.Display d) {

        int posHS = listMaHS.indexOf(d.getPhucKhao().getMaHS());
        if (posHS >= 0) binding.spinnerMahsPk.setSelection(posHS);

        int posMH = listTenMH.indexOf(d.getTenMH());
        if (posMH >= 0) binding.spinnerTenmhPk.setSelection(posMH);

        binding.etLydoPk.setText(d.getPhucKhao().getLyDo());

        switch (d.getPhucKhao().getTrangThai()) {
            case "Đang chờ": binding.spinnerTrangthaiPk.setSelection(1); break;
            case "Đã duyệt": binding.spinnerTrangthaiPk.setSelection(2); break;
            case "Từ chối": binding.spinnerTrangthaiPk.setSelection(3); break;
        }
    }

    private void add() {
        try {
            if (binding.spinnerMahsPk.getSelectedItem() == null ||
                    binding.spinnerTenmhPk.getSelectedItem() == null) {
                Toast.makeText(this, "Chưa có dữ liệu!", Toast.LENGTH_SHORT).show();
                return;
            }

            PhucKhao pk = new PhucKhao();
            pk.setMaHS(listHocSinh.get(binding.spinnerMahsPk.getSelectedItemPosition() - 1).getMaHS());
            pk.setMaMH(listMonHoc.get(binding.spinnerTenmhPk.getSelectedItemPosition() - 1).getMaMH());
            pk.setLyDo(binding.etLydoPk.getText().toString());
            pk.setNgayGui(getCurrentDate());
            pk.setTrangThai(binding.spinnerTrangthaiPk.getSelectedItem().toString());

            viewModel.insert(pk);

            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
            clearForm();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi thêm!", Toast.LENGTH_SHORT).show();
        }
    }

    private void update() {
        if (selected == null) {
            Toast.makeText(this, "Chọn dữ liệu!", Toast.LENGTH_SHORT).show();
            return;
        }
        selected.setMaHS(listHocSinh.get(binding.spinnerMahsPk.getSelectedItemPosition() - 1).getMaHS());
        selected.setMaMH(listMonHoc.get(binding.spinnerTenmhPk.getSelectedItemPosition() - 1).getMaMH());
        selected.setLyDo(binding.etLydoPk.getText().toString());
        selected.setNgayGui(getCurrentDate());
        selected.setTrangThai(binding.spinnerTrangthaiPk.getSelectedItem().toString());

        viewModel.update(selected);

        Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
        clearForm();
    }

    private void delete() {
        if (selected == null) {
            Toast.makeText(this, "Chọn dữ liệu!", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.delete(selected);

        Toast.makeText(this, "Xoá thành công!", Toast.LENGTH_SHORT).show();
        clearForm();
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
    }

    private void clearForm() {
        binding.spinnerMahsPk.setSelection(0);
        binding.spinnerTenmhPk.setSelection(0);
        binding.etLydoPk.setText("");
        binding.spinnerTrangthaiPk.setSelection(0);
        selected = null;
    }
}