package controller;

import dao.AddressDao;
import dao.PaymentDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.*;
import dao.OrderDao;

import java.io.IOException;
import java.util.List;
@WebServlet(name = "MyPageServlet", value = "/MyPageServlet")
public class MyPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("LOGGED_USER");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String tab = request.getParameter("tab");
        if (tab == null || tab.isEmpty()) {
            tab = "ho-so";
        }

        request.setAttribute("activeTab", tab);

        // ===== LOAD DỮ LIỆU THEO TAB =====
        if ("thanh-toan".equals(tab)) {
            PaymentDao paymentDao = new PaymentDao();
            List<Payment> listPayments = paymentDao.getPaymentsByUserId(user.getId());
            request.setAttribute("listPayments", listPayments);

         } else if ("don-hang".equals(tab)) {
        OrderDao orderDao = new OrderDao();
        // 1. Lấy tất cả đơn hàng của User để hiển thị và lọc
        List<Order> allOrders = orderDao.getOrdersByUserId(user.getId());

        // 2. Lấy tham số lọc từ URL (Tất cả, Chờ xác nhận, v.v.)
        String statusFilter = request.getParameter("status");
        List<Order> displayList = new java.util.ArrayList<>();

        // 3. Khai báo biến thống kê
        int countQualifiedOrders = 0; // Đếm số đơn thỏa mãn (Đã giao + Đã thanh toán)
        double totalSpent = 0;        // Tổng tiền của các đơn thỏa mãn

        for (Order o : allOrders) {
            // Nạp chi tiết sản phẩm cho từng đơn
            o.setDetails(orderDao.getDetailsByOrderId(o.getId()));

            // --- LOGIC THỐNG KÊ MỚI ---
            // Chỉ tính vào "Tổng đơn hàng" và "Tổng tích lũy" nếu:
            // Trạng thái đơn = "Đã giao" VÀ Trạng thái thanh toán = "Đã thanh toán"
            if ("Đã giao".equals(o.getStatus()) && "Đã thanh toán".equals(o.getPaymentStatus())) {
                countQualifiedOrders++;
                totalSpent += o.getTotalOrder();
            }

            // Logic để lọc danh sách hiển thị phía dưới
            if (statusFilter == null || statusFilter.isEmpty() || o.getStatus().equals(statusFilter)) {
                displayList.add(o);
            }
        }

        // 4. Đẩy dữ liệu ra JSP
        request.setAttribute("listO", displayList);           // Danh sách hiển thị theo filter
        request.setAttribute("countOrder", countQualifiedOrders); // Số lượng đơn thỏa mãn 2 điều kiện
        request.setAttribute("totalSpent", totalSpent);       // Tổng tiền thỏa mãn 2 điều kiện
        request.setAttribute("allOrders", allOrders);         // Tất cả đơn hàng (cho tab đánh giá)
    }
    else if ("dia-chi".equals(tab)) {
            AddressDao dao = new AddressDao();
            List<Address> addresses = dao.getAddressesByUserId(user.getId()); // Dùng hàm mới đã chỉnh sửa
            request.setAttribute("addresses", addresses);
        }

        request.getRequestDispatcher("/mypage_user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String orderIdStr = request.getParameter("orderId");
        OrderDao orderDao = new OrderDao();

        if (orderIdStr != null) {
            try {
                int orderId = Integer.parseInt(orderIdStr);

                if ("cancelOrder".equals(action)) {
                    // Hủy đơn và hoàn trả kho
                    orderDao.cancelOrReturnOrder(orderId, "Đã hủy");
                } else if ("returnOrder".equals(action)) {
                    // Yêu cầu hoàn hàng và hoàn trả kho (hoặc chờ xác nhận tùy bạn)
                    orderDao.cancelOrReturnOrder(orderId, "Hoàn hàng");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Quay lại tab đơn hàng
        response.sendRedirect("MyPageServlet?tab=don-hang");
    }
}