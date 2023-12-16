package com.example.shopbanhang.Model;

public class GioHang {


    private int id_gio_hang;
    private int id_khach_hang;


    public GioHang() {
    }

    public GioHang(int id_gio_hang, int id_khach_hang) {
        this.id_gio_hang = id_gio_hang;
        this.id_khach_hang = id_khach_hang;
    }

    public int getId_gio_hang() {
        return id_gio_hang;
    }

    public void setId_gio_hang(int id_gio_hang) {
        this.id_gio_hang = id_gio_hang;
    }

    public int getId_khach_hang() {
        return id_khach_hang;
    }

    public void setId_khach_hang(int id_khach_hang) {
        this.id_khach_hang = id_khach_hang;
    }

}
