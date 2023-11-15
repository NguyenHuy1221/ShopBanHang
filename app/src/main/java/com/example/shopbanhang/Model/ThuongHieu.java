package com.example.shopbanhang.Model;

public class ThuongHieu {
    private String idThuongHieu;
    private String tenThuongHieu;
    private String imageUrl; // Thay vì image

    public ThuongHieu() {
        // Hàm khởi tạo mặc định là cần thiết cho Firebase Realtime Database
    }

    public ThuongHieu(String idThuongHieu, String tenThuongHieu, String imageUrl) {
        this.idThuongHieu = idThuongHieu;
        this.tenThuongHieu = tenThuongHieu;
        this.imageUrl = imageUrl;
    }

    public String getIdThuongHieu() {
        return idThuongHieu;
    }

    public void setIdThuongHieu(String idThuongHieu) {
        this.idThuongHieu = idThuongHieu;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

