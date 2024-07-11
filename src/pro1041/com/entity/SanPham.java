/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.entity;

import java.util.Date;

/**
 *
 * @author Tom
 */
public class SanPham {

    private int id_sanPham;
    private String maSanPham;
    private String tenSanPham;
    private int gia;
    private int soluongtonkho;
    private Date ngayTao;
    private Date ngaySua;
    private String tenNSX;
    private String tenThuongHieu;
    private String tenChatLieu;
    private String IMG;
    private String tenKichThuoc;
    private String tenMauSac;
    private String loaiKhoa;
    private int id_SPCT;

    public SanPham() {
    }

    public SanPham(int id_sanPham, String maSanPham, String tenSanPham, Date ngayTao, Date ngaySua, String tenNSX, String tenThuongHieu) {
        this.id_sanPham = id_sanPham;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.ngayTao = ngayTao;
        this.ngaySua = ngaySua;
        this.tenNSX = tenNSX;
        this.tenThuongHieu = tenThuongHieu;
    }

    public SanPham(int id_sanPham, String maSanPham, String tenSanPham, int gia, int soluongtonkho, Date ngayTao, Date ngaySua, String tenNSX, String tenThuongHieu, String tenChatLieu, String IMG, String tenKichThuoc, String tenMauSac, String loaiKhoa, int id_SPCT) {
        this.id_sanPham = id_sanPham;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.soluongtonkho = soluongtonkho;
        this.ngayTao = ngayTao;
        this.ngaySua = ngaySua;
        this.tenNSX = tenNSX;
        this.tenThuongHieu = tenThuongHieu;
        this.tenChatLieu = tenChatLieu;
        this.IMG = IMG;
        this.tenKichThuoc = tenKichThuoc;
        this.tenMauSac = tenMauSac;
        this.loaiKhoa = loaiKhoa;
        this.id_SPCT = id_SPCT;
    }

    public int getId_sanPham() {
        return id_sanPham;
    }

    public void setId_sanPham(int id_sanPham) {
        this.id_sanPham = id_sanPham;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoluongtonkho() {
        return soluongtonkho;
    }

    public void setSoluongtonkho(int soluongtonkho) {
        this.soluongtonkho = soluongtonkho;
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

    public String getTenNSX() {
        return tenNSX;
    }

    public void setTenNSX(String tenNSX) {
        this.tenNSX = tenNSX;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getTenChatLieu() {
        return tenChatLieu;
    }

    public void setTenChatLieu(String tenChatLieu) {
        this.tenChatLieu = tenChatLieu;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public String getTenKichThuoc() {
        return tenKichThuoc;
    }

    public void setTenKichThuoc(String tenKichThuoc) {
        this.tenKichThuoc = tenKichThuoc;
    }

    public String getTenMauSac() {
        return tenMauSac;
    }

    public void setTenMauSac(String tenMauSac) {
        this.tenMauSac = tenMauSac;
    }

    public String getLoaiKhoa() {
        return loaiKhoa;
    }

    public void setLoaiKhoa(String loaiKhoa) {
        this.loaiKhoa = loaiKhoa;
    }

    public int getId_SPCT() {
        return id_SPCT;
    }

    public void setId_SPCT(int id_SPCT) {
        this.id_SPCT = id_SPCT;
    }

}
