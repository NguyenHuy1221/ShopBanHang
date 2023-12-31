package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.ImageChiTietAdapter;
import com.example.shopbanhang.Adapter.SanPhamAdapter;
import com.example.shopbanhang.DAO.SanPhamDAO;
import com.example.shopbanhang.Model.AddsizeColor;
import com.example.shopbanhang.Model.ChiTietSanPham;
import com.example.shopbanhang.Model.ChiTietSanPhamfix;
import com.example.shopbanhang.Model.KhuyenMai;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.TaiKhoan;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.example.shopbanhang.SharedPreferences.MySharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class ThemSanPhamActivity extends AppCompatActivity {

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
    private EditText edt_masp, edt_tensp, edt_so_luong_sp, edt_gia_nhap, edt_gia_ban, edt_ghi_chu,Edt_so_luong_sp;
    private Spinner spn_loaisp, spn_mausp, spn_sizesp, spn_trang_thai;
    private Button btnAdd;
    private RecyclerView rcy_ct_anh;

    private LinearLayout sizeLayout;
    private Button buttonAddSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_san_pham);


        anhxa();
        addSizeAndColor();
        themSanPham();

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
//        spn_mausp = findViewById(R.id.spn_mau_sp);
//        spn_sizesp = findViewById(R.id.spn_size_sp);
        spn_trang_thai = findViewById(R.id.spn_trang_thai_sp);
        edt_ghi_chu = findViewById(R.id.edt_ghi_chu_sp);
        sizeLayout = findViewById(R.id.sizeLayout);
        buttonAddSize = findViewById(R.id.buttonAddSize);
        btnAdd = findViewById(R.id.btn_them_sp);

    }

    private AddsizeColor addsizeColor;
    private HashMap<Integer, AddsizeColor> map = new HashMap<>();
    public void addSizeAndColor() {

        buttonAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.generateViewId();
                TextView tvName= new TextView(ThemSanPhamActivity.this);
                tvName.setId(id);
                tvName.setText("Thêm sản phẩm " + id);

                Spinner spinnerSize = new Spinner(ThemSanPhamActivity.this);
                spinnerSize.setId(id);
                List<String> sizeMoi = new ArrayList<>();
                sizeMoi.add("none");
                sizeMoi.add("M");
                sizeMoi.add("L");
                sizeMoi.add("Xl");
                sizeMoi.add("XXL");

                ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(ThemSanPhamActivity.this, android.R.layout.simple_spinner_item, sizeMoi);
                sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerSize.setAdapter(sizeAdapter);

                TextView tvMau = new TextView(ThemSanPhamActivity.this);
                tvMau.setText("Màu");
                Spinner spinnerMau = new Spinner(ThemSanPhamActivity.this);
                spinnerMau.setId(id);
                List<String> color = new ArrayList<>();
                color.add("none");
                color.add("Trắng");
                color.add("Vàng");
                color.add("Xanh");
                color.add("Đen");
                color.add("Đỏ");

                ArrayAdapter<String> mauAdapter = new ArrayAdapter<>(ThemSanPhamActivity.this, android.R.layout.simple_spinner_item, color);
                mauAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerMau.setAdapter(mauAdapter);

                EditText editTextSoLuong = new EditText(ThemSanPhamActivity.this);
                editTextSoLuong.setText(String.valueOf(0));
                editTextSoLuong.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextSoLuong.setId(id);

//                Button btnthem = new Button(ThemSanPhamActivity.this);
//                btnthem.setText("them");
                Button btnXoa = new Button(ThemSanPhamActivity.this);
                btnXoa.setText("Xóa");


                LinearLayout newLinearLayout = new LinearLayout(ThemSanPhamActivity.this);
                newLinearLayout.setOrientation(LinearLayout.VERTICAL);

                newLinearLayout.addView(tvName);
                newLinearLayout.addView(spinnerSize);
                newLinearLayout.addView(spinnerMau);
                newLinearLayout.addView(editTextSoLuong);
//                newLinearLayout.addView(btnthem);
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
//                kiemtradulieu(String.valueOf(spinnerMau.getSelectedItem()), String.valueOf(spinnerSize.getSelectedItem()));
                AddsizeColor addsizeColor1 = new AddsizeColor(tvName,spinnerSize,spinnerMau,editTextSoLuong);

                map.put(id, addsizeColor1);

                // Thêm cặp vào HashMap theo id
//                map.put(id, addsizeColor1);

//                btnthem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String masp2 = edt_masp.getText().toString().trim();
//                        String sizesp2 = spinnerSize.getSelectedItem().toString();
//                        String mausp2 = spinnerMau.getSelectedItem().toString();
//                        String soluongString = editTextSoLuong.getText().toString().trim();
//                        int soluong2 = -1;
//                        if (soluongString.isEmpty()) { soluong2 = -1; }
//                        else { soluong2 = Integer.parseInt(soluongString); }
//                        if (soluong2 >-1) {
//                            String id = UUID.randomUUID().toString();
//                            ChiTietSanPham chiTietSanPham = new ChiTietSanPham(id, Integer.parseInt(masp2), sizesp2, mausp2, soluong2);
//                            pushData(chiTietSanPham);
//
//                        }
//                    }
//                });

                sizeLayout.addView(newLinearLayout);

            }
        });


    }

    // Tạo một hàm để so sánh size và color trong HashMap
