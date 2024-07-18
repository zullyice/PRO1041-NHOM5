/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pro1041.com.service;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import pro1041.com.entity.chatLieu;
import pro1041.com.utils.DBConnect;

/**
 *
 * @author Van Can
 */
public class chatLieuDao {
    DBConnect dbconnect = new DBConnect();
    public ArrayList<chatLieu> getAll() {
        String sql = "SELECT id_chatLieu,tenChatLieu,maChatLieu FROM ChatLieu";
         ArrayList<chatLieu> list = new ArrayList<>();
        try (Connection conn = dbconnect.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                chatLieu t = new chatLieu();
                t.setMachatLieu(rs.getString("maChatLieu"));
                t.setTenchatLieu(rs.getString("tenChatLieu"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int addNew(chatLieu chLieu) {
       
        String sql = "INSERT INTO dbo.ChatLieu(maChatLieu,tenChatLieu) VALUES (?,?)";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement pst = conn.prepareCall(sql)){
             pst.setObject(1, chLieu.getMachatLieu());
            pst.setObject(2,chLieu.getTenchatLieu());
            return pst.executeUpdate();
            } catch (Exception e) {
            e.printStackTrace();
        }
    return 0;
    }

    public ArrayList<chatLieu> SearchByma(String maChatLieu) {
        String sql = "SELECT * FROM dbo.ChatLieu WHERE maChatLieu LIKE '%" + maChatLieu +  "%'";
         ArrayList<chatLieu> list = new ArrayList<>();
        try (Connection conn = dbconnect.getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                chatLieu t = new chatLieu();
                t.setId(rs.getInt("id_chatLieu"));
                t.setMachatLieu(rs.getString("maChatLieu"));
                t.setTenchatLieu(rs.getString("tenChatLieu"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int Update(chatLieu chatLieu1,String ma) {
        String sql = "UPDATE dbo.ChatLieu SET tenChatLieu = ? WHERE maChatLieu = ?";
    
    try (Connection conn = dbconnect.getConnection();
         PreparedStatement pst = conn.prepareStatement(sql)) {
        
        pst.setString(1, chatLieu1.getTenchatLieu());
        pst.setString(2, ma);
        
        return pst.executeUpdate();
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    
    return 0;
    }

    public int Delete(String machatLieu,int id ) {
      
        String sql = "UPDATE dbo.SanPhamChiTiet SET id_chatLieu = NULL WHERE id_chatLieu = ? DELETE FROM dbo.ChatLieu WHERE maChatLieu = ?;";
        
        try (Connection conn = dbconnect.getConnection(); PreparedStatement pst = conn.prepareCall(sql)) {
            pst.setInt(1, id);
            pst.setObject(2, machatLieu);
            return pst.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    return 0;
    }
    public boolean checkIdTrung(String id) {
      String sql = "SELECT COUNT(*) AS count FROM dbo.ChatLieu WHERE ID = ?";
        try (Connection conn = dbconnect.getConnection(); PreparedStatement pst = conn.prepareCall(sql)){
              ResultSet rs = pst.executeQuery();
              pst.setObject(1, id);
            if (rs.next()) {
                int count = rs.getInt("count");
                return count > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; 
    }
}

    
    
    
