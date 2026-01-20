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
                list.add(new Source(rs.getInt("id"), rs.getString("sourcename")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm nhà cung cấp mới
    public boolean insertSource(String name) {
        String sql = "INSERT INTO sources (sourcename) VALUES (?)";
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
                list.add(new Source(rs.getInt("id"), rs.getString("sourcename")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean deleteSource(int id) {
        String sql = "DELETE FROM sources WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Cập nhật nhà cung cấp (Dùng cho chức năng Edit)
    public boolean updateSource(int id, String name) {
        String sql = "UPDATE sources SET sourcename = ? WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Source> getAllSourcesWithTotalInventory(String keyword) {
        List<Source> list = new ArrayList<>();

        String sql = """
        SELECT 
            s.id,
            s.sourcename,
            COALESCE(SUM(pv.inventory_quantity), 0) AS total_inventory
        FROM sources s
        LEFT JOIN products p ON p.source_id = s.id
        LEFT JOIN product_variants pv ON pv.product_id = p.id
        WHERE (? IS NULL OR s.sourcename LIKE ?)
        GROUP BY s.id, s.sourcename
        ORDER BY s.id
    """;

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (keyword == null || keyword.trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
            } else {
                String pattern = "%" + keyword.trim() + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Source s = new Source();
                s.setId(rs.getInt("id"));
                s.setSourceName(rs.getString("sourcename"));
                s.setTotalInventory(rs.getLong("total_inventory"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}