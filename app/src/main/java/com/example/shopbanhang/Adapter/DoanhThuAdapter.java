package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.DoanhThu;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DoanhThuAdapter extends RecyclerView.Adapter<DoanhThuAdapter.ViewHolder> {

    Context context;
    List<HoaDon> doanhThuslist;

    public DoanhThuAdapter(Context context, List<HoaDon> doanhThuslist) {
        this.context = context;
        this.doanhThuslist = doanhThuslist;
    }

    @NonNull
    @Override
    public DoanhThuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doanh_thu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoanhThuAdapter.ViewHolder holder, int position) {
//        DoanhThu doanhThu = doanhThuslist.get(position);
        HoaDon doanhThu = doanhThuslist.get(position);

        if(doanhThu == null){
            return;
        }

        holder.txtmahd.setText(String.valueOf(doanhThu.getMaHD()));
        holder.txtngaydh.setText(doanhThu.getNgaytaoHD());
        holder.txtttdh.setText(String.valueOf(doanhThu.getTongtien()));



    }

    @Override
    public int getItemCount() {
        if (doanhThuslist != null){
            return doanhThuslist.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtmahd, txtngaydh, txtttdh;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmahd = itemView.findViewById(R.id.txtmadonhang);
            txtngaydh = itemView.findViewById(R.id.txtngaydathang);
            txtttdh = itemView.findViewById(R.id.txttongtiendonhang);
        }
    }
}
