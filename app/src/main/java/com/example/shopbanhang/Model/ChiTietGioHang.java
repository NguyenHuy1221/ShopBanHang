package com.example.shopbanhang.Model;

public class ChiTietGioHang {

    int id_chi_tiet_gio_hang;
    int id_gio_hang;
    int id_chi_tiet_san_pham;
    int so_luong;
    double don_gia;
    double tong_tien;

    public ChiTietGioHang() {
    }

    public ChiTietGioHang(int id_chi_tiet_gio_hang, int id_gio_hang, int id_chi_tiet_san_pham, int so_luong, double don_gia, double tong_tien) {
        this.id_chi_tiet_gio_hang = id_chi_tiet_gio_hang;
        this.id_gio_hang = id_gio_hang;
        this.id_chi_tiet_san_pham = id_chi_tiet_san_pham;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
        this.tong_tien = tong_tien;
    }

    public int getId_chi_tiet_gio_hang() {
        return id_chi_tiet_gio_hang;
    }

    public void setId_chi_tiet_gio_hang(int id_chi_tiet_gio_hang) {
        this.id_chi_tiet_gio_hang = id_chi_tiet_gio_hang;
    }

    public int getId_gio_hang() {
        return id_gio_hang;
    }

    public void setId_gio_hang(int id_gio_hang) {
        this.id_gio_hang = id_gio_hang;
    }

    public int getId_chi_tiet_san_pham() {
        return id_chi_tiet_san_pham;
    }

    public void setId_chi_tiet_san_pham(int id_chi_tiet_san_pham) {
        this.id_chi_tiet_san_pham = id_chi_tiet_san_pham;
    }

    public int getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(int so_luong) {
        this.so_luong = so_luong;
    }

    public double getDon_gia() {
        return don_gia;
    }

    public void setDon_gia(double don_gia) {
        this.don_gia = don_gia;
    }

    public double getTong_tien() {
        return tong_tien;
    }

    public void setTong_tien(double tong_tien) {
        this.tong_tien = tong_tien;
    }
}
