package dao;
import model.Payment;
import java.sql.*;
import java.util.*;

public class PaymentDao {
    public List<Payment> getPaymentsByUserId(int userId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT id, user_id, card_number, duration, type FROM payments WHERE user_id = ?";
        try (Connection conn = new dao.DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Chú ý thứ tự cột: id=1, user_id=2, card_number=3, duration=4, type=5
                list.add(new Payment(rs.getInt("id"), rs.getInt("user_id"), rs.getString("card_number"), rs.getDate("duration"), rs.getString("type")));

            }
        } catch (Exception e) { e.printStackTrace(); }
        System.out.println("User ID: " + userId);
        System.out.println("Payments retrieved: " + list.size());
        return list;
    }

    public void addPayment(int userId, String cardNumber, String type, String duration) {
        String sql = "INSERT INTO payments (user_id, card_number, type, duration) VALUES (?, ?, ?, ?)";
        try (Connection conn = new dao.DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, cardNumber);
            ps.setString(3, type);
            ps.setString(4, duration);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public static void main(String[] args) {
        int testUserId = 1; // Thay đổi user ID theo dữ liệu trong DB
        PaymentDao paymentDao = new PaymentDao();

        List<Payment> payments = paymentDao.getPaymentsByUserId(testUserId);
        System.out.println("Payments retrieved: " + payments.size());
        for (Payment payment : payments) {
            System.out.println(payment);
        }
    }
    public boolean deletePayment(int id) {
        String sql = "DELETE FROM payments WHERE id = ?";
        try (Connection conn = DBContext.getConnection(); // Thay bằng cách lấy connection của bạn
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}