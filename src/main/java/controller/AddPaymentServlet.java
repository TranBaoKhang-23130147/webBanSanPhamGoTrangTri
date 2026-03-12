package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import dao.PaymentDao;

import java.io.IOException;

@WebServlet("/AddPaymentServlet")
public class AddPaymentServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String cardNumber = request.getParameter("cardNumber");
        String type = request.getParameter("type");
        String duration = request.getParameter("duration");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("LOGGED_USER");
        if (user != null) {
            PaymentDao dao = new PaymentDao();
            dao.addPayment(user.getId(), cardNumber, type, duration);
        }
        response.sendRedirect("MyPageServlet?tab=thanh-toan");
    }
}