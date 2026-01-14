package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.Order;
import model.OrderDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
    // Hàm lấy tổng số đơn và tổng tiền theo User ID

        public List<Order> getOrdersByUserId(int userId) {
            List<Order> list = new ArrayList<>();
            // Câu SQL JOIN 3 bảng: orders, order_details và product_variants (để lấy tên/ảnh sản phẩm)
            String sql = "SELECT o.id, o.createAt, o.status, o.payment_status, " +
                    "od.quantity, od.total, pv.name, pv.color, pv.size, img.urlImage " +
                    "FROM orders o " +
                    "JOIN order_details od ON o.id = od.order_id " +
                    "JOIN product_variants pv ON od.product_variant_id = pv.id " +
                    "LEFT JOIN images img ON pv.image_id = img.id " +
                    "WHERE o.user_id = ?";

            try (Connection conn = DBContext.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Order o = new Order();
                        o.setId(rs.getInt("order_id"));
                        o.setCreateAt(rs.getTimestamp("createAt"));
                        o.setStatus(rs.getString("status"));

                        // Tạo chi tiết sản phẩm cho dòng này
                        OrderDetail det = new OrderDetail();
                        det.setProductName(rs.getString("product_name"));
                        det.setProductImg(rs.getString("urlImage"));
                        det.setColor(rs.getString("color"));
                        det.setSize(rs.getString("size"));
                        det.setQuantity(rs.getInt("quantity"));
                        det.setTotal(rs.getDouble("total"));

                        // Gán tổng tiền đơn hàng (trong ảnh bạn là total ở bảng chi tiết)
                        o.setTotalOrder(rs.getDouble("total"));

                        // Đưa vào danh sách (để đơn giản mỗi row kết quả là 1 item trên giao diện)
                        List<OrderDetail> details = new ArrayList<>();
                        details.add(det);
                        o.setDetails(details);

                        list.add(o);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }

}