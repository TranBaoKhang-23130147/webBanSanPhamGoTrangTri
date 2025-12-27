package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Product;

public class ProductDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

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

//
////    hien thi sp
//public List<Product> getAllProducts() {
//    List<Product> list = new ArrayList<>();
//    // Truy vấn JOIN: p.primary_image_id khớp với i.id để lấy i.urlImage
//    String query = "SELECT p.id, p.name_product, p.price, i.urlImage, p.isActive " +
//            "FROM products p " +
//            "LEFT JOIN images i ON p.primary_image_id = i.id " +
//            "WHERE p.isActive = 1";
//    try {
//        conn = new DBContext().getConnection();
//        ps = conn.prepareStatement(query);
//        rs = ps.executeQuery();
//        while (rs.next()) {
//            list.add(new Product(
//                    rs.getInt("id"),
//                    rs.getString("name_product"),
//                    rs.getDouble("price"),
//                    rs.getString("image_url"),
//                    rs.getInt("isActive")
//            ));
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//    return list;
//}

public List<Product> getAllProducts() {
    List<Product> list = new ArrayList<>();
    String sql = "SELECT * FROM products";

    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name_product"),
                    rs.getDouble("price"),
                    rs.getString("image_url"),
                    rs.getInt("rating")
            );
            list.add(p);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
// tim kim sp
public List<Product> searchProducts(String txtSearch, String category) {
    List<Product> list = new ArrayList<>();
    // Đảm bảo tên cột trong SQL khớp với tên cột trong Database của bạn
    String query = "SELECT * FROM Product WHERE nameProduct LIKE ?";

    if (category != null && !category.equals("all")) {
        query += " AND categoryId = ?"; // Giả sử bạn lọc theo ID của category
    }

    try {
        conn = new DBContext().getConnection();
        ps = conn.prepareStatement(query);
        ps.setString(1, "%" + txtSearch + "%");

        if (category != null && !category.equals("all")) {
            ps.setInt(2, Integer.parseInt(category)); // Chuyển category sang Int nếu nó là ID
        }

        rs = ps.executeQuery();
        while (rs.next()) {
            Product p = new Product();
            // Gán dữ liệu dựa trên các hàm Setter bạn đã cung cấp
            p.setId(rs.getInt("id"));
            p.setNameProduct(rs.getString("nameProduct"));
            p.setDescription(rs.getString("description"));
            p.setCategoryId(rs.getInt("categoryId"));
            p.setSourceId(rs.getInt("sourceId"));
            p.setProductTypeId(rs.getInt("productTypeId"));
            p.setPrice(rs.getDouble("price"));
            p.setPrimaryImageId(rs.getInt("primaryImageId"));
            p.setIsActive(rs.getInt("isActive"));
            p.setMfgDate(rs.getDate("mfgDate"));
            p.setImageUrl(rs.getString("imageUrl")); // Trường này bạn vừa thêm

            // Nếu trong JSP bạn có dùng ${p.rating}, hãy đảm bảo có cột rating trong DB
            // và thêm p.setRating(rs.getInt("rating")) nếu class Product có thuộc tính này.

            list.add(p);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}
}