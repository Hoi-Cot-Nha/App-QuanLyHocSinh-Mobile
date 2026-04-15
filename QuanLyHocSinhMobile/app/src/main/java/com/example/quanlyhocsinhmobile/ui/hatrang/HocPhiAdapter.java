package com.example.quanlyhocsinhmobile.ui.hatrang;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.HocPhi;

import java.util.List;

public class HocPhiAdapter extends RecyclerView.Adapter<HocPhiAdapter.ViewHolder> {

    private List<HocPhi.Display> list;
    private OnClick listener;

    public interface OnClick {
        void click(HocPhi.Display d);
    }

    public HocPhiAdapter(List<HocPhi.Display> list, OnClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<HocPhi.Display> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<HocPhi.Display> getList() {
        return list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        return new ViewHolder(LayoutInflater.from(p.getContext()).inflate(R.layout.hatrang_item_hocphi, p, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {
        HocPhi.Display d = list.get(i);
        HocPhi hp = d.getHocPhi();

        h.ma.setText(hp.getMaHS());
        h.ten.setText(d.getTenHS());
        h.lop.setText(d.getTenLop());
        h.hk.setText(hp.getHocKy()+"");
        h.nam.setText(hp.getNamHoc());
        h.tong.setText(hp.getTongTien()+"");
        h.mg.setText(hp.getMienGiam()+"");
        h.pd.setText(hp.getPhaiDong()+"");
        h.tt.setText(hp.getTrangThai());

        h.itemView.setOnClickListener(v -> listener.click(d));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ma, ten, lop, hk, nam, tong, mg, pd, tt;

        public ViewHolder(@NonNull View v) {
            super(v);
            ma = v.findViewById(R.id.tv_mahs);
            ten = v.findViewById(R.id.tv_hoten);
            lop = v.findViewById(R.id.tv_lop);
            hk = v.findViewById(R.id.tv_hk);
            nam = v.findViewById(R.id.tv_namhoc);
            tong = v.findViewById(R.id.tv_tongtien);
            mg = v.findViewById(R.id.tv_miengiam);
            pd = v.findViewById(R.id.tv_phaidong);
            tt = v.findViewById(R.id.tv_trangthai);
        }
    }
}