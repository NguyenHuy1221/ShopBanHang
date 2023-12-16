package com.example.shopbanhang.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.text.DecimalFormat;
import java.util.List;

public class QuanLyDonHangAdapter extends RecyclerView.Adapter<QuanLyDonHangAdapter.ViewHolder> {

    private HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private Context context;
    private List<HoaDon> mHoadon;

    public QuanLyDonHangAdapter(Context context, List<HoaDon> mHoadon) {
        this.context = context;
        this.mHoadon = mHoadon;
    }

    @NonNull
    @Override
    public QuanLyDonHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hoadon, parent, false);
        return new QuanLyDonHangAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyDonHangAdapter.ViewHolder holder, int position) {
        HoaDon hoaDon = mHoadon.get(position);
//        holder.tvSohoadon.setText("Số Hóa Đơn: " + hoaDon.getMaHD() + "");
//        holder.tvNguoimua.setText("Người Mua: " + hoaDon.getName_khachhang().toUpperCase());
//
//        holder.tvNgaytao.setText("Ngày Tạo: " + hoaDon.getNgaytaoHD());
//        holder.tvGiotao.setText(hoaDon.getGiotaoHD());
//
//        DecimalFormat decimalFormat = new DecimalFormat("#,###");
//        String formattedTien = decimalFormat.format(hoaDon.getTongtien());
//        holder.tvTongtien.setText("Tổng tiền : " + formattedTien + " đ");
//
//        holder.setSanPhamList(hoaDon.getSanPhamList());
//
//        holder.tvTrangThai.setText(trangThaiDonHang(hoaDon.getTinhTrang()));
//        holder.tvTrangThai.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showUpdateStatusDialog(hoaDon);
//            }
//        });
    }

    private void showUpdateStatusDialog(HoaDon hoaDon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn trạng thái mới");
        final String[] options = {"Đang xử lý", "Đã chấp nhận", "Thành công", "Đã hủy"};

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newStatus = which;
                updateOrderStatus(hoaDon, newStatus);
            }
        });

        builder.show();
    }

    private void updateOrderStatus(HoaDon hoaDon, int newStatus) {
//        hoaDon.setTinhTrang(newStatus);
//        hoaDonDAO.updateStatus(hoaDon);
        notifyDataSetChanged();
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


    @Override
    public int getItemCount() {
        if (mHoadon != null) {
            return mHoadon.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        public ViewHolder(@NonNull View itemView) {
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
