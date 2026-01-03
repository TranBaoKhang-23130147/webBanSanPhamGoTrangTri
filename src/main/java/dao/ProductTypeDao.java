package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ProductType;

import static dao.DBContext.getConnection;

public class ProductTypeDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<ProductType> getAllProductType() {
        List<ProductType> list = new ArrayList<>();
        String sql = "SELECT * FROM product_types";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ProductType t = new ProductType();
                t.setId(rs.getInt("id"));
                t.setProductTypeName(rs.getString("product_type_name"));
                list.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

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
                list.add(pt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insertProductType(String name) {
        // Chỉ insert tên loại sản phẩm
        String sql = "INSERT INTO product_types (product_type_name) VALUES (?)";
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
    public boolean deleteProductType(int id) {
        String sql = "DELETE FROM product_types WHERE id = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lỗi 1451 thường là lỗi vi phạm ràng buộc khóa ngoại (không thể xóa)
            System.out.println("Lỗi xóa: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}