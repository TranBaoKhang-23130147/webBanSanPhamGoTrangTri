package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

import java.io.IOException;

@WebServlet(name = "VerifyOtpServlet", value = "/VerifyOtpServlet")
public class VerifyOtpServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

// Trong VerifyOtpServlet.java, phần doPost
        String action = request.getParameter("action");
        if ("checkOnly".equals(action)) {
            String inputOtp = request.getParameter("otp");
            String sessionOtp = (String) session.getAttribute("OTP");

            response.setContentType("application/json");
            if (sessionOtp != null && sessionOtp.equals(inputOtp)) {
                response.getWriter().write("{\"status\": \"success\"}");
            } else {
                response.getWriter().write("{\"status\": \"error\"}");
            }
            return; // Kết thúc xử lý
        }

        String inputOtp = request.getParameter("otp");

        String sessionOtp = (String) session.getAttribute("OTP");
        Long otpTime = (Long) session.getAttribute("OTP_TIME");
        User regUser = (User) session.getAttribute("REG_USER");

        /* ================== VALIDATE ================== */

        if (sessionOtp == null || otpTime == null || regUser == null) {
            request.setAttribute("ERROR_MESSAGE", "Phiên đăng ký đã hết hạn. Vui lòng đăng ký lại!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // OTP hết hạn (5 phút)
        if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) {
            session.removeAttribute("OTP");
            session.removeAttribute("OTP_TIME");
            session.removeAttribute("REG_USER");

            request.setAttribute("ERROR_MESSAGE", "OTP đã hết hạn. Vui lòng đăng ký lại!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        // OTP sai
        if (!sessionOtp.equals(inputOtp)) {
            request.setAttribute("SHOW_OTP", true);
            request.setAttribute("ERROR", "OTP không đúng!");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        /* ================== OTP ĐÚNG → ĐĂNG KÝ ================== */

        UserDao dao = new UserDao();
        dao.signup(regUser.getUsername(), regUser.getEmail(), regUser.getPassword());

        // Xóa session tạm
        session.removeAttribute("OTP");
        session.removeAttribute("OTP_TIME");
        session.removeAttribute("REG_USER");

        request.setAttribute("MESS_SUCCESS", "Đăng ký thành công! Vui lòng đăng nhập.");
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
