/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.entity;

import java.util.Date;

/**
 *
 * @author datsky
 */
public class NhaSanXuat {
    private int idNsx;
    private String tenNsx;
    private String maNsx;
    private String diaChi;
    private String email;
    private String sdt;

    private Date ngayTao;
    private Date ngaySua;

    public NhaSanXuat() {
    }

    public NhaSanXuat(int idNsx, String tenNsx, String maNsx, String diaChi, String email, String sdt, Date ngayTao, Date ngaySua) {
        this.idNsx = idNsx;
        this.tenNsx = tenNsx;
        this.maNsx = maNsx;
        this.diaChi = diaChi;
        this.email = email;
        this.sdt = sdt;
        this.ngayTao = ngayTao;
        this.ngaySua = ngaySua;
    }

    public int getIdNsx() {
        return idNsx;
    }

    public void setIdNsx(int idNsx) {
        this.idNsx = idNsx;
    }

    public String getTenNsx() {
        return tenNsx;
    }

    public void setTenNsx(String tenNsx) {
        this.tenNsx = tenNsx;
    }

    public String getMaNsx() {
        return maNsx;
    }

    public void setMaNsx(String maNsx) {
        this.maNsx = maNsx;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Date getNgaySua() {
        return ngaySua;
    }

    public void setNgaySua(Date ngaySua) {
        this.ngaySua = ngaySua;
    }

   
    
    
    
}