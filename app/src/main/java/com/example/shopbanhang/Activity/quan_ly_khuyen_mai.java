package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.KhuyenMaiAdapter;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class quan_ly_khuyen_mai extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private FloatingActionButton floatAdd;
    private KhuyenMaiAdapter khuyenMaiAdapter;
    private RecyclerView recycle_qlkm;
    private ImageView backBtnKM, imgKM,imgLichBD,imgLichKT;
    private TextView txtNgayBatDauAddKM, txtNgayKetThucAddKM;
    private Uri uri;
    private List<KhuyenMai> list = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khuyen_mai);
        initView();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recycle_qlkm.setLayoutManager(gridLayoutManager);
        khuyenMaiAdapter = new KhuyenMaiAdapter(quan_ly_khuyen_mai.this, list, new KhuyenMaiAdapter.Click() {
            @Override
            public void onUpdateClick(KhuyenMai khuyenMai) {
                suaKhuyenMai(khuyenMai);
            }

            @Override
            public void onDeleteClick(KhuyenMai khuyenMai) {
                deleteKhuyenMai(khuyenMai);
            }
        });
        recycle_qlkm.setAdapter(khuyenMaiAdapter);

        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themKhuyenMai();
            }
        });

        getList();
    }




    private void themKhuyenMai() {
        AlertDialog.Builder builder = new AlertDialog.Builder(quan_ly_khuyen_mai.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_khuyen_mai,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        imgKM = view.findViewById(R.id.imgAddKM);
        imgLichBD = view.findViewById(R.id.imgLichBD);
        imgLichKT = view.findViewById(R.id.imgLichKT);

        imgLichBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayBD();
                openCalendar();

            }
        });

        imgLichKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDayKT();
                openCalendar();

            }
        });



        EditText txtNameAddKM = view.findViewById(R.id.txtNameAddKM);
        EditText txtPhanTramAddKM = view.findViewById(R.id.txtPhanTramAddKM);
        txtNgayBatDauAddKM = view.findViewById(R.id.txtNgayBatDauAddKM);
        txtNgayKetThucAddKM = view.findViewById(R.id.txtNgayKetThucAddKM);
        Button btnThemKM = view.findViewById(R.id.btnThemKM);

        imgKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        btnThemKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = UUID.randomUUID().toString();
                String tenKM = txtNameAddKM.getText().toString().trim();
                String phanTramKM = txtPhanTramAddKM.getText().toString().trim();
                String ngayBatDau = txtNgayBatDauAddKM.getText().toString();
                String ngayKetThuc = txtNgayKetThucAddKM.getText().toString().trim();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Date ngayBatDauKM =  simpleDateFormat.parse(txtNgayBatDauAddKM.getText().toString());
                    Date ngayKetThucKM = simpleDateFormat.parse(txtNgayKetThucAddKM.getText().toString());

                      if (ngayKetThucKM.compareTo(ngayBatDauKM) == 0){
                        Toast.makeText(quan_ly_khuyen_mai.this, "Ngày bắt đầu không được trùng với ngày kết thúc", Toast.LENGTH_SHORT).show();
                    } else if (ngayKetThucKM.compareTo(ngayBatDauKM) < 0) {
                        Toast.makeText(quan_ly_khuyen_mai.this, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu ", Toast.LENGTH_SHORT).show();
                    } else if (uri == null) {
                        Toast.makeText(quan_ly_khuyen_mai.this, "Chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                    }
                    else  {
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("KhuyenMai").child(uri.getLastPathSegment());
                        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete());
                                Uri uriImg = uriTask.getResult();
                                String img = uriImg.toString();
                                KhuyenMai khuyenMai = new KhuyenMai(id,img,tenKM,ngayBatDau,ngayKetThuc,phanTramKM);
                                pushData(khuyenMai);
                                dialog.dismiss();
                            }
                        });
                    }
                } catch (ParseException e) {
                      if (ngayBatDau.equals("")){
                        Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập ngày bắt đầu khuyến mãi", Toast.LENGTH_SHORT).show();
                    } else if (ngayKetThuc.equals("")){
                        Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập ngày kết thúc khuyến mãi", Toast.LENGTH_SHORT).show();
                    } else if (tenKM.equals("")){
                        Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập tên khuyến mãi", Toast.LENGTH_SHORT).show();
                    } else if (phanTramKM.equals("")){
                        Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập phần trăm khuyến mãi", Toast.LENGTH_SHORT).show();
                    }


                }

            }
        });

    }



    private void suaKhuyenMai(KhuyenMai khuyenMai) {
        AlertDialog.Builder builder = new AlertDialog.Builder(quan_ly_khuyen_mai.this);
        LayoutInflater inflater = ((Activity)this).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_them_khuyen_mai,null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        imgKM = view.findViewById(R.id.imgAddKM);
        TextView txtEditTenKM = view.findViewById(R.id.txtNameAddKM);
        TextView txtEditPhanTramKM = view.findViewById(R.id.txtPhanTramAddKM);
        TextView txtEditNgayBatDauKM = view.findViewById(R.id.txtNgayBatDauAddKM);
        TextView txtEditNgayKetThucKM = view.findViewById(R.id.txtNgayKetThucAddKM);
        Button btnThemKM = view.findViewById(R.id.btnThemKM);
        btnThemKM.setText("Sửa Khuyến Mãi");

        Picasso.get().load(khuyenMai.getImgKhuyenMai()).into(imgKM);
        txtEditTenKM.setText(khuyenMai.getTenKhuyenMai());
        txtEditPhanTramKM.setText(khuyenMai.getPhanTramKhuyenMai());
        txtEditNgayBatDauKM.setText(khuyenMai.getNgayBatDau());
        txtEditNgayKetThucKM.setText(khuyenMai.getNgayKetThuc());
        imgLichBD = view.findViewById(R.id.imgLichBD);
        imgLichKT = view.findViewById(R.id.imgLichKT);


        imgLichBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        txtEditNgayBatDauKM.setText(date);
                    }
                };
                openCalendar();

            }
        });

        imgLichKT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        txtEditNgayKetThucKM.setText(date);
                    }
                };
                openCalendar();

            }
        });

        imgKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFile();
            }
        });

        btnThemKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("KhuyenMai");
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://realtimedata-1e0aa.appspot.com");
                StorageReference storageRef = storage.getReference();
                StorageReference imageRef = storageRef.child(System.currentTimeMillis() + ".PNG");
                ProgressDialog progressDialog = ProgressDialog.show(quan_ly_khuyen_mai.this, "Chờ Chút", "Đang tải lên hình ảnh...", true);

                String tenKhuyenMaiMoi = txtEditTenKM.getText().toString().trim();
                String phanTramKhuyenMaiMoi = txtEditPhanTramKM.getText().toString().trim();
                String ngayBatDauKhuyenMaiMoi = txtEditNgayBatDauKM.getText().toString().trim();
                String ngayKetThucKhuyenMaiMoi = txtEditNgayKetThucKM.getText().toString().trim();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date ngayBatDauKM = simpleDateFormat.parse(ngayBatDauKhuyenMaiMoi);
                    Date ngayKetThucKM = simpleDateFormat.parse(ngayKetThucKhuyenMaiMoi);

