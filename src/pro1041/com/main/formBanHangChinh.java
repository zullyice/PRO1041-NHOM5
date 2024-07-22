/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package pro1041.com.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import pro1041.com.entity.HoaDon;
import pro1041.com.entity.HoaDonCho;
import pro1041.com.entity.KhuyenMai;
import pro1041.com.entity.SanPham;
import pro1041.com.service.HoaDonService;
import pro1041.com.service.KhuyenMaiService;
import pro1041.com.service.NhanVienService;
import pro1041.com.service.SanPhamService;
import pro1041.com.utils.DBConnect;

/**
 *
 * @author Tom
 */
public class formBanHangChinh extends javax.swing.JPanel {

    private DefaultTableModel dtm = new DefaultTableModel();
    private DefaultTableModel model = new DefaultTableModel();
    private SanPhamService sanPhamService = new SanPhamService();
    private List<HoaDonCho> hoaDonTest = new ArrayList<>();
    private HoaDonService hoaDonService = new HoaDonService();
    private NhanVienService nhanVienService = new NhanVienService();

    /**
     * Creates new form formBanHangChinh
     */
    public formBanHangChinh() {
        initComponents();
        setSize(2000, 2000);
        loadSanPham();
        loadHoadon();
        fillNhanVienComboBox();
    }

    public static int themHoaDonVaoDatabase(String tenHD, boolean trangThai) {
        String sql = "INSERT INTO HoaDon (tenHoaDon,TrangThai) VALUES (?,0)";
        int generatedId = -1;

        try (Connection con = DBConnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tenHD);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return generatedId;
    }

