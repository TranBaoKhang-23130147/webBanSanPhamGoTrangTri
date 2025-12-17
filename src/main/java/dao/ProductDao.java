package dao;

import java.sql.*;
import model.Product;

public class ProductDao {
    Connection conn = null;
    PreparedStatement ps = null;

    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO products (name_product, description, category_id, source_id, " +
                "product_type_id, price, primary_image_id, isActive, mfg_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
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
}