package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

@WebServlet(name = "VerifyOtpResetServlet", value = "/VerifyOtpResetServlet")
public class VerifyOtpResetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String inputOtp = request.getParameter("otp");
        String sessionOtp = (String) session.getAttribute("OTP");
        Long otpTime = (Long) session.getAttribute("OTP_TIME");

        if (sessionOtp == null || otpTime == null) {
            request.setAttribute("ERROR_MESSAGE", "OTP hết hạn!");
            request.getRequestDispatcher("/login_forgot_password.jsp").forward(request, response);
            return;
        }

        if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) {
            request.setAttribute("ERROR_MESSAGE", "OTP đã hết hạn!");
            request.getRequestDispatcher("/login_forgot_password.jsp").forward(request, response);
            return;
        }

        if (!sessionOtp.equals(inputOtp)) {
            request.setAttribute("ERROR_MESSAGE", "OTP không đúng!");
            request.getRequestDispatcher("/login_otp_pass.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("/login_pass_new.jsp").forward(request, response);
    }
}