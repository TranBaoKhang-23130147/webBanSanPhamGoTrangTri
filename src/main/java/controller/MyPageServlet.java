package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Payment;
import model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "MyPageServlet", value = "/MyPageServlet")
public class MyPageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 1. Lấy danh sách THANH TOÁN
        dao.PaymentDao pDao = new dao.PaymentDao();
        List<Payment> listP = pDao.getPaymentsByUserId(user.getId());
        request.setAttribute("listPayments", listP);

        // 2. Lấy danh sách ĐƠN HÀNG (Copy logic từ UpdateProfileController sang)
        dao.OrderDao orderDao = new dao.OrderDao();
        List<model.Order> listOrders = orderDao.getOrdersByUserId(user.getId());

        double totalSpent = 0;
        for (model.Order o : listOrders) {
            totalSpent += o.getTotalOrder();
        }

        request.setAttribute("listO", listOrders); // Đặt tên listO cho khớp với JSP
        request.setAttribute("countOrder", listOrders.size());
        request.setAttribute("totalSpent", totalSpent);

        // 3. Chuyển hướng
        request.getRequestDispatcher("/mypage_user.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}