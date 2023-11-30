package com.example.shopbanhang.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopbanhang.Adapter.ImageChiTietAdapter;
import com.example.shopbanhang.Adapter.SanPhamAdapter;
import com.example.shopbanhang.DAO.SanPhamDAO;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class SuaSanPhamActivity extends AppCompatActivity {

    private List<SanPham> sanPhams = new ArrayList<>();
    private List<String> spinerThuongHieu = new ArrayList<>();
    private Context context = this;
    private ArrayList<Uri> selectedImages = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE_REQUESTS = 2;
    private Uri mImageUri;
    private ImageView img_chinh;
    private ImageChiTietAdapter imageChiTietAdapter;
    private ImageView img_ct;
    private EditText edt_masp, edt_tensp, edt_so_luong_sp, edt_gia_nhap, edt_gia_ban, edt_ghi_chu;
    private Spinner spn_loaisp, spn_mausp, spn_sizesp, spn_trang_thai;
    private Button btnAdd;
    private RecyclerView rcy_ct_anh;

    private LinearLayout sizeLayout;
    private Button buttonAddSize;
    private DatabaseReference mDatabaseReference;
    private SanPham mSanPham;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_san_pham);
        int masanpham = 0;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SAN_PHAM")) {
            SanPham sanPham = (SanPham) intent.getSerializableExtra("SAN_PHAM");
            masanpham = intent.getIntExtra("masanpham",0);
            Log.d("HUY", "Received SanPham: " + sanPham.toString());
            Log.d("HUY", "Received SanPham: " + masanpham);
        }else {
            Log.e("HUY", "Intent does not have SANPHAM_EXTRA");
        }



        anhxa();
