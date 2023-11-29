package com.example.shopbanhang.Model;

import java.util.HashMap;

public class TaiKhoan {
    private String idtk;
    private String imgtk;
    private String tentk;
    private String emailtk;
    private String matkhautk;
    private String sdttk;
    private String diachitk;
    private String ngaytaotk;
    private String tinhtragtk;

    public TaiKhoan() {
    }

    public TaiKhoan(String idtk, String imgtk, String tentk, String emailtk, String matkhautk, String sdttk, String diachitk, String ngaytaotk, String tinhtragtk) {
        this.idtk = idtk;
        this.imgtk = imgtk;
        this.tentk = tentk;
        this.emailtk = emailtk;
        this.matkhautk = matkhautk;
        this.sdttk = sdttk;
        this.diachitk = diachitk;
        this.ngaytaotk = ngaytaotk;
        this.tinhtragtk = tinhtragtk;
    }

    public String getIdtk() {
        return idtk;
    }

    public void setIdtk(String idtk) {
        this.idtk = idtk;
    }

    public String getImgtk() {
        return imgtk;
    }

    public void setImgtk(String imgtk) {
        this.imgtk = imgtk;
    }

    public String getTentk() {
        return tentk;
    }

    public void setTentk(String tentk) {
        this.tentk = tentk;
    }

    public String getEmailtk() {
        return emailtk;
    }

    public void setEmailtk(String emailtk) {
        this.emailtk = emailtk;
    }

    public String getMatkhautk() {
        return matkhautk;
    }

    public void setMatkhautk(String matkhautk) {
        this.matkhautk = matkhautk;
    }

    public String getSdttk() {
        return sdttk;
    }

    public void setSdttk(String sdttk) {
        this.sdttk = sdttk;
    }

    public String getDiachitk() {
        return diachitk;
    }

    public void setDiachitk(String diachitk) {
        this.diachitk = diachitk;
    }

    public String getNgaytaotk() {
        return ngaytaotk;
    }

    public void setNgaytaotk(String ngaytaotk) {
        this.ngaytaotk = ngaytaotk;
    }

    public String getTinhtragtk() {
        return tinhtragtk;
    }

    public void setTinhtragtk(String tinhtragtk) {
        this.tinhtragtk = tinhtragtk;
    }
    public HashMap<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("imgtk",imgtk);
        result.put("tentk",tentk);
        result.put("emailtk",emailtk);
        result.put("sdttk",sdttk);
        result.put("diachitk",diachitk);
        result.put("ngaytaotk",ngaytaotk);
        result.put("tinhtragtk",tinhtragtk);
        return result;
    }
}
