package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.ChiTietHoaDon;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class ChiTietDonHangAdapter extends RecyclerView.Adapter<ChiTietDonHangAdapter.ViewHolder> {

    private List<ChiTietHoaDon> mListHoaDon;

    public ChiTietDonHangAdapter(List<ChiTietHoaDon> mListHoaDon) {
        this.mListHoaDon = mListHoaDon;
    }

    @NonNull
    @Override
    public ChiTietDonHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietDonHangAdapter.ViewHolder holder, int position) {
        ChiTietHoaDon chiTietHoaDon = mListHoaDon.get(position);
        holder.item_giaspchitiet.setText("SL : " + chiTietHoaDon.getSo_luong());
        NumberFormat formatter = new DecimalFormat("#,###");
        holder.txt_gia.setText("Tổng : " + formatter.format(chiTietHoaDon.getDon_gia()));

        duLieuChiTietSanPham(chiTietHoaDon.getId_chi_tiet_san_pham(),holder);
    }

    private void duLieuChiTietSanPham(int idChiTietSanPham, ChiTietDonHangAdapter.ViewHolder holder) {
        DatabaseReference chiTietSanPhamRef = FirebaseDatabase.getInstance().getReference("Chitietsanpham");

        chiTietSanPhamRef.child(String.valueOf(idChiTietSanPham)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ChiTietSanPham chiTietSanPham = dataSnapshot.getValue(ChiTietSanPham.class);

                    holder.txt_size.setText("Size : "+chiTietSanPham.getKichco());
                    int masp = chiTietSanPham.getMasp();
                    DuLieuSanPham(masp,holder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần thiết
            }
        });
    }

    private void DuLieuSanPham(int masp, ChiTietDonHangAdapter.ViewHolder holder){
        DatabaseReference sanphamRef = FirebaseDatabase.getInstance().getReference("sanpham");
        sanphamRef.child(String.valueOf(masp)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);

                    holder.item_tenspchitiet.setText("Tên sp : " + sanPham.getTensp());
                    Picasso.get().load(sanPham.getImageUrl()).into(holder.item_img_chhitiet);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public int getItemCount() {
        return mListHoaDon.size();
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
