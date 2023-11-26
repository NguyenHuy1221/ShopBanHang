package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {

    private Context mContext;
    private List<SanPham> mSanPham;
    private final OnClickItem onClickItem;

    public SanPhamAdapter(Context mContext, List<SanPham> mSanPham,OnClickItem onClickItem) {
        this.mContext = mContext;
        this.mSanPham = mSanPham;
        this.onClickItem = onClickItem;
    }

    public void updateList(List<SanPham> sanPhamList){
        this.mSanPham = sanPhamList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SanPhamAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_san_pham,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamAdapter.ViewHolder holder, int position) {
        SanPham sanPham = mSanPham.get(position);
        Picasso.get().load(sanPham.getImageUrl()).into(holder.imgSanPham);
        holder.tensp.setText(sanPham.getTensp());
        holder.tenth.setText(sanPham.getThuonghieu());
        holder.giasp.setText(sanPham.getGiaban() +" $");

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onclikUpdate(sanPham);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItem.onclickDelete(sanPham);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSanPham.size();
    }

    public interface OnClickItem {

        void onclikUpdate(SanPham sanPham);

        void onclickDelete(SanPham sanPham);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tensp,tenth,giasp;
        private ImageView edit, delete, imgSanPham;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.tv_ten);
            tenth = itemView.findViewById(R.id.tv_thuonghieu);
            giasp = itemView.findViewById(R.id.tv_gia);
            edit = itemView.findViewById(R.id.suaThuongHieu);
            delete = itemView.findViewById(R.id.xoaThuongHieu);
            imgSanPham = itemView.findViewById(R.id.imgSanPham);
        }
    }
}
