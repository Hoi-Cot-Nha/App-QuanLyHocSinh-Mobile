package com.example.quanlyhocsinhmobile.ui.letrang;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.MonHoc;
import java.util.List;
public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.ViewHolder> {
    private List<MonHoc> list;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(MonHoc monHoc);
    }
    public MonHocAdapter(List<MonHoc> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }
    public void setList(List<MonHoc> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.letrang_item_monhoc, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MonHoc monHoc = list.get(position);
        holder.tvMaMon.setText(monHoc.getMaMH());
        holder.tvTenMon.setText(monHoc.getTenMH());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(monHoc));
    }
    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaMon, tvTenMon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaMon = itemView.findViewById(R.id.tv_item_ma_mon);
            tvTenMon = itemView.findViewById(R.id.tv_item_ten_mon);
        }
    }
}