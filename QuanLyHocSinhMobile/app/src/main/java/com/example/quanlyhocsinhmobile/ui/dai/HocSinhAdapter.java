package com.example.quanlyhocsinhmobile.ui.dai;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.HocSinh;
import com.example.quanlyhocsinhmobile.utils.FormatDate;
import java.util.List;
public class HocSinhAdapter extends RecyclerView.Adapter<HocSinhAdapter.VH> {
    private List<HocSinh> list;
    private OnClick listener;
    interface OnClick {
        void click(HocSinh hs);
    }
    public HocSinhAdapter(List<HocSinh> list, OnClick l) {
        this.list = list;
        this.listener = l;
    }
    public void setList(List<HocSinh> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.dai_item_hocsinh, p, false);
        return new VH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull VH h, int i) {
        HocSinh hs = list.get(i);
        h.tvMa.setText(hs.getMaHS());
        h.tvTen.setText(hs.getHoTen());
        h.tvNS.setText(FormatDate.formatDateForDisplay(hs.getNgaySinh()));
        h.tvGT.setText(hs.getGioiTinh());
        h.tvDC.setText(hs.getDiaChi());
        h.tvLop.setText(hs.getMaLop());
        h.tvDT.setText(hs.getMaDT());
        h.itemView.setOnClickListener(v -> listener.click(hs));
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    static class VH extends RecyclerView.ViewHolder {
        TextView tvMa, tvTen, tvNS, tvGT, tvDC, tvLop, tvDT;
        VH(View v) {
            super(v);
            tvMa = v.findViewById(R.id.tv_item_ma_hs);
            tvTen = v.findViewById(R.id.tv_item_ten_hs);
            tvNS = v.findViewById(R.id.tv_item_ns);
            tvGT = v.findViewById(R.id.tv_item_gt);
            tvDC = v.findViewById(R.id.tv_item_dc);
            tvLop = v.findViewById(R.id.tv_item_lop);
            tvDT = v.findViewById(R.id.tv_item_dt);
        }
    }
}