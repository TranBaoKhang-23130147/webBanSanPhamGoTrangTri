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
            tab = "ho-so"; // Mặc định là profile (ho-so)
        }

        request.setAttribute("activeTab", tab); // Gửi tab đang active sang JSP

        // ===== LOAD DỮ LIỆU THEO TAB =====
        if ("thanh-toan".equals(tab)) {
            // Nếu tab là "Thanh toán", tải danh sách payments
            PaymentDao paymentDao = new PaymentDao();
            List<Payment> listPayments = paymentDao.getPaymentsByUserId(user.getId());
            request.setAttribute("listPayments", listPayments);
        } else if ("don-hang".equals(tab)) {
        } else if ("dia-chi".equals(tab)) {
            AddressDao dao = new AddressDao();
            List<Address> addresses = dao.getByUserId(user.getId());
            request.setAttribute("addresses", addresses);
        }

        try {
            request.getRequestDispatcher("/mypage_user.jsp").forward(request, response);
        } catch (Exception e) {

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
}