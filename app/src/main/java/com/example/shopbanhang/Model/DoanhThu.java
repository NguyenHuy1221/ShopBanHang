package com.example.shopbanhang.Model;

import com.example.shopbanhang.Activity.Doanh_Thu;

import java.util.HashMap;

public class DoanhThu {
    private String mahoadon;
    private String ngaydathang;
    private double tongtien;

    public DoanhThu(){

    }

    public DoanhThu(String mahoadon, String ngaydathang, double tongtien) {
        this.mahoadon = mahoadon;
        this.ngaydathang = ngaydathang;
        this.tongtien = tongtien;
    }

    public String getMahoadon() {
        return mahoadon;
    }

    public void setMahoadon(String mahoadon) {
        this.mahoadon = mahoadon;
    }

    public String getNgaydathang() {
        return ngaydathang;
    }

    public void setNgaydathang(String ngaydathang) {
        this.ngaydathang = ngaydathang;
    }

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("maHD",mahoadon);
        result.put("ngaytaoHD",ngaydathang);
        result.put("tongtien",tongtien);
        return result;
    }
}
