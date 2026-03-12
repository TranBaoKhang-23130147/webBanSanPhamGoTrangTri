package controller;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.CartItem;
import model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {

    OrderDao orderDao = new OrderDao();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<CartItem> fullCart = (List<CartItem>) session.getAttribute("CART");
        String[] selectedIds = request.getParameterValues("selectedItems");

        if (fullCart == null || selectedIds == null) {
            response.sendRedirect("CartServlet?action=view");
            return;
        }

        List<CartItem> cart = new ArrayList<>();

        for (int i = 0; i < selectedIds.length; i++) {
            int id = Integer.parseInt(selectedIds[i]);

            for (CartItem item : fullCart) {
                if (item.getVariant().getId() == id) {
                    cart.add(item);
                }
            }
        }

        if (cart.size() == 0) {
            response.sendRedirect("CartServlet?action=view");
            return;
        }

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String addressIdStr = request.getParameter("address_id");
        String note = request.getParameter("note");
        String paymentMethod = request.getParameter("paymentMethod");

        if (paymentMethod == null) {
            paymentMethod = "COD";
        }

        int addressId = Integer.parseInt(addressIdStr);

        try {

            int orderId = orderDao.checkout(
                    user.getId(),
                    fullName,
                    phone,
                    addressId,
                    note,
                    paymentMethod,
                    cart
            );

            for (CartItem bought : cart) {
                for (int i = 0; i < fullCart.size(); i++) {
                    if (fullCart.get(i).getVariant().getId() == bought.getVariant().getId()) {
                        fullCart.remove(i);
                        break;
                    }
                }
            }

            session.setAttribute("CART", fullCart);

            response.sendRedirect("MyPageServlet?tab=don-hang&success=1&orderId=" + orderId);

        } catch (Exception e) {

            response.sendRedirect("CartServlet?action=view&error=1");
        }
    }
}