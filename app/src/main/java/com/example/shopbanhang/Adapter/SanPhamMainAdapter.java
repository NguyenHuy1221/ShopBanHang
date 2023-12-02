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

import java.util.ArrayList;
import java.util.List;

public class SanPhamMainAdapter extends RecyclerView.Adapter<SanPhamMainAdapter.ViewHodel> {

    private Context context;
    private List<SanPham> mSanPham;
    private boolean clickTym = false;
    private DatabaseReference databaseReference;

    private List<SanPham> mFilteredSanPhamList;


    public SanPhamMainAdapter(Context context, List<SanPham> mSanPham) {
        this.context = context;
        this.mSanPham = mSanPham;
        this.mFilteredSanPhamList = new ArrayList<>(mSanPham);
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
        holder.txt_pirce.setText(String.valueOf(sanPham.getGiaban()));
        Picasso.get().load(sanPham.getImageUrl()).into(holder.img_main);

        holder.img_tym.setColorFilter(Color.BLACK);

        holder.img_tym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickTym){
                    holder.img_tym.setColorFilter(Color.BLACK);
//                    removeProductFromFavorites();
                }else {
                    holder.img_tym.setColorFilter(Color.RED);
//                    addProductToFavorites();
                }
                clickTym = !clickTym;
            }
        });



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ChiTietSanPhamActivity.class);
                intent.putExtra("masp",sanPham.getMasp());
                intent.putExtra("tensp",sanPham.getTensp());
                intent.putExtra("giaban",sanPham.getGiaban());
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

//    private void addProductToFavorites() {
//        String productId = String.valueOf(sanPham.getMasp());
//        databaseReference.child(productId).setValue(true);
//    }



//    private void removeProductFromFavorites() {
//        String productId = String.valueOf(sanPham.getMasp());
//
//        databaseReference.child(productId).removeValue();
//    }

    public void filterList(List<SanPham> filteredList) {
        mSanPham.clear();
        mSanPham.addAll(filteredList);
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
