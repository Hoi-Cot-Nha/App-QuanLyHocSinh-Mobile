package com.example.quanlyhocsinhmobile.ui.dat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.data.local.Model.Lop;

import java.util.List;

public class LopAdapter extends RecyclerView.Adapter<LopAdapter.ViewHolder>{

    private List<Lop> list;
    private LopAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Lop lop);
    }

    public LopAdapter(List<Lop> list, LopAdapter.OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }
    public void setList(List<Lop> list) {
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
        Lop lop = list.get(position);

        holder.tvMaLop.setText(lop.getMaLop());
        holder.tvGiaoVien.setText(lop.getTenLop());
        holder.tvNienKhoa.setText(lop.getNienKhoa());
        holder.tvGiaoVien.setText(lop.getMaGVCN());


        holder.itemView.setOnClickListener(v -> listener.onItemClick(lop));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaLop, tvTenLop, tvNienKhoa, tvGiaoVien;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaLop = itemView.findViewById(R.id.item_ma_lop);
            tvTenLop = itemView.findViewById(R.id.item_ten_lop);
            tvNienKhoa = itemView.findViewById(R.id.item_nien_khoa);
            tvGiaoVien = itemView.findViewById(R.id.item_giao_vien_chu_nhiem);
        }
    }
}
