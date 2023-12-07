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
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder>{
    private Context context;

    private List<GioHang> productList;
    private final IclickListener iclickListener;


    public GioHangAdapter(Context context, List<GioHang> productList, IclickListener iclickListener) {
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
        GioHang gioHang = productList.get(position);

        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        holder.txtName.setText(gioHang.getTensp());
        holder.txtMau.setText(gioHang.getColor());
        holder.txtSize.setText(gioHang.getSize());
        holder.txtGia.setText(decimalFormat.format(gioHang.getTongtien()) + " đ");

        holder.txtSo.setText(String.valueOf(gioHang.getSoluong()));
        Picasso.get().load(gioHang.getUrl()).into(holder.imgPic);

        holder.txtCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gioHang.setSoluong(gioHang.getSoluong() + 1);
                gioHang.setTongtien(gioHang.getGiasp() * gioHang.getSoluong());

                holder.txtSo.setText(String.valueOf(gioHang.getSoluong()));
                holder.txtGia.setText(gioHang.getTongtien()+" đ");
                iclickListener.onItemChanged(gioHang);
            }
        });

        holder.txtTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gioHang.getSoluong() > 1) {
                    gioHang.setSoluong(gioHang.getSoluong() - 1);
                    gioHang.setTongtien(gioHang.getGiasp() * gioHang.getSoluong());

                    holder.txtSo.setText(String.valueOf(gioHang.getSoluong()));
                    holder.txtGia.setText(gioHang.getTongtien()+" đ");
                    iclickListener.onItemChanged(gioHang);
                }
            }
        });

        holder.txtXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickListener.onclickDeleteCart(gioHang);
            }
        });
    }

    public interface IclickListener {
        void onItemChanged(GioHang gioHang);
        void onclickDeleteCart(GioHang gioHang);
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
