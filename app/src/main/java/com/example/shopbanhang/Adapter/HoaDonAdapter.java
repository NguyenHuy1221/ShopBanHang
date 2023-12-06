package com.example.shopbanhang.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.DAO.HoaDonDAO;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.R;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;


public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.HoaDonViewHoder> {

    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private List<HoaDon> mHoadon;
    private OnStatusClickListener mStatusClickListener;



    public HoaDonAdapter(List<HoaDon> mHoadon) {
        this.mHoadon = mHoadon;
    }
    public void setOnStatusClickListener(OnStatusClickListener listener) {
        mStatusClickListener = listener;
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
        holder.tvNguoimua.setText("Người Mua: " + hoaDon.getName_khachhang().toUpperCase());

        holder.tvNgaytao.setText("Ngày Tạo: " + hoaDon.getNgaytaoHD());
        holder.tvGiotao.setText(hoaDon.getGiotaoHD());

        holder.tvTongtien.setText("Tổng Tiền: " + hoaDon.getTongtien() + "$");

        holder.setSanPhamList(hoaDon.getSanPhamList());

        holder.tvTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStatusClickListener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        mStatusClickListener.onStatusClick(adapterPosition, hoaDon.getTinhTrang());
                    }
                }
            }
        });


    }

    public interface OnStatusClickListener {
        void onStatusClick(int position, int currentStatus);
    }



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
        private TextView tvNguoimua;
        private TextView tvDanhsachmathang;
        private TextView tvNgaytao;
        private TextView tvTrangThai;
        private TextView tvGiotao;
        private Button btnThanhToan;
        private CardView view_hoadon;
        private RecyclerView rcy_don_hang;

        public HoaDonViewHoder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTongtien = itemView.findViewById(R.id.tvTongtien);
//            tvTinhtrang = itemView.findViewById(R.id.tvTinhtrang);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvNguoimua = itemView.findViewById(R.id.tvNguoimua);
            tvSohoadon = itemView.findViewById(R.id.tvSohoadon);
            tvNgaytao = itemView.findViewById(R.id.tvNgaymua);
            tvGiotao = itemView.findViewById(R.id.tvGiomua);
            view_hoadon = itemView.findViewById(R.id.view_hoadon);
            rcy_don_hang = itemView.findViewById(R.id.rcy_donhang);
            rcy_don_hang.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
        public void setSanPhamList(List<GioHang> sanPhamList) {
            ChiTietDonHangAdapter chiTietDonHangAdapter = new ChiTietDonHangAdapter(sanPhamList);
            rcy_don_hang.setAdapter(chiTietDonHangAdapter);
        }
    }
}
