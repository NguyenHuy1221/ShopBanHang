package com.example.shopbanhang.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HoaDon {

    private String maHD;
    private String name_khachhang;
    private String ngaytaoHD;
    private String giotaoHD;
    private List<GioHang> sanPhamList;
    private double tongtien;
    private int tinhTrang;

    public HoaDon() {
    }

//    public HoaDon(String maHD, String name_khachhang, String ngaytaoHD, String giotaoHD, List<GioHang> sanPhamList, double tongtien) {
//        this.maHD = maHD;
//        this.name_khachhang = name_khachhang;
//        this.ngaytaoHD = ngaytaoHD;
//        this.giotaoHD = giotaoHD;
//        this.sanPhamList = sanPhamList;
//        this.tongtien = tongtien;
//    }


    public HoaDon(String maHD, String name_khachhang, String ngaytaoHD, String giotaoHD, List<GioHang> sanPhamList, double tongtien, int tinhTrang) {
        this.maHD = maHD;
        this.name_khachhang = name_khachhang;
        this.ngaytaoHD = ngaytaoHD;
        this.giotaoHD = giotaoHD;
        this.sanPhamList = sanPhamList;
        this.tongtien = tongtien;
        this.tinhTrang = tinhTrang;
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public String getName_khachhang() {
        return name_khachhang;
    }

    public void setName_khachhang(String name_khachhang) {
        this.name_khachhang = name_khachhang;
    }

    public String getNgaytaoHD() {
        return ngaytaoHD;
    }

    public void setNgaytaoHD(String ngaytaoHD) {
        this.ngaytaoHD = ngaytaoHD;
    }

    public String getGiotaoHD() {
        return giotaoHD;
    }

    public void setGiotaoHD(String giotaoHD) {
        this.giotaoHD = giotaoHD;
    }

    public List<GioHang> getSanPhamList() {
        return sanPhamList;
    }

    public void setSanPhamList(List<GioHang> sanPhamList) {
        this.sanPhamList = sanPhamList;
    }

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }


}
