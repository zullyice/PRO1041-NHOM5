/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.service;

import pro1041.com.entity.KhoaAo;

import com.sun.jdi.connect.spi.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import pro1041.com.utils.DBConnect;

/**
 *
 * @author datsky
 */
public class KhoaAoService {

    public List<KhoaAo> getAll() {
        String sql = """
                SELECT id_khoaAo, maKhoa, tenKhoa, ngaySua, ngayTao
                FROM khoaAo
                 """;
        List<KhoaAo> listKh = new ArrayList<>();
        try (java.sql.Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                KhoaAo ka = new KhoaAo(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getDate(5)
                );
                listKh.add(ka);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi để tiện kiểm tra
        }
        return listKh; // Luôn trả về danh sách, có thể rỗng
    }

    public int add(KhoaAo kh) {
        String sql = """
                     INSERT INTO khoaAo (maKhoa, tenKhoa, ngayTao, ngaySua)
                          VALUES
                                (?,?,?,?)
                     """;
        try (java.sql.Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, kh.getMaKhoa());
            ps.setObject(2, kh.getTenKhoa());
            ps.setObject(3, kh.getNgayTao());
            ps.setObject(4, kh.getNgaySua());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int sua(int id_khoaAo, KhoaAo ka) {
        String sql = """
                     UPDATE [dbo].[khoaAo]
                        SET [maKhoa] = ?
                           ,[tenKhoa] = ?
                     
                           ,[ngayTao] = ?
                           ,[ngaySua] = ?
                      WHERE id_khoaAo=?
                     """;
        try (java.sql.Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, ka.getMaKhoa());
            ps.setObject(2, ka.getTenKhoa());

            ps.setObject(3, ka.getNgayTao());
            ps.setObject(4, ka.getNgaySua());
            ps.setObject(5, id_khoaAo);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean xoa(int id) {
        String sql = """
                           DELETE FROM dbo.khoaAo
                           WHERE id_khoaAo = ?; 	
                     """;
        try (java.sql.Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
            ps.setObject(2, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
