package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.YeuThichSanPham;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AoQuanAdapter extends RecyclerView.Adapter<AoQuanAdapter.ViewHolder> {

    private Context context;
    private List<SanPham> mSanPham;

    private List<SanPham> mFilteredSanPhamList;
    private int user;
    private List<YeuThichSanPham> mYeuThichSanPhamList;

    public AoQuanAdapter(Context context, List<SanPham> mSanPham) {
        this.context = context;
        this.mSanPham = mSanPham;
        this.mFilteredSanPhamList = new ArrayList<>(mSanPham);
        this.mYeuThichSanPhamList = new ArrayList<>();
    }

    @NonNull
    @Override
    public AoQuanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_quan_ao,parent,false);
        return new AoQuanAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AoQuanAdapter.ViewHolder holder, int position) {
        SanPham sanPham = mSanPham.get(position);
        holder.txt_name.setText(sanPham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTien = decimalFormat.format(sanPham.getGiaban());
        holder.txt_pirce.setText(formattedTien + " đ");
        Picasso.get().load(sanPham.getImageUrl()).into(holder.img_main);


        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        if (!mySharedPreferences.getValue("remember_id_tk").isEmpty()){
            user = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));
        }


        if (isFavoriteProduct(sanPham.getMasp())) {
            holder.img_tym.setColorFilter(Color.RED);
        } else {
            holder.img_tym.setColorFilter(Color.BLACK);
        }

        holder.img_tym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int id = Integer.parseInt(String.valueOf(random.nextInt(1000000)));
                YeuThichSanPham yeuThichSanPham = new YeuThichSanPham(id, user, sanPham.getMasp());
                ThemSanPhamYeuThich(yeuThichSanPham);

                holder.img_tym.setColorFilter(Color.RED);
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

    private void ThemSanPhamYeuThich(YeuThichSanPham yeuThichSanPham) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("SanPhamYeuThich");

        DatabaseReference productRef = databaseReference.child(String.valueOf(yeuThichSanPham.getId_san_pham()));

        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    productRef.setValue(yeuThichSanPham);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error checking if product exists: " + error.getMessage());
            }
        });
    }

    private boolean isFavoriteProduct(int productId) {
        for (YeuThichSanPham yeuThich : mYeuThichSanPhamList) {
            if (yeuThich.getId_san_pham() == productId) {
                return true;
            }
        }
        return false;
    }



    public void filterList(List<SanPham> filteredList) {
        mSanPham.clear();
        mSanPham.addAll(filteredList);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_name,txt_pirce;
        private ImageView img_main,img_tym;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_pirce = itemView.findViewById(R.id.txt_price);
            img_main = itemView.findViewById(R.id.img_main);
            img_tym = itemView.findViewById(R.id.img_tym);
        }
    }
}
