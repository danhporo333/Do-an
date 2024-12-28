package com.example.do_an_qlnv_hutech.model;

import java.io.Serializable;

public class NhanVien implements Serializable {
    private String manv;
    private String hovaten;
    private String gioitinh;
    private int namsinh;
    private String phone;



    public NhanVien(String manv, String hovaten, String gioitinh, String phone) {
        this.manv = manv;
        this.hovaten = hovaten;
        this.gioitinh = gioitinh;
        this.phone = phone;
    }

//    public NhanVien(String manv, String hovaten, String gioitinh, int namsinh, int phone) {
//        this.manv = manv;
//        this.hovaten = hovaten;
//        this.gioitinh = gioitinh;
//        this.namsinh = namsinh;
//        this.phone = phone;
//    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getManv() {
        return manv;
    }

    public void setManv(String manv) {
        this.manv = manv;
    }

    public String getHovaten() {
        return hovaten;
    }

    public void setHovaten(String hovaten) {
        this.hovaten = hovaten;
    }

    public String getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(String gioitinh) {
        this.gioitinh = gioitinh;
    }

    public int getNamsinh() {
        return namsinh;
    }

    public void setNamsinh(int namsinh) {
        this.namsinh = namsinh;
    }
}
