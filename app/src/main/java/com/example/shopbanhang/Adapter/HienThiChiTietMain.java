package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Activity.ChiTietSanPhamActivity;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HienThiChiTietMain extends RecyclerView.Adapter<HienThiChiTietMain.ViewHodel> {
    private Context context;
    private List<String> stringList;
    private OnItemClickListener listener;

    public HienThiChiTietMain(Context context, List<String> stringList, OnItemClickListener listener) {
        this.context = context;
        this.stringList = stringList;
        this.listener = listener;
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


    public interface OnItemClickListener {
        void onItemClick(String imageUrl);
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

            imageView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(stringList.get(position));
                    }
                }
            });
        }
    }
}
