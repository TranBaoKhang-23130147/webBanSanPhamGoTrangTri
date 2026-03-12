package controller;

import dao.OrderDao;
import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Order;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/AdminOrderHistoryController")
public class AdminOrderHistoryController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null) {
            response.sendRedirect("customers");
            return;
        }
        int userId = Integer.parseInt(userIdStr);
        OrderDao orderDao = new OrderDao();
        List<Order> listOrders = orderDao.getOrdersByUserId(userId);
        for (Order o : listOrders) {
            o.setDetails(orderDao.getDetailsByOrderId(o.getId()));
        }
        UserDao userDao = new UserDao();
        User customer = userDao.getById(userId);
        request.setAttribute("listOrders", listOrders);
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("admin_order_history.jsp").forward(request, response);
    }
}