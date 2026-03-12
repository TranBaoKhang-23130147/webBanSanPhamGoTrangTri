package controller;

import dao.UserDao;
import model.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/AdminAddCustomerServlet")
public class AdminAddCustomerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        UserDao dao = new UserDao();

        String action = request.getParameter("action");

        if ("checkEmail".equals(action)) {
            String email = request.getParameter("email");
            boolean exists = dao.checkEmailExist(email);
            out.print("{\"exists\":" + exists + "}");
            return;
        }
        if ("verifyOnly".equals(action)) {
            String otp = request.getParameter("otp");
            String sessionOtp = (String) session.getAttribute("OTP");

            if (sessionOtp != null && sessionOtp.equals(otp)) {
                out.print("{\"status\":\"success\"}");
            } else {
                out.print("{\"status\":\"error\"}");
            }
            return;
        }
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String otp = request.getParameter("otp");

        String sessionOtp = (String) session.getAttribute("OTP");
        Long otpTime = (Long) session.getAttribute("OTP_TIME");

        try {
            String passwordPattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";
            if (!password.matches(passwordPattern)) {
                out.print("{\"status\":\"error\",\"message\":\"Mật khẩu phải có ít nhất 8 ký tự, có chữ hoa và ký tự đặc biệt\"}");
                return;
            }
            if (sessionOtp == null || otpTime == null) {
                out.print("{\"status\":\"error\",\"message\":\"Phiên OTP không tồn tại\"}");
                return;
            }
            if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) {
                session.removeAttribute("OTP");
                session.removeAttribute("OTP_TIME");
                out.print("{\"status\":\"error\",\"message\":\"OTP đã hết hạn\"}");
                return;
            }
            if (!sessionOtp.equals(otp)) {
                out.print("{\"status\":\"error\",\"message\":\"OTP không đúng\"}");
                return;
            }
            if (dao.checkEmailExist(email)) {
                out.print("{\"status\":\"error\",\"message\":\"Email đã tồn tại\"}");
                return;
            }
            boolean check = dao.adminInsertUser(username, email, phone, password, role);
            if (check) {
                session.removeAttribute("OTP");
                session.removeAttribute("OTP_TIME");
                out.print("{\"status\":\"success\",\"message\":\"Thêm người dùng thành công\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Không thể lưu dữ liệu\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"Lỗi\"}");
        }

        out.flush();
    }
}