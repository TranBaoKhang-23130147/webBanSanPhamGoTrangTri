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

        if (current == null || next == null || confirm == null || current.isEmpty() || next.isEmpty()) {
            errorMsg = "Vui lòng điền đầy đủ thông tin.";
        } else if (!next.equals(confirm)) {
            errorMsg = "Mật khẩu mới và xác nhận mật khẩu không khớp.";
        } else if (next.length() < 8) {
            errorMsg = "Mật khẩu phải có ít nhất 8 ký tự.";
        }

        if (errorMsg != null) {
            req.setAttribute("errorMessage", errorMsg);
            req.getRequestDispatcher("admin_setting.jsp#password").forward(req, resp);
            return;
        }

        try {
            String dbPassHash = userDao.getPasswordById(userId);

            if (dbPassHash == null || !PasswordUtils.checkPassword(current, dbPassHash)) {
                req.setAttribute("errorMessage", "Mật khẩu hiện tại không chính xác.");
                req.getRequestDispatcher("admin_setting.jsp#password").forward(req, resp);
                return;
            }

            boolean ok = userDao.updatePassword(userId, next);

            if (ok) {
                req.setAttribute("successMessage", "Đổi mật khẩu thành công.");
            } else {
                req.setAttribute("errorMessage", "Lỗi hệ thống. Không thể cập nhật mật khẩu.");
            }

            req.getRequestDispatcher("admin_setting.jsp#password").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect("error.jsp");
        }
    }
}