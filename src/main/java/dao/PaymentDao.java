package dao;
import model.Payment;
import java.sql.*;
import java.util.*;

public class PaymentDao {
    // 1. Lấy danh sách thẻ
    public List<Payment> getPaymentsByUserId(int userId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE user_id = ?";
        try (Connection conn = new dao.DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Payment(rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getString(4)));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // 2. Thêm thẻ mới
    public void addPayment(int userId, String type, String duration) {
        String sql = "INSERT INTO payments (user_id, type, duration) VALUES (?, ?, ?)";
        try (Connection conn = new dao.DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setString(3, duration);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}