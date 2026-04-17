package com.example.quanlyhocsinhmobile.ui.letrang;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.PhongHoc;
import java.util.List;
public class PhongHocAdapter extends RecyclerView.Adapter<PhongHocAdapter.ViewHolder> {
    private List<PhongHoc> list;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(PhongHoc phongHoc);
    }
    public PhongHocAdapter(List<PhongHoc> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }
    public void setList(List<PhongHoc> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.letrang_item_phonghoc, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhongHoc phongHoc = list.get(position);
        holder.tvMaPhong.setText(phongHoc.getMaPhong());
        holder.tvTenPhong.setText(phongHoc.getTenPhong());
        holder.tvSucChua.setText(String.valueOf(phongHoc.getSucChua()));
        holder.tvLoaiPhong.setText(phongHoc.getLoaiPhong());
        String tinhTrang = phongHoc.getTinhTrang();
        holder.tvTinhTrang.setText(tinhTrang);
        if ("Đang sử dụng".equals(tinhTrang)) {
            holder.tvTinhTrang.setTextColor(Color.RED);
        } else {
            holder.tvTinhTrang.setTextColor(Color.parseColor("#4CAF50")); 
        }
        holder.itemView.setOnClickListener(v -> listener.onItemClick(phongHoc));
    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaPhong, tvTenPhong, tvSucChua, tvLoaiPhong, tvTinhTrang;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaPhong = itemView.findViewById(R.id.tv_ma_phong);
            tvTenPhong = itemView.findViewById(R.id.tv_ten_phong);
            tvSucChua = itemView.findViewById(R.id.tv_suc_chua);
            tvLoaiPhong = itemView.findViewById(R.id.tv_loai_phong);
            tvTinhTrang = itemView.findViewById(R.id.tv_tinh_trang);
        }
    }
}