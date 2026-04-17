package com.example.quanlyhocsinhmobile.ui.dat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;

import java.util.List;

public class LopAdapter extends RecyclerView.Adapter<LopAdapter.ViewHolder>{

    private List<Lop.Display> list;
    private LopAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Lop.Display display);
    }

    public LopAdapter(List<Lop.Display> list, LopAdapter.OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }
    public void setList(List<Lop.Display> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dat_item_lop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LopAdapter.ViewHolder holder, int position) {
        Lop.Display display = list.get(position);

        Lop lop = display.getLop();
        if (lop == null) return;

        holder.tvMaLop.setText(lop.getMaLop());
        holder.tvTenLop.setText(lop.getTenLop());
        String tenGV = display.getTenGV();
        holder.tvGiaoVien.setText((tenGV == null || tenGV.trim().isEmpty()) ? lop.getMaGVCN() : tenGV);
        holder.tvNienKhoa.setText(lop.getNienKhoa());


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(display);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaLop, tvTenLop, tvGiaoVien, tvNienKhoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaLop = itemView.findViewById(R.id.item_ma_lop);
            tvTenLop = itemView.findViewById(R.id.item_ten_lop);
            tvGiaoVien = itemView.findViewById(R.id.item_giao_vien_chu_nhiem);
            tvNienKhoa = itemView.findViewById(R.id.item_nien_khoa);

        }
    }
}
