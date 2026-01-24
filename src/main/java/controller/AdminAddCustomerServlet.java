package controller;

import dao.UserDao;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "AdminAddCustomerServlet", value = "/AdminAddCustomerServlet")
public class AdminAddCustomerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // PHẢI ĐẶT TRƯỚC out = response.getWriter()
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json"); // Luôn trả về JSON

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
// Thêm vào đầu phương thức doPost hoặc doGet
        if ("checkEmail".equals(action)) {
            String email = request.getParameter("email");
            UserDao dao = new UserDao();
            boolean exists = dao.checkEmailExist(email);
            out.print("{\"exists\": " + exists + "}");
            out.flush();
            return;
        }
        // 1. Xử lý logic Xác nhận mã (verifyOnly)
        if ("verifyOnly".equals(action)) {
            String inputOtp = request.getParameter("otp");
            String sessionOtp = (String) session.getAttribute("OTP");

            if (sessionOtp != null && sessionOtp.equals(inputOtp)) {
                out.print("{\"status\": \"success\"}");
            } else {
                out.print("{\"status\": \"error\"}");
            }
            out.flush();
            return;
        }

        // 2. Logic Lưu người dùng
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String inputOtp = request.getParameter("otp");

        String sessionOtp = (String) session.getAttribute("OTP");
        Long otpTime = (Long) session.getAttribute("OTP_TIME");

        try {
            String passwordPattern = "^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$";

            if (!password.matches(passwordPattern)) {
                out.print("{\"status\": \"error\", \"message\": \"Mật khẩu phải có ít nhất 8 ký tự, bao gồm 1 chữ hoa và 1 ký tự đặc biệt.\"}");
                return;
            }
            // Kiểm tra dữ liệu đầu vào cơ bản
            if (username == null || email == null || password == null) {
                out.print("{\"status\": \"error\", \"message\": \"Vui lòng điền đầy đủ thông tin bắt buộc.\"}");
                return;
            }

            // --- KIỂM TRA OTP ---
            if (sessionOtp == null || otpTime == null) {
                out.print("{\"status\": \"error\", \"message\": \"Phiên xác thực đã hết hạn.\"}");
                return;
            }

            if (System.currentTimeMillis() - otpTime > 5 * 60 * 1000) {
                session.removeAttribute("OTP");
                out.print("{\"status\": \"error\", \"message\": \"Mã OTP đã hết hạn.\"}");
                return;
            }

            if (!sessionOtp.equals(inputOtp)) {
                out.print("{\"status\": \"error\", \"message\": \"Mã OTP không chính xác.\"}");
                return;
            }

            // --- LƯU NGƯỜI DÙNG ---
            UserDao dao = new UserDao();
            if (dao.checkEmailExist(email)) {
                out.print("{\"status\": \"error\", \"message\": \"Email này đã tồn tại trong hệ thống.\"}");
                return;
            }

            // Thực hiện chèn vào DB
            boolean success = dao.adminInsertUser(username, email, phone, password, role);

            if (success) {
                session.removeAttribute("OTP");
                session.removeAttribute("OTP_TIME");
                out.print("{\"status\": \"success\", \"message\": \"Thêm người dùng thành công!\"}");
            } else {
                out.print("{\"status\": \"error\", \"message\": \"Không thể lưu vào cơ sở dữ liệu.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"status\": \"error\", \"message\": \"Lỗi server: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}