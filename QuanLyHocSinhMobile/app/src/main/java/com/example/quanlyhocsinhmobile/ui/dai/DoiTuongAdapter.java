package com.example.quanlyhocsinhmobile.ui.dai;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.DoiTuongUuTien;
import java.util.List;
public class DoiTuongAdapter extends RecyclerView.Adapter<DoiTuongAdapter.ViewHolder> {
    private List<DoiTuongUuTien> list;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(DoiTuongUuTien doiTuong);
    }
    public DoiTuongAdapter(List<DoiTuongUuTien> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }
    public void setList(List<DoiTuongUuTien> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dai_item_doituong, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoiTuongUuTien doiTuong = list.get(position);
        holder.tvMaDT.setText(doiTuong.getMaDT());
        holder.tvTenDT.setText(doiTuong.getTenDT());
        if (doiTuong.getTiLeGiamHocPhi() != null) {
            holder.tvTiLe.setText(String.valueOf(doiTuong.getTiLeGiamHocPhi()));
        } else {
            holder.tvTiLe.setText("0.0");
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(doiTuong));
    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaDT, tvTenDT, tvTiLe;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDT = itemView.findViewById(R.id.tv_item_ma_dt);
            tvTenDT = itemView.findViewById(R.id.tv_item_ten_dt);
            tvTiLe = itemView.findViewById(R.id.tv_item_ti_le);
        }
    }
}