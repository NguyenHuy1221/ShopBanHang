package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.ChiTietSanPhamfix;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChiTietSanPhamAdapter extends RecyclerView.Adapter<ChiTietSanPhamAdapter.ViewHolder>{
    Context context;
    List<ChiTietSanPhamfix> chiTietSanPhams;

    private Click mClick;

    public interface Click{
        void onUpdateClick(ChiTietSanPhamfix chiTietSanPham);
        void onDeleteClick(ChiTietSanPhamfix chiTietSanPham);
    }

    public ChiTietSanPhamAdapter(Context context, List<ChiTietSanPhamfix> chiTietSanPhams, Click mClick) {
        this.context = context;
        this.chiTietSanPhams = chiTietSanPhams;
        this.mClick = mClick;
    }

    @NonNull
    @Override
    public ChiTietSanPhamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sua_chi_tiet_sp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChiTietSanPhamAdapter.ViewHolder holder, int position) {
        ChiTietSanPhamfix chiTietSanPham = chiTietSanPhams.get(position);
        if(chiTietSanPham == null){
            return;
        }

        holder.size.setText(chiTietSanPham.getKichco());
        holder.color.setText(chiTietSanPham.getMau());
        holder.number.setText(String.valueOf(chiTietSanPham.getSoluong()));


        holder.imgsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onUpdateClick(chiTietSanPham);
            }
        });

        holder.imgxoa.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClick.onDeleteClick(chiTietSanPham);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(chiTietSanPhams != null){
            return chiTietSanPhams.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgsua,imgxoa;
        TextView size,color,number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.txtkickco);
            color = itemView.findViewById(R.id.txtmausac);
            number = itemView.findViewById(R.id.txtsoluong);
            imgsua = itemView.findViewById(R.id.imgiconsua);
            imgxoa = itemView.findViewById(R.id.imgiconxoa);
        }
    }
}
