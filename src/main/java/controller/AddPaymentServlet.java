package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "AddPaymentServlet", value = "/AddPaymentServlet")
public class AddPaymentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cardNumber = request.getParameter("cardNumber"); // Lấy số thẻ
        String type = request.getParameter("type");
        String duration = request.getParameter("duration");
        User user = (User) request.getSession().getAttribute("LOGGED_USER");
        String tab = request.getParameter("tab");
        if (tab == null) tab = "ho-so";
        request.setAttribute("activeTab", tab);

        if (user != null) {
            new dao.PaymentDao().addPayment(user.getId(), cardNumber, type, duration);
        }
        response.sendRedirect("MyPageServlet?tab=thanh-toan");
    }
}