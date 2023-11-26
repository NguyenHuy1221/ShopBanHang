package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageChiTietAdapter extends RecyclerView.Adapter<ImageChiTietAdapter.ViewHolder> {

    ArrayList<Uri> imageUri;

    public ImageChiTietAdapter(ArrayList<Uri> imageUri) {
        this.imageUri = imageUri;
    }

    @NonNull
    @Override
    public ImageChiTietAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageChiTietAdapter.ViewHolder holder, int position) {
        Uri imageUris = imageUri.get(position);
        Picasso.get().load(imageUris).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUri.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.chi_tiet_anh);
        }
    }
}
