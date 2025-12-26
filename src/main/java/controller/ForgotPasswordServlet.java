package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import utils.EmailUtil;

import java.io.IOException;

@WebServlet(name = "ForgotPasswordServlet", value = "/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        UserDao dao = new UserDao();

        if (!dao.checkEmailExist(email)) {
            request.setAttribute("ERROR_MESSAGE", "Email không tồn tại!");
            request.getRequestDispatcher("/login_forgot_password.jsp").forward(request, response);
            return;
        }

        // Tạo OTP
        String otp = String.valueOf((int)(Math.random() * 900000 + 100000));

        try {
            EmailUtil.sendOTP(email, otp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        HttpSession session = request.getSession();
        session.setAttribute("RESET_EMAIL", email);
        session.setAttribute("OTP", otp);
        session.setAttribute("OTP_TIME", System.currentTimeMillis());

        request.getRequestDispatcher("/login_otp_pass.jsp").forward(request, response);
    }
}