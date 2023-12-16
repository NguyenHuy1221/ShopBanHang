package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.ViewHolder> {

    private List<GioHang> sanPhamList;

    public ChiTietDonHangAdapter(List<GioHang> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }

    @NonNull
    @Override
    public ChiTietDonHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietDonHangAdapter.ViewHolder holder, int position) {
        GioHang gioHang = sanPhamList.get(position);

//        holder.item_tenspchitiet.setText(gioHang.getTensp());
//        holder.item_giaspchitiet.setText("Số lượng: " + gioHang.getSoluong());
//
//        DecimalFormat decimalFormat = new DecimalFormat("#,###");
//        String formattedTien = decimalFormat.format(gioHang.getTongtien());
//        holder.txt_gia.setText(formattedTien + " đ");
//
//        holder.txt_size.setText("Size: "+ gioHang.getSize());
//        Picasso.get().load(gioHang.getUrl()).into(holder.item_img_chhitiet);
    }

    @Override
    public int getItemCount() {
        return sanPhamList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_img_chhitiet;
        private TextView item_giaspchitiet,item_tenspchitiet,txt_size,txt_gia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_img_chhitiet = itemView.findViewById(R.id.item_img_chhitiet);
            item_tenspchitiet = itemView.findViewById(R.id.item_tenspchitiet);
            item_giaspchitiet = itemView.findViewById(R.id.item_giaspchitiet);
            txt_gia = itemView.findViewById(R.id.item_giachitiet);
            txt_size = itemView.findViewById(R.id.item_sizechitiet);


        }
    }
}
