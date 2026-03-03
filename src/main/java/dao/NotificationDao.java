package dao;

import model.Notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;;
import java.util.List;


public class NotificationDao {
    public void createContactNotification(int adminId, int contactId, String content) {

        String sql = """
            INSERT INTO notifications(admin_id, type, related_id, content, createAt, isRead)
            VALUES (?, 'CONTACT', ?, ?, NOW(), 0)
        """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ps.setInt(2, contactId);
            ps.setString(3, content);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Notification> getTopNotifications(int adminId) {
        List<Notification> list = new ArrayList<>();

        String sql = """
        SELECT * FROM notifications
        WHERE admin_id = ?
        ORDER BY createAt DESC
        LIMIT 5
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, adminId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Notification n = new Notification();
                n.setId(rs.getInt("id"));
                n.setType(rs.getString("type"));
                n.setContent(rs.getString("content"));
                n.setCreateAt(rs.getTimestamp("createAt"));
                n.setRead(rs.getBoolean("isRead"));
                list.add(n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public void insert(Notification n) {
        String sql = """
        INSERT INTO notifications
        (admin_id, type, related_id, content, createAt, isRead)
        VALUES (?, 'CONTACT', ?, ?, ?, ?)
    """;

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, n.getAdminId());
            ps.setInt(2, n.getRelatedId());
            ps.setString(3, n.getContent());
            ps.setTimestamp(4, n.getCreateAt());
            ps.setBoolean(5, n.isRead());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
