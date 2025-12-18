package controller;

import dao.UserDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.User;

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
        resp.setContentType("application/json;charset=UTF-8");

        // Debug: log method, content-type and all parameters
        getServletContext().log("ChangePasswordServlet: method=" + req.getMethod() + ", contentType=" + req.getContentType());
        Map<String, String[]> pmap = req.getParameterMap();
        if (pmap.isEmpty()) {
            getServletContext().log("ChangePasswordServlet: parameter map is empty");
        } else {
            for (Map.Entry<String, String[]> e : pmap.entrySet()) {
                getServletContext().log("param: " + e.getKey() + " = " + Arrays.toString(e.getValue()));
            }
        }

        HttpSession session = req.getSession(false);
        Integer userId = null;
        if (session != null) {
            Object u = session.getAttribute("LOGGED_USER");
            if (u instanceof User) {
                userId = ((User) u).getId();
            } else {
                Object uid = session.getAttribute("userId");
                if (uid instanceof Integer) userId = (Integer) uid;
                else if (uid instanceof String) userId = Integer.valueOf((String) uid);
            }
        }

        try (PrintWriter out = resp.getWriter()) {
            if (userId == null) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"success\":false,\"message\":\"Not logged in\"}");
                return;
            }

            String current = req.getParameter("currentPassword");
            String next = req.getParameter("newPassword");
            String confirm = req.getParameter("confirmPassword");

            // keep logging the specific parameters for clarity
            getServletContext().log("ChangePasswordServlet: current=" + current + ", new=" + next + ", confirm=" + confirm);

            if (current == null || next == null || confirm == null) {
                out.print("{\"success\":false,\"message\":\"Missing parameters\"}");
                return;
            }
            if (!next.equals(confirm)) {
                out.print("{\"success\":false,\"message\":\"New password and confirm do not match\"}");
                return;
            }
            if (next.length() < 6) {
                out.print("{\"success\":false,\"message\":\"Password too short\"}");
                return;
            }

            String dbPass = userDao.getPasswordById(userId);
            if (dbPass == null || !dbPass.equals(current)) {
                out.print("{\"success\":false,\"message\":\"Current password is incorrect\"}");
                return;
            }

            boolean ok = userDao.updatePassword(userId, next);
            if (ok) {
                req.setAttribute("successMessage", "Đổi mật khẩu thành công.");
                req.getRequestDispatcher("/admin_setting.jsp").forward(req, resp); // Điều hướng lại admin_setting.jsp
            } else {
                req.setAttribute("errorMessage", "Cập nhật thất bại. Vui lòng thử lại.");
                req.getRequestDispatcher("/admin_setting.jsp").forward(req, resp); // Điều hướng lại admin_setting.jsp
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }}