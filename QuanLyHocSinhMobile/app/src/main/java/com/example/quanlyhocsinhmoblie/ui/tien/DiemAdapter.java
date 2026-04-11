package com.example.quanlyhocsinhmoblie.ui.tien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmoblie.R;
import com.example.quanlyhocsinhmoblie.data.Model.Diem;

import java.util.List;
import java.util.Locale;

public class DiemAdapter extends RecyclerView.Adapter<DiemAdapter.DiemViewHolder> {

    private List<Diem> diemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Diem diem);
    }

    public DiemAdapter(List<Diem> diemList, OnItemClickListener listener) {
        this.diemList = diemList;
        this.listener = listener;
    }

    public void setDiemList(List<Diem> diemList) {
        this.diemList = diemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DiemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_diem, parent, false);
        return new DiemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiemViewHolder holder, int position) {
        Diem diem = diemList.get(position);
        holder.tvMaHS.setText(diem.getMaHS());
        holder.tvHoTen.setText(diem.getTenHS() != null ? diem.getTenHS() : "N/A");
        holder.tvMon.setText(diem.getMaMH());
        holder.tvHK.setText(String.valueOf(diem.getHocKy()));
        holder.tv15p.setText(String.format(Locale.getDefault(), "%.1f", diem.getDiem15p()));
        holder.tv1Tiet.setText(String.format(Locale.getDefault(), "%.1f", diem.getDiem1Tiet()));
        holder.tvGK.setText(String.format(Locale.getDefault(), "%.1f", diem.getDiemGiuaKy()));
        holder.tvCK.setText(String.format(Locale.getDefault(), "%.1f", diem.getDiemCuoiKy()));
        holder.tvTK.setText(String.format(Locale.getDefault(), "%.2f", diem.getDiemTongKet()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(diem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return diemList != null ? diemList.size() : 0;
    }

    public static class DiemViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHS, tvHoTen, tvMon, tvHK, tv15p, tv1Tiet, tvGK, tvCK, tvTK;

        public DiemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHS = itemView.findViewById(R.id.tv_mahs);
            tvHoTen = itemView.findViewById(R.id.tv_hoten);
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
