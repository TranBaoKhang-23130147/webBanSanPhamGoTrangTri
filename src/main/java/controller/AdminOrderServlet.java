package controller;

import dao.OrderDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Order;

import java.io.IOException;
import java.util.List;
@WebServlet("/admin-orders")
public class AdminOrderServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao orderDao = new OrderDao();
        List<Order> listAllOrders = orderDao.getAllOrders();

        for (Order o : listAllOrders) {
            o.setDetails(orderDao.getDetailsByOrderId(o.getId()));
        }

        request.setAttribute("listOrders", listAllOrders);
        request.getRequestDispatcher("admin_order.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderIdStr = request.getParameter("orderId");
        String status = request.getParameter("status");
        String paymentStatus = request.getParameter("paymentStatus"); 

        if (orderIdStr != null && status != null && paymentStatus != null) {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDao dao = new OrderDao();

            boolean success = dao.updateOrderStatus(orderId, status, paymentStatus);

            if (success) {
                response.sendRedirect("admin-orders?msg=success");
            } else {
                response.sendRedirect("admin-orders?msg=error");
            }
        }
    }
}