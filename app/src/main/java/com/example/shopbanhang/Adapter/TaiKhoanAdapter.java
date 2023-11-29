package com.example.shopbanhang.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Activity.QuanLyTaiKhoanActivity;

import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

public class TaiKhoanAdapter extends RecyclerView.Adapter<TaiKhoanAdapter.ViewHolder> {
    Context context;
    List<TaiKhoan> TaiKhoanList;
    private TaiKhoanAdapter.Click mClick;
    public interface Click{
        void onUpdateClick(TaiKhoan taiKhoan);
        void onDeleteClick(TaiKhoan taiKhoan);
    }
//    public TaiKhoanAdapter(Context context, List<TaiKhoan> list, Click click) {
//    }



    public TaiKhoanAdapter(Context context, List<TaiKhoan> TaiKhoanList, TaiKhoanAdapter.Click listener) {
        this.context = context;
        this.TaiKhoanList = TaiKhoanList;
        this.mClick =listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tai_khoan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaiKhoan taiKhoan = TaiKhoanList.get(position);
        if(taiKhoan == null){
            return;
        }
        holder.txtmk.setText(taiKhoan.getMatkhautk());
        holder.txtid.setText(taiKhoan.getIdtk());
        holder.txtemail.setText(taiKhoan.getEmailtk());
        holder.txttentk.setText(taiKhoan.getTentk());
        holder.txtdctk.setText(taiKhoan.getDiachitk());
        holder.txtsdttk.setText(taiKhoan.getSdttk());
        holder.txtnttk.setText(taiKhoan.getNgaytaotk());
        holder.txttttk.setText(taiKhoan.getTinhtragtk());
        Picasso.get().load(taiKhoan.getImgtk()).into(holder.imgtkh);
        holder.btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClick.onUpdateClick(taiKhoan);
            }
        });
        holder.crdItemtk.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClick.onDeleteClick(taiKhoan);
                return false;
            }
        });
        holder.crdItemtk.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClick.onDeleteClick(taiKhoan);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(TaiKhoanList != null ){
            return TaiKhoanList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgtkh ;
        Button btnsua;
        TextView txtemail,txtmk,txtid,txttentk,txtdctk,txtsdttk,txtnttk,txttttk;
        CardView crdItemtk;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crdItemtk = itemView.findViewById(R.id.crdItemtk);
            txtid = itemView.findViewById(R.id.txtid);
            txtmk=itemView.findViewById(R.id.txtmk);
            imgtkh = itemView.findViewById(R.id.imgtk);
            txtemail = itemView.findViewById(R.id.txtemail);
            txttentk = itemView.findViewById(R.id.txttentk);
            txtdctk = itemView.findViewById(R.id.txtdctk);
            txtsdttk = itemView.findViewById(R.id.txtsdttk);
            txtnttk = itemView.findViewById(R.id.txtnttk);
            txttttk = itemView.findViewById(R.id.txttttk);
            btnsua=itemView.findViewById(R.id.btnsua);
        }
    }
}
