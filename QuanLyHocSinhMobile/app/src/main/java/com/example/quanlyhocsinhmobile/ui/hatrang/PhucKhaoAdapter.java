package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.PhucKhao;

import java.util.List;

public class PhucKhaoAdapter extends RecyclerView.Adapter<PhucKhaoAdapter.ViewHolder> {

    private List<PhucKhao.Display> list;
    private OnClick listener;

    // Interface để xử lý sự kiện click khi chọn một đơn phúc khảo
    public interface OnClick {
        void click(PhucKhao.Display d);
    }

    public PhucKhaoAdapter(List<PhucKhao.Display> list, OnClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<PhucKhao.Display> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<PhucKhao.Display> getList() {
        return list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.hatrang_item_phuckhao, p, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        PhucKhao.Display d = list.get(i);
        PhucKhao pk = d.getPhucKhao();

        // Đổ dữ liệu từ Model vào các TextView
        h.mahs.setText(pk.getMaHS());
        h.hoten.setText(d.getTenHS());
        h.tenmon.setText(d.getTenMH());
        h.lydo.setText(pk.getLyDo());
        h.ngaygui.setText(pk.getNgayGui());
        h.trangthai.setText(pk.getTrangThai());

        // Xử lý sự kiện click vào item
        h.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.click(d);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mahs, hoten, tenmon, lydo, ngaygui, trangthai;

        public ViewHolder(@NonNull View v) {
            super(v);
            mahs = v.findViewById(R.id.tv_mahs);
            hoten = v.findViewById(R.id.tv_hoten);
            tenmon = v.findViewById(R.id.tv_tenmon);
            lydo = v.findViewById(R.id.tv_lydo);
            ngaygui = v.findViewById(R.id.tv_ngaygui);
            trangthai = v.findViewById(R.id.tv_trangthai);
        }
    }
}