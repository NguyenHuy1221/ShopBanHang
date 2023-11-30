package com.example.shopbanhang.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shopbanhang.Adapter.CategoryMainAdapter;
import com.example.shopbanhang.Adapter.SanPhamMainAdapter;
import com.example.shopbanhang.Adapter.SliderAdapters;
import com.example.shopbanhang.Model.SanPham;
import com.example.shopbanhang.Model.SliderItems;
import com.example.shopbanhang.Model.ThuongHieu;
import com.example.shopbanhang.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrangChuActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler slideHander = new Handler();
    private RecyclerView recyclerViewCategory,recyclerViewProducts;
    private CategoryMainAdapter categoryMainAdapter;
    private RecyclerView.Adapter sanPhamMainAdapter;
    private List<ThuongHieu> mThuongHieu;
    private Context context = this;
    private LinearLayout Lme,next_gio_hang,nextTym;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);

        anhxa();
        banner();
        sendCategoryFirebase();
        sendProductsFirebase();

        Lme.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, Trang_Ca_Nhan.class);
            startActivity(intent);
        });

        next_gio_hang.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this,Gio_Hang.class);
            startActivity(intent);
        });

        nextTym.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChuActivity.this, SanPhamYeuThichMainActivity.class);
            startActivity(intent);
        });

    }

    public void anhxa(){
        Lme = findViewById(R.id.Lme);
        next_gio_hang = findViewById(R.id.next_gio_hang);
        nextTym = findViewById(R.id.next_tym);
        viewPager2 = findViewById(R.id.viewpagerSlider);
        recyclerViewCategory = findViewById(R.id.rcy_category);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerViewProducts = findViewById(R.id.rcy_products);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    private void banner() {
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.slider1));
        sliderItems.add(new SliderItems(R.drawable.slider2));
        sliderItems.add(new SliderItems(R.drawable.slider3));
        viewPager2.setAdapter(new SliderAdapters(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHander.removeCallbacks(sliderRunnable);
            }
        });
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem());
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        slideHander.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHander.postDelayed(sliderRunnable,2000);
    }


    public void sendCategoryFirebase() {
        mThuongHieu = new ArrayList<>();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("thuonghieu");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mThuongHieu.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren() ){
                    ThuongHieu thuongHieu = postSnapshot.getValue(ThuongHieu.class);
                    mThuongHieu.add(thuongHieu);
                }
                categoryMainAdapter = new CategoryMainAdapter(context,mThuongHieu);
                recyclerViewCategory.setAdapter(categoryMainAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendProductsFirebase() {
        List<SanPham> mSanPham = new ArrayList<>();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference("sanpham");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mSanPham.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                    mSanPham.add(sanPham);
                    Log.d("HUYNE", String.valueOf(mSanPham.size()));
                }
                sanPhamMainAdapter = new SanPhamMainAdapter(context,mSanPham);
                recyclerViewProducts.setAdapter(sanPhamMainAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}