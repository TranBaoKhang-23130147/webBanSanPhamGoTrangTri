package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;
import utils.PasswordUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

@WebServlet(name = "ChangePasswordServlet", value = "/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // Không cần setContentType JSON nếu bạn muốn chuyển hướng trang truyền thống

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("LOGGED_USER") : null;

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getId();
        String current = req.getParameter("currentPassword");
        String next = req.getParameter("newPassword");
        String confirm = req.getParameter("confirmPassword");

        String errorMsg = null;

        // 1. Kiểm tra tính hợp lệ của dữ liệu đầu vào
        if (current == null || next == null || confirm == null || current.isEmpty() || next.isEmpty()) {
            errorMsg = "Vui lòng điền đầy đủ thông tin.";
        } else if (!next.equals(confirm)) {
            errorMsg = "Mật khẩu mới và xác nhận mật khẩu không khớp.";
        } else if (next.length() < 8) { // Khớp với yêu cầu trong JSP của bạn
            errorMsg = "Mật khẩu phải có ít nhất 8 ký tự.";
        }

        if (errorMsg != null) {
            req.setAttribute("errorMessage", errorMsg);
            req.getRequestDispatcher("admin_setting.jsp#password").forward(req, resp);
            return;
        }

        try {
            // 2. Lấy mật khẩu đã băm từ DB để so sánh
            String dbPassHash = userDao.getPasswordById(userId);

            // Sử dụng PasswordUtils bạn đã cung cấp
            if (dbPassHash == null || !PasswordUtils.checkPassword(current, dbPassHash)) {
                req.setAttribute("errorMessage", "Mật khẩu hiện tại không chính xác.");
                req.getRequestDispatcher("admin_setting.jsp#password").forward(req, resp);
                return;
            }

            // 3. Cập nhật mật khẩu mới (Hàm updatePassword của bạn đã gọi PasswordUtils.hashPassword rồi)
            boolean ok = userDao.updatePassword(userId, next);

            if (ok) {
                // Dùng request attribute để JSP hiển thị thông báo thành công
                req.setAttribute("successMessage", "Đổi mật khẩu thành công.");
            } else {
                req.setAttribute("errorMessage", "Lỗi hệ thống. Không thể cập nhật mật khẩu.");
            }

            // Quay lại trang setting và nhảy đến tab mật khẩu
            req.getRequestDispatcher("admin_setting.jsp#password").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
    }
}