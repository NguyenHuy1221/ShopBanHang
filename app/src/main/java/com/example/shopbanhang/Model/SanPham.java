package com.example.shopbanhang.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SanPham implements Serializable {
    private long timestamp;
    private int masp;
    private String tensp;
    private int soluongnhap;
    private int soluongban;
    private double gianhap;
    private double giaban;
    private String thuonghieu;
    private String trangthai;
    private String ghichu;
    private String imageUrl;
    private List<String> urlChiTiet;



    public SanPham() {

    }

    public SanPham(long timestamp, int masp, String tensp, int soluongnhap, int soluongban, double gianhap, double giaban, String thuonghieu, String trangthai, String ghichu, String imageUrl, List<String> urlChiTiet) {
        this.timestamp = timestamp;
        this.masp = masp;
        this.tensp = tensp;
        this.soluongnhap = soluongnhap;
        this.soluongban = soluongban;
        this.gianhap = gianhap;
        this.giaban = giaban;
        this.thuonghieu = thuonghieu;

        this.trangthai = trangthai;
        this.ghichu = ghichu;
        this.imageUrl = imageUrl;
        this.urlChiTiet = urlChiTiet;
    }

    public SanPham(int soluongban) {
        this.soluongban = soluongban;
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

    public int getSoluongnhap() {
        return soluongnhap;
    }

    public void setSoluongnhap(int soluongnhap) {
        this.soluongnhap = soluongnhap;
    }

    public int getSoluongban() {
        return soluongban;
    }

    public void setSoluongban(int soluongban) {
        this.soluongban = soluongban;
    }

    public double getGianhap() {
        return gianhap;
    }

    public void setGianhap(double gianhap) {
        this.gianhap = gianhap;
    }

    public double getGiaban() {
        return giaban;
    }

    public void setGiaban(double giaban) {
        this.giaban = giaban;
    }

    public String getThuonghieu() {
        return thuonghieu;
    }

    public void setThuonghieu(String thuonghieu) {
        this.thuonghieu = thuonghieu;
    }



    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getUrlChiTiet() {
        return urlChiTiet;
    }

    public void setUrlChiTiet(List<String> urlChiTiet) {
        this.urlChiTiet = urlChiTiet;
    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("timestamp", timestamp);
        result.put("masp", masp);
        result.put("tensp", tensp);
        result.put("soluongnhap", soluongnhap);
        result.put("soluongban", soluongban);
        result.put("gianhap", gianhap);
        result.put("giaban", giaban);
        result.put("thuonghieu", thuonghieu);
        result.put("trangthai", trangthai);
        result.put("ghichu", ghichu);
        result.put("imageUrl", imageUrl);
        result.put("urlChiTiet", urlChiTiet);
        return result;
    }

}
