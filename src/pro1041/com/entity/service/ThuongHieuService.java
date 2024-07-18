/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.service;

import pro1041.com.entity.ThuongHieu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import pro1041.com.utils.DBConnect;

/**
 *
 * @author datsky
 */
public class ThuongHieuService {

    public List<ThuongHieu> getAll() {
        String sql = """
                SELECT id_thuongHieu, tenThuongHieu, maThuongHieu, ngaySua, ngayTao
                FROM ThuongHieu
                 """;
        List<ThuongHieu> listThuongHieu = new ArrayList<>();
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ThuongHieu th = new ThuongHieu(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getDate(4),
                        rs.getDate(5)
                );
                listThuongHieu.add(th);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ghi log lỗi để tiện kiểm tra
        }
        return listThuongHieu; // Luôn trả về danh sách, có thể rỗng
    }

    public int add(ThuongHieu th) {
        String sql = """
                     INSERT INTO ThuongHieu (maThuongHieu ,tenThuongHieu, ngayTao, ngaySua)
                          VALUES
                                (?,?,?,?)
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, th.getMaThuonghieu());
            ps.setObject(2, th.getTenThuongHieu());

            ps.setObject(3, th.getNgayTao());
            ps.setObject(4, th.getNgaySua());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
     public int sua(int idThuongHieu, ThuongHieu th) {
        String sql = """
                     UPDATE [dbo].[ThuongHieu]
                        SET [maThuongHieu] = ?
                           ,[tenThuongHieu] = ?
                    
                          
                           ,[ngayTao] = ?
                           ,[ngaySua] = ?
                      WHERE id_thuongHieu=?
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, th.getMaThuonghieu());
            ps.setObject(2, th.getTenThuongHieu());
       
            ps.setObject(3, th.getNgayTao());
            ps.setObject(4, th.getNgaySua());
            ps.setObject(5, idThuongHieu);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
       public boolean xoa(int id) {
        String sql = """
                     
                
                           DELETE FROM dbo.ThuongHieu
                           WHERE id_thuongHieu = ?;
                     
                           	
                     """;
        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setObject(1, id);
                 

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
