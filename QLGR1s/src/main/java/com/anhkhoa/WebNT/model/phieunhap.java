package com.anhkhoa.WebNT.model;

import java.sql.Date;

public class phieunhap {
   
    private Integer sophieunhap;

    private Date ngayphieunhap;

    private Integer maphanxuong;

    public Integer getSophieunhap() {
        return sophieunhap;
    }

    public void setSophieunhap(Integer sophieunhap) {
        this.sophieunhap = sophieunhap;
    }

    public Date getNgayphieunhap() {
        return ngayphieunhap;
    }

    public void setNgayphieunhap(Date ngayphieunhap) {
        this.ngayphieunhap = ngayphieunhap;
    }

    public Integer getMaphanxuong() {
        return maphanxuong;
    }

    public void setMaphanxuong(Integer maphanxuong) {
        this.maphanxuong = maphanxuong;
    }
}