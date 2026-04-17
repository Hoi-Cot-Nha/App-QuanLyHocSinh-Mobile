package com.example.quanlyhocsinhmobile.ui.letrang;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.ThoiKhoaBieu;
import java.util.List;
public class TKBAdapter extends RecyclerView.Adapter<TKBAdapter.TKBViewHolder> {
    private List<ThoiKhoaBieu.Display> tkbList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(ThoiKhoaBieu.Display display);
    }
    public TKBAdapter(List<ThoiKhoaBieu.Display> tkbList, OnItemClickListener listener) {
        this.tkbList = tkbList;
        this.listener = listener;
    }
    public void setTkbList(List<ThoiKhoaBieu.Display> tkbList) {
        this.tkbList = tkbList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TKBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.letrang_item_tkb, parent, false);
        return new TKBViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TKBViewHolder holder, int position) {
        ThoiKhoaBieu.Display display = tkbList.get(position);
        ThoiKhoaBieu tkb = display.getThoiKhoaBieu();
        holder.tvThu.setText("T" + tkb.getThu());
        holder.tvTiet.setText(tkb.getTietBatDau() + " - " + tkb.getTietKetThuc());
        holder.tvTenLop.setText(display.getTenLop() != null ? display.getTenLop() : "N/A");
        holder.tvMon.setText(display.getTenMH() != null ? display.getTenMH() : "N/A");
        holder.tvPhong.setText(display.getTenPhong() != null ? display.getTenPhong() : "N/A");
        holder.tvGiaoVien.setText(display.getTenGV() != null ? display.getTenGV() : "N/A");
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(display);
            }
        });
    }
    @Override
    public int getItemCount() {
        return tkbList != null ? tkbList.size() : 0;
    }
    public static class TKBViewHolder extends RecyclerView.ViewHolder {
        TextView tvThu, tvTiet, tvTenLop, tvMon, tvPhong, tvGiaoVien;
        public TKBViewHolder(@NonNull View itemView) {
            super(itemView);
            tvThu = itemView.findViewById(R.id.tv_thu);
            tvTiet = itemView.findViewById(R.id.tv_tiet);
            tvTenLop = itemView.findViewById(R.id.tv_tenLop);
            tvMon = itemView.findViewById(R.id.tv_mon);
            tvPhong = itemView.findViewById(R.id.tv_phong);
            tvGiaoVien = itemView.findViewById(R.id.tv_giaovien);
        }
    }
}