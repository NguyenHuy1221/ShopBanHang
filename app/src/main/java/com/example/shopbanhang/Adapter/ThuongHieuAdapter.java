package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ThuongHieuAdapter extends RecyclerView.Adapter<ThuongHieuAdapter.ViewHolder> {

    private Context context;
    private List<ThuongHieu> mThuongHieu;
    private final IclickListener mIclickListener;

    public ThuongHieuAdapter(Context context, List<ThuongHieu> mThuongHieu, IclickListener mIclickListener) {
        this.context = context;
        this.mThuongHieu = mThuongHieu;
        this.mIclickListener = mIclickListener;
    }

    public void updateList(List<ThuongHieu> thuongHieuList) {
        this.mThuongHieu = thuongHieuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_thuong_hieu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ThuongHieu thuongHieu = mThuongHieu.get(position);
        holder.tenThuongHieu.setText(thuongHieu.getTenThuongHieu());
        Picasso.get().load(thuongHieu.getImageUrl()).into(holder.imgThuongHieu);



        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIclickListener.onClickUpdateItem(thuongHieu);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIclickListener.onClickDeleteItem(thuongHieu);
            }
        });
    }

    public interface IclickListener {
        void onClickUpdateItem(ThuongHieu thuongHieu);

        void onClickDeleteItem(ThuongHieu thuongHieu);
    }

    @Override
    public int getItemCount() {
        return mThuongHieu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tenThuongHieu;
        private ImageView edit, delete, imgThuongHieu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenThuongHieu = itemView.findViewById(R.id.tv_thuonghieu_edit);
            edit = itemView.findViewById(R.id.suaThuongHieu);
            delete = itemView.findViewById(R.id.xoaThuongHieu);
            imgThuongHieu = itemView.findViewById(R.id.imgThuongHieu);
        }
    }

}
