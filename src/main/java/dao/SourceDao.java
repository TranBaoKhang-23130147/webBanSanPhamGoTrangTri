package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Source;

public class SourceDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. Lấy tất cả nhà cung cấp
    public List<Source> getAllSources() {
        List<Source> list = new ArrayList<>();
        String sql = "SELECT * FROM sources";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Source(rs.getInt("id"), rs.getString("source_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm nhà cung cấp mới
    public boolean insertSource(String name) {
        String sql = "INSERT INTO sources (source_name) VALUES (?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Tìm kiếm nhà cung cấp theo tên
    public List<Source> searchSourceByName(String keyword) {
        List<Source> list = new ArrayList<>();
        String sql = "SELECT * FROM sources WHERE source_name LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Source(rs.getInt("id"), rs.getString("source_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2
}