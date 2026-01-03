package controller;

import dao.ProductDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.CartItem;
import model.Product;
import model.ProductVariants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        List<CartItem> cart =
                (List<CartItem>) session.getAttribute("CART");

        if (cart == null) cart = new ArrayList<>();

        ProductDao dao = new ProductDao();

        switch (action) {

            case "add": {
                int productId = Integer.parseInt(request.getParameter("productId"));
                int variantId = Integer.parseInt(request.getParameter("variantId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));

                // nếu đã có variant → cộng số lượng
                for (CartItem item : cart) {
                    if (item.getVariant().getId() == variantId) {
                        item.setQuantity(item.getQuantity() + quantity);
                        session.setAttribute("CART", cart);
                        response.sendRedirect("shopping_cart.jsp");
                        return;
                    }
                }

                Product product = dao.getProductById(productId);
                ProductVariants variant = dao.getVariantById(variantId);

                CartItem item = new CartItem();
                item.setProduct(product);
                item.setVariant(variant);
                item.setQuantity(quantity);

                cart.add(item);
                break;
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
        response.sendRedirect("shopping_cart.jsp");
    }
}