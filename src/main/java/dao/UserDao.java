package dao;

import model.Address;
import model.Reviews;
import model.User;
import utils.PasswordUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static dao.DBContext.getConnection;

public class UserDao {

    /**
     * Kiểm tra thông tin đăng nhập trong DB.
     *
     * @param email    Email người dùng
     * @param password Mật khẩu người dùng
     * @return Đối tượng User nếu đăng nhập thành công, ngược lại trả về null.
     */
    public User checkLogin(String email, String password) {
        // SỬA: Sử dụng LEFT JOIN để lấy thêm cột urlImage từ bảng images
        String sql = """
    SELECT u.id, u.full_name, u.display_name, u.birth_date,
           u.email, u.password, u.phone, u.gender, u.avatar_id,
           u.role, u.status, u.createAt, i.urlImage
    FROM users u
    LEFT JOIN images i ON u.avatar_id = i.id
    WHERE u.email = ?
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("password");

                    // KIỂM TRA MẬT KHẨU
                    if (PasswordUtils.checkPassword(password, hashedPasswordFromDB)) {
                        User u = new User();
                        u.setId(rs.getInt("id"));
                        u.setUsername(rs.getString("full_name"));
                        u.setDisplayName(rs.getString("display_name"));
                        u.setBirthDate(rs.getDate("birth_date"));
                        u.setEmail(rs.getString("email"));
                        u.setPhone(rs.getString("phone"));
                        u.setGender(rs.getString("gender"));

                        // Lấy ID ảnh (dạng số)
                        u.setAvatarId(rs.getObject("avatar_id", Integer.class));

                        // QUAN TRỌNG: Lấy URL ảnh từ bảng images để hiển thị ở Sidebar
                        u.setAvatarUrl(rs.getString("urlImage"));

                        u.setRole(rs.getString("role"));
                        u.setStatus(rs.getString("status"));
                        u.setCreateAt(rs.getDate("createAt"));
                        return u;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    public User checkLogin(String email, String password) {
//
//        String sql = """
//        SELECT id, full_name, display_name, birth_date,
//               email, phone, gender, avatar_id,
//               role, status, createAt
//        FROM users
//        WHERE email = ?
//    """;
//
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, email);
//            ps.setString(2, password);
//
//            try (ResultSet rs = ps.executeQuery()) {
//                if (rs.next()) {
//                    User u = new User();
//                    u.setId(rs.getInt("id"));
//                    u.setUsername(rs.getString("full_name"));
//                    u.setDisplayName(rs.getString("display_name"));
//                    u.setBirthDate(rs.getDate("birth_date"));
//                    u.setEmail(rs.getString("email"));
//                    u.setPhone(rs.getString("phone"));
//                    u.setGender(rs.getString("gender"));
//                    u.setAvatarId(rs.getObject("avatar_id", Integer.class));
//                    u.setRole(rs.getString("role"));
//                    u.setStatus(rs.getString("status"));
//                    u.setCreateAt(rs.getDate("createAt"));
//                    return u;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * Kiểm tra xem một email đã tồn tại trong DB chưa.
     *
     * @param email Email cần kiểm tra
     * @return true nếu tồn tại, false nếu chưa.
     */
    public boolean checkEmailExist(String email) {
        String SQL = "SELECT id FROM users WHERE email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void signup(String username, String email, String password) {
        String SQL = "INSERT INTO users (full_name, email, password, role, status, createAt) VALUES (?, ?, ?, 'User', 'Active', ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {

            // BĂM MẬT KHẨU TRƯỚC KHI LƯU
            String hashedPassword = PasswordUtils.hashPassword(password);

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void signup(String username, String email, String password) {
//        String SQL = "INSERT INTO users (full_name, email, password, role, status,createAt) VALUES (?, ?, ?, 'User', 'Active',?)";
//
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(SQL)) {
//            ps.setString(1, username);
//            ps.setString(2, email);
//            ps.setString(3, password);
//            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // 👈 THÊM DÒNG NÀY
//            ps.executeUpdate();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public int countNewUsersLast30Days() {
        String sql = "SELECT COUNT(*) FROM users WHERE (role = 'User') and (createAt IS NULL OR createAt >= ?)";
        LocalDateTime since = LocalDateTime.now(ZoneId.systemDefault()).minusDays(30);
        Timestamp sinceTs = Timestamp.valueOf(since);

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, sinceTs);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
    public User getById(int id) {
        String sql = """
        SELECT u.*, i.urlImage,
               a.id AS addr_id, a.name AS addr_name, a.phone AS addr_phone,
               a.detail AS addr_detail, a.commune AS addr_commune,
               a.district AS addr_district, a.province AS addr_province
        FROM users u
        LEFT JOIN images i ON u.avatar_id = i.id
        LEFT JOIN addresses a ON a.user_id = u.id AND a.isDefault = 1
        WHERE u.id = ?
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("full_name"));
                    u.setDisplayName(rs.getString("display_name"));
                    u.setBirthDate(rs.getDate("birth_date"));
                    u.setEmail(rs.getString("email"));
                    u.setPhone(rs.getString("phone"));
                    u.setGender(rs.getString("gender"));
                    u.setAvatarId(rs.getObject("avatar_id", Integer.class));
                    u.setAvatarUrl(rs.getString("urlImage"));
                    u.setRole(rs.getString("role"));
                    u.setStatus(rs.getString("status"));
                    u.setCreateAt(rs.getDate("createAt"));

                    // --- LẤY ADDRESS NẾU CÓ ---
                    if (rs.getObject("addr_id") != null) {
                        Address addr = new Address();
                        addr.setId(rs.getInt("addr_id"));
                        addr.setUserId(u.getId());
                        addr.setName(rs.getString("addr_name"));
                        addr.setPhone(rs.getString("addr_phone"));
                        addr.setDetail(rs.getString("addr_detail"));
                        addr.setCommune(rs.getString("addr_commune"));
                        addr.setDistrict(rs.getString("addr_district"));
                        addr.setProvince(rs.getString("addr_province"));
                        u.setAddress(addr);
                    }

                    return u;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



    public boolean updateUserInfo(int id, String fullName, String phone) {
        String sql = "UPDATE users SET full_name = ?, phone = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setInt(3, id);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public String getPasswordById(int userId) throws Exception {
        String sql = "SELECT password FROM users WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("password");
                return null;
            }
        }
    }
    public boolean updatePassword(int userId, String newPassword) throws Exception {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // CẦN SỬA: Băm mật khẩu mới ở đây
            String hashedPassword = PasswordUtils.hashPassword(newPassword);

            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        }
    }
//    public boolean updatePassword(int userId, String newPassword) throws Exception {
//        String sql = "UPDATE users SET password = ? WHERE id = ?";
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, newPassword);
//            ps.setInt(2, userId);
//            return ps.executeUpdate() == 1;
//        }
//    }

    public boolean updateUser(User user) {
        // Lưu ý: Tên cột phải chính xác như trong DB (ví dụ: full_name hay fullName)
        String sql = "UPDATE users SET full_name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getDisplayName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getEmail());
            ps.setInt(4, user.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //    public boolean updatePasswordByEmail(String email, String newPassword) {
//        String sql = "UPDATE users SET password = ? WHERE email = ?";
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, newPassword);
//            ps.setString(2, email);
//
//            return ps.executeUpdate() == 1;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // CẦN SỬA: Băm mật khẩu mới ở đây
            String hashedPassword = PasswordUtils.hashPassword(newPassword);

            ps.setString(1, hashedPassword);
            ps.setString(2, email);

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updatePasswordById(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Băm mật khẩu mới bằng PasswordUtils (giống logic đổi theo email của bạn)
            String hashedPassword = PasswordUtils.hashPassword(newPassword);

            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //    hien thi ng dung admin
    public List<User> getAllCustomers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = 'User'"; // Dùng * để lấy hết các cột bao gồm createAt
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("full_name"));
                u.setDisplayName(rs.getString("display_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setStatus(rs.getString("status"));

                // QUAN TRỌNG: Phải có dòng này thì JSP mới nhận được ngày
                u.setCreateAt(rs.getDate("createAt"));

                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public User getAdminProfile(int userId) {
        // Thêm u.role vào câu lệnh SELECT nếu u.* không lấy hết
        String sql = "SELECT u.*, i.urlImage FROM users u " +
                "LEFT JOIN images i ON u.avatar_id = i.id WHERE u.id = ?";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("full_name"));
                u.setRole(rs.getString("role")); // DÒNG QUAN TRỌNG NHẤT: Lấy chữ 'Admin' hoặc 'Staff' từ DB
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                return u;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean deleteUser(int userId) throws Exception {
        String deleteAddressSQL = "DELETE FROM addresses WHERE user_id = ?";
        String removeAvatarSQL = "UPDATE users SET avatar_id = NULL WHERE id = ?";
        String deleteUserSQL = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao dịch

            try (PreparedStatement psAddr = conn.prepareStatement(deleteAddressSQL);
                 PreparedStatement psAvatar = conn.prepareStatement(removeAvatarSQL);
                 PreparedStatement psUser = conn.prepareStatement(deleteUserSQL)) {

                // 1. Xóa tất cả địa chỉ của User này trước
                psAddr.setInt(1, userId);
                psAddr.executeUpdate();

                // 2. Gỡ Avatar (Tránh lỗi khóa ngoại nếu ảnh thuộc bảng images)
                psAvatar.setInt(1, userId);
                psAvatar.executeUpdate();

                // 3. Xóa bản ghi User
                psUser.setInt(1, userId);
                int rows = psUser.executeUpdate();

                conn.commit(); // Thành công thì commit
                return rows > 0;

            } catch (Exception e) {
                conn.rollback(); // Lỗi thì quay lại trạng thái cũ
                throw e;
            }
        }
    }

    /**
     * Kiểm tra số lượng Admin hiện có.
     * Dùng để ngăn chặn việc xóa Admin cuối cùng trong hệ thống.
     */
    public int countAdmin() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'Admin'";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    public Integer getFirstAdminId() {
        String sql = "SELECT id FROM users WHERE role = 'Admin' LIMIT 1";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean deleteUser(String id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = getConnection(); // Hàm lấy kết nối DB của bạn
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean adminInsertUser(String username, String email, String phone, String password, String role) {
        String sql = "INSERT INTO users (full_name, email, phone, password, role, status, createAt) VALUES (?, ?, ?, ?, ?, 'Active', NOW())";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, PasswordUtils.hashPassword(password)); // HASH Ở ĐÂY
            ps.setString(5, role);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * Lấy hoặc tạo mới ID ảnh từ bảng images dựa trên urlImage
     */
    public Integer getImageIdByUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            System.out.println("getImageIdByUrl: URL null hoặc rỗng, trả về null");
            return null; // Hoặc trả về ID ảnh mặc định nếu bạn có
        }

        String sqlSelect = "SELECT id FROM images WHERE urlImage = ?";
        String sqlInsert = "INSERT INTO images (urlImage) VALUES (?)";

        try (Connection conn = DBContext.getConnection()) {
            // 1. Kiểm tra xem URL đã tồn tại chưa
            try (PreparedStatement psSelect = conn.prepareStatement(sqlSelect)) {
                psSelect.setString(1, url.trim());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        System.out.println("getImageIdByUrl: Tìm thấy URL tồn tại, ID = " + id);
                        return id;
                    }
                }
            }

            // 2. Insert mới nếu chưa có
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psInsert.setString(1, url.trim());
                psInsert.executeUpdate();

                try (ResultSet generatedKeys = psInsert.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int newId = generatedKeys.getInt(1);
                        System.out.println("getImageIdByUrl: Insert mới thành công, ID mới = " + newId);
                        return newId;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi getImageIdByUrl cho URL '" + url + "': " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("getImageIdByUrl: Lỗi, trả về ID mặc định 1");
        return 1; // ID mặc định nếu có lỗi
    }

    /**
     * Lấy User từ DB theo ID, lấy avatar_url trực tiếp từ cột avatar_url
     */
    public User getUserById(int id) {
        // Sử dụng LEFT JOIN để lấy URL ảnh từ bảng images
        String sql = "SELECT u.*, i.urlImage FROM users u " +
                "LEFT JOIN images i ON u.avatar_id = i.id WHERE u.id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    u.setUsername(rs.getString("full_name"));
                    u.setDisplayName(rs.getString("display_name"));
                    u.setPhone(rs.getString("phone"));
                    u.setGender(rs.getString("gender"));
                    u.setBirthDate(rs.getDate("birth_date"));
                    u.setEmail(rs.getString("email"));
                    u.setAvatarId(rs.getInt("avatar_id"));
                    u.setAvatarUrl(rs.getString("urlImage")); // Lấy URL thực tế ở đây
                    return u;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Update profile user, lưu cả avatar_id và avatar_url trực tiếp vào DB
     */
//    public boolean updateUserProfile(User u) {
//        String sql = """
//        UPDATE users
//        SET full_name    = ?,
//            display_name = ?,
//            phone        = ?,
//            gender       = ?,
//            birth_date   = ?,
//            avatar_id    = ?,
//            avatar_url   = ?    -- lưu URL trực tiếp vào DB
//        WHERE id = ?
//    """;
//
//        try (Connection conn = DBContext.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql)) {
//
//            ps.setString(1, u.getUsername());  // sửa thành getFullName() để khớp schema
//            ps.setString(2, u.getDisplayName());
//            ps.setString(3, u.getPhone());
//            ps.setString(4, u.getGender());
//
//            if (u.getBirthDate() != null) {
//                ps.setDate(5, new java.sql.Date(u.getBirthDate().getTime()));
//            } else {
//                ps.setNull(5, java.sql.Types.DATE);
//            }
//
//            if (u.getAvatarId() != null) {
//                ps.setInt(6, u.getAvatarId());
//            } else {
//                ps.setNull(6, java.sql.Types.INTEGER);
//            }
//
//            // Lưu avatar_url trực tiếp vào DB
//            if (u.getAvatarUrl() != null && !u.getAvatarUrl().trim().isEmpty()) {
//                ps.setString(7, u.getAvatarUrl().trim());
//            } else {
//                ps.setNull(7, java.sql.Types.VARCHAR);
//            }
//
//            ps.setInt(8, u.getId());
//
//            int rows = ps.executeUpdate();
//            System.out.println("updateUserProfile: Updated rows = " + rows
//                    + " | avatar_id = " + u.getAvatarId()
//                    + " | avatar_url = " + u.getAvatarUrl());
//            return rows > 0;
//
//        } catch (Exception e) {
//            System.err.println("Lỗi updateUserProfile cho user ID " + u.getId() + ": " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean updateUserAvatarId(int userId, int imageId) {
        String sql = "UPDATE users SET avatar_id = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateUserProfile(User u) {
        // Câu lệnh SQL phải cập nhật avatar_id
        String sql = "UPDATE users SET full_name=?, display_name=?, phone=?, gender=?, birth_date=?, avatar_id=? WHERE id=?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getDisplayName());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getGender());

            if (u.getBirthDate() != null) {
                ps.setDate(5, new java.sql.Date(u.getBirthDate().getTime()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            // Lưu avatar_id (khóa ngoại trỏ sang bảng images)
            if (u.getAvatarId() != null && u.getAvatarId() > 0) {
                ps.setInt(6, u.getAvatarId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            ps.setInt(7, u.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
    public List<Reviews> getUniqueProductsToReview(int orderId) {

        List<Reviews> list = new ArrayList<>();

        String sql = """
        SELECT DISTINCT p.id
        FROM order_details od
        JOIN products p ON od.product_id = p.id
        WHERE od.order_id = ?
    """;

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reviews r = new Reviews();
                r.setProductId(rs.getInt(1));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    // 2. Phương thức lưu đánh giá vào DB
    public boolean insertReview(Reviews r) {

        String sql = """
        INSERT INTO reviews(user_id, product_id, rating, comment)
        VALUES(?,?,?,?)
    """;

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getUserId());
            ps.setInt(2, r.getProductId());
            ps.setInt(3, r.getRating());
            ps.setString(4, r.getComment());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }public void updateOrderRatedStatus(int orderId, boolean status) {

        String sql = "UPDATE orders SET is_rated=? WHERE id=?";

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public User checkLoginGoogle(String email) {
        // Sử dụng LEFT JOIN để lấy luôn ảnh đại diện nếu có
        String sql = """
        SELECT u.*, i.urlImage 
        FROM users u 
        LEFT JOIN images i ON u.avatar_id = i.id 
        WHERE u.email = ?
    """;
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    // LẤY ĐỦ THÌ MỚI HIỆN ĐỦ
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("full_name")); // Đây là tên bạn đã cập nhật
                    user.setDisplayName(rs.getString("display_name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));       // Đây là số điện thoại bạn đã cập nhật
                    user.setRole(rs.getString("role"));
                    user.setStatus(rs.getString("status"));
                    user.setAvatarUrl(rs.getString("urlImage")); // URL ảnh

                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean insertUserFromGoogle(User user) {
        // Sửa 'fullname' thành 'full_name' cho khớp với bảng users của bạn
        String sql = "INSERT INTO users (email, full_name, role, status, password, createAt) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getStatus());
            ps.setString(5, user.getPassword());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

public boolean updateAdminRole(int adminId, String newRole) {
        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection(); // Thay bằng cách lấy connection của bạn
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newRole);
            ps.setInt(2, adminId);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<User> getAllAdmins() {
        List<User> list = new ArrayList<>();
        // Lọc những người dùng có quyền là 'Admin'
        String sql = "SELECT * FROM users WHERE role = 'Admin' or role = 'Staff'";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("full_name"));
                u.setDisplayName(rs.getString("display_name"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setRole(rs.getString("role"));
                u.setStatus(rs.getString("status"));
                u.setCreateAt(rs.getDate("createAt"));

                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}

