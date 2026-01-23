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

    // doGet giữ nguyên như của bạn để hiển thị danh sách
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDao orderDao = new OrderDao();
        List<Order> listAllOrders = orderDao.getAllOrders();

        // 2. Lặp qua từng đơn hàng để lấy chi tiết sản phẩm
        for (Order o : listAllOrders) {
            // Gọi phương thức bạn vừa viết để lấy List<OrderDetail>
            // Đảm bảo class Order đã có phương thức setDetails(List<OrderDetail> details)
            o.setDetails(orderDao.getDetailsByOrderId(o.getId()));
        }

        // 3. Đẩy dữ liệu sang JSP
        request.setAttribute("listOrders", listAllOrders);
        request.getRequestDispatcher("admin_order.jsp").forward(request, response);
    }

    // THÊM doPost để xử lý cập nhật trạng thái
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Lấy dữ liệu từ Form
        String orderIdStr = request.getParameter("orderId");
        String status = request.getParameter("status");
        String paymentStatus = request.getParameter("paymentStatus"); // Nhận thêm tham số này

        if (orderIdStr != null && status != null && paymentStatus != null) {
            int orderId = Integer.parseInt(orderIdStr);
            OrderDao dao = new OrderDao();

            // Cập nhật cả 2 trạng thái vào DB
            boolean success = dao.updateOrderStatus(orderId, status, paymentStatus);

            if (success) {
                response.sendRedirect("admin-orders?msg=success");
            } else {
                response.sendRedirect("admin-orders?msg=error");
            }
        }
    }
}