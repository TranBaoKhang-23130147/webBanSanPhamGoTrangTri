package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Images;
import model.Product;
import model.Reviews;
import java.sql.*;
import model.*;

public class ProductDao {

    /**
     * Lấy tất cả sản phẩm đang hoạt động kèm theo ảnh và đánh giá trung bình
     */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = """
            
                SELECT p.id, p.name_product, p.price, p.isActive,\s
                -- Logic: Nếu primary_image_id có thì lấy, nếu không thì lấy ảnh đầu tiên trong bảng images
                COALESCE(i.urlImage, (SELECT urlImage FROM images WHERE product_id = p.id LIMIT 1)) AS urlImage,\s
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
                p.setImageUrl(rs.getString("urlImage"));
                p.setAverageRating(rs.getDouble("avgRating"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Tìm kiếm sản phẩm nâng cao - JOIN 3 bảng để lấy thông tin chi tiết
     */
    public List<Product> searchProducts(String txtSearch, String category) {
        List<Product> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        // SQL sử dụng Double JOIN để lấy dữ liệu từ bảng descriptions và informations
        sb.append("""
            SELECT 
                p.*, 
                d.introduce, d.highlights, 
                inf.material, inf.color, inf.size, inf.guarantee,
                img.urlImage, 
                COALESCE(AVG(r.rate), 0) AS avgRating
            FROM products p
            LEFT JOIN descriptions d ON p.description_id = d.id
            LEFT JOIN informations inf ON d.information_id = inf.id
            LEFT JOIN images img ON p.primary_image_id = img.id
            LEFT JOIN reviews r ON p.id = r.product_id
            WHERE p.name_product LIKE ?
            """);

        if (category != null && !category.equals("all") && !category.isBlank()) {
            sb.append(" AND p.category_id = ?");
        }

        // Group by tất cả các cột cần thiết khi dùng hàm tổng hợp AVG
        sb.append("""
             GROUP BY p.id, d.id, inf.id, img.urlImage
            """);

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {

            ps.setString(1, "%" + txtSearch + "%");
            if (category != null && !category.equals("all") && !category.isBlank()) {
                ps.setInt(2, Integer.parseInt(category));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setNameProduct(rs.getString("name_product"));

                    // description_id bây giờ là int (khóa ngoại)
                    p.setDescription(rs.getInt("description_id"));

                    p.setCategoryId(rs.getInt("category_id"));
                    p.setSourceId(rs.getInt("source_id"));
                    p.setProductTypeId(rs.getInt("product_type_id"));
                    p.setPrice(rs.getDouble("price"));
                    p.setPrimaryImageId(rs.getInt("primary_image_id"));
                    p.setIsActive(rs.getInt("isActive"));
                    p.setMfgDate(rs.getDate("mfg_date"));
                    p.setImageUrl(rs.getString("urlImage"));
                    p.setAverageRating(rs.getDouble("avgRating"));

                    // Bạn có thể set thêm các field introduce/material nếu Model Product có hỗ trợ
                    list.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Hàm Insert mới sử dụng ID (Khóa ngoại)
     */
    public boolean insertProduct(Product p) {
        String sql = """
            INSERT INTO products (name_product, description_id, category_id, source_id, 
            product_type_id, price, primary_image_id, isActive, mfg_date) 
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNameProduct());
            ps.setInt(2, p.getDescriptionID()); // Đã đổi sang setInt cho description_id
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
    // Trong class ProductDao, thêm/sửa phương thức getProductById

    // 1. Lấy chi tiết 1 sản phẩm (JOIN tất cả các bảng liên quan: Source, Description, Information)
    public Product getProductById(int id) {
        String sql = """
        SELECT 
            p.*, 
            s.sourceName, 
            i.urlImage, 
            d.introduce, d.highlights, 
            inf.material, inf.color, inf.size, inf.guarantee
        FROM products p
        LEFT JOIN sources s ON p.source_id = s.id
        LEFT JOIN images i ON p.primary_image_id = i.id
        LEFT JOIN descriptions d ON p.description_id = d.id
        LEFT JOIN informations inf ON d.information_id = inf.id
        WHERE p.id = ?
    """;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setNameProduct(rs.getString("name_product"));
                    p.setPrice(rs.getDouble("price"));
                    p.setMfgDate(rs.getDate("mfg_date"));
                    p.setImageUrl(rs.getString("urlImage"));

                    // Mapping Source
                    Source source = new Source();
                    source.setSourceName(rs.getString("sourceName"));
                    p.setSource(source);

                    // Mapping Information
                    Information info = new Information();
                    info.setMaterial(rs.getString("material"));
                    info.setSize(rs.getString("size"));
                    info.setColor(rs.getString("color"));
                    info.setGuarantee(rs.getString("guarantee"));

                    // Mapping Description
                    Description desc = new Description();
                    desc.setIntroduce(rs.getString("introduce"));
                    desc.setHighlights(rs.getString("highlights"));
                    desc.setInformation(info); // Đảm bảo Model Description có setter này
                    p.setDetailDescription(desc);

                    return p;
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tại getProductById: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // 2. Lấy danh sách ảnh biến thể cho Gallery
    public List<Images> getProductImages(int productId) {
        List<Images> list = new ArrayList<>();
        // Phải JOIN qua bảng trung gian product_image mà bạn đã chụp
        String sql = """
        SELECT i.id, i.urlImage 
        FROM images i 
        JOIN product_images pi ON i.id = pi.image_id 
        WHERE pi.product_id = ?
    """;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Images(rs.getInt("id"), rs.getString("urlImage")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Lấy danh sách biến thể (Màu sắc & Kích thước) kèm tên từ bảng Color/Size
    // 3. Lấy danh sách biến thể (Màu sắc & Kích thước) kèm tên và MÃ MÀU
    public List<ProductVariants> getProductVariants(int productId) {
        List<ProductVariants> list = new ArrayList<>();
        String sql = """
        SELECT pv.*, c.colorName, c.color_code, s.size_name  -- THÊM c.color_code VÀO ĐÂY
        FROM product_variants pv
        LEFT JOIN colors c ON pv.color_id = c.id
        LEFT JOIN sizes s ON pv.size_id = s.id
        WHERE pv.product_id = ?
    """;
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ProductVariants v = new ProductVariants();
                    v.setId(rs.getInt("id"));
                    v.setVariant_price(rs.getBigDecimal("variant_price"));

                    // Khởi tạo đối tượng Color và gán giá trị
                    ProductColor c = new ProductColor();
                    c.setId(rs.getInt("color_id"));
                    c.setColorName(rs.getString("colorName"));

                    // --- DÒNG THÊM MỚI ---
                    c.setColorCode(rs.getString("color_code")); // Lấy mã màu từ cột mới trong DB
                    // ---------------------

                    v.setColor(c);

                    // Khởi tạo đối tượng Size
                    ProductSize sz = new ProductSize();
                    sz.setId(rs.getInt("size_id"));
                    sz.setSize_name(rs.getString("size_name"));
                    v.setSize(sz);

                    list.add(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 4. Lấy danh sách đánh giá của sản phẩm
    public List<Reviews> getProductReviews(int productId) {
        List<Reviews> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE product_id = ? ORDER BY id DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reviews r = new Reviews();
                    r.setUserId(rs.getInt("user_id")); // Nhớ lấy thêm user_id
                    r.setRating(rs.getInt("rate"));    // Khớp với rs.getInt("rate")
                    r.setComment(rs.getString("content"));
                    list.add(r);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

}