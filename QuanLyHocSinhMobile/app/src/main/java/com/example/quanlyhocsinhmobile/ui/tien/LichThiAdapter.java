package com.example.quanlyhocsinhmobile.ui.tien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.Model.LichThi;

import java.util.List;

public class LichThiAdapter extends RecyclerView.Adapter<LichThiAdapter.LichThiViewHolder> {

    private List<LichThi.Display> lichThiList;
    private final OnLichThiClickListener listener;

    public interface OnLichThiClickListener {
        void onLichThiClick(LichThi.Display display);
    }

    public LichThiAdapter(List<LichThi.Display> lichThiList, OnLichThiClickListener listener) {
        this.lichThiList = lichThiList;
        this.listener = listener;
    }

    public void setLichThiList(List<LichThi.Display> lichThiList) {
        this.lichThiList = lichThiList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LichThiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tien_lichthi, parent, false);
        return new LichThiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichThiViewHolder holder, int position) {
        LichThi.Display display = lichThiList.get(position);
        holder.bind(display, listener);
    }

    @Override
    public int getItemCount() {
        return lichThiList != null ? lichThiList.size() : 0;
    }

    static class LichThiViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKyThi, tvMon, tvNgayThi, tvThoiGian, tvPhong;

        public LichThiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKyThi = itemView.findViewById(R.id.tv_tenkythi);
            tvMon = itemView.findViewById(R.id.tv_mon);
            tvNgayThi = itemView.findViewById(R.id.tv_ngaythi);
            tvThoiGian = itemView.findViewById(R.id.tv_thoigian);
            tvPhong = itemView.findViewById(R.id.tv_phong);
        }

        public void bind(LichThi.Display display, OnLichThiClickListener listener) {
            LichThi lichThi = display.getLichThi();
            tvTenKyThi.setText(lichThi.getTenKyThi());
            tvMon.setText(display.getTenMH() != null ? display.getTenMH() : lichThi.getMaMH());
            tvNgayThi.setText(lichThi.getNgayThi());
            tvThoiGian.setText(lichThi.getGioBatDau() + " - " + lichThi.getGioKetThuc());
            tvPhong.setText(display.getTenPhong() != null ? display.getTenPhong() : lichThi.getMaPhong());

            itemView.setOnClickListener(v -> listener.onLichThiClick(display));
        }
    }
}
