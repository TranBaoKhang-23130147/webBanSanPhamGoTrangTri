package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrderDao {
    public int getOrderCount() throws Exception {
        String sql = "SELECT COUNT(*) AS cnt FROM orders";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt("cnt");
                System.out.println("Order Count from DB: " + count); // Log giá trị lấy được
                return count;
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu xảy ra
            throw new Exception("Failed to fetch order count!");
        }
    }
}