    private void xoaHoaDonDaThanhToan(int id) {
        DefaultTableModel dtmHoaDon = (DefaultTableModel) tblHoaDonCho.getModel();
        int rowCount = dtmHoaDon.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            int id_hoaDon = (int) dtmHoaDon.getValueAt(i, 0);
            if (id_hoaDon == id) {
                dtmHoaDon.removeRow(i);
                break;
            }
        }
    }

    private double tinhTongTienGioHang() {
        double tongTien = 0;
        for (int i = 0; i < tblHDCT.getRowCount(); i++) {
            Object value = tblHDCT.getValueAt(i, 3);
            if (value != null) {
                try {
                    tongTien += Double.parseDouble(value.toString());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Dữ liệu không hợp lệ ở hàng " + (i + 1) + ", cột 6", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        return tongTien;
    }

    public void hienThiThongTinKhuyenMai(KhuyenMai km) {
        if (km != null) {
            txtIDGG.setText(String.valueOf(km.getId_khuyenMai()));
            txtTenMa.setText(km.getTenKM());
            txtGiaTriGiam.setText(String.valueOf(km.getGiaTri()));
            apMaKhuyenMai(km);
        }
    }

    private void apMaKhuyenMai(KhuyenMai km) {
        double tongTien = tinhTongTienGioHang();
        double giaTriGiam = km.getGiaTri();
        boolean isPercent = true;
        double tienGiam = isPercent ? (tongTien * giaTriGiam / 100) : giaTriGiam;

        double tongTienSauGiam = tongTien - tienGiam;

        txtSoTienGiam.setText(String.format("%.2f", tienGiam));
        txtTongTien.setText(String.format("%.2f", tongTienSauGiam));

        updateTienThua(giaTriGiam, isPercent);
    }

    private void updateTienThua(double giaTriGiam, boolean isPercent) {
        String tienKhachDuaStr = txtTienKhachDua.getText();
        if (tienKhachDuaStr.isEmpty()) {
            txtTienThua.setText("");
            return;
        }
        try {
            double tienKhachDua = Double.parseDouble(tienKhachDuaStr);
            double tongTien = tinhTongTienGioHang();
            double tienGiam = isPercent ? (tongTien * giaTriGiam / 100) : giaTriGiam;
            double tongTienSauGiam = tongTien - tienGiam;
            double tienThua = tienKhachDua - tongTienSauGiam;
            txtTienThua.setText(String.format("%.2f", tienThua));
        } catch (NumberFormatException e) {
            txtTienThua.setText("");
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số tiền hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public double tinhTongTienSauGiam(double tongTien, double giaTriGiam, boolean isPercent) {
        if (isPercent) {
            return tongTien - (tongTien * giaTriGiam / 100);
        } else {
            return tongTien - giaTriGiam;
        }
    }

    public void loadSanPham() {
        model = (DefaultTableModel) tblSanPham.getModel();
        List<SanPham> list = sanPhamService.getAllSPBH();
        model.setRowCount(0);
        for (SanPham sp : list) {
            model.addRow(new Object[]{sp.getId_sanPham(), sp.getTenSanPham(), sp.getSoluongtonkho(), sp.getGia(), sp.getTenThuongHieu(), sp.getTenNSX(), sp.getTenChatLieu(), sp.getTenKichThuoc(), sp.getTenKhoa(), sp.getKieuDang(), sp.getTenMauSac()});
        }
    }

    public void loadHoadon() {
        DefaultTableModel dtm = (DefaultTableModel) tblHoaDonCho.getModel();
        dtm.setRowCount(0);

        hoaDonTest.clear();

        try (Connection con = DBConnect.getConnection(); PreparedStatement statement = con.prepareStatement("SELECT id_hoaDon, maHoaDon,tenHoaDon,trangThai,ngayTao FROM HoaDon WHERE TrangThai = ?")) {
            statement.setInt(1, 0);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id_hoaDon");
                String ten = resultSet.getString("tenHoaDon");
                String trangThai = "Chưa Thanh Toán";
                String ngayTao = resultSet.getString("ngayTao");
                Object[] row = new Object[]{id, ten, trangThai, ngayTao};
                dtm.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void huyHoaDon(int idHoaDon) {
        String sqlDeleteHoaDonCT = "DELETE FROM HoaDonChiTiet WHERE id_hoaDon = ?";
        String sqlDeleteHoaDon = "DELETE FROM HoaDon WHERE id_hoaDon = ?";

        try (Connection con = DBConnect.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement statementCT = con.prepareStatement(sqlDeleteHoaDonCT); PreparedStatement statementHD = con.prepareStatement(sqlDeleteHoaDon)) {
                statementCT.setInt(1, idHoaDon);
                statementCT.executeUpdate();
                statementHD.setInt(1, idHoaDon);
                statementHD.executeUpdate();
                con.commit();
            } catch (SQLException ex) {
                con.rollback();
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi khi hủy hóa đơn: " + ex.getMessage());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi kết nối cơ sở dữ liệu: " + ex.getMessage());
        }

        DefaultTableModel dtmHoaDon = (DefaultTableModel) tblHoaDonCho.getModel();
        int selectedRow = tblHoaDonCho.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn hủy đơn hàng?", "Xác nhận hủy đơn hàng", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dtmHoaDon.removeRow(selectedRow);
                txtTenKhachHang.setText("");
                txtIDGG.setText("");
                txtSdt.setText("");
                txtSoTienGiam.setText("");
                txtGiaTriGiam.setText("");
                txtTenMa.setText("");
                txtTongTien.setText("");
                txtTienKhachDua.setText("");
                txtTienThua.setText("");
                txtTenHD.setText("");

                JOptionPane.showMessageDialog(null, "Hóa đơn đã được hủy.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để hủy.");
        }
    }

    private void fillNhanVienComboBox() {
        DefaultComboBoxModel<String> thModel = nhanVienService.getAllTenNV();
        cboTenNV.setModel(thModel);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        txtTenKhachHang = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTenHD = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cboTenNV = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtTienThua = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtIDGG = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtTenMa = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtGiaTriGiam = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtSoTienGiam = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        cboHTTT = new javax.swing.JComboBox<>();
        btnThanhToan = new javax.swing.JButton();
        btnApDung = new javax.swing.JButton();
        btnLamMoiMa = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDonCho = new javax.swing.JTable();
        btnTaoMoiHoaDonCho = new javax.swing.JButton();
        btnXoaHoaDonCho = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHDCT = new javax.swing.JTable();

        jPanel1.setToolTipText("Hóa Đơn Chờ");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "THÔNG TIN CHI TIẾT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 19))); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "THÔNG TIN KHÁCH HÀNG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 14))); // NOI18N
        jPanel6.setToolTipText("");
        jPanel6.setName(""); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel6.setText("TÊN KHÁCH HÀNG");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel7.setText("SỐ ĐIỆN THOẠI");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(95, 95, 95))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel8.setText("TÊN HÓA ĐƠN");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel9.setText("TÊN NHÂN VIÊN");

        cboTenNV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel10.setText("TỔNG TIỀN");

        txtTongTien.setEnabled(false);

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel11.setText("TIỀN KHÁCH ĐƯA");

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel12.setText("TIỀN THỪA");

        txtTienThua.setEnabled(false);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel13.setText("MÃ GIẢM GIÁ ");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel14.setText("TÊN MÃ GIẢM GIÁ");

        txtTenMa.setEnabled(false);

        jLabel15.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel15.setText("GIÁ TRỊ GIẢM");

        txtGiaTriGiam.setEnabled(false);

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel16.setText("SỐ TIỀN GIẢM");

        txtSoTienGiam.setEnabled(false);

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel17.setText("HÌNH THỨC THANH TOÁN");

        cboHTTT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnThanhToan.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnThanhToan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pro1041/com/icon/expense_4025718.png"))); // NOI18N
        btnThanhToan.setText("THANH TOÁN");

        btnApDung.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pro1041/com/icon/icons8-tick-20.png"))); // NOI18N
        btnApDung.setText("ÁP DỤNG");
        btnApDung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApDungActionPerformed(evt);
            }
        });

        btnLamMoiMa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pro1041/com/icon/icons8-captcha-16.png"))); // NOI18N
        btnLamMoiMa.setText("LÀM MỚI");
        btnLamMoiMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiMaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cboTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addGap(18, 18, 18)
                                .addComponent(txtTenMa, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnApDung))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtTienKhachDua))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(40, 40, 40)
                                        .addComponent(txtTienThua))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtIDGG, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(btnLamMoiMa))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtTongTien))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtTenHD, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addGap(18, 18, 18)
                                .addComponent(txtGiaTriGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(18, 18, 18)
                                .addComponent(txtSoTienGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addGap(18, 18, 18)
                                .addComponent(cboHTTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(btnThanhToan)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtTenHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(cboTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtTienThua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtIDGG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoiMa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtTenMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnApDung))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtGiaTriGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(txtSoTienGiam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cboHTTT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnThanhToan)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HÓA ĐƠN CHỜ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        tblHoaDonCho.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID HÓA ĐƠN ", "TÊN HÓA ĐƠN", "TRẠNG THÁI", "NGÀY TẠO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblHoaDonCho.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonChoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDonCho);
        if (tblHoaDonCho.getColumnModel().getColumnCount() > 0) {
            tblHoaDonCho.getColumnModel().getColumn(0).setResizable(false);
        }

        btnTaoMoiHoaDonCho.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnTaoMoiHoaDonCho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pro1041/com/icon/expense_4025718.png"))); // NOI18N
        btnTaoMoiHoaDonCho.setText("TẠO HÓA ĐƠN MỚI");
        btnTaoMoiHoaDonCho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoMoiHoaDonChoActionPerformed(evt);
            }
        });

        btnXoaHoaDonCho.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        btnXoaHoaDonCho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pro1041/com/icon/trash-bin_5055247.png"))); // NOI18N
        btnXoaHoaDonCho.setText("HỦY HÓA ĐƠN CHỜ");
        btnXoaHoaDonCho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHoaDonChoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 653, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXoaHoaDonCho)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTaoMoiHoaDonCho)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXoaHoaDonCho)
                    .addComponent(btnTaoMoiHoaDonCho))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "SẢN PHẨM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID SẢN PHẨM", "TÊN SẢN PHẨM ", "SỐ LƯỢNG", "ĐƠN GIÁ ", "THƯƠNG HIỆU ", "NHÀ SẢN XUẤT", "CHẤT LIỆU", "KÍCH THƯỚC", "LOẠI KHÓA", "KIỂU DÁNG", "MÀU SẮC"
            }
        ));
        jScrollPane2.setViewportView(tblSanPham);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HÓA ĐƠN CHI TIẾT", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Times New Roman", 1, 18))); // NOI18N

        tblHDCT.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "MÃ HÓA ĐƠN", "TÊN SẢN PHẨM", "SỐ LƯỢNG", "ĐƠN GIÁ", "TỔNG TIỀN", "NGÀY TẠO HÓA ĐƠN"
            }
        ));
        jScrollPane3.setViewportView(tblHDCT);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoMoiHoaDonChoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoMoiHoaDonChoActionPerformed
        // TODO add your handling code here:
        boolean trangThai = true;
        String tenHD = txtTenHD.getText().trim();
        int id = themHoaDonVaoDatabase(tenHD, trangThai);
        if (id != -1) {
            xoaHoaDonDaThanhToan(id);
            loadHoadon();
        }
    }//GEN-LAST:event_btnTaoMoiHoaDonChoActionPerformed

    private void btnXoaHoaDonChoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHoaDonChoActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblHoaDonCho.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn hủy đơn hàng?", "Xác nhận hủy đơn hàng", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) tblHoaDonCho.getValueAt(selectedRow, 0);
                huyHoaDon(id);
                txtTenKhachHang.setText("");
                txtIDGG.setText("");
                txtSdt.setText("");
                txtSoTienGiam.setText("");
                txtGiaTriGiam.setText("");
                txtTenMa.setText("");
                txtTongTien.setText("");
                txtTienKhachDua.setText("");
                txtTienThua.setText("");
                txtTenHD.setText("");

            }
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hóa đơn để hủy.");
        }
    }//GEN-LAST:event_btnXoaHoaDonChoActionPerformed

    private void btnApDungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApDungActionPerformed
        // TODO add your handling code here:
        int idgg = Integer.parseInt(txtIDGG.getText());
        String tienKhachDuaStr = txtTienKhachDua.getText();
        KhuyenMaiService khuyenMaiService = new KhuyenMaiService();
        KhuyenMai km = khuyenMaiService.getKhuyenMaiByID(idgg);
        if (km != null) {
            hienThiThongTinKhuyenMai(km);
            double giaTriGiam = km.getGiaTri();
            double tongTien = tinhTongTienGioHang();
            double tienGiam = (tongTien * giaTriGiam / 100);
            double tongTienSauGiam = tongTien - tienGiam;
            txtSoTienGiam.setText(String.format("%.2f", tienGiam));
            txtTongTien.setText(String.format("%.2f", tongTienSauGiam));
            try {
                double tienKhachDua = Double.parseDouble(tienKhachDuaStr);
                double tienThua = tienKhachDua - tongTienSauGiam;
                txtTienThua.setText(String.format("%.2f", tienThua));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Số tiền khách đưa không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE); // Handle invalid input
            }
        } else {
            JOptionPane.showMessageDialog(null, "Mã khuyến mãi không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE); // Handle invalid promotion code
        }
    }//GEN-LAST:event_btnApDungActionPerformed

    private void btnLamMoiMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiMaActionPerformed
        txtIDGG.setText("");
        txtGiaTriGiam.setText("");
        txtTenMa.setText("");
        txtSoTienGiam.setText("");
    }//GEN-LAST:event_btnLamMoiMaActionPerformed

    private void tblHoaDonChoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonChoMouseClicked
        // TODO add your handling code here:
        int index = tblHoaDonCho.getSelectedRow();
        if (index != -1) {
            int id = Integer.parseInt(tblHoaDonCho.getValueAt(index, 0).toString());
            List<HoaDon> dsct = hoaDonService.getById(id);
            showTable(dsct);
        }
        String maHoaDon = tblHoaDonCho.getValueAt(index, 1).toString();
        txtTenHD.setText(maHoaDon);
    }//GEN-LAST:event_tblHoaDonChoMouseClicked

    void showTable(List<HoaDon> list) {
        dtm = (DefaultTableModel) tblHDCT.getModel();
        dtm.setRowCount(0);
        for (HoaDon hdct : list) {
            dtm.addRow(new Object[]{
                hdct.getMaHDCT(),
                hdct.getTenSanPham(),
                hdct.getSoLuong(),
                hdct.getGia(),
                hdct.getTongTien(),
                hdct.getNgayTaoHD(),});
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApDung;
    private javax.swing.JButton btnLamMoiMa;
    private javax.swing.JButton btnTaoMoiHoaDonCho;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnXoaHoaDonCho;
    private javax.swing.JComboBox<String> cboHTTT;
    private javax.swing.JComboBox<String> cboTenNV;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblHDCT;
    private javax.swing.JTable tblHoaDonCho;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtGiaTriGiam;
    private javax.swing.JTextField txtIDGG;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtSoTienGiam;
    private javax.swing.JTextField txtTenHD;
    private javax.swing.JTextField txtTenKhachHang;
    private javax.swing.JTextField txtTenMa;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTienThua;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
