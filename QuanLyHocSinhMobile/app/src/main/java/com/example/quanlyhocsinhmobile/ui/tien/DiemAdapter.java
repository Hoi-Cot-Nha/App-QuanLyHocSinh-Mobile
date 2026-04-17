package com.example.quanlyhocsinhmobile.ui.tien;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.Diem;
import java.util.List;
import java.util.Locale;
public class DiemAdapter extends RecyclerView.Adapter<DiemAdapter.DiemViewHolder> {
    private List<Diem.Display> diemList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Diem.Display display);
    }
    public DiemAdapter(List<Diem.Display> diemList, OnItemClickListener listener) {
        this.diemList = diemList;
        this.listener = listener;
    }
    public void setDiemList(List<Diem.Display> diemList) {
        this.diemList = diemList;
        notifyDataSetChanged();
    }
    public List<Diem.Display> getDiemList() {
        return diemList;
    }
    @NonNull
    @Override
    public DiemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tien_item_diem, parent, false);
        return new DiemViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull DiemViewHolder holder, int position) {
        Diem.Display display = diemList.get(position);
        Diem diem = display.getDiem();
        holder.tvMaHS.setText(diem.getMaHS());
        holder.tvHoTen.setText(display.getTenHS() != null ? display.getTenHS() : "N/A");
        holder.tvTenLop.setText(display.getTenLop() != null ? display.getTenLop() : "N/A");
        holder.tvMon.setText(display.getTenMH() != null ? display.getTenMH() : "N/A");
        holder.tvHK.setText(String.valueOf(diem.getHocKy()));
        Double d15 = diem.getDiem15p();
        Double d1t = diem.getDiem1Tiet();
        Double dgk = diem.getDiemGiuaKy();
        Double dck = diem.getDiemCuoiKy();
        Double dtk = diem.getDiemTongKet();
        holder.tv15p.setText(d15 != null ? String.format(Locale.getDefault(), "%.1f", d15) : "0.0");
        holder.tv1Tiet.setText(d1t != null ? String.format(Locale.getDefault(), "%.1f", d1t) : "0.0");
        holder.tvGK.setText(dgk != null ? String.format(Locale.getDefault(), "%.1f", dgk) : "0.0");
        holder.tvCK.setText(dck != null ? String.format(Locale.getDefault(), "%.1f", dck) : "0.0");
        holder.tvTK.setText(dtk != null ? String.format(Locale.getDefault(), "%.2f", dtk) : "0.0");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(display);
            }
        });
    }
    @Override
    public int getItemCount() {
        return diemList != null ? diemList.size() : 0;
    }
    public static class DiemViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHS, tvHoTen, tvTenLop, tvMon, tvHK, tv15p, tv1Tiet, tvGK, tvCK, tvTK;
        public DiemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHS = itemView.findViewById(R.id.tv_mahs);
            tvHoTen = itemView.findViewById(R.id.tv_hoten);
            tvTenLop = itemView.findViewById(R.id.tv_tenlop);
            tvMon = itemView.findViewById(R.id.tv_mon);
            tvHK = itemView.findViewById(R.id.tv_hk);
            tv15p = itemView.findViewById(R.id.tv_15p);
            tv1Tiet = itemView.findViewById(R.id.tv_1tiet);
            tvGK = itemView.findViewById(R.id.tv_gk);
            tvCK = itemView.findViewById(R.id.tv_ck);
            tvTK = itemView.findViewById(R.id.tv_tk);
        }
    }
}