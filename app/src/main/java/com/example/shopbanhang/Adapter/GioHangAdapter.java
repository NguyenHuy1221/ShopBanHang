package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Activity.Gio_Hang;
import com.example.shopbanhang.Model.ChiTietGioHang;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder>{
    private Context context;

    private List<ChiTietGioHang> productList;
    private final IclickListener iclickListener;
    private DatabaseReference chiTietGioHangRef;



    public GioHangAdapter(Context context, List<ChiTietGioHang> productList, IclickListener iclickListener) {
        this.context = context;
        this.productList = productList;
        this.iclickListener = iclickListener;
    }

    @NonNull
    @Override
    public GioHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gio_hang, parent, false);
        return new GioHangAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.ViewHolder holder, int position) {

//        ChiTietGioHang chiTietGioHang = productList.get(position);
//
//        int idGioHang = chiTietGioHang.getId_gio_hang();
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        chiTietGioHangRef = database.getReference("chitietgiohang");
//
//
//        Query query = chiTietGioHangRef.orderByChild("id_gio_hang").equalTo(idGioHang);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    ChiTietGioHang chiTietGioHang = snapshot.getValue(ChiTietGioHang.class);
//
//                    double tongTien = chiTietGioHang.getSo_luong() * chiTietGioHang.getDon_gia();
//
//                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
//                    holder.txtGia.setText(decimalFormat.format(tongTien) + " đ");
//                    holder.txtSo.setText(chiTietGioHang.getSo_luong() + "");
//
//                    // Gọi hàm để lấy thông tin Chi tiết sản phẩm
//                    duLieuChiTietSanPham(chiTietGioHang.getId_chi_tiet_san_pham(), holder);
//
//                    // Xử lý sự kiện cộng và trừ số lượng
//                    holder.txtCong.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            chiTietGioHang.setSo_luong(chiTietGioHang.getSo_luong() + 1);
//                            chiTietGioHang.setTong_tien(chiTietGioHang.getDon_gia() * chiTietGioHang.getSo_luong());
//
//                            DecimalFormat decimalFormat = new DecimalFormat("#,###");
//                            holder.txtGia.setText(decimalFormat.format(chiTietGioHang.getTong_tien()) + " đ");
//                            holder.txtSo.setText(String.valueOf(chiTietGioHang.getSo_luong()));
//
//                            iclickListener.onItemChanged(chiTietGioHang);
//
//                        }
//                    });
//
//                    holder.txtTru.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (chiTietGioHang.getSo_luong() > 1) {
//
//                                chiTietGioHang.setSo_luong(chiTietGioHang.getSo_luong() - 1);
//                                chiTietGioHang.setTong_tien(chiTietGioHang.getDon_gia() * chiTietGioHang.getSo_luong());
//
//                                DecimalFormat decimalFormat = new DecimalFormat("#,###");
//                                holder.txtGia.setText(decimalFormat.format(chiTietGioHang.getTong_tien()) + " đ");
//
//                                holder.txtSo.setText(String.valueOf(chiTietGioHang.getSo_luong()));
//
//                                iclickListener.onItemChanged(chiTietGioHang);
//                            }
//                        }
//                    });
//
//                    holder.txtXoa.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            iclickListener.onclickDeleteCart(chiTietGioHang);
//                        }
//                    });
//
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý lỗi nếu cần thiết
//            }
//        });

        ChiTietGioHang chiTietGioHang = productList.get(position);

        double tongTien = chiTietGioHang.getSo_luong() * chiTietGioHang.getDon_gia();
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        holder.txtGia.setText(decimalFormat.format(tongTien) + " đ");
        holder.txtSo.setText(String.valueOf(chiTietGioHang.getSo_luong()));

        // Gọi hàm để lấy thông tin Chi tiết sản phẩm
        duLieuChiTietSanPham(chiTietGioHang.getId_chi_tiet_san_pham(), holder);

        // Xử lý sự kiện cộng và trừ số lượng
        holder.txtCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chiTietGioHang.setSo_luong(chiTietGioHang.getSo_luong() + 1);
                chiTietGioHang.setTong_tien(chiTietGioHang.getDon_gia() * chiTietGioHang.getSo_luong());

                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                holder.txtGia.setText(decimalFormat.format(chiTietGioHang.getTong_tien()) + " đ");
                holder.txtSo.setText(String.valueOf(chiTietGioHang.getSo_luong()));

                iclickListener.onItemChanged(chiTietGioHang);
            }
        });

        holder.txtTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chiTietGioHang.getSo_luong() > 1) {
                    chiTietGioHang.setSo_luong(chiTietGioHang.getSo_luong() - 1);
                    chiTietGioHang.setTong_tien(chiTietGioHang.getDon_gia() * chiTietGioHang.getSo_luong());

                    DecimalFormat decimalFormat = new DecimalFormat("#,###");
                    holder.txtGia.setText(decimalFormat.format(chiTietGioHang.getTong_tien()) + " đ");
                    holder.txtSo.setText(String.valueOf(chiTietGioHang.getSo_luong()));

                    iclickListener.onItemChanged(chiTietGioHang);
                }
            }
        });

        holder.txtXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickListener.onclickDeleteCart(chiTietGioHang);
            }
        });


    }


    private void duLieuChiTietSanPham(int idChiTietSanPham, ViewHolder holder) {
        DatabaseReference chiTietSanPhamRef = FirebaseDatabase.getInstance().getReference("Chitietsanpham");

        chiTietSanPhamRef.child(String.valueOf(idChiTietSanPham)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ChiTietSanPham chiTietSanPham = dataSnapshot.getValue(ChiTietSanPham.class);

                    holder.txtMau.setText(chiTietSanPham.getMau());
                    holder.txtSize.setText(chiTietSanPham.getKichco());
                    int masp = chiTietSanPham.getMasp();
                    duLieuSanPham(masp, holder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần thiết
            }
        });
    }

    private void duLieuSanPham(int masp, ViewHolder holder) {
        DatabaseReference sanphamRef = FirebaseDatabase.getInstance().getReference("sanpham");
        sanphamRef.child(String.valueOf(masp)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);

                    // lấy giá tiền từ bảng sản phẩm
                    double gia = sanPham.getGiaban();

                    holder.txtName.setText(sanPham.getTensp());
                    Picasso.get().load(sanPham.getImageUrl()).into(holder.imgPic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần thiết
            }
        });
    }



    public interface IclickListener {
        void onItemChanged(ChiTietGioHang chiTietGioHang);
        void onclickDeleteCart(ChiTietGioHang chiTietGioHang);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPic;
        TextView txtName,txtGia,txtMau,txtSize,txtCong,txtSo,txtTru,txtXoa;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPic = itemView.findViewById(R.id.img_pic_gh);
            txtName = itemView.findViewById(R.id.txt_ten_gh);
            txtGia = itemView.findViewById(R.id.txt_gia_gh);
            txtMau = itemView.findViewById(R.id.txt_mau_gh);
            txtSize = itemView.findViewById(R.id.txt_size_gh);
            txtCong = itemView.findViewById(R.id.tvCong);
            txtSo = itemView.findViewById(R.id.tvSo);
            txtTru = itemView.findViewById(R.id.tvtru);
            txtXoa = itemView.findViewById(R.id.txtXoa);
        }
    }

}
