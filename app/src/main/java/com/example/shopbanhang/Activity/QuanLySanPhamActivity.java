package com.example.shopbanhang.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.ImageChiTietAdapter;
import com.example.shopbanhang.Adapter.SanPhamAdapter;
import com.example.shopbanhang.DAO.ChiTietSanPhamDAO;
import com.example.shopbanhang.DAO.SanPhamDAO;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class QuanLySanPhamActivity extends AppCompatActivity {

    private List<SanPham> sanPhams = new ArrayList<>();
    private final List<String> spinerThuongHieu = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private Context context = this;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUESTS = 2;
    private Uri mImageUri;
    private ImageView img_chinh;

    private SanPhamAdapter sanPhamAdapter;
    private RecyclerView recyclerView;
    private DatabaseReference mDatabaseReference;

    private ImageChiTietAdapter imageChiTietAdapter;
    List<ChiTietSanPham> chiTietSanPhamList = new ArrayList<>();

    int uploadCount = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_san_pham);

        floatingActionButton = findViewById(R.id.fabAdd);
        recyclerView = findViewById(R.id.rcy_qlsp);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

//        getDataSpinner();
        getDataFirebase();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProducts();
            }
        });
    }

    private void getDataFirebase() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham mSanPham = dataSnapshot.getValue(SanPham.class);
                    sanPhams.add(mSanPham);
                }
                    sanPhamAdapter = new SanPhamAdapter(context, sanPhams, new SanPhamAdapter.OnClickItem() {
                        @Override
                        public void onclikUpdate(SanPham sanPham) {
                            EditProducts(sanPham);
                        }

                        @Override
                        public void onclickDelete(SanPham sanPham) {
                            DeleteProducts(sanPham);
                        }
                    });
                    recyclerView.setAdapter(sanPhamAdapter);
                    sanPhamAdapter.updateList(sanPhams);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DeleteProducts(SanPham sanPham) {
    }

    private void EditProducts(SanPham sanPham) {

    }


    private void addProducts() {

        Intent intent = new Intent(QuanLySanPhamActivity.this, ThemSanPhamActivity.class);
        startActivity(intent);

//        BottomSheetDialog dialog;
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_them_san_pham, null);
//        dialog = new BottomSheetDialog(context);
//        dialog.setContentView(view);
//        dialog.show();

//        img_chinh = view.findViewById(R.id.image_chinh);
//        ImageView img_ct = view.findViewById(R.id.image_ct);
//        EditText edt_masp = view.findViewById(R.id.edt_ma_sp);
//        EditText edt_tensp = view.findViewById(R.id.edt_ten_sp);
//        EditText edt_so_luong_sp = view.findViewById(R.id.edt_so_luong);
//        EditText edt_gia_nhap = view.findViewById(R.id.edt_gia_sp);
//        EditText edt_gia_ban = view.findViewById(R.id.edt_gia_ban_sp);
//        Spinner spn_loaisp = view.findViewById(R.id.spn_loai_sp);
//        Spinner spn_mausp = view.findViewById(R.id.spn_mau_sp);
//        Spinner spn_sizesp = view.findViewById(R.id.spn_size_sp);
//        Spinner spn_trang_thai = view.findViewById(R.id.spn_trang_thai_sp);
//        EditText edt_ghi_chu = view.findViewById(R.id.edt_ghi_chu_sp);
//        Button btnAdd = view.findViewById(R.id.btn_them_sp);
//
//        RecyclerView rcy_ct_anh = view.findViewById(R.id.rcy_chi_tiet_anh);
//        rcy_ct_anh.setLayoutManager(new GridLayoutManager(context,3));
//        imageChiTietAdapter = new ImageChiTietAdapter(selectedImages);
//        rcy_ct_anh.setAdapter(imageChiTietAdapter);
//        img_ct.setOnClickListener(v -> { oppenFile();});
//
//        img_chinh.setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(intent,PICK_IMAGE_REQUESTS);
//        });



        //setAdapter spienr thuong hieu
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, spinerThuongHieu);
//        spn_loaisp.setAdapter(arrayAdapter);
//
//        List<String> size = new ArrayList<>();
//        size.add("M");
//        size.add("L");
//        size.add("Xl");
//        size.add("XXL");
//        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, size);
//        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_sizesp.setAdapter(sizeAdapter);
//
//        List<String> color = new ArrayList<>();
//        color.add("Trắng");
//        color.add("Vàng");
//        color.add("Xanh");
//        color.add("Đen");
//        color.add("Đỏ");
//        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, color);
//        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_mausp.setAdapter(colorAdapter);
//
//        List<String> trangthai = new ArrayList<>();
//        trangthai.add("Hoạt Động");
//
//        ArrayAdapter<String> trangThaiAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, trangthai);
//        trangThaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spn_trang_thai.setAdapter(trangThaiAdapter);
//
//
//
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String masp = edt_masp.getText().toString().trim();
//                String tensp = edt_tensp.getText().toString().trim();
//                String solgspString  = edt_so_luong_sp.getText().toString().trim();
//                String gianhap = (edt_gia_nhap.getText().toString().trim());
//                String giaban = (edt_gia_ban.getText().toString().trim());
//                String loaisp = spn_loaisp.getSelectedItem().toString();
//                String mausp = spn_mausp.getSelectedItem().toString();
//                String sizesp = spn_sizesp.getSelectedItem().toString();
//                String trangthaisp = spn_trang_thai.getSelectedItem().toString();
//                String ghichu = edt_ghi_chu.getText().toString().trim();
//
//                if (masp.isEmpty() || tensp.isEmpty() || solgspString.isEmpty() || gianhap.isEmpty() || giaban.isEmpty() ||
//                        loaisp == null || mausp == null || sizesp == null || trangthaisp == null) {
//                    Toast.makeText(context, "Chưa nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
//                }else {
//                    if (mImageUri == null){
//                        Toast.makeText(context, "Chưa chọn hình", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    for (SanPham check : sanPhams) {
//                        if (masp.equals(String.valueOf(check.getMasp()))){
//                            Toast.makeText(context, "Mã sản phẩm trùng nhau - nhập mã mới", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                    }
//                    int solgsp;
//                    solgsp = Integer.parseInt(solgspString);
//                    uploadSanPhamToFirebase(mImageUri, Integer.parseInt(masp), tensp, solgsp, 0, Double.parseDouble(gianhap), Double.parseDouble(giaban), loaisp, mausp, sizesp, trangthaisp, ghichu,selectedImages);
//
//                }
//
//            }
//        });

    }

