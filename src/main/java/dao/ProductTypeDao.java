package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ProductType;

public class ProductTypeDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. Lấy tất cả loại sản phẩm
    public List<ProductType> getAllProductType() {
        List<ProductType> list = new ArrayList<>();
        String sql = "SELECT * FROM product_types"; // Thay bằng tên bảng thực tế của bạn
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductType pt = new ProductType();
                pt.setId(rs.getInt("id"));
                pt.setProductTypeName(rs.getString("product_type_name"));
                pt.setCategoryId(rs.getInt("category_id"));
                list.add(pt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tìm kiếm theo tên
    public List<ProductType> searchProductTypeByName(String keyword) {
        List<ProductType> list = new ArrayList<>();
        String sql = "SELECT * FROM product_types WHERE product_type_name LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                ProductType pt = new ProductType();
                pt.setId(rs.getInt("id"));
                pt.setProductTypeName(rs.getString("product_type_name"));
                pt.setCategoryId(rs.getInt("category_id"));
                list.add(pt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Thêm loại sản phẩm mới
    public boolean insertProductType(String name, int categoryId) {
        String sql = "INSERT INTO product_types (product_type_name, category_id) VALUES (?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setInt(2, categoryId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}