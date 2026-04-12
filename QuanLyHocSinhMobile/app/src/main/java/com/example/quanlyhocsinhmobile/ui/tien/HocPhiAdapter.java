package com.example.quanlyhocsinhmobile.ui.tien;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.Model.HocPhi;

import java.util.List;

public class HocPhiAdapter extends RecyclerView.Adapter<HocPhiAdapter.ViewHolder> {

    private List<HocPhi.Display> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(HocPhi.Display display);
    }

    public HocPhiAdapter(List<HocPhi.Display> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<HocPhi.Display> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tien_hocphi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HocPhi.Display display = list.get(position);
        HocPhi hp = display.getHocPhi();
        holder.tvMaHS.setText(hp.getMaHS());
        holder.tvHoTen.setText(display.getTenHS());
        holder.tvLop.setText(display.getTenLop());
        holder.tvPhaiDong.setText(String.format("%,.0f", hp.getPhaiDong()));
        holder.tvTrangThai.setText(hp.getTrangThai());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(display));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHS, tvHoTen, tvLop, tvPhaiDong, tvTrangThai;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHS = itemView.findViewById(R.id.tv_item_mahs_hp);
            tvHoTen = itemView.findViewById(R.id.tv_item_hoten_hp);
            tvLop = itemView.findViewById(R.id.tv_item_lop_hp);
            tvPhaiDong = itemView.findViewById(R.id.tv_item_phaidong_hp);
            tvTrangThai = itemView.findViewById(R.id.tv_item_trangthai_hp);
        }
    }
}
