package com.example.quanlyhocsinhmobile.ui.tien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.Model.HanhKiem;

import java.util.List;

public class HanhKiemAdapter extends RecyclerView.Adapter<HanhKiemAdapter.HanhKiemViewHolder> {

    private List<HanhKiem.Display> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HanhKiem.Display display);
    }

    public HanhKiemAdapter(List<HanhKiem.Display> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<HanhKiem.Display> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HanhKiemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tien_hanhkiem, parent, false);
        return new HanhKiemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HanhKiemViewHolder holder, int position) {
        HanhKiem.Display display = list.get(position);
        HanhKiem hk = display.getHanhKiem();

        holder.tvMaHS.setText(hk.getMaHS());
        holder.tvHoTen.setText(display.getTenHS());
        holder.tvTenLop.setText(display.getTenLop());
        holder.tvXepLoai.setText(hk.getXepLoai());
        holder.tvHK.setText(String.valueOf(hk.getHocKy()));
        holder.tvNamHoc.setText(hk.getNamHoc());
        holder.tvNhanXet.setText(hk.getNhanXet());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(display));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class HanhKiemViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHS, tvHoTen, tvTenLop, tvXepLoai, tvHK, tvNamHoc, tvNhanXet;

        public HanhKiemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHS = itemView.findViewById(R.id.tv_mahs_hk);
            tvHoTen = itemView.findViewById(R.id.tv_hoten_hk);
            tvTenLop = itemView.findViewById(R.id.tv_tenlop_hk);
            tvXepLoai = itemView.findViewById(R.id.tv_xeploai_hk);
            tvHK = itemView.findViewById(R.id.tv_hk_hk);
            tvNamHoc = itemView.findViewById(R.id.tv_namhoc_hk);
            tvNhanXet = itemView.findViewById(R.id.tv_nhanxet_hk);
        }
    }
}
