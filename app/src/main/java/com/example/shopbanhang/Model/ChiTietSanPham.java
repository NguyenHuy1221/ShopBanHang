package com.example.shopbanhang.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChiTietSanPham {

    private int masp;
    private List<String> urlImages;


    public ChiTietSanPham () {

    }


    public ChiTietSanPham(int masp, List<String> urlImages) {
        this.masp = masp;
        this.urlImages = urlImages;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public List<String> getUrlImages() {
        return urlImages;
    }

    public void setUrlImages(List<String> urlImages) {
        this.urlImages = urlImages;
    }
}
