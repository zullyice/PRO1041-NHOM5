/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pro1041.com.entity.KhuyenMai;
import pro1041.com.utils.DBConnect;

/**
 *
 * @author Tom
 */
public class KhuyenMaiService {

    private static Connection con = null;
    private Connection c = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String sql = null;

    public List<KhuyenMai> getAll() {
        String sql = """
                     SELECT [id_khuyenMai]
                           ,[tenKM]
                           ,[ngayBatDau]
                           ,[ngayKetThuc]
                           ,[giaTri]
                           ,[donVi]
                           ,[trangThai]
                           ,[ngayTao]
                           ,[ngaySua]
                       FROM [dbo].[KhuyenMai]
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<KhuyenMai> dskm = new ArrayList<>();
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(rs.getInt(1), rs.getString(2), rs.getDate(3), rs.getDate(4), rs.getInt(5), rs.getString(6), rs.getBoolean(7), rs.getDate(8), rs.getDate(9));
                dskm.add(km);
            }
            return dskm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int them(KhuyenMai km) {
        String sql = """
                     INSERT INTO [dbo].[KhuyenMai]
                                ([tenKM]
                                ,[ngayBatDau]
                                ,[ngayKetThuc]
                                ,[giaTri]
                                ,[donVi]
                                ,[trangThai]
                                ,[ngayTao]
                                ,[ngaySua]
                                )
                          VALUES
                                (?,?,?,?,?,?,?,?)
                     """;
        try {
            Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, km.getTenKM());
            ps.setObject(2, km.getNgayBatDau());
            ps.setObject(3, km.getNgayKetThuc());
            ps.setObject(4, km.getGiaTri());
            ps.setObject(5, km.getDonVi());
            ps.setObject(6, km.getTrangThai());
            ps.setObject(7, km.getNgayTao());
            ps.setObject(8, km.getNgaySua());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int sua(int id, KhuyenMai km) {
        sql = """
              UPDATE [dbo].[KhuyenMai]
                 SET [tenKM] = ?
                    ,[ngayBatDau] = ?
                    ,[ngayKetThuc] =?
                    ,[giaTri] = ?
                    ,[donVi] = ?
                    ,[trangThai] = ?
                    ,[ngayTao] = ?
                    ,[ngaySua] = ?
               WHERE id_khuyenMai=?
              """;

        try {
            con = DBConnect.getConnection();
            ps = con.prepareStatement(sql);
            ps.setObject(1, km.getTenKM());
            ps.setObject(2, km.getNgayBatDau());
            ps.setObject(3, km.getNgayKetThuc());
            ps.setObject(4, km.getGiaTri());
            ps.setObject(5, km.getDonVi());
            ps.setObject(6, km.getTrangThai());
            ps.setObject(7, km.getNgayTao());
            ps.setObject(8, km.getNgaySua());
            ps.setObject(9, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean xoa(int id) {
        sql = """
          DELETE FROM [dbo].[KhuyenMai]
                WHERE id_khuyenMai=?
          """;
        try {
            con = DBConnect.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public KhuyenMai getKhuyenMaiByID(int id_khuyenMai) {
        KhuyenMai km = null;
         con = DBConnect.getConnection();
        String sql = """
                     SELECT * FROM [dbo].[KhuyenMai] WHERE id_khuyenMai = ? 
                     """;
        
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                km = new KhuyenMai();
                km.setId_khuyenMai(rs.getInt("id_khuyenMai"));
                km.setTenKM(rs.getString("tenKM"));
                km.setGiaTri(rs.getInt("giaTri"));
                km.setDonVi(rs.getString("donVi")); 
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return km;
    }
}
