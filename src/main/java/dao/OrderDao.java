package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.CartItem;
import model.Order;
import model.OrderDetail;
import model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class OrderDao {
    public int getOrderCount() throws Exception {
        String sql = "SELECT COUNT(*) AS cnt FROM orders WHERE payment_status = ? AND status = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "Đã thanh toán");
            ps.setString(2, "Đã giao");

            try(ResultSet rs = ps.executeQuery()){

                if (rs.next()) {
                    int count = rs.getInt("cnt");
                    System.out.println("Order Count from DB: " + count); // Log giá trị lấy được
                    return count;
                }
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu xảy ra
            throw new Exception("Failed to fetch order count!");
        }
    }
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        // Truy vấn đầy đủ các cột tiền đã lưu trong DB
        String sql = "SELECT id, user_id, fullName, phone, status, payment_status, " +
                "totalOrder, subTotal, taxAmount, shippingFee, createAt " +
                "FROM orders WHERE user_id = ? ORDER BY createAt DESC";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setFullName(rs.getString("fullName"));
                o.setStatus(rs.getString("status"));
                o.setPaymentStatus(rs.getString("payment_status"));
                o.setCreateAt(rs.getTimestamp("createAt"));

                // Lấy chính xác giá trị từ các cột trong Database
                o.setSubTotal(rs.getDouble("subTotal"));
                o.setTaxAmount(rs.getDouble("taxAmount"));
                o.setShippingFee(rs.getDouble("shippingFee"));
                o.setTotalOrder(rs.getDouble("totalOrder"));
                list.add(o);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
//    private List<OrderDetail> getDetailsByOrderId(int orderId) {
//        List<OrderDetail> details = new ArrayList<>();
//        String sql = "SELECT od.*, p.nameProduct, p.image FROM order_details od " +
//                "JOIN product_variants pv ON od.product_variant_id = pv.id " +
//                "JOIN products p ON pv.product_id = p.id WHERE od.order_id = ?";
//        try (Connection con = DBContext.getConnection();
//             PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setInt(1, orderId);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                OrderDetail d = new OrderDetail();
//                d.setProductName(rs.getString("nameProduct"));
//                d.setProductImg(rs.getString("image"));
//                d.setQuantity(rs.getInt("quantity"));
//                d.setTotal(rs.getDouble("total"));
//                details.add(d);
//            }
//        } catch (Exception e) { e.printStackTrace(); }
//        return details;
//    }

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

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        // JOIN với bảng addresses dựa trên cột address_id (hoặc cột liên kết trong DB của bạn)
        String sql = """
        SELECT o.*, 
               CONCAT(a.detail, ', ', a.commune, ', ', a.district, ', ', a.province) AS full_address
        FROM orders o
        LEFT JOIN addresses a ON o.address_id = a.id
        ORDER BY o.createAt DESC
    """;

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setFullName(rs.getString("fullName"));
                order.setPhone(rs.getString("phone"));
                order.setSubTotal(rs.getDouble("subTotal"));     // Tiền hàng
                order.setTaxAmount(rs.getDouble("taxAmount"));   // Thuế 8%
                order.setShippingFee(rs.getDouble("shippingFee")); // Phí ship
                // Lấy địa chỉ đầy đủ đã được nối chuỗi từ SQL
                String addr = rs.getString("full_address");
                // Nếu đơn hàng không có address_id (địa chỉ cũ), lấy từ cột address của bảng orders
                if (addr == null || addr.trim().isEmpty() || addr.equals(", , , ")) {
                    order.setAddress(rs.getString("address"));
                } else {
                    order.setAddress(addr);
                }

                order.setCreateAt(rs.getTimestamp("createAt"));
                order.setPaymentStatus(rs.getString("payment_status"));
                order.setTotalOrder(rs.getDouble("totalOrder"));
                order.setStatus(rs.getString("status"));
                order.setNote(rs.getString("note"));

                list.add(order);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean updateStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<OrderDetail> getDetailsByOrderId(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        String sql = """
        SELECT od.*, p.name_product, p.primary_image_id, pv.product_id
        FROM order_details od 
        JOIN product_variants pv ON od.product_variant_id = pv.id 
        JOIN products p ON pv.product_id = p.id 
        WHERE od.order_id = ?
    """;
        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetail d = new OrderDetail();
                d.setId(rs.getInt("id"));
                d.setOrderId(rs.getInt("order_id"));
                d.setProductVariantId(rs.getInt("product_variant_id"));
                d.setProductId(rs.getInt("product_id"));
                d.setProductName(rs.getString("name_product"));
                d.setProductImg(rs.getString("primary_image_id"));
                d.setQuantity(rs.getInt("quantity"));
                d.setTotal(rs.getDouble("total"));
                details.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return details;
    }
    public boolean updateOrderStatus(int orderId, String status, String paymentStatus) {
        String sql = "UPDATE orders SET status = ?, payment_status = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, paymentStatus);
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }// Cập nhật trạng thái cho người dùng (Hủy/Hoàn hàng)
    public boolean updateStatusByUser(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    // Lấy tổng doanh thu của TẤT CẢ đơn hàng (bao gồm cả chưa giao)
// Thêm vào class OrderDao
    public boolean cancelOrReturnOrder(int orderId, String newStatus) {
        String sqlUpdateStatus = "UPDATE orders SET status = ? WHERE id = ?";
        String sqlGetDetails = "SELECT product_variant_id, quantity FROM order_details WHERE order_id = ?";
        String sqlRestoreStock = "UPDATE product_variants SET inventory_quantity = inventory_quantity + ? WHERE id = ?";

        Connection conn = null;
        try {
            conn = DBContext.getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Cập nhật trạng thái đơn hàng
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateStatus)) {
                ps.setString(1, newStatus);
                ps.setInt(2, orderId);
                ps.executeUpdate();
            }

            // 2. Lấy danh sách sản phẩm để trả kho
            try (PreparedStatement psGet = conn.prepareStatement(sqlGetDetails)) {
                psGet.setInt(1, orderId);
                ResultSet rs = psGet.executeQuery();

                try (PreparedStatement psRestore = conn.prepareStatement(sqlRestoreStock)) {
                    while (rs.next()) {
                        psRestore.setInt(1, rs.getInt("quantity"));
                        psRestore.setInt(2, rs.getInt("product_variant_id"));
                        psRestore.addBatch();
                    }
                    psRestore.executeBatch();
                }
            }

            conn.commit(); // Thành công hết thì mới lưu
            return true;
        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    public double getTotalRevenue() {
        // Sử dụng tên cột chính xác từ file SQL của bạn: payment_status và status
        String sql = "SELECT SUM(totalOrder) FROM orders WHERE payment_status = ? AND status = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "Đã thanh toán");
            ps.setString(2, "Đã giao");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

}