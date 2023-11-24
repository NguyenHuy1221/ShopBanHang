package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;

import java.util.List;
import java.util.zip.Inflater;

public class KhuyenMaiAdapter extends RecyclerView.Adapter<KhuyenMaiAdapter.ViewHolder> {
    Context context;
    List<KhuyenMai> khuyenMaiList;

    public KhuyenMaiAdapter(Context context, List<KhuyenMai> khuyenMaiList) {
        this.context = context;
        this.khuyenMaiList = khuyenMaiList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khuyen_mai,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        if(khuyenMaiList.size() > 0){
            return khuyenMaiList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgKhuyenMai;
        TextView txtTenKhuyenMai,txtPhanTramKhuyenMai,txtNgayBatDau,txtNgayKetThuc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgKhuyenMai = itemView.findViewById(R.id.img_khuyenMai);
            txtTenKhuyenMai = itemView.findViewById(R.id.txtTenKhuyenMai);
            txtPhanTramKhuyenMai = itemView.findViewById(R.id.txtPhanTramKhuyenMai);
            txtNgayBatDau = itemView.findViewById(R.id.txtNgayBatDau);
            txtNgayKetThuc = itemView.findViewById(R.id.txtNgayKetThuc);
        }
    }
}