//        addSizeAndColor();
        suaSanPham(masanpham);
        getDataFirebase();



    }

    private void getDataFirebase() {
        anhxa();
        int masanpham2 = 0;
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SAN_PHAM")) {
            SanPham sanPham = (SanPham) intent.getSerializableExtra("SAN_PHAM");
            masanpham2 = intent.getIntExtra("masanpham",0);
            Log.d("HUY", "Received SanPham: " + sanPham.toString());
            Log.d("HUY", "Received SanPham: " + masanpham2);
        }else {
            Log.e("HUY", "Intent does not have SANPHAM_EXTRA");
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");
        int finalMasanpham = masanpham2;
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sanPhams.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    mSanPham = dataSnapshot.getValue(SanPham.class);
                    if (mSanPham.getMasp()== finalMasanpham){
                        sanPhams.add(mSanPham);
                        Picasso.get().load(mSanPham.getImageUrl()).into(img_chinh);

                        edt_masp.setText(String.valueOf(mSanPham.getMasp()));
                        edt_tensp.setText(mSanPham.getTensp());
                        edt_so_luong_sp.setText(String.valueOf(mSanPham.getSoluongnhap()));
                        edt_gia_nhap.setText(String.valueOf(mSanPham.getGianhap()));
                        edt_gia_ban.setText(String.valueOf(mSanPham.getGiaban()));
                        edt_ghi_chu.setText(mSanPham.getGhichu());
//                List<String> color = new ArrayList<>();
//                color.add(thuonghieu);
//                ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, color);
//                colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spn_mausp.setAdapter(colorAdapter);

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, spinerThuongHieu);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_loaisp.setAdapter(arrayAdapter);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("thuonghieu");
                        int vitrithuonghieu = 0;
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                if (spinerThuongHieu != null) {
                                    spinerThuongHieu.clear();
                                }
                                spinerThuongHieu.add(mSanPham.getThuonghieu());
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    ThuongHieu thuongHieu = dataSnapshot.getValue(ThuongHieu.class);
//                                    if (thuongHieu.getTenThuongHieu().equals(mSanPham.getThuonghieu())) {
//                                        spinerThuongHieu.add(mSanPham.getThuonghieu());
//                                    }


                                    if (!thuongHieu.getTenThuongHieu().equals(mSanPham.getThuonghieu())) {
                                        spinerThuongHieu.add(thuongHieu.getTenThuongHieu());
                                    }
//                                    spinerThuongHieu.add(thuongHieu.getTenThuongHieu());
                                }

                                arrayAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                        List<String> size = new ArrayList<>();
                        size.add(mSanPham.getSizesp());
                        size.add("M");
                        size.add("L");
                        size.add("Xl");
                        size.add("XXL");


                        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, size);
                        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_sizesp.setAdapter(sizeAdapter);

                        List<String> color = new ArrayList<>();
                        color.add(mSanPham.getMausp());
                        color.add("Trắng");
                        color.add("Vàng");
                        color.add("Xanh");
                        color.add("Đen");
                        color.add("Đỏ");

                        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, color);
                        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_mausp.setAdapter(colorAdapter);

                        List<String> trangthai2 = new ArrayList<>();
                        trangthai2.add(mSanPham.getTrangthai());
                        trangthai2.add("Hoạt Động");
                        trangthai2.add("Không hoạt động");

                        ArrayAdapter<String> trangThaiAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, trangthai2);
                        trangThaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spn_trang_thai.setAdapter(trangThaiAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void anhxa() {
        img_chinh = findViewById(R.id.image_chinh);
        img_ct = findViewById(R.id.image_ct);
        edt_masp = findViewById(R.id.edt_ma_sp);
        edt_tensp = findViewById(R.id.edt_ten_sp);
        edt_so_luong_sp = findViewById(R.id.edt_so_luong);
        edt_gia_nhap = findViewById(R.id.edt_gia_sp);
        edt_gia_ban = findViewById(R.id.edt_gia_ban_sp);
        spn_loaisp = findViewById(R.id.spn_loai_sp);
        spn_mausp = findViewById(R.id.spn_mau_sp);
        spn_sizesp = findViewById(R.id.spn_size_sp);
        spn_trang_thai = findViewById(R.id.spn_trang_thai_sp);
        edt_ghi_chu = findViewById(R.id.edt_ghi_chu_sp);
        sizeLayout = findViewById(R.id.sizeLayout);
        buttonAddSize = findViewById(R.id.buttonAddSize);
        btnAdd = findViewById(R.id.btn_them_sp);

    }



    public void suaSanPham(int id){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("sanpham");
        SanPham sanPham = new SanPham();
        rcy_ct_anh = findViewById(R.id.rcy_chi_tiet_anh);
        rcy_ct_anh.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        imageChiTietAdapter = new ImageChiTietAdapter(selectedImages);
        rcy_ct_anh.setAdapter(imageChiTietAdapter);
        img_ct.setOnClickListener(v -> { oppenFile();});

        img_chinh.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,PICK_IMAGE_REQUESTS);
        });




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masp = edt_masp.getText().toString().trim();
                String tensp = edt_tensp.getText().toString().trim();
                String solgspString  = edt_so_luong_sp.getText().toString().trim();
                String gianhap = (edt_gia_nhap.getText().toString().trim());
                String giaban = (edt_gia_ban.getText().toString().trim());
                String loaisp = spn_loaisp.getSelectedItem().toString();
                String mausp = spn_mausp.getSelectedItem().toString();
                String sizesp = spn_sizesp.getSelectedItem().toString();
                String trangthaisp = spn_trang_thai.getSelectedItem().toString();
                String ghichu = edt_ghi_chu.getText().toString().trim();

                if (masp.isEmpty() || tensp.isEmpty() || solgspString.isEmpty() || gianhap.isEmpty() || giaban.isEmpty() ||
                        ghichu.isEmpty() || loaisp == null || mausp == null || sizesp == null || trangthaisp == null) {
                    Toast.makeText(context, "Chưa nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                }else {
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

//                    sanPham.setMasp(Integer.parseInt(masp));
                    sanPham.setTensp(tensp);
                    sanPham.setSoluongnhap(Integer.parseInt(solgspString));
                    sanPham.setGianhap(Double.parseDouble(gianhap));
                    sanPham.setGiaban(Double.parseDouble(giaban));
                    sanPham.setThuonghieu(loaisp);
                    sanPham.setMausp(mausp);
                    sanPham.setSizesp(sizesp);
                    sanPham.setTrangthai(trangthaisp);
                    sanPham.setGhichu(ghichu);

                    databaseReference.child(String.valueOf(id)).updateChildren(sanPham.toMap());

//                    String id = UUID.randomUUID().toString();
//                    ChiTietSanPham chiTietSanPham = new ChiTietSanPham(id,Integer.parseInt(masp),sizesp,mausp,0);
//                    pushData(chiTietSanPham);

                }

            }
        });

    }




    private void oppenFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    private void uploadSanPhamToFirebase(Uri mImageUri, int masp, String tensp, int solgsp, int solgban, double gianhap, double giaban, String thuonghieu, String mau, String size, String trangthai, String ghichu, List<Uri> uriList) {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://realtimedata-1e0aa.appspot.com");
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(System.currentTimeMillis() + ".PNG");
        ProgressDialog progressDialog = ProgressDialog.show(context, "Chờ Chút", "Đang tải sản phẩm...", true);

        // Tải lên hình chính
        imageRef.putFile(mImageUri)
                .continueWithTask(taskSnapshot -> imageRef.getDownloadUrl())
                .addOnSuccessListener(uri -> {

                    // Sử dụng CountDownLatch để đồng bộ việc tải lên nhiều hình ảnh
                    CountDownLatch latch = new CountDownLatch(uriList.size());
                    // Tạo danh sách URL của các hình chi tiết
                    List<String> urlList = new ArrayList<>();
                    // Tải lên từng hình chi tiết và lấy URL
                    for (Uri uriChiTiet : uriList) {
                        StorageReference chiTietRef = storageRef.child(System.currentTimeMillis() + "_CT.PNG");

                        chiTietRef.putFile(uriChiTiet)
                                .continueWithTask(chiTietTaskSnapshot -> chiTietRef.getDownloadUrl())
                                .addOnSuccessListener(uriChiTietDownload -> {
                                    urlList.add(uriChiTietDownload.toString());
                                    latch.countDown(); // Đánh dấu là một tác vụ đã hoàn thành
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(context, "Thêm hình chi tiết không thành công", Toast.LENGTH_SHORT).show();
                                    latch.countDown();
                                });
                    }

                    // Đợi tất cả các tác vụ tải lên hình chi tiết hoàn thành
                    new Thread(() -> {
                        try {
                            latch.await();
                            SanPhamDAO sanPhamDAO = new SanPhamDAO();
                            SanPham sanPham = new SanPham(System.currentTimeMillis(),masp, tensp, solgsp, solgban, gianhap, giaban, thuonghieu, mau, size, trangthai, ghichu, uri.toString(), urlList);
                            sanPhamDAO.insertProducts(sanPham);
                            progressDialog.dismiss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Thêm sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                // Chọn nhiều ảnh
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    selectedImages.add(imageUri);
                }
            } else if (data.getData() != null) {
                // Chọn một ảnhQ
                Uri imageUri = data.getData();
                selectedImages.add(imageUri);
            }
            imageChiTietAdapter.notifyDataSetChanged();
        }

        if (requestCode == PICK_IMAGE_REQUESTS && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(img_chinh);
        }


    }

    private void pushData(ChiTietSanPham chiTietSanPham) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Chitietsanpham");

        String pathObj = chiTietSanPham.getIdchitietsanpham();
        databaseReference.child(pathObj).setValue(chiTietSanPham);

    }

    public void addSizeAndColor() {

        buttonAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tvName = new TextView(SuaSanPhamActivity.this);
                tvName.setText("Thêm sản phẩm ");
                Spinner spinnerSize = new Spinner(SuaSanPhamActivity.this);

                List<String> sizeMoi = new ArrayList<>();
                sizeMoi.add("none");
                sizeMoi.add("M");
                sizeMoi.add("L");
                sizeMoi.add("Xl");
                sizeMoi.add("XXL");

                ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(SuaSanPhamActivity.this, android.R.layout.simple_spinner_item, sizeMoi);
                sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSize.setAdapter(sizeAdapter);

                TextView tvMau = new TextView(SuaSanPhamActivity.this);
                tvMau.setText("Màu");
                Spinner spinnerMau = new Spinner(SuaSanPhamActivity.this);

                List<String> color = new ArrayList<>();
                color.add("none");
                color.add("Trắng");
                color.add("Vàng");
                color.add("Xanh");
                color.add("Đen");
                color.add("Đỏ");

                ArrayAdapter<String> mauAdapter = new ArrayAdapter<>(SuaSanPhamActivity.this, android.R.layout.simple_spinner_item, color);
                mauAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMau.setAdapter(mauAdapter);

                EditText editTextSoLuong = new EditText(SuaSanPhamActivity.this);
                editTextSoLuong.setHint("Số lượng");
                editTextSoLuong.setInputType(InputType.TYPE_CLASS_NUMBER);

                Button btnthem = new Button(SuaSanPhamActivity.this);
                btnthem.setText("them");
                Button btnXoa = new Button(SuaSanPhamActivity.this);
                btnXoa.setText("Xóa");


                LinearLayout newLinearLayout = new LinearLayout(SuaSanPhamActivity.this);
                newLinearLayout.setOrientation(LinearLayout.VERTICAL);

                newLinearLayout.addView(tvName);
                newLinearLayout.addView(spinnerSize);
                newLinearLayout.addView(spinnerMau);
                newLinearLayout.addView(editTextSoLuong);
                newLinearLayout.addView(btnthem);
                newLinearLayout.addView(btnXoa);
                // Tạo một tag duy nhất cho newLinearLayout
                String uniqueTag = "layout_" + System.currentTimeMillis();

                // Đặt tag duy nhất cho newLinearLayout
                newLinearLayout.setTag(uniqueTag);

                // Đặt tag cho btnXoa trực tiếp
                btnXoa.setTag(uniqueTag);

                btnXoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Lấy tag được gán cho LinearLayout cha
                        String tag = (String) view.getTag();

                        // Tìm LinearLayout có tag cụ thể
                        for (int i = 0; i < sizeLayout.getChildCount(); i++) {
                            View child = sizeLayout.getChildAt(i);
                            if (child instanceof LinearLayout && Objects.equals(child.getTag(), tag)) {
                                // Loại bỏ LinearLayout được tìm thấy khỏi sizeLayout
                                sizeLayout.removeView(child);
                                break; // Dừng tìm kiếm khi tìm thấy
                            }
                        }
                    }
                });
                btnthem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String masp2 = edt_masp.getText().toString().trim();
                        String sizesp2 = spinnerSize.getSelectedItem().toString();
                        String mausp2 = spinnerMau.getSelectedItem().toString();
                        String soluongString = editTextSoLuong.getText().toString().trim();
                        int soluong2 = -1;
                        if (soluongString.isEmpty()) { soluong2 = -1; }
                        else { soluong2 = Integer.parseInt(soluongString); }
                        if (soluong2 >-1) {
                            String id = UUID.randomUUID().toString();
                            ChiTietSanPham chiTietSanPham = new ChiTietSanPham(id, Integer.parseInt(masp2), sizesp2, mausp2, soluong2);
                            pushData(chiTietSanPham);

                        }
                    }
                });

                sizeLayout.addView(newLinearLayout);

            }
        });


    }
}
