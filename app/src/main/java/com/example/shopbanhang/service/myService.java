package com.example.shopbanhang.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.shopbanhang.Activity.ThanhToanMainActivity;
import com.example.shopbanhang.R;


public class myService extends Service {

    private static final String CHANNEL_ID = "MyChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String tenSanPham = intent.getStringExtra("tenSanPham");
            int tongTien = intent.getIntExtra("tongTien", 0);

            // Hiển thị thông báo
            showNotification(tenSanPham, tongTien);
        }

        return START_NOT_STICKY;
    }

    private void showNotification(String tenSanPham, int tongTien) {
        // Tạo Intent để mở một Activity khi thông báo được nhấn
        Intent resultIntent = new Intent(this, ThanhToanMainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Xây dựng thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_hear)
                .setContentTitle("Thông Báo Mua Hàng")
                .setContentText("Bạn Đã Mua Sản Phẩm: " + tenSanPham + " Tổng Tiền: " + tongTien + " ₫")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        // Lấy NotificationManager
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Hiển thị thông báo
        notificationManager.notify(0, builder.build());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
