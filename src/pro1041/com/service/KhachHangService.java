/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.service;

import java.util.ArrayList;
import pro1041.com.entity.KhachHang;
import pro1041.com.utils.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Tom
 */
public class KhachHangService {

    ArrayList<KhachHang> dskh = new ArrayList<>();

    public List<KhachHang> getAll() {
        String sql = """
                     SELECT [id_khachHang],[maKh],[hoTenKh],[diaChi],[gioiTinh],[email],[sdt],[ngayTao],[ngaySua]
                                              FROM [dbo].[KhachHang]
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<KhachHang> dskh = new ArrayList<>();
            while (rs.next()) {
                KhachHang kh = new KhachHang(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getDate(8), rs.getDate(9));
                dskh.add(kh);
            }
            return dskh;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int add(KhachHang khachHang) {
        String sql = """
                    INSERT INTO [dbo].[KhachHang]
                               ([maKh]
                               ,[hoTenKh]
                               ,[diaChi]
                               ,[gioiTinh]
                               ,[email]
                               ,[sdt]
                               ,[ngayTao]
                               ,[ngaySua])
                         VALUES
                               (?,?,?,?,?,?,?,?)
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, khachHang.getMaKh());
            ps.setObject(2, khachHang.getHoTenKh());
            ps.setObject(3, khachHang.getDiaChi());
            ps.setObject(4, khachHang.getGioiTinh());
            ps.setObject(5, khachHang.getEmail());
            ps.setObject(6, khachHang.getSdt());
            ps.setObject(7, khachHang.getNgayTao());
            ps.setObject(8, khachHang.getNgaySua());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
      public boolean xoa(int id) {
        String sql = """
          DELETE FROM [dbo].[KhachHang]
                WHERE id_khachHang=?
          """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
      public int sua(int id, KhachHang kh) {
        String sql = """
              UPDATE [dbo].[KhachHang]
                 SET [maKh] = ?
                    ,[hoTenKh] =?
                    ,[diaChi] = ?
                    ,[gioiTinh] = ?
                    ,[email] = ?
                    ,[sdt] = ?
                    ,[ngayTao] = ?
                    ,[ngaySua] = ?
               WHERE id_khachHang=?
              """;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)){
            
            ps.setObject(1, kh.getMaKh());
            ps.setObject(2, kh.getHoTenKh());
            ps.setObject(3, kh.getDiaChi());
            ps.setObject(4, kh.getGioiTinh());
            ps.setObject(5, kh.getEmail());
            ps.setObject(6, kh.getSdt());
            ps.setObject(7, kh.getNgayTao());
            ps.setObject(8, kh.getNgaySua());
            ps.setObject(9, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
