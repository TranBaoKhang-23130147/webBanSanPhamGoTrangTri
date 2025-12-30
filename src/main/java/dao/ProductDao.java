package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ProductDao {

    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO products (name_product, description, category_id, source_id, "
                + "product_type_id, price, primary_image_id, isActive, mfg_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNameProduct());
            ps.setString(2, p.getDescription());
            ps.setInt(3, p.getCategoryId());
            ps.setInt(4, p.getSourceId());
            ps.setInt(5, p.getProductTypeId());
            ps.setDouble(6, p.getPrice());
            ps.setInt(7, p.getPrimaryImageId());
            ps.setInt(8, p.getIsActive());
            ps.setDate(9, p.getMfgDate());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ĐÃ SỬA: Thêm p.isActive vào SELECT và GROUP BY, HOẶC xóa dòng setIsActive (ở đây mình chọn cách thêm để an toàn)
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        // Sử dụng COALESCE để tránh lỗi NULL khi chưa có review
        String sql = """
        SELECT
            p.id,
            p.name_product,
            p.price,
            p.isActive,
            i.urlImage,
            COALESCE(AVG(r.rate), 0) AS avgRating
        FROM products p
        LEFT JOIN images i ON p.primary_image_id = i.id
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.isActive = 1
        GROUP BY p.id, p.name_product, p.price, p.isActive, i.urlImage
        """;

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setIsActive(rs.getInt("isActive"));
                p.setImageUrl(rs.getString("urlImage")); // Sẽ lấy được "img/products/..."
                p.setAverageRating(rs.getDouble("avgRating"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // Phương thức searchProducts giữ nguyên vì đã SELECT đầy đủ các field cần thiết
    public List<Product> searchProducts(String txtSearch, String category) {
        List<Product> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("""
            SELECT
               p.id,
               p.name_product,
               p.description,
               p.category_id,
               p.source_id,
               p.product_type_id,
               p.price,
               p.primary_image_id,
               p.isActive,
               p.mfg_date,
               i.urlImage,
               COALESCE(AVG(r.rate), 0) AS avgRating
            FROM products p
            LEFT JOIN images i ON p.primary_image_id = i.id
            LEFT JOIN reviews r ON p.id = r.product_id
            WHERE p.name_product LIKE ?
            """);

        if (category != null && !category.equals("all") && !category.isBlank()) {
            sb.append(" AND p.category_id = ?");
        }

        sb.append(" GROUP BY p.id, p.name_product, p.description, p.category_id, p.source_id, p.product_type_id, p.price, p.primary_image_id, p.isActive, p.mfg_date, i.urlImage");

        String query = sb.toString();

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            int idx = 1;
            ps.setString(idx++, "%" + txtSearch + "%");

            if (category != null && !category.equals("all") && !category.isBlank()) {
                ps.setInt(idx++, Integer.parseInt(category));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setNameProduct(rs.getString("name_product"));
                    p.setDescription(rs.getString("description"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setSourceId(rs.getInt("source_id"));
                    p.setProductTypeId(rs.getInt("product_type_id"));
                    p.setPrice(rs.getDouble("price"));
                    p.setPrimaryImageId(rs.getInt("primary_image_id"));
                    p.setIsActive(rs.getInt("isActive"));
                    p.setMfgDate(rs.getDate("mfg_date"));
                    p.setImageUrl(rs.getString("urlImage"));
                    p.setAverageRating(rs.getDouble("avgRating"));
                    list.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}