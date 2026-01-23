package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.CartItem;
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

        String sql = """
        SELECT 
            o.id,
            o.user_id,
            o.fullName,
            o.phone,
            o.status,
            o.payment_status,
            o.totalOrder,
            o.createAt
        FROM orders o
        WHERE o.user_id = ?
        ORDER BY o.createAt DESC
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Order o = new Order();

                o.setId(rs.getInt("id"));
                o.setUserId(rs.getInt("user_id"));
                o.setFullName(rs.getString("fullName"));
                o.setPhone(rs.getString("phone"));
                o.setStatus(rs.getString("status"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setCreateAt(rs.getTimestamp("createAt"));

                // 🔥 DÒNG QUAN TRỌNG NHẤT
                o.setTotalOrder(rs.getDouble("totalOrder"));

                list.add(o);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    private List<OrderDetail> getDetailsByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = "SELECT od.*, p.nameProduct, p.image FROM order_details od " +
                "JOIN product_variants pv ON od.product_variant_id = pv.id " +
                "JOIN products p ON pv.product_id = p.id WHERE od.order_id = ?";
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetail d = new OrderDetail();
                d.setProductName(rs.getString("nameProduct"));
                d.setProductImg(rs.getString("image"));
                d.setQuantity(rs.getInt("quantity"));
                d.setTotal(rs.getDouble("total"));
                details.add(d);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return details;
    }

    public int insertOrder(int userId, String fullName, String phone, String address,
                           String note, String paymentMethod, List<CartItem> cart) throws Exception {

        if (cart == null || cart.isEmpty()) {
            throw new IllegalArgumentException("Giỏ hàng rỗng!");
        }

        Connection con = null;
        try {
            con = DBContext.getConnection();
            con.setAutoCommit(false);  // Bắt đầu transaction

            // 1. Insert vào bảng orders
            String sqlOrder = "INSERT INTO orders (user_id, fullName, phone, address, note, status, payment_status, createAt) " +
                    "VALUES (?, ?, ?, ?, ?, 'Chờ xác nhận', ?, NOW())";

            PreparedStatement psOrder = con.prepareStatement(sqlOrder);
            psOrder.setInt(1, userId);
            psOrder.setString(2, fullName);
            psOrder.setString(3, phone);
            psOrder.setString(4, address);
            psOrder.setString(5, note);

            // Xử lý payment_status
            String payStatus = "cod".equalsIgnoreCase(paymentMethod) ? "Chưa thanh toán" : "Đã thanh toán";
            psOrder.setString(6, payStatus);

            int affected = psOrder.executeUpdate();
            if (affected == 0) {
                throw new SQLException("Insert orders thất bại, không có dòng nào được thêm");
            }

            // 2. Lấy orderId vừa insert (cách ổn định với MySQL)
            int orderId = -1;
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() AS id")) {
                if (rs.next()) {
                    orderId = rs.getInt("id");
                    System.out.println("Đã tạo đơn hàng mới - orderId = " + orderId);
                } else {
                    throw new SQLException("Không lấy được LAST_INSERT_ID()");
                }
            }

            if (orderId <= 0) {
                throw new SQLException("orderId không hợp lệ: " + orderId);
            }

            // 3. Insert chi tiết đơn hàng (batch để nhanh)
            String sqlDetail = "INSERT INTO order_details (order_id, product_variant_id, quantity, total) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement psDetail = con.prepareStatement(sqlDetail);

            for (CartItem item : cart) {
                if (item.getVariant() == null || item.getVariant().getId() <= 0) {
                    throw new IllegalStateException("Variant không hợp lệ cho item: " + item);
                }

                BigDecimal price = item.getVariant().getVariant_price();
                if (price == null) {
                    throw new IllegalStateException("Giá variant null cho variantId: " + item.getVariant().getId());
                }

                BigDecimal total = price.multiply(BigDecimal.valueOf(item.getQuantity()));

                psDetail.setInt(1, orderId);
                psDetail.setInt(2, item.getVariant().getId());
                psDetail.setInt(3, item.getQuantity());
                psDetail.setBigDecimal(4, total);
                psDetail.addBatch();
            }

            int[] batchResults = psDetail.executeBatch();
            // Kiểm tra batch (tùy chọn)
            for (int res : batchResults) {
                if (res == PreparedStatement.EXECUTE_FAILED) {
                    throw new SQLException("Một hoặc nhiều chi tiết đơn hàng insert thất bại");
                }
            }

            con.commit();  // Thành công → commit
            return orderId;

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                    System.out.println("Rollback transaction do lỗi: " + e.getMessage());
                } catch (SQLException rollbackEx) {
                    // ignore
                }
            }
            e.printStackTrace();
            throw new Exception("Lỗi khi đặt hàng: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (Exception ignored) {}
            }
        }
    }
    public Order getOrderById(int orderId) {
        String sql = """
        SELECT *
        FROM orders
        WHERE id = ?
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setFullName(rs.getString("fullName"));
                o.setPhone(rs.getString("phone"));
                o.setAddress(rs.getString("address"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setStatus(rs.getString("status"));
                o.setCreateAt(rs.getTimestamp("createAt"));
                o.setTotalOrder(rs.getDouble("totalOrder"));
                return o;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}