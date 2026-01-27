package dao;

import model.ContactSettings;
import java.sql.*;

public class ContactSettingsDao {
    private static final String GET_SETTINGS_SQL =
            "SELECT * FROM contact_settings WHERE id = 1 LIMIT 1";

    private static final String UPDATE_SETTINGS_SQL =
            "UPDATE contact_settings SET " +
                    "phone = ?, email = ?, address = ?, " +
                    "facebook_url = ?, instagram_url = ?, twitter_url = ?, google_url = ? " +
                    "WHERE id = 1";

    // Lấy thông tin settings (chỉ có 1 bản ghi)
    public ContactSettings getSettings() {
        ContactSettings settings = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(GET_SETTINGS_SQL);
            rs = ps.executeQuery();

            if (rs.next()) {
                settings = new ContactSettings();
                settings.setId(rs.getInt("id"));
                settings.setPhone(rs.getString("phone"));
                settings.setEmail(rs.getString("email"));
                settings.setAddress(rs.getString("address"));
                settings.setFacebookUrl(rs.getString("facebook_url"));
                settings.setInstagramUrl(rs.getString("instagram_url"));
                settings.setTwitterUrl(rs.getString("twitter_url"));
                settings.setGoogleUrl(rs.getString("google_url"));
            } else {
                // Nếu chưa có bản ghi → tạo mặc định
                createDefaultSettings();
                return getSettings(); // gọi lại để lấy
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
        return settings;
    }

    // Tạo bản ghi mặc định nếu chưa có
    private void createDefaultSettings() {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            String sql = "INSERT INTO contact_settings (id, phone, email, address, facebook_url, instagram_url, twitter_url, google_url) " +
                    "VALUES (1, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "+84 123 456 789");
            ps.setString(2, "contact@company.com");
            ps.setString(3, "Đại học Nông Lâm TP.HCM");
            ps.setString(4, "https://facebook.com/yourshop");
            ps.setString(5, "https://instagram.com/yourshop");
            ps.setString(6, "https://twitter.com/yourshop");
            ps.setString(7, "https://google.com/search?q=yourshop");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    // Cập nhật settings từ admin
    public boolean updateSettings(ContactSettings settings) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(UPDATE_SETTINGS_SQL);
            ps.setString(1, settings.getPhone());
            ps.setString(2, settings.getEmail());
            ps.setString(3, settings.getAddress());
            ps.setString(4, settings.getFacebookUrl());
            ps.setString(5, settings.getInstagramUrl());
            ps.setString(6, settings.getTwitterUrl());
            ps.setString(7, settings.getGoogleUrl());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }
}