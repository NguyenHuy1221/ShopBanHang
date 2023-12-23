package com.example.shopbanhang.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.DAO.HoaDonDAO;
import com.example.shopbanhang.Model.ChiTietHoaDon;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.HoaDonViewHoder> {

    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private Context context;
    private List<HoaDon> mHoadon;

    public HoaDonAdapter(Context context, List<HoaDon> mHoadon) {
        this.context = context;
        this.mHoadon = mHoadon;
    }

    @NonNull
    @NotNull
    @Override
    public HoaDonViewHoder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hoadon, parent, false);
        return new HoaDonViewHoder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull @NotNull HoaDonViewHoder holder, int position) {
        HoaDon hoaDon = mHoadon.get(position);
        holder.tvSohoadon.setText("Số Hóa Đơn: " + hoaDon.getMaHD() + "");

        holder.tvNgaytao.setText("Ngày Tạo : " + hoaDon.getNgaytaoHD());
        holder.tvGiotao.setText(hoaDon.getGiotaoHD());
        holder.tvDiaChi.setText(hoaDon.getDiaChi());

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTien = decimalFormat.format(hoaDon.getTongtien());
        holder.tvTongtien.setText("Tổng tiền : " + formattedTien + " đ");
        DuLieuTaiKhoan(hoaDon.getIdKhachHang(),holder);
//        holder.setSanPhamList(hoaDon.getSanPhamList());
//
//        holder.tvTrangThai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                showUpdateStatusDialog(hoaDon);
//            }
//        });
        holder.tvTrangThai.setText(trangThaiDonHang(hoaDon.getTinhTrang()));
//        holder.imgDiaChi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showEditDialog("Sửa địa chỉ",holder.tvDiaChi,"diaChi",holder);
//            }
//        });
        holder.imgDiaChi.setVisibility(View.GONE);

        DatabaseReference chiTietHoaDonRef = FirebaseDatabase.getInstance().getReference("chitiethoadon");
        chiTietHoaDonRef.orderByChild("hoa_don").equalTo(hoaDon.getMaHD()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChiTietHoaDon chiTietHoaDon = snapshot.getValue(ChiTietHoaDon.class);
                        chiTietHoaDonList.add(chiTietHoaDon);
                    }

                    // Tạo một thể hiện của ChiTietDonHangAdapter và đặt nó cho rcy_don_hang
                    ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(chiTietHoaDonList);
                    holder.rcy_don_hang.setAdapter(chiTietDonHangAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần thiết
            }
        });
    }

//    private void showUpdateStatusDialog(HoaDon hoaDon) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("Chọn trạng thái mới");
//        final String[] options = {"Đang xử lý", "Đã chấp nhận", "Thành công", "Đã hủy"};
//
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                int newStatus = which;
//                updateOrderStatus(hoaDon, newStatus);
//            }
//        });
//
//        builder.show();
//    }

//    private void updateOrderStatus(HoaDon hoaDon, int newStatus) {
//        hoaDon.setTinhTrang(newStatus);
//        hoaDonDAO.updateStatus(hoaDon);
//        notifyDataSetChanged();
//    }

    private void DuLieuTaiKhoan(int idtk, HoaDonAdapter.HoaDonViewHoder holder){
        DatabaseReference taiKhoanRef = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        taiKhoanRef.child(String.valueOf(idtk)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                    holder.tvNguoimua.setText("Khách hàng : "+taiKhoan.getTentk());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private String trangThaiDonHang(int status) {
        String result = "";

        switch (status) {
            case 0:
                result = "Đơn hàng đang được xử lý ";
                break;
            case 1:
                result = "Đơn hàng đã chấp nhận ";
                break;
            case 2:
                result = "Thành công ";
                break;
            case 3:
                result = "Đơn hàng đã hủy ";
                break;
        }

        return result;
    }

//    private void showEditDialog(String title, TextView textView, String fieldName, HoaDonViewHoder holder) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle(title);
//
//        final EditText input = new EditText(context);
//        // Hiển thị thông tin hiện tại trong EditText
//        input.setText(textView.getText().toString());
//
//        builder.setView(input);
//
//        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String newValue = input.getText().toString();
//                textView.setText(newValue);
//
//                LuuThongTin(holder);
//
//
//
//            }
//        });
//
//        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//
//        builder.show();
//    }
//
//
//    private void LuuThongTin(HoaDonViewHoder holder) {
//
//        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("hoadon");
//        String address = holder.tvDiaChi.getText().toString();
//
//        if (address.isEmpty()) {
//            Toast.makeText(context, "Địa chỉ trống", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int hoadonId = mHoadon.get(holder.getAdapterPosition()).getMaHD();
//        Map<String, Object> updates2 = new HashMap<>();
//        updates2.put("diaChi",address);
//        mDatabaseReference.child(String.valueOf(hoadonId)).updateChildren(updates2);
//
//    }

    @Override
    public int getItemCount() {
        if (mHoadon != null) {
            return mHoadon.size();
        }
        return 0;
    }

    public class HoaDonViewHoder extends RecyclerView.ViewHolder {
        private TextView tvSohoadon;
        private TextView tvTongtien;
        private TextView tvTinhtrang;
        private TextView tvDiaChi;
        private TextView tvNguoimua;
        private TextView tvDanhsachmathang;
        private TextView tvNgaytao;
        private TextView tvTrangThai;
        private TextView tvGiotao;
        private Button btnThanhToan;
        private CardView view_hoadon;
        private RecyclerView rcy_don_hang;
        private ImageView imgDiaChi;

        public HoaDonViewHoder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTongtien = itemView.findViewById(R.id.tvTongtien);
//            tvTinhtrang = itemView.findViewById(R.id.tvTinhtrang);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvNguoimua = itemView.findViewById(R.id.tvNguoimua);
            tvSohoadon = itemView.findViewById(R.id.tvSohoadon);
            tvNgaytao = itemView.findViewById(R.id.tvNgaymua);
            tvGiotao = itemView.findViewById(R.id.tvGiomua);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            imgDiaChi = itemView.findViewById(R.id.img_dia_chi);
            view_hoadon = itemView.findViewById(R.id.view_hoadon);
            rcy_don_hang = itemView.findViewById(R.id.rcy_donhang);
            rcy_don_hang.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        }

    }
}
