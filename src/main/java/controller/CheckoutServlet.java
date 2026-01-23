package controller;

import dao.DBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.CartItem;
import model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");
        @SuppressWarnings("unchecked")
        List<CartItem> fullCart = (List<CartItem>) session.getAttribute("CART");
        String[] selectedIds = request.getParameterValues("selectedItems");

        if (fullCart == null || selectedIds == null || selectedIds.length == 0) {
            response.sendRedirect("CartServlet?action=view&error=empty_selection");
            return;
        }

// 2. Lọc ra danh sách 'cart' - chỉ chứa các món ĐƯỢC CHỌN
        List<CartItem> cart = new ArrayList<>();
        for (String idStr : selectedIds) {
            int id = Integer.parseInt(idStr);
            for (CartItem item : fullCart) {
                if (item.getVariant().getId() == id) {
                    cart.add(item);
                    break;
                }
            }
        }

        // 1. Kiểm tra điều kiện cơ bản
        if (user == null) {
            response.sendRedirect("login.jsp?error=not_logged_in");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("CartServlet?action=view&error=empty_cart");
            return;
        }


        // 2. Lấy thông tin từ form
        String fullName      = request.getParameter("fullName");
        String phone         = request.getParameter("phone");
        String addressIdStr  = request.getParameter("address_id");
        String note          = request.getParameter("note");
        String paymentMethod = request.getParameter("paymentMethod");
        String cardIdStr     = request.getParameter("cardId");

        // Bảo vệ: mặc định COD
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "COD";
        }
        paymentMethod = paymentMethod.toUpperCase();

        // Xử lý address_id (bắt buộc)
        Integer addressId = null;
        if (addressIdStr != null && !addressIdStr.trim().isEmpty()) {
            try {
                addressId = Integer.parseInt(addressIdStr);
            } catch (NumberFormatException e) {
                response.sendRedirect("CartServlet?action=view&error=1&msg=" +
                        URLEncoder.encode("Địa chỉ không hợp lệ", StandardCharsets.UTF_8));
                return;
            }
        }
        if (addressId == null) {
            response.sendRedirect("CartServlet?action=view&error=1&msg=" +
                    URLEncoder.encode("Vui lòng chọn địa chỉ nhận hàng từ danh sách", StandardCharsets.UTF_8));
            return;
        }

        // Xử lý payment_id (nếu BANK)
        Integer paymentId = null;
        if ("BANK".equals(paymentMethod)) {
            if (cardIdStr != null && !cardIdStr.trim().isEmpty()) {
                try {
                    paymentId = Integer.parseInt(cardIdStr);
                } catch (NumberFormatException ignored) {
                    paymentId = null;
                }
            }
            if (paymentId == null) {
                paymentMethod = "COD";
                paymentId = null;
            }
        }

        // Debug log
        System.out.println("=== CHECKOUT BẮT ĐẦU ===");
        System.out.println("User ID: " + user.getId());
        System.out.println("Cart size: " + cart.size());
        System.out.println("FullName: " + fullName);
        System.out.println("Phone: " + phone);
        System.out.println("Address ID: " + addressId);
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Payment ID: " + paymentId);
        System.out.println("Note: " + note);

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);

            // 3. Tính tổng tiền backend
            BigDecimal subTotal = BigDecimal.ZERO;
            for (CartItem item : cart) {
                if (item.getVariant() == null || item.getVariant().getVariant_price() == null) {
                    throw new IllegalStateException("Dữ liệu variant không hợp lệ");
                }
                BigDecimal itemTotal = item.getVariant().getVariant_price()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                subTotal = subTotal.add(itemTotal);
            }

            BigDecimal tax = subTotal.multiply(BigDecimal.valueOf(0.08));
            BigDecimal shipping = calculateShipping(subTotal);
            BigDecimal grandTotal = subTotal.add(tax).add(shipping);

            System.out.println("Tổng tiền backend: Subtotal=" + subTotal + " | Tax=" + tax +
                    " | Shipping=" + shipping + " | Total=" + grandTotal);

            // 4. Insert vào bảng orders (đầy đủ cột tiền)
            String sqlOrder = """
    INSERT INTO orders 
    (user_id, fullName, phone, address_id, note, createAt, status, payment_status, payment_id, 
     totalOrder, subTotal, taxAmount, shippingFee)
    VALUES (?, ?, ?, ?, ?, NOW(), 'Chờ xác nhận', ?, ?, ?, ?, ?, ?)
""";

