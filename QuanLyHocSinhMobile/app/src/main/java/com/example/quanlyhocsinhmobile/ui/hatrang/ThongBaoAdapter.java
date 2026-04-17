package com.example.quanlyhocsinhmobile.ui.hatrang;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.ThongBao;
import java.util.List;
public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ViewHolder> {
    private List<ThongBao> list;
    private OnClick listener;
    public interface OnClick {
        void click(ThongBao tb);
    }
    public ThongBaoAdapter(List<ThongBao> list, OnClick listener) {
        this.list = list;
        this.listener = listener;
    }
    public void setList(List<ThongBao> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public List<ThongBao> getList() {
        return list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new ViewHolder(LayoutInflater.from(p.getContext())
                .inflate(R.layout.hatrang_item_thongbao, p, false));
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        ThongBao tb = list.get(i);
        h.matb.setText(tb.getMaTB() + "");
        h.tieude.setText(tb.getTieuDe());
        h.noidung.setText(tb.getNoiDung());
        h.ngaytao.setText(tb.getNgayTao());
        h.nguoigui.setText(tb.getNguoiGui());
        h.itemView.setOnClickListener(v -> listener.click(tb));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView matb, tieude, noidung, ngaytao, nguoigui;
        public ViewHolder(@NonNull View v) {
            super(v);
            matb = v.findViewById(R.id.tv_matb);
            tieude = v.findViewById(R.id.tv_tieude);
            noidung = v.findViewById(R.id.tv_noidung);
            ngaytao = v.findViewById(R.id.tv_ngaytao);
            nguoigui = v.findViewById(R.id.tv_nguoigui);
        }
    }
}