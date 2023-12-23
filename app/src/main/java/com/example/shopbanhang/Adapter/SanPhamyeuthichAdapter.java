package com.example.shopbanhang.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Activity.ChiTietSanPhamActivity;
import com.example.shopbanhang.DAO.SanPhamyeuThichDAO;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.YeuThichSanPham;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SanPhamyeuthichAdapter extends RecyclerView.Adapter<SanPhamyeuthichAdapter.ViewHodel> {

    private Context context;
    private List<SanPham> mSanPham;

    private List<SanPham> mFilteredSanPhamList;
    private int user;
    private List<YeuThichSanPham> mYeuThichSanPhamList;
    private SanPhamyeuThichDAO sanPhamyeuThichDAO = new SanPhamyeuThichDAO();
//    private final SanPhamAdapter.OnClickItem onClickItem;



//    public SanPhamyeuthichAdapter(Context context, List<SanPham> mSanPham, SanPhamAdapter.OnClickItem onClickItem) {
//        this.context = context;
//        this.mSanPham = mSanPham;
//        this.mFilteredSanPhamList = new ArrayList<>(mSanPham);
//        this.mYeuThichSanPhamList = new ArrayList<>();
//        this.onClickItem = onClickItem;
//    }

        public SanPhamyeuthichAdapter(Context context, List<SanPham> mSanPham) {
        this.context = context;
        this.mSanPham = mSanPham;
        this.mFilteredSanPhamList = new ArrayList<>(mSanPham);
        this.mYeuThichSanPhamList = new ArrayList<>();
    }

    public void updateData(List<SanPham> newData) {
        mSanPham.clear();
        mSanPham.addAll(newData);
        notifyDataSetChanged();
    }
    public void updateYeuThichList(List<YeuThichSanPham> yeuThichList) {
        mYeuThichSanPhamList.clear();
        mYeuThichSanPhamList.addAll(yeuThichList);
        notifyDataSetChanged();
    }
    public void addSanPham(SanPham sanPham) {
        mSanPham.add(sanPham);
        notifyItemInserted(mSanPham.size() - 1);
    }
    @NonNull
    @Override
    public SanPhamyeuthichAdapter.ViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sanpham_yeuthich,parent,false);
        return new ViewHodel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamyeuthichAdapter.ViewHodel holder, int position) {


        SanPham sanPham = mSanPham.get(position);
        holder.txt_name.setText(sanPham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTien = decimalFormat.format(sanPham.getGiaban());
        holder.txt_pirce.setText(formattedTien + " đ");
        Picasso.get().load(sanPham.getImageUrl()).into(holder.img_main);


        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        if (user !=0){
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
                YeuThichSanPham yeuThichSanPham = new YeuThichSanPham();

//                Xoasanphamyeuthich(mYeuThichSanPhamList);;=-[p;l0
//                onClickItem.onclickDelete(sanPham);
//                Xoasanphamyeuthich(sanPham);
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Xóa sản phẩm yếu thích")
                        .setMessage("Bạn Có Chắc Muốn Xóa Sản Phẩm Yêu Thích Này")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                sanPhamyeuThichDAO.deleteHoaDon(sanPham);
                                mSanPham.remove(holder.getAdapterPosition());
                                Toast.makeText(context, "Xóa sản phẩm yêu thích thành công", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
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


    private void Xoasanphamyeuthich(SanPham sanPham) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("SanPhamYeuThich");
//        databaseReference.child(String.valueOf(sanPham.getMasp()).removeValue();
    }

    private boolean isFavoriteProduct(int productId) {
        for (YeuThichSanPham yeuThich : mYeuThichSanPhamList) {
            if (yeuThich.getId_san_pham() == productId) {
                return true;
            }
        }
        return false;
    }

//    public abstract void onclickDelete(SanPham sanPham);
//
////    public abstract void onclickDelete(SanPham sanPham);
//
//    public interface OnClickItem {
//
//        void onclickDelete(SanPham sanPham);
//    }


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
