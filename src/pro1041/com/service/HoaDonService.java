/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pro1041.com.entity.HoaDon;
import pro1041.com.entity.KhachHang;
import pro1041.com.entity.SanPham;
import pro1041.com.utils.DBConnect;

/**
 *
 * @author Tom
 */
public class HoaDonService {

    private ResultSet rs = null;

    public List<HoaDon> getAll() {
        String sql = """
                     SELECT [id_hoaDon]
                           ,[maHoaDon]
                           ,[tenHoaDon]
                            ,hd.trangThai
                            ,hd.ngayTao
                           ,nv.tenNhanVien
                           ,kh.hoTenKh
                           ,tt.loai
                       FROM [PRO1041_SD19308].[dbo].[HoaDon] hd
                       LEFT JOIN NhanVien nv ON hd.id_nhanVien = nv.id_nhanVien
                       LEFT JOIN KhachHang kh	ON hd.id_KhachHang = kh.id_khachHang
                       LEFT JOIN ThanhToan tt ON hd.id_HTTT = tt.id_HTTT
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<HoaDon> dshd = new ArrayList<>();
            while (rs.next()) {
                HoaDon hd = new HoaDon(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4), rs.getDate(5), rs.getString(6), rs.getString(7), rs.getString(8));
                dshd.add(hd);
            }
            return dshd;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HoaDon> getAllSP() {
        String sql = """
                     SELECT[maHDCT]
                     	,	sp.tenSanPham
                     	  ,[soLuong]
                           ,[tongTien]
                           ,hd.ngayTao 
                           ,sp.ngayTao 
                       FROM [dbo].[HoaDonChiTiet] hdct
                       JOIN SanPham sp ON sp.id_sanPham = hdct.id_SPCT
                       LEFT JOIN HoaDon hd on hdct.id_hoaDon = hd.id_hoaDon
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<HoaDon> dshdct = new ArrayList<>();
            while (rs.next()) {
                HoaDon hdct = new HoaDon(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDate(5), rs.getDate(6));
                dshdct.add(hdct);
            }
            return dshdct;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<HoaDon> getbyID(int idhd) {
        List<HoaDon> searchID = new ArrayList<>();
        String sql = """
                 SELECT [maHDCT]
                     , sp.tenSanPham
                     , [soLuong]
                     , [tongTien]
                     , hd.ngayTao 
                     , sp.ngayTao 
                 FROM [dbo].[HoaDonChiTiet] hdct
                 JOIN SanPham sp ON sp.id_sanPham = hdct.id_SPCT
                 LEFT JOIN HoaDon hd on hdct.id_hoaDon = hd.id_hoaDon
                 WHERE hd.id_hoaDon = ?
                 """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, idhd);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDon hdct = new HoaDon(
                            rs.getString(1),
                            rs.getString(2),
                            rs.getInt(3),
                            rs.getInt(4),
                            rs.getDate(5),
                            rs.getDate(6)
                    );
                    searchID.add(hdct);
                }
            }
            return searchID;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void themChiTietHoaDon(int idhdct, SanPham sp) {
        String sql = "INSERT INTO HoaDonChiTiet (id_hoaDon, id_SPCT, soluongtonkho, gia, tongTien, trangThai) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnect.getConnection(); PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, idhdct);
            statement.setInt(2, sp.getId_SPCT());
            statement.setInt(3, sp.getSoluongtonkho());
            statement.setDouble(4, sp.getGia());
            statement.setDouble(5, sp.getSoluongtonkho() * sp.getGia());
            statement.setBoolean(6, true);
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
