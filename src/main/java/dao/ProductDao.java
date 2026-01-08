package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Images;
import model.Product;
import model.Reviews;
import java.sql.*;
import model.*;

import static dao.DBContext.getConnection;

public class ProductDao {

    /**
     * Lấy tất cả sản phẩm đang hoạt động kèm theo ảnh và đánh giá trung bình
     */
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = """
            SELECT 
                p.id, p.name_product, p.price, p.isActive, 
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
    public List<Product> searchProducts(String txtSearch, Integer categoryId) {
        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            p.id,
            p.name_product,
            p.price,
            p.category_id,
            p.primary_image_id,
            img.urlImage,
            COALESCE(AVG(r.rate), 0) AS avgRating
        FROM products p
        LEFT JOIN images img ON p.primary_image_id = img.id
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.isActive = 1
          AND p.name_product LIKE ?
    """);

        if (categoryId != null) {
            sql.append(" AND p.category_id = ?");
        }

        sql.append(" GROUP BY p.id, img.urlImage ");

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, "%" + txtSearch + "%");
            if (categoryId != null) {
                ps.setInt(2, categoryId);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setCategoryId(rs.getInt("category_id"));
                p.setPrimaryImageId(rs.getInt("primary_image_id"));
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
        inf.material, inf.color, inf.size, inf.guarantee,
        /* THÊM 2 DÒNG NÀY VÀO SQL */
        (SELECT AVG(rate) FROM reviews WHERE product_id = p.id) AS avgRating,
        (SELECT COUNT(id) FROM reviews WHERE product_id = p.id) AS totalReviews
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
// Trong ProductDao.java
    public List<Reviews> getProductReviews(int productId) {
        List<Reviews> list = new ArrayList<>();
        // Câu lệnh SQL lấy tất cả đánh giá của 1 sản phẩm
        String sql = "SELECT * FROM reviews WHERE product_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reviews r = new Reviews();

                    // 1. Lấy các thông tin cơ bản
                    r.setId(rs.getInt("id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setComment(rs.getString("comment"));

                    // 2. Ánh xạ từ cột "rate" trong DB vào thuộc tính "rating" của Model
                    r.setRating(rs.getInt("rate"));

                    // 3. Ánh xạ từ cột "createAt" trong DB vào thuộc tính "createAt" của Model
                    r.setCreateAt(rs.getTimestamp("createAt"));

                    list.add(r);
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi tại getProductReviews: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public List<Product> filterProducts(String[] types,
                                        String[] prices,
                                        String[] ratings,
                                        Integer categoryId) {

        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name_product, p.price, img.urlImage, " +
                        "IFNULL(AVG(r.rate),0) AS avg_rate " +
                        "FROM products p " +
                        "LEFT JOIN images img ON p.primary_image_id = img.id " +
                        "LEFT JOIN reviews r ON p.id = r.product_id " +
                        "WHERE p.isActive = 1 "
        );

        /* 🔥 LỌC CATEGORY */
        if (categoryId != null) {
            sql.append(" AND p.category_id = ").append(categoryId);
        }

        /* LỌC LOẠI */
        if (types != null && types.length > 0) {
            sql.append(" AND p.product_type_id IN (")
                    .append(String.join(",", types))
                    .append(")");
        }

        /* LỌC GIÁ */
        if (prices != null && prices.length > 0) {
            List<String> cond = new ArrayList<>();
            for (String p : prices) {
                switch (p) {
                    case "1": cond.add("p.price < 1000000"); break;
                    case "2": cond.add("p.price BETWEEN 1000000 AND 3000000"); break;
                    case "3": cond.add("p.price BETWEEN 3000000 AND 5000000"); break;
                    case "4": cond.add("p.price BETWEEN 5000000 AND 10000000"); break;
                    case "5": cond.add("p.price > 10000000"); break;
                }
            }
            if (!cond.isEmpty()) {
                sql.append(" AND (").append(String.join(" OR ", cond)).append(")");
            }
        }

        sql.append(" GROUP BY p.id ");

        /* LỌC ĐÁNH GIÁ */
        if (ratings != null && ratings.length > 0) {
            int minRate = Arrays.stream(ratings)
                    .mapToInt(Integer::parseInt)
                    .min()
                    .getAsInt();
            sql.append(" HAVING avg_rate >= ").append(minRate);
        }




        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql.toString())) {

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name_product"),
                        rs.getDouble("price"),
                        rs.getString("urlImage"),
                        rs.getDouble("avg_rate")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Integer getCategoryIdByName(String key) {
        String sql = """
    SELECT id
    FROM categories
    WHERE LOWER(REPLACE(REPLACE(category_name,'Đ','D'),'đ','d'))
          LIKE ?
    LIMIT 1
    """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String name = key.replace("-", " ").toLowerCase();
            ps.setString(1, "%" + name + "%");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();
        String sql = """
        SELECT p.id, p.name_product, p.price, img.urlImage,
               IFNULL(AVG(r.rate),0) AS avg_rate
        FROM products p
        LEFT JOIN images img ON p.primary_image_id = img.id
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.isActive = 1 AND p.category_id = ?
        GROUP BY p.id
    """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name_product"),
                        rs.getDouble("price"),
                        rs.getString("urlImage"),
                        rs.getDouble("avg_rate")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public String getUsernameById(int userId) {
        String name = "Khách hàng ẩn danh";
        // Nếu trong DB cột tên là 'full_name' thì chọn 'full_name'
        String sql = "SELECT full_name FROM users WHERE id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // SỬA TẠI ĐÂY: Phải lấy đúng cột 'full_name' đã chọn ở trên
                    name = rs.getString("full_name");
                }
            }
        } catch (Exception e) {
            System.out.println("Lỗi getUsernameById: " + e.getMessage());
            e.printStackTrace();
        }
        return name;
    }
    public List<Product> getProductsByPage(int page, int pageSize) {
        List<Product> list = new ArrayList<>();

        String sql = """
        SELECT 
            p.id, p.name_product, p.price, p.isActive,
            img.urlImage,
            COALESCE(AVG(r.rate),0) AS avgRating
        FROM products p
        LEFT JOIN images img ON p.primary_image_id = img.id
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.isActive = 1
        GROUP BY p.id
        LIMIT ? OFFSET ?
    """;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name_product"),
                        rs.getDouble("price"),
                        rs.getString("urlImage"),
                        rs.getDouble("avgRating")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public int countAllProducts() {
        String sql = "SELECT COUNT(*) FROM products WHERE isActive = 1";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int countFilterProducts(String[] types,
                                   String[] prices,
                                   String[] ratings,
                                   Integer categoryId) {

        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT p.id) " +
                        "FROM products p " +
                        "LEFT JOIN reviews r ON p.id = r.product_id " +
                        "WHERE p.isActive = 1 "
        );

        if (categoryId != null) {
            sql.append(" AND p.category_id = ").append(categoryId);
        }

        if (types != null && types.length > 0) {
            sql.append(" AND p.product_type_id IN (")
                    .append(String.join(",", types)).append(")");
        }

        if (prices != null && prices.length > 0) {
            sql.append(" AND (");
            for (int i = 0; i < prices.length; i++) {
                if (i > 0) sql.append(" OR ");
                switch (prices[i]) {
                    case "1": sql.append("p.price < 1000000"); break;
                    case "2": sql.append("p.price BETWEEN 1000000 AND 3000000"); break;
                    case "3": sql.append("p.price BETWEEN 3000000 AND 5000000"); break;
                    case "4": sql.append("p.price BETWEEN 5000000 AND 10000000"); break;
                    case "5": sql.append("p.price > 10000000"); break;
                }
            }
            sql.append(")");
        }

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql.toString())) {

            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Product> getProductsByCategoryPaging(
            int categoryId, int page, int pageSize) {

        List<Product> list = new ArrayList<>();
        String sql =
                "SELECT p.id, p.name_product, p.price, img.urlImage " +
                        "FROM products p " +
                        "LEFT JOIN images img ON p.primary_image_id = img.id " +
                        "WHERE p.category_id = ? AND p.isActive = 1 " +
                        "LIMIT ? OFFSET ?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, pageSize);
            ps.setInt(3, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {

                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setImageUrl(rs.getString("urlImage"));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public int countProductsByCategory(int categoryId) {
        String sql =
                "SELECT COUNT(*) FROM products " +
                        "WHERE category_id = ? AND isActive = 1";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    // 1. Lấy danh sách Loại sản phẩm để hiện lên Sidebar
    public List<ProductType> getAllProductTypes() {
        List<ProductType> list = new ArrayList<>();
        String sql = "SELECT id, product_type_name FROM product_types";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductType pt = new ProductType();
                pt.setId(rs.getInt("id"));
                pt.setProductTypeName(rs.getString("product_type_name"));
                list.add(pt);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Lấy danh sách Màu sắc để hiện lên Sidebar
    public List<ProductColor> getAllColors() {
        List<ProductColor> list = new ArrayList<>();
        String sql = "SELECT id, colorName FROM colors";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductColor c = new ProductColor();
                c.setId(rs.getInt("id"));
                c.setColorName(rs.getString("colorName"));
                list.add(c);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 3. Hàm lọc sản phẩm hỗ trợ Màu sắc
    public List<Product> filterProductsWithColor(String[] types, String[] prices, String[] ratings, Integer categoryId, String colorId) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name_product, p.price, img.urlImage, IFNULL(AVG(r.rate),0) AS avg_rate " +
                        "FROM products p " +
                        "LEFT JOIN images img ON p.primary_image_id = img.id " +
                        "LEFT JOIN reviews r ON p.id = r.product_id " +
                        "LEFT JOIN product_variants pv ON p.id = pv.product_id " + // Cần Join bảng biến thể để lọc màu
                        "WHERE p.isActive = 1 "
        );

        if (categoryId != null) sql.append(" AND p.category_id = ").append(categoryId);
        if (types != null && types.length > 0) sql.append(" AND p.product_type_id IN (").append(String.join(",", types)).append(")");
        if (colorId != null && !colorId.isEmpty()) sql.append(" AND pv.color_id = ").append(colorId);

        if (prices != null && prices.length > 0) {
            List<String> conds = new ArrayList<>();
            for (String p : prices) {
                if (p.equals("1")) conds.add("p.price < 1000000");
                else if (p.equals("2")) conds.add("p.price BETWEEN 1000000 AND 3000000");
                else if (p.equals("3")) conds.add("p.price BETWEEN 3000000 AND 5000000");
                else if (p.equals("4")) conds.add("p.price BETWEEN 5000000 AND 10000000");
                else if (p.equals("5")) conds.add("p.price > 10000000");
            }
            sql.append(" AND (").append(String.join(" OR ", conds)).append(")");
        }

        sql.append(" GROUP BY p.id ");
        if (ratings != null && ratings.length > 0) {
            int min = Arrays.stream(ratings).mapToInt(Integer::parseInt).min().orElse(0);
            sql.append(" HAVING avg_rate >= ").append(min);
        }

        try (Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql.toString())) {
            while (rs.next()) {
                list.add(new Product(rs.getInt("id"), rs.getString("name_product"), rs.getDouble("price"), rs.getString("urlImage"), rs.getDouble("avg_rate")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    public ProductVariants getVariantById(int variantId) {

        String sql = """
        SELECT pv.*,
               c.id AS c_id, c.colorname, c.color_code,
               s.id AS s_id, s.size_name, s.length, s.width, s.height
        FROM product_variants pv
        LEFT JOIN colors c ON pv.color_id = c.id
        LEFT JOIN sizes s ON pv.size_id = s.id
        WHERE pv.id = ?
    """;

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variantId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ProductVariants v = new ProductVariants();

                v.setId(rs.getInt("id"));
                v.setProduct_id(rs.getInt("product_id"));
                v.setColor_id(rs.getInt("color_id"));
                v.setSize_id(rs.getInt("size_id"));
                v.setSku(rs.getString("sku"));
                v.setInventory_quantity(rs.getInt("inventory_quantity"));
                v.setVariant_price(rs.getBigDecimal("variant_price"));

                // ===== COLOR =====
                ProductColor color = new ProductColor();
                color.setId(rs.getInt("c_id"));
                color.setColorName(rs.getString("colorname"));
                color.setColorCode(rs.getString("color_code"));
                v.setColor(color);

                // ===== SIZE =====
                ProductSize size = new ProductSize();
                size.setId(rs.getInt("s_id"));
                size.setSize_name(rs.getString("size_name"));
                size.setLength(rs.getBigDecimal("length"));
                size.setWidth(rs.getBigDecimal("width"));
                size.setHeight(rs.getBigDecimal("height"));
                v.setSize(size);

                return v;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}