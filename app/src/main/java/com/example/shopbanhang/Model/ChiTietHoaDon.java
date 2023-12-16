package com.example.shopbanhang.Model;

public class ChiTietHoaDon {

    private int id_chi_tiet_hoa_don;
    private int hoa_don;
    private int id_chi_tiet_san_pham;
    private int so_luong;
    private double don_gia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(int id_chi_tiet_hoa_don, int hoa_don, int id_chi_tiet_san_pham, int so_luong, double don_gia) {
        this.id_chi_tiet_hoa_don = id_chi_tiet_hoa_don;
        this.hoa_don = hoa_don;
        this.id_chi_tiet_san_pham = id_chi_tiet_san_pham;
        this.so_luong = so_luong;
        this.don_gia = don_gia;
    }

    public int getId_chi_tiet_hoa_don() {
        return id_chi_tiet_hoa_don;
    }

    public void setId_chi_tiet_hoa_don(int id_chi_tiet_hoa_don) {
        this.id_chi_tiet_hoa_don = id_chi_tiet_hoa_don;
    }

    public int getHoa_don() {
        return hoa_don;
    }

    public void setHoa_don(int hoa_don) {
        this.hoa_don = hoa_don;
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
}
