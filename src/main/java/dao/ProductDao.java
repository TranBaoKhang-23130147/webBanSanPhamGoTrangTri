package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

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
            this.conn = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        String sql = "SELECT p.*, t.product_type_name, c.category_name, img.urlImage, " +
                "(SELECT SUM(pv.inventory_quantity) FROM product_variants pv WHERE pv.product_id = p.id) AS total_stock " +
                "FROM products p " +
                "LEFT JOIN product_types t ON p.product_type_id = t.id " +
                "LEFT JOIN categories c ON p.category_id = c.id " +
                "LEFT JOIN images img ON p.primary_image_id = img.id";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setTotalStock(rs.getInt("total_stock"));

                p.setIsActive(rs.getInt("isActive"));

                p.setMfgDate(rs.getDate("mfg_Date"));

                p.setImageUrl(rs.getString("urlImage"));
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

        try (Connection conn = getConnection();
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

                    Source source = new Source();
                    source.setSourceName(rs.getString("sourceName"));
                    p.setSource(source);

                    Information info = new Information();
                    info.setMaterial(rs.getString("material"));
                    info.setSize(rs.getString("size"));
                    info.setColor(rs.getString("color"));
                    info.setGuarantee(rs.getString("guarantee"));

                    Description desc = new Description();
                    desc.setIntroduce(rs.getString("introduce"));
                    desc.setHighlights(rs.getString("highlights"));
                    desc.setInformation(info);
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

    public List<Images> getProductImages(int productId) {
        List<Images> list = new ArrayList<>();
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
                    v.setInventory_quantity(rs.getInt("inventory_quantity"));

                    ProductColor c = new ProductColor();
                    c.setId(rs.getInt("color_id"));
                    c.setColorName(rs.getString("colorName"));

                    c.setColorCode(rs.getString("color_code"));

                    v.setColor(c);

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

    public List<Reviews> getProductReviews(int productId) {
        List<Reviews> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews WHERE product_id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reviews r = new Reviews();

                    r.setId(rs.getInt("id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setProductId(rs.getInt("product_id"));
                    r.setComment(rs.getString("comment"));

                    r.setRating(rs.getInt("rate"));

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

        if (categoryId != null) {
            sql.append(" AND p.category_id = ").append(categoryId);
        }

        if (types != null && types.length > 0) {
            sql.append(" AND p.product_type_id IN (")
                    .append(String.join(",", types))
                    .append(")");
        }

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
        String sql = "SELECT full_name FROM users WHERE id = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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

    public List<Product> filterProductsWithColor(String[] types, String[] prices, String[] ratings, Integer categoryId, String colorId) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.name_product, p.price, img.urlImage, IFNULL(AVG(r.rate),0) AS avg_rate " +
                        "FROM products p " +
                        "LEFT JOIN images img ON p.primary_image_id = img.id " +
                        "LEFT JOIN reviews r ON p.id = r.product_id " +
                        "LEFT JOIN product_variants pv ON p.id = pv.product_id " +
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

        try (Connection con = getConnection();
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

                ProductColor color = new ProductColor();
                color.setId(rs.getInt("c_id"));
                color.setColorName(rs.getString("colorname"));
                color.setColorCode(rs.getString("color_code"));
                v.setColor(color);

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

    public int insertFullDescription(String introduce, String highlights, String material, String size, String color, String guarantee) {
        String sqlInfo = "INSERT INTO informations (material, color, size, guarantee) VALUES (?, ?, ?, ?)";
        String sqlDesc = "INSERT INTO descriptions (introduce, highlights, information_id) VALUES (?, ?, ?)";

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try {
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

                int descId = 0;
                try (PreparedStatement ps2 = conn.prepareStatement(sqlDesc, Statement.RETURN_GENERATED_KEYS)) {
                    ps2.setString(1, introduce);
                    ps2.setString(2, highlights);
                    ps2.setInt(3, infoId);
                    ps2.executeUpdate();
                    ResultSet rs2 = ps2.getGeneratedKeys();
                    if (rs2.next()) descId = rs2.getInt(1);
                }

                conn.commit();
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

    public void updatePrimaryImage(int productId, int imageId) {
        String sql = "UPDATE products SET primary_image_id = ? WHERE id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

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
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, t.product_type_name, c.category_name, img.urlImage, " +
                        "(SELECT SUM(pv.inventory_quantity) FROM product_variants pv WHERE pv.product_id = p.id) AS total_stock " +
                        "FROM products p " +
                        "LEFT JOIN product_types t ON p.product_type_id = t.id " +
                        "LEFT JOIN categories c ON p.category_id = c.id " +
                        "LEFT JOIN images img ON p.primary_image_id = img.id " +
                        "WHERE 1=1"
        );

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
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setIsActive(rs.getInt("isActive"));

                p.setMfgDate(rs.getDate("mfg_date"));

                p.setImageUrl(rs.getString("urlImage"));

                p.setTotalStock(rs.getInt("total_stock"));

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

        try (Connection con = getConnection();
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
                "AVG(r.rate) AS avg_rate " +
                "FROM products p " +
                "LEFT JOIN images i ON p.primary_image_id = i.id " +
                "LEFT JOIN product_variants pv ON p.id = pv.product_id " +
                "LEFT JOIN order_details od ON pv.id = od.product_variant_id " +
                "LEFT JOIN reviews r ON p.id = r.product_id " +
                "GROUP BY p.id " +
                "ORDER BY total_sold DESC " +
                "LIMIT 8";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setNameProduct(rs.getString("name_product"));
                p.setPrice(rs.getDouble("price"));
                p.setImageUrl(rs.getString("urlImage"));
                p.setAverageRating(rs.getDouble("avg_rate"));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<Category> getAllCategory() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM categories";
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

    public List<Source> getAllSources() {
        List<Source> list = new ArrayList<>();
        String sql = "SELECT * FROM sources";
        try (Connection conn = getConnection();
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

    public List<ProductSize> getAllSizes() {
        List<ProductSize> list = new ArrayList<>();
        String sql = "SELECT * FROM sizes";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ProductSize s = new ProductSize();
                s.setId(rs.getInt("id"));
                s.setSize_name(rs.getString("size_name"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
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

        public boolean deleteFullProduct(int productId) {

            try (Connection conn = new DBContext().getConnection()) {

                conn.setAutoCommit(false);

                conn.prepareStatement(
                                "DELETE FROM product_variants WHERE product_id=?")
                        .executeUpdate();

                PreparedStatement ps1 = conn.prepareStatement(
                        "DELETE FROM product_images WHERE product_id=?");
                ps1.setInt(1, productId);
                ps1.executeUpdate();

                PreparedStatement ps2 = conn.prepareStatement(
                        "DELETE FROM products WHERE id=?");
                ps2.setInt(1, productId);
                ps2.executeUpdate();

                conn.commit();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public int getTotalStockByProductId(int productId) {
            int total = 0;
            String sql = "SELECT SUM(inventory_quantity) FROM product_variants WHERE product_id = ?";
            try (Connection conn = getConnection();
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
        public boolean updateProductStatus(int productId, int status) {
            String sql = "UPDATE products SET isActive = ? WHERE id = ?";
            Connection conn = null;
            PreparedStatement ps = null;
            try {
                conn = getConnection();
                ps = conn.prepareStatement(sql);
                ps.setInt(1, status);
                ps.setInt(2, productId);

                int rowsAffected = ps.executeUpdate();
                System.out.println("Update products id=" + productId + " → rows affected: " + rowsAffected);

                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try { if (ps != null) ps.close(); } catch (Exception ignored) {}
                try { if (conn != null) conn.close(); } catch (Exception ignored) {}
            }
        }
        public int getSoldQuantityByVariantId(int variantId) {
            String sql = """
        SELECT COALESCE(SUM(oi.quantity), 0)
        FROM order_details oi
        JOIN orders o ON oi.order_id = o.id
        WHERE oi.product_variant_id = ?
          AND o.status NOT IN ('Đã hủy', 'Hoàn hàng')
    """;

            try (Connection conn = new DBContext().getConnection();
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
        public Information getInformationByProductId(int productId) {
            String sql = """
        SELECT i.*
        FROM products p
        JOIN description d ON p.description_id = d.id
        JOIN information i ON d.information_id = i.id
        WHERE p.id = ?
    """;

            Connection connection = null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Information info = new Information();
                    info.setId(rs.getInt("id"));
                    info.setMaterial(rs.getString("material"));
                    info.setColor(rs.getString("color"));
                    info.setSize(rs.getString("size"));
                    info.setGuarantee(rs.getString("guarantee"));
                    return info;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        public Description getDescriptionByProductId(int productId) {
            String sql = """
        SELECT d.*
        FROM products p
        JOIN description d ON p.description_id = d.id
        WHERE p.id = ?
    """;

            Connection connection = null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Description d = new Description();
                    d.setId(rs.getInt("id"));
                    d.setIntroduce(rs.getString("introduce"));
                    d.setHighlights(rs.getString("highlight"));
                    d.setInformationId(rs.getInt("information_id"));
                    return d;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        public List<Integer> getVariantIdsByProduct(int productId) {
            List<Integer> ids = new ArrayList<>();
            String sql = "SELECT id FROM product_variants WHERE product_id = ?";

            Connection connection = null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    ids.add(rs.getInt("id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ids;
        }public void deleteVariant(int variantId) {
            String sql = "DELETE FROM product_variants WHERE id = ?";
            Connection connection =null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, variantId);
                ps.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void updateVariant(int variantId, double price, int stock) {
            String sql = """
        UPDATE product_variants
        SET variant_price = ?, inventory_quantity = ?
        WHERE id = ?
    """;

            try (Connection conn = new DBContext().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setDouble(1, price);
                ps.setInt(2, stock);
                ps.setInt(3, variantId);

                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Map<Integer, Integer> countProductByType() {
            Map<Integer, Integer> map = new HashMap<>();

            String sql = """
        SELECT product_type_id, COUNT(*) AS total
        FROM products
        WHERE isActive = 1
        GROUP BY product_type_id
    """;

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    map.put(
                            rs.getInt("product_type_id"),
                            rs.getInt("total")
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }
        public Map<Integer, Integer> countProductByCategory() {
            Map<Integer, Integer> map = new HashMap<>();

            String sql = """
        SELECT category_id, COUNT(*) AS total
        FROM products
        WHERE isActive = 1
        GROUP BY category_id
    """;

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    map.put(
                            rs.getInt("category_id"),
                            rs.getInt("total")
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }
        public Map<Integer, Integer> countProductBySource() {
            Map<Integer, Integer> map = new HashMap<>();

            String sql = """
        SELECT source_id, COUNT(*) AS total
        FROM products
        WHERE isActive = 1
        GROUP BY source_id
    """;

            try (Connection con = getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    map.put(
                            rs.getInt("source_id"),
                            rs.getInt("total")
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return map;
        }
        public boolean insertFullProduct(Product p, Description desc, Information info,
                List<ProductVariant> variants, List<String> imagePaths) {
            Connection conn = null;
            try {
                conn = new DBContext().getConnection();
                conn.setAutoCommit(false);

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

                String sqlDesc = "INSERT INTO descriptions (introduce, highlights, information_id) VALUES (?, ?, ?)";
                PreparedStatement psDesc = conn.prepareStatement(sqlDesc, Statement.RETURN_GENERATED_KEYS);
                psDesc.setString(1, desc.getIntroduce());
                psDesc.setString(2, desc.getHighlights());
                psDesc.setInt(3, infoId);
                psDesc.executeUpdate();
                ResultSet rsDesc = psDesc.getGeneratedKeys();
                if (!rsDesc.next()) { conn.rollback(); return false; }
                int descId = rsDesc.getInt(1);

                String sqlProd = "INSERT INTO products (name_product, price, category_id, source_id, product_type_id, description_id, mfg_date, isActive) VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
                PreparedStatement psProd = conn.prepareStatement(sqlProd, Statement.RETURN_GENERATED_KEYS);
                psProd.setString(1, p.getNameProduct());
                psProd.setDouble(2, p.getPrice());
                int catId = p.getCategoryId();
                if (catId > 0) {
                    psProd.setInt(3, catId);
                } else {
                    psProd.setNull(3, Types.INTEGER);
                }
                psProd.setInt(4, p.getSourceId());
                if (p.getProductTypeId() > 0) {
                    psProd.setInt(5, p.getProductTypeId());
                } else {
                    psProd.setNull(5, Types.INTEGER);
                }
                psProd.setInt(6, descId);
                psProd.setDate(7, p.getMfgDate());
                psProd.executeUpdate();
                ResultSet rsProd = psProd.getGeneratedKeys();
                if (!rsProd.next()) { conn.rollback(); return false; }
                int productId = rsProd.getInt(1);

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

                if (primaryId != -1) {
                    String sqlUp = "UPDATE products SET primary_image_id = ? WHERE id = ?";
                    PreparedStatement psUp = conn.prepareStatement(sqlUp);
                    psUp.setInt(1, primaryId);
                    psUp.setInt(2, productId);
                    psUp.executeUpdate();
                }

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
                if (e instanceof SQLIntegrityConstraintViolationException) {
                    System.out.println("→ Vi phạm ràng buộc: " + e.getMessage());
                }
                try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
                return false;
            }
        }
        public boolean updateFullProduct(Product p, Description desc, Information info,
                List<ProductVariants> newVariants, List<String> imagePaths) {
            Connection conn = null;
            try {
                conn = new DBContext().getConnection();
                conn.setAutoCommit(false);

                int descId = -1;
                int infoId = -1;
                PreparedStatement psGetIds = conn.prepareStatement(
                        "SELECT description_id FROM products WHERE id = ?");
                psGetIds.setInt(1, p.getId());
                ResultSet rsIds = psGetIds.executeQuery();
                if (rsIds.next()) {
                    descId = rsIds.getInt("description_id");
                } else {
                    throw new SQLException("Không tìm thấy sản phẩm với ID: " + p.getId());
                }

                PreparedStatement psGetInfoId = conn.prepareStatement(
                        "SELECT information_id FROM descriptions WHERE id = ?");
                psGetInfoId.setInt(1, descId);
                ResultSet rsInfo = psGetInfoId.executeQuery();
                if (rsInfo.next()) {
                    infoId = rsInfo.getInt("information_id");
                } else {
                    throw new SQLException("Không tìm thấy description cho sản phẩm ID: " + p.getId());
                }

                String sqlInfo = "UPDATE informations SET material = ?, guarantee = ? WHERE id = ?";
                PreparedStatement psInfo = conn.prepareStatement(sqlInfo);
                psInfo.setString(1, info.getMaterial() != null ? info.getMaterial() : "");
                psInfo.setString(2, info.getGuarantee() != null ? info.getGuarantee() : "");
                psInfo.setInt(3, infoId);
                int rowsInfo = psInfo.executeUpdate();
                if (rowsInfo == 0) {
                    throw new SQLException("Không cập nhật được informations (id=" + infoId + ")");
                }

                String sqlDesc = "UPDATE descriptions SET introduce = ?, highlights = ? WHERE id = ?";
                PreparedStatement psDesc = conn.prepareStatement(sqlDesc);
                psDesc.setString(1, desc.getIntroduce() != null ? desc.getIntroduce() : "");
                psDesc.setString(2, desc.getHighlights() != null ? desc.getHighlights() : "");
                psDesc.setInt(3, descId);
                int rowsDesc = psDesc.executeUpdate();
                if (rowsDesc == 0) {
                    throw new SQLException("Không cập nhật được descriptions (id=" + descId + ")");
                }

                String sqlProd = "UPDATE products SET name_product=?, price=?, category_id=?, source_id=?, product_type_id=?, mfg_date=? WHERE id=?";
                PreparedStatement psProd = conn.prepareStatement(sqlProd);
                psProd.setString(1, p.getNameProduct());
                psProd.setDouble(2, p.getPrice());
                if (p.getCategoryId() > 0) psProd.setInt(3, p.getCategoryId()); else psProd.setNull(3, java.sql.Types.INTEGER);
                psProd.setInt(4, p.getSourceId());
                if (p.getProductTypeId() > 0) psProd.setInt(5, p.getProductTypeId()); else psProd.setNull(5, java.sql.Types.INTEGER);
                psProd.setDate(6, p.getMfgDate());
                psProd.setInt(7, p.getId());
                int rowsProd = psProd.executeUpdate();
                if (rowsProd == 0) {
                    throw new SQLException("Không cập nhật được sản phẩm chính (id=" + p.getId() + ")");
                }

                conn.prepareStatement("DELETE FROM product_images WHERE product_id = ?")
                        .executeUpdate();

                Integer primaryImageId = null;
                if (imagePaths != null && !imagePaths.isEmpty()) {
                    for (int i = 0; i < imagePaths.size(); i++) {
                        String url = imagePaths.get(i);
                        if (url == null || url.trim().isEmpty()) continue;

                        PreparedStatement psImg = conn.prepareStatement(
                                "INSERT INTO images (urlImage) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                        psImg.setString(1, url);
                        psImg.executeUpdate();
                        ResultSet rs = psImg.getGeneratedKeys();
                        if (rs.next()) {
                            int imgId = rs.getInt(1);
                            if (i == 0) primaryImageId = imgId;

                            PreparedStatement psPI = conn.prepareStatement(
                                    "INSERT INTO product_images (image_id, product_id) VALUES (?, ?)");
                            psPI.setInt(1, imgId);
                            psPI.setInt(2, p.getId());
                            psPI.executeUpdate();
                        }
                    }

                    if (primaryImageId != null) {
                        PreparedStatement psUp = conn.prepareStatement(
                                "UPDATE products SET primary_image_id = ? WHERE id = ?");
                        psUp.setInt(1, primaryImageId);
                        psUp.setInt(2, p.getId());
                        psUp.executeUpdate();
                    }
                }

                List<ProductVariants> oldVariants = getVariantsByProductId(p.getId(), conn);

                java.util.Map<Integer, ProductVariants> oldMap = new java.util.HashMap<>();
                for (ProductVariants ov : oldVariants) {
                    oldMap.put(ov.getId(), ov);
                }

                PreparedStatement psUpdate = conn.prepareStatement(
                        "UPDATE product_variants SET color_id=?, size_id=?, sku=?, inventory_quantity=?, variant_price=? WHERE id=?");
                PreparedStatement psInsert = conn.prepareStatement(
                        "INSERT INTO product_variants (product_id, color_id, size_id, sku, inventory_quantity, variant_price) VALUES (?,?,?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                PreparedStatement psDelete = conn.prepareStatement("DELETE FROM product_variants WHERE id=?");

                java.util.Set<Integer> processedIds = new java.util.HashSet<>();

                for (ProductVariants nv : newVariants) {
                    if (nv.getColor_id() <= 0 || nv.getSize_id() <= 0 || nv.getSku() == null || nv.getSku().trim().isEmpty()) {
                        throw new SQLException("Biến thể thiếu color/size/SKU hợp lệ");
                    }
                    if (nv.getVariant_price() == null) {
                        nv.setVariant_price(BigDecimal.ZERO);
                    }

                    if (nv.getId() > 0) {
                        psUpdate.setInt(1, nv.getColor_id());
                        psUpdate.setInt(2, nv.getSize_id());
                        psUpdate.setString(3, nv.getSku().trim());
                        psUpdate.setInt(4, nv.getInventory_quantity());
                        psUpdate.setBigDecimal(5, nv.getVariant_price());
                        psUpdate.setInt(6, nv.getId());
                        psUpdate.addBatch();
                        processedIds.add(nv.getId());
                    } else {
                        psInsert.setInt(1, p.getId());
                        psInsert.setInt(2, nv.getColor_id());
                        psInsert.setInt(3, nv.getSize_id());
                        psInsert.setString(4, nv.getSku().trim());
                        psInsert.setInt(5, nv.getInventory_quantity());
                        psInsert.setBigDecimal(6, nv.getVariant_price());
                        psInsert.addBatch();
                    }
                }

                for (Integer oldId : oldMap.keySet()) {
                    if (!processedIds.contains(oldId)) {
                        psDelete.setInt(1, oldId);
                        psDelete.addBatch();
                    }
                }

                psUpdate.executeBatch();
                psInsert.executeBatch();
                psDelete.executeBatch();

                conn.commit();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                if (conn != null) {
                    try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
                }
                return false;
            } finally {
                if (conn != null) {
                    try {
                        conn.setAutoCommit(true);
                        conn.close();
                    } catch (SQLException e) { e.printStackTrace(); }
                }
            }
        }

        private List<ProductVariants> getVariantsByProductId(int productId, Connection conn) throws SQLException {
            List<ProductVariants> variants = new ArrayList<>();
            String sqlVar = "SELECT * FROM product_variants WHERE product_id = ?";
            PreparedStatement psVar = conn.prepareStatement(sqlVar);
            psVar.setInt(1, productId);
            ResultSet rsVar = psVar.executeQuery();
            while (rsVar.next()) {
                ProductVariants v = new ProductVariants();
                v.setId(rsVar.getInt("id"));
                v.setSku(rsVar.getString("sku"));
                v.setColor_id(rsVar.getInt("color_id"));
                v.setSize_id(rsVar.getInt("size_id"));
                v.setInventory_quantity(rsVar.getInt("inventory_quantity"));
                v.setVariant_price(rsVar.getBigDecimal("variant_price"));
                variants.add(v);
            }
            return variants;
        }
        public Product getFullProductById(int productId) {
            Product p = null;
            Connection conn = null;
            try {
                conn = new DBContext().getConnection();
                String sql = "SELECT p.*, d.introduce, d.highlights, i.material, i.color, i.size, i.guarantee, i.id as info_id, d.id as desc_id " +
                        "FROM products p " +
                        "JOIN descriptions d ON p.description_id = d.id " +
                        "JOIN informations i ON d.information_id = i.id " +
                        "WHERE p.id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, productId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setNameProduct(rs.getString("name_product"));
                    p.setPrice(rs.getDouble("price"));
                    p.setCategoryId(rs.getInt("category_id"));
                    p.setProductTypeId(rs.getInt("product_type_id"));
                    p.setSourceId(rs.getInt("source_id"));
                    p.setMfgDate(rs.getDate("mfg_date"));

                    Description desc = new Description();
                    desc.setId(rs.getInt("desc_id"));
                    desc.setIntroduce(rs.getString("introduce"));
                    desc.setHighlights(rs.getString("highlights"));
                    p.setDetailDescription(desc);

                    Information info = new Information();
                    info.setId(rs.getInt("info_id"));
                    info.setMaterial(rs.getString("material"));
                    info.setGuarantee(rs.getString("guarantee"));
                    p.setInformation(info);

                    List<String> images = new ArrayList<>();
                    String sqlImg = "SELECT img.urlImage FROM images img JOIN product_images pi ON img.id = pi.image_id WHERE pi.product_id = ?";
                    PreparedStatement psImg = conn.prepareStatement(sqlImg);
                    psImg.setInt(1, productId);
                    ResultSet rsImg = psImg.executeQuery();
                    while (rsImg.next()) images.add(rsImg.getString("urlImage"));
                    p.setListImages(images);
                    p.setImagesRaw(String.join(",", images));

                    List<ProductVariants> variants = new ArrayList<>();
                    String sqlVar = "SELECT * FROM product_variants WHERE product_id = ?";
                    PreparedStatement psVar = conn.prepareStatement(sqlVar);
                    psVar.setInt(1, productId);
                    ResultSet rsVar = psVar.executeQuery();
                    while (rsVar.next()) {
                        ProductVariants v = new ProductVariants();
                        v.setId(rsVar.getInt("id"));
                        v.setSku(rsVar.getString("sku"));
                        v.setColor_id(rsVar.getInt("color_id"));
                        v.setSize_id(rsVar.getInt("size_id"));
                        v.setInventory_quantity(rsVar.getInt("inventory_quantity"));
                        v.setVariant_price(rsVar.getBigDecimal("variant_price"));
                        variants.add(v);
                    }
                    p.setVariants(variants);
                }
            } catch (Exception e) { e.printStackTrace(); }
            return p;
        }

        public boolean addReview(Reviews review) {
            String sql = "INSERT INTO reviews (user_id, product_id, rate, comment, createAt) VALUES (?, ?, ?, ?, GETDATE())";
            try (Connection conn = new DBContext().getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, review.getUserId());
                ps.setInt(2, review.getProductId());
                ps.setInt(3, review.getRating());
                ps.setString(4, review.getComment());
                return ps.executeUpdate() > 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public double getAverageRating(int productId) {

            String sql = "SELECT IFNULL(AVG(rate),0) FROM reviews WHERE product_id=?";

            try (Connection conn = DBContext.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, productId);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    return rs.getDouble(1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }

    }