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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String duration = request.getParameter("duration");
        User user = (User) request.getSession().getAttribute("LOGGED_USER");

        if (user != null) {
            new dao.PaymentDao().addPayment(user.getId(), type, duration);
        }
        // Sau khi thêm xong quay về trang cá nhân
        response.sendRedirect("MyPageServlet");
    }
}