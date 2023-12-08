package com.example.shopbanhang.Model;

import java.util.HashMap;

public class ChiTietSanPhamfix {

    private String idchitietsanpham;
    private int masp;
    private String kichco;
    private String mau;
    private int soluong;


    public ChiTietSanPhamfix() {

    }

    public ChiTietSanPhamfix(String idchitietsanpham, int masp, String kichco, String mau, int soluong) {
        this.idchitietsanpham = idchitietsanpham;
        this.masp = masp;
        this.kichco = kichco;
        this.mau = mau;
        this.soluong = soluong;
    }

    public String getIdchitietsanpham() {
        return idchitietsanpham;
    }

    public void setIdchitietsanpham(String idchitietsanpham) {
        this.idchitietsanpham = idchitietsanpham;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getKichco() {
        return kichco;
    }

    public void setKichco(String kichco) {
        this.kichco = kichco;
    }

    public String getMau() {
        return mau;
    }

    public void setMau(String mau) {
        this.mau = mau;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }


    public HashMap<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("idchitietsanpham",idchitietsanpham);
        result.put("masp",masp);
        result.put("kichco",kichco);
        result.put("mau",mau);
        result.put("soluong",soluong);
        return result;
    }



}
