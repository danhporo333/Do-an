package com.example.do_an_qlnv_hutech.model;

import java.io.Serializable;

public class NhanVien implements Serializable {
    private String manv;
    private String hovaten;
    private String gioitinh;
    private int namsinh;

    public NhanVien(String hovaten, String gioitinh) {
        this.hovaten = hovaten;
        this.gioitinh = gioitinh;
    }

    public NhanVien(String manv, String hovaten, String gioitinh, int namsinh) {
        this.manv = manv;
        this.hovaten = hovaten;
        this.gioitinh = gioitinh;
        this.namsinh = namsinh;
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
