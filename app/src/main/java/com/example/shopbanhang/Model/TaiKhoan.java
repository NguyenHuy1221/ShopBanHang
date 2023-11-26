package com.example.shopbanhang.Model;

public class TaiKhoan {
    private String idtk;
    private String imgtk;
    private String emailtk;
    private String matkhautk;

    public TaiKhoan(String idtk, String imgtk, String emailtk, String matkhautk) {
        this.idtk = idtk;
        this.imgtk = imgtk;
        this.emailtk = emailtk;
        this.matkhautk = matkhautk;
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
}
