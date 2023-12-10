package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SanPhamMainAdapter extends RecyclerView.Adapter<SanPhamMainAdapter.ViewHodel> {

    private Context context;
    private List<SanPham> mSanPham;
    private boolean clickTym = false;
    private DatabaseReference databaseReference;
    private List<String> favoriteProductIds = new ArrayList<>();

    private List<SanPham> mFilteredSanPhamList;


    public SanPhamMainAdapter(Context context, List<SanPham> mSanPham) {
        this.context = context;
        this.mSanPham = mSanPham;
        this.mFilteredSanPhamList = new ArrayList<>(mSanPham);
    }

    public void updateData(List<SanPham> newData) {
        mSanPham.clear();
        mSanPham.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SanPhamMainAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_san_pham_main,parent,false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamMainAdapter.ViewHodel holder, int position) {


//        databaseReference = FirebaseDatabase.getInstance().getReference().child("sanphamyeuthich");
        SanPham sanPham = mSanPham.get(position);
        holder.txt_name.setText(sanPham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTien = decimalFormat.format(sanPham.getGiaban());
        holder.txt_pirce.setText(formattedTien + " đ");
        Picasso.get().load(sanPham.getImageUrl()).into(holder.img_main);


        String productId = String.valueOf(sanPham.getMasp());

        // Kiểm tra trạng thái yêu thích và cập nhật giao diện người dùng
        if (isFavorite(productId)) {
            holder.img_tym.setColorFilter(Color.RED);
        } else {
            holder.img_tym.setColorFilter(Color.BLACK);
        }

        holder.img_tym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra trạng thái yêu thích khi ấn vào nút
                boolean isFavorite = isFavorite(productId);

                // Thay đổi trạng thái yêu thích khi ấn vào nút
                if (isFavorite) {
                    holder.img_tym.setColorFilter(Color.BLACK);
                    removeFromFavorites(productId);
                } else {
                    holder.img_tym.setColorFilter(Color.RED);
                    addToFavorites(productId);
                }
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ChiTietSanPhamActivity.class);
                intent.putExtra("masp",sanPham.getMasp());
                intent.putExtra("tensp",sanPham.getTensp());
                intent.putExtra("giaban",sanPham.getGiaban());
                intent.putExtra("soluongnhap",sanPham.getSoluongnhap());
                intent.putExtra("ghichu",sanPham.getGhichu());
                intent.putExtra("imageUrl",sanPham.getImageUrl());
                List<String> urlChiTiet = sanPham.getUrlChiTiet();
                if (urlChiTiet!= null){
                    intent.putStringArrayListExtra("urlChiTiet",new ArrayList<>(urlChiTiet));
                }
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    // Phương thức để thêm sản phẩm vào danh sách yêu thích
    private void addToFavorites(String productId) {
        if (!favoriteProductIds.contains(productId)) {
            favoriteProductIds.add(productId);
            saveFavoriteProducts();
        }
    }

    private void removeFromFavorites(String productId) {
        favoriteProductIds.remove(productId);
        saveFavoriteProducts();
    }

    // Lưu các ID sản phẩm yêu thích vào SharedPreferences
    private void saveFavoriteProducts() {
        SharedPreferences preferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> favoriteSet = new HashSet<>(favoriteProductIds);
        editor.putStringSet("favoriteProducts", favoriteSet);
        editor.apply();
    }

    // Tải các ID sản phẩm yêu thích từ SharedPreferences
    private void loadFavoriteProducts() {
        SharedPreferences preferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        Set<String> favoriteSet = preferences.getStringSet("favoriteProducts", new HashSet<>());
        favoriteProductIds.addAll(favoriteSet);
    }

    private boolean isFavorite(String productId) {
        return favoriteProductIds.contains(productId);
    }




    public void filterList(List<SanPham> filteredList) {
        mFilteredSanPhamList.clear();
        mFilteredSanPhamList.addAll(filteredList);
        notifyDataSetChanged();
    }

    public void clear() {
        mFilteredSanPhamList.clear();
        notifyDataSetChanged();
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
