package controller;

import dao.OrderDao;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Order;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin-orders")
public class AdminOrderServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrderDao orderDao = new OrderDao();
        List<Order> listOrders = orderDao.getAllOrders();

        for (Order o : listOrders) {
            o.setDetails(orderDao.getDetailsByOrderId(o.getId()));
        }
        request.setAttribute("listOrders", listOrders);
        request.getRequestDispatcher("admin_order.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null) {
            response.sendRedirect("admin-orders");
            return;
        }
        int orderId = Integer.parseInt(orderIdStr);
        String status = request.getParameter("status");
        String paymentStatus = request.getParameter("paymentStatus");

        OrderDao dao = new OrderDao();
        dao.updateOrderStatus(orderId, status, paymentStatus);

        response.sendRedirect("admin-orders");
    }
}