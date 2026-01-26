package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "AdminOrderHistoryController", value = "/AdminOrderHistoryController")
public class AdminOrderHistoryController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userIdStr = request.getParameter("userId");
        if (userIdStr != null) {
            int userId = Integer.parseInt(userIdStr);

            dao.OrderDao orderDao = new dao.OrderDao();
            // 1. Lấy danh sách đơn hàng cơ bản
            java.util.List<model.Order> listOrders = orderDao.getOrdersByUserId(userId);

            // 2. QUAN TRỌNG: Phải lấy chi tiết cho từng đơn hàng
            for (model.Order o : listOrders) {
                o.setDetails(orderDao.getDetailsByOrderId(o.getId()));
            }

            dao.UserDao userDao = new dao.UserDao();
            model.User customer = userDao.getById(userId);

            request.setAttribute("listOrders", listOrders);
            request.setAttribute("customer", customer);

            request.getRequestDispatcher("admin_order_history.jsp").forward(request, response);
        } else {
            response.sendRedirect("customers");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}