package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HienThiChiTietMain extends RecyclerView.Adapter<HienThiChiTietMain.ViewHodel> {
    private Context context;
    private List<String> stringList;

    public HienThiChiTietMain(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public HienThiChiTietMain.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HienThiChiTietMain.ViewHodel holder, int position) {
        String imageUrl = stringList.get(position);
        Picasso.get().load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHodel extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHodel(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chi_tiet_anh);
        }
    }
}
