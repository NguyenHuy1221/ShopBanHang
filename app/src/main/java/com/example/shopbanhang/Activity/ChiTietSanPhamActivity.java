package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.HienThiChiTietMain;
import com.example.shopbanhang.Adapter.MauSizeAdapter;
import com.example.shopbanhang.Model.ChiTietGioHang;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChiTietSanPhamActivity extends AppCompatActivity {

    private ImageView imgPic, imgBack, imgCart;
    private TextView txtName, txtPrice, txtTitle;
    private Button btnAdd;
    private Context context = this;
    private RecyclerView recyclerView;
    private String color = null;
    private String size = null;
    private List<ChiTietSanPham> chiTietSanPhamList;
    private List<GioHang> mListGioHang;
    private List<ChiTietGioHang> mListChiTiet;
    private int so = 1;
    BottomSheetDialog dialog;
    NotificationBadge badge;

    RecyclerView rcyMau;
    RecyclerView rcySize;

    private int idChiTietSanPham = 0;
    private int soLuongTrongKho = 0;

    private int user = 0;
    TextView soLuongKho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);



        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        user = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));

        anhXa();
        getIntentSanPham();
        hienThiSoLuong();
    }

    private void anhXa() {
        imgPic = findViewById(R.id.itemPic);
        imgCart = findViewById(R.id.imageView4);
        txtName = findViewById(R.id.titleTxt);
        txtPrice = findViewById(R.id.priceTxt);
        txtTitle = findViewById(R.id.moTaTxt);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imgBack = findViewById(R.id.backBtn);
        imgBack.setOnClickListener(v -> finish());
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> addToCart());
        badge = findViewById(R.id.menu_sl);

        imgCart.setOnClickListener(v -> {
            Intent intent = new Intent(ChiTietSanPhamActivity.this, Gio_Hang.class);
            startActivity(intent);
        });
    }

    public void getIntentSanPham() {
        Intent intent = getIntent();
        String tenSP = intent.getStringExtra("tensp");
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String ghiChu = intent.getStringExtra("ghichu");
        String imageUrl = intent.getStringExtra("imageUrl");

        if (intent.hasExtra("urlChiTiet")) {
            List<String> anhChiTiet = getIntent().getStringArrayListExtra("urlChiTiet");
            if (anhChiTiet != null && !anhChiTiet.isEmpty()) {
                HienThiChiTietMain adapter = new HienThiChiTietMain(context, anhChiTiet, new HienThiChiTietMain.OnItemClickListener() {
                    @Override
                    public void onItemClick(String imageUrl) {
                        Picasso.get().load(imageUrl).into(imgPic);
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        }

        txtName.setText(tenSP);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTien = decimalFormat.format(giaban);
        txtPrice.setText(formattedTien + " đ ");
        txtTitle.setText(ghiChu);
        Picasso.get().load(imageUrl).into(imgPic);
    }

    public void addToCart() {
        View dialogSheetView = LayoutInflater.from(context).inflate(R.layout.dialog_add_to_cart, null);
        dialog = new BottomSheetDialog(context);
        dialog.setContentView(dialogSheetView);
        dialog.show();

        ImageView imgPic = dialogSheetView.findViewById(R.id.itemPicCt);
        TextView txtTien = dialogSheetView.findViewById(R.id.txtMoney);
        TextView txtCong = dialogSheetView.findViewById(R.id.tvCong);
        TextView txtSo = dialogSheetView.findViewById(R.id.tvSo);
        TextView txtTru = dialogSheetView.findViewById(R.id.tvtru);
        Button btnAdd = dialogSheetView.findViewById(R.id.btnAddCt);

        rcyMau = dialogSheetView.findViewById(R.id.rcy_mau);
        hienThiSizeMau();

        Intent intent = getIntent();
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String imageUrl = intent.getStringExtra("imageUrl");

        soLuongKho = dialogSheetView.findViewById(R.id.soLuongKho);
        int slKho = intent.getIntExtra("soluongnhap", 0);
        soLuongKho.setText(Integer.toString(slKho));

        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String formattedTien = decimalFormat.format(giaban);
        txtTien.setText(formattedTien + " đ ");

        Picasso.get().load(imageUrl).into(imgPic);

        txtTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (so > 1) {
                    so--;
                    txtSo.setText(String.valueOf(so));
                }
            }
        });

        txtCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                so++;
                txtSo.setText(String.valueOf(so));
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == 0) {
                    Intent loginIntent = new Intent(ChiTietSanPhamActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    Toast.makeText(context, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                    return;
                }
                addProductToCart();
            }
        });
    }

    private void addProductToCart() {
        Intent intent = getIntent();
        int maSP = intent.getIntExtra("masp", 0);
        String tenSP = intent.getStringExtra("tensp");
        double giaban = intent.getDoubleExtra("giaban", 0.0);
        String imageUrl = intent.getStringExtra("imageUrl");

        double tongTien = giaban * so;

        if (color == null || size == null) {
            Toast.makeText(context, "Chưa chọn màu hoặc size", Toast.LENGTH_SHORT).show();
            return;
        }

        if (so > soLuongTrongKho) {
            Toast.makeText(context, "Số lượng sản phẩm trong kho không đủ", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference gioHangRef = FirebaseDatabase.getInstance().getReference("giohang").child(String.valueOf(user));
        gioHangRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Người dùng đã có giỏ hàng, lấy ID giỏ hàng
                    int idGioHang = dataSnapshot.child("id_gio_hang").getValue(Integer.class);
                    addChiTietGioHang(idGioHang, giaban, tongTien);
                } else {
                    // Người dùng chưa có giỏ hàng, tạo giỏ hàng mới
                    createNewCart(giaban, tongTien);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });

    }

    private void createNewCart(double giaban, double tongTien) {
        DatabaseReference gioHangRef = FirebaseDatabase.getInstance().getReference("giohang");
        Random random = new Random();
        int idGioHang = random.nextInt(1000);

        GioHang gioHang = new GioHang(idGioHang, user);
        gioHangRef.child(String.valueOf(user)).setValue(gioHang);

        addChiTietGioHang(idGioHang, giaban, tongTien);
    }

    private void addChiTietGioHang(int idGioHang, double giaban, double tongTien) {
        DatabaseReference chiTietGioHangRef = FirebaseDatabase.getInstance().getReference("chitietgiohang");
        chiTietGioHangRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean productExistsInCart = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChiTietGioHang chiTietGioHang = dataSnapshot.getValue(ChiTietGioHang.class);
                    if (chiTietGioHang != null &&
                            chiTietGioHang.getId_gio_hang() == idGioHang &&
                            chiTietGioHang.getId_chi_tiet_san_pham() == idChiTietSanPham) {
                        // Người dùng mua đúng sản phẩm, tăng số lượng
                        int newSoLuong = chiTietGioHang.getSo_luong() + so;
                        updateChiTietGioHang(dataSnapshot.getKey(), newSoLuong);
                        productExistsInCart = true;
                        break;
                    }
                }

                // Nếu sản phẩm chưa có trong giỏ hàng, tạo chi tiết giỏ hàng mới
                if (!productExistsInCart) {
                    createNewChiTietGioHang(idGioHang, giaban, tongTien);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void createNewChiTietGioHang(int idGioHang, double giaban, double tongTien) {
        DatabaseReference chiTietGioHangRef = FirebaseDatabase.getInstance().getReference("chitietgiohang");
        Random random = new Random();
        int idChiTietGioHang = random.nextInt(1000);

        ChiTietGioHang chiTietGioHang = new ChiTietGioHang(idChiTietGioHang, idGioHang, idChiTietSanPham, so, giaban, tongTien);
        chiTietGioHangRef.child(String.valueOf(idChiTietGioHang)).setValue(chiTietGioHang);

        Toast.makeText(context, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        hienThiSoLuong();
    }



//    private void updateChiTietGioHang(String chiTietGioHangKey, int newSoLuong) {
//        DatabaseReference chiTietGioHangRef = FirebaseDatabase.getInstance().getReference("chitietgiohang");
//        chiTietGioHangRef.child(chiTietGioHangKey).child("so_luong").setValue(newSoLuong);
//
//        Toast.makeText(context, "Số lượng sản phẩm đã được cập nhật trong giỏ hàng", Toast.LENGTH_SHORT).show();
//        dialog.dismiss();
//    }

    private void updateChiTietGioHang(String chiTietGioHangKey, int newSoLuong) {
        DatabaseReference chiTietGioHangRef = FirebaseDatabase.getInstance().getReference("chitietgiohang");

        chiTietGioHangRef.child(chiTietGioHangKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChiTietGioHang chiTietGioHang = dataSnapshot.getValue(ChiTietGioHang.class);

                if (chiTietGioHang != null) {
                    chiTietGioHangRef.child(chiTietGioHangKey).child("so_luong").setValue(newSoLuong);

                    double donGia = chiTietGioHang.getDon_gia();
                    double tongTien = donGia * newSoLuong;

                    chiTietGioHangRef.child(chiTietGioHangKey).child("tong_tien").setValue(tongTien);

                    Toast.makeText(context, "Số lượng sản phẩm và tổng tiền đã được cập nhật trong giỏ hàng", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }



    private void hienThiSizeMau() {
        chiTietSanPhamList = new ArrayList<>();
        Intent intent = getIntent();
        int maSP = intent.getIntExtra("masp", 0);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Chitietsanpham");

        myRef.orderByChild("masp").equalTo(maSP).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ChiTietSanPham chiTietSanPham = dataSnapshot.getValue(ChiTietSanPham.class);

                        if (chiTietSanPham != null) {
                            chiTietSanPhamList.add(chiTietSanPham);
                        }
                    }

                    MauSizeAdapter mauSizeAdapter = new MauSizeAdapter(context, chiTietSanPhamList);
                    rcyMau.setLayoutManager(new LinearLayoutManager(context));
                    rcyMau.setAdapter(mauSizeAdapter);

                    mauSizeAdapter.setOnItemClickListener(new MauSizeAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(String size, String mau, int idChiTietSanPham, int soLuongTrongKho) {
                            color = mau;
                            ChiTietSanPhamActivity.this.size = size;
                            ChiTietSanPhamActivity.this.idChiTietSanPham = idChiTietSanPham;
                            ChiTietSanPhamActivity.this.soLuongTrongKho = soLuongTrongKho;

                            soLuongKho.setText(String.valueOf(soLuongTrongKho));
                            Log.d("HUY", "Size and color:" + color + size + ", idChiTietSanPham: " + idChiTietSanPham + " số lượng :" + soLuongTrongKho);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void hienThiSoLuong() {

        mListGioHang = new ArrayList<>();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("giohang");
        myRef.orderByChild("id_khach_hang").equalTo(user).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListGioHang.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GioHang gioHang = dataSnapshot.getValue(GioHang.class);
                    if (mListGioHang != null) {
                        mListGioHang.add(gioHang);
                        hienThiSoLuong(gioHang.getId_gio_hang());
                    }
                }

                if (badge != null) {
                    badge.setText(String.valueOf(mListGioHang.size()));
                }

                Log.d("HUY" ,"dữ lệu" + mListGioHang.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void hienThiSoLuong(int idGioHang) {
        mListChiTiet = new ArrayList<>();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("chitietgiohang");
        myRef.orderByChild("id_gio_hang").equalTo(idGioHang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mListChiTiet.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChiTietGioHang chiTietGioHang = dataSnapshot.getValue(ChiTietGioHang.class);
                    if (mListChiTiet != null) {
                        mListChiTiet.add(chiTietGioHang);
                    }
                }

                if (badge != null) {
                    badge.setText(String.valueOf(mListChiTiet.size()));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu cần
            }
        });
    }


}

