package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductImageDao {

    private int insertImage(Connection conn, String url) throws Exception {
        String sql = "INSERT INTO images (urlImage) VALUES (?)";
        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, url);
        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }
    private void insertProductImage(Connection conn, int productId, int imageId) throws Exception {
        String sql = "INSERT INTO product_images (product_id, image_id) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, productId);
        ps.setInt(2, imageId);
        ps.executeUpdate();
    }
    private void updatePrimaryImage(Connection conn, int productId, int imageId) throws Exception {
        String sql = "UPDATE products SET primary_image_id = ? WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, imageId);
        ps.setInt(2, productId);
        ps.executeUpdate();
    }

}
