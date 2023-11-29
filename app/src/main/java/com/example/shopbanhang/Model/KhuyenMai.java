package com.example.shopbanhang.Model;

import java.util.HashMap;

public class KhuyenMai {
    private String idKhuyenMai;
    private String imgKhuyenMai;
    private String tenKhuyenMai;
    private String ngayBatDau;
    private String ngayKetThuc;
    private String phanTramKhuyenMai;

    public KhuyenMai(){

    }

    public KhuyenMai(String idKhuyenMai, String imgKhuyenMai, String tenKhuyenMai, String ngayBatDau, String ngayKetThuc, String phanTramKhuyenMai) {
        this.idKhuyenMai = idKhuyenMai;
        this.imgKhuyenMai = imgKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.phanTramKhuyenMai = phanTramKhuyenMai;
    }

    public String getIdKhuyenMai() {
        return idKhuyenMai;
    }

    public void setIdKhuyenMai(String idKhuyenMai) {
        this.idKhuyenMai = idKhuyenMai;
    }

    public String getImgKhuyenMai() {
        return imgKhuyenMai;
    }

    public void setImgKhuyenMai(String imgKhuyenMai) {
        this.imgKhuyenMai = imgKhuyenMai;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getPhanTramKhuyenMai() {
        return phanTramKhuyenMai;
    }

    public void setPhanTramKhuyenMai(String phanTramKhuyenMai) {
        this.phanTramKhuyenMai = phanTramKhuyenMai;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("imgKhuyenMai",imgKhuyenMai);
        result.put("tenKhuyenMai",tenKhuyenMai);
        result.put("ngayBatDau",ngayBatDau);
        result.put("ngayKetThuc",ngayKetThuc);
        result.put("phanTramKhuyenMai",phanTramKhuyenMai);
        return result;
    }
}
