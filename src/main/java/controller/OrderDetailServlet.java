package controller;

import dao.OrderDao;
import dao.OrderDetailDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Order;
import model.OrderDetail;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderDetailServlet", value = "/OrderDetailServlet")
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdRaw = request.getParameter("orderId");

        // Kiểm tra nếu tham số rỗng hoặc null
        if (orderIdRaw == null || orderIdRaw.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã đơn hàng!");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdRaw);
            OrderDao orderDao = new OrderDao();
            OrderDetailDao detailDao = new OrderDetailDao();

            Order order = orderDao.getOrderById(orderId);
            List<OrderDetail> details = detailDao.getByOrderId(orderId);

            request.setAttribute("order", order);
            request.setAttribute("details", details);
            request.getRequestDispatcher("ajax_order_detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đơn hàng không hợp lệ!");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}