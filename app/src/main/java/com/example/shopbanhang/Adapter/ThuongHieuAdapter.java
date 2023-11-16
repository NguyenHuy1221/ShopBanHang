package com.example.shopbanhang.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.DAO.ThuongHieuDAO;
import com.example.shopbanhang.InterFace.ThayImage;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ThuongHieuAdapter extends RecyclerView.Adapter<ThuongHieuAdapter.ViewHolder> {

    private Context context;
    private List<ThuongHieu> mThuongHieu;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("thuonghieu");
    private static final int PICK_IMAGE_REQUEST = 123;
    private Uri selectedImageUri;
    private ThayImage thayImage;

    public ThuongHieuAdapter(ThayImage thayImage) {
        this.thayImage = thayImage;
    }

    public void updateList(List<ThuongHieu> thuongHieuList) {
        this.mThuongHieu = thuongHieuList;
        notifyDataSetChanged();
    }

    public ThuongHieuAdapter(Context context, List<ThuongHieu> mThuongHieu) {
        this.context = context;
        this.mThuongHieu = mThuongHieu;
    }

    @NonNull
    @Override
    public ThuongHieuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_thuong_hieu,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThuongHieuAdapter.ViewHolder holder, int position) {
        ThuongHieu thuongHieu = mThuongHieu.get(position);
        holder.tenThuongHieu.setText(thuongHieu.getTenThuongHieu());
        Picasso.get().load(thuongHieu.getImageUrl()).into(holder.imgThuongHieu);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBrand(thuongHieu);
            }
        });



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBrand(thuongHieu);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mThuongHieu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tenThuongHieu;
        private ImageView edit,delete,imgThuongHieu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tenThuongHieu = itemView.findViewById(R.id.tv_thuonghieu_edit);
            edit = itemView.findViewById(R.id.suaThuongHieu);
            delete = itemView.findViewById(R.id.xoaThuongHieu);
            imgThuongHieu = itemView.findViewById(R.id.imgThuongHieu);
        }
    }

    private void deleteBrand(ThuongHieu thuongHieu) {
        new AlertDialog.Builder(context)
                .setTitle("Xóa thương hiệu")
                .setMessage("Bạn có chắc muốn xóa thương hiệu này")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ThuongHieuDAO thuongHieuDAO = new ThuongHieuDAO();
                        thuongHieuDAO.deleteThuongHieu(thuongHieu);
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Không",null).show();
    }

    private void updateBrand(ThuongHieu thuongHieu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_thuong_hieu,null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        ImageView imgBrand = view.findViewById(R.id.imgBrand);
        TextView tenth = view.findViewById(R.id.update_th);
        EditText edtName = view.findViewById(R.id.edtBrandName);
        Button btnThem = view.findViewById(R.id.btnAddThuongHieu);

        tenth.setText("Sửa thương hiệu");
        btnThem.setText("Sửa");
        edtName.setText(thuongHieu.getTenThuongHieu());
        Picasso.get().load(thuongHieu.getImageUrl()).into(imgBrand);

        imgBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oppenFile();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String suaTen = edtName.getText().toString().trim().toUpperCase();
                if (!suaTen.equals("")){
                    thuongHieu.setTenThuongHieu(suaTen);
                }
                dialog.dismiss();
            }
        });

    }

    private void oppenFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity) context).startActivityForResult(intent, PICK_IMAGE_REQUEST);
        thayImage.clickImage(selectedImageUri);
    }

    // aor maf so co la


}
