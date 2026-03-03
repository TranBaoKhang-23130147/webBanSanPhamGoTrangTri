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
    public boolean updateProductType(int id, String name) {
        String sql = "UPDATE product_types SET product_type_name = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<ProductType> getAllProductTypesWithTotalInventory(String keyword) {
        List<ProductType> list = new ArrayList<>();

        String sql = """
        SELECT 
            pt.id,
            pt.product_type_name,
            COALESCE(SUM(pv.inventory_quantity), 0) AS total_inventory
        FROM product_types pt
        LEFT JOIN products p ON p.product_type_id = pt.id
        LEFT JOIN product_variants pv ON pv.product_id = p.id
        WHERE (? IS NULL OR pt.product_type_name LIKE ?)
        GROUP BY pt.id, pt.product_type_name
        ORDER BY pt.id
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
                ProductType pt = new ProductType();
                pt.setId(rs.getInt("id"));
                pt.setProductTypeName(rs.getString("product_type_name"));
                pt.setTotalInventory(rs.getLong("total_inventory"));
                list.add(pt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }
    public List<Integer> getProductIdsInOrder(int orderId) {
        List<Integer> list = new ArrayList<>();

        String sql = """
        SELECT DISTINCT pv.product_id
        FROM order_detail od
        JOIN product_variant pv ON od.variant_id = pv.id
        WHERE od.order_id = ?
    """;

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(rs.getInt("product_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}