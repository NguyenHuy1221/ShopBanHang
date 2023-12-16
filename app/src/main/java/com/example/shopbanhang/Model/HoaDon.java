package com.example.shopbanhang.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HoaDon {

    private int maHD;
    private int idKhachHang;
    private String ngaytaoHD;
    private String giotaoHD;
    private int tinhTrang;
    private int idKhuyenMai;
    private String diaChi;
    private double tongtien;

    public HoaDon() {
    }

    public HoaDon(int maHD, int idKhachHang, String ngaytaoHD, String giotaoHD, int tinhTrang, int idKhuyenMai, String diaChi, double tongtien) {
        this.maHD = maHD;
        this.idKhachHang = idKhachHang;
        this.ngaytaoHD = ngaytaoHD;
        this.giotaoHD = giotaoHD;
        this.tinhTrang = tinhTrang;
        this.idKhuyenMai = idKhuyenMai;
        this.diaChi = diaChi;
        this.tongtien = tongtien;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public int getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(int idKhachHang) {
        this.idKhachHang = idKhachHang;
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

    public int getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(int tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public int getIdKhuyenMai() {
        return idKhuyenMai;
    }

    public void setIdKhuyenMai(int idKhuyenMai) {
        this.idKhuyenMai = idKhuyenMai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }
}
