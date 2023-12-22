package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Model.ChiTietGioHang;
import com.example.shopbanhang.Model.ChiTietHoaDon;
import com.example.shopbanhang.Model.ChiTietSanPhamfix;
import com.example.shopbanhang.Model.GioHang;
import com.example.shopbanhang.Model.HoaDon;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.example.shopbanhang.service.myAplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.RemoteMessage;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ThanhToanMainActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView txtTongTien,txtGmail,txtSdt;
    private EditText edtDiaChi;
    private Button btn_mua_hang;
    private HoaDon hoaDon;
    double tien;
    private ImageView img_dia_chi;
    private ArrayList<GioHang> gioHangList = new ArrayList<>(Gio_Hang.mlistGioHang);
    private ArrayList<ChiTietGioHang> chiTietGioHangArrayList = new ArrayList<>(Gio_Hang.mListChiTiet);
    private ArrayList<GioHang> list = new ArrayList<>();
    private ArrayList<SanPham> listsanpham = new ArrayList<>();

    private  List<TaiKhoan> mListTaiKhoan;

    private GioHang gioHang;
    private SanPham sanPham;

    private Context context = this ;
    private int idKhachHang;
    private static final String CHANNEL_ID = "com.example.shopbanhang.notification.channel";

    private double latitude = 10.7769;
    private double longitude = 106.7009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_main);

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        idKhachHang = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));
        Log.d("HUY","id kahcs hang : " + idKhachHang);




        anhxa();
        sendDataCart();
        getDataTaiKhoan(idKhachHang);

    }

    private void anhxa() {

        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
        int idtaikhoan = Integer.parseInt(mySharedPreferences.getValue("remember_id_tk"));

        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        DatabaseReference timkiemtaikhoan = mDatabaseReference.child(String.valueOf(idtaikhoan));
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        txtTongTien = findViewById(R.id.txtTongTien);
        txtGmail = findViewById(R.id.tvEmail);
        txtSdt = findViewById(R.id.tvSdt);
        img_dia_chi = findViewById(R.id.img_dia_chi);

        img_dia_chi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap();
            }
        });

        edtDiaChi = findViewById(R.id.edtdiaChi);
        btn_mua_hang = findViewById(R.id.btn_mua_hang);
        int trangthai = 0;
        getDataFirebasegiohang();
        btn_mua_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timkiemtaikhoan.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        TaiKhoan taiKhoan = snapshot.getValue(TaiKhoan.class);
                        if (taiKhoan.getSdttk().equals("none")){
                            Toast.makeText(ThanhToanMainActivity.this, "Bạn chưa nhập Thông tin Số điện thoại ở trang cá nhân", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (taiKhoan.getDiachitk().equals("none")){
                            Toast.makeText(ThanhToanMainActivity.this, "Bạn chưa nhập Thông tin địa chỉ ở trang cá nhân", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            String strDiaChi = edtDiaChi.getText().toString().trim();
                            Random random = new Random();
                            int id = random.nextInt(1000000);
                            if (TextUtils.isEmpty(strDiaChi)){
                                Toast.makeText(ThanhToanMainActivity.this, "Bạn chưa nhập địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
                            }else {
                                String DateToday = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                                String TimeToday = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                                hoaDon = new HoaDon(id,idKhachHang,DateToday,TimeToday,trangthai,0,strDiaChi,tien);
//                    getDataFirebasesanpham();
                                addHoaDon(hoaDon);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    private void getDataTaiKhoan(int idKhachHang) {
        DatabaseReference taiKhoanRef = FirebaseDatabase.getInstance().getReference("TaiKhoan");
        taiKhoanRef.child(String.valueOf(idKhachHang)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    TaiKhoan taiKhoan = dataSnapshot.getValue(TaiKhoan.class);
                    txtGmail.setText(taiKhoan.getEmailtk());
                    txtSdt.setText(taiKhoan.getSdttk());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private ArrayList<Integer> listmasp = new ArrayList<>();
    private ArrayList<Integer> listmaspnumber = new ArrayList<>();
    private void getDataFirebasegiohang() {
        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("giohang");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (listmasp != null) {
                    listmasp.clear();
                }
                if (listmaspnumber != null) {
                    listmaspnumber.clear();
                }
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

//                    DoanhThu doanhThu = dataSnapshot.getValue(DoanhThu.class);
                    gioHang = dataSnapshot.getValue(GioHang.class);
//                    if (gioHang != null){
//                        listmasp.add(gioHang.getMasp());
//                        listmaspnumber.add((gioHang.getSoluong()));
//                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    private void getDataFirebasesanpham() {
//        DatabaseReference mDatabaseReference;
//        mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");
//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (listsanpham != null) {
//                    listsanpham.clear();
//                }
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//
////                    DoanhThu doanhThu = dataSnapshot.getValue(DoanhThu.class);
//                    sanPham = dataSnapshot.getValue(SanPham.class);
//                    if (sanPham != null){
//                        Toast.makeText(ThanhToanMainActivity.this, ""+listmasp.size(), Toast.LENGTH_SHORT).show();
//
//                        if (listmasp != null){
//                            for (int i = 0; i < listmasp.size();i++){
//
//                                if (sanPham.getMasp() == listmasp.get(i)){
//                                    listsanpham.add(sanPham);
//                                    int soluongdaban = 0;
//                                    int soluongbancuagiohang;
//                                    int soluongdabanbandau;
//                                    soluongdabanbandau = sanPham.getSoluongban();
//                                    soluongbancuagiohang = listmaspnumber.get(i);
//                                    soluongdaban = soluongdabanbandau + soluongbancuagiohang;
//
//
//                                    String pathObj = String.valueOf(sanPham.getMasp());
//                                    Map<String, Object> updates = new HashMap<>();
//                                    updates.put("soluongban", soluongdaban);
//                                    mDatabaseReference.child(pathObj).updateChildren(updates);
//                                    soluongdaban = 0;
//
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void sendDataCart() {
        Intent intent = getIntent();
        tien = intent.getDoubleExtra("tongtien",0);
        txtTongTien.setText(formatTien(tien) + " đ");
    }

    private void addHoaDon(HoaDon hoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hoaDonRef = database.getReference("hoadon");
        String TimeToday = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        hoaDonRef.child(String.valueOf(hoaDon.getMaHD())).setValue(hoaDon)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            addChiTietHoaDon(hoaDon.getMaHD());
                            clearGioHangData(idKhachHang);
                            Toast.makeText(ThanhToanMainActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            sendNotification();
                        } else {
                            Toast.makeText(ThanhToanMainActivity.this, "Lỗi khi thêm hóa đơn", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void addChiTietHoaDon(int idHoaDon) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference chiTietHoaDonRef = database.getReference("chitiethoadon");

        DatabaseReference mDatabaseReference;
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Chitietsanpham");
        DatabaseReference DatabaseReferencesanpham;
        DatabaseReferencesanpham = FirebaseDatabase.getInstance().getReference("sanpham");
        for (ChiTietGioHang chiTietGioHang : chiTietGioHangArrayList) {
            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setId_chi_tiet_hoa_don(idHoaDon);
            chiTietHoaDon.setHoa_don(idHoaDon);
            chiTietHoaDon.setId_chi_tiet_san_pham(chiTietGioHang.getId_chi_tiet_san_pham());
            chiTietHoaDon.setSo_luong(chiTietGioHang.getSo_luong());
            chiTietHoaDon.setDon_gia(chiTietGioHang.getDon_gia());

            int pathObj = chiTietGioHang.getId_chi_tiet_san_pham();
            Map<String, Object> updates = new HashMap<>();


            DatabaseReference chitietsanpham = mDatabaseReference.child(String.valueOf(chiTietGioHang.getId_chi_tiet_san_pham()));
            chitietsanpham.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ChiTietSanPhamfix chiTietSanPhamfix = snapshot.getValue(ChiTietSanPhamfix.class);
                        updates.put("soluong", chiTietSanPhamfix.getSoluong()-chiTietGioHang.getSo_luong());
                        mDatabaseReference.child(String.valueOf(pathObj)).updateChildren(updates);
                        DatabaseReference timkiemsanpham = DatabaseReferencesanpham.child(String.valueOf(chiTietSanPhamfix.getMasp()));
                        timkiemsanpham.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    SanPham sanPham1 = snapshot.getValue(SanPham.class);


                                    int pathObj2 = sanPham1.getMasp();
                                    Map<String, Object> updates2 = new HashMap<>();
                                    updates2.put("soluongban", sanPham1.getSoluongban() + chiTietGioHang.getSo_luong());
                                    DatabaseReferencesanpham.child(String.valueOf(pathObj2)).updateChildren(updates2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Random random = new Random();
            int idChiTietHoaDon = random.nextInt(100000);
            chiTietHoaDonRef.child(String.valueOf(idChiTietHoaDon)).setValue(chiTietHoaDon);
        }
    }

    private void clearGioHangData(int idKhachHang) {
        DatabaseReference gioHangRef = FirebaseDatabase.getInstance().getReference("giohang");

        Query query = gioHangRef.orderByChild("id_khach_hang").equalTo(idKhachHang);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    GioHang gioHang = childSnapshot.getValue(GioHang.class);
                    childSnapshot.getRef().removeValue();
                    clearChiTietGioHangData(gioHang.getId_gio_hang());
                }
                gioHangList.clear();
                startActivity(new Intent(ThanhToanMainActivity.this, DatHangThanhCongActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void clearChiTietGioHangData(int idGioHang) {
        DatabaseReference chiTietGioHangRef = FirebaseDatabase.getInstance().getReference("chitietgiohang");

        Query query = chiTietGioHangRef.orderByChild("id_gio_hang").equalTo(idGioHang);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    childSnapshot.getRef().removeValue();
                }
                chiTietGioHangArrayList.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private String formatTien(double value) {
        NumberFormat formatter = new DecimalFormat("#,###");
        return formatter.format(value) + " đ";
    }

    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.h1);

        if (bitmap != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, myAplication.CHANNEL_ID)
                    .setSmallIcon(R.drawable.h1)
                    .setContentTitle("CẢM ƠN BẠN ĐÃ QUAN TÂM VÀ ỦNG HỘ")
                    .setContentText("THANK KIU FOR LISTENING")
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null)
                    )
                    .setLargeIcon(bitmap)
                    .setColor(Color.RED)
                    .setAutoCancel(true);

            Intent resultIntent = new Intent(context, LichSuHoaDonMainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    resultIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            builder.setContentIntent(resultPendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

            // Kiểm tra quyền gửi thông báo
            if (notificationManagerCompat.areNotificationsEnabled()) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // Yêu cầu quyền gửi thông báo từ người dùng
                    ActivityCompat.requestPermissions((Activity) context,
                            new String[]{Manifest.permission.POST_NOTIFICATIONS}, 7979);
                } else {
                    notificationManagerCompat.notify(getNotificationID(), builder.build());
                }
            } else {
                // Yêu cầu quyền thông báo
                // Hãy thực hiện yêu cầu quyền thông qua hộp thoại hoặc cài đặt ứng dụng
            }
        } else {
            // Xử lý khi bitmap là null
        }
    }

    private int getNotificationID() {
        return (int) new Date().getTime();
    }


    private void openMap() {
        Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + Uri.encode("geo:" + latitude + "," + longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            Toast.makeText(this, "Ứng dụng Bản đồ không khả dụng.", Toast.LENGTH_SHORT).show();
        }
    }
}