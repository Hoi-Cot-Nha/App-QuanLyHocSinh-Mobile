package com.example.quanlyhocsinhmobile.ui.letrang;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
public class MonHocActivity extends AppCompatActivity {
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvMonHoc;
    private TextView tvMonHocInfo;
    private TextInputEditText etMaMon, etTenMon;
    private Button btnAdd, btnSave, btnDelete, btnRefresh;
    private MonHocViewModel viewModel;
    private MonHocAdapter adapter;
    private MonHoc selectedMonHoc = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.letrang_activity_monhoc);
        viewModel = new ViewModelProvider(this).get(MonHocViewModel.class);
        initViews();
        setupRecyclerView();
        observeViewModel();
    }
    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        rvMonHoc = findViewById(R.id.rv_mon_hoc);
        tvMonHocInfo = findViewById(R.id.tv_mon_hoc_info);
        etMaMon = findViewById(R.id.et_ma_mon);
        etTenMon = findViewById(R.id.et_ten_mon);
        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnSearch.setOnClickListener(v -> viewModel.search(etSearch.getText().toString().trim()));
        btnAdd.setOnClickListener(v -> viewModel.insert(etMaMon.getText().toString().trim(), etTenMon.getText().toString().trim()));
        btnSave.setOnClickListener(v -> viewModel.update(selectedMonHoc, etTenMon.getText().toString().trim()));
        btnDelete.setOnClickListener(v -> viewModel.delete(selectedMonHoc));
        btnRefresh.setOnClickListener(v -> {
            viewModel.loadAllMonHocs();
            clearInputs();
        });
    }
    private void setupRecyclerView() {
        adapter = new MonHocAdapter(new ArrayList<>(), monHoc -> {
            selectedMonHoc = monHoc;
            tvMonHocInfo.setText("Môn học: " + monHoc.getTenMH());
            etMaMon.setText(monHoc.getMaMH());
            etTenMon.setText(monHoc.getTenMH());
            etMaMon.setEnabled(false); 
        });
        rvMonHoc.setLayoutManager(new LinearLayoutManager(this));
        rvMonHoc.setAdapter(adapter);
    }
    private void observeViewModel() {
        viewModel.getAllMonHocs().observe(this, monHocs -> {
            adapter.setList(monHocs);
        });
        viewModel.getToastMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                if (message.contains("thành công")) {
                    clearInputs();
                }
            }
        });
    }
    private void clearInputs() {
        selectedMonHoc = null;
        tvMonHocInfo.setText("Môn học: --");
        etMaMon.setText("");
        etTenMon.setText("");
        etMaMon.setEnabled(true);
        etSearch.setText("");
    }
}