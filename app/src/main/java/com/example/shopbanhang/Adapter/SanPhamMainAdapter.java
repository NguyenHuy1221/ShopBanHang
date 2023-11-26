package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Activity.ChiTietSanPhamActivity;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SanPhamMainAdapter extends RecyclerView.Adapter<SanPhamMainAdapter.ViewHodel> {

    private Context context;
    private List<SanPham> mSanPham;

    public SanPhamMainAdapter(Context context, List<SanPham> mSanPham) {
        this.context = context;
        this.mSanPham = mSanPham;
    }

    @NonNull
    @Override
    public SanPhamMainAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham_main,parent,false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamMainAdapter.ViewHodel holder, int position) {

        SanPham sanPham = mSanPham.get(position);
        holder.txt_name.setText(sanPham.getTensp());
        holder.txt_pirce.setText(String.valueOf(sanPham.getGiaban()));
        Picasso.get().load(sanPham.getImageUrl()).into(holder.img_main);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ChiTietSanPhamActivity.class);
                intent.putExtra("masp",sanPham.getMasp());
                intent.putExtra("tensp",sanPham.getTensp());
                intent.putExtra("giaban",sanPham.getGiaban());
                intent.putExtra("ghichu",sanPham.getGhichu());
                intent.putExtra("imageUrl",sanPham.getImageUrl());
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSanPham.size();
    }

    public class ViewHodel extends RecyclerView.ViewHolder {

        private TextView txt_name,txt_pirce;
        private ImageView img_main,img_tym;
        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_pirce = itemView.findViewById(R.id.txt_price);
            img_main = itemView.findViewById(R.id.img_main);
            img_tym = itemView.findViewById(R.id.img_tym);
        }
    }
}
