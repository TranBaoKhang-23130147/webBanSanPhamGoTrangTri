package controller;

import dao.AddressDao;
import dao.PaymentDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Address;
import model.Order;
import model.Payment;
import model.User;
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
            // --- SỬA TẠI ĐÂY ---
            OrderDao orderDao = new OrderDao();
            // 1. Lấy danh sách đơn hàng (bao gồm danh sách chi tiết bên trong mỗi đơn)
            List<Order> listO = orderDao.getOrdersByUserId(user.getId());

            // 2. Tính toán thống kê cho Dashboard đơn hàng
            int countOrder = listO.size();
            double totalSpent = 0;
            for(Order o : listO) {
                // Giả sử bạn có thuộc tính tổng tiền trong model Order hoặc tính từ chi tiết
                totalSpent += o.getTotalAmount();
            }

            request.setAttribute("listO", listO);
            request.setAttribute("countOrder", countOrder);
            request.setAttribute("totalSpent", totalSpent);

        } else if ("dia-chi".equals(tab)) {
            AddressDao dao = new AddressDao();
            List<Address> addresses = dao.getAddressesByUserId(user.getId()); // Dùng hàm mới đã chỉnh sửa
            request.setAttribute("addresses", addresses);
        }

        request.getRequestDispatcher("/mypage_user.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}