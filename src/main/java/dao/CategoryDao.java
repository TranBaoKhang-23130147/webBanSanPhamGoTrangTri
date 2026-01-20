package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import dao.DBContext;

public class CategoryDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. Lấy tất cả danh mục để hiển thị
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories"; // Tên bảng trong DB của bạn
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("category_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm danh mục mới
    public boolean insertCategory(String name) {
        String sql = "INSERT INTO categories (category_name) VALUES (?)";
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
    // Tìm kiếm danh mục theo tên
    public List<Category> searchCategoryByName(String keyword) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories WHERE category_name LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%"); // Tìm kiếm theo kiểu chứa từ khóa
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("category_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lỗi thường gặp: Danh mục đang được dùng bởi sản phẩm khác
            System.out.println("Lỗi xóa danh mục: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Category> getAllCategoriesWithTotalInventory(String keyword) {
        List<Category> list = new ArrayList<>();

        String sql = """
        SELECT 
            c.id,
            c.category_name,
            COALESCE(SUM(pv.inventory_quantity), 0) AS total_inventory
        FROM categories c
        LEFT JOIN products p ON p.category_id = c.id
        LEFT JOIN product_variants pv ON pv.product_id = p.id
        WHERE (? IS NULL OR c.category_name LIKE ?)
        GROUP BY c.id, c.category_name
        ORDER BY c.id
    """;

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {


            if (keyword == null || keyword.trim().isEmpty()) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
            } else {
                String searchPattern = "%" + keyword.trim() + "%";
                ps.setString(1, searchPattern);
                ps.setString(2, searchPattern);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setCategoryName(rs.getString("category_name"));
                c.setTotalInventory(rs.getLong("total_inventory"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // hoặc throw exception tùy dự án
        }
        return list;
    }
}