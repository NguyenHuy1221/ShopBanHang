package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class KhuyenMaiAdapter extends RecyclerView.Adapter<KhuyenMaiAdapter.ViewHolder> {
    Context context;
    List<KhuyenMai> khuyenMaiList;

    private Click mClick;

    public interface Click{
        void onUpdateClick(KhuyenMai khuyenMai);
        void onDeleteClick(KhuyenMai khuyenMai);
    }

    public KhuyenMaiAdapter(Context context, List<KhuyenMai> khuyenMaiList, Click listener) {
        this.context = context;
        this.khuyenMaiList = khuyenMaiList;
        this.mClick =listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khuyen_mai,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KhuyenMai khuyenMai = khuyenMaiList.get(position);
        if(khuyenMai == null){
            return;
        }

        holder.txtTenKhuyenMai.setText(khuyenMai.getTenKhuyenMai());
        Picasso.get().load(khuyenMai.getImgKhuyenMai()).into(holder.imgKhuyenMai);
        holder.txtNgayBatDau.setText(khuyenMai.getNgayBatDau());
        holder.txtNgayKetThuc.setText(khuyenMai.getNgayKetThuc());
        holder.txtPhanTramKhuyenMai.setText(khuyenMai.getPhanTramKhuyenMai());

        holder.imgEditKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onUpdateClick(khuyenMai);
            }
        });

        holder.crdItemKM.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClick.onDeleteClick(khuyenMai);
                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        if(khuyenMaiList != null){
            return khuyenMaiList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgKhuyenMai,imgEditKM;
        TextView txtTenKhuyenMai,txtPhanTramKhuyenMai,txtNgayBatDau,txtNgayKetThuc;

        CardView crdItemKM;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crdItemKM = itemView.findViewById(R.id.crdItemKM);
            imgKhuyenMai = itemView.findViewById(R.id.img_khuyenMai);
            imgEditKM = itemView.findViewById(R.id.imgEditKM);
            txtTenKhuyenMai = itemView.findViewById(R.id.txtTenKhuyenMai);
            txtPhanTramKhuyenMai = itemView.findViewById(R.id.txtPhanTramKhuyenMai);
            txtNgayBatDau = itemView.findViewById(R.id.txtNgayBatDau);
            txtNgayKetThuc = itemView.findViewById(R.id.txtNgayKetThuc);
        }
    }
}
