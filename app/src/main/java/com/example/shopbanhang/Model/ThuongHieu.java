package com.example.shopbanhang.Model;

import java.util.HashMap;
import java.util.Map;

public class ThuongHieu {
    private String idThuongHieu;
    private String tenThuongHieu;
    private String imageUrl;

    public ThuongHieu() {

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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("idThuongHieu", idThuongHieu);
        result.put("tenThuongHieu", tenThuongHieu);
        result.put("imageUrl", imageUrl);
        return result;
    }


}

