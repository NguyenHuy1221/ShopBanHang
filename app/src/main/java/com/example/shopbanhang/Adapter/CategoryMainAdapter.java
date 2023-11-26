package com.example.shopbanhang.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CategoryMainAdapter extends RecyclerView.Adapter<CategoryMainAdapter.ViewHolder> {

    private Context mContext;
    private List<ThuongHieu> thuongHieus;

    public CategoryMainAdapter(Context mContext, List<ThuongHieu> thuongHieus) {
        this.mContext = mContext;
        this.thuongHieus = thuongHieus;
    }

    @NonNull
    @Override
    public CategoryMainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryMainAdapter.ViewHolder holder, int position) {
      ThuongHieu thuongHieu = thuongHieus.get(position);
      Picasso.get().load(thuongHieu.getImageUrl()).into(holder.img_category);
      holder.tv_category.setText(thuongHieu.getTenThuongHieu());
    }

    @Override
    public int getItemCount() {
        return thuongHieus.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_category;
        TextView tv_category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_category = itemView.findViewById(R.id.img_category);
            tv_category = itemView.findViewById(R.id.text_category);
        }
    }
}