// Xác định trạng thái thanh toán dựa trên phương thức
            String paymentStatus = "COD".equals(paymentMethod) ? "Chưa thanh toán" : "Đã thanh toán";

            int orderId;
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, user.getId());
                psOrder.setString(2, fullName);
                psOrder.setString(3, phone);
                psOrder.setInt(4, addressId);
                psOrder.setString(5, note != null ? note : "");
                psOrder.setString(6, paymentStatus); // payment_status

                // XỬ LÝ PAYMENT_ID: Nếu là COD thì ép buộc set null
                if ("COD".equals(paymentMethod) || paymentId == null) {
                    psOrder.setNull(7, Types.INTEGER); // payment_id = null
                } else {
                    psOrder.setInt(7, paymentId);      // payment_id thực tế từ thẻ ngân hàng
                }

                psOrder.setBigDecimal(8, grandTotal);     // totalOrder
                psOrder.setBigDecimal(9, subTotal);       // subTotal
                psOrder.setBigDecimal(10, tax);           // taxAmount
                psOrder.setBigDecimal(11, shipping);      // shippingFee

                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Không lấy được orderId");
                    }
                }
            }

            System.out.println("Tạo đơn hàng thành công - Order ID: " + orderId);

            // 5. Insert order_details (batch)
            String sqlDetail = """
                INSERT INTO order_details 
                (order_id, product_variant_id, quantity, total)
                VALUES (?, ?, ?, ?)
            """;

            try (PreparedStatement psDetail = conn.prepareStatement(sqlDetail)) {
                for (CartItem item : cart) {
                    BigDecimal itemTotal = item.getVariant().getVariant_price()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

                    psDetail.setInt(1, orderId);
                    psDetail.setInt(2, item.getVariant().getId());
                    psDetail.setInt(3, item.getQuantity());
                    psDetail.setBigDecimal(4, itemTotal);
                    psDetail.addBatch();
                }
                psDetail.executeBatch();
            }

            // 6. Trừ tồn kho (inventory_quantity) trong bảng product_variant
            String sqlUpdateStock = """
                UPDATE product_variants 
                SET inventory_quantity = inventory_quantity - ? 
                WHERE id = ? AND inventory_quantity >= ?
            """;

            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock)) {
                for (CartItem item : cart) {
                    int qtyOrdered = item.getQuantity();
                    int variantId = item.getVariant().getId();

                    psUpdate.setInt(1, qtyOrdered);          // trừ số lượng đặt
                    psUpdate.setInt(2, variantId);           // biến thể nào
                    psUpdate.setInt(3, qtyOrdered);          // kiểm tra còn đủ không

                    int rowsAffected = psUpdate.executeUpdate();

                    if (rowsAffected == 0) {
                        throw new SQLException("Biến thể ID " + variantId +
                                " không đủ tồn kho (còn ít hơn " + qtyOrdered + ")");
                    }

                    System.out.println("Đã trừ " + qtyOrdered + " sản phẩm cho variant ID: " + variantId +
                            " (tồn kho còn lại sẽ giảm tương ứng)");
                }
            }

            // 7. Commit toàn bộ (đơn hàng + trừ kho)
            conn.commit();
            if (fullCart != null) {
                // Xóa những món vừa mua ra khỏi giỏ hàng tổng
                for (CartItem boughtItem : cart) {
                    fullCart.removeIf(item -> item.getVariant().getId() == boughtItem.getVariant().getId());
                }
                // Cập nhật lại session với những món còn sót lại
                session.setAttribute("CART", fullCart);
            }
            response.sendRedirect("MyPageServlet?tab=don-hang&success=1&orderId=" + orderId);

        } catch (Exception e) {
            System.err.println("CHECKOUT THẤT BẠI: " + e.getMessage());
            e.printStackTrace();

            String errMsg = URLEncoder.encode("Đặt hàng thất bại: " + e.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect("CartServlet?action=view&error=1&msg=" + errMsg);
        }
    }

    private BigDecimal calculateShipping(BigDecimal subTotal) {
        if (subTotal.compareTo(BigDecimal.valueOf(100_000)) < 0) return BigDecimal.ZERO;
        if (subTotal.compareTo(BigDecimal.valueOf(1_000_000)) < 0) return BigDecimal.valueOf(50_000);
        if (subTotal.compareTo(BigDecimal.valueOf(3_000_000)) < 0) return BigDecimal.valueOf(100_000);
        if (subTotal.compareTo(BigDecimal.valueOf(5_000_000)) < 0) return BigDecimal.valueOf(200_000);
        if (subTotal.compareTo(BigDecimal.valueOf(10_000_000)) < 0) return BigDecimal.valueOf(500_000);
        return BigDecimal.valueOf(1_000_000);
    }
}