package com.example.quanlyhocsinhmobile.ui.dat;
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
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
public class ToBoMonActivity extends AppCompatActivity {
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvToHop;
    private TextView tvToHopInfo;
    private TextInputEditText etMaToHop, etTenToHop;
    private Button btnAdd, btnSave, btnDelete, btnRefresh;
    private ToBoMonViewModel viewModel;
    private ToBoMonAdapter adapter;
    private ToHopMon selectedToHop = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dat_activity_tobomon);
        viewModel = new ViewModelProvider(this).get(ToBoMonViewModel.class);
        initViews();
        setupRecyclerView();
        observeViewModel();
    }
    private void initViews() {
        etSearch = findViewById(R.id.et_search);
        btnSearch = findViewById(R.id.btn_search);
        rvToHop = findViewById(R.id.rv_to_bo_mon);
        tvToHopInfo = findViewById(R.id.tv_to_bo_mon_info);
        etMaToHop = findViewById(R.id.et_ma_to_hop);
        etTenToHop = findViewById(R.id.et_ten_to_hop);
        btnAdd = findViewById(R.id.btn_add);
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        btnRefresh = findViewById(R.id.btn_refresh);
        btnSearch.setOnClickListener(v ->
                viewModel.search(etSearch.getText().toString().trim())
        );
        btnAdd.setOnClickListener(v -> {
            String ma = etMaToHop.getText().toString().trim();
            String ten = etTenToHop.getText().toString().trim();
            if (ma.isEmpty() || ten.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.insert(ma, ten);
        });
        btnSave.setOnClickListener(v -> {
            if (selectedToHop == null) {
                Toast.makeText(this, "Chưa chọn tổ bộ môn", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.update(selectedToHop, etTenToHop.getText().toString().trim());
        });
        btnDelete.setOnClickListener(v -> {
            if (selectedToHop == null) {
                Toast.makeText(this, "Chưa chọn tổ bộ môn", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.delete(selectedToHop);
        });
        btnRefresh.setOnClickListener(v -> {
            viewModel.loadAllToHops();
            clearInputs();
        });
    }
    private void setupRecyclerView() {
        adapter = new ToBoMonAdapter(new ArrayList<>(), toHopMon -> {
            if (toHopMon == null) return;
            selectedToHop = toHopMon;
            tvToHopInfo.setText("Tổ Hợp: " + toHopMon.getTenToHop());
            etMaToHop.setText(toHopMon.getMaToHop());
            etTenToHop.setText(toHopMon.getTenToHop());
            etMaToHop.setEnabled(false);
        });
        rvToHop.setLayoutManager(new LinearLayoutManager(this));
        rvToHop.setHasFixedSize(true);
        rvToHop.setAdapter(adapter);
    }
    private void observeViewModel() {
        viewModel.getAllToHop().observe(this, toHopMons -> {
            adapter.setList(toHopMons);
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
        selectedToHop = null;
        tvToHopInfo.setText("Tổ bộ môn: --");
        etMaToHop.setText("");
        etTenToHop.setText("");
        etMaToHop.setEnabled(true);
        etSearch.setText("");
    }
}