//    public static boolean compareSizeAndColor (HashMap<Integer, AddsizeColor> map) {
//        // Tạo một HashSet để lưu trữ các cặp size và color đã thấy
//        Set<String> seen = map;
//
//        // Duyệt qua các phần tử trong HashMap
//        for (Map.Entry<Integer, AddsizeColor> entry : map.entrySet ()) {
//            // Lấy size và color từ giá trị của phần tử
//            String size = String.valueOf(entry.getValue ().getSpnsize().getSelectedItem());
//            String color = String.valueOf(entry.getValue ().getSpncolor().getSelectedItem());
//
//            // Tạo một chuỗi để biểu diễn cặp size và color
//            String pair = size + "-" + color;
//
//            // Kiểm tra xem cặp này đã xuất hiện trước đó hay chưa
//            if (seen.contains (pair)) {
//                // Nếu có, thì trả về false và dừng hàm
//                return false;
//            } else {
//                // Nếu không, thì thêm cặp này vào HashSet
//                seen.add (pair);
//            }
//        }
//
//        // Nếu không có cặp size và color nào trùng nhau, thì trả về true
//        return true;
//    }

//    private List<ChiTietSanPhamfix> chiTietSanPhams = new ArrayList<>();
//
//    private void kiemtradulieu(String mau , String kickco) {
//        MySharedPreferences mySharedPreferences = new MySharedPreferences(context);
//        int idsanpham = Integer.parseInt(edt_masp.getText().toString());
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Chitietsanpham");
//        DatabaseReference userRef = myRef.child(String.valueOf(idsanpham));
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (chiTietSanPhams != null) {
//                    chiTietSanPhams.clear();
//
//                }
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ChiTietSanPhamfix chiTietSanPham = dataSnapshot.getValue(ChiTietSanPhamfix.class);
//                    if (chiTietSanPham != null){
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    public void themSanPham(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinerThuongHieu);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_loaisp.setAdapter(arrayAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("thuonghieu");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (spinerThuongHieu != null) {
                    spinerThuongHieu.clear();
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ThuongHieu thuongHieu = dataSnapshot.getValue(ThuongHieu.class);
                    spinerThuongHieu.add(thuongHieu.getTenThuongHieu());
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

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

        List<String> trangthai = new ArrayList<>();
        trangthai.add("Hoạt Động");

        ArrayAdapter<String> trangThaiAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, trangthai);
        trangThaiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn_trang_thai.setAdapter(trangThaiAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null){
                    // Sử dụng vòng lặp for để duyệt qua các khóa của HashMap
                    for (Integer i : map.keySet()) {

                        // Lấy giá trị của HashMap tại khóa i
                        AddsizeColor value1 = map.get(i);

                        // Sử dụng vòng lặp for khác để duyệt qua các khóa còn lại của HashMap
                        for (Integer j : map.keySet()) {

                            // Nếu i và j bằng nhau, bỏ qua vòng lặp này
                            if (i.equals(j)) {
                                continue;
                            }

                            // Lấy giá trị của HashMap tại khóa j
                            AddsizeColor value2 = map.get(j);

                            // So sánh size và color của hai giá trị bằng phương thức equalsIgnoreCase ()
                            if (value1.getSpnsize().getSelectedItem().toString().equals(value2.getSpnsize().getSelectedItem().toString()) && value1.getSpncolor().getSelectedItem().toString().equals(value2.getSpncolor().getSelectedItem().toString())) {

                                // Nếu size và color trùng nhau, hiển thị thông báo và dừng hàm
                                Toast.makeText(ThemSanPhamActivity.this, "Có Màu và kích cỡ bị trùng", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                    }

                } else {
                    Toast.makeText(context, "bạn chưa thêm chi tiết sản phẩm", Toast.LENGTH_SHORT).show();

                    return;
                }
                String masp2 = edt_masp.getText().toString().trim();
//                String sizesp2 = spinnerSize.getSelectedItem().toString();
//                String mausp2 = spinnerMau.getSelectedItem().toString();
//                String soluongString = Edt_so_luong_sp.getText().toString().trim();

                String masp = edt_masp.getText().toString().trim();
                String tensp = edt_tensp.getText().toString().trim();
                String solgspString  = String.valueOf(edt_so_luong_sp.getText().toString().trim());
                String gianhap = (edt_gia_nhap.getText().toString().trim());
                String giaban = (edt_gia_ban.getText().toString().trim());
                String loaisp = spn_loaisp.getSelectedItem().toString();
//                String mausp = spn_mausp.getSelectedItem().toString();
//                String sizesp = spn_sizesp.getSelectedItem().toString();
                String trangthaisp = spn_trang_thai.getSelectedItem().toString();
                String ghichu = edt_ghi_chu.getText().toString().trim();
//                String soluongsizecolor = edt_so_luong_sp.getText().toString().trim();

                if (masp.isEmpty() || tensp.isEmpty() || gianhap.isEmpty() || giaban.isEmpty() ||
                        ghichu.isEmpty() ||  loaisp == null  || trangthaisp == null || solgspString.isEmpty()) {
                    Toast.makeText(context, "Chưa nhập đủ dữ liệu", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (mImageUri == null){
                        Toast.makeText(context, "Chưa chọn hình", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (SanPham check : sanPhams) {
                        if (masp.equals(String.valueOf(check.getMasp()))){
                            Toast.makeText(context, "Mã sản phẩm trùng nhau - nhập mã mới", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    int solgsp;

//                    sortDescending(map);
                    solgsp = Integer.parseInt(solgspString);
//                    String id = UUID.randomUUID().toString();
//                    ChiTietSanPham chiTietSanPham = new ChiTietSanPham(id,Integer.parseInt(masp),sizesp,mausp,Integer.parseInt(soluongsizecolor));
//                    pushData(chiTietSanPham);
                    int tongsoluong=0;
                    for (int id2 : map.keySet()){

                        AddsizeColor addsizeColor1 = map.get(id2);
                        String spinnerkichco = addsizeColor1.getSpnsize().getSelectedItem().toString();
                        String spinnermau = addsizeColor1.getSpncolor().getSelectedItem().toString();
                        int soluong = Integer.parseInt(addsizeColor1.getEdtsoluong().getText().toString());
                        tongsoluong +=soluong;
                        String id3 = UUID.randomUUID().toString();
                        Random random = new Random();
                        int id = random.nextInt(100000);
                        ChiTietSanPham chiTietSanPham2 = new ChiTietSanPham(id,Integer.parseInt(masp),spinnerkichco,spinnermau,soluong);
                        pushData(chiTietSanPham2);

                    }
                    Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    uploadSanPhamToFirebase(mImageUri, Integer.parseInt(masp), tensp, tongsoluong, 0, Double.parseDouble(gianhap), Double.parseDouble(giaban), loaisp,  trangthaisp, ghichu,selectedImages);

                }

            }
        });

    }

//    public void sortDescending() {
//        // Sử dụng vòng lặp for để duyệt qua các khóa của HashMap
//        for (Integer i : map.keySet()) {
//
//            // Lấy giá trị của HashMap tại khóa i
//            AddsizeColor value1 = map.get(i);
//
//            // Sử dụng vòng lặp for khác để duyệt qua các khóa còn lại của HashMap
//            for (Integer j : map.keySet()) {
//
//                // Nếu i và j bằng nhau, bỏ qua vòng lặp này
//                if (i.equals(j)) {
//                    continue;
//                }
//
//                // Lấy giá trị của HashMap tại khóa j
//                AddsizeColor value2 = map.get(j);
//
//                // So sánh size và color của hai giá trị bằng phương thức equalsIgnoreCase ()
//                if (value1.getSpnsize().toString().equals(value2.getSpnsize().toString()) && value1.getSpncolor().toString().equals(value2.getSpncolor().toString())) {
//
//                    // Nếu size và color trùng nhau, hiển thị thông báo và dừng hàm
//                    Toast.makeText(ThemSanPhamActivity.this, "trùng nhau" +value1.getSpnsize() + value2.getSpnsize(), Toast.LENGTH_SHORT).show();
//                    return;
//                } else {
//                    Toast.makeText(ThemSanPhamActivity.this, "Không vấn đề gì" +value1.getSpnsize() + value2.getSpnsize(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        }
//    }



    private void oppenFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Cho phép chọn nhiều ảnh
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }

    private void uploadSanPhamToFirebase(Uri mImageUri, int masp, String tensp, int solgsp, int solgban, double gianhap, double giaban, String thuonghieu,  String trangthai, String ghichu, List<Uri> uriList) {
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
//                            if (map != null){
//                                for (int i = 0; i < map.size();i++){
//                                    if (map.get(i).getSpncolor().equals() && map.get(i).getSpnsize().equals()){
//                                        Toast.makeText(context, "Màu và size này đã tồn tại", Toast.LENGTH_SHORT).show();
//
//                                        return;
//                                    }
//                                }
//                            }



                            latch.await();
                            SanPhamDAO sanPhamDAO = new SanPhamDAO();
                            SanPham sanPham = new SanPham(System.currentTimeMillis(),masp, tensp, solgsp, solgban, gianhap, giaban, thuonghieu, trangthai, ghichu, uri.toString(), urlList);
                            sanPhamDAO.insertProducts(sanPham);
                            progressDialog.dismiss();
//                            Toast.makeText(ThemSanPhamActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ThemSanPhamActivity.this, QuanLySanPhamActivity.class);
                            startActivity(intent);
                            finish();
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
                // Chọn một ảnh
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

        int pathObj = chiTietSanPham.getIdchitietsanpham();
        databaseReference.child(String.valueOf(pathObj)).setValue(chiTietSanPham);

    }
}
