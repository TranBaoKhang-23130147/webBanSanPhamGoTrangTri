package controller;

import dao.ProductDao;
import dao.AddressDao;
import dao.PaymentDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("updateQtyAjax".equals(action)) {

            HttpSession session = request.getSession(false);
            List<CartItem> cart = (session != null) ? (List<CartItem>) session.getAttribute("CART") : null;

            response.setContentType("application/json;charset=UTF-8");

            if (cart == null) {
                response.getWriter().print("{\"success\":false}");
                return;
            }

            try {
                int variantId = Integer.parseInt(request.getParameter("variantId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                ProductDao dao = new ProductDao();
                ProductVariants variant = dao.getVariantById(variantId);

                if (variant == null || quantity > variant.getInventory_quantity()) {
                    response.getWriter().print("{\"success\":false}");
                    return;
                }

                for (CartItem item : cart) {
                    if (item.getVariant().getId() == variantId) {
                        item.setQuantity(quantity);

                        BigDecimal price = variant.getVariant_price();
                        item.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
                        break;
                    }
                }

                session.setAttribute("CART", cart);
                response.getWriter().print("{\"success\":true}");

            } catch (Exception e) {
                response.getWriter().print("{\"success\":false}");
            }

            return;
        }

        if ("view".equals(action)) {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("LOGGED_USER");

            if (user != null) {
                AddressDao addrDao = new AddressDao();
                PaymentDao payDao = new PaymentDao();

                request.setAttribute("addresses", addrDao.getAddressesByUserId(user.getId()));
                request.setAttribute("listPayments", payDao.getPaymentsByUserId(user.getId()));
            }

            List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
            BigDecimal total = BigDecimal.ZERO;

            if (cart != null) {
                for (CartItem item : cart) {
                    total = total.add(
                            item.getVariant().getVariant_price()
                                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                }
            }

            request.setAttribute("total", total);
            request.getRequestDispatcher("shopping_cart.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if (session.getAttribute("LOGGED_USER") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("CART");
        if (cart == null) cart = new ArrayList<>();

        ProductDao dao = new ProductDao();

        if ("add".equals(action)) {

            int productId = Integer.parseInt(request.getParameter("productId"));
            int variantId = Integer.parseInt(request.getParameter("variantId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            ProductVariants variant = dao.getVariantById(variantId);
            Product product = dao.getProductById(productId);

            boolean found = false;

            for (CartItem item : cart) {
                if (item.getVariant().getId() == variantId) {
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }

            if (!found) {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setVariant(variant);
                item.setQuantity(quantity);
                cart.add(item);
            }

        } else if ("update".equals(action)) {

            int variantId = Integer.parseInt(request.getParameter("variantId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            for (CartItem item : cart) {
                if (item.getVariant().getId() == variantId) {
                    item.setQuantity(quantity);
                    break;
                }
            }

        } else if ("remove".equals(action)) {

            int variantId = Integer.parseInt(request.getParameter("variantId"));
            cart.removeIf(i -> i.getVariant().getId() == variantId);
        }

        session.setAttribute("CART", cart);
        response.sendRedirect("CartServlet?action=view");
    }
}