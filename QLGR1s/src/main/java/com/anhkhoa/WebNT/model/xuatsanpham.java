package com.anhkhoa.WebNT.model;

import java.sql.Date;

public class xuatsanpham {

	private String tenkh;
	private String sdt;
	private String diachi;
	private Date ngayphieuxuat;
	private Integer sophieuxuat;
	private Integer mabanthanhphan;
	private Integer soluong;
	private Integer makh;
	public Integer getMakh()
	{
		return makh;
	}
	public void setMakh(Integer makh)
	{
		this.makh = makh;
	}
	public Integer getSophieuxuat()
	{
		return sophieuxuat;
	}
	public void setSophieuxuat(Integer sophieuxuat)
	{
		this.sophieuxuat = sophieuxuat;
	}
	public String getTenkh() {
		return tenkh;
	}

	public void setTenkh(String tenkh) {
		this.tenkh = tenkh;
	}
	
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt)
	{
		this.sdt = sdt;
	}
	public String getDiachi() {
		return diachi;
	}
	public void setDiachi(String diachi)
	{
		this.diachi = diachi;
	}

	public Date getNgayphieuxuat() {
		return ngayphieuxuat;
	}

	public void setNgayphieuxuat(Date ngayphieuxuat) {
		this.ngayphieuxuat = ngayphieuxuat;
	}

	public Integer getMabanthanhphan() {
		return mabanthanhphan;
	}

	public void setMabanthanhphan(Integer mabanthanhphan) {
		this.mabanthanhphan = mabanthanhphan;
	}

	public Integer getSoluong() {
		return soluong;
	}

	public void setSoluong(Integer soluong) {
		this.soluong = soluong;
	}
}
