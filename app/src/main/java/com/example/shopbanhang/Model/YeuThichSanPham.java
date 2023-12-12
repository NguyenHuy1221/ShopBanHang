package com.example.shopbanhang.Model;

public class YeuThichSanPham {

    private int id_yeu_thich;
    private int id_khach_hang;
    private int id_san_pham;

    public YeuThichSanPham() {
    }

    public YeuThichSanPham(int id_yeu_thich, int id_khach_hang, int id_san_pham) {
        this.id_yeu_thich = id_yeu_thich;
        this.id_khach_hang = id_khach_hang;
        this.id_san_pham = id_san_pham;
    }

    public int getId_yeu_thich() {
        return id_yeu_thich;
    }

    public void setId_yeu_thich(int id_yeu_thich) {
        this.id_yeu_thich = id_yeu_thich;
    }

    public int getId_khach_hang() {
        return id_khach_hang;
    }

    public void setId_khach_hang(int id_khach_hang) {
        this.id_khach_hang = id_khach_hang;
    }

    public int getId_san_pham() {
        return id_san_pham;
    }

    public void setId_san_pham(int id_san_pham) {
        this.id_san_pham = id_san_pham;
    }
}
