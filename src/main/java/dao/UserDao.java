package dao;

import model.Address;
import model.User;

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

        String sql = """
        SELECT id, full_name, display_name, birth_date,
               email, phone, gender, avatar_id,
               role, status, createAt
        FROM users
        WHERE email = ? AND password = ?
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

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
                    u.setRole(rs.getString("role"));
                    u.setStatus(rs.getString("status"));
                    u.setCreateAt(rs.getDate("createAt"));
                    return u;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

    /**
     * Thực hiện đăng ký tài khoản mới.
     * Mặc định set role='User' và status='Active'.
     */
    public void signup(String username, String email, String password) {
        String SQL = "INSERT INTO users (full_name, email, password, role, status,createAt) VALUES (?, ?, ?, 'User', 'Active',?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis())); // 👈 THÊM DÒNG NÀY
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            ps.setString(1, newPassword);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        }
    }

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
    public boolean updatePasswordByEmail(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, email);

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
//    public boolean deleteUser(int userId) throws Exception {
//
//        String deleteAddressSQL = "DELETE FROM addresses WHERE user_id = ?";
//        String removeAvatarSQL = "UPDATE users SET avatar_id = NULL WHERE id = ?";
//        String deleteUserSQL = "DELETE FROM users WHERE id = ?";
//
//        try (Connection conn = DBContext.getConnection()) {
//            conn.setAutoCommit(false); // TRANSACTION
//
//            try (
//                    PreparedStatement psAddr = conn.prepareStatement(deleteAddressSQL);
//                    PreparedStatement psAvatar = conn.prepareStatement(removeAvatarSQL);
//                    PreparedStatement psUser = conn.prepareStatement(deleteUserSQL)
//            ) {
//                // 1. Xóa địa chỉ
//                psAddr.setInt(1, userId);
//                psAddr.executeUpdate();
//
//                // 2. Gỡ avatar
//                psAvatar.setInt(1, userId);
//                psAvatar.executeUpdate();
//
//                // 3. Xóa user
//                psUser.setInt(1, userId);
//                int rows = psUser.executeUpdate();
//
//                conn.commit();
//                return rows > 0;
//
//            } catch (Exception e) {
//                conn.rollback();
//                throw e;
//            }
//        }
//    }
    /**
     * Xóa người dùng (bao gồm xóa địa chỉ và gỡ liên kết ảnh đại diện)
     * Sử dụng Transaction để đảm bảo tính toàn vẹn dữ liệu.
     */
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

    public List<User> getAllAdmins() {
        List<User> list = new ArrayList<>();
        // Lọc những người dùng có quyền là 'Admin'
        String sql = "SELECT * FROM users WHERE role = 'Admin'";

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

    public boolean updateUserProfile(User u) {
        String sql = "UPDATE users SET full_name=?, display_name=?, phone=?, gender=?, birth_date=? WHERE id=?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());    // full_name
            ps.setString(2, u.getDisplayName()); // display_name
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getGender());
            ps.setDate(5, (java.sql.Date) u.getBirthDate());
            ps.setInt(6, u.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
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

}
