package com.example.shopbanhang.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;

import java.util.List;
import java.util.zip.Inflater;

public class TaiKhoanAdapter extends RecyclerView.Adapter<TaiKhoanAdapter.ViewHolder> {
    Context context;
    List<TaiKhoan> TaiKhoanList;

    public TaiKhoanAdapter(Context context, List<TaiKhoan> TaiKhoanList) {
        this.context = context;
        this.TaiKhoanList = TaiKhoanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tai_khoan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaiKhoan TaiKhoan = TaiKhoanList.get(position);
        if(TaiKhoan == null){
            return;
        }
        holder.txtid.setText(TaiKhoan.getIdtk());
        holder.txtemail.setText(TaiKhoan.getEmailtk());
        holder.txtmk.setText(TaiKhoan.getMatkhautk());

    }

    @Override
    public int getItemCount() {
        if(TaiKhoanList.size() > 0){
            return TaiKhoanList.size();
        }

        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgtkh ;
        TextView txtemail,txtmk,txtid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.txtid);
            imgtkh = itemView.findViewById(R.id.imgtk);
            txtemail = itemView.findViewById(R.id.txtemail);
            txtmk = itemView.findViewById(R.id.txtmk);


        }
    }
}
