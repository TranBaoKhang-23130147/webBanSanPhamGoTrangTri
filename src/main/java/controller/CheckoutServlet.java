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

        if (user == null) {
            response.sendRedirect("login.jsp?error=not_logged_in");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("CartServlet?action=view&error=empty_cart");
            return;
        }


        String fullName      = request.getParameter("fullName");
        String phone         = request.getParameter("phone");
        String addressIdStr  = request.getParameter("address_id");
        String note          = request.getParameter("note");
        String paymentMethod = request.getParameter("paymentMethod");
        String cardIdStr     = request.getParameter("cardId");

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "COD";
        }
        paymentMethod = paymentMethod.toUpperCase();

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

            String sqlOrder = """
    INSERT INTO orders 
    (user_id, fullName, phone, address_id, note, createAt, status, payment_status, payment_id, 
     totalOrder, subTotal, taxAmount, shippingFee)
    VALUES (?, ?, ?, ?, ?, NOW(), 'Chờ xác nhận', ?, ?, ?, ?, ?, ?)
""";

            String paymentStatus = "COD".equals(paymentMethod) ? "Chưa thanh toán" : "Đã thanh toán";

            int orderId;
            try (PreparedStatement psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setInt(1, user.getId());
                psOrder.setString(2, fullName);
                psOrder.setString(3, phone);
                psOrder.setInt(4, addressId);
                psOrder.setString(5, note != null ? note : "");
                psOrder.setString(6, paymentStatus);

                if ("COD".equals(paymentMethod) || paymentId == null) {
                    psOrder.setNull(7, Types.INTEGER);
                } else {
                    psOrder.setInt(7, paymentId);
                }

                psOrder.setBigDecimal(8, grandTotal);
                psOrder.setBigDecimal(9, subTotal);
                psOrder.setBigDecimal(10, tax);
                psOrder.setBigDecimal(11, shipping);

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

            String sqlUpdateStock = """
                UPDATE product_variants 
                SET inventory_quantity = inventory_quantity - ? 
                WHERE id = ? AND inventory_quantity >= ?
            """;

            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStock)) {
                for (CartItem item : cart) {
                    int qtyOrdered = item.getQuantity();
                    int variantId = item.getVariant().getId();

                    psUpdate.setInt(1, qtyOrdered);
                    psUpdate.setInt(2, variantId);
                    psUpdate.setInt(3, qtyOrdered);

                    int rowsAffected = psUpdate.executeUpdate();

                    if (rowsAffected == 0) {
                        throw new SQLException("Biến thể ID " + variantId +
                                " không đủ tồn kho (còn ít hơn " + qtyOrdered + ")");
                    }

                    System.out.println("Đã trừ " + qtyOrdered + " sản phẩm cho variant ID: " + variantId +
                            " (tồn kho còn lại sẽ giảm tương ứng)");
                }
            }

            conn.commit();
            if (fullCart != null) {
                for (CartItem boughtItem : cart) {
                    fullCart.removeIf(item -> item.getVariant().getId() == boughtItem.getVariant().getId());
                }
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