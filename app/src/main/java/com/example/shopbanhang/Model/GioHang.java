package com.example.shopbanhang.Model;

import java.util.ArrayList;
import java.util.List;

public class GioHang {

    private List<SanPham> sanPhamList;

    public GioHang(){
        this.sanPhamList = new ArrayList<>();
    }

    public List<SanPham> getSanPhamList(){
        return sanPhamList;
    }

    public void addToCart(SanPham sanPham) {
        // kiểm tra xem sản phẩm có trong giỏ hàng hay chưa
        boolean sanPhamExists = false;

        for (SanPham sp : sanPhamList) {
            if (sp.getMasp() == sanPham.getMasp()) {
                // Nếu sản phẩm đã tồn tại trong giỏ hàng, tăng số lượng lên 1
                sp.setSoluongban(sp.getSoluongban() + 1);
                sanPhamExists = true;
                break;
            }
        }

        if (!sanPhamExists) {
            // Nếu sản phẩm chưa tồn tại trong giỏ hàng, thêm vào giỏ hàng với số lượng là 1
            SanPham newSanPham = new SanPham(
                    System.currentTimeMillis(),
                    sanPham.getMasp(),
                    sanPham.getTensp(),
                    sanPham.getSoluongnhap(),
                    1,  // Số lượng là 1
                    sanPham.getGianhap(),
                    sanPham.getGiaban(),
                    sanPham.getThuonghieu(),
                    sanPham.getMausp(),
                    sanPham.getSizesp(),
                    sanPham.getTrangthai(),
                    sanPham.getGhichu(),
                    sanPham.getImageUrl(),
                    sanPham.getUrlChiTiet()
            );
            sanPhamList.add(newSanPham);
        }
    }
}
