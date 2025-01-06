package com.example.do_an_qlnv_hutech.model;

import java.io.Serializable;

public class LichGV implements Serializable {
    private String malich;
    private String hovaten;
    private String lop;
    private String thu;
    private String cahoc;
    private String id_monhoc;
    private String monhoc;
    private String phong_hoc;


    public LichGV(String malich ,String thu, String cahoc, String lop, String monhoc, String phong_hoc) {
        this.malich = malich;
        this.thu = thu;
        this.cahoc = cahoc;
        this.lop = lop;
        this.monhoc = monhoc;
        this.phong_hoc = phong_hoc;
    }

    public String getPhong_hoc() {
        return phong_hoc;
    }

    public void setPhong_hoc(String phong_hoc) {
        this.phong_hoc = phong_hoc;
    }

    public String getMonhoc() {
        return monhoc;
    }

    public void setMonhoc(String monhoc) {
        this.monhoc = monhoc;
    }

    public String getMalich() {
        return malich;
    }

    public void setMalich(String malich) {
        this.malich = malich;
    }

    public String getHovaten() {
        return hovaten;
    }

    public void setHovaten(String hovaten) {
        this.hovaten = hovaten;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getCahoc() {
        return cahoc;
    }

    public void setCahoc(String cahoc) {
        this.cahoc = cahoc;
    }

    public String getId_monhoc() {
        return id_monhoc;
    }

    public void setId_monhoc(String id_monhoc) {
        this.id_monhoc = id_monhoc;
    }
}
