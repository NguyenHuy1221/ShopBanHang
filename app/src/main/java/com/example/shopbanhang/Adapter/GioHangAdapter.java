package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder>{

    private List<SanPham> productList;
    private Context context;

    public GioHangAdapter(List<SanPham> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public GioHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang, parent, false);
        return new GioHangAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.ViewHolder holder, int position) {
        SanPham sanPham = productList.get(position);
        holder.txtName.setText(sanPham.getTensp());
        holder.txtMau.setText(sanPham.getMausp());
        holder.txtSize.setText(sanPham.getSizesp());
        Picasso.get().load(sanPham.getImageUrl()).into(holder.imgPic);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPic;
        TextView txtName,txtGia,txtMau,txtSize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPic = itemView.findViewById(R.id.img_pic_gh);
            txtName = itemView.findViewById(R.id.txt_ten_gh);
//            txtGia = itemView.findViewById(R.id.txt_g_gh);
            txtMau = itemView.findViewById(R.id.txt_mau_gh);
            txtSize = itemView.findViewById(R.id.txt_size_gh);
        }
    }
}
