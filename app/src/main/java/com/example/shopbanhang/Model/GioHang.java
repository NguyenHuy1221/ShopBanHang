package com.example.shopbanhang.Model;

import java.util.ArrayList;
import java.util.List;

public class GioHang {

    private int masp;
    private String tensp;
    private double giasp;
    private int soluong;
    private String url;
    private String size;
    private String color;
    private double tongtien;
    private String user;
    private String key;


    public GioHang() {
    }

    public GioHang(int masp, String tensp, double giasp, int soluong, String url, String size, String color, double tongtien) {
        this.masp = masp;
        this.tensp = tensp;
        this.giasp = giasp;
        this.soluong = soluong;
        this.url = url;
        this.size = size;
        this.color = color;
        this.tongtien = tongtien;
    }

    public GioHang(int masp, String tensp, double giasp, int soluong, String url, String size, String color, double tongtien, String user) {
        this.masp = masp;
        this.tensp = tensp;
        this.giasp = giasp;
        this.soluong = soluong;
        this.url = url;
        this.size = size;
        this.color = color;
        this.tongtien = tongtien;
        this.user = user;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public double getGiasp() {
        return giasp;
    }

    public void setGiasp(double giasp) {
        this.giasp = giasp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getTongtien() {
        return tongtien;
    }

    public void setTongtien(double tongtien) {
        this.tongtien = tongtien;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
