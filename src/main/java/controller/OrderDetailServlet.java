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

        // Lấy tham số từ URL hoặc AJAX request
        String orderIdRaw = request.getParameter("orderId");

        // 1. Kiểm tra an toàn: Tránh lỗi NumberFormatException
        if (orderIdRaw == null || orderIdRaw.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Lỗi: Không tìm thấy mã đơn hàng!");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdRaw.trim());

            OrderDao orderDao = new OrderDao();
            OrderDetailDao detailDao = new OrderDetailDao();

            // 2. Lấy thông tin đơn hàng và danh sách sản phẩm chi tiết
            Order order = orderDao.getOrderById(orderId);
            List<OrderDetail> details = detailDao.getByOrderId(orderId);

            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Đơn hàng không tồn tại!");
                return;
            }

            // 3. Đẩy dữ liệu sang trang hiển thị (thường dùng AJAX để trả về một đoạn HTML)
            request.setAttribute("order", order);
            request.setAttribute("details", details);

            // Trang này sẽ chỉ chứa nội dung Modal chi tiết
            request.getRequestDispatcher("ajax_order_detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đơn hàng phải là con số!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}