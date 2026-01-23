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
    private Connection conn;

    public ProductDao() {
        try {
            // Thay thế bằng cách kết nối DB của bạn
            this.conn = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    public List<Product> getAllProductsAdmin() {
        List<Product> list = new ArrayList<>();
        // SQL đã sửa: Lấy đúng cột từ bảng categories và product_types
        String sql = "SELECT p.*, t.product_type_name, c.category_name, img.urlImage, " +
                "(SELECT SUM(pv.inventory_quantity) FROM product_variants pv WHERE pv.product_id = p.id) AS total_stock " +
                "FROM products p " +
                "LEFT JOIN product_types t ON p.product_type_id = t.id " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "LEFT JOIN images img ON p.primary_image_id = img.id";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price")); // Lấy giá ở đây
                p.setTotalStock(rs.getInt("total_stock"));

                // Lưu ý: Kiểm tra lại tên cột isActive trong DB có đúng chữ hoa/thường không
                p.setIsActive(rs.getInt("isActive"));

                // Kiểm tra cột ngày tháng trong DB là mfgDate hay mfg_Date
                p.setMfgDate(rs.getDate("mfg_Date"));

                // Lấy URL ảnh từ Alias "primary_image"
                p.setImageUrl(rs.getString("urlImage"));
                // Đổ dữ liệu từ bảng JOIN (Tên cột phải khớp chính xác với SQL trên)
                p.setCategoryName(rs.getString("category_name"));
                p.setTypeName(rs.getString("product_type_name"));

                list.add(p);
            }
        } catch (Exception e) {
            System.err.println("Lỗi SQL: " + e.getMessage());
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
                    v.setInventory_quantity(rs.getInt("inventory_quantity")); //

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

    // 1. Lưu Information và Description (Trả về ID của Description)
    public int insertFullDescription(String introduce, String highlights, String material, String size, String color, String guarantee) {
        String sqlInfo = "INSERT INTO informations (material, color, size, guarantee) VALUES (?, ?, ?, ?)";
        String sqlDesc = "INSERT INTO descriptions (introduce, highlights, information_id) VALUES (?, ?, ?)";

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction
            try {
                // Thêm vào bảng informations
                int infoId = 0;
                try (PreparedStatement ps1 = conn.prepareStatement(sqlInfo, Statement.RETURN_GENERATED_KEYS)) {
                    ps1.setString(1, material);
                    ps1.setString(2, color);
                    ps1.setString(3, size);
                    ps1.setString(4, guarantee);
                    ps1.executeUpdate();
                    ResultSet rs1 = ps1.getGeneratedKeys();
                    if (rs1.next()) infoId = rs1.getInt(1);
                }

                // Thêm vào bảng descriptions
                int descId = 0;
                try (PreparedStatement ps2 = conn.prepareStatement(sqlDesc, Statement.RETURN_GENERATED_KEYS)) {
                    ps2.setString(1, introduce);
                    ps2.setString(2, highlights);
                    ps2.setInt(3, infoId);
                    ps2.executeUpdate();
                    ResultSet rs2 = ps2.getGeneratedKeys();
                    if (rs2.next()) descId = rs2.getInt(1);
                }

                conn.commit(); // Hoàn tất
                return descId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Lưu Product và trả về ID sản phẩm


    // 3. Lưu ảnh vào bảng images và bảng trung gian product_images
    public int insertImageAndGetId(String url, int productId) {
        String sqlImg = "INSERT INTO images (urlImage) VALUES (?)";
        String sqlMap = "INSERT INTO product_images (product_id, image_id) VALUES (?, ?)";

        try (Connection conn = new DBContext().getConnection()) {
            int imgId = 0;
            try (PreparedStatement ps1 = conn.prepareStatement(sqlImg, Statement.RETURN_GENERATED_KEYS)) {
                ps1.setString(1, url);
                ps1.executeUpdate();
                ResultSet rs = ps1.getGeneratedKeys();
                if (rs.next()) imgId = rs.getInt(1);
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlMap)) {
                ps2.setInt(1, productId);
                ps2.setInt(2, imgId);
                ps2.executeUpdate();
            }
            return imgId;
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    // 4. Cập nhật ảnh chính cho sản phẩm
    public void updatePrimaryImage(int productId, int imageId) {
        String sql = "UPDATE products SET primary_image_id = ? WHERE id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 5. Lưu biến thể sản phẩm
    public void insertVariant(int productId, int colorId, int sizeId, double price, int stock) {
        String sql = "INSERT INTO product_variants (product_id, color_id, size_id, variant_price, stock_quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, colorId);
            ps.setInt(3, sizeId);
            ps.setDouble(4, price);
            ps.setInt(5, stock);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public List<Product> searchProducts(String keyword, String typeId, String categoryId) {
        List<Product> list = new ArrayList<>();
        // 1. Thêm subquery SUM(inventory_quantity) để lấy tổng số lượng từ bảng biến thể
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, t.product_type_name, c.category_name, img.urlImage, " +
                        "(SELECT SUM(pv.inventory_quantity) FROM product_variants pv WHERE pv.product_id = p.id) AS total_stock " +
                        "FROM products p " +
                        "LEFT JOIN product_types t ON p.product_type_id = t.id " +
                        "LEFT JOIN categories c ON p.category_id = c.id " +
                        "LEFT JOIN images img ON p.primary_image_id = img.id " +
                        "WHERE 1=1"
        );

        // 2. Thêm các điều kiện lọc động
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND p.name_product LIKE ?");
        }
        if (typeId != null && !typeId.trim().isEmpty()) {
            sql.append(" AND p.product_type_id = ?");
        }
        if (categoryId != null && !categoryId.trim().isEmpty()) {
            sql.append(" AND p.category_id = ?");
        }

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(index++, "%" + keyword + "%");
            }
            if (typeId != null && !typeId.trim().isEmpty()) {
                ps.setInt(index++, Integer.parseInt(typeId));
            }
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                ps.setInt(index++, Integer.parseInt(categoryId));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                // Lấy dữ liệu cơ bản từ bảng products
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setIsActive(rs.getInt("isActive"));

                // Chú ý: Đồng bộ tên cột mfg_date như hàm getAll của bạn
                p.setMfgDate(rs.getDate("mfg_date"));

                // Lấy URL ảnh từ bảng images đã join
                p.setImageUrl(rs.getString("urlImage"));

                // Lấy tổng kho (từ Alias 'total_stock')
                p.setTotalStock(rs.getInt("total_stock"));

                // Lấy dữ liệu từ bảng JOIN (Khớp với Alias trong câu lệnh SELECT)
                p.setProductTypeName(rs.getString("product_type_name"));
                p.setCategoryName(rs.getString("category_name"));

                list.add(p);
            }
        } catch (Exception e) {
            System.err.println("Lỗi SQL Search: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public List<Product> getTop3FeaturedProducts() {
        List<Product> list = new ArrayList<>();
        String sql = """
        SELECT 
            p.id,
            p.name_product,
            p.price,
            img.urlImage,
            AVG(r.rate) AS avg_rate,
            COUNT(r.id) AS review_count
        FROM products p
        LEFT JOIN reviews r ON p.id = r.product_id
        LEFT JOIN images img ON p.primary_image_id = img.id
        WHERE p.isActive = 1
        GROUP BY p.id
        ORDER BY avg_rate DESC, review_count DESC
        LIMIT 3
    """;

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setImageUrl(rs.getString("urlImage"));
                p.setAverageRating(rs.getDouble("avg_rate"));
                p.setTotalReviews(rs.getInt("review_count"));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Product> getTop8BestSellers() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.name_product, p.price, i.urlImage, " +
                "SUM(od.quantity) AS total_sold, " +
                "AVG(r.rate) AS avg_rate " + // Thêm lấy trung bình cộng rate
                "FROM products p " +
                "LEFT JOIN images i ON p.primary_image_id = i.id " +
                "LEFT JOIN product_variants pv ON p.id = pv.product_id " +
                "LEFT JOIN order_details od ON pv.id = od.product_variant_id " +
                "LEFT JOIN reviews r ON p.id = r.product_id " + // QUAN TRỌNG: JOIN thêm bảng reviews
                "GROUP BY p.id " +
                "ORDER BY total_sold DESC " +
                "LIMIT 8";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product")); // Lưu ý đúng tên cột name_product
                p.setPrice(rs.getDouble("price"));
                p.setImageUrl(rs.getString("urlImage"));
                // Nếu bạn có hàm tính rating trung bình từ bảng reviews, hãy gọi ở đây
                p.setAverageRating(rs.getDouble("avg_rate")); // Tạm thời để demo hoặc viết hàm lấy từ bảng reviews

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    // Sửa hàm lấy tất cả danh mục
    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories"; // Đảm bảo tên bảng là 'categories'
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Category(rs.getInt("id"), rs.getString("category_name")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Lấy tất cả nhà cung cấp (Nguồn)
    public List<Source> getAllSources() {
        List<Source> list = new ArrayList<>();
        String sql = "SELECT * FROM sources";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Source s = new Source();
                s.setId(rs.getInt("id"));
                s.setSourceName(rs.getString("sourceName"));
                list.add(s);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    // Sửa hàm lấy sản phẩm theo danh mục
    public List<Product> getProductsByCategoryID(String cid) {
        List<Product> list = new ArrayList<>();
        String sql = """
        SELECT p.id, p.name_product, p.price, img.urlImage, 
               COALESCE(AVG(r.rate), 0) AS avgRating
        FROM products p
        LEFT JOIN images img ON p.primary_image_id = img.id
        LEFT JOIN reviews r ON p.id = r.product_id
        WHERE p.category_id = ? AND p.isActive = 1
        GROUP BY p.id
        """;

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setNameProduct(rs.getString("name_product"));
                    p.setPrice(rs.getDouble("price"));
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




    // 5. Lấy danh sách kích thước
    public List<ProductSize> getAllSizes() {
        List<ProductSize> list = new ArrayList<>();
        String sql = "SELECT * FROM sizes";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductSize s = new ProductSize();
                s.setId(rs.getInt("id"));
                s.setSize_name(rs.getString("size_name"));
                list.add(s);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }    // 1. Lấy danh sách Loại sản phẩm để hiện lên Sidebar
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
    public boolean insertFullProduct(Product p, Description desc, Information info,
                                     List<ProductVariant> variants, List<String> imagePaths) {
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            // BƯỚC 1: Chèn vào bảng informations
            String sqlInfo = "INSERT INTO informations (material, color, size, guarantee) VALUES (?, ?, ?, ?)";
            PreparedStatement psInfo = conn.prepareStatement(sqlInfo, Statement.RETURN_GENERATED_KEYS);
            psInfo.setString(1, info.getMaterial());
            psInfo.setString(2, info.getColor());
            psInfo.setString(3, info.getSize());
            psInfo.setString(4, info.getGuarantee());
            psInfo.executeUpdate();
            ResultSet rsInfo = psInfo.getGeneratedKeys();
            if (!rsInfo.next()) { conn.rollback(); return false; }
            int infoId = rsInfo.getInt(1);

            // BƯỚC 2: Chèn vào bảng descriptions (information_id)
            String sqlDesc = "INSERT INTO descriptions (introduce, highlights, information_id) VALUES (?, ?, ?)";
            PreparedStatement psDesc = conn.prepareStatement(sqlDesc, Statement.RETURN_GENERATED_KEYS);
            psDesc.setString(1, desc.getIntroduce());
            psDesc.setString(2, desc.getHighlights());
            psDesc.setInt(3, infoId);
            psDesc.executeUpdate();
            ResultSet rsDesc = psDesc.getGeneratedKeys();
            if (!rsDesc.next()) { conn.rollback(); return false; }
            int descId = rsDesc.getInt(1);

            // BƯỚC 3: Chèn vào bảng products (description_id)
            String sqlProd = "INSERT INTO products (name_product, price, category_id, source_id, product_type_id, description_id, mfg_date, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
            PreparedStatement psProd = conn.prepareStatement(sqlProd, Statement.RETURN_GENERATED_KEYS);
            psProd.setString(1, p.getNameProduct());
            psProd.setDouble(2, p.getPrice());
            int catId = p.getCategoryId();
            if (catId > 0) {
                psProd.setInt(3, catId);
            } else {
                psProd.setNull(3, java.sql.Types.INTEGER);
            }
            psProd.setInt(4, p.getSourceId());
            if (p.getProductTypeId() > 0) {
                psProd.setInt(5, p.getProductTypeId());
            } else {
                psProd.setNull(5, java.sql.Types.INTEGER);
            }
            psProd.setInt(6, descId);
            psProd.setDate(7, p.getMfgDate());
            psProd.executeUpdate();
            ResultSet rsProd = psProd.getGeneratedKeys();
            if (!rsProd.next()) { conn.rollback(); return false; }
            int productId = rsProd.getInt(1);

            // BƯỚC 4: Chèn ảnh
            int primaryId = -1;
            String sqlImg = "INSERT INTO images (urlImage) VALUES (?)";
            String sqlPI = "INSERT INTO product_images (image_id, product_id) VALUES (?, ?)";
            for (int i = 0; i < imagePaths.size(); i++) {
                PreparedStatement psImg = conn.prepareStatement(sqlImg, Statement.RETURN_GENERATED_KEYS);
                psImg.setString(1, imagePaths.get(i));
                psImg.executeUpdate();
                ResultSet rsImg = psImg.getGeneratedKeys();
                if (rsImg.next()) {
                    int imgId = rsImg.getInt(1);
                    if (i == 0) primaryId = imgId;
                    PreparedStatement psPI = conn.prepareStatement(sqlPI);
                    psPI.setInt(1, imgId);
                    psPI.setInt(2, productId);
                    psPI.executeUpdate();
                }
            }

            // BƯỚC 5: Cập nhật primary_image_id
            if (primaryId != -1) {
                String sqlUp = "UPDATE products SET primary_image_id = ? WHERE id = ?"; // Kiểm tra cột PK là id hay product_id
                PreparedStatement psUp = conn.prepareStatement(sqlUp);
                psUp.setInt(1, primaryId);
                psUp.setInt(2, productId);
                psUp.executeUpdate();
            }

            // BƯỚC 6: Chèn biến thể
            String sqlVar = "INSERT INTO product_variants (product_id, color_id, size_id, sku, inventory_quantity, variant_price) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psVar = conn.prepareStatement(sqlVar);
            for (ProductVariant v : variants) {
                psVar.setInt(1, productId);
                psVar.setInt(2, v.getColorId());
                psVar.setInt(3, v.getSizeId());
                psVar.setString(4, v.getSku());
                psVar.setInt(5, v.getInventoryQuantity());
                psVar.setDouble(6, v.getVariantPrice());
                psVar.addBatch();
            }
            psVar.executeBatch();

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi ở bước: " + e.getMessage());
            // Thêm dòng này để xem chính xác
            if (e instanceof java.sql.SQLIntegrityConstraintViolationException) {
                System.out.println("→ Vi phạm ràng buộc: " + e.getMessage());
            }
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            return false;
        }
    }
    public boolean deleteFullProduct(int productId) {
        Connection conn = null;
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            // 1. Lấy description_id, information_id
            int descId = -1, infoId = -1;
            String q = """
            SELECT d.id AS descId, i.id AS infoId
            FROM products p
            JOIN descriptions d ON p.description_id = d.id
            JOIN informations i ON d.information_id = i.id
            WHERE p.id = ?
        """;
            PreparedStatement ps = conn.prepareStatement(q);
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                descId = rs.getInt("descId");
                infoId = rs.getInt("infoId");
            }

            // 2. Xóa biến thể
            conn.prepareStatement(
                    "DELETE FROM product_variants WHERE product_id = " + productId
            ).executeUpdate();

            // 3. Xóa ảnh
            conn.prepareStatement(
                    "DELETE i FROM images i JOIN product_images pi ON i.id = pi.image_id WHERE pi.product_id = " + productId
            ).executeUpdate();

            conn.prepareStatement(
                    "DELETE FROM product_images WHERE product_id = " + productId
            ).executeUpdate();

            // 4. Xóa product
            conn.prepareStatement(
                    "DELETE FROM products WHERE id = " + productId
            ).executeUpdate();

            // 5. Xóa mô tả + thông tin
            if (descId > 0)
                conn.prepareStatement("DELETE FROM descriptions WHERE id = " + descId).executeUpdate();

            if (infoId > 0)
                conn.prepareStatement("DELETE FROM informations WHERE id = " + infoId).executeUpdate();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
            return false;
        }
    }


    public int getSoldQuantityByVariantId(int variantId) {
        String sql = """
        SELECT COALESCE(SUM(oi.quantity), 0)
        FROM order_items oi
        JOIN orders o ON oi.order_id = o.order_id
        WHERE oi.variant_id = ?
          AND o.status IN ('completed', 'shipped', 'delivered')  -- đơn đã giao hoặc hoàn thành
        """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, variantId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getTotalStockByProductId(int productId) {
        int total = 0;
        String sql = "SELECT SUM(inventory_quantity) FROM product_variants WHERE product_id = ?"; //
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
}
