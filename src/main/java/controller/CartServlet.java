package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.CartItem;
import model.Product;
import model.ProductVariants;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import dao.AddressDao;
import dao.PaymentDao;
import model.User;

@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("updateQtyAjax".equals(action)) {
            HttpSession session = request.getSession(false);
            List<CartItem> cart = (session != null) ? (List<CartItem>) session.getAttribute("CART") : null;

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Debug giỏ hàng ngay đầu
            System.out.println("=== DEBUG FULL CART IN SERVLET (AJAX UPDATE QTY) ===");
            if (cart == null || cart.isEmpty()) {
                System.out.println("Giỏ hàng NULL hoặc rỗng");
                response.setStatus(400);
                response.getWriter().write("{\"success\":false, \"message\":\"Giỏ hàng rỗng hoặc session không tồn tại\"}");
                response.getWriter().flush();
                return;
            }

            for (CartItem item : cart) {
                ProductVariants v = item.getVariant();
                System.out.println("  - Item: variantId = " + (v != null ? v.getId() : "NULL")
                        + ", productName = " + (item.getProduct() != null ? item.getProduct().getNameProduct() : "NULL")
                        + ", quantity = " + item.getQuantity());
            }

            try {
                String variantIdRaw = request.getParameter("variantId");
                String quantityRaw  = request.getParameter("quantity");

                if (variantIdRaw == null || variantIdRaw.trim().isEmpty()
                        || quantityRaw == null || quantityRaw.trim().isEmpty()) {

                    System.out.println("LỖI: variantId hoặc quantity rỗng");
                    response.setStatus(400);
                    response.getWriter().write("{\"success\":false, \"message\":\"Thiếu tham số\"}");
                    response.getWriter().flush();
                    return;
                }

                int variantId = Integer.parseInt(variantIdRaw);
                int quantity  = Integer.parseInt(quantityRaw);


                // --- CHÈN CODE KIỂM TRA TỒN KHO VÀO ĐÂY ---
                ProductDao pDao = new ProductDao();
                ProductVariants dbVariant = pDao.getVariantById(variantId);

                if (dbVariant == null) {
                    response.setStatus(404);
                    response.getWriter().write("{\"success\":false, \"message\":\"Biến thể không tồn tại\"}");
                    return;
                }

                if (quantity > dbVariant.getInventory_quantity()) {
                    response.setStatus(400); // Trả về lỗi 400 để Ajax bắt được
                    response.getWriter().write("{\"success\":false, \"message\":\"Số lượng vượt quá tồn kho hiện có (" + dbVariant.getInventory_quantity() + ")\"}");
                    response.getWriter().flush();
                    return; // Dừng xử lý, không cập nhật giỏ hàng nữa
                }



                boolean found = false;
                for (CartItem item : cart) {
                    ProductVariants variant = item.getVariant();
                    if (variant == null) {
                        System.out.println("Cảnh báo: Item có variant NULL! Bỏ qua item này.");
                        continue;
                    }

                    int itemVariantId = variant.getId();
                    System.out.println("  So sánh: item variantId = " + itemVariantId + " vs gửi lên " + variantId);

                    if (itemVariantId == variantId) {
                        BigDecimal price = variant.getVariant_price();
                        if (price == null) {
                            System.out.println("LỖI: variant_price là NULL cho variantId = " + variantId);
                            throw new IllegalStateException("Giá variant null cho variantId " + variantId);
                        }

                        item.setQuantity(quantity);
                        item.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));

                        found = true;
                        System.out.println("→ CẬP NHẬT THÀNH CÔNG variantId " + variantId
                                + " | new qty = " + quantity
                                + " | new totalPrice = " + item.getTotalPrice());
                        break;
                    }
                }

                if (found) {
                    session.setAttribute("CART", cart);
                    response.setStatus(200);
                    response.getWriter().write("{\"success\":true, \"message\":\"Cập nhật thành công\"}");
                    response.getWriter().flush();
                    System.out.println("Response 200 đã gửi cho client");
                } else {
                    System.out.println("Không tìm thấy variantId " + variantId + " trong giỏ");
                    response.setStatus(400);
                    response.getWriter().write("{\"success\":false, \"message\":\"Không tìm thấy sản phẩm với variantId " + variantId + "\"}");
                    response.getWriter().flush();
                }

            } catch (NumberFormatException e) {
                System.out.println("Lỗi parse tham số: " + e.getMessage());
                response.setStatus(400);
                response.getWriter().write("{\"success\":false, \"message\":\"Dữ liệu không hợp lệ (variantId hoặc quantity sai định dạng)\"}");
                response.getWriter().flush();
            } catch (Exception e) {
                System.out.println("LỖI SERVER TRONG AJAX UPDATE: " + e.getMessage());
                e.printStackTrace();
                response.setStatus(500);
                response.getWriter().write("{\"success\":false, \"message\":\"Lỗi server: " + e.getMessage() + "\"}");
                response.getWriter().flush();
            }
            return;
        }

        // Xử lý action=view
        if ("view".equals(action)) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGGED_USER");

            if (user != null) {
                // Khởi tạo các DAO để lấy dữ liệu từ My Page
                AddressDao addrDao = new AddressDao();
                PaymentDao payDao = new PaymentDao();

                // 1. Lấy danh sách địa chỉ của user
                request.setAttribute("addresses", addrDao.getAddressesByUserId(user.getId()));

                // 2. Lấy danh sách thẻ thanh toán của user
                request.setAttribute("listPayments", payDao.getPaymentsByUserId(user.getId()));

                // 3. (Tùy chọn) Tính tổng tiền giỏ hàng để hiển thị ban đầu
                List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
                BigDecimal subTotal = BigDecimal.ZERO;
                if (cart != null) {
                    for (CartItem item : cart) {
                        subTotal = subTotal.add(item.getVariant().getVariant_price().multiply(BigDecimal.valueOf(item.getQuantity())));
                    }
                }
                request.setAttribute("total", subTotal);
            }

            request.getRequestDispatcher("shopping_cart.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        // Kiểm tra đăng nhập
        if (session.getAttribute("LOGGED_USER") == null) {
            request.setAttribute("ERROR_MESSAGE", "Bạn cần đăng nhập để thực hiện thao tác này!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        ProductDao dao = new ProductDao();

        switch (action) {
            case "add": {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int variantId = Integer.parseInt(request.getParameter("variantId"));
                int quantityToAdd = Integer.parseInt(request.getParameter("quantity"));

                ProductVariants variant = dao.getVariantById(variantId);
                int inventory = variant.getInventory_quantity(); // Lấy tồn kho từ DB

                boolean isExisted = false;
                for (CartItem item : cart) {
                    if (item.getVariant().getId() == variantId) {
                        int newTotalQty = item.getQuantity() + quantityToAdd;

                        // KIỂM TRA TỒN KHO TẠI ĐÂY
                        if (newTotalQty > inventory) {
                            session.setAttribute("ERROR_CART", "Không thể thêm! Tổng số lượng trong giỏ (" + newTotalQty + ") vượt quá tồn kho (" + inventory + ")");
                            response.sendRedirect("detail?id=" + productId);
                            return;
                        }

                        item.setQuantity(newTotalQty);
                        isExisted = true;
                        break;
                    }
                }

                if (!isExisted) {
                    // Kiểm tra cho sản phẩm mới thêm lần đầu
                    if (quantityToAdd > inventory) {
                        session.setAttribute("ERROR_CART", "Số lượng yêu cầu vượt quá tồn kho hiện có!");
                        response.sendRedirect("detail?id=" + productId);
                        return;
                    }

                    Product product = dao.getProductById(productId);
                    CartItem newItem = new CartItem();
                    newItem.setProduct(product);
                    newItem.setVariant(variant);
                    newItem.setQuantity(quantityToAdd);
                    cart.add(newItem);
                }

                session.setAttribute("CART", cart);
                session.setAttribute("ADD_CART_SUCCESS", "Đã thêm sản phẩm vào giỏ hàng!");
                response.sendRedirect("detail?id=" + productId);
                return;
            }

            case "update": {
                int variantId = Integer.parseInt(request.getParameter("variantId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                for (CartItem item : cart) {
                    if (item.getVariant().getId() == variantId) {
                        item.setQuantity(quantity);
                        break;
                    }
                }
                break;
            }

            case "remove": {
                int variantId = Integer.parseInt(request.getParameter("variantId"));
                cart.removeIf(i -> i.getVariant().getId() == variantId);
                break;
            }
        }

        session.setAttribute("CART", cart);

        String productId = request.getParameter("productId");
        if (productId != null) {
            response.sendRedirect("detail?id=" + productId + "&added=1");
        } else {
            response.sendRedirect("CartServlet?action=view");
        }
    }
}