//                    if (ngayKetThucKM.compareTo(ngayBatDauKM) == 0){
//                        Toast.makeText(quan_ly_khuyen_mai.this, "Ngày bắt đầu không được trùng với ngày kết thúc", Toast.LENGTH_SHORT).show();
//                    } else if (ngayKetThucKM.compareTo(ngayBatDauKM) < 0) {
//                        Toast.makeText(quan_ly_khuyen_mai.this, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu ", Toast.LENGTH_SHORT).show();
//                    } else if (tenKhuyenMaiMoi.equals("")){
//                        Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập tên khuyến mãi", Toast.LENGTH_SHORT).show();
//                    } else if (phanTramKhuyenMaiMoi.equals("")){
//                        Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập phần trăm khuyến mãi", Toast.LENGTH_SHORT).show();
                      if (uri == null) {
                        if (ngayKetThucKM.compareTo(ngayBatDauKM) == 0){
                            Toast.makeText(quan_ly_khuyen_mai.this, "Ngày bắt đầu không được trùng với ngày kết thúc", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (ngayKetThucKM.compareTo(ngayBatDauKM) < 0) {
                            Toast.makeText(quan_ly_khuyen_mai.this, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu ", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (tenKhuyenMaiMoi.equals("")){
                            Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập tên khuyến mãi", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (phanTramKhuyenMaiMoi.equals("")){
                            Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập phần trăm khuyến mãi", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else {
                            khuyenMai.setImgKhuyenMai(khuyenMai.getImgKhuyenMai());
                            khuyenMai.setTenKhuyenMai(tenKhuyenMaiMoi);
                            khuyenMai.setPhanTramKhuyenMai(phanTramKhuyenMaiMoi);
                            khuyenMai.setNgayBatDau(ngayBatDauKhuyenMaiMoi);
                            khuyenMai.setNgayKetThuc(ngayKetThucKhuyenMaiMoi);
                            databaseReference.child(khuyenMai.getIdKhuyenMai()).updateChildren(khuyenMai.toMap());
                            progressDialog.dismiss();
                            dialog.dismiss();
                            Toast.makeText(quan_ly_khuyen_mai.this, "Sửa thành công", Toast.LENGTH_SHORT).show();

                        }


                    } else {
                          if (ngayKetThucKM.compareTo(ngayBatDauKM) == 0){
                              Toast.makeText(quan_ly_khuyen_mai.this, "Ngày bắt đầu không được trùng với ngày kết thúc", Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                          } else if (ngayKetThucKM.compareTo(ngayBatDauKM) < 0) {
                              Toast.makeText(quan_ly_khuyen_mai.this, "Ngày kết thúc không được nhỏ hơn ngày bắt đầu ", Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                          } else if (tenKhuyenMaiMoi.equals("")){
                              Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập tên khuyến mãi", Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                          } else if (phanTramKhuyenMaiMoi.equals("")){
                              Toast.makeText(quan_ly_khuyen_mai.this, "Chưa nhập phần trăm khuyến mãi", Toast.LENGTH_SHORT).show();
                              progressDialog.dismiss();
                          }else {
                              imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                  @Override
                                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                      Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                      while (!uriTask.isComplete());
                                      Uri uriImg = uriTask.getResult();
                                      String img = uriImg.toString();
                                      khuyenMai.setImgKhuyenMai(img);
                                      khuyenMai.setTenKhuyenMai(tenKhuyenMaiMoi);
                                      khuyenMai.setPhanTramKhuyenMai(phanTramKhuyenMaiMoi);
                                      khuyenMai.setNgayBatDau(ngayBatDauKhuyenMaiMoi);
                                      khuyenMai.setNgayKetThuc(ngayKetThucKhuyenMaiMoi);
                                      databaseReference.child(khuyenMai.getIdKhuyenMai()).updateChildren(khuyenMai.toMap());
                                      progressDialog.dismiss();
                                      dialog.dismiss();
                                      Toast.makeText(quan_ly_khuyen_mai.this, "Sửa thành công", Toast.LENGTH_SHORT).show();


                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      progressDialog.dismiss();
                                      Toast.makeText(quan_ly_khuyen_mai.this, "ERROR", Toast.LENGTH_SHORT).show();
                                  }
                              });
                          }

                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);

                }
            }

        });

    }

    private void deleteKhuyenMai(KhuyenMai khuyenMai) {
        new AlertDialog.Builder(this)
                .setTitle("Shop Quần Áo")
                .setMessage("Xác Nhận Xóa ??")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("KhuyenMai");

                        myRef.child(String.valueOf(khuyenMai.getIdKhuyenMai())).removeValue();

                    }
                })

                .setNegativeButton("Cancel",null)
                .show();
    }
    private void pushData(KhuyenMai khuyenMai) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("KhuyenMai");

        String pathObj = khuyenMai.getIdKhuyenMai();
        databaseReference.child(pathObj).setValue(khuyenMai);

    }

    private void getList(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("KhuyenMai");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                if(khuyenMai != null){
                    list.add(khuyenMai);
                    khuyenMaiAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                if(khuyenMai == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i<list.size(); i++){
                    if(khuyenMai.getIdKhuyenMai() == list.get(i).getIdKhuyenMai()){
                        list.set(i,khuyenMai);
                        break;
                    }
                }
                    khuyenMaiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                KhuyenMai khuyenMai = snapshot.getValue(KhuyenMai.class);
                if(khuyenMai == null || list == null || list.isEmpty()){
                    return;
                }
                for (int i = 0; i<list.size(); i++){
                    if(khuyenMai.getIdKhuyenMai() == list.get(i).getIdKhuyenMai()){
                        list.remove(list.get(i));
                        break;
                    }
                }
                khuyenMaiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void openFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    // mở lịch
    private void openCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year =  calendar.get(Calendar.YEAR);
        int month =  calendar.get(Calendar.MONTH);
        int day =  calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(quan_ly_khuyen_mai.this,
                android.R.style.Theme_Holo_Dialog_MinWidth,onDateSetListener,year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void setDayBD(){
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                txtNgayBatDauAddKM.setText(date);
            }
        };
    }

    private void setDayKT(){
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                txtNgayKetThucAddKM.setText(date);
            }
        };
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            Picasso.get().load(uri).into(imgKM);
        }
    }

    private void initView() {
        floatAdd = findViewById(R.id.floatAdd);
        recycle_qlkm = findViewById(R.id.recycle_qlkm);
        backBtnKM = findViewById(R.id.backBtnKM);
        backBtnKM.setOnClickListener(v -> finish());
    }


}