//    private void oppenFile() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
//        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
//                if (data.getClipData() != null) {
//                    // Chọn nhiều ảnh
//                    int count = data.getClipData().getItemCount();
//                    for (int i = 0; i < count; i++) {
//                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                        selectedImages.add(imageUri);
//                    }
//                } else if (data.getData() != null) {
//                   // Chọn một ảnh
//                    Uri imageUri = data.getData();
//                    selectedImages.add(imageUri);
//                }
//                imageChiTietAdapter.notifyDataSetChanged();
//            }
//
//            if (requestCode == PICK_IMAGE_REQUESTS && resultCode == RESULT_OK && data != null) {
//                mImageUri = data.getData();
//                Picasso.get().load(mImageUri).into(img_chinh);
//            }
//
//        }

//
//    // laays du lieu thuong hieu vao spiner
//    private void getDataSpinner() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("thuonghieu");
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if (spinerThuongHieu != null) {
//                    spinerThuongHieu.clear();
//                }
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ThuongHieu thuongHieu = dataSnapshot.getValue(ThuongHieu.class);
//                    spinerThuongHieu.add(thuongHieu.getTenThuongHieu());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void uploadSanPhamToFirebase(Uri mImageUri, int masp, String tensp, int solgsp, int solgban, double gianhap, double giaban, String thuonghieu, String mau, String size, String trangthai, String ghichu, List<Uri> uriList) {
//        FirebaseStorage storage = FirebaseStorage.getInstance("gs://realtimedata-1e0aa.appspot.com");
//        StorageReference storageRef = storage.getReference();
//        StorageReference imageRef = storageRef.child(System.currentTimeMillis() + ".PNG");
//        ProgressDialog progressDialog = ProgressDialog.show(context, "Chờ Chút", "Đang tải sản phẩm...", true);
//
//        // Tải lên hình chính
//        imageRef.putFile(mImageUri)
//                .continueWithTask(taskSnapshot -> imageRef.getDownloadUrl())
//                .addOnSuccessListener(uri -> {
//
//                    // Sử dụng CountDownLatch để đồng bộ việc tải lên nhiều hình ảnh
//                    CountDownLatch latch = new CountDownLatch(uriList.size());
//                    // Tạo danh sách URL của các hình chi tiết
//                    List<String> urlList = new ArrayList<>();
//                    // Tải lên từng hình chi tiết và lấy URL
//                    for (Uri uriChiTiet : uriList) {
//                        StorageReference chiTietRef = storageRef.child(System.currentTimeMillis() + "_CT.PNG");
//
//                        chiTietRef.putFile(uriChiTiet)
//                                .continueWithTask(chiTietTaskSnapshot -> chiTietRef.getDownloadUrl())
//                                .addOnSuccessListener(uriChiTietDownload -> {
//                                    urlList.add(uriChiTietDownload.toString());
//                                    latch.countDown(); // Đánh dấu là một tác vụ đã hoàn thành
//                                })
//                                .addOnFailureListener(e -> {
//                                    Toast.makeText(context, "Thêm hình chi tiết không thành công", Toast.LENGTH_SHORT).show();
//                                    latch.countDown();
//                                });
//                    }
//
//                    // Đợi tất cả các tác vụ tải lên hình chi tiết hoàn thành
//                    new Thread(() -> {
//                        try {
//                            latch.await();
//                            SanPhamDAO sanPhamDAO = new SanPhamDAO();
//                            SanPham sanPham = new SanPham(System.currentTimeMillis(),masp, tensp, solgsp, solgban, gianhap, giaban, thuonghieu, mau, size, trangthai, ghichu, uri.toString(), urlList);
//                            sanPhamDAO.insertProducts(sanPham);
//                            progressDialog.dismiss();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }).start();
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(context, "Thêm sản phẩm không thành công", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                });
//    }

}