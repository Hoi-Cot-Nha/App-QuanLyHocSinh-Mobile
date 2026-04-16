package com.example.quanlyhocsinhmobile.ui.dat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlyhocsinhmobile.R;
import com.example.quanlyhocsinhmobile.data.local.Model.ToHopMon;

import java.util.List;

public class ToBoMonAdapter extends RecyclerView.Adapter<ToBoMonAdapter.ViewHolder> {

    private List<ToHopMon> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ToHopMon toHopMon);
    }

    public ToBoMonAdapter(List<ToHopMon> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<ToHopMon> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dat_item_tobomon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToHopMon toHopMon = list.get(position);

        holder.tvMaToHop.setText(toHopMon.getMaToHop());
        holder.tvTenToHop.setText(toHopMon.getTenToHop());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(toHopMon));
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaToHop, tvTenToHop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaToHop = itemView.findViewById(R.id.item_ma_to_hop);
            tvTenToHop = itemView.findViewById(R.id.item_ten_to_hop);
        }
    }
}