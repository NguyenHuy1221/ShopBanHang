package com.example.shopbanhang.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MauSizeAdapter extends RecyclerView.Adapter<MauSizeAdapter.ViewHolder> {

    private Context context;
    private List<ChiTietSanPham> mListSanPham;
    private int selectedItemPosition = RecyclerView.NO_POSITION;

    public interface OnItemClickListener {
        void onItemClick(String size, String mau,int idChiTietSanPham, int soLuongTrongKho);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public MauSizeAdapter(Context context, List<ChiTietSanPham> mListSanPham) {
        this.context = context;
        this.mListSanPham = mListSanPham;
    }

    @NonNull
    @Override
    public MauSizeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_size,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MauSizeAdapter.ViewHolder holder, int position) {
        ChiTietSanPham chiTietSanPham = mListSanPham.get(position);
        holder.txtSize.setText("size : "+chiTietSanPham.getKichco());
        holder.txtMau.setText("màu : "+chiTietSanPham.getMau());


        // Kiểm tra xem mục hiện tại có được chọn không
        if (position == selectedItemPosition) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.red));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(
                            chiTietSanPham.getKichco(),
                            chiTietSanPham.getMau(),
                            Integer.parseInt(chiTietSanPham.getIdchitietsanpham()),
                            chiTietSanPham.getSoluong());

                    // Cập nhật vị trí mục đã chọn
                    int previousSelectedItem = selectedItemPosition;
                    selectedItemPosition = holder.getAdapterPosition();

                    // Thông báo thay đổi cho mục trước đó và mục hiện tại
                    notifyItemChanged(previousSelectedItem);
                    notifyItemChanged(selectedItemPosition);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mListSanPham.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSize,txtMau;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSize = itemView.findViewById(R.id.size_click);
            txtMau = itemView.findViewById(R.id.mau_click);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
