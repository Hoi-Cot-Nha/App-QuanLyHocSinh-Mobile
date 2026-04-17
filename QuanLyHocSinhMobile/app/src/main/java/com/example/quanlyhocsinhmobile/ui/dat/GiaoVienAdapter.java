package com.example.quanlyhocsinhmobile.ui.dat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.GiaoVien;
import com.example.quanlyhocsinhmobile.utils.FormatDate;
import java.util.List;
public class GiaoVienAdapter extends RecyclerView.Adapter<GiaoVienAdapter.ViewHolder>{
    private List<GiaoVien.Display> list;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(GiaoVien.Display display);
    }
    public GiaoVienAdapter(List<GiaoVien.Display> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }
    public void setList(List<GiaoVien.Display> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public List<GiaoVien.Display> getGiaoVienList(){
        return list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dat_item_giaovien, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiaoVien.Display display = list.get(position);
        holder.bind(display, listener);
    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaGV, tvTenGV, tvNgaySinh, TvSDT, tvMaToHop, tvTenMH;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaGV = itemView.findViewById(R.id.item_ma_gv);
            tvTenGV = itemView.findViewById(R.id.item_ten_gv);
            tvNgaySinh = itemView.findViewById(R.id.item_ngay_sinh);
            TvSDT = itemView.findViewById(R.id.item_sdt);
            tvMaToHop = itemView.findViewById(R.id.item_ma_to_hop);
            tvTenMH = itemView.findViewById(R.id.item_ten_mon);
        }
        public void bind(GiaoVien.Display display, OnItemClickListener listener) {
            GiaoVien giaoVien = display.getGiaoVien();
            tvMaGV.setText(giaoVien.getMaGV());
            tvTenGV.setText(giaoVien.getHoTen());
            tvNgaySinh.setText(FormatDate.formatDateForDisplay(giaoVien.getNgaySinh()));
            TvSDT.setText(giaoVien.getSdt());
            tvMaToHop.setText(display.getTenToHop() != null
                    ? display.getTenToHop()
                    : giaoVien.getMaToHop());
            tvTenMH.setText(display.getTenMH() != null
                    ? display.getTenMH()
                    : giaoVien.getMaMH());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(display);
                }
            });
        }
    }
}